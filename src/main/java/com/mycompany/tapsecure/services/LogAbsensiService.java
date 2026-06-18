package com.mycompany.tapsecure.services;

import com.mycompany.tapsecure.dao.GenericDAO;
import com.mycompany.tapsecure.objects.LogAbsensi;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author vaena
 */
public class LogAbsensiService {
    // Inisialisasi DAO untuk koleksi "log_absensi" [7]
    private final GenericDAO<LogAbsensi> logDAO = new GenericDAO<>("log_absensi", LogAbsensi.class);
    
    public void simpanLog(String hashedUid, String status) {
        // Membuat objek LogAbsensi sesuai parameter di sumber [6]
        LogAbsensi log = new LogAbsensi(
            UUID.randomUUID().toString(), 
            hashedUid, 
            LocalDateTime.now(), 
            status
        );
        logDAO.save(log); // Menyimpan ke MongoDB [7]
    }
    
    
}
