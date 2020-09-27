//
// Created by yeuchi on 12/10/18.
//
// Module:      Convolution.cpp
//
// Description: Demonstrate integration of a C++ class in Android NDK
//

#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include "Common.h"
#include "Convolution.h"

//////////////////////////////////////////////////////////////////
// Construct / Destruct

Convolution::Convolution()
{
    mKernelWidth = 0;
}

Convolution::~Convolution()
{

}

//////////////////////////////////////////////////////////////////
// Public methods

bool Convolution::LoadKernel(jint *kernel,
                             int kernelWidth)
{
    mKernelWidth = kernelWidth;

    if(0>=mKernelWidth ||           // must be larger than 0
            0==(mKernelWidth % 2))  // must be odd number
        return false;

    // get a pointer to the array
    mKernel = kernel;
    if(NULL==mKernel)
            return false;

    findKernelDenominator();
    return true;
}

/*
 * Perform spatial convolution with raster image
 */
bool Convolution::Convolve(AndroidBitmapInfo infoSource,
                           void* pixelsSource,
                           AndroidBitmapInfo infoConvolved,
                           void* pixelsConvolved,
                           int threshold)
{
    int y, cy;
    int x, cx;
    void* currentPixelsSource;

    int pad = (mKernelWidth-1)/2;

    if(0>=mKernelWidth)
        return false;

    try {
        int maxYIndex = infoSource.height-pad;
        int maxXIndex = infoSource.width-pad;

        // modify pixels with image processing algorithm
        for (y=pad;y<maxYIndex;y++)
        {
            rgba * destline = (rgba *) pixelsConvolved;
            for (x=pad;x<maxXIndex;x++)
            {
                double integralR = 0;
                double integralG = 0;
                double integralB = 0;

                int srcR, srcG, srcB;

                // perform convolution with kernel
                int ki = 0;
                for(cy=0-pad; cy<=pad; cy++)
                {
                    currentPixelsSource = (char *)pixelsSource + (infoSource.stride * (y+cy));
                    rgba * srcline = (rgba *) currentPixelsSource;

                    for(cx=0-pad; cx<=pad; cx++)
                    {
                        int i = x+cx;
                        double kernelValue = mKernel[ki++];
                        integralR += double(srcline[i].red) * kernelValue;
                        integralG += double(srcline[i].green) * kernelValue;
                        integralB += double(srcline[i].blue) * kernelValue;

                        if(cy==0 && cx==0){
                            srcR = srcline[i].red;
                            srcG = srcline[i].green;
                            srcB = srcline[i].blue;
                        }
                    }
                }

                // peak value regardless of color channel
                double max = findMax(integralR / mDenominator,integralG / mDenominator,integralB / mDenominator);
                int num = bound(max);

                // threshold
                if( num > threshold){
                    // highlight color
                    destline[x].alpha = 255;
                    destline[x].red = 0; // red
                    destline[x].green = 255;   // green
                    destline[x].blue = 255;  // blue
                }
                else {
                    // original pixels
                    destline[x].alpha = 255;
                    destline[x].red = srcR; // red
                    destline[x].green = srcG;   // green
                    destline[x].blue = srcB;  // blue
                }

                // standard convolution
//                destline[x].red = bound(integralR / mDenominator); // red
//                destline[x].green = bound(integralG / mDenominator);   // green
//                destline[x].blue = bound(integralB / mDenominator);  // blue
            }

            pixelsConvolved = (char *) pixelsConvolved + infoConvolved.stride;
        }

        return true;
    }
    catch (...)
    {
        return false;
    }
}

//////////////////////////////////////////////////////////////////
// Protected / Private methods

int Convolution::findMax(double r, double g, double b) {
    double max = r;
    if (g > max) {
        max = g;
    }
    if (b > max) {
        max = b;
    }
    return max;
}

int Convolution::bound(double value)
{
    if (value > 255)
        return 255;

    if (value < 0)
        return 0;

    return value;
}

void Convolution::findKernelDenominator()
{
    mDenominator = 0;
    for(int k=0; k<mKernelWidth*mKernelWidth; k++)
        mDenominator += mKernel[k];

    if(0==mDenominator)
        mDenominator = 1;

    if(0>mDenominator)
        mDenominator *= -1;
}