package com.example.kimjihyeon.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FriendFragment extends Fragment {

    private LinearLayout mSearchArea;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View friendView = inflater.inflate(R.layout.fragment_friend, container, false);
        mSearchArea = friendView.findViewById(R.id.search_area);

        return friendView;
    }

    public void toggleSearchBar(){
        Log.e("DDDD", "id : " + mSearchArea.getId() + " isunll :" + mSearchArea.isActivated());
        mSearchArea.setVisibility(mSearchArea.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }


}
