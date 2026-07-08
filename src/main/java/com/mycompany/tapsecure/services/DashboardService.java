package com.mycompany.tapsecure.services;

import com.mycompany.tapsecure.dao.GenericDAO;
import com.mycompany.tapsecure.objects.Karyawan;
import com.mycompany.tapsecure.objects.LogAbsensi;
import com.mongodb.client.model.Filters;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author vaena
 */

public class DashboardService {

    private final GenericDAO<Karyawan> daoKaryawan;
    private final GenericDAO<LogAbsensi> daoLog;

    public DashboardService() {
        daoKaryawan = new GenericDAO<>("karyawan", Karyawan.class);
        daoLog = new GenericDAO<>("log_absensi", LogAbsensi.class);
    }

    //Jumlah seluruh karyawan
    public int getJumlahKaryawan() {
        return daoKaryawan.findAll().size();
    }

    //Jumlah absensi hari ini
    public int getTotalHadirHariIni() {

        int total = 0;
        LocalDate today = LocalDate.now();

        List<LogAbsensi> logs = daoLog.findAll();

        for (LogAbsensi log : logs) {

            if (log.getWaktuTap() != null &&
                log.getWaktuTap().toLocalDate().equals(today) &&
                log.getStatus().equalsIgnoreCase("HADIR")) {

                total++;
            }
        }

        return total;
    }

    //Jumlah terlambat
    public int getJumlahTerlambat() {

        int total = 0;
        LocalDate today = LocalDate.now();

        List<LogAbsensi> logs = daoLog.findAll();

        for (LogAbsensi log : logs) {

            if (log.getWaktuTap() != null &&
                log.getWaktuTap().toLocalDate().equals(today) &&
                log.getStatus().equalsIgnoreCase("TERLAMBAT")) {

                total++;
            }
        }

        return total;
    }
    
    // Jumlah karyawan yang belum presensi hari ini
    public int getBelumPresensi() {

        int jumlahKaryawan = getJumlahKaryawan();
        int hadir = getTotalHadirHariIni();
        int terlambat = getJumlahTerlambat();

        return jumlahKaryawan - hadir - terlambat;
    }

}