package org.byters.bcphotoanimations.view.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterFrames;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterFramesCallback;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterFrames;

import javax.inject.Inject;

public class FragmentFrames extends FragmentBase
        implements View.OnClickListener {

    private static final int SPAN_NUM = 4;

    @Inject
    IPresenterFrames presenterFrames;

    private Presentercallback presenterCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationStopMotion.getComponent().inject(this);
        presenterCallback = new Presentercallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frames, container, false);

        view.findViewById(R.id.ivPlay).setOnClickListener(this);
        view.findViewById(R.id.tvSelectCancel).setOnClickListener(this);
        view.findViewById(R.id.tvSelectRange).setOnClickListener(this);
        view.findViewById(R.id.tvRemove).setOnClickListener(this);
        view.findViewById(R.id.tvCopy).setOnClickListener(this);
        view.findViewById(R.id.tvMove).setOnClickListener(this);
        view.findViewById(R.id.tvRevert).setOnClickListener(this);

        setFrames(view);
        return view;
    }

    private void setFrames(View view) {
        RecyclerView rvItems = view.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_NUM));
        rvItems.setAdapter(new AdapterFrames());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPlay)
            presenterFrames.onClickPlay();

        if (v.getId() == R.id.tvSelectCancel)
            presenterFrames.onClickSelectCancel();

        if (v.getId() == R.id.tvSelectRange)
            presenterFrames.onClickSelectRange();

        if (v.getId() == R.id.tvRemove)
            presenterFrames.onClickRemove();

        if (v.getId() == R.id.tvCopy)
            presenterFrames.onClickCopy();

        if (v.getId() == R.id.tvMove)
            presenterFrames.onClickMove();

        if (v.getId() == R.id.tvRevert)
            presenterFrames.onClickRevert();

    }

    @Override
    public void onResume() {
        super.onResume();
        presenterFrames.setCallback(presenterCallback);
        presenterFrames.onResume();
    }

    private class Presentercallback implements IPresenterFramesCallback {

        private ListenerPositionPositive listenerPositionPositive;
        private ListenerRangePositive listenerRangePositive;
        private AlertDialog dialogRange, dialogPosition;

        Presentercallback() {
            listenerRangePositive = new ListenerRangePositive();
            listenerPositionPositive = new ListenerPositionPositive();
        }

        @Override
        public void setSelectedMode(boolean isModeSelected) {
            if (getView() == null) return;
            getView().findViewById(R.id.tvSelectCancel).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvRemove).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvCopy).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvMove).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvRevert).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
        }

        @Override
        public void showAlertSelectRange(int framesNum) {
            if (dialogRange != null && dialogRange.isShowing())
                return;

            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_range, null);

            NumberPicker pickerFrom = view.findViewById(R.id.npFrom);
            pickerFrom.setMaxValue(framesNum);
            pickerFrom.setMinValue(1);

            NumberPicker pickerTo = view.findViewById(R.id.npTo);
            pickerTo.setMaxValue(framesNum);
            pickerTo.setMinValue(1);

            dialogRange = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle(R.string.dialog_range_title)
                    .setPositiveButton(R.string.dialog_ok, listenerRangePositive)
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .setCancelable(true)
                    .create();

            dialogRange.show();
        }

        @Override
        public void showAlertCopyToPosition(int framesNum) {
            if (dialogPosition != null && dialogPosition.isShowing())
                return;

            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_position, null);

            NumberPicker pickerTo = view.findViewById(R.id.npPosition);
            pickerTo.setMaxValue(framesNum);
            pickerTo.setMinValue(0);

            dialogPosition = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle(R.string.dialog_position_title)
                    .setPositiveButton(R.string.dialog_ok, listenerPositionPositive)
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .setCancelable(true)
                    .create();

            dialogPosition.show();
        }

        private class ListenerRangePositive implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogRange == null) return;
                NumberPicker pickerFrom = dialogRange.findViewById(R.id.npFrom);
                NumberPicker pickerTo = dialogRange.findViewById(R.id.npTo);

                presenterFrames.onSelectRange(pickerFrom.getValue(), pickerTo.getValue());

            }
        }

        private class ListenerPositionPositive implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogPosition == null) return;
                NumberPicker pickerTo = dialogPosition.findViewById(R.id.npPosition);

                presenterFrames.onSelectCopyPosition(pickerTo.getValue());

            }
        }
    }

    //todo export
}
