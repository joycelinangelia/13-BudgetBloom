-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 26, 2024 at 04:26 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `logrecord`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `ID` int(11) NOT NULL,
  `Name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`ID`, `Name`) VALUES
(16, 'Food'),
(17, 'Makeup'),
(18, 'Laundry'),
(19, 'Game'),
(20, 'Drink'),
(22, 'Gift'),
(24, 'Pet');

-- --------------------------------------------------------

--
-- Table structure for table `currencylist`
--

CREATE TABLE `currencylist` (
  `ID` int(11) NOT NULL,
  `Currency` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `currencylist`
--

INSERT INTO `currencylist` (`ID`, `Currency`) VALUES
(1, 'TWD');

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

CREATE TABLE `log` (
  `ID` int(11) NOT NULL,
  `Type` text NOT NULL,
  `Date` date NOT NULL,
  `Amount` double NOT NULL,
  `Currency` text NOT NULL,
  `Category` text NOT NULL,
  `Note` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `log`
--

INSERT INTO `log` (`ID`, `Type`, `Date`, `Amount`, `Currency`, `Category`, `Note`) VALUES
(33, 'Expense', '2024-05-26', 120, 'TWD', 'Food', ''),
(34, 'Expense', '2024-05-26', 13000, 'KRW', 'Makeup', ''),
(36, 'Expense', '2024-05-26', 50, 'TWD', 'Laundry', ''),
(37, 'Expense', '2024-05-26', 100, 'SGD', 'Game', 'mystbloom bundle'),
(40, 'Expense', '2024-05-26', 55, 'TWD', 'Drink', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `currencylist`
--
ALTER TABLE `currencylist`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `currencylist`
--
ALTER TABLE `currencylist`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
