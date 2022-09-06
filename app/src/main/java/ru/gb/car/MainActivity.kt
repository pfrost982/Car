package ru.gb.car

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gb.car.databinding.ActivityMainBinding
import ru.gb.car.entity.Car
import ru.gb.car.entity.Point

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var surfaceHolder: SurfaceHolder? = null

    private lateinit var carBitmap: Bitmap
    private lateinit var pointBitmap: Bitmap

    private val car = Car()
    private val point = Point()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                this@MainActivity.surfaceHolder = surfaceHolder
                val canvas =surfaceHolder.lockCanvas()
                drawCar(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)

            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                this@MainActivity.surfaceHolder = surfaceHolder
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                this@MainActivity.surfaceHolder = null
            }
        })
        carBitmap = BitmapFactory.decodeResource(resources, R.drawable.car)
        pointBitmap = BitmapFactory.decodeResource(resources, R.drawable.point)

        binding.surface.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                point.x = motionEvent.x
                point.y = motionEvent.y
                carToPoint(surfaceHolder)
/*
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    render(canvas)
                    surfaceHolder?.unlockCanvasAndPost(canvas)
                }
*/
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener false
            }
        }
    }

    private fun carToPoint(surfaceHolder: SurfaceHolder?) {
        GlobalScope.launch {
            val dx = (point.x - car.x) / 100
            val dy = (point.y - car.y) / 100
            for (i in 1..100) {
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    render(canvas)
                    surfaceHolder?.unlockCanvasAndPost(canvas)
                }
                car.x += dx
                car.y += dy
                delay(100)
            }
        }
    }

    private fun render(canvas: Canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawCar(canvas)
        drawPoint(canvas)
    }

    private fun drawCar(canvas: Canvas) {
        val matrix = Matrix()
        matrix.postScale(0.1f, 0.1f)
        matrix.postRotate(car.angle)

        val rotatedBitmap =
            Bitmap.createBitmap(carBitmap, 0, 0, carBitmap.width, carBitmap.height, matrix, false)
        canvas.drawBitmap(
            rotatedBitmap,
            car.x - rotatedBitmap.width.toFloat() / 2, car.y - rotatedBitmap.height.toFloat() / 2, null
        )
    }

    private fun drawPoint(canvas: Canvas) {
        val matrix = Matrix()
        matrix.postScale(0.1f, 0.1f)

        val rotatedBitmap =
            Bitmap.createBitmap(pointBitmap, 0, 0, pointBitmap.width, pointBitmap.height, matrix, false)
        canvas.drawBitmap(
            rotatedBitmap,
            point.x - rotatedBitmap.width.toFloat() / 2, point.y - rotatedBitmap.height.toFloat() / 2, null
        )
    }
}