package com.chipik.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Rex St. John (on behalf of AirPair.com) on 3/6/14.
 */
public class DialogHelper {

    /**
     * Generic error dialog with a close button.
     * @param title
     * @param message
     * @param context
     */
    public static void showDialog(String title, String message, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
