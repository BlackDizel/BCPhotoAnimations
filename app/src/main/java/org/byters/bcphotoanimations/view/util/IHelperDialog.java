package org.byters.bcphotoanimations.view.util;

import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;
import org.byters.bcphotoanimations.view.ui.dialog.listener.IDialogProjectRemoveListener;

public interface IHelperDialog {
    void set(ActivityBase activityBase);

    void showDialogExportJCodec();

    void showDialogFrame(String frameUrl);

    void showDialogExportMediaCodec();

    void showDialogProjectRemove(String title, IDialogProjectRemoveListener listener);
}
