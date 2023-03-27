package com.epam.task3;

public class FolderInfo {
    private long filesCount;
    private long foldersCount;
    private long size;

    public FolderInfo() {
    }


    public FolderInfo(long filesCount, long foldersCount, long size) {
        this.filesCount = filesCount;
        this.foldersCount = foldersCount;
        this.size = size;
    }

    public long getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(long filesCount) {
        this.filesCount = filesCount;
    }

    public long getFoldersCount() {
        return foldersCount;
    }

    public void setFoldersCount(long foldersCount) {
        this.foldersCount = foldersCount;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FolderInfo{" +
                "filesCount=" + filesCount +
                ", foldersCount=" + foldersCount +
                ", size=" + size +
                '}';
    }
}
