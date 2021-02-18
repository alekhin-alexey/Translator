package model;

import java.util.HashMap;

public class FileData {
    private final FileModel fileModel;
    private final HashMap<String, String> content;

    public FileData(FileModel fileModel, HashMap<String, String> content) {
        this.fileModel = fileModel;
        this.content = content;
    }

    public String getLangContent() {
        return fileModel.getLangContent();
    }

    public HashMap<String, String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "fileModel=" + fileModel +
                ", content=" + content +
                '}';
    }
}