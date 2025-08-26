package com.quest.util;

import java.util.Locale;

public class ResourceBundleManager {
    private static final java.util.ResourceBundle messageBundle =
            java.util.ResourceBundle.getBundle("messages", new Locale("ru", "RU"));
    private static final java.util.ResourceBundle settingBundle =
            java.util.ResourceBundle.getBundle("settings", new Locale("ru", "RU"));

    public static String getMessage(String key) {
        return messageBundle.getString(key);
    }

    public static String getSetting(String key) {
        return settingBundle.getString(key);
    }
}
