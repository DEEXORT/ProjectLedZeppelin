package com.quest.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class ResourceBundleManager {
    private final java.util.ResourceBundle messageBundle =
            java.util.ResourceBundle.getBundle("messages", new Locale("ru", "RU"));
    private final java.util.ResourceBundle settingBundle =
            java.util.ResourceBundle.getBundle("settings", new Locale("ru", "RU"));

    public String getMessage(String key) {
        return messageBundle.getString(key);
    }

    public String getSetting(String key) {
        return settingBundle.getString(key);
    }
}
