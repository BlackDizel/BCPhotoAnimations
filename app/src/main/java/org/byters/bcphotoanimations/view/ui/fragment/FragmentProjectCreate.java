package org.byters.bcphotoanimations.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectCreate;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectCreateListener;

import javax.inject.Inject;

public class FragmentProjectCreate extends FragmentBase implements View.OnClickListener {

    @Inject
    IPresenterProjectCreate presenter;

    private TextView tvTitle;
    private ListenerPresenter listenerPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationStopMotion.getComponent().inject(this);
        presenter.setListener(listenerPresenter = new ListenerPresenter());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_create, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.etTitle);
        view.findViewById(R.id.tvContinue).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvContinue)
            presenter.onClickContinue(tvTitle.getText().toString());
    }

    private class ListenerPresenter implements IPresenterProjectCreateListener {
        @Override
        public void showErrorTitleEmpty() {
            if (!isAdded()) return;
            Toast.makeText(getContext(), R.string.error_title_empty, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void hideKeyboard() {
            if (!isAdded()) return;
            if (getActivity() == null) return;
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
