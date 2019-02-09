package com.example.user.goeat_3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.token;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;

public class SignInUpActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    private JSONArray josnarray = null;
    private JSONObject jObj = null;
    LinearLayout llsignin, llsignup;
    private TextView btn_signin,btn_signup,btn_res,tvSignupback,google_login;
    private TextView btn_return,btn_forgotpassword;
    private GoogleSignInClient mGooglesigninClient;


    private TextInputEditText edi_user_id1,edi_user_password1,edi_user_password1_1,edi_nick_name; //for註冊
    private TextInputEditText edi_user_id2,edi_user_password2,edi_user_mail;  //for登入
    String user_id1="";              //註冊id
    String user_password1="";        //註冊密碼
    String nick_name1="";             //註冊暱稱
    String user_id2="";              //登入id
    String user_password2="";        //登入密碼
    String user_mail="";             //登入信箱

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private  void handleSignInResult(Task<GoogleSignInAccount>completedTask){

        try {
            GoogleSignInAccount account= completedTask.getResult(ApiException.class);


            nick_name=account.getDisplayName();

            user_id=account.getEmail();
            Toast.makeText(SignInUpActivity.this, "歡迎您:"+nick_name, Toast.LENGTH_SHORT).show();

            SharedPreferences userInfo = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();//获取Editor

            editor.putString("user_id",user_id);
            editor.putString("nick_name", nick_name);

            editor.commit();//提交修改

        }catch (ApiException e){
            Log.d("kay123", "錯誤: ");
        }
        if(!nick_name.equals("")&&!user_id.equals("")){

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putString("status","google_login");
            intent.putExtras(bundle);
            setResult(5, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        //Google 連線設定
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGooglesigninClient= GoogleSignIn.getClient(this,gso);

        llSignin =  findViewById(R.id.llSignin);
        llSignin.setOnClickListener(this);
        //LinearLayout singnin =(LinearLayout)findViewById(R.id.signin);
        llsignup =findViewById(R.id.llSignup);
        llsignup.setOnClickListener(this);
        tvSignupInvoker =  findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker =  findViewById(R.id.tvSigninInvoker);

        btnSignup= findViewById(R.id.btnSignup);
        btnSignin= findViewById(R.id.btnSignin);
        btn_res=findViewById(R.id.btn_res);
        tvSignupback=findViewById(R.id.tvSignupback);



        btn_signin = findViewById(R.id.btn_signin); //登入按鈕
        btn_signup = findViewById(R.id.btn_signup); //註冊按鈕
        google_login=findViewById(R.id.google_login); //google登入
        btn_return = findViewById(R.id.btn_return); //返回
        btn_forgotpassword = findViewById(R.id.btn_forgotpassword);//忘記密碼


        llSignup = findViewById(R.id.llSignup);
        llSignin = findViewById(R.id.llSignin);

        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSignupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSigninScreen = true;
                showSigninForm();
            }
        });

        showSigninForm();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
                if(isSigninScreen)
                    btnSignup.startAnimation(clockwise);
            }
        });

        edi_user_id1 = findViewById(R.id.edi_user_id1);
        edi_user_password1 = findViewById(R.id.edi_user_password1);
        edi_user_password1_1 = findViewById(R.id.edi_user_password1_1);
        edi_nick_name = findViewById(R.id.edi_nick_name);
        edi_user_id2 = findViewById(R.id.edi_user_id2);
        edi_user_password2 = findViewById(R.id.edi_user_password2);
        edi_user_mail = findViewById(R.id.edi_user_mail);


        //註冊按鈕
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_id1 = edi_user_id1.getText().toString();
                user_password1 = edi_user_password1.getText().toString();
                nick_name1 = edi_nick_name.getText().toString();
                Log.v("hint",user_id1+","+user_password1+","+nick_name1);

                if(edi_user_id1.getText().length()== 0||edi_user_password1.getText().length()== 0||edi_user_password1_1.getText().length()== 0||edi_nick_name.getText().length()== 0){
                    Toast.makeText(SignInUpActivity.this, "內容不可為空", Toast.LENGTH_SHORT).show();
                }else if(!(edi_user_password1.getText().toString()).equals(edi_user_password1_1.getText().toString())) {
                    Toast.makeText(SignInUpActivity.this, "確認密碼不相符", Toast.LENGTH_SHORT).show();
                }else if(!(Linkify.addLinks(edi_user_mail.getText(), Linkify.EMAIL_ADDRESSES))){
                    Toast.makeText(SignInUpActivity.this, "Mail格式不正確", Toast.LENGTH_SHORT).show();
                }else {
                    Log.v("hint","有進入");
                    new ccon().execute("registered", user_id1, user_password1, nick_name1,token,edi_user_mail.getText().toString());
                }
            }
        });

        //登入按鈕
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id2 = edi_user_id2.getText().toString();
                user_password2 = edi_user_password2.getText().toString();
                Log.v("hint",user_id2+","+user_password2);
                if(edi_user_id2.getText().length()== 0){
                    edi_user_id2.setError("帳號不可為空");
                }
                else if(edi_user_password2.getText().length()== 0){
                    Toast.makeText(SignInUpActivity.this,"密碼不可為空",LENGTH_SHORT).show();
                }
                else {
                    new ccon().execute("login", user_id2,user_password2);
                }
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        google_login.setOnClickListener(new View.OnClickListener() {//這邊放google登入
            @Override
            public void onClick(View v) {
                Intent signIntent =mGooglesigninClient.getSignInIntent();
                startActivityForResult(signIntent,1);
            }
        });

        btn_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInUpActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

    }

    private class ccon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            switch (strings[0]) {
                case "login":
                    Log.v("hint","有進入1");
                    result = DBConnector.executeQuery(strings[0], strings[1], strings[2]);
                    return result;
                case "registered":
                    result = DBConnector.executeQuery(strings[0], strings[1], strings[2], strings[3],strings[4],strings[5]);
                    return result;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                jObj = new JSONObject(result);
                int success=jObj.getInt("success");

                if(success==1){   //登入成功

                    JSONArray login=jObj.getJSONArray("data");

                    JSONObject c =login.getJSONObject(0);

                    nick_name =c.getString("nick_name");

                    user_id=c.getString("user_id");
                    Toast.makeText(SignInUpActivity.this, "歡迎您:"+nick_name, Toast.LENGTH_SHORT).show();

                    SharedPreferences userInfo = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userInfo.edit();//获取Editor
                    editor.putString("user_id", user_id);
                    editor.putString("nick_name", nick_name);
                    editor.commit();//提交修改


                    finish();
                }
                else if(success==0){
                    nick_name="";
                    Toast.makeText(SignInUpActivity.this, "帳號密碼輸入錯誤", Toast.LENGTH_SHORT).show();
                }
                else if(success==2){//註冊成功
                    Toast.makeText(SignInUpActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    showSigninForm();
                }
                else  if(success==3){//註冊失敗
                    Toast.makeText(SignInUpActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0f;
        llSignin.requestLayout();

        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 1f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 1f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0f;
        llSignup.requestLayout();

        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llSignin || v.getId() ==R.id.llSignup){
            // Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
