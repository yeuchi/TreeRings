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

bool RingDetector::Find(AndroidBitmapInfo infoSource, void *pixelsSource, int threshold) {
    int y, cy;
    int x, cx;
    void* currentPixelsSource;


    try {

        return true;
    }
    catch (...)
    {
        return false;
    }
}