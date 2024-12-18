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
public class FormSiswa extends javax.swing.JInternalFrame {

    private Connection connection; // Koneksi database
    
    /**
     * Creates new form FormSiswa
     */
    public FormSiswa(Connection connection) {
        this.connection = connection; // Simpan koneksi yang diterima
        initComponents();
        loadDataSiswa(); // Load data siswa ke tabel
    }
    
    private void loadDataSiswa() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Tanggal Lahir");
        model.addColumn("NISN");
        model.addColumn("No. Telp");
        model.addColumn("Email");
        model.addColumn("Alamat");

        String query = "SELECT * FROM siswa";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_siswa"),
                        rs.getString("nama_siswa"),
                        rs.getDate("tanggal_lahir"),
                        rs.getString("nisn"),
                        rs.getString("no_hp"),
                        rs.getString("email"),
                        rs.getString("alamat")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data siswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        tblSiswa.setModel(model);
    }

    /**
     * Tambah data siswa ke database
     */
    private void tambahSiswa() {
        String nama = txtNama.getText();
        String nisn = txtNisn.getText();
        String noHp = txtTelpon.getText();
        String email = txtEmail.getText();
        String alamat = txtAlamat.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalLahir = sdf.format(jLahir.getDate());

        String query = "INSERT INTO siswa (nama_siswa, tanggal_lahir, nisn, no_hp, email, alamat) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, tanggalLahir);
            stmt.setString(3, nisn);
            stmt.setString(4, noHp);
            stmt.setString(5, email);
            stmt.setString(6, alamat);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data siswa berhasil ditambahkan.");
            clearForm();
            loadDataSiswa();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan siswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Update data siswa di database
     */
    private void ubahSiswa() {
        int selectedRow = tblSiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data siswa yang akan diubah terlebih dahulu.");
            return;
        }

        String id = tblSiswa.getValueAt(selectedRow, 0).toString();
        String nama = txtNama.getText();
        String nisn = txtNisn.getText();
        String noHp = txtTelpon.getText();
        String email = txtEmail.getText();
        String alamat = txtAlamat.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalLahir = sdf.format(jLahir.getDate());

        String query = "UPDATE siswa SET nama_siswa = ?, tanggal_lahir = ?, nisn = ?, no_hp = ?, email = ?, alamat = ? WHERE id_siswa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, tanggalLahir);
            stmt.setString(3, nisn);
            stmt.setString(4, noHp);
            stmt.setString(5, email);
            stmt.setString(6, alamat);
            stmt.setString(7, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data siswa berhasil diubah.");
            clearForm();
            loadDataSiswa();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah siswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Hapus data siswa dari database
     */
    private void hapusSiswa() {
        int selectedRow = tblSiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data siswa yang akan dihapus terlebih dahulu.");
            return;
        }

        String id = tblSiswa.getValueAt(selectedRow, 0).toString();

        String query = "DELETE FROM siswa WHERE id_siswa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data siswa berhasil dihapus.");
            clearForm();
            loadDataSiswa();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus siswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Membersihkan form input
     */
    private void clearForm() {
        txtNama.setText("");
        txtNisn.setText("");
        txtTelpon.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
        jLahir.setDate(null);
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
        lblNama = new javax.swing.JLabel();
        lblTanggalLahir = new javax.swing.JLabel();
        lblTelpon = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtTelpon = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        btnTambahSiswa = new javax.swing.JButton();
        btnUbahSiswa = new javax.swing.JButton();
        btnHapusSiswa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        lblAlamat = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        lblNisn = new javax.swing.JLabel();
        txtNisn = new javax.swing.JTextField();
        jLahir = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();

        setTitle("Siswa");
        setPreferredSize(new java.awt.Dimension(888, 700));

        jPanel1.setBackground(new java.awt.Color(51, 255, 0));

        lblNama.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblNama.setText("Nama Lengkap:");

        lblTanggalLahir.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblTanggalLahir.setText("Tanggal Lahir");

        lblTelpon.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblTelpon.setText("No. Hp:");

        lblEmail.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblEmail.setText("Email:");

        btnTambahSiswa.setBackground(new java.awt.Color(0, 204, 51));
        btnTambahSiswa.setText("Tambah");
        btnTambahSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahSiswaActionPerformed(evt);
            }
        });

        btnUbahSiswa.setBackground(new java.awt.Color(204, 153, 255));
        btnUbahSiswa.setText("Ubah");
        btnUbahSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahSiswaActionPerformed(evt);
            }
        });

        btnHapusSiswa.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusSiswa.setText("Hapus");
        btnHapusSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusSiswaActionPerformed(evt);
            }
        });

        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama", "TTL", "NISN", "No. Telp", "Email", "Alamat"
            }
        ));
        tblSiswa.setToolTipText("Daftar Siswa");
        jScrollPane1.setViewportView(tblSiswa);

        lblAlamat.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblAlamat.setText("Alamat:");

        lblNisn.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblNisn.setText("NISN:");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Tabel Siswa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNama)
                    .addComponent(lblTanggalLahir)
                    .addComponent(lblAlamat)
                    .addComponent(lblNisn)
                    .addComponent(lblTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail)
                    .addComponent(txtTelpon)
                    .addComponent(txtNisn)
                    .addComponent(txtAlamat)
                    .addComponent(txtNama)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(58, 58, 58))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambahSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUbahSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btnHapusSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTanggalLahir)
                    .addComponent(jLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNisn)
                    .addComponent(txtNisn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelpon)
                    .addComponent(txtTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlamat)
                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHapusSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(btnUbahSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUbahSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahSiswaActionPerformed
        ubahSiswa();
    }//GEN-LAST:event_btnUbahSiswaActionPerformed

    private void btnTambahSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahSiswaActionPerformed
        tambahSiswa();
    }//GEN-LAST:event_btnTambahSiswaActionPerformed

    private void btnHapusSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusSiswaActionPerformed
        hapusSiswa();
    }//GEN-LAST:event_btnHapusSiswaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusSiswa;
    private javax.swing.JButton btnTambahSiswa;
    private javax.swing.JButton btnUbahSiswa;
    private javax.swing.JLabel jLabel1;
    private com.toedter.calendar.JDateChooser jLahir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNisn;
    private javax.swing.JLabel lblTanggalLahir;
    private javax.swing.JLabel lblTelpon;
    private javax.swing.JTable tblSiswa;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNisn;
    private javax.swing.JTextField txtTelpon;
    // End of variables declaration//GEN-END:variables
}
