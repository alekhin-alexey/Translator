package controller;

import org.junit.jupiter.api.Test;
import service.api.TypesAPI;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguagesTest {

    // test Singleton
    @Test
    void getLanguages() {
        Map<String, String> mapLang1 = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);
        Map<String, String> mapLang2 = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);
        Map<String, String> mapLang3 = Languages.getInstance().getLanguages(TypesAPI.MICROSOFT);

        assertEquals(mapLang1, mapLang2);
        assertEquals(mapLang1, mapLang3);

        System.out.println("1:" + mapLang1.getClass().hashCode() + "; " +
                "2:" + mapLang2.getClass().hashCode() + "; " +
                "3:" + mapLang3.getClass().hashCode() + "; "
        );

    }

    @Test
    void getLangSuffix() {
        final String name = "Russian";
        final String expected = "ru";
        Languages.getInstance().getLanguages(TypesAPI.MICROSOFT); // get instance mapLanguages
        final String result = Languages.getInstance().getLangSuffix(name);

        assertEquals(expected, result);

        System.out.println(result);
    }
}