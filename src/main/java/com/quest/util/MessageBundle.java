package com.quest.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageBundle {
    private static final ResourceBundle bundle =
            ResourceBundle.getBundle("messages", new Locale("ru", "RU"));

    public static String get(String key) {
        return bundle.getString(key);
    }
}
