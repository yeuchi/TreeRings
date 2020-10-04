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
    int y, cy;
    int x, cx;
    void* currentPixelsSource;

    try {
        /*
         * 1. step through all calculated intersection points
         * 2. evaluate point on line as ring intersection
         */

        // test to write data back
        for (int i=0; i<length; i++) {
            if(i % 2 == 0){
                lineIntersects[i] = 1;
            }
        }
        return true;
    }
    catch (...)
    {
        return false;
    }
}