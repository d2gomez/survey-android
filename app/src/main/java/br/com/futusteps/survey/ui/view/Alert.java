package br.com.futusteps.survey.ui.view;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import br.com.futusteps.survey.util.StringUtils;


public class Alert {

    @SuppressWarnings("UnusedReturnValue")
    public static AlertDialog showMessage(Context context, String title, String message) {
        return showMessage(context, title, message,
                context.getString(android.R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }, null, null);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AlertDialog showMessage(Context context, String title, String message,
                                          String positiveButton, DialogInterface.OnClickListener positiveClick) {
        return showMessage(context, title, message, positiveButton, positiveClick, null, null);
    }

    public static AlertDialog showMessage(Context context, String title, String message,
                                          String positiveButton, final DialogInterface.OnClickListener positiveClick,
                                          String negativeButton, DialogInterface.OnClickListener negativeClick) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);

        if (!StringUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!StringUtils.isEmpty(positiveButton) && positiveClick != null) {
            builder.setPositiveButton(positiveButton, positiveClick);
        }

        if (!StringUtils.isEmpty(negativeButton) && negativeClick != null) {
            builder.setNegativeButton(negativeButton, negativeClick);
        }

        try {
            return builder.show();
        } catch (WindowManager.BadTokenException ex) {
            //activity is not visible
            return builder.create();
        }
    }

    public static void showMessage(Context context, String message) {
        showMessage(context, null, message);
    }

    public static void showConfirm(Context context, int resIdmessage, int resIdpositive,
                                   int resIdnegative, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(resIdmessage));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(resIdpositive), positiveListener);
        builder.setNegativeButton(context.getString(resIdnegative), null);
        try {
            builder.show();
        } catch (WindowManager.BadTokenException ex) {
            //activity is not visible
        }
    }

    public static void dismiss(AlertDialog dialog, Activity activity) {
        try {
            if (dialog != null && dialog.isShowing() && !activity.isFinishing()) {
                dialog.dismiss();
            }
        } catch (Exception ex) {
            //activity is not visible
        }
    }

}
