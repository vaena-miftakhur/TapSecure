/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tapsecure.services;

import com.mycompany.tapsecure.gui.panel.KaryawanPanel;
import com.mycompany.tapsecure.dao.GenericDAO;
import com.mycompany.tapsecure.objects.Karyawan;
import com.mongodb.client.model.Filters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bson.conversions.Bson;
import com.mycompany.tapsecure.services.I18nService;
import com.mycompany.tapsecure.util.EncryptionUtils;


/**
 *
 * @author Muhammad-Satria, vaena
 */
public class KaryawanService {

    // Inisialisasi GenericDAO khusus untuk entitas Karyawan
    // Menggunakan koleksi "karyawan" dan referensi Class Karyawan [3]
    private final GenericDAO<Karyawan> DAO;

    public KaryawanService() {
        this.DAO = new GenericDAO<>("karyawan", Karyawan.class);
    }

    /**
     * 1.CREATE: Fungsi untuk menyimpan data karyawan baru ke MongoDB [2], [3]
     *
     * @param karyawanBaru
     */
    public void tambahKaryawan(Karyawan karyawanBaru) {
        DAO.save(karyawanBaru); // Memanggil insertOne melalui GenericDAO [3]
    }

    public void tambahKaryawan(String uidRfid, String idKaryawan, String namaLengkap, String departemen) {
        Karyawan karyawanBaru = new Karyawan(uidRfid, idKaryawan, namaLengkap, departemen);
        DAO.save(karyawanBaru); // Memanggil insertOne melalui GenericDAO [3]
    }

    /**
     * 2. READ (All): Fungsi untuk mengambil semua data karyawan [5], [6]
     */
    public void tampilkanDaftarKaryawan() {
        List<Karyawan> daftar = DAO.findAll();
        System.out.println("--- Daftar Karyawan ---");
        for (Karyawan k : daftar) {
            System.out.println(k.toString()); // Menggunakan format toString di sumber [7]
        }
    }

    /**
     * 2.READ (All): Fungsi untuk mengambil semua data karyawan [5], [6]
     *
     * @param panelTarget
     * @param key
     */
    public void tampilKaryawan(JPanel panelTarget, String key) {
        //1. 
        // Menampilkan data berdasarkan request
        // key "null/kosong" = get all data
        // key "filled" = get specific data

        List<Karyawan> daftarKaryawan;
        if (key.isEmpty()) {
            //Mengambil data dari database menggunakan GenericDAO
            daftarKaryawan = DAO.findAll();
        } else {
            //Mengambil data dari database menggunakan GenericDAO
            //berdasarkan kata kunci yang diketik
            daftarKaryawan = cariKaryawan(key);
        }
        // 2. Membersihkan panel target utama sebelum memuat data baru
        panelTarget.removeAll();

        // Mengubah layout panel target menjadi BorderLayout
        panelTarget.setLayout(new BorderLayout());
        // Mengatur warna background utama menjadi putih
        panelTarget.setBackground(new Color(255, 255, 255));

        // Membuat panel grid khusus untuk menampung kotak/card
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false); // Transparan agar warna biru panelTarget terlihat
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Memberi jarak dari tepi layar

        // 3. Iterasi data dan menambahkannya ke panel grid
        try {
            for (Karyawan k : daftarKaryawan) {
                // Membuat panel 'Card' (box orange) untuk 1 karyawan
                // Layout 4 baris 1 kolom agar kolor berisi Nama,ID, Departemen, panel control 
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 0));
                cardPanel.setBackground(new Color(0, 0, 255)); // Warna background biru

                // Memberikan garis tepi tipis membulat (rounded) dan padding/jarak ke dalam
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.MAGENTA, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // Membuat Label Nama & Set warna teks jadi Putih
                JLabel lblNama = new JLabel(
                    I18nService.get("ui.emp.name") + ": " + k.getNamaLengkap());
                lblNama.setForeground(Color.WHITE);

                // Membuat Label ID Karyawan & Set warna teks jadi Putih
                //  KODE YANG BENAR (Didekripsi dulu sebelum tampil di card):
                JLabel lblIDK = new JLabel(
                    I18nService.get("ui.emp.id") + ": "
                    + EncryptionUtils.decrypt(k.getIdKaryawan()));
                lblIDK.setForeground(Color.WHITE);

                // Membuat Label Departemen & Set warna teks jadi Putih
                JLabel lblDept = new JLabel(
                    I18nService.get("ui.emp.dept") + ": " + I18nService.get(k.getDepartemen()));
                lblDept.setForeground(Color.WHITE);

                // Membuat panel kontrol 1 baris 2 kolom, berisi tombol edit dan hapus
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                controlPanel.setBackground(new Color(0, 0, 255));

                JButton tombolEdit = new JButton(
                     I18nService.get("ui.btn.edit"));
                tombolEdit.setBackground(Color.WHITE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {

                // SIMPAN HASH UID LAMA
                KaryawanPanel.uidLama = k.getUidRfid();

                // Jangan tampilkan hash ke user
                KaryawanPanel.txtUID.setText("");

                // Tampilkan ID hasil decrypt
                KaryawanPanel.txtKRID.setText(
                    com.mycompany.tapsecure.util.EncryptionUtils.decrypt(
                        k.getIdKaryawan()
                    )
                );

                KaryawanPanel.txtKRID.setEnabled(false);
                KaryawanPanel.txtKRName.setText(k.getNamaLengkap());
                                String[] deptKeys = {"dept.hrd", "dept.finance", "dept.marketing",
                                      "dept.sales", "dept.operation", "dept.it", "dept.ga"};
                int deptIndex = 0;
                for (int i = 0; i < deptKeys.length; i++) {
                    if (deptKeys[i].equals(k.getDepartemen())) {
                        deptIndex = i;
                        break;
                    }
                }
                KaryawanPanel.txtKRDept.setSelectedIndex(deptIndex);

                KaryawanPanel.btnUpdate.setEnabled(true);
                KaryawanPanel.btnSave.setEnabled(false);

                JOptionPane.showMessageDialog(
                    null,
                    I18nService.get("msg.changeCard")
                );
                });
                JButton tombolDelete = new JButton(
                     I18nService.get("ui.btn.delete"));
                tombolDelete.setBackground(Color.WHITE);
                tombolDelete.setForeground(Color.BLACK);
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {
                        I18nService.get("btn.yesDelete"),
                        I18nService.get("btn.cancel")
                    };
                    int choice = JOptionPane.showOptionDialog(
                            null, // Parent component
                            I18nService.get("msg.deleteConfirm") + " " + k.getNamaLengkap() + "?", // Message
                            I18nService.get("title.confirm"), // Title
                            JOptionPane.YES_NO_OPTION, // Option type
                            JOptionPane.QUESTION_MESSAGE, // Message type
                            null, // Custom icon (null uses default)
                            options, // The array of custom button text
                            options[0] // Default button focused
                    );

                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusKaryawan(k.getIdKaryawan());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {
                        }
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                // Memasukkan label ke dalam cardPanel (box orange)
                cardPanel.add(lblNama);
                cardPanel.add(lblIDK);
                cardPanel.add(lblDept);
                cardPanel.add(controlPanel);

                // Memasukkan cardPanel utuh ke dalam gridPanel
                gridPanel.add(cardPanel);
            }

            // Memasukkan gridPanel ke bagian ATAS (NORTH) dari panel target.
            panelTarget.add(gridPanel, BorderLayout.NORTH);

            // 4. Me-refresh panel agar perubahan muncul di GUI
            panelTarget.revalidate();
            panelTarget.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    /**
     * 3.READ (One): Mencari satu karyawan spesifik berdasarkan UID RFID [5],
         * [6] Sangat krusial untuk alur Tap Kartu pada Pertemuan 14.
     */
    public Karyawan findByUid(String hashedUid) {
        try {
            // Bersihkan string dan pastikan huruf kecil agar pencarian ke MongoDB 100% akurat
            String cleanUid = hashedUid.trim().toLowerCase();
            return DAO.findOne(Filters.eq("uidRfid", cleanUid));
        } catch (Exception e) {
            System.err.println("Gagal mencari karyawan: " + e.getMessage());
            return null;
        }
    }

    /**
     * 4. SEARCH: Fungsi pencarian data berdasarkan kemiripan nama.
     */
    public List<Karyawan> cariKaryawan(String key) {
        try {
            return DAO.findMany(Filters.regex("namaLengkap", key, "i"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 5. DELETE: Fungsi menghapus data berdasarkan ID Karyawan.
     */
    public void hapusKaryawan(String idKaryawan) {
        try {
            DAO.delete(Filters.eq("idKaryawan", idKaryawan));
            JOptionPane.showMessageDialog(null, I18nService.get("msg.deleteSuccess"));
            KaryawanPanel.showData(""); // ← tambahkan ini, refresh tampilan card
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, I18nService.get("msg.deleteFailed") + e.getMessage());
        }
    }

    /**
     * 6. UPDATE: Fungsi untuk memperbarui data ke MongoDB.
     */
    public boolean updateKaryawan(Karyawan karyawan) {
        try {
            DAO.update(Filters.eq("idKaryawan", karyawan.getIdKaryawan()), karyawan);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

