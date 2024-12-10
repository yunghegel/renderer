package org.yunghegel.gdx.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.sin
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

fun oscillate(t: Float, period: Float, amplitude: Float): Float {
    return amplitude * Math.sin(2 * Math.PI * t / period).toFloat()
}

fun Matrix4.oscillate(t: Float, period: Float, amplitude: Float, axis: Int) {
    val value = oscillate(t, period, amplitude)
    when (axis) {
        0 -> this.translate(value, 0f, 0f)
        1 -> this.translate(0f, value, 0f)
        2 -> this.translate(0f, 0f, value)
    }
}

class Oscillator(private val amplitude: Float, private val speed: Float) {
    private var time: Float = 0f

    fun update(position: Vector3) {
        time += Gdx.graphics.deltaTime * speed
        val oscillationX = amplitude * sin(time)
        val oscillationY = amplitude * sin(time + Math.PI.toFloat() / 2) // Phase shift for Y
        val oscillationZ = amplitude * sin(time + Math.PI.toFloat()) // Phase shift for Z

        position.set(oscillationX, oscillationY, oscillationZ) // Update all axes
    }

    fun update(matrix: Matrix4) {
        time += Gdx.graphics.deltaTime * speed
        matrix.oscillate(time, 1f, amplitude, 0)
        matrix.oscillate(time, 1f, amplitude, 1)
        matrix.oscillate(time, 1f, amplitude, 2)
    }
}
