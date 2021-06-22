package com.knu.moneymanagement.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.R;
import com.knu.moneymanagement.database.StaticVariable;

import java.util.Calendar;

public class Setting_ResetFragment extends Fragment {

    private Activity activity;

    public Setting_ResetFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        else
            activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting_reset, container, false);

        Button reset = root.findViewById(R.id.btn_reset);
        Button cancel = root.findViewById(R.id.btn_resetCancel);

        reset.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Loading_Dialog_Theme);

            builder.setMessage("초기화를 진행하시겠습니까?");

            builder.setPositiveButton("OK", (dialog, id) -> {
                MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Calendar cal = Calendar.getInstance();

                String sqlDrop = MoneyDBCtrct.SQL_DROP_TBL;
                db.execSQL(sqlDrop);
                String sqlCreate = MoneyDBCtrct.SQL_CREATE_TBL;
                db.execSQL(sqlCreate);

                db.close();
                dbHelper.close();

                StaticVariable.year = cal.get(Calendar.YEAR);
                StaticVariable.month = cal.get(Calendar.MONTH) + 1;
                StaticVariable.day = cal.get(Calendar.DATE);

                Toast.makeText(activity, "초기화되었습니다.", Toast.LENGTH_SHORT).show();
                activity.finish();
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        cancel.setOnClickListener(view -> {
            Toast.makeText(activity, "초기화가 취소되었습니다.", Toast.LENGTH_SHORT).show();
            activity.finish();
        });
        return root;
    }

}
