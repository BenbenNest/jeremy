package com.jeremy.lychee.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;

public class DialogUtil {

    public static void showConfirmDialog(
            Context context, String content,
            String positiveText, Action1<DialogInterface> positiveAction,
            String negativeText, Action1<DialogInterface> negativeAction) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setCustomContent(content.trim())
                .setPositiveButton(positiveText, v -> positiveAction.call(dialog))
                .setNegativeButton(negativeText, v -> negativeAction.call(dialog))
                .show();
    }

    public static void showAlertDialog(Context context, String content, Action0 action) {
        AlertDialog dialog = new AlertDialog(context);
        dialog.setCustomContent(content.trim(), action).show();
    }

    public static void showEditTextDialog(
            Context context, String title, String content,
            Action2<DialogInterface, CharSequence> positiveAction,
            Action2<DialogInterface, CharSequence> negativeAction) {
        EditTextDialog dialog = new EditTextDialog(context);
        dialog.setCustomTitle(title.trim());
        dialog.setInput(content.trim());
        dialog.setOnConfirmListener(v -> positiveAction.call(dialog, dialog.getInput()))
                .setOnCancleListener(v -> negativeAction.call(dialog, dialog.getInput()))
                .show();
    }
}
