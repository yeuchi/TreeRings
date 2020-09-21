package com.ctyeung.treerings

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.ArrayList

class MyPaperView :View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    val regressionColor = Color.BLACK
    val dotColor = Color.BLUE
    val tangentColor = Color.GREEN

    // defines paint and canvas
    var drawPaint: Paint? = null
    var path: Path? = null
    var listPoints = ArrayList<PointF>()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        path = Path()
        listPoints = ArrayList()
    }

    override fun onDraw(canvas: Canvas) {
        if(listPoints.size >= 2)
            drawLine(listPoints[0], listPoints[1], canvas)

        for(p in listPoints)
            drawPoint(p, canvas)
    }

    fun clear() {
        listPoints.clear()
        path = Path()
        invalidate() // Indicate view should be redrawn
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = PointF(event.x, event.y)

        // Checks for the event that occurs
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }

            /*case MotionEvent.ACTION_MOVE:
                path.lineTo(point.x, point.y);
                break;*/

            MotionEvent.ACTION_UP -> {
                onActionUp(point)
            }

            else -> return false
        }// Starts a new line in the path

        return true
    }

    private fun onActionUp(p:PointF) {

        when(listPoints.size) {
            0, 1 -> {   // draw dot or line
                listPoints?.add(p)
            }

            else -> {   // 2 is the max supported - find nearest, replace, draw line
                updateLine(p)
            }
        }
        invalidate()
    }

    private fun drawPoint(p:PointF, canvas:Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        canvas?.drawCircle(p.x, p.y, 5F, paint);
    }

    private fun drawLine(p1:PointF, p2:PointF, canvas:Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.GREEN
        canvas?.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
    }

    private fun updateLine(p:PointF) {
        val dis1 = distance(listPoints[0], p)
        val dis2 = distance(listPoints[1], p)

        if(dis1 < dis2) {
            listPoints[0] = p
        }
        else {
            listPoints[1] = p
        }
    }

    private fun distance(p1:PointF, p2:PointF):Double {
        val dx:Double = p2.x.toDouble()-p1.x.toDouble()
        val dy:Double = p2.y.toDouble()-p1.y.toDouble()
        val diff = dx*dx + dy*dy
        return Math.sqrt(diff)
    }
}