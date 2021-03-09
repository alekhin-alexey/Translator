package model;

public class FileModel {
    /**
     * original file name
     */
    private final String name;
    /**
     * language of data content
     */
    private final String lang;

    public FileModel(String name, String lang) {
        this.name = name;
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public String toString() {
        return "ModelFile{" +
                "name='" + name + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
