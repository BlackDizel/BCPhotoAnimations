package org.byters.bcphotoanimations.view.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectExportJCodec;

import javax.inject.Inject;

public class DialogExportJCodec implements IDialog, View.OnClickListener {
    private final AlertDialog dialog;

    @Inject
    IPresenterProjectExportJCodec presenter;
    private TextView tvFps, tvWidth, tvHeight;

    public DialogExportJCodec(Context context) {
        ApplicationStopMotion.getComponent().inject(this);
        dialog = new AlertDialog.Builder(context)
                .create();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_export_jcodec, null);

        initViews(view);
        dialog.setView(view);
    }

    private void initViews(View view) {
        tvFps = view.findViewById(R.id.etFPS);
        tvWidth = view.findViewById(R.id.etWidth);
        tvHeight = view.findViewById(R.id.etHeight);

        view.findViewById(R.id.tvExport).setOnClickListener(this);
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
        if (v.getId() == R.id.tvExport) {
            cancel();
            presenter.onClickExport(tvFps.getText().toString(), tvWidth.getText().toString(), tvHeight.getText().toString());
        }
    }
}
