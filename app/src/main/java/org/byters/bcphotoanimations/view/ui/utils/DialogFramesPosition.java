package org.byters.bcphotoanimations.view.ui.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;

public class DialogFramesPosition {

    private AlertDialog dialogPosition;

    public DialogFramesPosition(Context context, int framesNum, DialogInterface.OnClickListener listener) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_position, null);

        NumberPicker pickerTo = view.findViewById(R.id.npPosition);
        pickerTo.setMaxValue(framesNum);
        pickerTo.setMinValue(0);

        dialogPosition = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.dialog_position_title)
                .setPositiveButton(R.string.dialog_ok, listener)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setCancelable(true)
                .create();
    }

    public AlertDialog getDialog() {
        return dialogPosition;
    }
}
