//
// Created by yeuchi on 10/3/20.
//

#ifndef TREERINGS_RINGDETECTOR_H
#define TREERINGS_RINGDETECTOR_H

class RingDetector
{
public:
    RingDetector();
    ~RingDetector();

public:
    bool Find(AndroidBitmapInfo infoSource,
              void* pixelsSource,
              jint *lineIntersects, int length, // array of interection points
              int x0, int y0, int x1, int y1);  // line endpoints 0 -> 1

protected:


private:

};

#endif //TREERINGS_RINGDETECTOR_H
