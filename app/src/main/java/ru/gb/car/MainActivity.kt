package ru.gb.car

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.gb.car.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var surfaceHolder: SurfaceHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val callback = object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                this@MainActivity.surfaceHolder = surfaceHolder
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
        }

        binding.surface.holder.addCallback(callback)

        GlobalScope.launch {
            while (true) {
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    drawCar(canvas)
                    surfaceHolder?.unlockCanvasAndPost(canvas)

                }
            }
        }
    }

    private fun drawCar(canvas: Canvas) {
        val carBitmap = BitmapFactory.decodeResource(resources, R.drawable.car)
        val matrix = Matrix()
        matrix.postScale(0.3f, 0.3f)
        matrix.postRotate(15f)

        val rotatedBitmap =
            Bitmap.createBitmap(carBitmap, 0, 0, carBitmap.width, carBitmap.height, matrix, true)
        canvas.drawBitmap(rotatedBitmap, 100f, 510f, null)
    }
}