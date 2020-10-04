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
              int threshold);

protected:


private:

};

#endif //TREERINGS_RINGDETECTOR_H
