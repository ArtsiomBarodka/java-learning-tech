package com.epam.task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

public class FolderScannerRecursiveAction extends RecursiveTask<FolderInfo> {
    private final Path path;
    private final boolean isRoot;

    public FolderScannerRecursiveAction(Path path) {
        this(path, true);
    }

    public FolderScannerRecursiveAction(Path path, boolean isRoot) {
        this.path = path;
        this.isRoot = isRoot;
    }


    @Override
    protected FolderInfo compute() {
        if (!Files.exists(path.toAbsolutePath())) {
            return new FolderInfo();
        }

        System.out.printf("Scanning the Folder/File: %s \n", path);

        FolderInfo folderInfo;
        try {
            if (Files.isDirectory(path)) {
                folderInfo = getFolderInfo();
                if (isRoot) {
                    folderInfo.setFoldersCount(folderInfo.getFoldersCount() - 1);
                }
            } else {
                folderInfo = getFileInfo();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return folderInfo;
    }

    private FolderInfo getFileInfo() throws IOException {
        return new FolderInfo(1, 0, Files.size(path));
    }

    private FolderInfo getFolderInfo() throws IOException {
        try (Stream<Path> stream = Files.list(path)){
            final List<FolderScannerRecursiveAction> folderScannerRecursiveActions = stream
                    .map(Path::toAbsolutePath)
                    .map(item -> new FolderScannerRecursiveAction(item, false))
                    .peek(ForkJoinTask::fork)
                    .toList();

            final List<FolderInfo> folderInfo = folderScannerRecursiveActions.stream()
                    .map(ForkJoinTask::join)
                    .toList();

            return folderInfo.stream().reduce(new FolderInfo(0, 1, 0), (info1, info2) -> {
                return new FolderInfo(
                        info1.getFilesCount() + info2.getFilesCount(),
                        info1.getFoldersCount() + info2.getFoldersCount(),
                        info1.getSize() + info2.getSize());
            });
        }
    }
}
