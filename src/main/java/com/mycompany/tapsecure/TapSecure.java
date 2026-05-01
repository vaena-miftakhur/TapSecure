/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tapsecure;

import com.mycompany.tapsecure.objects.Karyawan;

/**
 *
 * @author LENOVO
 */
public class TapSecure {
    public static void main(String[] args) {
        Karyawan KR = new Karyawan();
        if(KR instanceof Karyawan){
            System.err.println("Karyawan");
        }else {
            System.err.println("Something else");
        }// //
        // //
    }
}
