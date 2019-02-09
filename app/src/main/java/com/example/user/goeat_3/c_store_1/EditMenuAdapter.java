package com.example.user.goeat_3.c_store_1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.e_record_1.RecordAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class EditMenuAdapter extends RecyclerView.Adapter<EditMenuAdapter.ViewHolder>{

    //宣告，儲存要顯示的資料
    private ArrayList<HashMap<String,String>> maps = new ArrayList<>();

    // 建構式，用來接收外部程式傳入的項目資料。
    public EditMenuAdapter(  ArrayList<HashMap<String,String>> food_maps) {

        maps=food_maps;
        if(maps.size()==0) {
            HashMap<String,String> map=new HashMap<>();
            map.put("menu_name","");         //菜名
            map.put("menu_price","");        //價錢
            maps.add(map);
        }
    }

    public ArrayList<HashMap<String,String>> getMenuString(){

        return maps;
    }

    @NonNull
    @Override
    public EditMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_add_menu, viewGroup, false);
        return new EditMenuAdapter.ViewHolder(mView,i);
    }

    @Override
    public void onBindViewHolder(@NonNull EditMenuAdapter.ViewHolder viewHolder, int i) {
        viewHolder.edi_menu_name.setText(maps.get(i).get("menu_name"));
        viewHolder.edi_menu_price.setText(maps.get(i).get("menu_price"));
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // 建立ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        private EditText edi_menu_name, edi_menu_price;  //菜名,菜
        private ImageView btn_remove_menu;  //移除菜項目

        public ViewHolder(@NonNull View itemView, final int position) {
            super(itemView);

            edi_menu_name=itemView.findViewById(R.id.edi_menu_name);
            edi_menu_price=itemView.findViewById(R.id.edi_menu_price);
            btn_remove_menu=itemView.findViewById(R.id.btn_remove_menu);

            edi_menu_price.addTextChangedListener(new TextWatcher() {
                //文字改變前
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                //文字改變中
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(!edi_menu_price.getText().toString().equals(maps.get(position).get("menu_price"))) {

                        maps.get(position).put("menu_price",edi_menu_price.getText().toString());
                        maps.get(position).put("menu_name",edi_menu_name.getText().toString());

                        if(position==maps.size()-1){
                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put("menu_name","");
                            map.put("menu_price","");
                            maps.add(map);
                            notifyItemInserted(maps.size());
                        }
                    }
                }
                //文字改變後
                @Override
                public void afterTextChanged(Editable s) { }
            });

            //移除菜單
            btn_remove_menu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(maps.size()>2) {
                        maps.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}
