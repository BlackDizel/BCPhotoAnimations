package org.byters.bcphotoanimations.view.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        @Override
        public void setSelectedMode(boolean isModeSelected) {
            if (getView() == null) return;
            getView().findViewById(R.id.tvSelectCancel).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvRemove).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvCopy).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvMove).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvRevert).setVisibility(isModeSelected ? View.VISIBLE : View.GONE);

        }
    }

    //todo export
}
