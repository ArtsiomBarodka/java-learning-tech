package com.epam.task3;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private final static String MESSAGE = "Enter the path: ";

    public static void main(String[] args) {
        var folderScanner = new FJPFolderScanner();

        try (var scanner = new Scanner(System.in)){
            System.out.print(MESSAGE);

            while (scanner.hasNext()){

                var input = scanner.nextLine();
                if ("c".equalsIgnoreCase(input)) {
                    System.out.println("Application has finished");
                    break;
                }

                var path = Path.of(input);
                if (!Files.exists(path)){
                    System.out.printf("The path: %s doesn't exist \n", path);
                    System.out.print(MESSAGE);
                    continue;
                }

                folderScanner.scan(path);

                System.out.print(MESSAGE);
            }
        }
    }
}
