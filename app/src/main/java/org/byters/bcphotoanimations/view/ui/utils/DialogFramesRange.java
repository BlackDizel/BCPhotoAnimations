package org.byters.bcphotoanimations.view.ui.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.byters.bcphotoanimations.R;

public class DialogFramesRange {

    private AlertDialog dialogRange;

    public DialogFramesRange(Context context, int framesNum, DialogInterface.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_range, null);

        NumberPicker pickerFrom = view.findViewById(R.id.npFrom);
        pickerFrom.setMaxValue(framesNum);
        pickerFrom.setMinValue(1);

        NumberPicker pickerTo = view.findViewById(R.id.npTo);
        pickerTo.setMaxValue(framesNum);
        pickerTo.setMinValue(1);

        dialogRange = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.dialog_range_title)
                .setPositiveButton(R.string.dialog_ok, listener)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setCancelable(true)
                .create();
    }

    public AlertDialog getDialog() {
        return dialogRange;
    }
}
