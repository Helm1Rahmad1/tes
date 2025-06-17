rm -rf bin

javac -d bin -cp "lib/*" src/**/*.java

cp -r src/assets bin/assets

java -cp "bin:lib/*" main.Main

# Collect The Skill Balls - Ultra Edition

A modern fantasy magic-themed desktop game built with Java Swing, featuring advanced UI design and engaging gameplay mechanics.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [Build & Run](#build--run)
- [Project Structure](#project-structure)
- [Academic Integrity Statement](#academic-integrity-statement)
- [Contributing](#contributing)
- [License](#license)

## 🎮 Overview

"Collect The Skill Balls - Ultra Edition" is a desktop game that combines fantasy magic themes with modern UI design principles. Players control a character to shoot lassos, collect skill balls for points, and avoid dangerous black gems (bombs). The game features a sophisticated scoring system with persistent leaderboards stored in MySQL database.

## ✨ Features

### 🎨 Modern UI Design
- **Neumorphism & Glassmorphism**: Ultra-modern visual design combining both design trends
- **Interactive Visual Effects**: Animated particle systems in backgrounds
- **Smooth Animations**: Physics-based spring animations and seamless transitions
- **Micro-interactions**: Enhanced user experience with hover effects and responsive elements

### 🎯 Gameplay
- **Character Control**: Navigate and aim your character strategically
- **Lasso Mechanics**: Shoot lassos to collect skill balls
- **Obstacle Avoidance**: Dodge dangerous black gems (bombs)
- **Scoring System**: Earn points by collecting skill balls

### 🏆 Progression & Records
- **Hall of Fame**: Persistent leaderboard system
- **MySQL Integration**: Scores saved to database
- **Player Statistics**: Track performance over time

### 🔊 Audio Experience
- **Background Music**: Immersive soundtracks for menu and gameplay
- **Sound Effects**: Interactive audio feedback

## 🏗️ Architecture

This project implements the **Model-View-ViewModel (MVVM)** architectural pattern for clean separation of concerns:

- **Model**: Data handling and business logic
- **View**: User interface presentation
- **ViewModel**: Bridge between Model and View, manages UI state and presentation logic

## 📋 Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 11 or higher
- **MySQL Server**: Running MySQL instance
- **MySQL Connector/J**: JDBC driver for MySQL (place in `lib/` folder)

## 🚀 Installation & Setup

1. **Clone the repository** (or extract the project files)
2. **Ensure MySQL Server is running**
3. **Place MySQL Connector/J** `.jar` file in the `lib/` directory

## 🗄️ Database Configuration

The application uses a MySQL database named `game_scores_db`. On first run, the application will automatically:

1. Create the database if it doesn't exist
2. Create the `thasil` table with the following schema:
   - `username` (VARCHAR)
   - `skor` (INT)
   - `count` (INT)
   - `created_at` (TIMESTAMP)
   - `updated_at` (TIMESTAMP)
3. Insert sample data if the table is empty

### Database Connection Settings
Default configuration (modify in `src/model/Database.java` if needed):
- **Host**: localhost
- **User**: root
- **Password**: (empty)
- **Database**: game_scores_db

## 🔧 Build & Run

### Using Command Line

1. **Clean previous builds** (optional):
   ```bash
   rm -rf bin
   ```

2. **Compile the source code**:
   ```bash
   javac -d bin -cp "lib/*" src/**/*.java
   ```

3. **Copy assets**:
   ```bash
   cp -r src/assets bin/assets
   ```

4. **Run the application**:
   ```bash
   java -cp "bin:lib/*" main.Main
   ```

### Quick Start Script
You can also create a shell script with all commands:
```bash
#!/bin/bash
rm -rf bin
javac -d bin -cp "lib/*" src/**/*.java
cp -r src/assets bin/assets
java -cp "bin:lib/*" main.Main
```

## 📁 Project Structure

```
├── src/
│   ├── main/                 # Application entry point
│   ├── model/               # Data models and business logic
│   │   ├── GameData.java
│   │   ├── Player.java
│   │   └── Database.java
│   ├── view/                # User interface components
│   │   ├── MainMenuView.java
│   │   ├── GameView.java
│   │   └── [custom UI components]
│   ├── viewmodel/           # Presentation logic
│   │   ├── MainMenuViewModel.java
│   │   └── GameViewModel.java
│   ├── utils/               # Utility classes
│   │   ├── AssetLoader.java
│   │   └── GameConstants.java
│   └── assets/              # Game resources
│       ├── images/
│       └── sounds/
├── lib/                     # External libraries
│   └── mysql-connector-j-*.jar
└── bin/                     # Compiled classes (generated)
```

## 📜 Academic Integrity Statement

**Janji Kejujuran Akademik**

Saya, Muhammad Helmi Rahmadi, mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi Objek. Dengan ini saya menyatakan bahwa saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## 🤝 Contributing

**Primary Developer**: Muhammad Helmi Rahmadi

## 📄 License

This project is developed as part of an academic assignment for Object-Oriented Design and Programming course.

---

**Note**: This project demonstrates advanced Java Swing capabilities combined with modern UI design principles and robust architectural patterns. It serves as an excellent example of applying MVVM architecture in desktop application development.