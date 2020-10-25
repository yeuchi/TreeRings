package com.ctyeung.treerings.img

import android.graphics.PointF

class Line (val p0:PointF, val p1:PointF) {

    fun findIntersect(line:Line):PointF? {
        var p = PointF()

        if(isHorizontal()){
            // overlap or parallel
            if( line.isHorizontal()) {
                return null
            }
            else {
                val m = line.getSlope()
                val b = line.getIntercept()
                val x = (p0.y - b)/m
                return PointF(x, p0.y)
            }
        }
        else if (isVertical()) {
            // overlap or parallel
            if ( line.isVertical()) {
                return null
            }
            else {
                val m = line.getSlope()
                val b = line.getIntercept()
                val y = m * p0.x + b
                return PointF(p0.x, y)
            }
        }
        else {
            val m = getSlope()
            val b = getIntercept()

            // overlap or parallel
            if(getSlope() == line.getSlope()) {
                return null
            }
            else if(line.isHorizontal()){
                // (y - b)m = x
                val x = (line.p0.y - b)/m
                return PointF(x, line.p0.y)
            }
            else if (line.isVertical()) {
                // y = mx + b
                val y = m * line.p0.x + b
                return PointF(line.p0.x, y)
            }
            else {
                val x = (line.getIntercept() - b) / (m - line.getSlope())
                // y = mx + b
                val y = m * x + b
                return PointF(x, y)
            }
        }
        return p
    }

    /*
     * slope - m
     */
    fun getSlope():Float {
        return (p1.y - p0.y) / (p1.x - p0.x)
    }

    /*
     * y-intercept - b
     */
    fun getIntercept():Float {
       return p0.y - getSlope() * p0.x
    }

    fun isVertical():Boolean {
        return if (p0.x == p1.x) true else false
    }

    fun isHorizontal():Boolean {
        return if (p0.y == p1.y) true else false
    }
}