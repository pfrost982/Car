package ru.gb.car.entity

class Car {
    @Volatile
    var x: Float = 0.0f
    @Volatile
    var y: Float = 0.0f
    @Volatile
    var angle: Float = 0.0f
        set(value) {
            field = if (value > 180 || value < -180) {
                if (value > 180) {
                    value - 360
                } else {
                    value + 360
                }
            } else {
                value
            }
        }
}