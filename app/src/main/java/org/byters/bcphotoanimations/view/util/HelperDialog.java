package org.byters.bcphotoanimations.view.util;

import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;
import org.byters.bcphotoanimations.view.ui.dialog.DialogExportJCodec;
import org.byters.bcphotoanimations.view.ui.dialog.DialogExportMediaCodec;
import org.byters.bcphotoanimations.view.ui.dialog.DialogFramePreview;
import org.byters.bcphotoanimations.view.ui.dialog.DialogProjectRemove;
import org.byters.bcphotoanimations.view.ui.dialog.IDialog;
import org.byters.bcphotoanimations.view.ui.dialog.listener.IDialogProjectRemoveListener;

import java.lang.ref.WeakReference;

public class HelperDialog implements IHelperDialog {
    private WeakReference<ActivityBase> refContext;
    private WeakReference<IDialog> refDialog;

    @Override
    public void set(ActivityBase activityBase) {
        this.refContext = new WeakReference<>(activityBase);
    }

    @Override
    public void showDialogExportJCodec() {
        if (refContext == null || refContext.get() == null) return;
        showDialog(new DialogExportJCodec(refContext.get()));
    }

    @Override
    public void showDialogExportMediaCodec() {
        if (refContext == null || refContext.get() == null) return;
        showDialog(new DialogExportMediaCodec(refContext.get()));
    }

    @Override
    public void showDialogProjectRemove(String title, IDialogProjectRemoveListener listener) {
        if (refContext == null || refContext.get() == null) return;
        showDialog(new DialogProjectRemove(refContext.get(), title, listener));
    }

    @Override
    public void showDialogFrame(String frameUrl) {
        if (refContext == null || refContext.get() == null) return;
        showDialog(new DialogFramePreview(refContext.get(), frameUrl));
    }

    private void showDialog(IDialog dialog) {
        if (refDialog != null && refDialog.get() != null)
            refDialog.get().cancel();

        refDialog = new WeakReference<>(dialog);
        refDialog.get().show();

    }
}
