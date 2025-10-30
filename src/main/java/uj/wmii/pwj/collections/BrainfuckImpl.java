package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.PrintStream;

public class BrainfuckImpl implements Brainfuck {

    private final String program;
    private final PrintStream out;
    private final InputStream in;
    private final int stackSize;

    private BrainfuckImpl(String program, PrintStream out, InputStream in, int stackSize) {
        this.program = program;
        this.out = out;
        this.in = in;
        this.stackSize = stackSize;
    }

    @Override
    public void execute() {
        byte[] memory = new byte[stackSize];
        int pointer = 0;
        int programCounter = 0;

        while (programCounter < program.length()) {
            char command = program.charAt(programCounter);

            switch (command) {
                case '>':
                    pointer++;
                    if (pointer >= stackSize) {
                        pointer = 0;
                    }
                    break;

                case '<':
                    pointer--;
                    if (pointer < 0) {
                        pointer = stackSize - 1;
                    }
                    break;

                case '+':
                    memory[pointer]++;
                    break;

                case '-':
                    memory[pointer]--;
                    break;

                case '.':
                    out.print((char) memory[pointer]);
                    break;

                case ',':
                    try {
                        int readByte = in.read();
                        memory[pointer] = (byte) (readByte == -1 ? 0 : readByte);
                    } catch (Exception e) {
                        memory[pointer] = 0;
                    }
                    break;

                case '[':
                    if (memory[pointer] == 0) {
                        int depth = 1;
                        while (depth > 0 && programCounter < program.length() - 1) {
                            programCounter++;
                            char nextChar = program.charAt(programCounter);
                            if (nextChar == '[') {
                                depth++;
                            } else if (nextChar == ']') {
                                depth--;
                            }
                        }
                    }
                    break;

                case ']':
                    if (memory[pointer] != 0) {
                        int depth = 1;
                        while (depth > 0 && programCounter > 0) {
                            programCounter--;
                            char prevChar = program.charAt(programCounter);
                            if (prevChar == ']') {
                                depth++;
                            } else if (prevChar == '[') {
                                depth--;
                            }
                        }
                    }
                    break;
            }

            programCounter++;
        }
    }

    public static Brainfuck createInstance(String program, PrintStream out, InputStream in, int stackSize) {
        if (program == null || program.isEmpty()) {
            throw new IllegalArgumentException("Program cannot be null or empty");
        }
        if (out == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        if (stackSize < 1) {
            throw new IllegalArgumentException("Stack size must be at least 1");
        }

        return new BrainfuckImpl(program, out, in, stackSize);
    }
}