package com.mycompany.tapsecure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18nService {
    private static ResourceBundle bundle;
    private static Locale currentLocale;
    
    public interface I18nChangeListener {
        void onLanguageChanged();
    }
    
    private static final List<I18nChangeListener> listeners = new ArrayList<>();
    
    static {
        currentLocale = new Locale("id");
        loadBundle();
    }
    
    private static void loadBundle() {
        ResourceBundle.clearCache();
        try {
        // Coba dengan locale persis
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
        System.out.println("Bundle loaded: locale " + currentLocale);
        } catch (MissingResourceException e1) {
            try {
                // Coba hanya language-nya saja (tanpa country)
                Locale langOnly = new Locale(currentLocale.getLanguage());
                bundle = ResourceBundle.getBundle("i18n.messages", langOnly);
                System.out.println("Bundle loaded dengan language only: " + langOnly);
            } catch (MissingResourceException e2) {
                try {
                    // Fallback ke messages.properties
                    bundle = ResourceBundle.getBundle("i18n.messages", Locale.ROOT);
                    System.out.println("Bundle fallback ke ROOT");
                } catch (MissingResourceException e3) {
                    System.err.println("WARNING: i18n bundle tidak ditemukan! " + e3.getMessage());
                    bundle = null;
                }
            }
        }
    }
    
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        loadBundle();
        notifyListeners();
    }
    
    public static String get(String key) {
        if (bundle == null) return "!" + key + "!";
        try {
            return bundle.getString(key);
        } catch (MissingResourceException | NullPointerException e) {
            return "!" + key + "!";
        }
    }
    
    public static Locale getCurrentLocale() {
        return currentLocale != null ? currentLocale : new Locale("id");
    }
    
    public static synchronized void registerListener(I18nChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public static synchronized void unregisterListener(I18nChangeListener listener) {
        listeners.remove(listener);
    }
    
    private static void notifyListeners() {
        for (I18nChangeListener listener : listeners) {
            if (listener != null) {
                listener.onLanguageChanged();
            }
        }
    }
}