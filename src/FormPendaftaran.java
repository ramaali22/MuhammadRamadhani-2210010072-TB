import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class FormPendaftaran extends javax.swing.JInternalFrame {

    private Connection connection; // Koneksi database
    
    /**
     * Creates new form FormPendaftaran
     */
    public FormPendaftaran(Connection connection) {
        this.connection = connection; // Simpan koneksi yang diterima
        initComponents();
        loadComboBoxSiswa(); // Memuat data siswa ke combo box
        loadComboBoxJurusan(); // Memuat data jurusan ke combo box
        loadDataPendaftaran(); // Memuat data pendaftaran ke tabel
    }
    
    /**
     * Load data siswa ke combo box
     */
    private void loadComboBoxSiswa() {
        String query = "SELECT id_siswa, nama_siswa FROM siswa";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            cmbPilihSiswa.removeAllItems(); // Bersihkan combo box
            while (rs.next()) {
                String item = rs.getInt("id_siswa") + " - " + rs.getString("nama_siswa");
                cmbPilihSiswa.addItem(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data siswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load data jurusan ke combo box
     */
    private void loadComboBoxJurusan() {
        String query = "SELECT id_jurusan, nama_jurusan FROM jurusan";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            cmbPilihJurusan.removeAllItems(); // Bersihkan combo box
            while (rs.next()) {
                String item = rs.getInt("id_jurusan") + " - " + rs.getString("nama_jurusan");
                cmbPilihJurusan.addItem(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data jurusan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load data pendaftaran ke JTable
     */
    private void loadDataPendaftaran() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Siswa");
        model.addColumn("Jurusan");
        model.addColumn("Tanggal Daftar");

        String query = "SELECT p.id_pendaftaran, s.nama_siswa, j.nama_jurusan, p.tanggal "
                     + "FROM pendaftaran p "
                     + "JOIN siswa s ON p.id_siswa = s.id_siswa "
                     + "JOIN jurusan j ON p.id_jurusan = j.id_jurusan";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_pendaftaran"),
                        rs.getString("nama_siswa"),
                        rs.getString("nama_jurusan"),
                        rs.getDate("tanggal")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data pendaftaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        tblDaftar.setModel(model);
    }

    /**
     * Tambah data pendaftaran ke database
     */
    private void tambahPendaftaran() {
        String siswa = cmbPilihSiswa.getSelectedItem().toString().split(" - ")[0];
        String jurusan = cmbPilihJurusan.getSelectedItem().toString().split(" - ")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(jTanggalDaftar.getDate());

        String query = "INSERT INTO pendaftaran (id_siswa, id_jurusan, tanggal) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(siswa));
            stmt.setInt(2, Integer.parseInt(jurusan));
            stmt.setString(3, tanggal);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data pendaftaran berhasil ditambahkan.");
            loadDataPendaftaran();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan pendaftaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ubahPendaftaran() {                                              
        int selectedRow = tblDaftar.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idPendaftaran = tblDaftar.getValueAt(selectedRow, 0).toString();
        String siswa = cmbPilihSiswa.getSelectedItem().toString().split(" - ")[0];
        String jurusan = cmbPilihJurusan.getSelectedItem().toString().split(" - ")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(jTanggalDaftar.getDate());

        String query = "UPDATE pendaftaran SET id_siswa = ?, id_jurusan = ?, tanggal = ? WHERE id_pendaftaran = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(siswa));
            stmt.setInt(2, Integer.parseInt(jurusan));
            stmt.setString(3, tanggal);
            stmt.setInt(4, Integer.parseInt(idPendaftaran));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data pendaftaran berhasil diubah.");
            loadDataPendaftaran();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah pendaftaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusPendaftaran() {                                                
        int selectedRow = tblDaftar.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idPendaftaran = tblDaftar.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM pendaftaran WHERE id_pendaftaran = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(idPendaftaran));
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data pendaftaran berhasil dihapus.");
                loadDataPendaftaran();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus pendaftaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        lblPilihSiswa = new javax.swing.JLabel();
        lblPilihJurusan = new javax.swing.JLabel();
        cmbPilihSiswa = new javax.swing.JComboBox<>();
        cmbPilihJurusan = new javax.swing.JComboBox<>();
        jTanggalDaftar = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDaftar = new javax.swing.JTable();
        btnTambahDaftar = new javax.swing.JButton();
        btnUbahDaftar = new javax.swing.JButton();
        btnHapusDaftar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setTitle("Pendaftaran");
        setPreferredSize(new java.awt.Dimension(888, 700));

        jPanel1.setBackground(new java.awt.Color(0, 255, 0));

        lblPilihSiswa.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblPilihSiswa.setText("Pilih Siswa:");

        lblPilihJurusan.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblPilihJurusan.setText("Pilih Jurusan:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel3.setText("Tanggal Pendaftaran:");

        tblDaftar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Siswa", "Jurusan", "Tanggal Daftar"
            }
        ));
        jScrollPane1.setViewportView(tblDaftar);

        btnTambahDaftar.setBackground(new java.awt.Color(0, 204, 51));
        btnTambahDaftar.setText("Tambah");
        btnTambahDaftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDaftarActionPerformed(evt);
            }
        });

        btnUbahDaftar.setBackground(new java.awt.Color(204, 153, 255));
        btnUbahDaftar.setText("Ubah");
        btnUbahDaftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahDaftarActionPerformed(evt);
            }
        });

        btnHapusDaftar.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusDaftar.setText("Hapus");
        btnHapusDaftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusDaftarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Tabel Pendaftaran");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPilihJurusan)
                    .addComponent(lblPilihSiswa)
                    .addComponent(jLabel3))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPilihSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPilihJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTanggalDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTambahDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(btnUbahDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHapusDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbPilihSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPilihSiswa))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPilihJurusan)
                            .addComponent(cmbPilihJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jTanggalDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbahDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
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

    private void btnUbahDaftarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahDaftarActionPerformed
        ubahPendaftaran();
    }//GEN-LAST:event_btnUbahDaftarActionPerformed

    private void btnTambahDaftarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDaftarActionPerformed
        tambahPendaftaran();
    }//GEN-LAST:event_btnTambahDaftarActionPerformed

    private void btnHapusDaftarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDaftarActionPerformed
        hapusPendaftaran();
    }//GEN-LAST:event_btnHapusDaftarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusDaftar;
    private javax.swing.JButton btnTambahDaftar;
    private javax.swing.JButton btnUbahDaftar;
    private javax.swing.JComboBox<String> cmbPilihJurusan;
    private javax.swing.JComboBox<String> cmbPilihSiswa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jTanggalDaftar;
    private javax.swing.JLabel lblPilihJurusan;
    private javax.swing.JLabel lblPilihSiswa;
    private javax.swing.JTable tblDaftar;
    // End of variables declaration//GEN-END:variables
}
