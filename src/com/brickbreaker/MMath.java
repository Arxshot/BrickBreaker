package com.brickbreaker;

// Collection of special math functions
public final class MMath {
    private MMath() {
    }

    public static float invSqrt(float x) { // error of 1%
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        //x *= (1.5f - xhalf * x * x);
        return x;
    }

    public static double invSqrt(double x) { // error of 1%
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x);
        //x *= (1.5f - xhalf * x * x);
        return x;
    }
}
