package com.ctyeung.treerings;

import com.ctyeung.treerings.Kernel;

public class KernelFactory {
    public static Kernel XYDerivative()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = 0;
        kernel[1] = 0;
        kernel[2] = 0;

        kernel[3] = 0;
        kernel[4] = 3;
        kernel[5] = -1;

        kernel[6] = -1;
        kernel[7] = -1;
        kernel[8] = 0;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_HOR_DERIVATIVE);
    }

    public static Kernel horizontalDerivative()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = 1;
        kernel[2] = 0;

        kernel[3] = -1;
        kernel[4] = 1;
        kernel[5] = 0;

        kernel[6] = -1;
        kernel[7] = 1;
        kernel[8] = 0;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_HOR_DERIVATIVE);
    }

    public static Kernel verticalDerivative()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = 1;
        kernel[4] = 1;
        kernel[5] = 1;

        kernel[6] = 0;
        kernel[7] = 0;
        kernel[8] = 0;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_VERT_DERIVATIVE);
    }

    public static Kernel isotropicDerivative()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = -1;
        kernel[4] = 8;
        kernel[5] = -1;

        kernel[6] = -1;
        kernel[7] = -1;
        kernel[8] = -1;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_ISO_DERIVATIVE);
    }

    public static Kernel sharpen()
    {
        int kernelWidth = 3;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = -1;
        kernel[1] = -1;
        kernel[2] = -1;

        kernel[3] = -1;
        kernel[4] = 10;
        kernel[5] = -1;

        kernel[6] = -1;
        kernel[7] = -1;
        kernel[8] = -1;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_SHARPEN);
    }

    public static Kernel blur()
    {
        int kernelWidth = 7;
        int[] kernel = new int[kernelWidth*kernelWidth];
        for(int i=0; i<kernel.length; i++)
            kernel[i] = 1;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_BLUR);
    }

    public static Kernel identity()
    {
        int kernelWidth = 1;
        int[] kernel = new int[kernelWidth*kernelWidth];
        kernel[0] = 1;

        return new Kernel(kernelWidth, kernel, Kernel.TYPE_IDENTITY);
    }
}
