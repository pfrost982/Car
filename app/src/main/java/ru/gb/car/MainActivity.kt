package ru.gb.car

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.gb.car.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                viewModel.setSurfaceHolder(surfaceHolder)
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                viewModel.setSurfaceHolder(surfaceHolder)
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
        })
        viewModel.setBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.car),
            BitmapFactory.decodeResource(resources, R.drawable.point)
        )

        binding.surface.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                viewModel.newPoint(motionEvent.x, motionEvent.y)
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener false
            }
        }
    }
}