-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 04, 2024 at 03:10 PM
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
(20, 'Drink'),
(22, 'Gift'),
(27, 'Charity'),
(28, 'Others'),
(29, 'Transport');

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
(40, 'Expense', '2024-05-26', 55, 'TWD', 'Drink', ''),
(43, 'Expense', '2024-05-28', 50, 'TWD', 'Food', ''),
(44, 'Expense', '2024-05-28', 100, 'TWD', 'Food', ''),
(47, 'Expense', '2024-05-27', 85, 'TWD', 'Food', ''),
(50, 'Expense', '2024-05-29', 100, 'TWD', 'Food', ''),
(56, 'Income', '2024-05-26', 100, 'TWD', 'Gift', ''),
(58, 'Expense', '2024-04-30', 130, 'TWD', 'Food', ''),
(59, 'Expense', '2024-04-29', 60, 'TWD', 'Drink', ''),
(60, 'Expense', '2024-04-25', 50, 'TWD', 'Laundry', ''),
(62, 'Income', '2024-05-27', 100, 'TWD', 'Drink', ''),
(63, 'Expense', '2024-05-28', 340, 'TWD', 'Charity', ''),
(64, 'Expense', '2024-05-29', 300, 'TWD', 'Food', ''),
(65, 'Expense', '2024-05-28', 230, 'TWD', 'Gift', ''),
(66, 'Expense', '2024-05-29', 55, 'TWD', 'Drink', ''),
(67, 'Expense', '2024-05-27', 200, 'SGD', 'Laundry', ''),
(68, 'Expense', '2024-05-31', 80, 'TWD', 'Food', ''),
(69, 'Expense', '2024-05-31', 570, 'TWD', 'Makeup', 'pudding pot'),
(70, 'Expense', '2024-06-01', 390, 'TWD', 'Food', ''),
(71, 'Expense', '2024-06-01', 449, 'TWD', 'Gift', ''),
(72, 'Expense', '2024-06-01', 200, 'TWD', 'Transport', ''),
(73, 'Expense', '2024-06-02', 104, 'TWD', 'Food', ''),
(74, 'Expense', '2024-06-03', 50, 'TWD', 'Food', ''),
(75, 'Expense', '2024-06-03', 25, 'TWD', 'Drink', ''),
(76, 'Expense', '2024-06-04', 50, 'TWD', 'Food', ''),
(77, 'Expense', '2024-06-04', 15, 'TWD', 'Drink', '');

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
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `currencylist`
--
ALTER TABLE `currencylist`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
