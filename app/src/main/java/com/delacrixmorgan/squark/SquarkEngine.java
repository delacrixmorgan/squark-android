package com.delacrixmorgan.squark;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class SquarkEngine {
    private static String TAG = "SquarkEngine";
    private static SquarkEngine sSquarkEngine;

    public static synchronized SquarkEngine getInstance(@NonNull Context context) {
        if (sSquarkEngine == null){
            sSquarkEngine = new SquarkEngine(context);
        }
        return sSquarkEngine;
    }

    private SquarkEngine(@NonNull Context context){

    }
}
