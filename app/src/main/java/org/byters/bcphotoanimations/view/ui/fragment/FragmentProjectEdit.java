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
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectEdit;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

import javax.inject.Inject;

public class FragmentProjectEdit extends FragmentBase
        implements View.OnClickListener {

    @Inject
    IPresenterProjectEdit presenterProjectEdit;
    @Inject
    INavigator navigator;

    private PresenterCallback presenterCallback;
    private TextView etTitle;
    private TextChangeListener textChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);
        presenterCallback = new PresenterCallback();
        textChangeListener = new TextChangeListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_edit, container, false);

        etTitle = view.findViewById(R.id.etTitle);

        view.setOnClickListener(this);
        view.findViewById(R.id.ivClose).setOnClickListener(this);
        view.findViewById(R.id.tvSave).setOnClickListener(this);
        view.findViewById(R.id.tvRemove).setOnClickListener(this);
        view.findViewById(R.id.tvExport).setOnClickListener(this);
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

        if (v.getId() == R.id.tvRemove) {
            presenterProjectEdit.onClickRemove();
        }
        if (v.getId() == R.id.tvExport) {
            presenterProjectEdit.onClickExport();
        }

        if (v == getView())
            presenterProjectEdit.onClickRoot();
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
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
        public void setProjectEditVisibility(boolean isVisible) {
            getView().findViewById(R.id.tvRemove).setVisibility(isVisible ? View.VISIBLE : View.GONE);
            getView().findViewById(R.id.tvExport).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

        @Override
        public void showDialogProgressExport(String projectTitle) {
            Toast.makeText(getContext(), String.format(getString(R.string.export_project_format), projectTitle), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onExportError() {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), R.string.export_project_error, Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onExportSuccess(final String title, final String projectOutputFolder) {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), String.format(getString(R.string.export_project_success_format), title, projectOutputFolder), Toast.LENGTH_SHORT).show();

                    navigator.chooseFolder(getActivity(), projectOutputFolder);

                }
            });

        }
    }
}
