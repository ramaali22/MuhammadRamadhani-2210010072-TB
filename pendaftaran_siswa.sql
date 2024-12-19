-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 19, 2024 at 02:03 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pendaftaran_siswa`
--

-- --------------------------------------------------------

--
-- Table structure for table `jurusan`
--

CREATE TABLE `jurusan` (
  `id_jurusan` int(11) NOT NULL,
  `nama_jurusan` varchar(100) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jurusan`
--

INSERT INTO `jurusan` (`id_jurusan`, `nama_jurusan`, `deskripsi`) VALUES
(1, 'Ilmu Pengetahuan Alam', 'IPA'),
(2, 'Ilmu Pengetahuan Alam', 'IPA'),
(3, 'Ilmu Pengetahuan Alam', 'IPA'),
(4, 'Ilmu Pengetahuan Alam', 'IPA'),
(5, 'Ilmu Pengetahuan Alam', 'IPA'),
(6, 'Ilmu Pengetahuan Sosial', 'IPS'),
(7, 'Ilmu Pengetahuan Sosial', 'IPS'),
(8, 'Ilmu Pengetahuan Sosial', 'IPS'),
(9, 'Bahasa Indonesia, bahasa daerah, dan bahasa asing', 'BAHASA INDONESIA'),
(10, 'Bahasa Indonesia, bahasa daerah, dan bahasa asing', 'BAHASA INDONESIA'),
(11, 'Ilmu Matematika Dasar', 'MATEMATIKA'),
(12, 'Ilmu Matematika Dasar', 'MATEMATIKA'),
(13, 'Ilmu Matematika Dasar', 'MATEMATIKA'),
(14, 'Ilmu Matematika Dasar', 'MATEMATIKA'),
(15, 'Ilmu Pengetahuan Sosial', 'IPS'),
(16, 'Ilmu Pengetahuan Sosial', 'IPS'),
(17, 'Bahasa Indonesia, bahasa daerah, dan bahasa asing', 'BAHASA INDONESIA'),
(18, 'Bahasa Indonesia, bahasa daerah, dan bahasa asing', 'BAHASA INDONESIA'),
(19, 'Ilmu Matematika Dasar', 'MATEMATIKA'),
(20, 'Bahasa Indonesia, bahasa daerah, dan bahasa asing', 'BAHASA INDONESIA');

-- --------------------------------------------------------

--
-- Table structure for table `pendaftaran`
--

CREATE TABLE `pendaftaran` (
  `id_pendaftaran` int(11) NOT NULL,
  `id_siswa` int(11) DEFAULT NULL,
  `id_jurusan` int(11) DEFAULT NULL,
  `tanggal` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pendaftaran`
--

INSERT INTO `pendaftaran` (`id_pendaftaran`, `id_siswa`, `id_jurusan`, `tanggal`) VALUES
(1, 1, 1, '2024-12-14'),
(2, 2, 1, '2024-12-14'),
(3, 3, 1, '2024-12-15'),
(4, 4, 7, '2024-12-15'),
(5, 5, 6, '2024-12-15'),
(6, 6, 11, '2024-12-16');

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `id_siswa` int(11) NOT NULL,
  `nama_siswa` varchar(100) DEFAULT NULL,
  `tanggal_lahir` date DEFAULT NULL,
  `alamat` varchar(200) DEFAULT NULL,
  `no_hp` varchar(15) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `nisn` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`id_siswa`, `nama_siswa`, `tanggal_lahir`, `alamat`, `no_hp`, `email`, `nisn`) VALUES
(1, 'Muhammad Ramadhani', '2002-11-22', 'Martapura', '081257881091', 'rmarmaali@gmail.com', '2210010072'),
(2, 'Aisyah Nurhaliza', '2003-05-15', 'Banjarmasin', '085678901234', 'aisyah.nurh@gmail.com', '2210010073'),
(3, 'Ahmad Fauzi', '2002-09-07', 'Banjarbaru', '087654321098', 'ahmad.fauzi@gmail.com', '2210010074'),
(4, 'Siti Khadijah', '2003-12-02', 'Kotabaru', '082345678901', 'sis.khadijah@gmail.com', '2210010075'),
(5, 'Rizki Kurniawan', '2002-07-30', 'Kandangan', '089012345678', 'rizki.kurniawan@gmail.com', '2210010076'),
(6, 'Nur Aliya', '2003-01-18', 'Amuntai', '081234567890', 'nur.aliya@gmail.com', '2210010077'),
(7, 'Muhammad Arifin', '2002-12-05', 'Alabio', '087890123456', 'muhammad.arifin@gmail.com', '2210010078'),
(8, 'Dewi Sartika', '2003-06-22', 'Marabahan', '085432109876', 'dewi.sartika@gmail.com', '2210010079'),
(9, 'Harun Arrasyid', '2002-10-11', 'Kotabaru', '082567890123', 'harun.arrasyid@gmail.com', '2210010080'),
(10, 'Zahra Amelia', '2003-03-27', 'Rantau', '089876543210', 'zahra.amelia@gmail.com', '2210010081'),
(11, 'Budi Setiawan', '2002-08-16', 'Barabai', '081345678901', 'budi.setiawan@gmail.com', '2210010082'),
(12, 'Rini Oktavia', '2003-04-09', 'Tanjung', '087654321099', 'rini.oktavia@gmail.com', '2210010083'),
(13, 'Dian Permana', '2002-06-03', 'Paringin', '085678901235', 'dian.permana@gmail.com', '2210010084'),
(14, 'Fitri Handayani', '2003-01-25', 'Bajangan', '082987654321', 'fitri.handayani@gmail.com', '2210010085'),
(15, 'Yusuf Mansur', '2002-11-30', 'Kuangan', '089123456789', 'yusuf.mansur@gmail.com', '2210010086');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jurusan`
--
ALTER TABLE `jurusan`
  ADD PRIMARY KEY (`id_jurusan`);

--
-- Indexes for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD PRIMARY KEY (`id_pendaftaran`),
  ADD KEY `id_siswa` (`id_siswa`),
  ADD KEY `id_jurusan` (`id_jurusan`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id_siswa`),
  ADD UNIQUE KEY `nisn` (`nisn`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jurusan`
--
ALTER TABLE `jurusan`
  MODIFY `id_jurusan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  MODIFY `id_pendaftaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `siswa`
--
ALTER TABLE `siswa`
  MODIFY `id_siswa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD CONSTRAINT `pendaftaran_ibfk_1` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id_siswa`),
  ADD CONSTRAINT `pendaftaran_ibfk_2` FOREIGN KEY (`id_jurusan`) REFERENCES `jurusan` (`id_jurusan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
