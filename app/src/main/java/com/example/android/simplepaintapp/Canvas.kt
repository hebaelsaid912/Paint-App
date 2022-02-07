package com.example.android.simplepaintapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.*
import androidx.core.content.res.ResourcesCompat

class Canvas(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var params: ViewGroup.LayoutParams? = null
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private var pathForPencil = Path()
    private var pathForArrow = Path()
    private var pathForRect = Path()
    private var pathForEllipse = Path()
    private val touchListener = ViewConfiguration.get(context).scaledEdgeSlop
    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f

    companion object {
        var paintForPencil = Paint().apply {
            color = current_brush
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND

        }
        var paintForRect = Paint().apply {
            color = current_brush
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND

        }
        var paintForArrow = Paint().apply {
            color = current_brush
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND

        }
        var paintForEllipse = Paint().apply {
            color = current_brush
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND

        }
        var current_brush = Color.BLACK
        var type = "path"

    }

    init {
        params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::bitmap.isInitialized)
            bitmap.recycle()
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, 0f, 0f, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionX = event!!.x
        motionY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startDrawing()
            MotionEvent.ACTION_MOVE -> startDrawingMOVE()
            MotionEvent.ACTION_UP -> startDrawingUP()
        }
        return true

    }

    private fun startDrawingUP() {
        when (type) {
            MainActivity.TYPE_PATH -> {
                pathForPencil.reset()
            }
            MainActivity.TYPE_ARROW -> {
                drawArrow()
            }
            MainActivity.TYPE_RECT -> {
                drawRectangle()
            }
            MainActivity.TYPE_ELLIPSE -> {
                drawEllipse()
            }
        }
    }

    private fun startDrawingMOVE() {
        val distX = Math.abs(motionX - currentX)
        val distY = Math.abs(motionY - currentY)
        if (distX >= touchListener || distY >= touchListener) {
            when (type) {
                MainActivity.TYPE_PATH -> {
                    pathForPencil.quadTo(
                        currentX,
                        currentY,
                        (motionX + currentX) / 2,
                        (motionY + currentY) / 2
                    )
                    currentX = motionX
                    currentY = motionY
                    canvas.drawPath(pathForPencil, paintForPencil)
                }
                MainActivity.TYPE_ARROW -> {
                    if (!pathForArrow.isEmpty) {
                        pathForArrow.reset()
                    }

                }
                MainActivity.TYPE_RECT -> {
                    if (!pathForRect.isEmpty) {
                        pathForRect.reset()
                    }
                }
                MainActivity.TYPE_ELLIPSE -> {
                    if (!pathForEllipse.isEmpty) {
                        pathForEllipse.reset()
                    }
                }
            }
        }
        invalidate()

    }

    private fun startDrawing() {
        when (type) {
            MainActivity.TYPE_PATH -> {
                pathForPencil.reset()
                pathForPencil.moveTo(motionX, motionY)
            }
            MainActivity.TYPE_ARROW -> {
                pathForArrow.reset()
                pathForArrow.moveTo(motionX, motionY)
            }
            MainActivity.TYPE_RECT -> {
                pathForRect.reset()
                pathForRect.moveTo(motionX, motionY)
            }
            MainActivity.TYPE_ELLIPSE -> {
                pathForEllipse.reset()
                pathForEllipse.moveTo(motionX, motionY)
            }
        }
        currentX = motionX
        currentY = motionY
    }

    private fun drawArrow(){
        // motion is start & current is end
        //set arrow head points values
        val deltaX = currentX - motionX
        val deltaY = currentY - motionY
        val frac = 0.1f
        val point_x_1 = motionX + ((1 - frac) * deltaX + frac * deltaY)
        val point_y_1 = motionY + ((1 - frac) * deltaY - frac * deltaX)
        val point_x_2 = currentX
        val point_y_2 = currentY
        val point_x_3 = motionX + ((1 - frac) * deltaX - frac * deltaY)
        val point_y_3 = motionY + ((1 - frac) * deltaY + frac * deltaX)
        //draw head
        pathForArrow.moveTo(point_x_1, point_y_1)
        pathForArrow.lineTo(point_x_2, point_y_2)
        pathForArrow.lineTo(point_x_3, point_y_3)
        //draw line
        pathForArrow.moveTo(point_x_2, point_y_2)
        pathForArrow.lineTo(motionX, motionY)
        canvas.drawPath(pathForArrow, paintForArrow)
        pathForArrow.reset()
    }
    private fun drawRectangle(){
        if (pathForRect.isEmpty) {
            pathForRect.addRect(
                currentX,
                currentY,
                motionX,
                motionY,
                Path.Direction.CW
            )
            canvas.drawPath(pathForRect, paintForRect)
        } else {
            pathForRect.reset()
        }

        pathForRect.reset()
    }
    private fun drawEllipse(){
        if (pathForEllipse.isEmpty) {
            pathForEllipse.addOval(
                currentX,
                currentY,
                motionX,
                motionY,
                Path.Direction.CW
            )
            canvas.drawPath(pathForEllipse, paintForEllipse)
        } else {
            pathForEllipse.reset()
        }
        pathForEllipse.reset()
    }
}