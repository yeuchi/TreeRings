package com.ctyeung.treerings

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ctyeung.treerings.data.SharedPref
import com.ctyeung.treerings.img.Line
import java.util.*

class MyPaperView :View {
    var callback:IOnNewLine?=null
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {}

    companion object {
        const val RADIUS = 40F
        const val OFFSET = 20
    }

    // defines paint and canvas
    var listPoints = ArrayList<PointF>()
    var lineIntersects:IntArray? = null

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        listPoints = ArrayList()
    }

    fun demoLine(width: Int, height: Int) {
        if(width > 0 && height > 0) {
            // draw a demo line so user has something to start with
            listPoints.clear()

            val midPoint = PointF(width.toFloat() / 2F, height.toFloat() / 2F)
            listPoints.add(midPoint)

            val endPoint = PointF(width.toFloat() / 2F, height.toFloat() * 0.2F / 2F)
            listPoints.add(endPoint)
            invalidate()
        }
    }

    fun drawIntersects(lineIntersects: IntArray?) {
        /*
         * draw highlight along line for intersections
         */
        this.lineIntersects = lineIntersects
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if(listPoints.size >= 2)
            drawLine(listPoints[0], listPoints[1], canvas)

        for(p in listPoints)
            drawPoint(p, canvas)

        if(this.lineIntersects != null &&
            this.lineIntersects?.size?:0 > 0) {
            drawIntersectLines(lineIntersects!!, canvas)
        }
    }

    fun hasLine():Boolean {
        if(listPoints.size >= 2)
            return true

        return false
    }

    fun clear() {
        listPoints.clear()
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

    private fun onActionUp(p: PointF) {

        when(listPoints.size) {
            0, 1 -> {   // draw dot or line
                if(listPoints.size>0 && listPoints[0].y < p.y){
                    listPoints.add(0, p)
                }
                listPoints?.add(p)
            }

            else -> {   // 2 is the max supported - find nearest, replace, draw line
                updateLine(p)
                if(callback!=null)
                    callback!!.onNewLine(listPoints)
            }
        }
        invalidate()
    }

    /*
     * Draw highlight marker for each ring
     */
    private fun drawIntersectLines(lineIntersects: IntArray, canvas: Canvas) {

        /*
         *                             ________
         * transition: leading edge __|
         */

       // val points = getCartesian(listPoints, lineIntersects)
        var isTransition = true
        var userLine = Line(listPoints[0], listPoints[1])
        val scaler = getRatio()
        var count = 0
        var size = SharedPref.getImageViewSize()

        for(i in 0 .. lineIntersects.size-1) {
            if(1 == lineIntersects[i]) {
                if(true == isTransition) {
                    isTransition = false

                    var y = (listPoints[0].y + (i / scaler.second)).toFloat()
                    var horizon = Line(PointF(0F, y), PointF(size.first.toFloat(), y))
                    var p = userLine.findIntersect(horizon)

                    /*
                     * TODO: replace with normal line
                     */
                    if(p != null) {
                        count ++
                        val color = if(i%10==0)Color.BLACK else Color.BLUE
                        val w = if(i%10==0)20 else 10
                        val p1: PointF = PointF(p.x - w, p.y)
                        val p2: PointF = PointF(p.x + w, p.y)
                        drawLine(p1, p2, canvas, color)
                    }
                }
            }
            else {
                isTransition = true
            }
        }
        val pp = if(listPoints[0].y < (size.second - listPoints[1].y)) {
            listPoints[0]
        }
        else {
            listPoints[1]
        }

        drawTextBackground(pp, canvas)
        drawText(count.toString(), pp, canvas)
    }

    private fun getRatio():Pair<Double, Double> {
        val imageSize = SharedPref.getImageViewSize()
        val bmpSize = SharedPref.getBitmapSize()

        return Pair<Double, Double>(bmpSize.first.toDouble()/imageSize.first.toDouble(),
                                    bmpSize.second.toDouble()/imageSize.second.toDouble())
    }

    /*
     * Draw End points of the line
     */
    private fun drawPoint(p: PointF, canvas: Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        canvas?.drawCircle(p.x, p.y, 5F, paint);
    }

    private fun drawTextBackground(p:PointF, canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        canvas.drawCircle(p.x, p.y, RADIUS, paint)
    }

    private fun drawText(text:String, p:PointF, canvas: Canvas) {
        val paint = Paint()
        paint.textSize = RADIUS
        paint.isAntiAlias = true
        paint.color = Color.BLUE
        canvas.drawText(text, p.x-OFFSET, p.y+OFFSET, paint)
    }

    private fun drawLine(p1: PointF,
                         p2: PointF,
                         canvas: Canvas,
                         color:Int? = Color.BLUE) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = color!!
        canvas?.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
    }

    private fun updateLine(p: PointF) {
        val dis1 = distance(listPoints[0], p)
        val dis2 = distance(listPoints[1], p)

        val i = if(dis1 < dis2) 0 else 1
        listPoints[i] = p
        listPoints.sortBy { it.y }
    }

    private fun distance(p1: PointF, p2: PointF):Double {
        val dx:Double = p2.x.toDouble()-p1.x.toDouble()
        val dy:Double = p2.y.toDouble()-p1.y.toDouble()
        val diff = dx*dx + dy*dy
        return Math.sqrt(diff)
    }
}