package ru.gb.car

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.gb.car.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var surfaceHolder: SurfaceHolder? = null
    lateinit var carBitmap: Bitmap

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                this@MainActivity.surfaceHolder = surfaceHolder
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                this@MainActivity.surfaceHolder = surfaceHolder
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                this@MainActivity.surfaceHolder = null
            }
        })
        carBitmap = BitmapFactory.decodeResource(resources, R.drawable.car)


        binding.surface.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    drawCar(
                        canvas,
                        motionEvent.x,
                        motionEvent.y,
                        (0..360).random().toFloat()
                    )
                    surfaceHolder?.unlockCanvasAndPost(canvas)
                }
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener false
            }
        }
    }

    private fun drawCar(canvas: Canvas, x: Float, y: Float, angle: Float) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val matrix = Matrix()
        matrix.postScale(0.1f, 0.1f)
        matrix.postRotate(angle)

        val rotatedBitmap =
            Bitmap.createBitmap(carBitmap, 0, 0, carBitmap.width, carBitmap.height, matrix, false)
        canvas.drawBitmap(
            rotatedBitmap,
            x - rotatedBitmap.width.toFloat() / 2, y - rotatedBitmap.height.toFloat() / 2, null
        )
    }
}