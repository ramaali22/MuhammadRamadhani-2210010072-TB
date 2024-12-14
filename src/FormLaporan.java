import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class FormLaporan extends javax.swing.JInternalFrame {

    private Connection connection;
    private DefaultTableModel tableModel;

    
    /**
     * Creates new form FormLaporan
     */    
    public FormLaporan(Connection connection) {
        this.connection = connection;
        initComponents();
        initializeTable();
        initializeConnection();  // Inisialisasi koneksi ke database
    }

    // Inisialisasi koneksi database
    private void initializeConnection() {
        try {
            // Gantilah dengan informasi koneksi database Anda
            String url = "jdbc:mysql://localhost:3306/pendaftaran_siswa";  // URL database
            String username = "root";  // Username database
            String password = "";  // Password database

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeTable() {
        String[] columns = {"ID", "Nama Siswa", "Jurusan", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        tblLaporan.setModel(tableModel);
    }

    private void loadData(String laporan, String tanggal) {
        tableModel.setRowCount(0);

        try (Statement stmt = connection.createStatement()) {

            String query = "";

            if ("Laporan Pendaftaran".equals(laporan)) {
                query = "SELECT siswa.id_siswa, siswa.nama_siswa, jurusan.nama_jurusan, pendaftaran.tanggal " +
                        "FROM siswa " +
                        "JOIN pendaftaran ON siswa.id_siswa = pendaftaran.id_siswa " +
                        "JOIN jurusan ON pendaftaran.id_jurusan = jurusan.id_jurusan";
            } else if ("Laporan Per Jurusan".equals(laporan)) {
                query = "SELECT jurusan.nama_jurusan, COUNT(pendaftaran.id_siswa) AS jumlah, MAX(pendaftaran.tanggal) AS tanggal_terakhir " +
                        "FROM pendaftaran " +
                        "JOIN jurusan ON pendaftaran.id_jurusan = jurusan.id_jurusan " +
                        "GROUP BY jurusan.nama_jurusan";
            }

            if (!tanggal.isEmpty()) {
                query += " WHERE pendaftaran.tanggal = '" + tanggal + "'";
            }

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row;
                if ("Laporan Pendaftaran".equals(laporan)) {
                    row = new Object[]{
                            rs.getString("id_siswa"),
                            rs.getString("nama_siswa"),
                            rs.getString("nama_jurusan"),
                            rs.getString("tanggal")
                    };
                } else {
                    row = new Object[]{
                            rs.getString("nama_jurusan"),
                            rs.getString("jumlah"),
                            " - ",
                            rs.getString("tanggal_terakhir")
                    };
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToPDF() {
        try {
            String filePath = "Laporan.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Laporan Pendaftaran"));
            document.add(new Paragraph("======================================"));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(tableModel.getColumnCount());

            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                pdfTable.addCell(tableModel.getColumnName(i));
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    pdfTable.addCell(tableModel.getValueAt(i, j).toString());
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this, "Laporan berhasil dicetak ke PDF: " + filePath, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        lblPilihLaporan = new javax.swing.JLabel();
        lblTglLaporan = new javax.swing.JLabel();

        setTitle("Laporan Pendaftaran");

        jPanel1.setBackground(new java.awt.Color(0, 255, 0));

        cmbLaporan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laporan Pendaftaran", "Laporan Per Jurusan", " " }));

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nama Siswa", "Jurusan", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(tblLaporan);

        btnCetak.setText("Cetak Laporan PDF");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        lblPilihLaporan.setText("Pilih Laporan");

        lblTglLaporan.setText("Tanggal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(289, 289, 289))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPilihLaporan)
                    .addComponent(lblTglLaporan))
                .addGap(197, 197, 197)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPilihLaporan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTglLaporan))
                .addGap(39, 39, 39)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnCetak, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPilihLaporan;
    private javax.swing.JLabel lblTglLaporan;
    private com.toedter.calendar.JDateChooser tanggalLaporan;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}
