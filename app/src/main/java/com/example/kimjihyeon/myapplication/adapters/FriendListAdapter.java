package com.example.kimjihyeon.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kimjihyeon.myapplication.R;
import com.example.kimjihyeon.myapplication.models.User;
import com.example.kimjihyeon.myapplication.customviews.RoundedImageView;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendHolder> {

    public static final int UNSELECTION_MODE = 1; //단일 선택 모드
    public static final int SELECTION_MODE = 2; //복수 선택 모드

    private int selectionMode = UNSELECTION_MODE;

    private ArrayList<User> friendList;

    public FriendListAdapter(){
        friendList = new ArrayList<>();
    }

    public void addItem(User friend){
        friendList.add(friend);
        notifyDataSetChanged();
    }

    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public int getSelectionMode() {
        return this.selectionMode;
    }

    public int getSelectionUsersCount() {
        int selectedCount = 0;
        for ( User user : friendList) {
            if ( user.isSelection() ) {
                selectedCount++;
            }
        }
        return selectedCount;
    }

    public String [] getSelectedUids() {
        String [] selecteUids = new String[getSelectionUsersCount()];
        int i = 0;
        for ( User user : friendList) {
            if ( user.isSelection() ) {
                selecteUids[i] = user.getUid();
            }
        }
        return selecteUids;
    }


    public User getItem(int position){
        return this.friendList.get(position);
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_friend_item, parent, false);
        FriendHolder friendholder = new FriendHolder(view);
        return friendholder;
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int position) {
        User friend = getItem(position);
        holder.mEmailView.setText(friend.getEmail());
        holder.mNameView.setText(friend.getName());
        if(friend.getProfileUrl() != null){
            //holder.mProfileView.set
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendHolder extends RecyclerView.ViewHolder{

        RoundedImageView mProfileView;
        TextView mNameView;
        TextView mEmailView;

        private FriendHolder(View itemView) {
            super(itemView);
            mProfileView = (RoundedImageView) itemView.findViewById(R.id.thumb);
            mNameView = (TextView) itemView.findViewById(R.id.tv_name);
            mEmailView = (TextView) itemView.findViewById(R.id.tv_email);

        }
    }

}
