/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.tapsecure.gui.panel;

import java.util.List;

/**
 *
 * @author Muhammad Satria, vaena
 */
public class Reports extends javax.swing.JPanel {

    /**
     * Creates new form Reports
     */
    public Reports() {
        initComponents();

        // Set layout utama
        this.setLayout(new java.awt.BorderLayout());

        // Buat panel atas untuk combobox dan button - center
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        // Ganti layout jPanel1
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        this.add(jPanel1, java.awt.BorderLayout.CENTER);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei ", "Juni ", 
            "Juli", "Agustus", "September", "Oktober", "November", "Desember" 
        }));
        jComboBox1.setSelectedIndex(0);
        showReportData();
    }
    
    public void showReportData() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new Object[][]{}, 
            new String[]{"Id", "Nama Karyawan", "Departemen", "Status Absensi"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        jTable1.setModel(model);

        String bulanDipilih = jComboBox1.getSelectedItem().toString().trim();

        com.mycompany.tapsecure.services.LogAbsensiService logService = new com.mycompany.tapsecure.services.LogAbsensiService();
        com.mycompany.tapsecure.services.KaryawanService karyawanService = new com.mycompany.tapsecure.services.KaryawanService();

        java.util.List<com.mycompany.tapsecure.objects.LogAbsensi> listLog = logService.getAllLog();

        if (listLog == null || listLog.isEmpty()) return;

        for (com.mycompany.tapsecure.objects.LogAbsensi log : listLog) {
            
            // 💡 STRATEGI PENYARINGAN AMAN BERDASARKAN TEKS COMBOBOX
            if (!bulanDipilih.equalsIgnoreCase("Semua Bulan")) {
                int bulanLog = 0;
                
                if (log.getWaktuTap() != null) {
                    // Jika sukses dipetakan sebagai LocalDateTime Java
                    bulanLog = log.getWaktuTap().getMonthValue();
                } else {
                    // 🌟 TRIK BYPASS: Jika database berupa String, kita tebak bulannya dari format teks mentah database Anda!
                    // Contoh teks database Anda di gambar: "2026-06-30T23:29..." -> Angka bulan berada di karakter indeks ke-5 sampai ke-7
                    try {
                        // Mengambil isi data dokumen log menggunakan refleksi java secara aman
                        java.lang.reflect.Field fieldWaktu = log.getClass().getDeclaredField("waktuTap");
                        fieldWaktu.setAccessible(true);
                        Object objWaktu = fieldWaktu.get(log);
                        
                        if (objWaktu != null) {
                            String teksWaktuMentah = objWaktu.toString().trim(); // Hasilnya: "2026-06-30..."
                            if (teksWaktuMentah.length() >= 7) {
                                String teksBulan = teksWaktuMentah.substring(5, 7); // Mengambil potongan string "06"
                                bulanLog = Integer.parseInt(teksBulan); // Mengubah string "06" menjadi angka 6 (Juni)
                            }
                        }
                    } catch (Exception e) {
                        // Jika gagal menebak, anggap bulan 0 agar disaring keluar
                        bulanLog = 0; 
                    }
                }
                
                // Konversi angka bulan (1-12) menjadi teks nama bulan Bahasa Indonesia
                String namaBulanLog = "";
                switch (bulanLog) {
                    case 1 -> namaBulanLog = "Januari";
                    case 2 -> namaBulanLog = "Februari";
                    case 3 -> namaBulanLog = "Maret";
                    case 4 -> namaBulanLog = "April";
                    case 5 -> namaBulanLog = "Mei";
                    case 6 -> namaBulanLog = "Juni";
                    case 7 -> namaBulanLog = "Juli";
                    case 8 -> namaBulanLog = "Agustus";
                    case 9 -> namaBulanLog = "September";
                    case 10 -> namaBulanLog = "Oktober";
                    case 11 -> namaBulanLog = "November";
                    case 12 -> namaBulanLog = "Desember";
                }
                
                // Jika nama bulannya tidak cocok dengan pilihan di ComboBox, lewati baris data ini
                if (!namaBulanLog.equalsIgnoreCase(bulanDipilih)) {
                    continue; 
                }
            }
            // 🌟 JIKA "Semua Bulan" terpilih, penyaringan di atas dilewati total, seluruh data langsung lolos!

            String idTampil = "Tidak Terdaftar";
            String namaTampil = "-";
            String deptTampil = "-";

            com.mycompany.tapsecure.objects.Karyawan karyawan = karyawanService.findByUid(log.getUidRfid());

            if (karyawan != null) {
                idTampil = com.mycompany.tapsecure.util.EncryptionUtils.decrypt(karyawan.getIdKaryawan());
                namaTampil = karyawan.getNamaLengkap();
                deptTampil = karyawan.getDepartemen();
            } else {
                if (log.getUidRfid() != null) {
                    String shortUid = log.getUidRfid().length() > 8 ? log.getUidRfid().substring(0, 8) : log.getUidRfid();
                    idTampil = "UID: " + shortUid;
                }
            }

            // Membaca waktu untuk ditampilkan di tabel (menggunakan refleksi jika getWaktuTap null)
            String waktuTampil = "-";
            if (log.getWaktuTap() != null) {
                waktuTampil = log.getWaktuTap().toString();
            } else {
                try {
                    java.lang.reflect.Field fieldWaktu = log.getClass().getDeclaredField("waktuTap");
                    fieldWaktu.setAccessible(true);
                    Object objWaktu = fieldWaktu.get(log);
                    if (objWaktu != null) waktuTampil = objWaktu.toString();
                } catch(Exception ex) {}
            }

            model.addRow(new Object[]{
                idTampil,
                namaTampil,
                deptTampil,
                log.getStatus()
            });
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));

        jScrollPane1.setBackground(new java.awt.Color(102, 204, 255));
        jScrollPane1.setOpaque(false);

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Nama Karyawan", "Departemen", "Status Absensi"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei ", "Juni ", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox1);

        jButton1.setText("Export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(278, 278, 278)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
         // Setiap kali pilihan bulan diganti, panggil kembali fungsi tampil data
        showReportData();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String bulanIni = jComboBox1.getSelectedItem().toString().trim();
        
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Bulanan");
        fileChooser.setSelectedFile(new java.io.File("Laporan_Absensi_" + bulanIni + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try (java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {
                javax.swing.table.TableModel tableModel = jTable1.getModel();
                
                // 💡 MENGGUNAKAN TITIK KOMA (;) Agar Excel otomatis memisahkan kolom dengan rapi
                fw.write("ID Karyawan;Nama Karyawan;Departemen;Status Absensi\n");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String id = tableModel.getValueAt(i, 0).toString();
                    String nama = tableModel.getValueAt(i, 1).toString();
                    String dept = tableModel.getValueAt(i, 2).toString();
                    String status = tableModel.getValueAt(i, 3).toString();
                    
                    // Menggabungkan data baris laporan dengan format pemisah titik koma
                    fw.write(id + ";" + nama + ";" + dept + ";" + status + "\n");
                }
                
                javax.swing.JOptionPane.showMessageDialog(this, "Laporan " + bulanIni + " Berhasil Dieksport ke: \n" + fileToSave.getAbsolutePath());
                
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengeksport data: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
