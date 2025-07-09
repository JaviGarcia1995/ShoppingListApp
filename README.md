# 🛒 ShoppingListApp

A modern Android shopping list application built with **Clean Architecture**, **MVVM**, and **Jetpack Compose**. While functional as a note-taking shopping list app, its **primary value lies as a comprehensive template** for Android developers seeking to implement best practices in modern Android development.

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-2196F3?style=for-the-badge&logo=android&logoColor=white)
![Testing](https://img.shields.io/badge/Tests-Included-4EAA25?style=for-the-badge)

</div>
</br>
<div align="center">
  <img src="https://github.com/user-attachments/assets/349d365b-4bab-4897-9a21-14f8b49c5d64" width="250"/>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/7b3f19dc-1fa5-4531-9f42-d39e3bac90ba" width="250"/>
</div>

## 🎯 Project Purpose

This project serves as a **production-ready template** demonstrating:
- **Clean Architecture** implementation with proper layer separation
- **MVVM pattern** with ViewModels and Use Cases
- **SOLID principles** applied throughout the codebase
- **Comprehensive testing** structure

## ✨ Features

### 📱 App Functionality
- ✅ Create and manage multiple shopping lists
- ✅ Add, edit, and delete items within lists
- ✅ Mark items as completed with visual feedback
- ✅ Swipe-to-delete functionality for lists and items
- ✅ Confirmation dialogs for destructive actions
- ✅ Responsive UI with Material Design 3
- ✅ Multilingual support (English/Spanish)

### 🏗️ Architecture Features
- ✅ **Clean Architecture** with 4 distinct modules
- ✅ **Use Cases** for business logic encapsulation
- ✅ **Repository Pattern** for data abstraction
- ✅ **Dependency Injection** with Hilt
- ✅ **Type-safe Navigation** with Compose Navigation
- ✅ **Reactive Programming** with Kotlin Flows
- ✅ **Local Database** with Room persistence
- ✅ **Test-Ready Architecture:** Designed for testability with Unit and Integration tests.

### 📂 Module Structure

- **`app/`** - Presentation layer with Compose UI, ViewModels, and Navigation
- **`domain/`** - Business logic with Use Cases, Models, and Repository interfaces
- **`data/`** - Data layer with Repository implementations and Data Sources
- **`framework/`** - Infrastructure layer with Room database, DAOs, and Entities

## 📜 Docs
- [Android Architecture Guidelines](https://developer.android.com/topic/architecture)
- [Clean Architecture by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
