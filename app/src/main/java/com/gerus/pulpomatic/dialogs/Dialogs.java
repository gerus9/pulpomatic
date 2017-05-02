package com.gerus.pulpomatic.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerus.pulpomatic.R;

/**
 * Created by Gerardo.
 */
public class Dialogs {

    private AlertDialog mProgressDialog;
    private Activity mActivity;
    private DialogInterface.OnDismissListener onDismissListener;

    public Dialogs(Activity poContext) {
        this.onDismissListener = null;
        mActivity = poContext;
    }

    public Dialogs(Activity poContext, DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        mActivity = poContext;
    }

    public void showProgressBar(String psMessage) {
        showProgressBar(psMessage, false);
    }

    public void showProgressBar(String psMessage, boolean isCancelable) {

        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mActivity);
        View mView = mActivity.getLayoutInflater().inflate(R.layout.dialog_progress, null);
        mAlertDialog.setView(mView);
        ((TextView) mView.findViewById(android.R.id.text1)).setText(psMessage);
        mAlertDialog.setCancelable((isCancelable));
        mProgressDialog = mAlertDialog.show();
        mProgressDialog.setOnDismissListener(onDismissListener);
    }

    public void dismissProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void showNegativeMessage(String psMsg, DialogInterface.OnDismissListener poDismissListener) {
        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mActivity);
        View mView = mActivity.getLayoutInflater().inflate(R.layout.dialog_generic, null);
        mAlertDialog.setView(mView);
        ((TextView) mView.findViewById(android.R.id.text1)).setText(psMsg);
        ((ImageView) mView.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_vc_error);
        mAlertDialog.setCancelable((poDismissListener == null));
        mAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog.setOnDismissListener(poDismissListener);
        mAlertDialog.show();
    }

    public void showInfoMessage(String psMsg, DialogInterface.OnDismissListener poDismissListener) {
        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mActivity);
        View mView = mActivity.getLayoutInflater().inflate(R.layout.dialog_generic, null);
        mAlertDialog.setView(mView);
        ((TextView) mView.findViewById(android.R.id.text1)).setText(psMsg);
        ((ImageView) mView.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_vc_info);
        mAlertDialog.setCancelable((poDismissListener == null));
        mAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog.setOnDismissListener(poDismissListener);
        mAlertDialog.show();
    }

    public void showQuestionMessage(String psMsg,@NonNull final onAnswer poDismissListener) {
        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mActivity);
        View mView = mActivity.getLayoutInflater().inflate(R.layout.dialog_generic, null);
        mAlertDialog.setView(mView);
        ((TextView) mView.findViewById(android.R.id.text1)).setText(psMsg);
        ((ImageView) mView.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_vc_interrogation);
        mAlertDialog.setCancelable((poDismissListener == null));
        mAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                poDismissListener.isAccept(true);
                dialog.dismiss();
            }
        });
        mAlertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                poDismissListener.isAccept(false);
                dialog.dismiss();
            }
        });
        mAlertDialog.show();
    }

    public interface onAnswer{
        void isAccept(boolean pbDecision);
    }

}
