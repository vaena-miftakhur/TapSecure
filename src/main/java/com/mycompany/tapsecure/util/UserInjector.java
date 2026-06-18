/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tapsecure.util;

import com.mycompany.tapsecure.services.AuthService;

/**
 *
 * @author mnish
 */
public class UserInjector {
    public static void main(String[] args) {
        AuthService userService = new AuthService();
        userService.registerUser("vaena", "uhn", "vaena123"); 
    }
}
