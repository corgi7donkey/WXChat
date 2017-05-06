package com.bupt.wxchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sjb on 2017/5/6.
 */

public class ViewPagerFragment extends Fragment{
    private String mTitle;
    public static final String BUNDLE_TITLE="title";

    public static ViewPagerFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        ViewPagerFragment fragment=new ViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        if (bundle!=null){
            mTitle=bundle.getString(BUNDLE_TITLE);
        }
        TextView tv = new TextView(getActivity());
        tv.setGravity(Gravity.CENTER);
        tv.setText(mTitle);
        tv.setTextSize(25);

        return tv;
    }
}
