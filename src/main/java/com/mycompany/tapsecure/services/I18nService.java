/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tapsecure.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author mnish
 */
public class I18nService {
    private static ResourceBundle bundle;
    private static Locale currentLocale;
    
    // Interface untuk mendaftarkan UI yang ingin mendengarkan perubahan bahasa
    public interface I18nChangeListener {
        void onLanguageChanged();
    }
    
    // Daftar semua form/panel yang sedang aktif mendengarkan perubahan
    private static final List<I18nChangeListener> listeners = new ArrayList<>();
    
    // Blok inisialisasi default agar tidak NullPointerException di awal aplikasi
    static {
        setLocale(Locale.of("id")); 
    }
    
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        // Membaca file di src/i18n/messages_xx.properties
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
        
        // PICU PERUBAHAN: Beritahu semua UI untuk update teks mereka secara serentak
        notifyListeners();
    }
    
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException | NullPointerException e) {
            return "!" + key + "!"; 
        }
    }
    
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    // --- MANAJEMEN LISTENER (OBSERVER) ---
    
    public static synchronized void registerListener(I18nChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public static synchronized void unregisterListener(I18nChangeListener listener) {
        listeners.remove(listener);
    }
    
    private static void notifyListeners() {
        // Jalankan perulangan untuk mengeksekusi fungsi update di setiap form
        for (I18nChangeListener listener : listeners) {
            if (listener != null) {
                listener.onLanguageChanged();
            }
        }
    }
}