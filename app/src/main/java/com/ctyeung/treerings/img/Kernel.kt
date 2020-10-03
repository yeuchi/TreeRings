package com.ctyeung.treerings.img

class Kernel {
    var mWidth: Int
    var mValues: IntArray?
    var mType: KernelType

    constructor(
        width: Int,
        values: IntArray?,
        type: KernelType
    ) {
        mWidth = width
        mValues = values
        mType = type
    }

    constructor() {
        mWidth = 0
        mValues = null
        mType = KernelType.TYPE_NONE
    }
}