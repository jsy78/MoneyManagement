package com.knu.moneymanagement.setting;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.knu.moneymanagement.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Setting_RestoreFragment extends Fragment {

    private Activity activity;
    private File sd, data;

    public Setting_RestoreFragment() {
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

        View root = inflater.inflate(R.layout.fragment_setting_restore, container, false);

        TextView fileDate = root.findViewById(R.id.backupFileInfoText);
        String filePath = Environment.getExternalStorageDirectory() + "//moneyDB//money.db";
        File file = new File(filePath);

        if(file.exists()) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            fileDate.setText(simpleDate.format(file.lastModified()));
        }
        else
            fileDate.setText("파일이 존재하지 않습니다.");


        Button restore = root.findViewById(R.id.btn_restore);
        Button cancel = root.findViewById(R.id.btn_restoreCancel);

        restore.setOnClickListener(view -> {
            sd = Environment.getExternalStorageDirectory();
            data = Environment.getDataDirectory();
            if(sd.canWrite()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Loading_Dialog_Theme);

                builder.setMessage("복원을 진행하시겠습니까?");
                builder.setPositiveButton("OK", (dialog, id) -> {
                    try {
                        String currentDBPath = "//data//" + activity.getPackageName() + "//databases//money.db";
                        String backupDBPath = "//moneyDB//money.db";

                        File currentDB = new File(sd, backupDBPath);
                        File backupDB = new File(data, currentDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(activity, "복원이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                    catch (FileNotFoundException e1) {
                        Toast.makeText(activity, "원본 폴더나 파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e2) {
                        Toast.makeText(activity, "오류가 발생하여 백업할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e3) {
                        Toast.makeText(activity, "백업할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                }
                else {
                    Toast.makeText(activity, "저장공간 접근 권한을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    showPermissionDialog();
                }
            });

        cancel.setOnClickListener(view -> {
            Toast.makeText(activity, "복원이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            activity.finish();
        });

       return root;
    }

    private void showPermissionDialog() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(activity, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(activity, "권한이 거부되었습니다. " + deniedPermissions.get(0), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("외부 저장소에 접근하기 위해 권한 설정이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

}
