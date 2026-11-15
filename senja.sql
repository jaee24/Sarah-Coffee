-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 16, 2026 at 06:30 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `senja`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `tanggal` varchar(10) NOT NULL,
  `shift_jenis` varchar(1) NOT NULL,
  `masuk` varchar(5) DEFAULT NULL,
  `pulang` varchar(5) DEFAULT NULL,
  `status` varchar(10) NOT NULL CHECK (`status` in ('hadir','terlambat','alpa')),
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id`, `user_id`, `tanggal`, `shift_jenis`, `masuk`, `pulang`, `status`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('09566418-0828-3f45-a407-680a88b3cb05', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-08', 'P', '09:53', '16:33', 'hadir', 1791661371565, 1791661371565, 0, 0),
('12c603c9-29c6-3351-a35f-66127039c0f0', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-07', 'S', '16:24', '23:04', 'hadir', 1791661371565, 1791661371565, 0, 0),
('13253959-a9db-312a-84ad-96f29d306310', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-09', 'S', '16:22', '23:02', 'hadir', 1791661371565, 1791661371565, 0, 0),
('280a26d6-c0ff-3e15-8783-edc63b80585e', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-09', 'P', '09:52', '16:32', 'hadir', 1791661371563, 1791661371563, 0, 0),
('48e4531c-deed-3940-b622-900ce3e133e4', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-05', 'P', '09:56', '16:30', 'hadir', 1791661371566, 1791661371566, 0, 0),
('5060f8a6-5c97-3b39-ba7d-3f841d98e3a7', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-08', 'S', '16:23', '23:03', 'hadir', 1791661371565, 1791661371565, 0, 0),
('a2ed6d6e-a22a-3871-b3ec-35baabd78680', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-10', 'P', '09:51', '16:31', 'hadir', 1791661371563, 1791661371563, 0, 0),
('b813c6ef-5b02-3850-9d39-785f677f3fd4', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-07', 'P', '09:54', '16:34', 'hadir', 1791661371565, 1791661371565, 0, 0),
('bab3da10-e37c-3799-b0a1-b5e9a3a809ff', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-05', 'S', '16:26', '23:00', 'hadir', 1791661371566, 1791661371566, 0, 0),
('bc8688cf-39a9-397d-8dba-362fab054f15', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-06', 'S', '16:25', '23:05', 'hadir', 1791661371566, 1791661371566, 0, 0),
('cbb122f5-05f2-3cb2-a3ea-1de086f253a9', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-10', 'S', '16:21', '23:01', 'hadir', 1791661371563, 1791661371563, 0, 0),
('f7802eea-4e38-39ab-a4ae-f7384edf4376', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-06', 'P', '09:55', '16:35', 'hadir', 1791661371566, 1791661371566, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `auth_tokens`
--

CREATE TABLE `auth_tokens` (
  `token_hash` char(64) NOT NULL,
  `user_id` char(36) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `expires_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `auth_tokens`
--

INSERT INTO `auth_tokens` (`token_hash`, `user_id`, `created_at`, `expires_at`) VALUES
('0dd8624c7721b0a5fee070635dee1db85d7895d55e91ae0a95da5be0b49742cc', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791880712267, 1791923912267),
('124733315aa1d220987d978f8feda17cc5b8fdd1e1978dec85525147d736556c', '4f751928-7886-3558-8bb4-684636d79d5b', 1791673370790, 1791716570790),
('2c53ef1c48c40624ee4075bb77ba45bafd3d5d51a3b4bf6c04e2fdf78b5dfcc0', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791671026985, 1791714226985),
('2d357a225d8b91798ed02deb47caf682fdf5abf89ed818c2c1efeb4653d3f759', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791672298841, 1791715498841),
('340e43f83a0d59ce6ad4b7c9170f0a9c3a24acc465df2f2b81da00fc124a62b5', '4f751928-7886-3558-8bb4-684636d79d5b', 1791671932541, 1791715132541),
('3679a51bf18f58fe7f7f8ae3927660de41a2cbef6518da651c23336e71c4d118', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791672218307, 1791715418307),
('3933cadd4028a3dd5438118a7480afef684ae66a5fd3bb78fe232d6922024a24', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791671932121, 1791715132121),
('4ce14e19a1fd8b41b4e6dd9bd3352fc0d39fc0ae35ea2eaa239eee877604df9c', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791881573542, 1791924773542),
('586050e2d2d1b04b72e7f9d9e06e7fc86b794ada37a73325005a7ea83103f5bd', '4f751928-7886-3558-8bb4-684636d79d5b', 1791671027431, 1791714227431),
('595c7a0fd4c5b2d5ebf64b5f7d111b6ff6f4d159bbd99c88cac772dc2405b445', '4f751928-7886-3558-8bb4-684636d79d5b', 1791671593611, 1791714793611),
('5e9c265253ea6c9b4e5fd3d43442fefeca3faa89105d2efba05774f78a38a765', '4f751928-7886-3558-8bb4-684636d79d5b', 1791670013452, 1791713213452),
('5ee96649daad934f8d348cb5ffcfac312b1121a8c7db3389af5f21d0c560f2f4', '4f751928-7886-3558-8bb4-684636d79d5b', 1791671219905, 1791714419905),
('8996e67789235c05ae6e6c2e865dcec0bb592b9e1433fa651c67ee65d4d5700e', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791881217935, 1791924417935),
('8c5b6d9adac358b7bfae743b21f6904f6173dcd2d5362ab002f546322462fda2', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791671593150, 1791714793150),
('a3e4722fb75d837559da73474dc01dc75641fb99fd3872acb17e6386fb1ff4c1', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791671059180, 1791714259180),
('b4fee866a04ba155eac08878062efd16998072546e2030dfe156f51317acdd89', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791671219606, 1791714419606),
('c1a728c19a8b89444875410ac7fcc525f12d5c75e4fa38ece8d054352a22ef68', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370317, 1791716570317),
('f827d5c1ed7443a5b8cb68c91145ca5f34138ee3b016f6b0ab39d68d609d5b00', '4f751928-7886-3558-8bb4-684636d79d5b', 1791672299154, 1791715499154),
('feadb8bde3b4b5d35c90321f9513889f4932abc2390c42b0cce25f8fc1d66121', '4f751928-7886-3558-8bb4-684636d79d5b', 1791671059511, 1791714259511),
('ff18b27a9ba4a43b314f962767fcf784935ce500c92fe89e969fd463a9dd596e', '4f751928-7886-3558-8bb4-684636d79d5b', 1791672218711, 1791715418711);

-- --------------------------------------------------------

--
-- Table structure for table `bahan`
--

CREATE TABLE `bahan` (
  `id` char(36) NOT NULL,
  `kode` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `satuan` varchar(10) NOT NULL,
  `stok` double NOT NULL DEFAULT 0,
  `stok_min` double NOT NULL DEFAULT 0,
  `harga` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bahan`
--

INSERT INTO `bahan` (`id`, `kode`, `nama`, `satuan`, `stok`, `stok_min`, `harga`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 'BB-18', 'Pisang Raja', 'pcs', 132, 40, 2000, 1791661210149, 1791661210149, 0, 0),
('212e97d1-2113-3d95-9afd-2bb3e00b55ae', 'BB-16', 'Es Batu', 'gram', 25000, 6000, 1, 1791661210149, 1791661210149, 0, 0),
('22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'BB-08', 'Air Mineral', 'ml', 52870, 15000, 1, 1791661210149, 1791661210149, 0, 0),
('37e189d8-30d3-3476-926a-4b826125bf34', 'BB-19', 'Gula Aren', 'gram', 1875, 500, 35, 1791661210149, 1791661210149, 0, 0),
('3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'BB-01', 'Kopi Arabica', 'gram', 2656, 500, 130, 1791661210145, 1791661210145, 0, 0),
('452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'BB-20', 'Minyak Goreng', 'ml', 6850, 2000, 3, 1791661210149, 1791661210149, 0, 0),
('567af7ad-36d6-37b0-95d5-da5e41a65262', 'BB-04', 'Gula Pasir', 'gram', 5550, 1500, 15, 1791661210147, 1791661210147, 0, 0),
('5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'BB-09', 'Susu Kental Manis', 'gram', 5780, 1000, 40, 1791661210149, 1791661210149, 0, 0),
('5f991edd-bb70-3cb9-b368-02a93b901562', 'BB-03', 'Susu Segar', 'ml', 14640, 4000, 18, 1791661210147, 1791661210147, 0, 0),
('62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'BB-15', 'Patty Ayam', 'pcs', 43, 20, 6500, 1791661210149, 1791661210149, 0, 0),
('6ec304f4-f5dc-3385-9e6b-b2a197288824', 'BB-06', 'Bubuk Matcha', 'gram', 816, 250, 340, 1791661210147, 1791661210147, 0, 0),
('6f5593e6-91c1-32d9-91b2-5dafb6717996', 'BB-14', 'Bun Burger', 'pcs', 33, 15, 3500, 1791661210149, 1791661210149, 0, 0),
('7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'BB-17', 'Bubuk Teh Hitam', 'gram', 514, 180, 90, 1791661210149, 1791661210149, 0, 0),
('794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'BB-11', 'Tepung Terigu', 'gram', 4020, 2000, 12, 1791661210149, 1791661210149, 0, 0),
('853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'BB-10', 'Telur', 'pcs', 78.25, 24, 2500, 1791661210149, 1791661210149, 0, 0),
('aba285d6-e49b-3ccd-964c-be5824ff3afd', 'BB-21', 'Kentang Beku', 'gram', 2400, 1500, 28, 1791661210154, 1791661210154, 0, 0),
('b281d40a-f934-3888-b428-9bf35178ef2a', 'BB-13', 'Keju Cheddar', 'gram', 1160, 500, 110, 1791661210149, 1791661210149, 0, 0),
('bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'BB-02', 'Espresso Blend', 'gram', 2554, 500, 120, 1791661210145, 1791661210145, 0, 0),
('c62202be-fcf8-3c83-97bf-a119162d90c2', 'BB-07', 'Cokelat Bubuk', 'gram', 575, 300, 180, 1791661210149, 1791661210149, 0, 0),
('dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'BB-05', 'Sirup Caramel', 'ml', 2360, 600, 90, 1791661210147, 1791661210147, 0, 0),
('f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'BB-12', 'Mentega', 'gram', 1662, 600, 45, 1791661210149, 1791661210149, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `devices`
--

CREATE TABLE `devices` (
  `id` char(36) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `tipe` varchar(10) NOT NULL,
  `api_key_hash` char(64) NOT NULL,
  `last_seen` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`id`, `nama`, `tipe`, `api_key_hash`, `last_seen`, `created_at`) VALUES
('04de79ba-187a-4b46-8a33-d9897baf157a', 'PC-Kasir-1', 'kasir', 'e2dcd2fd0ef550d1fa6f2d11fe0bc62d6f4d9011a30b7b0e2cb3a3d0c12f023e', 1791657882025, 1791657882025),
('81adaa1b-642b-4de7-8f4f-8acaee4019a8', 'Poodyng-admin', 'admin', '0210769d29b6ca89c250c60434caf343d511026187ef38901e4695275797fad5', 1791881654290, 1791661372205);

-- --------------------------------------------------------

--
-- Table structure for table `diskon`
--

CREATE TABLE `diskon` (
  `id` char(36) NOT NULL,
  `kode` varchar(20) NOT NULL,
  `tipe` varchar(10) NOT NULL CHECK (`tipe` in ('persen','nominal')),
  `nilai` bigint(20) NOT NULL,
  `min_belanja` bigint(20) NOT NULL DEFAULT 0,
  `mulai` varchar(10) NOT NULL,
  `berakhir` varchar(10) NOT NULL,
  `status` varchar(10) NOT NULL CHECK (`status` in ('aktif','nonaktif')),
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1,
  `maks_potongan` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diskon`
--

INSERT INTO `diskon` (`id`, `kode`, `tipe`, `nilai`, `min_belanja`, `mulai`, `berakhir`, `status`, `created_at`, `updated_at`, `is_deleted`, `dirty`, `maks_potongan`) VALUES
('013ef5cd-041f-3674-8bd9-233081ee8315', 'SENJA10', 'persen', 10, 30000, '2026-09-11', '2026-12-10', 'aktif', 1791661210167, 1791661210167, 0, 0, NULL),
('29141baa-d8a2-3f4b-a0c8-c595d54c692c', 'PROMO20', 'persen', 20, 50000, '2026-07-13', '2026-10-06', 'aktif', 1791661210167, 1791661210167, 0, 0, NULL),
('db3c5437-98eb-3d93-9e51-d8e2ea34b100', 'HEMAT5K', 'nominal', 5000, 25000, '2026-10-01', '2026-11-10', 'aktif', 1791661210167, 1791661210167, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id` char(36) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jenis` varchar(10) NOT NULL CHECK (`jenis` in ('minuman','makanan')),
  `penyajian` varchar(50) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id`, `nama`, `jenis`, `penyajian`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('3de222f3-e4d1-3cc6-b2db-1479998b522e', 'Food', 'makanan', '[]', 1791661210145, 1791661210145, 0, 0),
('9b9e94b9-0b93-349f-b278-5cad34d8ab86', 'Non-Coffee', 'minuman', '[\"hot\",\"cold\"]', 1791661210145, 1791661210145, 0, 0),
('adf0b349-8a92-3887-850f-849c8758ff54', 'Cemilan', 'makanan', '[]', 1791661210145, 1791661210145, 0, 0),
('ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', 'Coffee', 'minuman', '[\"hot\",\"cold\"]', 1791661210144, 1791661210144, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `laporan_bug`
--

CREATE TABLE `laporan_bug` (
  `id` char(36) NOT NULL,
  `app` varchar(10) NOT NULL CHECK (`app` in ('admin','kasir')),
  `user_id` char(36) DEFAULT NULL,
  `judul` varchar(150) NOT NULL,
  `deskripsi` varchar(1000) DEFAULT NULL,
  `waktu` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pembelian`
--

CREATE TABLE `pembelian` (
  `id` char(36) NOT NULL,
  `kode` varchar(12) NOT NULL,
  `tanggal` varchar(10) NOT NULL,
  `supplier_id` char(36) NOT NULL,
  `user_id` char(36) DEFAULT NULL,
  `total` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pembelian`
--

INSERT INTO `pembelian` (`id`, `kode`, `tanggal`, `supplier_id`, `user_id`, `total`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('4bbaf1bc-4b79-3410-b635-f4f39caa5c16', 'PO-001', '2026-10-02', 'fc9a8b77-197a-3abe-92a5-0fec9daccab2', '4f751928-7886-3558-8bb4-684636d79d5b', 266600, 1791661371546, 1791661371546, 0, 0),
('c81e08f2-bd3c-3ee0-b9e0-84c2e24f945a', 'PO-002', '2026-10-06', '788c53d5-7e2f-36b4-a906-4683d72fbb68', '4f751928-7886-3558-8bb4-684636d79d5b', 246000, 1791661371548, 1791661371548, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pembelian_detail`
--

CREATE TABLE `pembelian_detail` (
  `id` char(36) NOT NULL,
  `pembelian_id` char(36) NOT NULL,
  `bahan_id` char(36) NOT NULL,
  `jumlah` double NOT NULL,
  `harga` bigint(20) NOT NULL,
  `subtotal` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pembelian_detail`
--

INSERT INTO `pembelian_detail` (`id`, `pembelian_id`, `bahan_id`, `jumlah`, `harga`, `subtotal`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('01e360c0-2c70-365e-af4d-a9407a81df30', 'c81e08f2-bd3c-3ee0-b9e0-84c2e24f945a', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 2000, 38, 76000, 1791661371548, 1791661371548, 0, 0),
('63c588e7-a2aa-3a4a-9f6b-202f11b4a91e', '4bbaf1bc-4b79-3410-b635-f4f39caa5c16', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 1200, 118, 141600, 1791661371548, 1791661371548, 0, 0),
('f2410373-eeb1-3c88-b008-2b2ac6dda644', '4bbaf1bc-4b79-3410-b635-f4f39caa5c16', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 1000, 125, 125000, 1791661371548, 1791661371548, 0, 0),
('faf72b97-4f82-31d1-9b8d-4836fcf8896b', 'c81e08f2-bd3c-3ee0-b9e0-84c2e24f945a', '5f991edd-bb70-3cb9-b368-02a93b901562', 10000, 17, 170000, 1791661371548, 1791661371548, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pengaturan`
--

CREATE TABLE `pengaturan` (
  `id` char(36) NOT NULL,
  `kunci` varchar(50) NOT NULL,
  `nilai` varchar(300) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengaturan`
--

INSERT INTO `pengaturan` (`id`, `kunci`, `nilai`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('09f8da95-459c-3431-96a9-79b2c18d2a0c', 'toko_alamat', 'Jl. Kenanga No. 12, Yogyakarta', 1791661210168, 1791661210168, 0, 0),
('24956023-973f-379c-ab57-5d84453ba0ac', 'theme', 'light', 1791661210168, 1791881645258, 0, 0),
('4ed6e4c9-01c1-3f8c-9776-2b24b4617135', 'lang', 'id', 1791661210168, 1791661210168, 0, 0),
('a23ea137-3e79-3599-b57d-12da245ecf5b', 'toko_telp', '0812-3456-7890', 1791661210168, 1791661210168, 0, 0),
('d6ed3897-38fa-3679-bb7c-d85f774e3e04', 'toko_nama', 'SENJA COFFEE', 1791661210168, 1791661210168, 0, 0),
('f7034ea1-9b91-33da-a1aa-ca3d0f4e5a4b', 'struk_footer', 'Terima kasih sudah mampir!', 1791661210168, 1791661210168, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pengeluaran`
--

CREATE TABLE `pengeluaran` (
  `id` char(36) NOT NULL,
  `kode` varchar(12) NOT NULL,
  `tanggal` varchar(10) NOT NULL,
  `keterangan` varchar(200) NOT NULL,
  `kategori` varchar(20) NOT NULL,
  `jenis` varchar(15) NOT NULL CHECK (`jenis` in ('rutin','tak_terduga')),
  `jumlah` bigint(20) NOT NULL,
  `bukti` mediumblob DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengeluaran`
--

INSERT INTO `pengeluaran` (`id`, `kode`, `tanggal`, `keterangan`, `kategori`, `jenis`, `jumlah`, `bukti`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('0ce92fcf-5a62-3ee8-8bd4-400bf270aa7e', 'EXP-004', '2026-10-10', 'Servis mesin espresso & ganti seal', 'Perawatan', 'tak_terduga', 275000, NULL, 1791661371548, 1791661371548, 0, 0),
('64b07209-05ba-3446-b729-29253d186936', 'EXP-003', '2026-10-08', 'Bonus penjualan tim', 'Lainnya', 'rutin', 300000, NULL, 1791661371548, 1791661371548, 0, 0),
('79e3238a-a0a7-3147-b302-5816846084db', 'EXP-001', '2026-10-05', 'Pembayaran token listrik & air', 'Operasional', 'rutin', 450000, NULL, 1791661371548, 1791661371548, 0, 0),
('cb69e265-ccc9-3454-9ce0-5a1d24ee7654', 'EXP-002', '2026-10-07', 'Isi ulang LPG 2 tabung', 'Operasional', 'rutin', 340000, NULL, 1791661371548, 1791661371548, 0, 0),
('e9e93529-d8c9-355b-8bf3-59cca6bf27af', 'EXP-005', '2026-10-11', 'Sample biji kopi baru', 'Operasional', 'rutin', 150000, NULL, 1791661371548, 1791661371548, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id` char(36) NOT NULL,
  `kode` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `kategori_id` char(36) NOT NULL,
  `penyajian` varchar(50) DEFAULT NULL,
  `opsi` varchar(150) DEFAULT NULL,
  `harga` bigint(20) NOT NULL,
  `status` varchar(10) NOT NULL CHECK (`status` in ('aktif','nonaktif')),
  `gambar` varchar(300) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id`, `kode`, `nama`, `kategori_id`, `penyajian`, `opsi`, `harga`, `status`, `gambar`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('0f28aa41-2867-3062-b27c-223b666b18ed', 'PR-06', 'Matcha Latte', '9b9e94b9-0b93-349f-b278-5cad34d8ab86', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 26000, 'aktif', 'https://picsum.photos/seed/senja-06/600/450.jpg', 1791661210159, 1791661210159, 0, 0),
('1f7ceb6f-8278-301d-ba60-31651db59cfc', 'PR-02', 'Caramel Latte', 'ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 28000, 'aktif', 'https://picsum.photos/seed/senja-02/600/450.jpg', 1791661210158, 1791661210158, 0, 0),
('2366d505-feda-3c55-9854-f16cdcff382f', 'PR-14', 'Donat Gula', 'adf0b349-8a92-3887-850f-849c8758ff54', '[]', '[]', 12000, 'aktif', 'https://picsum.photos/seed/senja-14/600/450.jpg', 1791661210165, 1791661210165, 0, 0),
('49badec4-c17e-3a3a-85ea-6813e5fa504b', 'PR-01', 'Kopi Susu Gula Aren', 'ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 22000, 'aktif', 'https://picsum.photos/seed/senja-01/600/450.jpg', 1791661210154, 1791661210154, 0, 0),
('4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'PR-03', 'Americano', 'ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 18000, 'aktif', 'https://picsum.photos/seed/senja-03/600/450.jpg', 1791661210159, 1791661210159, 0, 0),
('4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'PR-07', 'Cokelat Hangat', '9b9e94b9-0b93-349f-b278-5cad34d8ab86', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 24000, 'aktif', 'https://picsum.photos/seed/senja-07/600/450.jpg', 1791661210159, 1791661210159, 0, 0),
('6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'PR-04', 'Cappuccino', 'ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 26000, 'aktif', 'https://picsum.photos/seed/senja-04/600/450.jpg', 1791661210159, 1791661210159, 0, 0),
('7cd624e1-6eb4-3253-a5c0-55948f1674af', 'PR-13', 'Pisang Goreng Keju', 'adf0b349-8a92-3887-850f-849c8758ff54', '[]', '[]', 22000, 'aktif', 'https://picsum.photos/seed/senja-13/600/450.jpg', 1791661210165, 1791661210165, 0, 0),
('853228d1-e232-3ada-aca6-150a97589f03', 'PR-09', 'Lemon Tea', '9b9e94b9-0b93-349f-b278-5cad34d8ab86', '[\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 16000, 'aktif', 'https://picsum.photos/seed/senja-09/600/450.jpg', 1791661210163, 1791661210163, 0, 0),
('8b3287f4-5e3c-305e-85ee-109af650647b', 'PR-11', 'Kentang Goreng', '3de222f3-e4d1-3cc6-b2db-1479998b522e', '[]', '[]', 20000, 'aktif', 'https://picsum.photos/seed/senja-11/600/450.jpg', 1791661210163, 1791661210163, 0, 0),
('94a1614e-041b-38ee-8d96-9624a0ea879a', 'PR-15', 'Cireng Bumbu Rujak', 'adf0b349-8a92-3887-850f-849c8758ff54', '[]', '[]', 15000, 'aktif', 'https://picsum.photos/seed/senja-15/600/450.jpg', 1791661210165, 1791661210165, 0, 0),
('9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'PR-16', 'Croissant Butter', 'adf0b349-8a92-3887-850f-849c8758ff54', '[]', '[]', 25000, 'aktif', 'https://picsum.photos/seed/senja-16/600/450.jpg', 1791661210167, 1791661210167, 0, 0),
('c28ddb25-9795-370b-b83b-16d127500814', 'PR-12', 'Roti Bakar Cokelat Keju', 'adf0b349-8a92-3887-850f-849c8758ff54', '[]', '[]', 24000, 'aktif', 'https://picsum.photos/seed/senja-12/600/450.jpg', 1791661210163, 1791661210163, 0, 0),
('c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'PR-05', 'Kopi Tubruk', 'ff16e32d-4ddc-3e78-b499-7ef6d7a5069a', '[\"hot\"]', '[\"regular\",\"less_sugar\",\"normal_sugar\"]', 12000, 'aktif', 'https://picsum.photos/seed/senja-05/600/450.jpg', 1791661210159, 1791661210159, 0, 0),
('d385d882-9a3e-3fba-a7d8-b10a5f74b022', 'PR-08', 'Teh Tarik', '9b9e94b9-0b93-349f-b278-5cad34d8ab86', '[\"hot\",\"cold\"]', '[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]', 18000, 'aktif', 'https://picsum.photos/seed/senja-08/600/450.jpg', 1791661210162, 1791661210162, 0, 0),
('d9ea31ec-4ea3-3452-8901-94f521317d3b', 'PR-10', 'Chicken Burger', '3de222f3-e4d1-3cc6-b2db-1479998b522e', '[]', '[]', 32000, 'aktif', 'https://picsum.photos/seed/senja-10/600/450.jpg', 1791661210163, 1791661210163, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `resep`
--

CREATE TABLE `resep` (
  `id` char(36) NOT NULL,
  `produk_id` char(36) NOT NULL,
  `bahan_id` char(36) NOT NULL,
  `jumlah` double NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep`
--

INSERT INTO `resep` (`id`, `produk_id`, `bahan_id`, `jumlah`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('07365363-5008-39c6-b146-67c1c6988c55', '0f28aa41-2867-3062-b27c-223b666b18ed', '567af7ad-36d6-37b0-95d5-da5e41a65262', 10, 1791661210159, 1791661210159, 0, 0),
('08c0e93b-b1ce-390d-9b2d-a9a779ecc0c5', '853228d1-e232-3ada-aca6-150a97589f03', '567af7ad-36d6-37b0-95d5-da5e41a65262', 15, 1791661210163, 1791661210163, 0, 0),
('140b1076-f264-3232-b587-8544c8b85d93', '94a1614e-041b-38ee-8d96-9624a0ea879a', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 40, 1791661210165, 1791661210165, 0, 0),
('208394da-36cd-359b-ad67-59f876928c8c', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 200, 1791661210159, 1791661210159, 0, 0),
('22923f90-9e1e-3871-adb7-a3acf10e72f7', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 12, 1791661210159, 1791661210159, 0, 0),
('22fa5485-9c1d-3881-aec3-efbd58821898', '853228d1-e232-3ada-aca6-150a97589f03', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 10, 1791661210163, 1791661210163, 0, 0),
('237dc7a9-9faf-351d-9456-c7bfc54acb1d', '7cd624e1-6eb4-3253-a5c0-55948f1674af', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 60, 1791661210165, 1791661210165, 0, 0),
('2386fb47-73c3-3e46-9cbb-2775c105d26a', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 10, 1791661210163, 1791661210163, 0, 0),
('2c4c15f8-4557-3ca5-9b81-b57f29edb19d', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 1, 1791661210163, 1791661210163, 0, 0),
('30bab7c9-2499-325e-ade3-ccc09d7b07a4', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 20, 1791661210159, 1791661210159, 0, 0),
('398d3d05-d5e0-36fe-8ca6-f9310a2999d7', 'c28ddb25-9795-370b-b83b-16d127500814', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 12, 1791661210165, 1791661210165, 0, 0),
('429f1b1e-12a7-376e-9f1a-ed57084cb11f', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 18, 1791661210158, 1791661210158, 0, 0),
('4f296fe3-d8bf-3b54-be31-b8012fd0eb3a', 'c28ddb25-9795-370b-b83b-16d127500814', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 15, 1791661210163, 1791661210163, 0, 0),
('52b1e660-9b21-3027-8f3c-866e2dbaf409', '7cd624e1-6eb4-3253-a5c0-55948f1674af', '0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 2, 1791661210165, 1791661210165, 0, 0),
('5601cae4-e458-36d8-a58a-82d56b96dac4', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 8, 1791661210162, 1791661210162, 0, 0),
('5a56c7ac-14e7-3443-9db8-ab1567f0e330', '0f28aa41-2867-3062-b27c-223b666b18ed', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 12, 1791661210159, 1791661210159, 0, 0),
('5bf9cc2e-2422-3e1b-9bf1-f9d98cc1315b', '853228d1-e232-3ada-aca6-150a97589f03', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 250, 1791661210163, 1791661210163, 0, 0),
('699169f9-68b3-3907-a723-83d8a594dd42', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 15, 1791661210163, 1791661210163, 0, 0),
('73ed09fb-bab6-3cc6-832c-54e0e407c6a8', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 1, 1791661210163, 1791661210163, 0, 0),
('7b0d7de7-1eb2-3b7f-a344-3b51b6a64476', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 18, 1791661210156, 1791661210156, 0, 0),
('80ee8057-6289-3bdb-acf1-7e71cbbadc99', '7cd624e1-6eb4-3253-a5c0-55948f1674af', 'b281d40a-f934-3888-b428-9bf35178ef2a', 20, 1791661210165, 1791661210165, 0, 0),
('88b9a7a0-a1bd-390f-93b0-0a82dd1fc2d3', '8b3287f4-5e3c-305e-85ee-109af650647b', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 200, 1791661210163, 1791661210163, 0, 0),
('8b90e101-4823-3cd4-81ac-baeaef7c1719', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', '5f991edd-bb70-3cb9-b368-02a93b901562', 120, 1791661210163, 1791661210163, 0, 0),
('9505bfd3-62d3-3b79-a2de-2be9999caa3f', '49badec4-c17e-3a3a-85ea-6813e5fa504b', '37e189d8-30d3-3476-926a-4b826125bf34', 25, 1791661210158, 1791661210158, 0, 0),
('98a9ee56-1ac9-3a8f-8f86-15621c56ef6d', '1f7ceb6f-8278-301d-ba60-31651db59cfc', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 60, 1791661210159, 1791661210159, 0, 0),
('a13c39b5-9b00-3f5d-b872-977a8aebd08e', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 40, 1791661210167, 1791661210167, 0, 0),
('a4a34081-45e6-310e-9741-5aab910309c1', '8b3287f4-5e3c-305e-85ee-109af650647b', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 30, 1791661210163, 1791661210163, 0, 0),
('a59e2e1f-ac27-399f-87e6-1bfdf3534907', '94a1614e-041b-38ee-8d96-9624a0ea879a', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 80, 1791661210165, 1791661210165, 0, 0),
('aacaff21-8099-3c51-8f2c-0e069d41f9f2', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', '5f991edd-bb70-3cb9-b368-02a93b901562', 150, 1791661210159, 1791661210159, 0, 0),
('ad0f2900-7f40-3e75-a02a-da5f30e2ffa0', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 220, 1791661210159, 1791661210159, 0, 0),
('b135276a-e798-324b-b0ad-c2785e4f4f5f', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 18, 1791661210159, 1791661210159, 0, 0),
('b5d22634-77d1-3265-bbaa-75e2e86959b4', 'c28ddb25-9795-370b-b83b-16d127500814', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 80, 1791661210163, 1791661210163, 0, 0),
('b8a8760e-0957-3cd1-af90-87c0fc576372', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 20, 1791661210159, 1791661210159, 0, 0),
('b8ef4f41-882f-3aa3-8ef1-762b0026df5e', '49badec4-c17e-3a3a-85ea-6813e5fa504b', '5f991edd-bb70-3cb9-b368-02a93b901562', 150, 1791661210158, 1791661210158, 0, 0),
('babccfa1-a77c-30a6-afc3-7a5fca8a9a84', '2366d505-feda-3c55-9854-f16cdcff382f', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 10, 1791661210165, 1791661210165, 0, 0),
('c76cc85d-2a40-3dc0-b427-a5cf949f6072', '2366d505-feda-3c55-9854-f16cdcff382f', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 0.25, 1791661210165, 1791661210165, 0, 0),
('ca00f3e3-cf1a-326f-8c38-fd5458277a71', '1f7ceb6f-8278-301d-ba60-31651db59cfc', '5f991edd-bb70-3cb9-b368-02a93b901562', 180, 1791661210159, 1791661210159, 0, 0),
('ca4a561c-ad53-3847-afa0-b86dbf2a4a6c', '2366d505-feda-3c55-9854-f16cdcff382f', '567af7ad-36d6-37b0-95d5-da5e41a65262', 15, 1791661210165, 1791661210165, 0, 0),
('caf23868-9689-31ce-99d2-1c3f324e620f', '94a1614e-041b-38ee-8d96-9624a0ea879a', '567af7ad-36d6-37b0-95d5-da5e41a65262', 10, 1791661210165, 1791661210165, 0, 0),
('cd5d9f58-c23f-3aea-97d5-4df1e726cdc0', 'c28ddb25-9795-370b-b83b-16d127500814', 'b281d40a-f934-3888-b428-9bf35178ef2a', 20, 1791661210163, 1791661210163, 0, 0),
('d3396949-7d51-343a-b915-ae058b35611c', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'b281d40a-f934-3888-b428-9bf35178ef2a', 15, 1791661210163, 1791661210163, 0, 0),
('d8172114-387c-3d43-a711-e11e543f5481', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', '5f991edd-bb70-3cb9-b368-02a93b901562', 140, 1791661210159, 1791661210159, 0, 0),
('e30660b6-ddc1-3191-9403-af6dac143237', '2366d505-feda-3c55-9854-f16cdcff382f', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 100, 1791661210165, 1791661210165, 0, 0),
('e9307b68-3f20-354f-a2d1-90cbe3137a3f', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 18, 1791661210159, 1791661210159, 0, 0),
('f28cf8d6-8852-3c43-b398-0581d6694699', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 20, 1791661210162, 1791661210162, 0, 0),
('f2b06747-daf5-3e86-9ca2-8b173fbfa0e3', '7cd624e1-6eb4-3253-a5c0-55948f1674af', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 50, 1791661210165, 1791661210165, 0, 0),
('f2bff549-85c8-389f-9e2f-e45f75b3ef34', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 100, 1791661210167, 1791661210167, 0, 0),
('f45fd359-d15f-376b-9a6e-ef5f45f63280', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 50, 1791661210163, 1791661210163, 0, 0),
('fde33656-81ca-30c3-827c-f87c10b6012d', '0f28aa41-2867-3062-b27c-223b666b18ed', '5f991edd-bb70-3cb9-b368-02a93b901562', 150, 1791661210159, 1791661210159, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `shift`
--

CREATE TABLE `shift` (
  `id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `tanggal` varchar(10) NOT NULL,
  `jenis` varchar(1) NOT NULL CHECK (`jenis` in ('P','S')),
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shift`
--

INSERT INTO `shift` (`id`, `user_id`, `tanggal`, `jenis`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('0157349f-badf-3993-ac69-99965bc618b5', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-16', 'P', 1791661371557, 1791661371557, 0, 0),
('052aab21-ba39-31dc-a985-67e845717c57', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-23', 'P', 1791661371562, 1791661371562, 0, 0),
('0699340d-6a93-395c-845d-f91916cf8e37', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-13', 'S', 1791661371557, 1791661371557, 0, 0),
('16982184-6da1-3726-bf7c-19ccfafae950', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-08', 'S', 1791661371557, 1791661371557, 0, 0),
('1c1def8e-3b27-373c-80af-ab576f63d9a1', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-06', 'S', 1791661371557, 1791661371557, 0, 0),
('1d2c7be4-3721-369f-b4f3-5573bf183c4a', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-22', 'P', 1791661371562, 1791661371562, 0, 0),
('233129bb-246a-3f9b-96d5-39f40c31325a', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-20', 'P', 1791661371557, 1791661371557, 0, 0),
('256a28fe-bc74-3dd4-bc6b-03f0aa0edbf3', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-17', 'S', 1791661371557, 1791661371557, 0, 0),
('3060957b-05f7-3e04-8d0c-4c3d5d58f29e', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-13', 'P', 1791661371557, 1791661371557, 0, 0),
('31ff2617-3115-3d37-9ac3-87068a44f5bf', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-05', 'P', 1791661371548, 1791661371548, 0, 0),
('3e799301-d863-38a2-bb72-305f5b9f0ab7', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-10', 'S', 1791661371557, 1791661371557, 0, 0),
('55c2e2ca-8d67-3957-923b-72009bc72173', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-17', 'P', 1791661371557, 1791661371557, 0, 0),
('586b67ef-6c08-3a5a-9db6-8f35f3f8a845', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-05', 'S', 1791661371557, 1791661371557, 0, 0),
('5df75bff-3ae1-34f1-ad1a-af0237dee968', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-15', 'P', 1791661371557, 1791661371557, 0, 0),
('5fefd370-8bcb-3b0a-a8c9-fe4bd73af5fd', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-19', 'S', 1791661371557, 1791661371557, 0, 0),
('686da092-b4dc-3d82-a0f8-a076b46bc4c4', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-12', 'P', 1791661371557, 1791661371557, 0, 0),
('68b956a6-2cc8-3d16-b351-a32069041b58', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-11', 'S', 1791661371557, 1791666319208, 0, 0),
('6e4cef5e-6f54-3857-bc22-0258ae02119a', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-19', 'P', 1791661371557, 1791661371557, 0, 0),
('6f9e72ae-482e-3ef6-b540-65f43f728603', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-08', 'P', 1791661371557, 1791661371557, 0, 0),
('7504aed5-4411-3e6b-8a99-f50b36af31c0', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-21', 'S', 1791661371562, 1791661371562, 0, 0),
('78ba6e6d-abf7-31d6-8ca1-3c42f553716e', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-14', 'S', 1791661371557, 1791661371557, 0, 0),
('79092c6b-19ba-3092-ad35-b0d727a76c88', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-20', 'S', 1791661371557, 1791661371557, 0, 0),
('7b34e03c-6ef7-3d0a-935d-a79b11b449b1', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-16', 'S', 1791661371557, 1791661371557, 0, 0),
('8c09b7ce-7ce0-3fd6-8600-70fc43b89865', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-12', 'S', 1791661371557, 1791661371557, 0, 0),
('92709fee-b343-3a6f-919d-a221183958fa', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-10', 'P', 1791661371557, 1791661371557, 0, 0),
('989e3622-dc0a-3dd8-85fc-76f56cd6aeb8', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-18', 'S', 1791661371557, 1791661371557, 0, 0),
('abfcd51d-5097-30ab-a2b3-d6e3c13310b2', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-06', 'P', 1791661371557, 1791661371557, 0, 0),
('adf96405-cf00-3cef-a414-9ee87af059e3', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-09', 'P', 1791661371557, 1791661371557, 0, 0),
('bef242c9-4e21-3a82-8423-1249745ce510', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-24', 'P', 1791661371563, 1791661371563, 0, 0),
('c6180cc5-d03f-3814-99c5-8fa141c5ff28', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-23', 'S', 1791661371562, 1791661371562, 0, 0),
('c986f34c-e750-3fbb-8e5d-5bf03bf72f4f', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-09', 'S', 1791661371557, 1791661371557, 0, 0),
('cc7b17b1-bbcf-3aa8-a1cf-2863ef8b30b5', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-07', 'P', 1791661371557, 1791661371557, 0, 0),
('d3fe6808-5d5e-385f-a14e-2fc685e9d2ec', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-21', 'P', 1791661371557, 1791661371557, 0, 0),
('d4234b15-c7bc-3faa-a37e-8a3aef89f977', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-18', 'P', 1791661371557, 1791661371557, 0, 0),
('d6294c77-4fc0-371a-b25a-8d2fe9b16ddd', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-22', 'S', 1791661371562, 1791661371562, 0, 0),
('e03fce20-ba2d-300b-a7bf-08be25479cce', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-24', 'S', 1791661371563, 1791661371563, 0, 0),
('f1a96d0e-04ad-3cb6-bb90-7d2a1fca169a', '3cccbc1a-815e-39a2-ab64-afab77181cc9', '2026-10-14', 'P', 1791661371557, 1791661371557, 0, 0),
('f53d2115-e225-369c-8da3-49f895a206f2', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-11', 'S', 1791661371557, 1791666319208, 0, 0),
('fb6e59d2-20d0-328c-bc72-fb984cfa267b', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-15', 'S', 1791661371557, 1791661371557, 0, 0),
('fca80c64-1b9f-37f3-a1e9-29d1884a000c', '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', '2026-10-07', 'S', 1791661371557, 1791661371557, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `stok_mutasi`
--

CREATE TABLE `stok_mutasi` (
  `id` char(36) NOT NULL,
  `bahan_id` char(36) NOT NULL,
  `arah` varchar(6) NOT NULL CHECK (`arah` in ('masuk','keluar')),
  `jumlah` double NOT NULL,
  `sumber` varchar(12) NOT NULL CHECK (`sumber` in ('transaksi','pembelian','manual','awal')),
  `ref_id` char(36) DEFAULT NULL,
  `keterangan` varchar(200) DEFAULT NULL,
  `waktu` bigint(20) NOT NULL,
  `device_id` char(36) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stok_mutasi`
--

INSERT INTO `stok_mutasi` (`id`, `bahan_id`, `arah`, `jumlah`, `sumber`, `ref_id`, `keterangan`, `waktu`, `device_id`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('00101911-0d3b-364f-9b81-4601b8db88f4', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('00c4cc8f-275d-372f-a3ab-7c151fa5b7d5', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('013d10b0-b9c3-3bec-a2cf-9e800d0a10ba', '0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 'masuk', 140, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('014a6268-2b1a-36c8-ad47-93f135d9d318', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 120, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('01da14e3-875c-32b7-8e84-1556a59865a6', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371596, 1791661371596, 0, 0),
('025f74da-a2ae-3d5c-a054-9b3478af6589', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 20, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371609, 1791661371609, 0, 0),
('02f370e2-3eb9-3b16-b43f-8f1b6d2b3c63', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('0302e124-2033-3d5a-9bd5-e99d31e9dde9', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('04483327-744c-3693-b112-c759a854e6c9', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('052f9e73-9bd7-30dc-8141-058d0ee080b0', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('0540e466-64d8-319a-a819-3ad325645a7d', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('05977382-865b-3fa6-89f6-df3505795921', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371585, 1791661371585, 0, 0),
('05ba3e95-38c2-3b74-82c5-f023d4b16e7f', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 40, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('0690ddbf-9419-325c-978e-0e3cda350e5f', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'keluar', 12, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('080260b2-8efa-3710-a717-3a847cfc7208', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 20, 'transaksi', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('0813c60e-8d49-3aaf-83c2-8a7a8016a81f', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('0a32822f-b16b-311c-8eb9-c368ad2b71cc', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'masuk', 1200, 'pembelian', '4bbaf1bc-4b79-3410-b635-f4f39caa5c16', 'PO-001', 1791661371542, 'seed', 1791661371548, 1791661371548, 0, 0),
('0a38a087-96a3-334d-89e7-a651c6c9d808', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('0aa25d3f-70f0-3ff5-b6b0-d4b8e32d7f28', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 2, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('0b474359-1b18-3acb-898d-039cbf4a99d8', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'masuk', 1800, 'awal', NULL, 'Stok awal', 1791661210145, 'seed', 1791661210145, 1791661210145, 0, 0),
('0b63ed70-d6ac-30f3-95b8-c36dd237d281', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'masuk', 55, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('0be8d196-dae1-3eae-98b7-d60e00d60f30', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 15, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371589, 1791661371589, 0, 0),
('0bf93ed2-5490-38f1-8ac0-5c519cddde6f', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 80, 'transaksi', '130d105b-58bc-3637-a1e6-e3fd38c6af4d', 'TRX-20261008-05', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('0c18875f-2150-3371-a30b-7d07bb9a41a9', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 20, 'transaksi', '168e7c8a-e470-4921-bbbb-2c2ea7633026', 'TRX-20261011-S-002', 1791673370627, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370627, 1791673370627, 0, 0),
('0c5c4769-a827-306e-9629-06167b109c24', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 60, 'transaksi', '168e7c8a-e470-4921-bbbb-2c2ea7633026', 'TRX-20261011-S-002', 1791673370627, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370627, 1791673370627, 0, 0),
('0d16be21-8802-3349-85dd-6e8128ef6ebf', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('0d8168d1-8028-38fa-ac56-3b870825219d', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '1cc39890-dd54-31b1-b165-685518ab101a', 'TRX-20261007-01', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('0de2d8d4-46b6-3e05-a32c-1b0188b18669', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '168e7c8a-e470-4921-bbbb-2c2ea7633026', 'TRX-20261011-S-002', 1791673370627, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370627, 1791673370627, 0, 0),
('0df8f0df-d9d7-3e6c-8288-2a2af9911590', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 180, 'transaksi', '7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', 'TRX-20261005-02', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('0eb93fab-e623-3f0a-885c-491311985756', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('0f4b6d83-bc3c-374e-9758-26b3771c5486', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('100f49dc-2239-3f88-bf20-924a967308a2', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '9aa286f3-4123-32b7-a169-4d0d8987733d', 'TRX-20261005-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('106255fd-b116-33fb-8731-7103a32fd283', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 16, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371597, 1791661371597, 0, 0),
('110e0a42-bb02-388e-aad2-460f2609e239', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 2, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('12b2507d-3904-3f69-9933-7f725a122240', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371650, 1791661371650, 0, 0),
('136c2baf-62c1-3ce1-ab3d-291d6e2da0c9', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 60, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('148380b2-9637-37e2-a419-3ca23939d81b', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('152be5c2-c53c-39ef-acc9-0ff24cfb6c9e', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 100, 'transaksi', 'd02cd129-c688-3a2b-9b3e-7144a303a139', 'TRX-20261003-03', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('16185da8-40c3-36c6-9388-a3026db5ec1a', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('1685467e-7a79-3100-8aa8-45c74512cfbb', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('16be15d7-b4fc-34ed-a54c-b83579ad12d5', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 25, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('16e82861-1fee-3bba-86ab-e465ef340bba', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('16fdf841-9f4f-3e31-a48f-4b806ad4c421', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('17bc5721-0154-3d4a-8e22-4109d900f60a', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371612, 1791661371612, 0, 0),
('1811d536-bfc9-34dd-8fa1-b5c50b89d1dc', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371650, 1791661371650, 0, 0),
('1b29e206-2247-3f85-97bd-337c1d2f8904', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 120, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371603, 1791661371603, 0, 0),
('1bb2e49c-2675-375b-8655-d2cfd22d26c0', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 20, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371583, 1791661371583, 0, 0),
('1c293ac0-221c-3d05-bc7f-ba6bec5382c0', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('1c47ad30-adfa-3652-bafd-114f0683c403', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('1c84833a-acf3-3036-9a1d-c86c432fcba6', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('1d342029-c1eb-3d34-bbcc-4c0fe973bc51', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 180, 'transaksi', '168e7c8a-e470-4921-bbbb-2c2ea7633026', 'TRX-20261011-S-002', 1791673370627, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370627, 1791673370627, 0, 0),
('1d71c87e-7c79-3a53-877a-4cbcb63345c5', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791661371542, 'seed', 1791661371661, 1791661371661, 0, 0),
('1f2efc65-4c58-3b3d-9f42-9a3608707975', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 220, 'transaksi', '99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('1f6ff3d7-d3b7-354d-ae87-c63819072d86', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 2, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371577, 1791661371577, 0, 0),
('20393725-7370-342a-95c1-1f80d6465405', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 80, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('221ae7e6-1647-351d-9eee-88f41f2f146a', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('228317db-1228-37c3-9669-6773640bf02e', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 8, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('22cbd893-2622-3357-a851-6981f361aaa9', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 15, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('233219be-3f64-3b8a-bc68-0b9bf20f3cca', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('23764fbe-8d54-38ed-ae6a-d0c5ab872bf5', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('2405fe8b-58ee-3712-9768-c5297f463617', '37e189d8-30d3-3476-926a-4b826125bf34', 'masuk', 2200, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('2419d39e-9594-3a31-9b7a-a1b962c1211b', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 50, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371592, 1791661371592, 0, 0),
('24769065-9f3f-343e-be8e-ab11933618ad', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 30, 'transaksi', 'da2e5100-926d-3035-a6b6-5b64d5f509b2', 'TRX-20261002-02', 1791661371542, 'seed', 1791661371609, 1791661371609, 0, 0),
('2486a482-bd2f-3744-a59e-01facea8a531', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 15, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371596, 1791661371596, 0, 0),
('24d4ead8-34b2-3587-9988-45f559710f08', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 440, 'transaksi', '9aa286f3-4123-32b7-a169-4d0d8987733d', 'TRX-20261005-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('24f7456b-8af0-3e39-9ee1-6af409cd9982', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371597, 1791661371597, 0, 0),
('255819cf-83be-3c61-b34f-84304b4553d8', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', '99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('26be847c-3636-3ce3-8b48-b71a7e247503', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '1cc39890-dd54-31b1-b165-685518ab101a', 'TRX-20261007-01', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('282c4575-ccc2-3d39-af8b-cf9899df50b8', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 30, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('28e05872-4cb6-3706-84f1-d1bf0ffcbd8c', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', '1c69a1d7-9951-3b3e-b37a-538724512826', 'TRX-20260928-02', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('299221ad-8d19-3854-aaf4-d1749500a5ad', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 1, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371595, 1791661371595, 0, 0),
('2b41a131-119d-3ba4-b114-d5e6212badc4', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', '99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('2b44c057-2a42-378c-a524-9d1c92a7b994', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'keluar', 0.5, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('2ce03916-c588-3681-97f6-34539bd10162', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('2d47c934-461f-33d5-8fd0-54b450c98ce4', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 40, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371598, 1791661371598, 0, 0),
('2da068dd-f483-3af9-8dde-8d355b5ed231', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('2e5e593a-3b2e-3c9b-b3e3-7e9580fdcb99', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 16, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('2ee36156-ed22-3fde-a02e-adf391cd39bc', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 40, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('2f7d1c49-c177-3f04-808c-d96dea6fdf70', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('319beb13-ec96-3bc8-958e-e17b1214020d', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 40, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('325f750a-97eb-33f8-9d19-82a5a3e5f726', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 20, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('32e12b64-8b48-3f65-8c57-55ef8adb2053', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'ccaf8d00-7957-3511-bd05-37629940b0a9', 'TRX-20261002-04', 1791661371542, 'seed', 1791661371613, 1791661371613, 0, 0),
('33588d99-61ab-377b-b0f8-e3d2ecaf6405', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 20, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('33d963ea-49c2-3233-9ff6-ca28d3a4a166', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371598, 1791661371598, 0, 0),
('345dec4f-36e9-3639-bbf1-0fec4fe73bdb', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 1, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371595, 1791661371595, 0, 0),
('34bc7909-5c3e-316d-a56d-0170ea60b782', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', 'd02cd129-c688-3a2b-9b3e-7144a303a139', 'TRX-20261003-03', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('34c68f4e-035e-343a-b42b-a6c7313e4deb', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 50, 'transaksi', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('35058229-2705-37a9-9775-f25ac8d9dd44', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 220, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371580, 1791661371580, 0, 0),
('372a312f-1dd5-32ec-892b-49ad243ac370', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('377c5263-595d-37cb-910f-4bce71df4601', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 60, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('37f831b6-a142-3c3e-82d8-1abad48ba62d', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 12, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('38669b04-4c38-3841-a453-6ca003c2932e', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 2, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('38cbeb8f-3a6d-3f12-a4f8-a65eea92c03f', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', '9c7a0b57-e1c3-386c-8bdf-bf163e7ae0c8', 'TRX-20261002-01', 1791661371542, 'seed', 1791661371607, 1791661371607, 0, 0),
('38cd6e5d-ae7c-3591-be14-4d235af3aeaf', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 80, 'transaksi', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791661371542, 'seed', 1791661371661, 1791661371661, 0, 0),
('39236532-4c7a-32f7-9b7c-53c1b9fc240d', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('392c9e66-a393-3fc0-a7b6-b596e9cadb0e', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('3a9aab51-c878-35a9-8710-d3509a1c9b6f', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', '9c7a0b57-e1c3-386c-8bdf-bf163e7ae0c8', 'TRX-20261002-01', 1791661371542, 'seed', 1791661371607, 1791661371607, 0, 0),
('3aba8e2f-bcd9-3b7e-a27d-4cc332800bb8', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'masuk', 45, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('3b39f315-74f8-35db-a73c-44a4d2d0a1fe', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '4ae699cc-3925-33a9-a7d9-65b439abab1b', 'TRX-20261010-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('3b3bd727-cd43-3a86-997b-de02888dcf56', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('3c7720ab-4787-3d8e-8a7f-4c0f86ec71a4', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 60, 'transaksi', '476fa51b-f17a-3655-8b93-a12076975953', 'TRX-20261010-02', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('3ca65164-0f4c-38c2-96e0-2208a77f5362', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371605, 1791661371605, 0, 0),
('3df4fd36-aa3e-33ba-be68-18ed23ef2075', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'masuk', 1800, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('3e04afda-be78-3826-bd8d-bad1b66bab9a', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('3e4a305a-cc7b-3c15-bbf7-87abdc8bb2b0', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('3e6860fc-f9c4-3aac-8920-c42c406e4e8b', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '1cc39890-dd54-31b1-b165-685518ab101a', 'TRX-20261007-01', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('3e76a004-91af-35ef-a68c-11c2568f5fc8', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 1, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371592, 1791661371592, 0, 0),
('3ea1d4a6-ed56-3a96-a871-f215f5e84dc4', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('401bce75-2268-38f0-bbdf-f0c35ecc380a', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('40eb60a3-700b-34c3-a382-3d285a81658b', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', 'd3ae8063-905d-3748-a23b-e269833c68cb', 'TRX-20261004-03', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('437e38af-43db-3cbc-aded-210997ee8585', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'TRX-20261009-04', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('4387329c-0430-31dc-829f-daafa88170f3', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '36800b66-243a-3a04-94ba-1aa46ff677ed', 'TRX-20261010-01', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('43c8b00c-5ab4-3c22-bef1-30b4112125ba', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371580, 1791661371580, 0, 0),
('45a1e606-0130-3fe9-b7ba-4cead1cc8c44', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 1, 'transaksi', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('4638aa83-6aa6-3989-8f90-1d9e5a3e7f47', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 2, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('463ec6f2-e06f-3fff-8e6d-6e923e00f60e', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('46f68305-fe3d-3d90-9251-ef83bbe32f26', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('47ae5e05-ffe8-311f-baea-d510d6f9bd49', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 280, 'transaksi', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('48b5900c-9c5b-3203-af84-ced68c9fef25', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('48c69b1d-81ea-3986-92d5-87c46defd977', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', 'a445aa20-e42d-3e44-9c1a-810bc86a73d3', 'TRX-20261007-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('48d9f7b7-6424-389c-a20b-3e61b8088e30', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'masuk', 40, 'manual', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'Pembatalan TRX-20261011-S-001', 1791673370797, '4f751928-7886-3558-8bb4-684636d79d5b', 1791673370797, 1791673370797, 0, 0),
('496372fa-b139-32b3-84f1-a17a01201369', '5f991edd-bb70-3cb9-b368-02a93b901562', 'masuk', 360, 'manual', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'Pembatalan TRX-20261011-S-001', 1791673370797, '4f751928-7886-3558-8bb4-684636d79d5b', 1791673370797, 1791673370797, 0, 0),
('49abe60c-260c-3298-892a-53b3018d3964', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('49ea7eb8-4dcb-318d-88a9-50f6dd504565', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 20, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('49eb0ecc-137b-319f-b8d7-c3f0b756e89c', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371579, 1791661371579, 0, 0),
('4a27bce2-0f4d-3be4-b763-ede657dbee7c', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 200, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('4aa46ad6-0f94-3633-83c9-cf0540766057', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371587, 1791661371587, 0, 0),
('4aafa0c6-ff91-3296-9c3d-91b26536c6b0', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'masuk', 900, 'awal', NULL, 'Stok awal', 1791661210147, 'seed', 1791661210147, 1791661210147, 0, 0),
('4ad11eac-74af-3037-9d87-9ece4b261a2a', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 15, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('4c620bf4-a561-3b3a-9370-8a0d01e0f175', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('4ca3204f-ca62-3cc8-970f-73c697187678', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', '9aa286f3-4123-32b7-a169-4d0d8987733d', 'TRX-20261005-01', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('4cdf25c7-dec3-33f9-91db-c2c1eec6412e', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 60, 'transaksi', '12d40dde-4e7f-3e69-bfbc-fc6441adb4e2', 'TRX-20260929-04', 1791661371542, 'seed', 1791661371587, 1791661371587, 0, 0),
('4cf1d52f-2977-38f4-94cf-2a607ca64349', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 40, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('4e140903-d45e-332b-bf1d-37aaad40d9df', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371604, 1791661371604, 0, 0),
('4e33e89e-d29d-3148-a31e-1a83d6fbdebe', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 1, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('4e388d97-c2c5-3fff-8b9f-854e8cd0cef7', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 60, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371595, 1791661371595, 0, 0),
('4e7ecaef-c560-3209-94c2-0bea97142aed', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 400, 'transaksi', '72ffe998-7085-3782-a22e-e8830b091968', 'TRX-20261001-06', 1791661371542, 'seed', 1791661371606, 1791661371606, 0, 0),
('4ede13e5-4962-3e7a-8a8e-0d268c2f7da0', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'masuk', 700, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('4ef555a1-b3d2-317b-bb1d-aa8d2fe21367', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'masuk', 9000, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('51a0adbe-5853-35a8-a16c-2ee7aad073e8', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('53ca53c9-3976-31b6-8648-7c511cb67e8f', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('54287c86-adf9-3c23-b84b-e8bdf409bc2f', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('547dfe04-710f-3c6b-93d8-f6e7d6c5a3a5', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371579, 1791661371579, 0, 0),
('55361b8a-b4df-3ba0-9537-fb7d31762e0d', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'TRX-20261011-S-001', 1791673370460, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370460, 1791673370460, 0, 0),
('55acc586-3a56-3a54-bbde-3ff256fcdcb2', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 12, 'transaksi', '476fa51b-f17a-3655-8b93-a12076975953', 'TRX-20261010-02', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('56214773-abf8-3f31-a25a-e7095f1b665c', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 20, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('56a0e257-594f-39e1-8cb0-80de43ec72d6', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371585, 1791661371585, 0, 0),
('56e438ac-d23f-3463-82ee-baf88f2c63ed', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('57c16bb2-35f2-3050-a836-5894d31eaf5d', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 50, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('59198d47-ea5a-3cdb-b754-036fabd0cab3', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'TRX-20261009-04', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('59828a35-7846-3a84-9bab-ee57aa1a6ad2', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 24, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371577, 1791661371577, 0, 0),
('5a3ff5ae-a3fd-36f8-95cd-a25df95224e2', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 30, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('5b12deaf-2c38-3c85-94aa-a76ee716272f', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('5b8330d8-d974-3d5e-8007-830fb6f7e4a3', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 400, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371577, 1791661371577, 0, 0),
('5dd2cf53-a5ce-370b-99b3-816137ac7dda', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 2, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('5dedafdd-5e80-38aa-afc5-0b7b5e8103c7', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'masuk', 4200, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('5f86f04f-2a29-3577-9c17-55fa0baec354', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 16, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('60b731f0-2e5d-3f5d-98c8-5c7e4074dc17', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791661371542, 'seed', 1791661371661, 1791661371661, 0, 0),
('61309c2d-8165-3f40-8b5e-135d2dbcba5a', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 20, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('61b52ae9-146f-3efb-8a08-4e82a4c46233', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 25, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371584, 1791661371584, 0, 0),
('624f5fac-f17a-345b-b2e3-d8b6df189b56', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 30, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('632a2e05-b61b-3044-a1f6-8fef5c2cb3c7', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371602, 1791661371602, 0, 0),
('638e9ff0-2dc7-390a-a722-aeb5b971bb63', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '98582e89-ec8d-3829-a33c-887ff5436690', 'TRX-20261006-01', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('644c0035-03ed-392d-abcf-65373bcd2f7c', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('6531a277-7ca8-342d-bb5c-b3b27ac04ce3', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 400, 'transaksi', '12d40dde-4e7f-3e69-bfbc-fc6441adb4e2', 'TRX-20260929-04', 1791661371542, 'seed', 1791661371587, 1791661371587, 0, 0),
('65419361-d78c-305d-b82a-cd4919d23655', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '1c69a1d7-9951-3b3e-b37a-538724512826', 'TRX-20260928-02', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('658016e4-d0aa-3835-b856-08ad43b11eda', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 30, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('66370633-6e2b-3d32-a372-796a027a9d07', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('66666c0c-7ed3-361f-9db2-24af1195e773', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('670a7da9-819d-3326-9c79-490e6435c2ba', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 120, 'transaksi', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'TRX-20261011-S-001', 1791673370460, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370460, 1791673370460, 0, 0),
('6737ad6c-1969-3024-b389-126ebe7da91d', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 2, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371577, 1791661371577, 0, 0),
('675f3bd2-1dc5-36c1-970e-f603a3dd7e64', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('67e05523-a127-3533-9cf5-5c5f8ee8c4bd', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 60, 'transaksi', '4ae699cc-3925-33a9-a7d9-65b439abab1b', 'TRX-20261010-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('69538055-f718-3d22-96e0-950e79996ed4', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 50, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('69fac13c-f4fb-3a63-99c0-6d29955549b1', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 240, 'transaksi', 'dc803830-6c67-3dce-a866-67cbd4763741', 'TRX-20260930-02', 1791661371542, 'seed', 1791661371593, 1791661371593, 0, 0),
('6a91a3be-5364-3814-92e6-a88422d9f759', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 500, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371609, 1791661371609, 0, 0),
('6adc01c2-c643-3deb-a8b0-eeac3bcaff04', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371602, 1791661371602, 0, 0),
('6b624ed3-0d75-3ab6-88e2-655d848bc2c3', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'masuk', 1100, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('6c2e8fe0-7580-331d-a211-f88a616dc139', '5f991edd-bb70-3cb9-b368-02a93b901562', 'masuk', 15000, 'awal', NULL, 'Stok awal', 1791661210147, 'seed', 1791661210147, 1791661210147, 0, 0),
('6ccb86df-c534-3642-9b35-ef41db9863a9', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'masuk', 60000, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('6f709905-8a2a-36c5-9db0-932414a4d685', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 200, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('71db6127-bb16-3721-808c-197cb9f6a133', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('7217a81b-a609-3497-ba07-2d0d9d1c30a8', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371599, 1791661371599, 0, 0),
('738ea6bb-681c-3446-a07e-3349dd61eb35', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('73e04c51-eb8d-370a-b2cd-e1c6d565835c', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 50, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('7690a663-cc3e-3e9c-9944-6647e92836e9', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'keluar', 0.25, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371644, 1791661371644, 0, 0),
('7786bae6-908c-3b9b-9900-b38df6025e66', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '98582e89-ec8d-3829-a33c-887ff5436690', 'TRX-20261006-01', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('7799187b-da83-3ef3-a8ab-2a743b37a6cc', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 200, 'transaksi', 'd4e3988f-771f-3c4d-8fc7-f066f71c6fde', 'TRX-20261008-01', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('786b95db-3204-3d34-931f-297708187dc9', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 180, 'transaksi', '4ae699cc-3925-33a9-a7d9-65b439abab1b', 'TRX-20261010-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('7947493c-62a6-3931-9151-a0a349779db1', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('7957466b-dc11-3971-879f-a8f1cb9add6b', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 200, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('79801092-d7e1-3235-ad26-ec9f01653cb7', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('7a042d78-f3d2-3d98-868c-534aee8c5c82', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 40, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('7a5cf2c5-74be-3cb8-9c29-78c2252a9fb4', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('7a91b089-f057-3df0-94a5-346f991af7e2', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 50, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371605, 1791661371605, 0, 0),
('7aa27a41-bc84-3af3-8105-87ff3995e4f1', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('7ad5cf8c-28cb-3f4b-b187-28ec403f1121', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 40, 'transaksi', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'TRX-20261011-S-001', 1791673370460, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370460, 1791673370460, 0, 0),
('7ae61aef-3487-313b-b296-8729f8bf79f3', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371599, 1791661371599, 0, 0),
('7c951909-723c-34a9-9764-92384dc2096a', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 20, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('7c9dcd68-eacb-381c-96c8-643d5d2ed462', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 400, 'transaksi', '8782cf19-39da-3080-bf35-83cdaa1ce75f', 'TRX-20261008-02', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('7d30a31f-bc45-3a57-b5e7-f845b55f205a', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 440, 'transaksi', 'f6c9f621-5918-39bb-aff8-3fc9db2ee34d', 'TRX-20261003-04', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('7ec0af29-7611-33bb-a4bb-e2275e8a2776', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 220, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371604, 1791661371604, 0, 0),
('8035f0bb-ed8b-3258-b08a-b1674efe3c5c', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 80, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('811b5594-15b2-3749-b9c8-6776653de5b6', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 220, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371598, 1791661371598, 0, 0),
('8162f894-b50f-3437-9b98-2f7e33c157b1', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 15, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371592, 1791661371592, 0, 0),
('81fda836-4f75-3066-bd38-ea9437179867', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '34d15834-ac28-36ae-b040-c55144ab25c7', 'TRX-20261003-02', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('82423d47-63a2-3148-a90b-33340ddaed4e', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371602, 1791661371602, 0, 0),
('828513b8-3e84-3750-a434-93f453dd710f', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 50, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371595, 1791661371595, 0, 0),
('84260b2f-9122-3952-98fb-77c5fe84a4f0', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 360, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371587, 1791661371587, 0, 0),
('84bb07a9-bc46-36d9-84f5-0a034ff14a05', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '9c7a0b57-e1c3-386c-8bdf-bf163e7ae0c8', 'TRX-20261002-01', 1791661371542, 'seed', 1791661371607, 1791661371607, 0, 0),
('84c15e8a-b62f-3798-9370-313a4a444d45', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371577, 1791661371577, 0, 0),
('857ef0a6-1bd2-33d8-be6c-9216916cff88', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('86a6edb8-f825-3305-94b7-2687827cf536', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('86ba3a58-d8a7-307e-ab1c-d93ac31d60a1', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 20, 'transaksi', '7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', 'TRX-20261005-02', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('87329782-a470-3450-88a3-d0c6f8aaf1dc', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('87d45029-1ac8-388d-b8d2-7550a637e2dc', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 20, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('888f73ab-3653-323f-b25d-62fde5e4aef5', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'masuk', 5000, 'awal', NULL, 'Stok awal', 1791661210154, 'seed', 1791661210154, 1791661210154, 0, 0),
('88ea0c44-5f8e-35bb-8ba5-18f60dd12d8b', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', '98582e89-ec8d-3829-a33c-887ff5436690', 'TRX-20261006-01', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('89bd0eab-db72-3712-bede-4cf41e93a638', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'masuk', 120, 'manual', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'Pembatalan TRX-20261011-S-001', 1791673370797, '4f751928-7886-3558-8bb4-684636d79d5b', 1791673370797, 1791673370797, 0, 0),
('8a3eb72f-598f-3d22-b883-667e38282867', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371579, 1791661371579, 0, 0),
('8af623a6-5042-392a-a04a-4a19e85c0eb6', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 40, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('8b08a797-7e8f-3790-b94d-b42a1e9e736a', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371579, 1791661371579, 0, 0);
INSERT INTO `stok_mutasi` (`id`, `bahan_id`, `arah`, `jumlah`, `sumber`, `ref_id`, `keterangan`, `waktu`, `device_id`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('8b0b2946-44f9-3a07-925d-e236b7384eef', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', 'd3ae8063-905d-3748-a23b-e269833c68cb', 'TRX-20261004-03', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('8b881374-4c24-37f2-bdb3-042403fd8779', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('8c59f66b-c9ac-3bde-9e6b-9a723a547a29', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 240, 'transaksi', 'da2e5100-926d-3035-a6b6-5b64d5f509b2', 'TRX-20261002-02', 1791661371542, 'seed', 1791661371608, 1791661371608, 0, 0),
('8d5c1a3a-edff-3d11-a2be-83d8be2067d0', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371644, 1791661371644, 0, 0),
('8dda90c8-e08e-3763-a10a-a5f929670404', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'keluar', 0.25, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371582, 1791661371582, 0, 0),
('8deb2f15-96d1-3699-be0d-405bda927a3a', '0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 'keluar', 2, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('8ef26a74-2df2-371e-a14e-c60724599385', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 12, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('8f2ff161-7bb3-3a82-80cd-337214ce1cd6', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 120, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('8f3326ec-a7b6-32c9-a16e-8e735b568265', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 280, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371585, 1791661371585, 0, 0),
('8f950a9f-bde7-3aa0-9186-bb2449f46223', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('90c3f455-1eb2-3291-b8b8-9600a454088f', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'f6c9f621-5918-39bb-aff8-3fc9db2ee34d', 'TRX-20261003-04', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('9158f1bc-67e0-3ab1-aaac-b96cd9dd7f3f', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'keluar', 0.25, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('91a96163-9633-3ed0-adec-916b37608c05', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('91b6e82a-1aa2-3963-8762-86dfa1e60ba8', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 80, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('921ce2c5-c120-32ff-81b5-15bedabfbb12', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 60, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('92f4325c-511b-398d-a4bb-ae35f00831dd', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 30, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('93bb6516-2c2e-3604-9784-11998cbabeed', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 30, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371597, 1791661371597, 0, 0),
('94e6ddee-25b0-3587-9de1-0a11c6f310ac', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('95c1c17d-3118-35a3-9890-36705c01379f', '0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 'keluar', 4, 'transaksi', 'd02cd129-c688-3a2b-9b3e-7144a303a139', 'TRX-20261003-03', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('96396174-730f-3610-875e-d49add63b267', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('97f9fc65-a6c8-3d4f-ad16-7c879c50d9fc', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('98b07990-e378-3a6e-b5a7-04b70681b9cf', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 360, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371603, 1791661371603, 0, 0),
('997f3e7e-061c-35f7-abef-dd9d860d18f9', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 180, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('9a238f7c-1b69-3577-8695-a57ce1681f47', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'masuk', 2600, 'awal', NULL, 'Stok awal', 1791661210147, 'seed', 1791661210147, 1791661210147, 0, 0),
('9a5bd228-ae06-3a65-99db-09154f5c066c', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371603, 1791661371603, 0, 0),
('9a8b0395-2a2c-349f-93a0-c44b1ef6a0f6', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('9c59ca4d-7031-3ceb-8e3c-2f54e9af95ee', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 120, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371589, 1791661371589, 0, 0),
('9c8cba48-e47d-39a1-874f-577710eb076c', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 15, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('9d6b98e0-8949-3f11-a2ae-d83675cdf74e', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'masuk', 2200, 'awal', NULL, 'Stok awal', 1791661210145, 'seed', 1791661210145, 1791661210145, 0, 0),
('9dd14593-5ffd-36e9-9d38-81b9cf0490a6', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '98582e89-ec8d-3829-a33c-887ff5436690', 'TRX-20261006-01', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('9e636f10-bd71-3028-b72a-bd68f06d8aa2', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 15, 'transaksi', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('9eaf2aab-28c2-3dce-aa78-54151fe0ddf6', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371583, 1791661371583, 0, 0),
('9f427231-082f-39cc-bd6a-1994e8dd4bb1', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '34d15834-ac28-36ae-b040-c55144ab25c7', 'TRX-20261003-02', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('a06009bd-95c4-3f5d-9b84-d752eb0a7f96', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 50, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('a0678bdd-077f-3dc2-980f-11abb91667e2', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('a0841377-4929-39b1-a2f9-aaa0d847f443', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 30, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('a1a39bc7-cf8c-3435-a35f-e829e260a7cc', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 20, 'transaksi', '4ae699cc-3925-33a9-a7d9-65b439abab1b', 'TRX-20261010-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('a1e3438d-1058-3ebc-a262-2bab98f5b47d', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', 'a445aa20-e42d-3e44-9c1a-810bc86a73d3', 'TRX-20261007-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('a300cbc9-eab1-3af3-bb8f-14a8272c98f8', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 120, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('a446bdc2-f91c-37d2-9ea5-ccb023f05853', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('a6838d22-8918-3f6d-8421-88e83bc3b5c0', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 1, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('a7ab4b52-1617-3412-ac20-2296d8d23913', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371602, 1791661371602, 0, 0),
('a80ecd40-e1de-3e45-96b8-c908320d3b58', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 80, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('a901f078-c65e-38b8-a9a0-dc455c736b68', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 280, 'transaksi', 'ccaf8d00-7957-3511-bd05-37629940b0a9', 'TRX-20261002-04', 1791661371542, 'seed', 1791661371613, 1791661371613, 0, 0),
('a93c6340-862e-327f-99d6-3187a6e716aa', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('a98b5f8c-0940-368b-8f31-16aed4be1c04', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'masuk', 36, 'manual', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'Pembatalan TRX-20261011-S-001', 1791673370797, '4f751928-7886-3558-8bb4-684636d79d5b', 1791673370797, 1791673370797, 0, 0),
('a9f39bbb-8b37-38a0-b3f9-e6947b138944', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1791661371542, 'seed', 1791661371580, 1791661371580, 0, 0),
('ab5d85df-10c1-3477-981b-17b7958c1ae8', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 24, 'transaksi', '8782cf19-39da-3080-bf35-83cdaa1ce75f', 'TRX-20261008-02', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('abefb7e6-23a4-3206-90ce-e9451989514d', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 16, 'transaksi', 'da2e5100-926d-3035-a6b6-5b64d5f509b2', 'TRX-20261002-02', 1791661371542, 'seed', 1791661371608, 1791661371608, 0, 0),
('aca9feba-c92a-3229-9b92-7b1e8bdcdfba', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'TRX-20261009-04', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('ad4f3a3a-fba3-3351-9ed4-9ed75c55328a', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 25, 'transaksi', 'a445aa20-e42d-3e44-9c1a-810bc86a73d3', 'TRX-20261007-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('adaecd0f-fd9b-3986-b62e-02bb87153360', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 1, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371592, 1791661371592, 0, 0),
('ae1d6a27-c35c-3579-abbe-7dec95b981c1', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371603, 1791661371603, 0, 0),
('af1cb6f1-a1eb-3014-bf1d-a32766a92bf6', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 360, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('b0b04921-dd4b-3b2c-8a24-2d438ca42340', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('b2cb2c2a-e339-3b9b-8556-c6c48ba6f227', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('b3d572aa-9645-321e-b924-8313541681a6', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371599, 1791661371599, 0, 0),
('b44b5c38-60c4-32fa-bbbb-c5954bb8daed', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('b4989537-4773-3884-b2dd-aa9ff659aeed', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 30, 'transaksi', 'dc803830-6c67-3dce-a866-67cbd4763741', 'TRX-20260930-02', 1791661371542, 'seed', 1791661371593, 1791661371593, 0, 0),
('b5186ba8-3cfe-3e7e-9cd4-9bad01949901', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371586, 1791661371586, 0, 0),
('b57abb88-b832-3722-9e2c-71720318d28a', '62ab7623-7cc9-35a6-8552-cad6d7d6ea89', 'keluar', 2, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('b5ba7f9e-ee93-3bfd-8271-b76ff2bf5ce0', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371605, 1791661371605, 0, 0),
('b7092381-bdb6-3b68-b76a-f4dedf77487a', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 20, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('b70feb84-019c-339d-a83d-e2f1ca36eb45', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371585, 1791661371585, 0, 0),
('b97d21cd-8c48-30b0-8839-4591aa03df7f', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 200, 'transaksi', '130d105b-58bc-3637-a1e6-e3fd38c6af4d', 'TRX-20261008-05', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('b9ca883e-c83b-3e27-bf43-bd5ebdf06e6c', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 240, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('b9ef10ad-faab-3bea-ba8e-ad40f7daf4d1', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'keluar', 0.5, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('ba579e5f-6a90-3736-8e89-13b7a7a4e309', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 400, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371611, 1791661371611, 0, 0),
('bc3c8c8d-aa9b-311e-b8e9-7ee52ce2e880', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('bd0cb0bf-2035-323e-acc9-8b2676144784', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'keluar', 24, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('be24be9d-c994-323c-a03a-edc3489bfca0', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 24, 'transaksi', '72ffe998-7085-3782-a22e-e8830b091968', 'TRX-20261001-06', 1791661371542, 'seed', 1791661371606, 1791661371606, 0, 0),
('be4d6227-a454-3cda-9a90-2091755e43cb', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371604, 1791661371604, 0, 0),
('be719fbd-89b5-3d10-b5ca-349e50379e86', '0616a037-f7a1-3a7d-ab8b-4f8839df32f6', 'keluar', 2, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('bea468fd-19b8-384b-bba2-cb187f20c989', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', '2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('bf83ec5c-6c3c-35b7-8616-a2af989f2b73', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 200, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('bf87f7f6-1bbc-3702-9055-811d0b1942b4', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 40, 'transaksi', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1791661371542, 'seed', 1791661371603, 1791661371603, 0, 0),
('c10b70fc-b552-322e-a166-ee3823831189', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'keluar', 30, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('c18abd58-a9f0-3b49-abd6-af4e3d5de216', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791661371542, 'seed', 1791661371661, 1791661371661, 0, 0),
('c199437b-55d1-3b71-9cc2-c4b9008b0906', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', '764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('c1b720a6-bf3c-3ab2-a0ef-d5ed8d590505', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 120, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('c1ec5417-9106-3a54-bf35-aebccd168be3', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 12, 'transaksi', 'd4e3988f-771f-3c4d-8fc7-f066f71c6fde', 'TRX-20261008-01', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('c22d890e-d4a7-36f5-bcd8-968cf37e6b8a', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('c250d8cb-7b30-3c1c-a451-6b489d407bfe', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('c26adca4-7d3b-3cd7-a186-294804482834', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 40, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371613, 1791661371613, 0, 0),
('c4835882-dc6f-3533-b0b7-cea4793d4498', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('c4b2770f-f0d9-327b-958b-9525e8558359', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 280, 'transaksi', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791661371542, 'seed', 1791661371661, 1791661371661, 0, 0),
('c53e1b92-13b7-3419-aaf2-2751df6a42e0', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371584, 1791661371584, 0, 0),
('c853d670-3632-3758-aac1-d10b1f164f85', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 40, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('c885cab9-d070-3320-a16b-81a0dabfd5b3', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371644, 1791661371644, 0, 0),
('c9086ce3-46ee-322d-a727-6cb97c01259c', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('c951c00d-d350-39e7-b453-f80790d99356', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'masuk', 6000, 'awal', NULL, 'Stok awal', 1791661210147, 'seed', 1791661210147, 1791661210147, 0, 0),
('c98360ef-70f1-3438-aaed-1679797b5599', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('cb545b72-fb5f-3203-aa48-0d351a00dd74', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('cd10a7bd-b893-33e1-badf-1416a1fb55c8', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('cef6c1f1-3cc4-3644-b0a8-6f0675837609', '6f5593e6-91c1-32d9-91b2-5dafb6717996', 'keluar', 1, 'transaksi', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('cf451ed5-ea96-3b3c-9241-03454f16a5e4', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371590, 1791661371590, 0, 0),
('cf9d66f6-0115-37a3-942e-8cad144c7b78', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 80, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('cfb6dbaa-3613-34b4-8208-e5dfb4dbe639', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 50, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371650, 1791661371650, 0, 0),
('cffa72e8-ee8a-3d07-85dc-76f532d0f404', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 400, 'transaksi', '476fa51b-f17a-3655-8b93-a12076975953', 'TRX-20261010-02', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('d0db0a37-f854-3f58-8dc9-ab9e7d334eb1', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('d15d36c0-0cbd-3110-9cfb-f8943d758257', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 60, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371583, 1791661371583, 0, 0),
('d172b7aa-264d-3b7d-8ac5-be7333ea749c', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 24, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('d20fedef-ef12-3d7c-a417-90bb61df9370', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'masuk', 2400, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('d310d65e-0ab8-3ce1-8715-09c33f1c2b72', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'masuk', 1000, 'pembelian', '4bbaf1bc-4b79-3410-b635-f4f39caa5c16', 'PO-001', 1791661371542, 'seed', 1791661371548, 1791661371548, 0, 0),
('d3c64ea6-2f41-37be-8a3c-01a512a149a8', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('d419884c-7698-3687-86be-15707d09a689', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371585, 1791661371585, 0, 0),
('d41bc521-bb96-3496-89b9-560c8c331752', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 360, 'transaksi', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'TRX-20261011-S-001', 1791673370460, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370460, 1791673370460, 0, 0),
('d57b9faf-292a-3f5a-b406-697330229ab3', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791661371542, 'seed', 1791661371652, 1791661371652, 0, 0),
('d5df380e-a784-3f92-85df-0bd35388073a', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 40, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371598, 1791661371598, 0, 0),
('d628fd30-dfe3-3634-96e1-b1731325fe38', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'keluar', 12, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371599, 1791661371599, 0, 0),
('d6496123-cbd2-3294-afc5-5fe608cd5678', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('d6a19da5-8d4b-357e-a3ef-6d24b0fd4e28', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371613, 1791661371613, 0, 0),
('d807aad0-8bdf-3707-b0df-29144a990256', '212e97d1-2113-3d95-9afd-2bb3e00b55ae', 'masuk', 25000, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('d82ad956-01c9-3614-bcb6-f315452b9009', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 60, 'transaksi', '7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', 'TRX-20261005-02', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('d8a0e2e8-c9b7-3736-8397-608b040980e7', '5b1459be-ae2a-3bb5-b90e-7d7bb14fd203', 'masuk', 2000, 'pembelian', 'c81e08f2-bd3c-3ee0-b9e0-84c2e24f945a', 'PO-002', 1791661371542, 'seed', 1791661371548, 1791661371548, 0, 0),
('d94d6d99-4e4a-340b-98de-bcbf861df2e2', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 12, 'transaksi', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791661371542, 'seed', 1791661371657, 1791661371657, 0, 0),
('d9e94dd7-8548-362f-abdd-95834e690b05', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 200, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('da3ca25f-8cc3-3d48-b014-920e43cd3046', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', '7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', 'TRX-20261005-02', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('dacefc19-9898-33de-b9be-f3f5383b412b', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 36, 'transaksi', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('db1542fb-7814-3dc9-a769-f09208e333ab', '37e189d8-30d3-3476-926a-4b826125bf34', 'keluar', 50, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('db526968-c52a-38f9-935b-f0593de95016', '853bdd08-75b6-3f37-ac86-2bc550d1be4b', 'masuk', 80, 'awal', NULL, 'Stok awal', 1791661210149, 'seed', 1791661210149, 1791661210149, 0, 0),
('dce31bb8-84e8-3e6b-8b1e-74a8b05786fc', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 240, 'transaksi', '576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791661371542, 'seed', 1791661371618, 1791661371618, 0, 0),
('dd179117-1cd2-3304-9d62-c5c276976ebd', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371578, 1791661371578, 0, 0),
('dd666904-7673-32f4-ae0b-43291ea84127', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 10, 'transaksi', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1791661371542, 'seed', 1791661371599, 1791661371599, 0, 0),
('dd8bf90e-3950-38cf-ad75-1901b20ab49a', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('ded45a83-90a8-3005-ae2f-568b5f745390', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('e0339d97-11fa-3c71-b36a-3eac38c046f5', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 30, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371609, 1791661371609, 0, 0),
('e0816205-1393-31e9-9fe6-7b36c006f953', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 40, 'transaksi', '1c69a1d7-9951-3b3e-b37a-538724512826', 'TRX-20260928-02', 1791661371542, 'seed', 1791661371566, 1791661371566, 0, 0),
('e0d7e48c-4556-3a2b-af49-2c2e1cafb6fe', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371644, 1791661371644, 0, 0),
('e0ea85d1-0a87-32d8-95f2-2f9806f842cb', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('e0f38384-f5b2-378a-afbb-20b2c5532045', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'keluar', 30, 'transaksi', '01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('e131de2e-44d7-3466-98d3-cefd8261986f', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 20, 'transaksi', '36800b66-243a-3a04-94ba-1aa46ff677ed', 'TRX-20261010-01', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('e1d02714-9749-33b9-a26a-206747f6fd7b', '3f65ef4e-af98-3b90-8aab-bea7ec64b826', 'keluar', 24, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371611, 1791661371611, 0, 0),
('e361474e-f023-3891-b01d-b90b19a3f78e', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 360, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('e4389da5-8de2-3207-a002-285046ddaac1', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 50, 'transaksi', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1791661371542, 'seed', 1791661371575, 1791661371575, 0, 0),
('e534ba03-ab3d-3a89-a5ae-e9abbfe10926', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'keluar', 24, 'transaksi', '36800b66-243a-3a04-94ba-1aa46ff677ed', 'TRX-20261010-01', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('e568d7a0-4b8c-32a2-95a1-063a07dde24c', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 20, 'transaksi', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('e644a6bb-c45e-344a-8a54-d97b5bf29e79', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791661371542, 'seed', 1791661371629, 1791661371629, 0, 0),
('e673243b-ca8a-3f2e-a6ef-0f81fc863972', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 30, 'transaksi', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1791661371542, 'seed', 1791661371578, 1791661371578, 0, 0),
('e6b462e2-12e6-3929-839d-d25b64a303f5', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 20, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('e6b6054a-69f4-3a1f-85a2-6bdc8ec2a7ea', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791661371542, 'seed', 1791661371644, 1791661371644, 0, 0),
('e91086c3-213e-3c9b-85e7-2226aedbbe4b', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('ea43cd5c-2417-3352-8f4d-8eec1d6c87b4', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 40, 'transaksi', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791661371542, 'seed', 1791661371654, 1791661371654, 0, 0),
('eb93b7df-3969-35d4-92c9-e9324c23048b', 'dc80c45c-1458-30a5-8653-7ba4dd0b80ee', 'keluar', 40, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('ec0fa10a-c06e-3ef2-840e-3f6863c1c9b2', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 10, 'transaksi', '98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1791661371542, 'seed', 1791661371605, 1791661371605, 0, 0),
('ed060e03-b875-3eb5-9460-76dc5020cc56', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 40, 'transaksi', '37c327fa-a839-32d8-89dc-afe780ba58b6', 'TRX-20261001-07', 1791661371542, 'seed', 1791661371606, 1791661371606, 0, 0),
('ed2862f0-5cfd-30e1-8b73-9a33c7f26898', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1791661371542, 'seed', 1791661371592, 1791661371592, 0, 0),
('ed58cfed-1e72-3b4d-84f7-abca58af12f9', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 150, 'transaksi', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1791661371542, 'seed', 1791661371600, 1791661371600, 0, 0),
('ef71800b-c71e-3ce1-a4f9-9eb7317dc1ed', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 8, 'transaksi', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1791661371542, 'seed', 1791661371588, 1791661371588, 0, 0),
('ef8e253a-78d7-3f65-af5b-8ffc6116427b', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 80, 'transaksi', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('efff1b58-cf73-3ff5-a817-2ac604069edf', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 240, 'transaksi', '44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1791661371542, 'seed', 1791661371597, 1791661371597, 0, 0),
('f0ac2143-5f71-3135-bbf0-31574bca56a6', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 140, 'transaksi', 'e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1791661371542, 'seed', 1791661371612, 1791661371612, 0, 0),
('f1449da3-e003-31ce-9c14-6fce7fafd031', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 400, 'transaksi', '7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1791661371542, 'seed', 1791661371595, 1791661371595, 0, 0),
('f1df167a-594d-3efb-9d7f-8c420099b328', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 200, 'transaksi', '476fa51b-f17a-3655-8b93-a12076975953', 'TRX-20261010-02', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('f2a3df60-378e-324d-9fe5-79930f625165', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 100, 'transaksi', '37c327fa-a839-32d8-89dc-afe780ba58b6', 'TRX-20261001-07', 1791661371542, 'seed', 1791661371606, 1791661371606, 0, 0),
('f47aefc4-1297-3398-a580-338c97685930', 'bc2e1158-9793-31c7-ba0c-340f6ddfb10d', 'keluar', 18, 'transaksi', 'ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1791661371542, 'seed', 1791661371584, 1791661371584, 0, 0),
('f5a4a5a9-4135-351b-8538-537e76ebaaa1', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 180, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371583, 1791661371583, 0, 0),
('f5f98233-9546-3ba3-bca6-bc6ce3819e2a', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '34d15834-ac28-36ae-b040-c55144ab25c7', 'TRX-20261003-02', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('f62e5b0c-38f8-3500-8b08-34d929c0d34f', '6ec304f4-f5dc-3385-9e6b-b2a197288824', 'keluar', 12, 'transaksi', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791661371542, 'seed', 1791661371656, 1791661371656, 0, 0),
('f69d5f69-421d-3bcc-8598-8d8b900c5cc9', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 20, 'transaksi', '66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('f79010d1-bb86-3021-8403-55687a189c87', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 120, 'transaksi', 'd02cd129-c688-3a2b-9b3e-7144a303a139', 'TRX-20261003-03', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0),
('f7da15cc-2c62-337a-b269-12fcf6ed4489', 'aba285d6-e49b-3ccd-964c-be5824ff3afd', 'keluar', 200, 'transaksi', '9aa286f3-4123-32b7-a169-4d0d8987733d', 'TRX-20261005-01', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('f8124fb2-5fca-3c69-98d3-4546052db7b6', '452bfd81-c2e4-3bbc-bdca-796c0f98c9fc', 'masuk', 8000, 'awal', NULL, 'Stok awal', 1791661210154, 'seed', 1791661210154, 1791661210154, 0, 0),
('f847e573-2d66-3770-80df-9649ecbc79f3', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('f911b830-4e7b-3258-840d-6e73155367d2', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 200, 'transaksi', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('f915a926-04ca-330a-9b01-6e874f0e640b', 'f8324eaa-c50a-3fca-83de-c8d1117e4b40', 'keluar', 10, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371582, 1791661371582, 0, 0),
('fa2aad4b-e928-3022-9868-b656f72f082d', '567af7ad-36d6-37b0-95d5-da5e41a65262', 'keluar', 15, 'transaksi', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1791661371542, 'seed', 1791661371580, 1791661371580, 0, 0),
('fada6e60-cb36-3cd3-9b62-dd10f63d7786', '7938e78b-5bbf-3ef8-98a6-cc1b2eab034c', 'keluar', 16, 'transaksi', 'dc803830-6c67-3dce-a866-67cbd4763741', 'TRX-20260930-02', 1791661371542, 'seed', 1791661371593, 1791661371593, 0, 0),
('fb08394d-8460-3ec7-bff4-bdb81a0cda52', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 20, 'transaksi', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791661371542, 'seed', 1791661371620, 1791661371620, 0, 0),
('fb379d2b-6afb-3081-acc7-c303257be85c', '794b0250-d0b1-3a3d-8ef8-45ed41191dbb', 'keluar', 160, 'transaksi', '9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'TRX-20261009-04', 1791661371542, 'seed', 1791661371662, 1791661371662, 0, 0),
('fb3bbedc-04f8-33a9-b044-413402e56d62', '22feba2c-c6c7-34c6-b0a0-e5a7b95ac120', 'keluar', 250, 'transaksi', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('fbf97164-b145-32a9-aece-6404cebd271d', '5f991edd-bb70-3cb9-b368-02a93b901562', 'keluar', 300, 'transaksi', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791661371542, 'seed', 1791661371631, 1791661371631, 0, 0),
('fc1fb4c1-9d38-3a6c-bc36-f314848ddbe8', '5f991edd-bb70-3cb9-b368-02a93b901562', 'masuk', 10000, 'pembelian', 'c81e08f2-bd3c-3ee0-b9e0-84c2e24f945a', 'PO-002', 1791661371542, 'seed', 1791661371548, 1791661371548, 0, 0),
('fdc7f643-21db-3ba2-81c8-18bd33f317a9', 'b281d40a-f934-3888-b428-9bf35178ef2a', 'keluar', 40, 'transaksi', '1cc39890-dd54-31b1-b165-685518ab101a', 'TRX-20261007-01', 1791661371542, 'seed', 1791661371639, 1791661371639, 0, 0),
('ff2a00fb-8533-3d63-bd2d-8e11d214e650', 'c62202be-fcf8-3c83-97bf-a119162d90c2', 'keluar', 30, 'transaksi', '34d15834-ac28-36ae-b040-c55144ab25c7', 'TRX-20261003-02', 1791661371542, 'seed', 1791661371614, 1791661371614, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `id` char(36) NOT NULL,
  `kode` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `alamat` varchar(200) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`id`, `kode`, `nama`, `no_hp`, `alamat`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('480e953b-d5d1-3efb-b096-aa26e03a5bf2', 'SUP-03', 'Segar Jaya Bakery', '081555444333', 'Jl. Solo KM 8, Yogyakarta', 1791661210168, 1791661210168, 0, 0),
('788c53d5-7e2f-36b4-a906-4683d72fbb68', 'SUP-02', 'Susu Murni Nusantara', '081377665544', 'Jl. Kaliurang KM 5, Sleman', 1791661210168, 1791661210168, 0, 0),
('fc9a8b77-197a-3abe-92a5-0fec9daccab2', 'SUP-01', 'Toko Kopi Makmur', '081299887766', 'Jl. Pasar Gede No. 12, Yogyakarta', 1791661210168, 1791661210168, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `sync_meta`
--

CREATE TABLE `sync_meta` (
  `kunci` varchar(50) NOT NULL,
  `nilai` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` char(36) NOT NULL,
  `kode` varchar(30) NOT NULL,
  `waktu` bigint(20) NOT NULL,
  `user_id` char(36) NOT NULL,
  `subtotal` bigint(20) NOT NULL,
  `diskon` bigint(20) NOT NULL DEFAULT 0,
  `diskon_kode` varchar(20) DEFAULT NULL,
  `total` bigint(20) NOT NULL,
  `metode` varchar(5) NOT NULL CHECK (`metode` in ('cash','qris','debit')),
  `dibayar` bigint(20) NOT NULL,
  `kembalian` bigint(20) NOT NULL,
  `ref` varchar(50) DEFAULT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'selesai',
  `device_id` char(36) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `kode`, `waktu`, `user_id`, `subtotal`, `diskon`, `diskon_kode`, `total`, `metode`, `dibayar`, `kembalian`, `ref`, `status`, `device_id`, `created_at`, `updated_at`, `is_deleted`, `dirty`) VALUES
('01ff8bb9-ec07-379e-928f-c20fd73b1439', 'TRX-20261007-06', 1791345600000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 57000, 0, NULL, 57000, 'cash', 60000, 3000, NULL, 'selesai', 'seed', 1791661371652, 1791661371652, 0, 0),
('065c5c7c-64ae-3904-95ba-bfdafbb645ac', 'TRX-20260929-02', 1790650800000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 45000, 0, NULL, 45000, 'cash', 45000, 0, NULL, 'selesai', 'seed', 1791661371580, 1791661371580, 0, 0),
('0f065ebe-437b-3dcf-8f91-5c71f167d708', 'TRX-20260928-03', 1790596800000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 68000, 6800, 'SENJA10', 61200, 'cash', 65000, 3800, NULL, 'selesai', 'seed', 1791661371566, 1791661371566, 0, 0),
('12d40dde-4e7f-3e69-bfbc-fc6441adb4e2', 'TRX-20260929-04', 1790658000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 40000, 0, NULL, 40000, 'cash', 40000, 0, NULL, 'selesai', 'seed', 1791661371586, 1791661371586, 0, 0),
('130d105b-58bc-3637-a1e6-e3fd38c6af4d', 'TRX-20261008-05', 1791468000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 50000, 0, NULL, 50000, 'qris', 50000, 0, NULL, 'selesai', 'seed', 1791661371657, 1791661371657, 0, 0),
('168e7c8a-e470-4921-bbbb-2c2ea7633026', 'TRX-20261011-S-002', 1791673370627, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 28000, 0, NULL, 28000, 'qris', 28000, 0, NULL, 'selesai', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370627, 1791673370627, 0, 0),
('19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'TRX-20260930-01', 1790751600000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 86000, 0, NULL, 86000, 'cash', 90000, 4000, NULL, 'selesai', 'seed', 1791661371589, 1791661371589, 0, 0),
('1c69a1d7-9951-3b3e-b37a-538724512826', 'TRX-20260928-02', 1790593200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 15000, 0, NULL, 15000, 'qris', 15000, 0, NULL, 'selesai', 'seed', 1791661371566, 1791661371566, 0, 0),
('1cc39890-dd54-31b1-b165-685518ab101a', 'TRX-20261007-01', 1791378000000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 48000, 0, NULL, 48000, 'qris', 48000, 0, NULL, 'selesai', 'seed', 1791661371639, 1791661371639, 0, 0),
('1e2c8d57-24f1-33dd-abbd-efee5abef4e4', 'TRX-20261007-07', 1791360000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 92000, 0, NULL, 92000, 'debit', 92000, 0, NULL, 'selesai', 'seed', 1791661371654, 1791661371654, 0, 0),
('291a634d-3480-36fd-a369-320f4ec4433b', 'TRX-20261006-02', 1791291600000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 90000, 0, NULL, 90000, 'qris', 90000, 0, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('2c76a81c-ad69-36a5-bffb-8073975d117a', 'TRX-20261003-05', 1791032400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 40000, 0, NULL, 40000, 'debit', 40000, 0, NULL, 'selesai', 'seed', 1791661371614, 1791661371614, 0, 0),
('34d15834-ac28-36ae-b040-c55144ab25c7', 'TRX-20261003-02', 1791014400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 48000, 0, NULL, 48000, 'cash', 50000, 2000, NULL, 'selesai', 'seed', 1791661371614, 1791661371614, 0, 0),
('36800b66-243a-3a04-94ba-1aa46ff677ed', 'TRX-20261010-01', 1791633600000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 52000, 0, NULL, 52000, 'debit', 52000, 0, NULL, 'selesai', 'seed', 1791661371662, 1791661371662, 0, 0),
('37c327fa-a839-32d8-89dc-afe780ba58b6', 'TRX-20261001-07', 1790856000000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 25000, 0, NULL, 25000, 'qris', 25000, 0, NULL, 'selesai', 'seed', 1791661371606, 1791661371606, 0, 0),
('3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', 'TRX-20260929-01', 1790672400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 64000, 0, NULL, 64000, 'cash', 65000, 1000, NULL, 'selesai', 'seed', 1791661371578, 1791661371578, 0, 0),
('4454302f-b1a9-32c1-a97d-43503504c8af', 'TRX-20261007-02', 1791360000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 100000, 0, NULL, 100000, 'qris', 100000, 0, NULL, 'selesai', 'seed', 1791661371639, 1791661371639, 0, 0),
('44998848-fbca-3d35-abbd-936354fd5ff1', 'TRX-20261001-01', 1790838000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 112000, 0, NULL, 112000, 'debit', 112000, 0, NULL, 'selesai', 'seed', 1791661371597, 1791661371597, 0, 0),
('475d865e-dc4f-369e-b493-1eeeafed5f6c', 'TRX-20261005-05', 1791180000000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 87000, 0, NULL, 87000, 'qris', 87000, 0, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('476fa51b-f17a-3655-8b93-a12076975953', 'TRX-20261010-02', 1791640800000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 52000, 5200, 'SENJA10', 46800, 'debit', 46800, 0, NULL, 'selesai', 'seed', 1791661371662, 1791661371662, 0, 0),
('4ae699cc-3925-33a9-a7d9-65b439abab1b', 'TRX-20261010-03', 1791615600000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 28000, 0, NULL, 28000, 'cash', 30000, 2000, NULL, 'selesai', 'seed', 1791661371662, 1791661371662, 0, 0),
('576115ca-e939-3739-8e50-17d41dd9026b', 'TRX-20261003-06', 1791000000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 110000, 0, NULL, 110000, 'cash', 110000, 0, NULL, 'selesai', 'seed', 1791661371618, 1791661371618, 0, 0),
('58d44aa6-df7f-3118-bc12-e8b2596f7518', 'TRX-20261008-04', 1791464400000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 88000, 0, NULL, 88000, 'debit', 88000, 0, NULL, 'selesai', 'seed', 1791661371657, 1791661371657, 0, 0),
('5f7b5f0c-8037-3228-91bd-21b1960ed1e5', 'TRX-20261001-02', 1790848800000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 56000, 5600, 'SENJA10', 50400, 'debit', 50400, 0, NULL, 'selesai', 'seed', 1791661371599, 1791661371599, 0, 0),
('66ec8ba9-81f8-38b6-b55e-013d9e352574', 'TRX-20261003-01', 1791028800000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 47000, 0, NULL, 47000, 'cash', 50000, 3000, NULL, 'selesai', 'seed', 1791661371613, 1791661371613, 0, 0),
('67d54733-b5a6-3f7b-a964-2e2656068f45', 'TRX-20260928-04', 1790575200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 114000, 0, NULL, 114000, 'cash', 115000, 1000, NULL, 'selesai', 'seed', 1791661371575, 1791661371575, 0, 0),
('72ffe998-7085-3782-a22e-e8830b091968', 'TRX-20261001-06', 1790863200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 24000, 0, NULL, 24000, 'qris', 24000, 0, NULL, 'selesai', 'seed', 1791661371606, 1791661371606, 0, 0),
('7509260f-8818-39b8-93d1-af1dedc88772', 'TRX-20260930-03', 1790740800000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 72000, 0, NULL, 72000, 'cash', 75000, 3000, NULL, 'selesai', 'seed', 1791661371594, 1791661371594, 0, 0),
('764089c4-87ba-39a8-b30c-6617805dd19d', 'TRX-20261009-05', 1791536400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 106000, 0, NULL, 106000, 'qris', 106000, 0, NULL, 'selesai', 'seed', 1791661371662, 1791661371662, 0, 0),
('7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'TRX-20261001-04', 1790848800000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 122000, 0, NULL, 122000, 'qris', 122000, 0, NULL, 'selesai', 'seed', 1791661371602, 1791661371602, 0, 0),
('7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', 'TRX-20261005-02', 1791201600000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 33000, 3300, 'SENJA10', 29700, 'cash', 30000, 300, NULL, 'selesai', 'seed', 1791661371629, 1791661371629, 0, 0),
('86b9150c-31db-3b98-8f7b-93f619df3b2d', 'TRX-20261007-04', 1791349200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 28000, 0, NULL, 28000, 'debit', 28000, 0, NULL, 'selesai', 'seed', 1791661371639, 1791661371639, 0, 0),
('8782cf19-39da-3080-bf35-83cdaa1ce75f', 'TRX-20261008-02', 1791457200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 34000, 0, NULL, 34000, 'cash', 35000, 1000, NULL, 'selesai', 'seed', 1791661371656, 1791661371656, 0, 0),
('87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'TRX-20261008-03', 1791432000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 49000, 0, NULL, 49000, 'debit', 49000, 0, NULL, 'selesai', 'seed', 1791661371656, 1791661371656, 0, 0),
('8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'TRX-20261005-04', 1791201600000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 116000, 11600, 'SENJA10', 104400, 'qris', 104400, 0, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('8ed27789-dc46-3dcf-9b69-062e3270cdc8', 'TRX-20261005-06', 1791169200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 88000, 0, NULL, 88000, 'debit', 88000, 0, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('98582e89-ec8d-3829-a33c-887ff5436690', 'TRX-20261006-01', 1791266400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 48000, 0, NULL, 48000, 'qris', 48000, 0, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('98876dea-57be-30cf-8167-923171d4bbe0', 'TRX-20261001-05', 1790845200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 60000, 0, NULL, 60000, 'qris', 60000, 0, NULL, 'selesai', 'seed', 1791661371604, 1791661371604, 0, 0),
('99c65152-1058-37f4-8bd8-84608d731985', 'TRX-20261004-01', 1791086400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 33000, 0, NULL, 33000, 'debit', 33000, 0, NULL, 'selesai', 'seed', 1791661371620, 1791661371620, 0, 0),
('9aa286f3-4123-32b7-a169-4d0d8987733d', 'TRX-20261005-01', 1791205200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 56000, 0, NULL, 56000, 'qris', 56000, 0, NULL, 'selesai', 'seed', 1791661371620, 1791661371620, 0, 0),
('9c7a0b57-e1c3-386c-8bdf-bf163e7ae0c8', 'TRX-20261002-01', 1790938800000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 16000, 0, NULL, 16000, 'debit', 16000, 0, NULL, 'selesai', 'seed', 1791661371607, 1791661371607, 0, 0),
('9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'TRX-20261009-04', 1791536400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 48000, 4800, 'SENJA10', 43200, 'cash', 45000, 1800, NULL, 'selesai', 'seed', 1791661371662, 1791661371662, 0, 0),
('9fd2ceb9-040b-45d0-b388-72d6127db8bf', 'TRX-20261011-S-001', 1791673370460, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 66000, 6600, 'SENJA10', 59400, 'cash', 70000, 10600, NULL, 'dibatalkan', '3cccbc1a-815e-39a2-ab64-afab77181cc9', 1791673370460, 1791673370797, 0, 0),
('a445aa20-e42d-3e44-9c1a-810bc86a73d3', 'TRX-20261007-03', 1791349200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 22000, 0, NULL, 22000, 'cash', 25000, 3000, NULL, 'selesai', 'seed', 1791661371639, 1791661371639, 0, 0),
('a53e3330-9973-3c01-a2ef-df3ffac619f4', 'TRX-20261006-03', 1791255600000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 106000, 0, NULL, 106000, 'cash', 110000, 4000, NULL, 'selesai', 'seed', 1791661371631, 1791661371631, 0, 0),
('bf91082b-0ba7-3c96-8524-0081858ad7a7', 'TRX-20261009-03', 1791522000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 67000, 0, NULL, 67000, 'qris', 67000, 0, NULL, 'selesai', 'seed', 1791661371661, 1791661371661, 0, 0),
('ccaf8d00-7957-3511-bd05-37629940b0a9', 'TRX-20261002-04', 1790949600000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 52000, 0, NULL, 52000, 'debit', 52000, 0, NULL, 'selesai', 'seed', 1791661371612, 1791661371612, 0, 0),
('d02cd129-c688-3a2b-9b3e-7144a303a139', 'TRX-20261003-03', 1791025200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 44000, 4400, 'SENJA10', 39600, 'cash', 40000, 400, NULL, 'selesai', 'seed', 1791661371614, 1791661371614, 0, 0),
('d3ae8063-905d-3748-a23b-e269833c68cb', 'TRX-20261004-03', 1791086400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 26000, 0, NULL, 26000, 'qris', 26000, 0, NULL, 'selesai', 'seed', 1791661371620, 1791661371620, 0, 0),
('d4e3988f-771f-3c4d-8fc7-f066f71c6fde', 'TRX-20261008-01', 1791428400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 12000, 0, NULL, 12000, 'qris', 12000, 0, NULL, 'selesai', 'seed', 1791661371654, 1791661371654, 0, 0),
('da2e5100-926d-3035-a6b6-5b64d5f509b2', 'TRX-20261002-02', 1790942400000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 36000, 0, NULL, 36000, 'debit', 36000, 0, NULL, 'selesai', 'seed', 1791661371608, 1791661371608, 0, 0),
('dc803830-6c67-3dce-a866-67cbd4763741', 'TRX-20260930-02', 1790769600000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 46000, 4600, 'SENJA10', 41400, 'qris', 41400, 0, NULL, 'selesai', 'seed', 1791661371593, 1791661371593, 0, 0),
('e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'TRX-20260928-01', 1790600400000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 32000, 0, NULL, 32000, 'debit', 32000, 0, NULL, 'selesai', 'seed', 1791661371566, 1791661371566, 0, 0),
('e2d86474-f578-3c10-b818-ee9d139c7518', 'TRX-20261009-01', 1791514800000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 60000, 6000, 'SENJA10', 54000, 'debit', 54000, 0, NULL, 'selesai', 'seed', 1791661371657, 1791661371657, 0, 0),
('e338d511-a365-3606-9554-56e572ed554b', 'TRX-20261002-03', 1790924400000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 82000, 0, NULL, 82000, 'debit', 82000, 0, NULL, 'selesai', 'seed', 1791661371609, 1791661371609, 0, 0),
('e5633e9f-1cb7-39e2-b12d-533734790c7f', 'TRX-20261005-03', 1791180000000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 118000, 0, NULL, 118000, 'cash', 120000, 2000, NULL, 'selesai', 'seed', 1791661371629, 1791661371629, 0, 0),
('ee998cfc-606a-3925-b8dc-3684525d8d8f', 'TRX-20261009-02', 1791518400000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 92000, 9200, 'SENJA10', 82800, 'debit', 82800, 0, NULL, 'selesai', 'seed', 1791661371657, 1791661371657, 0, 0),
('f6c9f621-5918-39bb-aff8-3fc9db2ee34d', 'TRX-20261003-04', 1791025200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 46000, 4600, 'SENJA10', 41400, 'qris', 41400, 0, NULL, 'selesai', 'seed', 1791661371614, 1791661371614, 0, 0),
('f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', 'TRX-20261004-02', 1791097200000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 82000, 8200, 'SENJA10', 73800, 'qris', 73800, 0, NULL, 'selesai', 'seed', 1791661371620, 1791661371620, 0, 0),
('fcacbf5b-9ba6-315c-af13-37419a82230b', 'TRX-20261004-04', 1791090000000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 81000, 0, NULL, 81000, 'debit', 81000, 0, NULL, 'selesai', 'seed', 1791661371620, 1791661371620, 0, 0),
('fcb29396-4f5f-3691-9663-88d4d530c596', 'TRX-20260929-05', 1790654400000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 89000, 0, NULL, 89000, 'debit', 89000, 0, NULL, 'selesai', 'seed', 1791661371587, 1791661371587, 0, 0),
('fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', 'TRX-20261001-03', 1790863200000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 44000, 0, NULL, 44000, 'qris', 44000, 0, NULL, 'selesai', 'seed', 1791661371600, 1791661371600, 0, 0),
('fed266d7-46b3-32a2-9793-f0bd39e918ee', 'TRX-20261007-05', 1791342000000, '3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 85000, 0, NULL, 85000, 'cash', 85000, 0, NULL, 'selesai', 'seed', 1791661371650, 1791661371650, 0, 0),
('ffc2af51-418a-3005-b822-ae48558d6772', 'TRX-20260929-03', 1790686800000, '3cccbc1a-815e-39a2-ab64-afab77181cc9', 127000, 0, NULL, 127000, 'cash', 130000, 3000, NULL, 'selesai', 'seed', 1791661371584, 1791661371584, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_detail`
--

CREATE TABLE `transaksi_detail` (
  `id` char(36) NOT NULL,
  `transaksi_id` char(36) NOT NULL,
  `produk_id` char(36) DEFAULT NULL,
  `nama` varchar(100) NOT NULL,
  `opsi` varchar(100) DEFAULT NULL,
  `qty` int(11) NOT NULL,
  `harga` bigint(20) NOT NULL,
  `subtotal` bigint(20) NOT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1,
  `hpp` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_detail`
--

INSERT INTO `transaksi_detail` (`id`, `transaksi_id`, `produk_id`, `nama`, `opsi`, `qty`, `harga`, `subtotal`, `created_at`, `updated_at`, `is_deleted`, `dirty`, `hpp`) VALUES
('009cd490-10ae-30a3-83b3-5874475b77a8', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', '2366d505-feda-3c55-9854-f16cdcff382f', 'Donat Gula', NULL, 1, 12000, 12000, 1791661371580, 1791661371580, 0, 0, NULL),
('01a3dfa4-1ad6-3599-9306-ceb2a1b4d70f', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Cold · Regular · Less Ice · Less Sugar', 1, 24000, 24000, 1791661371600, 1791661371600, 0, 0, NULL),
('077d8105-b2f4-3ccf-8607-0240a30d9466', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371662, 1791661371662, 0, 0, NULL),
('0b7fcf5e-5756-3f24-befe-af55dc83d01b', 'dc803830-6c67-3dce-a866-67cbd4763741', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Cold · Large · Normal Ice · Normal Sugar', 2, 23000, 46000, 1791661371593, 1791661371593, 0, 0, NULL),
('0e59479b-dfb2-3e86-b605-6e87d7a772d8', 'fdae6a2c-29ba-3e4a-b4cb-843a8095f0f6', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371600, 1791661371600, 0, 0, NULL),
('0fadec16-c1ba-36bb-b85f-41e7c8a388fe', '2c76a81c-ad69-36a5-bffb-8073975d117a', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Cold · Regular · Less Ice · Normal Sugar', 1, 24000, 24000, 1791661371614, 1791661371614, 0, 0, NULL),
('147f5250-592d-395b-965d-f49f301dcdd1', '72ffe998-7085-3782-a22e-e8830b091968', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Hot · Regular · Normal Sugar', 2, 12000, 24000, 1791661371606, 1791661371606, 0, 0, NULL),
('15111ba7-95b4-3104-8a6e-12a6da235255', '168e7c8a-e470-4921-bbbb-2c2ea7633026', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', NULL, 1, 28000, 28000, 1791673370627, 1791673370627, 0, 0, 7260),
('1519918b-b0fd-350b-a501-009c91ba1630', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 1, 32000, 32000, 1791661371590, 1791661371590, 0, 0, NULL),
('1bc36cd3-d7b8-32dd-b816-ea741fba19b6', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', '2366d505-feda-3c55-9854-f16cdcff382f', 'Donat Gula', NULL, 1, 12000, 12000, 1791661371620, 1791661371620, 0, 0, NULL),
('1e3adcc4-4efd-3969-a394-b8d46a1ffe76', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Large · Normal Ice · Less Sugar', 2, 31000, 62000, 1791661371661, 1791661371661, 0, 0, NULL),
('2079eaa7-f628-3c74-8de5-49f8e066998f', '67d54733-b5a6-3f7b-a964-2e2656068f45', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Regular · Less Ice · Normal Sugar', 1, 26000, 26000, 1791661371575, 1791661371575, 0, 0, NULL),
('20e5ccbf-cc8e-3614-9b32-2da3e77f1be0', '2c76a81c-ad69-36a5-bffb-8073975d117a', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Less Sugar', 1, 16000, 16000, 1791661371618, 1791661371618, 0, 0, NULL),
('23137d25-044b-37d7-93b6-8844176f02ac', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Cold · Regular · Less Ice · Normal Sugar', 2, 24000, 48000, 1791661371629, 1791661371629, 0, 0, NULL),
('23af34a1-2cfd-37e5-9e4b-f9209e923239', '37c327fa-a839-32d8-89dc-afe780ba58b6', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'Croissant Butter', NULL, 1, 25000, 25000, 1791661371606, 1791661371606, 0, 0, NULL),
('23c23682-528c-33b3-a16a-053f016742e7', 'e338d511-a365-3606-9554-56e572ed554b', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Less Sugar', 2, 16000, 32000, 1791661371609, 1791661371609, 0, 0, NULL),
('243b275a-b833-36b8-be00-b292409e352e', 'bf91082b-0ba7-3c96-8524-0081858ad7a7', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Regular · Normal Ice · Less Sugar', 2, 26000, 52000, 1791661371662, 1791661371662, 0, 0, NULL),
('2b2443a5-8e6d-31e4-a249-819daaf89f64', '01ff8bb9-ec07-379e-928f-c20fd73b1439', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371654, 1791661371654, 0, 0, NULL),
('2b2ca0cb-1f67-3d07-b2e4-a082e054cdc4', 'da2e5100-926d-3035-a6b6-5b64d5f509b2', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Cold · Regular · Less Ice · Less Sugar', 2, 18000, 36000, 1791661371608, 1791661371608, 0, 0, NULL),
('2befe7b3-0b4e-3494-a769-059cf64e7af2', '476fa51b-f17a-3655-8b93-a12076975953', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 2, 20000, 40000, 1791661371662, 1791661371662, 0, 0, NULL),
('2e8ed690-4f61-34a4-9fe7-eae64a60008c', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', '0f28aa41-2867-3062-b27c-223b666b18ed', 'Matcha Latte', 'Hot · Regular · Normal Sugar', 1, 26000, 26000, 1791661371656, 1791661371656, 0, 0, NULL),
('34969988-4d83-3a8e-82ff-d89b7173df43', 'd4e3988f-771f-3c4d-8fc7-f066f71c6fde', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Cold · Regular · Normal Ice · Normal Sugar', 1, 12000, 12000, 1791661371654, 1791661371654, 0, 0, NULL),
('34ad0b73-6d86-3e28-9299-947610faca2f', 'ccaf8d00-7957-3511-bd05-37629940b0a9', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Regular · Normal Ice · Normal Sugar', 2, 26000, 52000, 1791661371612, 1791661371612, 0, 0, NULL),
('34c64023-3f75-310d-acbe-50b04bac41e6', '475d865e-dc4f-369e-b493-1eeeafed5f6c', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371631, 1791661371631, 0, 0, NULL),
('38fd2164-8a1a-3db9-906b-7d6514b788b3', '291a634d-3480-36fd-a369-320f4ec4433b', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Hot · Regular · Normal Sugar', 1, 26000, 26000, 1791661371631, 1791661371631, 0, 0, NULL),
('3ab01bf4-3bb6-3b55-a6c9-88bc509ead98', '576115ca-e939-3739-8e50-17d41dd9026b', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371618, 1791661371618, 0, 0, NULL),
('3fd88918-89a4-3b59-b507-cd14c4d58ae3', '7f0c4db7-5d19-3642-8738-3d9e7bfd8e14', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Hot · Large · Normal Sugar', 1, 33000, 33000, 1791661371629, 1791661371629, 0, 0, NULL),
('41b20f58-240c-3e86-b16f-2f9ba334016b', '8782cf19-39da-3080-bf35-83cdaa1ce75f', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Cold · Large · Normal Ice · Less Sugar', 2, 17000, 34000, 1791661371656, 1791661371656, 0, 0, NULL),
('42b48d2d-aeb5-3205-bbce-b58ca4d44f5c', 'a445aa20-e42d-3e44-9c1a-810bc86a73d3', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Hot · Regular · Normal Sugar', 1, 22000, 22000, 1791661371639, 1791661371639, 0, 0, NULL),
('44908127-1b7c-3820-b23d-7881592f6db6', 'ffc2af51-418a-3005-b822-ae48558d6772', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371585, 1791661371585, 0, 0, NULL),
('4ba49cf5-a79f-3889-a8db-2008282fb0c5', 'fcb29396-4f5f-3691-9663-88d4d530c596', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Hot · Regular · Less Sugar', 1, 18000, 18000, 1791661371588, 1791661371588, 0, 0, NULL),
('4d329be0-adec-32f8-b3c9-4048d2b1998d', 'fcacbf5b-9ba6-315c-af13-37419a82230b', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 1, 24000, 24000, 1791661371620, 1791661371620, 0, 0, NULL),
('519f3e49-4635-39dc-aede-0535f6788a9c', '576115ca-e939-3739-8e50-17d41dd9026b', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Hot · Large · Normal Sugar', 2, 23000, 46000, 1791661371618, 1791661371618, 0, 0, NULL),
('51b3bd57-40cc-3d2e-a606-829cbe9bba4a', '12d40dde-4e7f-3e69-bfbc-fc6441adb4e2', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 2, 20000, 40000, 1791661371587, 1791661371587, 0, 0, NULL),
('52395c46-5674-34f8-a6e1-2d65e247a8b9', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Cold · Regular · Less Ice · Less Sugar', 2, 22000, 44000, 1791661371654, 1791661371654, 0, 0, NULL),
('5377f1d7-672f-3bf7-8d78-0eee602456e6', 'd3ae8063-905d-3748-a23b-e269833c68cb', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Hot · Regular · Normal Sugar', 1, 26000, 26000, 1791661371620, 1791661371620, 0, 0, NULL),
('546b8173-80fb-39de-babc-249d8476abfb', '130d105b-58bc-3637-a1e6-e3fd38c6af4d', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'Croissant Butter', NULL, 2, 25000, 50000, 1791661371657, 1791661371657, 0, 0, NULL),
('547ead65-1de9-3616-88a4-b27c8e26e785', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371657, 1791661371657, 0, 0, NULL),
('54dc6b64-1e19-3313-a23c-5f1915144ae2', 'ffc2af51-418a-3005-b822-ae48558d6772', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Hot · Large · Normal Sugar', 1, 27000, 27000, 1791661371584, 1791661371584, 0, 0, NULL),
('573bddd9-0879-31ca-9f51-a71ee5df9b0e', '9fd2ceb9-040b-45d0-b388-72d6127db8bf', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Cold - Large', 2, 33000, 66000, 1791673370460, 1791673370460, 0, 0, 14520),
('58942ae3-414c-3c84-825f-9b1317023264', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Cold · Regular · Less Ice · Normal Sugar', 2, 22000, 44000, 1791661371650, 1791661371650, 0, 0, NULL),
('60f80538-3ea8-3451-9382-8d8a15eb85a6', '065c5c7c-64ae-3904-95ba-bfdafbb645ac', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Hot · Large · Normal Sugar', 1, 33000, 33000, 1791661371582, 1791661371582, 0, 0, NULL),
('63f488fb-2802-36e2-b59c-f842e6dd1352', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371620, 1791661371620, 0, 0, NULL),
('64caad4d-1d9d-3ac4-86ab-b480d29f6d20', '475d865e-dc4f-369e-b493-1eeeafed5f6c', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371631, 1791661371631, 0, 0, NULL),
('6676e4f4-4fec-396c-8c54-aee71e429d10', 'fcacbf5b-9ba6-315c-af13-37419a82230b', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Cold · Regular · Less Ice · Normal Sugar', 1, 24000, 24000, 1791661371620, 1791661371620, 0, 0, NULL),
('675cf7e8-d802-3f6d-8ed1-42f7036a5c9c', 'd02cd129-c688-3a2b-9b3e-7144a303a139', '7cd624e1-6eb4-3253-a5c0-55948f1674af', 'Pisang Goreng Keju', NULL, 2, 22000, 44000, 1791661371614, 1791661371614, 0, 0, NULL),
('6a77e324-53ec-38c9-85bc-71cc5ae5f488', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 1, 32000, 32000, 1791661371566, 1791661371566, 0, 0, NULL),
('6aa5d6cd-304c-3aa8-b02f-218286b2ed13', '87c111cd-a4aa-33ac-9101-8bfc5d2ac2da', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Cold · Large · Less Ice · Less Sugar', 1, 23000, 23000, 1791661371656, 1791661371656, 0, 0, NULL),
('6c27f2e3-b9be-3410-a3cc-97cc1dc2be47', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 2, 15000, 30000, 1791661371590, 1791661371590, 0, 0, NULL),
('716247d5-d1f1-31f8-a552-28b554336fbc', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371579, 1791661371579, 0, 0, NULL),
('72257d0e-b503-3ef8-b90f-0b79c0a36041', 'fcacbf5b-9ba6-315c-af13-37419a82230b', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Cold · Large · Less Ice · Less Sugar', 1, 33000, 33000, 1791661371620, 1791661371620, 0, 0, NULL),
('7478ac27-f488-31a0-a4dc-d6e06ad08d45', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Cold · Large · Less Ice · Less Sugar', 2, 33000, 66000, 1791661371631, 1791661371631, 0, 0, NULL),
('769e5fd4-9b5d-30ab-9652-33630777116e', '98582e89-ec8d-3829-a33c-887ff5436690', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371631, 1791661371631, 0, 0, NULL),
('77935735-f9ed-3b60-a579-306e7b9ac7f6', '58d44aa6-df7f-3118-bc12-e8b2596f7518', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Cold · Large · Normal Ice · Less Sugar', 2, 29000, 58000, 1791661371657, 1791661371657, 0, 0, NULL),
('77c175db-372c-3c5b-8186-e2cfce142450', '58d44aa6-df7f-3118-bc12-e8b2596f7518', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 2, 15000, 30000, 1791661371657, 1791661371657, 0, 0, NULL),
('7b130db2-e9af-3c85-a74d-52320f3a1812', '9c7a0b57-e1c3-386c-8bdf-bf163e7ae0c8', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Normal Sugar', 1, 16000, 16000, 1791661371607, 1791661371607, 0, 0, NULL),
('83173af2-e06e-39a5-92a2-96606e4d76fd', '9aa286f3-4123-32b7-a169-4d0d8987733d', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371620, 1791661371620, 0, 0, NULL),
('85c43fd5-a75f-3b74-b841-7cc3065dfcea', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 2, 32000, 64000, 1791661371577, 1791661371577, 0, 0, NULL),
('874680c8-1004-3bc8-9eb1-33ab8c723eaa', '291a634d-3480-36fd-a369-320f4ec4433b', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 2, 32000, 64000, 1791661371631, 1791661371631, 0, 0, NULL),
('8a0180b9-d9b0-3559-b976-49f0f98dc3bb', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'Croissant Butter', NULL, 2, 25000, 50000, 1791661371629, 1791661371629, 0, 0, NULL),
('8cbc3371-ee36-37fd-a0a5-42ba049a64aa', 'f6c9f621-5918-39bb-aff8-3fc9db2ee34d', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Hot · Large · Less Sugar', 2, 23000, 46000, 1791661371614, 1791661371614, 0, 0, NULL),
('8debd398-8229-39d5-83fb-15701e13ec35', '8ed27789-dc46-3dcf-9b69-062e3270cdc8', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Cold · Regular · Less Ice · Normal Sugar', 1, 22000, 22000, 1791661371631, 1791661371631, 0, 0, NULL),
('91d24aca-cfcc-3c8b-937d-0f2f736af7bb', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Hot · Regular · Normal Sugar', 1, 18000, 18000, 1791661371603, 1791661371603, 0, 0, NULL),
('93396265-ed37-36cc-b396-0f9eb5f3cc8c', 'ee998cfc-606a-3925-b8dc-3684525d8d8f', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 2, 15000, 30000, 1791661371661, 1791661371661, 0, 0, NULL),
('9638e069-71d4-3ac9-a652-ca82642614ae', 'f955d6d9-7e9b-3cbe-881f-87567dd6fdfe', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'Croissant Butter', NULL, 2, 25000, 50000, 1791661371620, 1791661371620, 0, 0, NULL),
('96954fae-4f87-3109-bae7-71399881297b', 'e2d86474-f578-3c10-b818-ee9d139c7518', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Hot · Regular · Normal Sugar', 1, 12000, 12000, 1791661371657, 1791661371657, 0, 0, NULL),
('97d4bc97-4a00-32f0-9c30-fcb8f67cc610', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Regular · Less Ice · Normal Sugar', 1, 26000, 26000, 1791661371579, 1791661371579, 0, 0, NULL),
('97e401ee-faae-39e9-8211-70b6475576cf', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371602, 1791661371602, 0, 0, NULL),
('99016095-f3d8-3619-86e7-9d6df219ec85', '1c69a1d7-9951-3b3e-b37a-538724512826', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371566, 1791661371566, 0, 0, NULL),
('9a391a95-db6c-3e81-b2fd-6d44055b3fdd', '36800b66-243a-3a04-94ba-1aa46ff677ed', '0f28aa41-2867-3062-b27c-223b666b18ed', 'Matcha Latte', 'Cold · Regular · Less Ice · Normal Sugar', 2, 26000, 52000, 1791661371662, 1791661371662, 0, 0, NULL),
('9a7bd2fb-8047-35ec-ac43-3592fbe73d14', '44998848-fbca-3d35-abbd-936354fd5ff1', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Cold · Regular · Less Ice · Less Sugar', 1, 18000, 18000, 1791661371597, 1791661371597, 0, 0, NULL),
('a1f75d14-b5b2-381a-bafb-b979852b2b50', '4454302f-b1a9-32c1-a97d-43503504c8af', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Hot · Regular · Normal Sugar', 2, 22000, 44000, 1791661371639, 1791661371639, 0, 0, NULL),
('a30f6d2e-d612-32ab-bdc6-3169006b7e6d', 'e338d511-a365-3606-9554-56e572ed554b', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Hot · Regular · Normal Sugar', 2, 12000, 24000, 1791661371609, 1791661371609, 0, 0, NULL),
('a40ade08-e905-3346-813d-93dc37b6d854', '7d4fd3e5-ecc8-35a3-a777-655153cb3f85', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Cold · Regular · Less Ice · Normal Sugar', 2, 28000, 56000, 1791661371602, 1791661371602, 0, 0, NULL),
('a6ef85d8-b58e-3f76-ba78-3c1e518af40f', '66ec8ba9-81f8-38b6-b55e-013d9e352574', '7cd624e1-6eb4-3253-a5c0-55948f1674af', 'Pisang Goreng Keju', NULL, 1, 22000, 22000, 1791661371614, 1791661371614, 0, 0, NULL),
('a8d63336-fc56-3add-b317-0ccc6968a361', '66ec8ba9-81f8-38b6-b55e-013d9e352574', '9d6e2743-b5f1-3818-a77e-68b8f1ac9624', 'Croissant Butter', NULL, 1, 25000, 25000, 1791661371613, 1791661371613, 0, 0, NULL),
('aa9f350c-d492-3c67-a988-dce17b95db9c', 'fcb29396-4f5f-3691-9663-88d4d530c596', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Hot · Regular · Normal Sugar', 2, 28000, 56000, 1791661371587, 1791661371587, 0, 0, NULL),
('abe511ef-3ac1-3c43-b10b-30d998c2116a', '98876dea-57be-30cf-8167-923171d4bbe0', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Less Sugar', 1, 16000, 16000, 1791661371605, 1791661371605, 0, 0, NULL),
('ac163e6c-123d-367a-926a-010328c98c52', '9aa286f3-4123-32b7-a169-4d0d8987733d', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Hot · Regular · Less Sugar', 2, 18000, 36000, 1791661371620, 1791661371620, 0, 0, NULL),
('ad153836-f51c-3afa-8a7b-9e21e243892b', '764089c4-87ba-39a8-b30c-6617805dd19d', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Hot · Regular · Normal Sugar', 2, 22000, 44000, 1791661371662, 1791661371662, 0, 0, NULL),
('b254c034-66fc-3fa4-89bd-b1e921b0dafc', '0f065ebe-437b-3dcf-8f91-5c71f167d708', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Hot · Regular · Less Sugar', 1, 12000, 12000, 1791661371575, 1791661371575, 0, 0, NULL),
('b34eb4e5-2ff2-38ad-9c8a-7a6fae0b766f', '98876dea-57be-30cf-8167-923171d4bbe0', '49badec4-c17e-3a3a-85ea-6813e5fa504b', 'Kopi Susu Gula Aren', 'Cold · Regular · Normal Ice · Normal Sugar', 2, 22000, 44000, 1791661371604, 1791661371604, 0, 0, NULL),
('b34f647b-dce6-3b94-8665-a9199196fa6e', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371652, 1791661371652, 0, 0, NULL),
('b964e609-4583-3f10-9993-8583676759f6', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 2, 32000, 64000, 1791661371631, 1791661371631, 0, 0, NULL),
('ba251e48-1ab7-35e0-b774-3d38433135aa', '764089c4-87ba-39a8-b30c-6617805dd19d', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Hot · Regular · Less Sugar', 2, 18000, 36000, 1791661371662, 1791661371662, 0, 0, NULL),
('bca7259c-72b2-3a20-b15a-eb4b3bedc2ea', '475d865e-dc4f-369e-b493-1eeeafed5f6c', '2366d505-feda-3c55-9854-f16cdcff382f', 'Donat Gula', NULL, 2, 12000, 24000, 1791661371631, 1791661371631, 0, 0, NULL),
('c5cc6ced-2299-3fe9-9d64-31c00c5dae9e', 'fed266d7-46b3-32a2-9793-f0bd39e918ee', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Cold · Large · Normal Ice · Less Sugar', 1, 21000, 21000, 1791661371650, 1791661371650, 0, 0, NULL),
('c86eb694-bc39-37f9-9ff5-e9610664f6b4', '3e3bfcd9-0707-3f5d-b12b-f9be99a77e6e', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Cold · Regular · Normal Ice · Less Sugar', 1, 18000, 18000, 1791661371580, 1791661371580, 0, 0, NULL),
('c8ce7c43-e2b0-3fde-bda7-84344cfbbd71', '19bf7bac-868b-3942-87b5-7c5d18bc7a79', '2366d505-feda-3c55-9854-f16cdcff382f', 'Donat Gula', NULL, 2, 12000, 24000, 1791661371589, 1791661371589, 0, 0, NULL),
('c8e8f9b0-a594-313a-8772-162a2212340b', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', '0f28aa41-2867-3062-b27c-223b666b18ed', 'Matcha Latte', 'Hot · Regular · Normal Sugar', 1, 26000, 26000, 1791661371631, 1791661371631, 0, 0, NULL),
('cdfae304-fc2f-31d2-bb11-9086249eb94f', '99c65152-1058-37f4-8bd8-84608d731985', '4ac5bb63-f900-3dcf-8f5b-62c310ad1d0c', 'Americano', 'Cold · Regular · Less Ice · Normal Sugar', 1, 18000, 18000, 1791661371620, 1791661371620, 0, 0, NULL),
('cf542c36-1434-31a9-8c2e-c4e75ac15693', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Cold · Regular · Normal Ice · Less Sugar', 1, 16000, 16000, 1791661371639, 1791661371639, 0, 0, NULL),
('cf90d891-94ce-3ba2-b1d5-c15e43b69cca', '7509260f-8818-39b8-93d1-af1dedc88772', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 2, 20000, 40000, 1791661371595, 1791661371595, 0, 0, NULL),
('d01038ca-d939-3d05-9b8f-17845bab01df', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 2, 15000, 30000, 1791661371599, 1791661371599, 0, 0, NULL),
('d5ea7b00-c9f1-369d-93bf-69e9084bead2', '44998848-fbca-3d35-abbd-936354fd5ff1', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Hot · Large · Less Sugar', 2, 29000, 58000, 1791661371598, 1791661371598, 0, 0, NULL),
('dac73cf8-9e88-307f-9b24-e05d8dfe3098', '1e2c8d57-24f1-33dd-abbd-efee5abef4e4', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Hot · Regular · Less Sugar', 2, 24000, 48000, 1791661371654, 1791661371654, 0, 0, NULL),
('db9776a5-2643-3be5-b714-c5afd24f737f', '476fa51b-f17a-3655-8b93-a12076975953', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Cold · Regular · Less Ice · Less Sugar', 1, 12000, 12000, 1791661371662, 1791661371662, 0, 0, NULL),
('dbc10645-05a1-389f-8760-92ab4dd4339e', '67d54733-b5a6-3f7b-a964-2e2656068f45', 'c852a5d6-1fe7-3c5e-93c9-34ade57de9fa', 'Kopi Tubruk', 'Hot · Regular · Less Sugar', 2, 12000, 24000, 1791661371575, 1791661371575, 0, 0, NULL),
('deb60805-0e24-3295-91c2-a00578f27bdd', '9fb0b7a6-e5c4-3eb5-908f-a5efea7aa81a', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371662, 1791661371662, 0, 0, NULL),
('e0949f01-b4ba-32b0-8e1b-82b874e4c351', '99c65152-1058-37f4-8bd8-84608d731985', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371620, 1791661371620, 0, 0, NULL),
('e0ce3bbc-39a4-3091-b46b-00d2474bc677', '4454302f-b1a9-32c1-a97d-43503504c8af', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Cold · Regular · Normal Ice · Less Sugar', 2, 28000, 56000, 1791661371639, 1791661371639, 0, 0, NULL),
('e37afccf-7062-304a-aad7-8c50291003a0', 'a53e3330-9973-3c01-a2ef-df3ffac619f4', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 2, 32000, 64000, 1791661371631, 1791661371631, 0, 0, NULL),
('e70aaa2b-bf0d-3a5b-b884-8827a8f1568b', '1cc39890-dd54-31b1-b165-685518ab101a', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371639, 1791661371639, 0, 0, NULL),
('e794b711-f256-347c-93d7-e3c9784e2e88', '8d0d3444-464b-34df-aff2-d9f3e099c0e0', '0f28aa41-2867-3062-b27c-223b666b18ed', 'Matcha Latte', 'Hot · Regular · Normal Sugar', 2, 26000, 52000, 1791661371631, 1791661371631, 0, 0, NULL),
('e80f93ca-cdca-30ce-92ff-21d158126f1f', 'e2d3fe82-8628-3b27-80e4-0706c716f4e1', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 1, 32000, 32000, 1791661371566, 1791661371566, 0, 0, NULL),
('e8c88a74-367d-3119-b709-fd6041009813', '01ff8bb9-ec07-379e-928f-c20fd73b1439', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371652, 1791661371652, 0, 0, NULL),
('eb1bf723-14ce-36e5-80f5-ff36e596957e', '0f065ebe-437b-3dcf-8f91-5c71f167d708', '4c5490fc-5f6d-3923-9921-7f54c3c07ab3', 'Cokelat Hangat', 'Hot · Regular · Less Sugar', 1, 24000, 24000, 1791661371566, 1791661371566, 0, 0, NULL),
('ed1be013-88e2-32dc-b36c-e11f110ab07d', '5f7b5f0c-8037-3228-91bd-21b1960ed1e5', '0f28aa41-2867-3062-b27c-223b666b18ed', 'Matcha Latte', 'Hot · Regular · Less Sugar', 1, 26000, 26000, 1791661371599, 1791661371599, 0, 0, NULL),
('ee0c1c5c-91ea-30e3-89a6-2208606c35e2', 'e338d511-a365-3606-9554-56e572ed554b', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Cold · Regular · Normal Ice · Normal Sugar', 1, 26000, 26000, 1791661371612, 1791661371612, 0, 0, NULL),
('ee33c967-2902-3c30-aa95-b3a9e0f06c02', '7509260f-8818-39b8-93d1-af1dedc88772', 'd9ea31ec-4ea3-3452-8901-94f521317d3b', 'Chicken Burger', NULL, 1, 32000, 32000, 1791661371595, 1791661371595, 0, 0, NULL),
('eec20071-17dc-302b-98c5-390a65eec72b', '86b9150c-31db-3b98-8f7b-93f619df3b2d', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Less Sugar', 1, 16000, 16000, 1791661371639, 1791661371639, 0, 0, NULL),
('eee98992-5d25-374f-8a95-683de35354a9', 'fcb29396-4f5f-3691-9663-88d4d530c596', '94a1614e-041b-38ee-8d96-9624a0ea879a', 'Cireng Bumbu Rujak', NULL, 1, 15000, 15000, 1791661371588, 1791661371588, 0, 0, NULL),
('f01e5ff8-1b82-3846-b2a4-479b19201ecd', 'ffc2af51-418a-3005-b822-ae48558d6772', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Hot · Regular · Less Sugar', 2, 26000, 52000, 1791661371585, 1791661371585, 0, 0, NULL),
('f0947bf9-fcc8-3b2b-ba99-4e386d562b67', '764089c4-87ba-39a8-b30c-6617805dd19d', '6e1a921e-d387-31ba-9c4a-0f29e9786bf9', 'Cappuccino', 'Hot · Regular · Normal Sugar', 1, 26000, 26000, 1791661371662, 1791661371662, 0, 0, NULL),
('f4812598-37db-3497-83c4-ad1ae3313d40', '44998848-fbca-3d35-abbd-936354fd5ff1', 'd385d882-9a3e-3fba-a7d8-b10a5f74b022', 'Teh Tarik', 'Hot · Regular · Less Sugar', 2, 18000, 36000, 1791661371597, 1791661371597, 0, 0, NULL),
('f57067ff-3d92-3c90-ba18-3a5503e96db9', '01ff8bb9-ec07-379e-928f-c20fd73b1439', '7cd624e1-6eb4-3253-a5c0-55948f1674af', 'Pisang Goreng Keju', NULL, 1, 22000, 22000, 1791661371652, 1791661371652, 0, 0, NULL),
('f6504af4-6c80-3c96-ae89-db455665e1f7', '34d15834-ac28-36ae-b040-c55144ab25c7', 'c28ddb25-9795-370b-b83b-16d127500814', 'Roti Bakar Cokelat Keju', NULL, 2, 24000, 48000, 1791661371614, 1791661371614, 0, 0, NULL),
('f7460b33-bc88-35a3-ac9d-b89b642bb808', 'e5633e9f-1cb7-39e2-b12d-533734790c7f', '8b3287f4-5e3c-305e-85ee-109af650647b', 'Kentang Goreng', NULL, 1, 20000, 20000, 1791661371631, 1791661371631, 0, 0, NULL),
('f9b3ab17-0223-3efc-bbeb-979b3018b211', '86b9150c-31db-3b98-8f7b-93f619df3b2d', '2366d505-feda-3c55-9854-f16cdcff382f', 'Donat Gula', NULL, 1, 12000, 12000, 1791661371644, 1791661371644, 0, 0, NULL),
('fece94ec-24e7-37d7-bba5-50b37c01f7c6', '4ae699cc-3925-33a9-a7d9-65b439abab1b', '1f7ceb6f-8278-301d-ba60-31651db59cfc', 'Caramel Latte', 'Hot · Regular · Less Sugar', 1, 28000, 28000, 1791661371662, 1791661371662, 0, 0, NULL),
('ff0d4c3e-c02e-3a12-90fa-5a1776e2dc46', '576115ca-e939-3739-8e50-17d41dd9026b', '853228d1-e232-3ada-aca6-150a97589f03', 'Lemon Tea', 'Hot · Regular · Normal Sugar', 1, 16000, 16000, 1791661371620, 1791661371620, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` char(36) NOT NULL,
  `kode` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `level` varchar(10) NOT NULL CHECK (`level` in ('admin','kasir')),
  `password_hash` varchar(100) NOT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `alamat` varchar(200) DEFAULT NULL,
  `created_at` bigint(20) NOT NULL,
  `updated_at` bigint(20) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `dirty` int(11) NOT NULL DEFAULT 1,
  `status` varchar(10) NOT NULL DEFAULT 'aktif'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `kode`, `nama`, `level`, `password_hash`, `no_hp`, `alamat`, `created_at`, `updated_at`, `is_deleted`, `dirty`, `status`) VALUES
('3cccbc1a-815e-39a2-ab64-afab77181cc9', 'KSR01', 'Rani Safitri', 'kasir', '$2a$10$pdyDFGTGlxo7nHGfqBAxz.jkCvebLSxpYhhqbbByyUftRPp9zt7ui', '081234567002', 'Jl. Kenanga No. 7, Yogyakarta', 1791661210144, 1791661210144, 0, 0, 'aktif'),
('3f880ed3-d790-3ab6-8768-d76d3fb5b89a', 'KSR02', 'Bagas Pratama', 'kasir', '$2a$10$pdyDFGTGlxo7nHGfqBAxz.jkCvebLSxpYhhqbbByyUftRPp9zt7ui', '081234567003', 'Jl. Anggrek No. 21, Sleman', 1791661210144, 1791661210144, 0, 0, 'aktif'),
('4f751928-7886-3558-8bb4-684636d79d5b', 'ADM01', 'Nadia Prameswari', 'admin', '$2a$10$Ql1sM.HZGwDa/8QWPcGR3eSW4JBHByL6sGn3F7fGQTjtHPY5s3q8.', '081234567001', 'Jl. Melati No. 3, Yogyakarta', 1791661210136, 1791661210136, 0, 0, 'aktif');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`tanggal`,`shift_jenis`),
  ADD KEY `idx_absensi_d` (`dirty`),
  ADD KEY `idx_absensi_tgl` (`tanggal`);

--
-- Indexes for table `auth_tokens`
--
ALTER TABLE `auth_tokens`
  ADD PRIMARY KEY (`token_hash`),
  ADD KEY `fk_token_user` (`user_id`);

--
-- Indexes for table `bahan`
--
ALTER TABLE `bahan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `idx_bahan_d` (`dirty`);

--
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `diskon`
--
ALTER TABLE `diskon`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `idx_diskon_d` (`dirty`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `idx_kategori_d` (`dirty`);

--
-- Indexes for table `laporan_bug`
--
ALTER TABLE `laporan_bug`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bug_d` (`dirty`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `supplier_id` (`supplier_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_pembelian_d` (`dirty`);

--
-- Indexes for table `pembelian_detail`
--
ALTER TABLE `pembelian_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pembelian_id` (`pembelian_id`),
  ADD KEY `bahan_id` (`bahan_id`),
  ADD KEY `idx_pgd_d` (`dirty`);

--
-- Indexes for table `pengaturan`
--
ALTER TABLE `pengaturan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kunci` (`kunci`),
  ADD KEY `idx_set_d` (`dirty`);

--
-- Indexes for table `pengeluaran`
--
ALTER TABLE `pengeluaran`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `idx_pengeluaran_d` (`dirty`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `kategori_id` (`kategori_id`),
  ADD KEY `idx_produk_d` (`dirty`);

--
-- Indexes for table `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `produk_id` (`produk_id`,`bahan_id`),
  ADD KEY `bahan_id` (`bahan_id`),
  ADD KEY `idx_resep_d` (`dirty`);

--
-- Indexes for table `shift`
--
ALTER TABLE `shift`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`tanggal`),
  ADD KEY `idx_shift_d` (`dirty`),
  ADD KEY `idx_shift_tgl` (`tanggal`);

--
-- Indexes for table `stok_mutasi`
--
ALTER TABLE `stok_mutasi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_mutasi_d` (`dirty`),
  ADD KEY `idx_mutasi_bahan` (`bahan_id`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `idx_supplier_d` (`dirty`);

--
-- Indexes for table `sync_meta`
--
ALTER TABLE `sync_meta`
  ADD PRIMARY KEY (`kunci`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_transaksi_d` (`dirty`),
  ADD KEY `idx_trx_waktu` (`waktu`),
  ADD KEY `idx_trx_user` (`user_id`);

--
-- Indexes for table `transaksi_detail`
--
ALTER TABLE `transaksi_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_txd_d` (`dirty`),
  ADD KEY `idx_txd_trx` (`transaksi_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode` (`kode`),
  ADD KEY `idx_users_d` (`dirty`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `auth_tokens`
--
ALTER TABLE `auth_tokens`
  ADD CONSTRAINT `fk_token_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD CONSTRAINT `pembelian_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  ADD CONSTRAINT `pembelian_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `pembelian_detail`
--
ALTER TABLE `pembelian_detail`
  ADD CONSTRAINT `pembelian_detail_ibfk_1` FOREIGN KEY (`pembelian_id`) REFERENCES `pembelian` (`id`),
  ADD CONSTRAINT `pembelian_detail_ibfk_2` FOREIGN KEY (`bahan_id`) REFERENCES `bahan` (`id`);

--
-- Constraints for table `produk`
--
ALTER TABLE `produk`
  ADD CONSTRAINT `produk_ibfk_1` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`id`);

--
-- Constraints for table `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `resep_ibfk_1` FOREIGN KEY (`produk_id`) REFERENCES `produk` (`id`),
  ADD CONSTRAINT `resep_ibfk_2` FOREIGN KEY (`bahan_id`) REFERENCES `bahan` (`id`);

--
-- Constraints for table `shift`
--
ALTER TABLE `shift`
  ADD CONSTRAINT `shift_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `stok_mutasi`
--
ALTER TABLE `stok_mutasi`
  ADD CONSTRAINT `stok_mutasi_ibfk_1` FOREIGN KEY (`bahan_id`) REFERENCES `bahan` (`id`);

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `transaksi_detail`
--
ALTER TABLE `transaksi_detail`
  ADD CONSTRAINT `transaksi_detail_ibfk_1` FOREIGN KEY (`transaksi_id`) REFERENCES `transaksi` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
