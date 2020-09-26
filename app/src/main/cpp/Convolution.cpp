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

    FindKernelDenominator();
    return true;
}

/*
 * Perform spatial convolution with raster image
 */
bool Convolution::Convolve(AndroidBitmapInfo infoSource,
                           void* pixelsSource,
                           AndroidBitmapInfo infoConvolved,
                           void* pixelsConvolved)
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

                // perform convolution with kernel
                int ki = 0;
                for(cy=0-pad; cy<=pad; cy++)
                {
                    currentPixelsSource = (char *)pixelsSource + (infoSource.stride * (y+cy));
                    rgba * line = (rgba *) currentPixelsSource;

                    for(cx=0-pad; cx<=pad; cx++)
                    {
                        int i = x+cx;
                        int kernelValue = mKernel[ki++];
                        integralR += line[i].red * kernelValue;
                        integralG += line[i].green * kernelValue;
                        integralB += line[i].blue * kernelValue;
                    }
                }

                destline[x].alpha = 255;   // alpha channel
                destline[x].red = bound(integralR / mDenominator); // red
                destline[x].green = bound(integralG / mDenominator);   // green
                destline[x].blue = bound(integralB / mDenominator);  // blue
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

int Convolution::bound(double value)
{
    if (value > 255)
        return 255;

    if (value < 0)
        return 255;

    return value;
}

void Convolution::FindKernelDenominator()
{
    mDenominator = 0;
    for(int k=0; k<mKernelWidth*mKernelWidth; k++)
        mDenominator += mKernel[k];

    if(0==mDenominator)
        mDenominator = 1;

    if(0>mDenominator)
        mDenominator *= -1;
}