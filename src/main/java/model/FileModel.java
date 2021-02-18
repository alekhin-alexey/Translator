package model;

public class FileModel {
    private final String fileName;
    private final String langContent;

    public FileModel(String fileNameOriginal, String langContent) {
        this.fileName = fileNameOriginal;
        this.langContent = langContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLangContent() {
        return langContent;
    }

    @Override
    public String toString() {
        return "ModelFile{" +
                "fileName='" + fileName + '\'' +
                ", langContent='" + langContent + '\'' +
                '}';
    }
}
