import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */

/**
 * FormLaporan.java
 * Form untuk menampilkan laporan pendaftaran siswa dan laporan per jurusan,
 * dengan fitur export PDF.
 */
public class FormLaporan extends javax.swing.JInternalFrame {

    private Connection connection;
    private DefaultTableModel tableModel;

    
    // Konstruktor
    public FormLaporan(Connection connection) {
        this.connection = connection;
        initComponents();
        initializeTable();
        initializeConnection();
    }

    // Inisialisasi koneksi database
    private void initializeConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:mysql://localhost:3306/pendaftaran_siswa";
                String username = "root";
                String password = "";

                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inisialisasi tabel dengan kolom yang sesuai
    private void initializeTable() {
        String[] columns = {"ID", "Nama Siswa", "Jurusan", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        tblLaporan.setModel(tableModel);
    }

    // Memuat data ke tabel berdasarkan filter
    private void loadData(String laporan, String tanggal) {
    tableModel.setRowCount(0); // Reset data di tabel
    try (Statement stmt = connection.createStatement()) {
        String query = "";

        if ("Laporan Pendaftaran".equals(laporan)) {
            // Set kolom tabel untuk laporan pendaftaran
            String[] columns = {"ID", "Nama Siswa", "Jurusan", "Tanggal"};
            setTableColumns(columns);

            // Query data pendaftaran
            query = "SELECT siswa.id_siswa, siswa.nama_siswa, jurusan.nama_jurusan, pendaftaran.tanggal " +
                    "FROM siswa " +
                    "JOIN pendaftaran ON siswa.id_siswa = pendaftaran.id_siswa " +
                    "JOIN jurusan ON pendaftaran.id_jurusan = jurusan.id_jurusan";
            if (!tanggal.isEmpty()) {
                query += " WHERE pendaftaran.tanggal = '" + tanggal + "'";
            }
        } else if ("Laporan Per Jurusan".equals(laporan)) {
            // Set kolom tabel untuk laporan per jurusan
            String[] columns = {"Jurusan", "Jumlah Siswa"};
            setTableColumns(columns);

            // Query data jumlah siswa per jurusan
            query = "SELECT jurusan.nama_jurusan, COUNT(siswa.id_siswa) AS jumlah_siswa " +
                    "FROM jurusan " +
                    "JOIN pendaftaran ON jurusan.id_jurusan = pendaftaran.id_jurusan " +
                    "JOIN siswa ON pendaftaran.id_siswa = siswa.id_siswa ";
            if (!tanggal.isEmpty()) {
                query += " WHERE pendaftaran.tanggal = '" + tanggal + "'";
            }
            query += " GROUP BY jurusan.nama_jurusan ORDER BY jurusan.nama_jurusan";
        }

        // Eksekusi query
        ResultSet rs = stmt.executeQuery(query);

        // Masukkan data ke tabel
        while (rs.next()) {
            if ("Laporan Pendaftaran".equals(laporan)) {
                Object[] row = {
                    rs.getString("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("nama_jurusan"),
                    rs.getString("tanggal")
                };
                tableModel.addRow(row);
            } else if ("Laporan Per Jurusan".equals(laporan)) {
                Object[] row = {
                    rs.getString("nama_jurusan"),
                    rs.getInt("jumlah_siswa")
                };
                tableModel.addRow(row);
            }
        }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    // Fungsi untuk mengatur kolom tabel sesuai laporan
    private void setTableColumns(String[] columns) {
        tableModel = new DefaultTableModel(); // Reset model tabel
        tableModel.setColumnIdentifiers(columns); // Set kolom tabel baru
        tblLaporan.setModel(tableModel); // Terapkan model ke JTable
    }


    private void exportToPDF() {
    // Validasi tabel tidak kosong
    if (tableModel == null || tableModel.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Tidak ada data untuk diekspor", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Membuat nama file dinamis berdasarkan tanggal dan waktu
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
        String fileName = "laporan_" + timeStamp + ".pdf";
        
        // Path lengkap untuk menyimpan file
        String filePath = "C:\\Users\\User\\OneDrive\\Documents\\NetBeansProjects\\AplikasiPendaftaranSiswa\\" + fileName;

        // Menggunakan try-with-resources untuk manajemen dokumen
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            // Dokumen PDF
            Document document = new Document();
            PdfWriter.getInstance(document, fileOut);
            document.open();

            // Judul Laporan
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Laporan Data Siswa", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Tanggal Export: " + new java.util.Date()));
            document.add(new Paragraph(" "));

            // Membuat tabel PDF
            PdfPTable pdfTable = new PdfPTable(tableModel.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Menambahkan header kolom ke PDF
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(tableModel.getColumnName(i), headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

            // Menambahkan baris data ke PDF
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    // Menangani null values
                    String cellValue = (value != null) ? value.toString() : "";
                    pdfTable.addCell(cellValue);
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this, "Laporan berhasil disimpan di: " + filePath, 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);

        }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan laporan: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
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
        cmbLaporan = new javax.swing.JComboBox<>();
        tanggalLaporan = new com.toedter.calendar.JDateChooser();
        btnCari = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        lblPilihLaporan = new javax.swing.JLabel();
        lblTglLaporan = new javax.swing.JLabel();

        setTitle("Laporan");
        setPreferredSize(new java.awt.Dimension(888, 700));

        jPanel1.setBackground(new java.awt.Color(0, 255, 0));

        cmbLaporan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laporan Pendaftaran", "Laporan Per Jurusan", " " }));

        btnCari.setBackground(new java.awt.Color(51, 51, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Tabel Laporan");

        jScrollPane1.setViewportView(tblLaporan);

        btnCetak.setBackground(new java.awt.Color(0, 153, 0));
        btnCetak.setText("Cetak Laporan PDF");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        lblPilihLaporan.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblPilihLaporan.setText("Pilih Laporan:");

        lblTglLaporan.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblTglLaporan.setText("Tanggal:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(289, 289, 289))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPilihLaporan)
                            .addComponent(lblTglLaporan))
                        .addGap(195, 195, 195)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(353, 353, 353)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPilihLaporan)
                    .addComponent(cmbLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTglLaporan)
                    .addComponent(tanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCetak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String laporan = cmbLaporan.getSelectedItem().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = (tanggalLaporan.getDate() != null) ? sdf.format(tanggalLaporan.getDate()) : "";

        loadData(laporan, tanggal);
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        exportToPDF();
    }//GEN-LAST:event_btnCetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCetak;
    private javax.swing.JComboBox<String> cmbLaporan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPilihLaporan;
    private javax.swing.JLabel lblTglLaporan;
    private com.toedter.calendar.JDateChooser tanggalLaporan;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}
