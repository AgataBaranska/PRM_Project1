package com.example.financialmanager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.collections.LinkedHashMap

class ChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint().apply { color = Color.BLACK }
    var dataMap = LinkedHashMap<Int, Double>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            resources.displayMetrics
        )

        if (dataMap.isNotEmpty()) {

            val borderAxisDistance = 150f
            var borderLabelsDistance = 1f

            val verGraphHeight = height - borderAxisDistance
            val horGraphWidth = width - borderAxisDistance
            val halfNumberWidth = 20f
            val scaleLinesHeight = 50f


            val maxAmount = dataMap.values.maxOrNull()
            var minAmount = dataMap.values.minOrNull()


            if (minAmount!! >= 0.0) {
                minAmount = 0.0
            }
            var ratAmount = 1.0
            if (maxAmount != minAmount) {
                ratAmount = (verGraphHeight - borderAxisDistance) / (maxAmount!! - minAmount)
            }
            val horizontalChartAxisY = (verGraphHeight + minAmount * ratAmount).toFloat()
            val horizontalAxisLabelsY =
                (height - borderLabelsDistance + ratAmount * minAmount).toFloat()

            val maxDay = dataMap.keys.maxOrNull()
            val ratDay = ((horGraphWidth - borderAxisDistance) / maxDay!!)
            paint.color = Color.GRAY
            paint.textSize = 40f
            paint.strokeWidth = 10f

            var previousPointX: Float = 0f
            var previousPointY: Float = 0f

            for ((day, amount) in dataMap) {
                //horizontal axis scale lines
                canvas?.drawLine(
                    (ratDay * day) + borderAxisDistance,
                    horizontalChartAxisY - scaleLinesHeight,
                    (ratDay * day) + borderAxisDistance,
                    horizontalChartAxisY + scaleLinesHeight,
                    paint
                )

                //horizontal axis labels
                canvas?.drawText(
                    day.toString(),
                    (ratDay * day + borderAxisDistance) - halfNumberWidth,
                    horizontalAxisLabelsY,
                    paint
                )

                // vertical axis scale lines
                canvas?.drawLine(
                    borderAxisDistance - scaleLinesHeight,
                    (horizontalChartAxisY - (amount * ratAmount)).toFloat(),
                    borderAxisDistance + scaleLinesHeight,
                    (horizontalChartAxisY - (amount * ratAmount)).toFloat(),
                    paint
                )

                //vertical axis scale labels
                canvas?.drawText(
                    amount.toString(),
                    borderLabelsDistance,
                    (horizontalChartAxisY - (amount) * ratAmount).toFloat() + halfNumberWidth,
                    paint
                )

                //values
                var currentPointX = day * ratDay + borderAxisDistance
                var currentPointY = horizontalChartAxisY - (amount * ratAmount).toFloat()
                if (previousPointX == 0f || previousPointY == 0f) {
                    previousPointX = borderAxisDistance
                    previousPointY = currentPointY
                }


                //currentPointY and previousPointY both above x axis line
                if (currentPointY <= horizontalChartAxisY && previousPointY <= horizontalChartAxisY) {
                    paint.color = Color.GREEN
                    canvas?.drawLine(
                        previousPointX,
                        previousPointY,
                        currentPointX,
                        currentPointY,
                        paint
                    )
                    paint.color = Color.GRAY
                    //previousPointY below and currentPointY below axis x line
                } else if (currentPointY > horizontalChartAxisY && previousPointY > horizontalChartAxisY) {
                    paint.color = Color.RED
                    canvas?.drawLine(
                        previousPointX,
                        previousPointY,
                        currentPointX,
                        currentPointY,
                        paint
                    )
                    paint.color = Color.GRAY

                    //previousPointY above and currentPointY below the axis x line
                } else if (currentPointY > horizontalChartAxisY && previousPointY <= horizontalChartAxisY) {
                    paint.color = Color.GREEN
                    val a = (currentPointX - previousPointX) / (currentPointY - previousPointY)
                    val intersectionWithHorizontalScaleLineX =
                        (a * horizontalChartAxisY) + (currentPointX - a * currentPointY)

                    canvas?.drawLine(
                        previousPointX,
                        previousPointY,
                        intersectionWithHorizontalScaleLineX,
                        horizontalChartAxisY,
                        paint
                    )
                    paint.color = Color.RED
                    canvas?.drawLine(
                        intersectionWithHorizontalScaleLineX,
                        horizontalChartAxisY,
                        currentPointX,
                        currentPointY,
                        paint
                    )

                    paint.color = Color.GRAY

                    //previousPointY below  and currentPointY above the axis x line
                } else if (currentPointY < horizontalChartAxisY && previousPointY > horizontalChartAxisY) {

                    paint.color = Color.RED
                    val a = (currentPointX - previousPointX) / (currentPointY - previousPointY)
                    val intersectionWithHorizontalScaleLineX =
                        (a * horizontalChartAxisY) + (currentPointX - a * currentPointY)
                    canvas?.drawLine(
                        previousPointX,
                        previousPointY,
                        intersectionWithHorizontalScaleLineX,
                        horizontalChartAxisY,
                        paint
                    )
                    paint.color = Color.GREEN
                    canvas?.drawLine(
                        intersectionWithHorizontalScaleLineX,
                        horizontalChartAxisY,
                        currentPointX,
                        currentPointY,
                        paint
                    )

                    paint.color = Color.GRAY
                }

                previousPointX = currentPointX
                previousPointY = currentPointY

            }
            //horizontal chart axis
            canvas?.drawLine(
                borderAxisDistance,
                horizontalChartAxisY,
                horGraphWidth,
                horizontalChartAxisY,
                paint
            )
            canvas?.drawText(
                "days",
                horGraphWidth + borderAxisDistance/3,
                horizontalChartAxisY,
                paint
            )

            //vertical chart axis
            canvas?.drawLine(
                borderAxisDistance,
                verGraphHeight,
                borderAxisDistance,
                borderAxisDistance,
                paint
            )
            canvas?.drawText(
                "amount",
                borderAxisDistance/2,
                borderAxisDistance/2,
                paint
            )

        } else {
            paint.textSize = 100f
            canvas?.drawText("No data found", 10f, 100f, paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}
