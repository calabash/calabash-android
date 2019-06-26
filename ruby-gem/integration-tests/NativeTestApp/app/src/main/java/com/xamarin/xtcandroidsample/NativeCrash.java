package com.xamarin.xtcandroidsample;

import com.sun.jna.Pointer;

public class NativeCrash {
    static {
        System.loadLibrary("jnidispatch");
    }

    public static void crash() {
        Pointer pointer = new Pointer(1);
        pointer.setMemory(0, 1, (byte) 1);
    }
}
