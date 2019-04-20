package org.byters.bcphotoanimations.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    private TextView etTitle;
    private TextChangeListener textChangeListener;
    private View vExportMediaCodec;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);

        presenterProjectEdit.setCallback(presenterCallback = new PresenterCallback());

        textChangeListener = new TextChangeListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_edit, container, false);

        initViews(view);
        presenterProjectEdit.onCreateView();
        return view;
    }

    private void initViews(View view) {
        etTitle = view.findViewById(R.id.etTitle);
        vExportMediaCodec = view.findViewById(R.id.tvExportVideoMediaCodec);

        view.setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);
        view.findViewById(R.id.tvSave).setOnClickListener(this);
        view.findViewById(R.id.tvRemove).setOnClickListener(this);
        view.findViewById(R.id.llExport).setOnClickListener(this);
        view.findViewById(R.id.tvExportVideoJCodec).setOnClickListener(this);
        view.findViewById(R.id.tvExportVideoMediaCodec).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
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

        if (v.getId() == R.id.tvRemove) {
            presenterProjectEdit.onClickRemove();
        }
        if (v.getId() == R.id.llExport) {
            presenterProjectEdit.onClickExport();
        }

        if (v.getId() == R.id.tvExportVideoJCodec)
            presenterProjectEdit.onClickExportJCodec();

        if (v.getId() == R.id.tvExportVideoMediaCodec)
            presenterProjectEdit.onClickExportMediaCodec();

        if (v == getView())
            presenterProjectEdit.onClickRoot();
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        if (getActivity() == null) return;
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

        @Override
        public void setTitle(String title) {
            etTitle.removeTextChangedListener(textChangeListener);
            etTitle.setText(TextUtils.isEmpty(title) ? "" : title);
            etTitle.addTextChangedListener(textChangeListener);

            etTitle.requestFocus();
            showKeyboard(etTitle);
        }

        @Override
        public void hideKeyboard() {
            FragmentProjectEdit.this.hideKeyboard();
        }

        @Override
        public void showDialogProgressExport(String projectTitle) {
            if (!isAdded()) return;
            Toast.makeText(getContext(), String.format(getString(R.string.export_project_format), projectTitle), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setExportAttemptsUnlimited() {
            if (!isAdded()) return;
            TextView tvAttempts = getView().findViewById(R.id.tvExportAttempts);
            tvAttempts.setText("Unlimited");
        }

        @Override
        public void setExportAttemptsNotEnough() {
            if (!isAdded()) return;
            TextView tvAttempts = getView().findViewById(R.id.tvExportAttempts);
            tvAttempts.setText(R.string.export_attempts_not_enough);
        }

        @Override
        public void setExportMediaCodecVisibility(boolean isVisible) {
            if (!isAdded()) return;
            vExportMediaCodec.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setExportAttempts(int attempts) {
            if (!isAdded()) return;
            TextView tvAttempts = getView().findViewById(R.id.tvExportAttempts);
            tvAttempts.setText(attempts > 0 ? String.format(getString(R.string.export_attempts_remaining), attempts) : getString(R.string.export_attempts_not_enough));
        }

    }
}
