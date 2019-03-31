package org.byters.bcphotoanimations.view.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.byters.bcphotoanimations.R;

public class DialogFramePreview implements IDialog, View.OnClickListener {
    private final Dialog dialog;
    private final String frameUrl;
    private ImageView ivFrame;

    public DialogFramePreview(Context context, String frameUrl) {

        this.frameUrl = frameUrl;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_frame_preview, null);
        initViews(view);

        dialog = new Dialog(context, R.style.dialog_fullscreen);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initViews(View view) {

        ivFrame = view.findViewById(R.id.ivFrame);

        Glide.with(ivFrame.getContext())
                .load(frameUrl)
                .into(ivFrame);

        view.setOnClickListener(this);
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        cancel();
    }

}
