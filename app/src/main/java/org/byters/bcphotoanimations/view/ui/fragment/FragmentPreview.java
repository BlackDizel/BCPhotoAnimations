package org.byters.bcphotoanimations.view.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterPreview;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterPreviewCallback;
import org.byters.bcphotoanimations.view.ui.utils.DialogFramesRange;

import javax.inject.Inject;

public class FragmentPreview extends FragmentBase
        implements View.OnClickListener {

    @Inject
    IPresenterPreview presenterPreview;

    private PresenterPreviewCallback presenterPreviewCallback;

    private ImageView ivFrame;
    private TextView tvFrameFrom, tvFrameCurrent, tvFrameTo;
    private SeekBar seekBar;
    private ListenerSeekbar listenerSeekbar;
    private DialogInterface.OnClickListener listenerDialogFramesRange;
    private AlertDialog dialogFramesRange;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);

        presenterPreviewCallback = new PresenterPreviewCallback();
        presenterPreview.selCallback(presenterPreviewCallback);
        listenerDialogFramesRange = new ListenerDialogFramesRange();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        ivFrame = view.findViewById(R.id.ivFrame);
        seekBar = view.findViewById(R.id.sbPreview);
        tvFrameFrom = view.findViewById(R.id.tvFrameFrom);
        tvFrameCurrent = view.findViewById(R.id.tvFrameCurrent);
        tvFrameTo = view.findViewById(R.id.tvFrameTo);

        view.findViewById(R.id.ivFrame).setOnClickListener(this);
        view.findViewById(R.id.ivPlay).setOnClickListener(this);
        view.findViewById(R.id.tvFPS).setOnClickListener(this);
        tvFrameFrom.setOnClickListener(this);
        tvFrameTo.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(listenerSeekbar = new ListenerSeekbar());

        presenterPreview.onCreateView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterPreview.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenterPreview.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivFrame) {
            presenterPreview.onClickFrame();
        }
        if (v.getId() == R.id.ivPlay) {
            presenterPreview.onClickPlay();
        }
        if (v.getId() == R.id.tvFPS) {
            presenterPreview.onClickFPS();
        }
        if (v.getId() == R.id.tvFrameFrom) {
            presenterPreview.onClickFrameRange();
        }
        if (v.getId() == R.id.tvFrameTo) {
            presenterPreview.onClickFrameRange();
        }
    }

    private class PresenterPreviewCallback implements IPresenterPreviewCallback {

        @Override
        public void setButtonPlayVisibility(boolean isVisible) {
            if (getView() == null) return;
            getView().findViewById(R.id.ivPlay).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setImage(String path) {
            if (getContext() == null) return;

            if (TextUtils.isEmpty(path))
                ivFrame.setImageDrawable(null);
            else
                Glide.with(getContext())
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(ivFrame.getDrawable()))
                        .into(ivFrame);
        }

        @Override
        public void setFPS(int fps) {
            if (getView() == null) return;
            TextView tvFPS = getView().findViewById(R.id.tvFPS);
            tvFPS.setText(String.format(getString(R.string.label_fps), String.valueOf(fps)));
        }

        @Override
        public void setFrameCurrent(int frameRangeIndexFrom, int to, int current, boolean isUpdateSeekbar) {
            if (isUpdateSeekbar) {
                seekBar.setMax(to - frameRangeIndexFrom);
                seekBar.setProgress(current - frameRangeIndexFrom);
            }
            tvFrameCurrent.setText(String.valueOf(current + 1));
        }

        @Override
        public void setFramesRange(int from, int to) {
            tvFrameFrom.setText(String.valueOf(from + 1));
            tvFrameTo.setText(String.valueOf(to + 1));
        }

        @Override
        public void showAlertFrameRange(int frameRangeFrom, int frameRangeTo, int projectSelectedFramesNum) {
            dialogFramesRange = new DialogFramesRange(getContext(), projectSelectedFramesNum, 1, frameRangeFrom + 1, 1, frameRangeTo + 1, listenerDialogFramesRange).getDialog();
            dialogFramesRange.show();
        }
    }

    private class ListenerSeekbar implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) return;
            presenterPreview.onFrameSelected(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            presenterPreview.onTrackingStart();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            presenterPreview.onTrackingStop();
        }
    }

    private class ListenerDialogFramesRange implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialogFramesRange == null) return;
            NumberPicker pickerFrom = dialogFramesRange.findViewById(R.id.npFrom);
            NumberPicker pickerTo = dialogFramesRange.findViewById(R.id.npTo);

            presenterPreview.onSelectRange(pickerFrom.getValue(), pickerTo.getValue());
        }
    }

    /*private class PresenterExportCallback implements IPresenterExportCallback {
        @Override
        public void showCounter(final int counter, final int allFrames) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() == null) return;
                    showCounterUI(counter, allFrames);
                }
            });
        }

        @Override
        public void showProcessingComplete(final long millis) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() == null) return;
                    showProcessingCompleteUI(millis);
                }

            });
        }


        private void showProcessingCompleteUI(long millis) {
            if (getView() == null) return;
            TextView tvCounter = getView().findViewById(R.id.tvCounter);
            tvCounter.setText(String.format("complete in %s", millis / 1000f));
        }

        private void showCounterUI(int counter, int allFrames) {
            if (getView() == null) return;
            TextView tvCounter = getView().findViewById(R.id.tvCounter);
            tvCounter.setText(String.format("processing %d/%d", counter + 1, allFrames));
        }
    }*/
}
