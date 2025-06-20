-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 20, 2025 at 09:40 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `game_scores_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `thasil`
--

CREATE TABLE `thasil` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `skor` int(11) NOT NULL DEFAULT 0,
  `count` int(11) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `thasil`
--

INSERT INTO `thasil` (`id`, `username`, `skor`, `count`, `created_at`, `updated_at`) VALUES
(1, 'NalarJalan', 1000, 100, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(2, 'UseYourLogic', 800, 80, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(3, 'NoJudgement', 700, 40, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(4, 'EkoPrasetyo', 950, 95, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(5, 'Fitriani', 1320, 94, '2025-06-19 12:39:10', '2025-06-19 13:20:55'),
(6, 'GatotKaca', 750, 70, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(7, 'DewiSartika', 650, 60, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(8, 'Cahya', 500, 50, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(9, 'BayuSamudra', 450, 45, '2025-06-19 12:39:10', '2025-06-19 12:39:10'),
(10, 'AnisaRahma', 710, 44, '2025-06-19 12:39:10', '2025-06-19 18:21:48'),
(11, 'Helmi', 690, 14, '2025-06-19 13:55:20', '2025-06-19 16:40:08'),
(12, 'Anisa', 630, 13, '2025-06-19 14:47:26', '2025-06-19 18:01:22'),
(13, 'Varel', 630, 15, '2025-06-19 14:57:13', '2025-06-19 18:45:15'),
(14, 'Roger', 450, 10, '2025-06-19 15:05:18', '2025-06-19 16:55:34'),
(15, 'Farhan', 200, 5, '2025-06-19 18:34:40', '2025-06-19 18:34:40'),
(16, 'Gani', 270, 4, '2025-06-19 19:04:59', '2025-06-19 19:04:59');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `thasil`
--
ALTER TABLE `thasil`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `thasil`
--
ALTER TABLE `thasil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
