package com.epam.task3;

import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

public final class FJPFolderScanner implements FolderScanner {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public void scan(Path path) {
        var folderScannerRecursiveAction = new FolderScannerRecursiveAction(path);
        var result = forkJoinPool.invoke(folderScannerRecursiveAction);
        System.out.printf("The result of path: %s: %s \n",path, result);
    }
}
