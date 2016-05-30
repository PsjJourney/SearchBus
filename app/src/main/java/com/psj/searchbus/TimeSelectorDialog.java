package com.psj.searchbus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by psj on 2016/5/27.
 */
public class TimeSelectorDialog extends Dialog {
    public TimeSelectorDialog(Context context) {
        super(context);
    }

    public TimeSelectorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TimeSelectorDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
