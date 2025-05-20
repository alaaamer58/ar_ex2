import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Kefel {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        int k = Integer.parseInt(args[0]);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("kefel.s"));

            writer.println(".section .text");
            writer.println(".globl kefel");
            writer.println("kefel:");

            // נבדוק אם k הוא חזקת 2
            if ((k & (k - 1)) == 0) {
                int shift = Integer.numberOfTrailingZeros(k);
                writer.println("    movq %rdi, %rax");
                writer.printf("    shlq $%d, %%rax\n", shift);
            } else {
                boolean optimized = false;

                // חיבור של שתי חזקות 2
                for (int i = 63; i >= 0 && !optimized; i--) {
                    for (int j = i - 1; j >= 0; j--) {
                        if ((1L << i) + (1L << j) == k) {
                            writer.println("    movq %rdi, %rax");
                            writer.printf("    shlq $%d, %%rax\n", i);
                            writer.println("    movq %rdi, %rcx");
                            writer.printf("    shlq $%d, %%rcx\n", j);
                            writer.println("    addq %rcx, %rax");
                            optimized = true;
                        }
                    }
                }

                // חיסור של שתי חזקות 2
                if (!optimized) {
                    for (int i = 63; i >= 0 && !optimized; i--) {
                        for (int j = i - 1; j >= 0; j--) {
                            if ((1L << i) - (1L << j) == k) {
                                writer.println("    movq %rdi, %rax");
                                writer.printf("    shlq $%d, %%rax\n", i);
                                writer.println("    movq %rdi, %rcx");
                                writer.printf("    shlq $%d, %%rcx\n", j);
                                writer.println("    subq %rcx, %rax");
                                optimized = true;
                            }
                        }
                    }
                }

                // פירוק כללי לפי ביטים דלוקים
                if (!optimized) {
                    boolean first = true;
                    for (int i = 63; i >= 0; i--) {
                        if (((k >> i) & 1) != 0) {
                            if (first) {
                                writer.println("    movq %rdi, %rax");
                                if (i != 0)
                                    writer.printf("    shlq $%d, %%rax\n", i);
                                first = false;
                            } else {
                                writer.println("    movq %rdi, %rcx");
                                writer.printf("    shlq $%d, %%rcx\n", i);
                                writer.println("    addq %rcx, %rax");
                            }
                        }
                    }
                }
            }

            writer.println("    ret");
            writer.close();
        } catch (IOException e) {
            // אין הדפסות שגיאה לפי דרישות המטלה
        }
    }
}
