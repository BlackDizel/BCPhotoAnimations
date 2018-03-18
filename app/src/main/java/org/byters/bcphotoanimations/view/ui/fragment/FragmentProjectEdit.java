package org.byters.bcphotoanimations.view.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectEdit;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

import javax.inject.Inject;

public class FragmentProjectEdit extends FragmentBase
        implements View.OnClickListener {

    @Inject
    IPresenterProjectEdit presenterProjectEdit;

    private PresenterCallback presenterCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);
        presenterCallback = new PresenterCallback();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_edit, container, false);

        TextView etTitle = view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(new TextChangeListener());


        view.setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);
        view.findViewById(R.id.tvSave).setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterProjectEdit.setCallback(presenterCallback);
        presenterProjectEdit.onStart();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivClose) {
            presenterProjectEdit.onClickClose();
        }

        if (v.getId() == R.id.tvSave) {
            presenterProjectEdit.onClickSave();
        }
        if (v == getView())
            presenterProjectEdit.onClickRoot();
    }

    private class TextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            presenterProjectEdit.onTitleEdit(s.toString());
        }
    }

    private class PresenterCallback implements IPresenterProjectEditCallback {
        @Override
        public void showErrorTitleEmpty() {
            Toast.makeText(getContext(), R.string.error_title_empty, Toast.LENGTH_SHORT).show();
        }
    }
}
