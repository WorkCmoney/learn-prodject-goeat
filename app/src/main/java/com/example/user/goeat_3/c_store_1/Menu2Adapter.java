package com.example.user.goeat_3.c_store_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.goeat_3.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    RatingBar ratingBar;
    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> Update_maps = new ArrayList<>();
    // 建構式，用來接收外部程式傳入的項目資料。
    public Menu2Adapter(ArrayList<HashMap<String, Object>> data) {

        maps=data;
        no.add(10);
    }

    private ArrayList<Integer> no = new ArrayList<>();
    private  EditText edi_coment;
    //宣告, 添加點擊事件
    private Menu2Adapter.ItemClickListener ClickListener;  //創建一個介面

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        if(i==0){
            return new ViewHolder1(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_menu_2_1, viewGroup, false));
        }else{
            return new ViewHolder2(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_menu_2_2, viewGroup, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        Log.v("hint","onBindViewHolder:"+ i);
        if(i==0){

            if(ClickListener!=null){
                ((ViewHolder1)viewHolder).btn_share.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ClickListener.OnIvClick(v, i); //送出評論

                    }
                });
            }
        }else {
            ((ViewHolder2)viewHolder).txt_nick_name.setText(maps.get(i-1).get("nick_name").toString());
            ((ViewHolder2)viewHolder).txt_comment.setText(maps.get(i-1).get("comment").toString());
//            ((ViewHolder2)viewHolder).like_num.setText(maps.get(i-1).get("comment_like") + "個讚");
            ((ViewHolder2)viewHolder).comstar.setImageResource( (int) (maps.get(i-1).get("score")));
            ((ViewHolder2)viewHolder).img_username.setImageResource( (int) (R.drawable.person));
            ((ViewHolder2)viewHolder).txt_broadcastDate.setText(maps.get(i-1).get("Date").toString());



        }
    }

    //設置adapter接口
    public Menu2Adapter setClickListener(Menu2Adapter.ItemClickListener ClickListener){
        this.ClickListener = ClickListener;
        return this;
    }

    //聲明接口ItemClickListener
    public interface ItemClickListener{
        void OnIvClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return maps.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else {
            return 1;
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView txt_nick_name, txt_comment;
        private ImageView comstar,img_username;
        private TextView txt_broadcastDate; //評分日期

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            txt_nick_name = itemView.findViewById(R.id.txt_nick_name);
            txt_comment = itemView.findViewById(R.id.txt_comment);

            comstar=itemView.findViewById(R.id.comstar);
            img_username=itemView.findViewById(R.id.img_username);
            txt_broadcastDate = itemView.findViewById(R.id.txt_broadcastDate);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView test;
        private TextView btn_share;



        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            test = itemView.findViewById(R.id.test);
            btn_share = itemView.findViewById(R.id.btn_share);
            edi_coment=itemView.findViewById(R.id.edi_coment);

        }
    }

    public String get_coment(){
        return  edi_coment.getText().toString();
    }
    public RatingBar getstart(){

        return ratingBar;
    }
}
