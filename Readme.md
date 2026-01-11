# 📱 Task Master - Менеджер задач для Android

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

Простое и элегантное приложение для управления задачами, созданное с использованием современных технологий Android разработки.

## ✨ Возможности

- ✅ **Добавление задач** с категориями
- 📂 **Фильтрация** по категориям (Общее, Работа, Личное, Покупки)
- ✏️ **Редактирование** существующих задач
- 🗑️ **Удаление** задач с подтверждением
- ☑️ **Отметка выполнения** задач
- 🎨 **Современный UI** с Material Design 3

## 🛠️ Технологии

- **Язык:** Kotlin
- **UI Framework:** Jetpack Compose
- **Архитектура:** MVVM (Model-View-ViewModel)
- **Локальное хранилище:** Room Database
- **Асинхронность:** Kotlin Coroutines, Flow
- **DI:** Hilt (Dagger)
- **Навигация:** Compose Navigation

## 🚀 Быстрый старт

### Установка
1. Клонируйте репозиторий:
```bash
git clone https://github.com/ваш-ник/MyTaskApp.git
```
**🧩 Основные компоненты**
**Модель данных**
```kotlin
data class Task(
val id: Int,
val title: String,
val isCompleted: Boolean = false,
val category: String = "Общее",
val createdAt: Long = System.currentTimeMillis()
)
```
**Основной экран**
```kotlin
@Composable
fun TaskScreen(
viewModel: TaskViewModel = viewModel()
) {
val tasks by viewModel.tasks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Добавить задачу */ }) {
                Icon(Icons.Default.Add, "Добавить")
            }
        }
    ) { padding ->
        // ... UI код
    }
}
```
**📊 Архитектура**
```text
Пользовательский интерфейс (UI Layer)
↑
| (События)
↓
ViewModel (Presentation Layer)
↑
| (Flow/LiveData)
↓
Repository (Domain Layer)
↑
| (Suspend функции)
↓
DataSource (Data Layer)
↑
| (Room/SQL)
↓
Local Database
```
**📝 Планы развития**
- **Синхронизация с облаком** 
- **Напоминания о задачах**
- **Резервное копирование**
- **Виджеты для домашнего экрана**

**📚 Чему я научился**
- **Работу с Jetpack Compose и Material Design 3**
- **Архитектуру MVVM с ViewModel и StateFlow**
- **Локальное хранение данных с Room**
- **Асинхронные операции с Coroutines**
- **Dependency Injection с Hilt**
- **Навигацию в Compose**
- **Создание адаптивного UI**

**👨‍💻 Автор**
Алексей

**GitHub:** AlexejMatushkin

**Telegram:** [**@AlexxMat**](https://t.me/AlexxMat)

**Email:** [**matushkin.leha2018@gmail.com**](mailto:matushkin.leha2018@gmail.com)