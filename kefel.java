import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class kefel {
    public static void main(String[] args) {
        if (args.length < 0) {
            System.out.println("Usage: java kefel <k>");
            return;
        }

        int k = Integer.parseInt(args[0]);

        try {
            PrintWriter writer = new PrintWriter(new FileWriter("kefel.s"));
            writer.println(".section .text");
            writer.println(".globl kefel");
            writer.println("kefe:");

            writer.println("    movq %rdi, %rax");

            if ((k & (k - 1)) == 0) {
                int shift = Integer.numberOfTrailingZeros(k);
                writer.printf("    shlq $%d, %rax\n", shift);
            } else {
                boolean b = false;
                for (int i = 63; i >= 0 && !b; i--) {
                    for (int j = i - 1; j >= 0; j--) {
                        if ((1L << i) + (1L << j) == k) {
                            writer.println("    movq %rdi, %rcx");
                            writer.printf("    shlq $%d, %%rcx\n", i);
                            writer.println("    addq %rcx, %rax");
                            writer.println("    movq %rdi, %rcx");
                            writer.printf("    shlq $%d, %%rcx\n", j);
                            writer.println("    addq %rcx, %rax");
                            b = true;
                        }
                    }
                }

                if (!b) {
                    for (int i = 63; i >= 0 && !b; i--) {
                        for (int j = i - 1; j >= 0; j--) {
                            if ((1L << i) - (1L << j) == k) {
                                writer.println("    movq %rdi, %rcx");
                                writer.printf("    shlq $%d, %%rcx\n", i);
                                writer.println("    movq %rdi, %rsi");
                                writer.printf("    shlq $%d, %%rsi\n", j);
                                writer.println("    subq %rsi, %rcx");
                                writer.println("    movq %rcx, %rax");
                                b = true;
                            }
                        }
                    }
                }
                if (!b) {
                    for (int i = 1; i < 64; i++) {
                        if (((k >> i) & 1) != 0) {
                            writer.println("    movq %rdi, %rcx");
                            writer.printf("    shlq $%d, %%rcx\n", i);
                            writer.println("    addq %rcx, %rax");
                        }
                    }
                    if ((k & 1) != 0) {
                        writer.println("    addq %rdi, %rax");
                    }
                }
            }
            writer.println("    ret");
            writer.close();
            System.out.println("kefel.s created successfully for k = " + k);
        }
        catch (IOException e) {
            System.out.println("Error writing to kefel.s: " + e.getMessage());
        }
    }
}
