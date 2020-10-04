package com.ctyeung.treerings.img

object KernelFactory {

    fun create(type:KernelType):Kernel {
        return when(type) {
            KernelType.TYPE_BLUR -> blur()
            KernelType.TYPE_HOR_DERIVATIVE -> horizontalDerivative()
            KernelType.TYPE_VERT_DERIVATIVE -> verticalDerivative()
            KernelType.TYPE_ISO_DERIVATIVE -> isotropicDerivative()
            KernelType.TYPE_SHARPEN -> sharpen()
            KernelType.TYPE_CUSTOM -> custom()
            KernelType.TYPE_XY45_DERIVATIVE -> XYDerivative()

            KernelType.TYPE_IDENTITY -> identity()
            else -> identity()
        }
    }

    // TODO - give user ability to enter custom values
    fun custom():Kernel {
        return identity()
    }

    /*
     * 0 | 0 | 0
     * - - - - -
     * 0 | 3 | -1
     * - - - - -
     * -1|-1 | 0
     */
    fun XYDerivative(): Kernel {
        val kernelWidth = 3
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = 0
        kernel[1] = 0
        kernel[2] = 0
        kernel[3] = 0
        kernel[4] = 3
        kernel[5] = -1
        kernel[6] = -1
        kernel[7] = -1
        kernel[8] = 0
        return Kernel(kernelWidth, kernel, KernelType.TYPE_HOR_DERIVATIVE)
    }

    /*
     * -1| 1 | 0
     * - - - - -
     * -1| 1 | 0
     * - - - - -
     * -1| 1 | 0
     */
    fun horizontalDerivative(): Kernel {
        val kernelWidth = 3
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = -1
        kernel[1] = 1
        kernel[2] = 0
        kernel[3] = -1
        kernel[4] = 1
        kernel[5] = 0
        kernel[6] = -1
        kernel[7] = 1
        kernel[8] = 0
        return Kernel(kernelWidth, kernel, KernelType.TYPE_HOR_DERIVATIVE)
    }

    /*
     * -1|-1 |-1
     * - - - - -
     *  1| 1 | 1
     * - - - - -
     *  0| 0 | 0
     */
    fun verticalDerivative(): Kernel {
        val kernelWidth = 3
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = -1
        kernel[1] = -1
        kernel[2] = -1
        kernel[3] = 1
        kernel[4] = 1
        kernel[5] = 1
        kernel[6] = 0
        kernel[7] = 0
        kernel[8] = 0
        return Kernel(kernelWidth, kernel, KernelType.TYPE_VERT_DERIVATIVE)
    }

    fun isotropicDerivative(): Kernel {
        val kernelWidth = 3
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = -1
        kernel[1] = -1
        kernel[2] = -1
        kernel[3] = -1
        kernel[4] = 8
        kernel[5] = -1
        kernel[6] = -1
        kernel[7] = -1
        kernel[8] = -1
        return Kernel(kernelWidth, kernel, KernelType.TYPE_ISO_DERIVATIVE)
    }

    fun sharpen(): Kernel {
        val kernelWidth = 3
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = -1
        kernel[1] = -1
        kernel[2] = -1
        kernel[3] = -1
        kernel[4] = 10
        kernel[5] = -1
        kernel[6] = -1
        kernel[7] = -1
        kernel[8] = -1
        return Kernel(kernelWidth, kernel, KernelType.TYPE_SHARPEN)
    }

    fun blur(): Kernel {
        val kernelWidth = 7
        val kernel = IntArray(kernelWidth * kernelWidth)
        for (i in kernel.indices) kernel[i] = 1
        return Kernel(kernelWidth, kernel, KernelType.TYPE_BLUR)
    }

    fun identity(): Kernel {
        val kernelWidth = 1
        val kernel = IntArray(kernelWidth * kernelWidth)
        kernel[0] = 1
        return Kernel(kernelWidth, kernel, KernelType.TYPE_IDENTITY)
    }
}