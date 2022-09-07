package ru.gb.car

import android.graphics.*
import android.view.SurfaceHolder
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.gb.car.entity.Car
import ru.gb.car.entity.Point
import kotlin.math.atan2

class MainViewModel : ViewModel() {
    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var carBitmap: Bitmap
    private lateinit var pointBitmap: Bitmap
    private val car = Car()
    private val point = Point()
    private val scope = CoroutineScope(Dispatchers.IO)


    fun setSurfaceHolder(surfaceHolder: SurfaceHolder) {
        this.surfaceHolder = surfaceHolder
        val canvas = surfaceHolder.lockCanvas()
        car.x = (canvas.width / 2).toFloat()
        car.y = (canvas.height / 2).toFloat()
        drawCar(canvas)
        surfaceHolder.unlockCanvasAndPost(canvas)

    }

    fun setBitmap(car: Bitmap, point: Bitmap) {
        carBitmap = car
        pointBitmap = point
    }

    fun newPoint(x: Float, y: Float) {
        point.x = x
        point.y = y
        carToPoint(surfaceHolder)
    }

    private fun carToPoint(surfaceHolder: SurfaceHolder?) {
        scope.launch {
            val angle = atan2((point.y - car.y), (point.x - car.x)) * 180.0f / 3.14159f
            car.angle = angle

            point.angle = 0f
            val dPointAngle = 360f / 100

            val dx = (point.x - car.x) / 100
            val dy = (point.y - car.y) / 100
            for (i in 1..100) {
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    render(canvas)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
                car.x += dx
                car.y += dy
                point.angle += dPointAngle
            }
        }
    }

    private fun render(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawPoint(canvas)
        drawCar(canvas)
    }

    private fun drawCar(canvas: Canvas) {
        val matrix = Matrix()
        matrix.postScale(0.1f, 0.1f)
        matrix.postRotate(car.angle + 90)

        val rotatedBitmap =
            Bitmap.createBitmap(carBitmap, 0, 0, carBitmap.width, carBitmap.height, matrix, false)
        canvas.drawBitmap(
            rotatedBitmap,
            car.x - rotatedBitmap.width.toFloat() / 2,
            car.y - rotatedBitmap.height.toFloat() / 2,
            null
        )
    }

    private fun drawPoint(canvas: Canvas) {
        val matrix = Matrix()
        matrix.postScale(0.1f, 0.1f)
        matrix.postRotate(point.angle)

        val rotatedBitmap =
            Bitmap.createBitmap(
                pointBitmap,
                0,
                0,
                pointBitmap.width,
                pointBitmap.height,
                matrix,
                false
            )
        canvas.drawBitmap(
            rotatedBitmap,
            point.x - rotatedBitmap.width.toFloat() / 2,
            point.y - rotatedBitmap.height.toFloat() / 2,
            null
        )
    }

    override fun onCleared() {
        surfaceHolder = null
        super.onCleared()
    }
}