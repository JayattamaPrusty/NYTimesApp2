package com.promobi.nyt.nytimesapp.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by dw on 23/02/17.
 */

public class Util {
    public static void toast(Activity a, String msg) {
        Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Activity a, String msg) {
        Toast.makeText(a, msg, Toast.LENGTH_LONG).show();
    }
}
