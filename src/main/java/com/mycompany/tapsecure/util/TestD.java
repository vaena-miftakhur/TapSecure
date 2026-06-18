/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tapsecure.util;

import com.mycompany.tapsecure.services.AuthService;

/**
 *
 * @author vaena
 */
public class TestD {   
    public static void main(String[] args) {
        try {
            AuthService auth = new AuthService();
            // Ini akan membuat akun baru dan meng-hash password otomatis dengan cara yang benar
            auth.registerUser("Admin TapSecure", "admin", "tapsecure123");
            System.out.println("[BERHASIL] Akun admin resmi terdaftar di database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
