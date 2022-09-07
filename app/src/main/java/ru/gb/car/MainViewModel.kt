package ru.gb.car

import android.graphics.*
import android.view.SurfaceHolder
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.gb.car.entity.Car
import ru.gb.car.entity.Point
import kotlin.math.*

class MainViewModel : ViewModel() {
    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var carBitmap: Bitmap
    private lateinit var pointBitmap: Bitmap
    private val car = Car()
    private val point = Point()
    private val matrix = Matrix()
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
            while (distance() > 50) {
                val canvas = surfaceHolder?.lockCanvas()
                if (canvas != null) {
                    render(canvas)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
                car.x = car.x + MACHINE_SPEED * cos(car.angle * 3.14159f / 180.0f)
                car.y = car.y + MACHINE_SPEED * sin(car.angle * 3.14159f / 180.0f)
                car.angle = car.angle + dAngle()

                point.angle += POINT_ROTATION_SPEED
            }
        }
    }

    private fun dAngle(): Float {
        val angle = atan2((point.y - car.y), (point.x - car.x)) * 180.0f / 3.14159f
        if (abs(angle - car.angle) < 1f) {
            car.angle = angle
            return 0f
        }
        if (abs(angle - car.angle) < 180) {
            if ((angle - car.angle) < 0) return MAX_ANGLE_OF_ROTATION * -1
            if ((angle - car.angle) > 0) return MAX_ANGLE_OF_ROTATION
        }
        if (abs(angle - car.angle) >= 180) {
            if ((angle - car.angle) < 0) return MAX_ANGLE_OF_ROTATION
            if ((angle - car.angle) > 0) return MAX_ANGLE_OF_ROTATION * -1
        }
        return 0f
    }

    private fun distance() =
        sqrt((point.x - car.x) * (point.x - car.x) + (point.y - car.y) * (point.y - car.y))

    private fun render(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawPoint(canvas)
        drawCar(canvas)
    }

    private fun drawCar(canvas: Canvas) {
        matrix.setRotate(car.angle + 90)
        matrix.postScale(0.1f, 0.1f)

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
        matrix.setRotate(point.angle)
        matrix.postScale(0.1f, 0.1f)

        val rotatedBitmap =
            Bitmap.createBitmap(
                pointBitmap, 0, 0, pointBitmap.width, pointBitmap.height, matrix, false
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

    companion object {
        const val MAX_ANGLE_OF_ROTATION = 1f
        const val MACHINE_SPEED = 5
        const val POINT_ROTATION_SPEED = 3f
    }
}