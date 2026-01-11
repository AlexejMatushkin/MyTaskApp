package com.practicum.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.myapplication.presentation.screen.task.TaskActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var textCounter: TextView
    private lateinit var buttonIncrement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Вызывается ОДИН РАЗ - при создании Activity
        // Здесь: подключение разметки, инициализация viewModel
        setContentView(R.layout.activity_main)
        println("MainActivity: onCreate()")

        val intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)


        // Находим View по ID
        textCounter = findViewById(R.id.textCounter)
        buttonIncrement = findViewById(R.id.buttonIncrement)

        // Получаем ViewModel
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Наблюдаем за изменениями
        viewModel.clickCounter.observe(this) { count ->
            textCounter.text =  "Счётчик: $count"
        }

        // Обработчик клика
        buttonIncrement.setOnClickListener {
            viewModel.increment()
        }
    }

    override fun onStart() {
        super.onStart()
        // Activity становится видимой (но ещё не активна)
        println("MainActivity: onStart()")
    }

    override fun onResume() {
        super.onResume()
        // Activity находится на переднем плане, пользователь взаимодействует
        // Здесь: запуск анимаций, обновление данных в реальном времени
        println("MainActivity: onResume()")
        viewModel.increment()
    }

    override fun onPause() {
        super.onPause()
        // Пользователь уходит (например, открывает другое приложение)
        // Здесь: сохранение данных, остановка анимаций
        println("MainActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        // Activity больше не видна
        println("MainActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Activity уничтожается - освобождение ресурсов
        println("MainActivity: onDestroy()")
    }
}