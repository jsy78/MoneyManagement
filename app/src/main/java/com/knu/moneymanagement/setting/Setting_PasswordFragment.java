package com.knu.moneymanagement.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.knu.moneymanagement.R;

import static android.content.Context.MODE_PRIVATE;

public class Setting_PasswordFragment extends Fragment {

    private Activity activity;

    private String currnetPassword;
    private RadioButton radioButtonPassword, radioButtonReset;
    private EditText currentPwInput, newPwInput, newPwConfirm, pwHint;
    private Button pwSubmit;

    public Setting_PasswordFragment() {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting_password, container, false);

        SharedPreferences sharedPref = activity.getSharedPreferences("SHARED_PREF", MODE_PRIVATE);// 핸들 객체 얻기
        currnetPassword = sharedPref.getString("password", "");//두 번째 인자는 반환 값

        RadioGroup radioGroup = root.findViewById(R.id.radioGroupPassword);
        radioButtonPassword = root.findViewById(R.id.rbtn_password_set);
        radioButtonReset = root.findViewById(R.id.rbtn_password_reset);

        currentPwInput = root.findViewById(R.id.currentPwInputEdit);
        newPwInput = root.findViewById(R.id.newPwInputEdit);
        newPwConfirm = root.findViewById(R.id.newPwConfirmEdit);
        pwHint = root.findViewById(R.id.pwHintEdit);
        pwSubmit = root.findViewById(R.id.pwSubmit);
        Button pwCancel = root.findViewById(R.id.pwCancel);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == radioButtonPassword.getId()) {
                if (currnetPassword.equals("")) {
                    currentPwInput.setClickable(false);
                    currentPwInput.setEnabled(false);
                    currentPwInput.setFocusable(false);
                    currentPwInput.setFocusableInTouchMode(false);
                } else {
                    currentPwInput.setClickable(true);
                    currentPwInput.setEnabled(true);
                    currentPwInput.setFocusable(true);
                    currentPwInput.setFocusableInTouchMode(true);
                }
                newPwInput.setClickable(true);
                newPwInput.setEnabled(true);
                newPwInput.setFocusable(true);
                newPwInput.setFocusableInTouchMode(true);
                newPwConfirm.setClickable(true);
                newPwConfirm.setEnabled(true);
                newPwConfirm.setFocusable(true);
                newPwConfirm.setFocusableInTouchMode(true);
                pwHint.setClickable(true);
                pwHint.setEnabled(true);
                pwHint.setFocusable(true);
                pwHint.setFocusableInTouchMode(true);
                pwSubmit.setText("설정");
            } else if (checkedId == radioButtonReset.getId()) {
                if (currnetPassword.equals("")) {
                    currentPwInput.setText("");
                    currentPwInput.setClickable(false);
                    currentPwInput.setEnabled(false);
                    currentPwInput.setFocusable(false);
                    currentPwInput.setFocusableInTouchMode(false);
                } else {
                    currentPwInput.setText("");
                    currentPwInput.setClickable(true);
                    currentPwInput.setEnabled(true);
                    currentPwInput.setFocusable(true);
                    currentPwInput.setFocusableInTouchMode(true);
                }
                newPwInput.setText("");
                newPwInput.setClickable(false);
                newPwInput.setEnabled(false);
                newPwInput.setFocusable(false);
                newPwInput.setFocusableInTouchMode(false);
                newPwConfirm.setText("");
                newPwConfirm.setClickable(false);
                newPwConfirm.setEnabled(false);
                newPwConfirm.setFocusable(false);
                newPwConfirm.setFocusableInTouchMode(false);
                pwHint.setText("");
                pwHint.setClickable(false);
                pwHint.setEnabled(false);
                pwHint.setFocusable(false);
                pwHint.setFocusableInTouchMode(false);
                pwSubmit.setText("해제");
            } else {
                Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        radioButtonPassword.setChecked(false);
        radioButtonReset.setChecked(true);

        pwSubmit.setOnClickListener(view -> {
            if (pwSubmit.getText().toString().equals("설정")) {
                if (currentPwInput.getText().toString().equals(currnetPassword)) { // 현재 비밀번호를 알맞게 입력함
                    if (!newPwInput.getText().toString().equals("")) { // 새 비밀번호를 입력함
                        if (!newPwInput.getText().toString().equals(currnetPassword)) { // 현재 비밀번호와 다른 비밀번호를 입력함
                            if (newPwInput.getText().toString().equals(newPwConfirm.getText().toString())) { // 입력한 새 비밀번호와 확인용 비밀번호가 일치
                                if (!pwHint.getText().toString().equals("")) { // 힌트를 입력함
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Loading_Dialog_Theme);

                                    builder.setMessage("비밀번호를 설정하시겠습니까?");
                                    builder.setPositiveButton("OK", (dialog, id) -> {
                                        String password = newPwInput.getText().toString();
                                        String passwordHint = pwHint.getText().toString();

                                        setPasswordInfomation(password, passwordHint);
                                        Toast.makeText(activity, "비밀번호가 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    });

                                    builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show());

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                                else { // 힌트를 입력하지 않았음
                                    Toast.makeText(activity, "힌트를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else { // 입력한 새 비밀번호와 확인용 비밀번호가 불일치
                                newPwConfirm.setText("");
                                Toast.makeText(activity, "비밀번호 확인이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else { // 현재 비밀번호와 같은 비밀번호를 입력함
                            newPwInput.setText("");
                            newPwConfirm.setText("");
                            pwHint.setText("");
                            Toast.makeText(activity, "다른 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else { // 새 비밀번호를 입력하지 않았음
                        newPwConfirm.setText("");
                        pwHint.setText("");
                        Toast.makeText(activity, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else { //현재 비밀번호를 알맞게 입력하지 않음
                    currentPwInput.setText("");
                    Toast.makeText(activity, "현재 비밀번호를 잘못 입력하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else if (pwSubmit.getText().toString().equals("해제")) {
                if (currentPwInput.getText().toString().equals(currnetPassword)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Loading_Dialog_Theme);

                    builder.setMessage("비밀번호를 초기화하시겠습니까?");
                    builder.setPositiveButton("OK", (dialog, id) -> {
                        setPasswordInfomation("", "");
                        Toast.makeText(activity, "비밀번호가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    });

                    builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    currentPwInput.setText("");
                    Toast.makeText(activity, "현재 비밀번호를 잘못 입력하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        });

        pwCancel.setOnClickListener(view -> {
            Toast.makeText(activity, "비밀번호 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            activity.finish();
        });

        return root;
    }

    private void setPasswordInfomation(String pw, String pwHint) {
        SharedPreferences sharedPref = activity.getSharedPreferences("SHARED_PREF", MODE_PRIVATE); // 핸들 객체 얻기
        SharedPreferences.Editor editor = sharedPref.edit();//edit 객체 생성
        editor.putString("password", pw);// password라는 key 값으로 pw 문자를 저장
        editor.putString("passwordHint", pwHint);// passwordHint라는 key 값으로 pwHint 문자를 저장
        editor.apply();// 데이터 저장을 완료
    }

}
