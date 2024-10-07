package com.cursokotlin.eco

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class ProgressDrawable(private val context: Context) : Drawable() {

    private var mPaint: Paint = Paint()

    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        val b: Rect = bounds
        val width = b.width()
        val height = b.height()

        for (i in 0 until 100) {
            val ratio = i / 100f // Calcula la proporción de la posición actual
            val left = (ratio * width).toInt()
            val right = ((i + 1) / 100f * width).toInt()

            // Calcular el color de manera suave de verde a rojo
            val color = blendColors(
                ContextCompat.getColor(context, android.R.color.holo_green_light),
                ContextCompat.getColor(context, android.R.color.holo_red_light),
                ratio
            )
            mPaint.color = color

            canvas.drawRect(left.toFloat(), b.top.toFloat(), right.toFloat(), b.bottom.toFloat(), mPaint)
        }
    }

    // Función para mezclar dos colores basados en la proporción
    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val r = Color.red(from) * inverseRatio + Color.red(to) * ratio
        val g = Color.green(from) * inverseRatio + Color.green(to) * ratio
        val b = Color.blue(from) * inverseRatio + Color.blue(to) * ratio
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    companion object {
        private const val NUM_RECTS = 6
    }
}
