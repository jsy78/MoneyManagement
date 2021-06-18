package com.knu.moneymanagement;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

    private final TextView titleText;

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(R.layout.loading_dialog);
        titleText = findViewById(R.id.dialogTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        titleText.setText(title);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
