package com.example.user.goeat_3.e_record_1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.goeat_3.R;
import com.example.user.goeat_3.b_Main.MainActivity;


public class RecordFragment extends Fragment {

    //宣告
    private View mView;
    private MainActivity mainActivity;
    private ViewPager viewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_record, container, false);

        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        viewPager = mView.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = mView.findViewById(R.id.tableLayout);
        tabLayout.setupWithViewPager(viewPager);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ToolBar 設定
        setHasOptionsMenu(true);  //重要!
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//隱藏標題
        Toolbar toolbar = view.findViewById(R.id.toolbar_record);
        toolbar.inflateMenu(R.menu.base_toolbar_menu_record);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        mainActivity.setLogout();
                    default:
                        return true;
                }
            }
        });
    }

    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    Record1Fragment record_new = new Record1Fragment();
                    return record_new;
                case 1:
                    Record2Fragment record_old = new Record2Fragment();
                    return record_old;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "新進訂單";  //getResources().getText(R.string.Record1_title);
                case 1:
                    return "歷史訂單";
            }

            return null;
        }
    }


}