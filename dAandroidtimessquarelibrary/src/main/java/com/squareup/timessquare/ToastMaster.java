package com.squareup.timessquare;

/**
 * Created by xuewe on 7/3/2016.
 */

import android.widget.Toast;

public class ToastMaster {
    private static Toast sToast = null;

    private ToastMaster() {

    }

    public static void setToast(Toast toast) {
        if (sToast != null)
            sToast.cancel();
        sToast = toast;
    }

    public static void cancelToast() {
        if (sToast != null)
            sToast.cancel();
        sToast = null;
    }

}