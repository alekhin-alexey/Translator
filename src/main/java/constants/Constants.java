package constants;

public interface Constants {
    String PATH = System.getProperty("user.dir");

    String PROP_FILE_NAME = PATH + "/config.properties";
    String PROP_LANG_FROM = "langFrom";
    String PROP_LANG_TO = "langTo";
    String PROP_DIR = "dir";

    String JSON_NODE_ROOT = "translation";
    String JSON_NODE_NAME = "name";
    String JSON_NODE_TEXT = "text";

    String XML_TAG_NAME_ROOT = "properties";
    String XML_TAG_NAME_PROP = "entry";
    String XML_TAG_NAME_PROP_ATTRIBUTE = "key";
    String XML_FILE_EXT = ".xml";

    String LANG_NAME_DEFAULT = "English";
    String LANG_SUFFIX_DEFAULT = "en";

    String SYS_INFORMATION_MESSAGE_TITLE = "Information";
    String SYS_FONT_TITLE_UI = "Courier";

}
