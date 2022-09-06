package ru.gb.car

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.gb.car.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
*/
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
    }
}