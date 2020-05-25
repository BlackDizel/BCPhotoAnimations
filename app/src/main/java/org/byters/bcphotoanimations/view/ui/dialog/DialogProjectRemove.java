package org.byters.bcphotoanimations.view.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.ui.dialog.listener.IDialogProjectRemoveListener;

public class DialogProjectRemove implements IDialog {
    private final AlertDialog dialog;
    private final IDialogProjectRemoveListener listener;
    private final String title;

    public DialogProjectRemove(Context context, String title, IDialogProjectRemoveListener listener) {
        this.listener = listener;
        this.title = title;

        dialog = new AlertDialog.Builder(context)
                .create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_project_remove, null);

        dialog.setView(view);
        initViews(view);
    }

    private void initViews(View view) {
        String message = String.format(view.getContext().getString(R.string.title_project_remove_format), title);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(message);
        ((EditText) view.findViewById(R.id.etTitle)).addTextChangedListener(new TextListener());
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }

    @Override
    public void show() {
        dialog.show();
    }

    private class TextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().equals(title.trim())) {
                cancel();
                listener.onRemove();
            }
        }
    }
}
