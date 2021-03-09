package model;

import java.util.Map;

public class FileTask {
    private final FileModel fileModel;
    private final Map<String, String> content;

    public FileTask(FileModel fileModel, Map<String, String> content) {
        this.fileModel = fileModel;
        this.content = content;
    }

    public Map<String, String> getContent() {
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