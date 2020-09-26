package com.ctyeung.treerings;

import android.app.Activity;
import android.app.Application;

public class Kernel
{
    public static String TYPE_NONE="none";
    public static String TYPE_IDENTITY="identity";
    public static String TYPE_BLUR="blur";
    public static String TYPE_SHARPEN="sharpen";
    public static String TYPE_ISO_DERIVATIVE="isotropic derivative";
    public static String TYPE_VERT_DERIVATIVE="vertical derivative";
    public static String TYPE_HOR_DERIVATIVE="horizontal derivative";

    public Kernel(int width,
                  int[] values,
                  String type)
    {
        mWidth = width;
        mValues = values;
        mType = type;
    }

    public Kernel()
    {
        mWidth = 0;
        mValues = null;
        mType = TYPE_NONE;
    }

    public int mWidth;
    public int[] mValues;
    public String mType;
}
