package com.example.user.goeat_3;

import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edi_user_id1, edi_user_mail, edi_user_password1, edi_user_password1_1;
    private TextView btn_editPassword, tvSignupback;
    private JSONArray josnarray = null;
    private JSONObject jObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edi_user_id1 = findViewById(R.id.edi_user_id1);
        edi_user_mail = findViewById(R.id.edi_user_mail);
        edi_user_password1 = findViewById(R.id.edi_user_password1);
        edi_user_password1_1 = findViewById(R.id.edi_user_password1_1);
        btn_editPassword = findViewById(R.id.btn_editPassword);
        tvSignupback = findViewById(R.id.tvSignupback);

        btn_editPassword.setOnClickListener(this);
        tvSignupback.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editPassword:
                if (edi_user_id1.getText().length() == 0 || edi_user_mail.getText().length() == 0 || edi_user_password1.getText().length() == 0 || edi_user_password1_1.getText().length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, "內容不可為空", Toast.LENGTH_SHORT).show();
                } else if (!(edi_user_password1.getText().toString()).equals(edi_user_password1_1.getText().toString())) {
                    Toast.makeText(ForgotPasswordActivity.this, "確認密碼不相符", Toast.LENGTH_SHORT).show();
                } else if (!(Linkify.addLinks(edi_user_mail.getText(), Linkify.EMAIL_ADDRESSES))) {
                    Toast.makeText(ForgotPasswordActivity.this, "Mail格式不正確", Toast.LENGTH_SHORT).show();
                } else {
                    new ccon().execute("Update_password", edi_user_id1.getText().toString(),edi_user_password1.getText().toString(),edi_user_mail.getText().toString());

                    //判斷帳號與信箱是否正確
                }
                break;
            case R.id.tvSignupback:
                finish();
                break;
        }
    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            result= DBConnector.executeQuery(strings[0], strings[1], strings[2],strings[3]);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equals("")) {
                try {
                    jObj = new JSONObject(result);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        Toast.makeText(ForgotPasswordActivity.this, "密碼修改成功請重新登入", Toast.LENGTH_SHORT).show();
                        finish();
                    }//登入成功
                    else {
                        Toast.makeText(ForgotPasswordActivity.this, "請確認帳號信箱是否輸入正確", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(ForgotPasswordActivity.this, "請確保網路暢通", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
