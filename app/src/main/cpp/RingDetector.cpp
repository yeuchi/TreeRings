//
// Created by yeuchi on 10/3/20.
//

#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include "Common.h"
#include "RingDetector.h"

RingDetector::RingDetector() {

}

RingDetector::~RingDetector() {

}

//////////////////////////////////////////////////////////////////
// Public methods

bool RingDetector::Find(AndroidBitmapInfo infoSource, void *pixelsSource,
                        jint *lineIntersects, int length, int x0, int y0, int x1, int y1) {

    void* currentPixelsSource;

    try {
        /*
         * ASSUME y0 < y1
         * ASSUME moving through y not x
         *
         * 1. step through all calculated intersection points
         * 2. evaluate point on line as ring intersection
         */
        double m = (x0==x1)?
                0:                  // don't have imaginary number
                ((double)y1-(double)y0) / ((double)x1 - (double)x0);    // legal y-intercept
        double b = y0 - m * x0;

        int i = 0;
        for (int y = y0; y < y1; y++) {
            currentPixelsSource = (char *)pixelsSource + (infoSource.stride * (y));
            rgba * srcline = (rgba *) currentPixelsSource;
            int x = (x0 == x1)?
                    x0 :      // vertical line
                    (y-b)/m;  // legal y-intercept

            if(srcline[x].red == 0 && srcline[x].green == 255 && srcline[x].blue == 255) {
                lineIntersects[i] = 1;

                // highlight pixel for testing
//                srcline[x].red = 0;
//                srcline[x].green = 0;
//                srcline[x].blue = 0;
            }
            i++;
        }

        // test to write data back
 /*       for (int i=0; i<length; i++) {
            if(i % 2 == 0){
                lineIntersects[i] = 1;
            }
        }*/
        return true;
    }
    catch (...)
    {
        return false;
    }
}