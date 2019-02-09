package com.example.user.goeat_3.c_store_1;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.goeat_3.DBConnector;
import com.example.user.goeat_3.R;
import com.example.user.goeat_3.b_Main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.example.user.goeat_3.b_Main.MainActivity.nick_name;
import static com.example.user.goeat_3.b_Main.MainActivity.user_id;


public class InitAnOrderDiFragment extends DialogFragment {

    //宣告
    private View mView;
    private TextView txt_storeName, btn_cancel, btn_sendOrder;
    private TextView txt_orderTitle, txt_endTime, txt_deliveryTime, txt_remarks, txt_ok;
    private EditText edi_orderTitle, edi_remarks;
    private TextView txt_end_Date, txt_end_Time, txt_delivery_Date, txt_delivery_Time;  //結單日期,結單時間,取餐日期,取餐時間
    private Calendar c;  //建立日曆物件
    private String store_name;
    private String order_title = "";
    private String order_end_time = "";
    private String order_delivery_time = "";
    private String order_remarks = "";
    private String str_end_date = "";
    private String str_end_time = "";
    private String str_delivery_date = "";
    private String str_delivery_time = "";
    private String store_id, order_id;
    private Activity act;
    private JSONArray josnarray = null;
    private JSONObject jObj = null;
    private boolean check = false;
    private ConstraintLayout layout_ok;

    //通過重寫 onCreateDialog(Bundle savedInstanceState) 方法建立預設dialogFragment
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
//        dialog.setCancelable(false); // 設定點選螢幕Dialog不消失,無效
        dialog.setCanceledOnTouchOutside(false); // 設定點選螢幕Dialog不消失

        Bundle bundle = getArguments();
        if (bundle != null) {
            store_name = bundle.getString("store_name");
            store_id = bundle.getString("store_id");
            Log.d("abc", store_name);
        }

        c = Calendar.getInstance();   //建立日曆物件

//        return super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    //通過重寫 onCreateView 方法來自定義dialogFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //设置背景透明,實現圓角效果
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //引入Layout

//        txt_storeName.setText(store_name);

//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_init_an_order_di, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_storeName = view.findViewById(R.id.txt_storeName);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_sendOrder = view.findViewById(R.id.btn_sendOrder);
        edi_orderTitle = view.findViewById(R.id.edi_orderTitle);
        txt_end_Date = view.findViewById(R.id.txt_end_Date);
        txt_end_Time = view.findViewById(R.id.txt_end_Time);
        txt_delivery_Date = view.findViewById(R.id.txt_delivery_Date);
        txt_delivery_Time = view.findViewById(R.id.txt_delivery_Time);
        edi_remarks = view.findViewById(R.id.edi_remarks);

        txt_orderTitle = view.findViewById(R.id.txt_orderTitle);
        txt_endTime = view.findViewById(R.id.txt_endTime);
        txt_deliveryTime = view.findViewById(R.id.txt_deliveryTime);
        txt_remarks = view.findViewById(R.id.txt_remarks);
        txt_ok = view.findViewById(R.id.txt_ok);
        layout_ok = view.findViewById(R.id.layout_ok);


        //選擇截單日期
        txt_end_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog((MainActivity) getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                if ((month + 1) < 10 && dayOfMonth < 10) {
                                    txt_end_Date.setText(year + "-0" + (month + 1) + "-0" + dayOfMonth);
                                } else if ((month + 1) < 10) {
                                    txt_end_Date.setText(year + "-0" + (month + 1) + "-" + dayOfMonth);
                                } else if (dayOfMonth < 10) {
                                    txt_end_Date.setText(year + "-" + (month + 1) + "-0" + dayOfMonth);
                                } else {
                                    txt_end_Date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                                }
                                str_end_date = txt_end_Date.getText().toString();
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        //選擇截單時間
        txt_end_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog((MainActivity) getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                if (hourOfDay < 10 && minute < 10) {
                                    txt_end_Time.setText("0" + hourOfDay + ":0" + minute + ":00");
                                } else if (hourOfDay < 10) {
                                    txt_end_Time.setText("0" + hourOfDay + ":" + minute + ":00");
                                } else if (minute < 10) {
                                    txt_end_Time.setText(hourOfDay + ":0" + minute + ":00");
                                } else {
                                    txt_end_Time.setText(hourOfDay + ":" + minute + ":00");
                                }
                                str_end_time = txt_end_Time.getText().toString();
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), true);
                dialog.show();

            }
        });


        //選擇取餐日期
        txt_delivery_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog((MainActivity) getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                if ((month + 1) < 10 && dayOfMonth < 10) {
                                    txt_delivery_Date.setText(year + "-0" + (month + 1) + "-0" + dayOfMonth);
                                } else if ((month + 1) < 10) {
                                    txt_delivery_Date.setText(year + "-0" + (month + 1) + "-" + dayOfMonth);
                                } else if (dayOfMonth < 10) {
                                    txt_delivery_Date.setText(year + "-" + (month + 1) + "-0" + dayOfMonth);
                                } else {
                                    txt_delivery_Date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                                }
                                str_delivery_date = txt_delivery_Date.getText().toString();
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        //選擇取餐時間
        txt_delivery_Time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog((MainActivity) getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);

                                if (hourOfDay < 10 && minute < 10) {
                                    txt_delivery_Time.setText("0" + hourOfDay + ":0" + minute + ":00");
                                } else if (hourOfDay < 10) {
                                    txt_delivery_Time.setText("0" + hourOfDay + ":" + minute + ":00");
                                } else if (minute < 10) {
                                    txt_delivery_Time.setText(hourOfDay + ":0" + minute + ":00");
                                } else {
                                    txt_delivery_Time.setText(hourOfDay + ":" + minute + ":00");
                                }
                                str_delivery_time = txt_delivery_Time.getText().toString();
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), true);
                dialog.show();

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_title = edi_orderTitle.getText().toString();
                order_end_time = str_end_date + " " + str_end_time;
                order_delivery_time = str_delivery_date + " " + str_delivery_time;
                order_remarks = edi_remarks.getText().toString();

                if (!order_title.equals("") && !str_end_date.equals("") && !str_end_time.equals("") && !str_delivery_date.equals("") && !str_delivery_time.equals("")) {

                    // 2019-01-16 16:00:00
                    //Line 連結分享
                    if (!check) {
                        Log.d("zzzz", user_id);
                        new InitAnOrderDiFragment.ccon().execute("InitiateAnOrder", store_name, order_title, order_end_time, order_delivery_time, order_remarks, nick_name, user_id);
                        btn_sendOrder.setText("分享連結");
                        // 讓所有元件便唯讀
                        edi_orderTitle.setCursorVisible(false);
                        edi_orderTitle.setVisibility(View.GONE);

                        edi_orderTitle.setFocusable(false);
                        edi_orderTitle.setVisibility(View.GONE);

                        edi_orderTitle.setFocusableInTouchMode(false);
                        edi_orderTitle.setVisibility(View.GONE);

                        edi_remarks.setCursorVisible(false);
                        edi_remarks.setVisibility(View.GONE);

                        edi_remarks.setFocusable(false);
                        edi_remarks.setVisibility(View.GONE);


                        edi_remarks.setFocusableInTouchMode(false);
                        edi_remarks.setVisibility(View.GONE);

                        //txt_end_Date.setClickable(false);
                        txt_end_Date.setVisibility(View.GONE);
                        txt_end_Date.setText("");

                        //txt_end_Time.setClickable(false);
                        txt_end_Time.setVisibility(View.GONE);
                        txt_end_Time.setText("");

                        //txt_delivery_Date.setClickable(false);
                        txt_delivery_Date.setVisibility(View.GONE);
                        txt_delivery_Date.setText("");

                        //txt_delivery_Time.setClickable(false);
                        txt_delivery_Time.setVisibility(View.GONE);
                        txt_delivery_Time.setText("");

                        /*txt_storeName.setVisibility(View.GONE);
                        txt_orderTitle.setVisibility(View.GONE);
                        txt_endTime.setVisibility(View.GONE);
                        txt_deliveryTime.setVisibility(View.GONE);
                        txt_remarks.setVisibility(View.GONE);*/

                        txt_storeName.setText("");
                        txt_orderTitle.setText("");
                        txt_endTime.setText("");
                        txt_deliveryTime.setText("");
                        txt_remarks.setText("");
                        txt_ok.setVisibility(View.VISIBLE);

                        ViewGroup.LayoutParams lp;
                        lp = layout_ok.getLayoutParams();
                        //lp.width=400;
                        lp.height = 400;
                        layout_ok.setLayoutParams(lp);

                    }
                    if (check) {
                        final Dialog d = new Dialog(getActivity());
                        d.setContentView(R.layout.dialog_line_link);
                        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button btn_copy_d, btn_cancel_d, btn_invite_d;
                        final EditText edit_link_d;

                        btn_copy_d = d.findViewById(R.id.btn_copy_d);
                        edit_link_d = d.findViewById(R.id.edit_link_d);
                        btn_cancel_d = d.findViewById(R.id.btn_cancel_d);
                        btn_invite_d = d.findViewById(R.id.btn_invite_d);
                        Log.d("abcde", "測試3" + order_id);
                        edit_link_d.setText("http://34.80.250.76/httpname.php?store_name=" + store_name + "&&order_id=" + order_id);

                        d.show();
                        btn_sendOrder.setText("發起訂單");
                        btn_copy_d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                copyToClipboard(edit_link_d.getText().toString());
                                Log.v("hint", "已複製");   //複製連結
                            }
                        });

                        btn_cancel_d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.v("hint", "取消");   //複製連結
                                d.dismiss();   //回到店家主頁
                            }
                        });

                        btn_invite_d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.v("hint", "連接到line");   //分享到line
                                shareLine(edit_link_d.getText().toString());
                            }
                        });


                        act = getDialog().getOwnerActivity();
                        getDialog().dismiss();
                    }

                } else {

                    Toast.makeText((MainActivity) getActivity(), "資料尚未齊全", Toast.LENGTH_SHORT).show();
                    if (order_title.equals("")) {
                        edi_orderTitle.setError("訂單不可為空");
                    }
                    if (str_end_date.equals("") || str_end_time.equals("")) {
                        txt_end_Time.setError("截單日期時間請選擇");
                        Log.v("hint", str_end_date + " " + str_end_time);
                    }
                    if (str_delivery_date.equals("") || str_delivery_time.equals("")) {
                        txt_delivery_Time.setError("取餐日期時間請選擇");
                        Log.v("hint", str_delivery_date + " " + str_delivery_time);
                    }

                }

            }
        });

    }

    //複製的方法
    private void copyToClipboard(String str) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            Log.e("version", "1 version");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", str);
            clipboard.setPrimaryClip(clip);
            Log.e("version", "2 version");
        }
    }


    //当Fragment可見時用
    @Override
    public void onStart() {
        super.onStart();
        txt_storeName.setText(store_name);

        //DialogFragment寬高與位置的設定
        Dialog dialog = getDialog();
        if (dialog != null) {
            //設置透明度
            Window dialogWindow = getDialog().getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.alpha = 0.85f;
            lp.alpha = 1.0f;
            dialogWindow.setAttributes(lp);

            //返回鍵不執行
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()
                            == KeyEvent.ACTION_DOWN) {
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    private class ccon extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            Log.d("zzzz", strings[7]);
            String result = DBConnector.executeQuery(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);

            try {
                Log.d("abcde", result);
                jObj = new JSONObject(result);
                josnarray = jObj.getJSONArray("data");

                JSONObject jsonData = josnarray.getJSONObject(0);
                Log.d("abcde", "11: ");
                Log.d("abcde", jsonData.getString("order_id"));
                order_id = jsonData.getString("order_id").toString();

                check = true;
                Log.d("abcde", order_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }
    }

    //賴分享的function
    public void shareLine(String a) {

        String string = a;

        ComponentName cn = new ComponentName("jp.naver.line.android"
                , "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));
        //shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //shareIntent.setType("image/jpeg"); //圖片分享
        shareIntent.setType("text/plain"); // 純文
        //shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享的標題");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "這是我發起的團購訂單進入網址就可以下單了:" + string);
        shareIntent.setComponent(cn);//跳到指定APP的Activity
        act.startActivity(Intent.createChooser(shareIntent, ""));
    }
}
