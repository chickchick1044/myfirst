package com.example.kimjihyeon.myapplication.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kimjihyeon.myapplication.DLog;
import com.example.kimjihyeon.myapplication.R;
import com.example.kimjihyeon.myapplication.adapters.FriendListAdapter;
import com.example.kimjihyeon.myapplication.customviews.RecyclerViewItemClickListener;
import com.example.kimjihyeon.myapplication.models.User;
import com.example.kimjihyeon.myapplication.views.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class FriendFragment extends Fragment {

    private Context context;
    private LinearLayout mSearchArea;
    private EditText mEditEmail;
    private Button mBtnSearch;
    private RecyclerView mRecyclerView;
    private FriendListAdapter mFriendListAdapter;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mFriendsDBRef;
    private DatabaseReference mUserDBRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View friendView = inflater.inflate(R.layout.fragment_friend, container, false);
        context = container.getContext();
        mSearchArea = friendView.findViewById(R.id.search_area);
        mEditEmail = friendView.findViewById(R.id.edt_email);
        mBtnSearch = friendView.findViewById(R.id.btn_search);
        mRecyclerView= friendView.findViewById(R.id.recyclerview_friend);
        mBtnSearch.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                addFriend();
            }
        });

        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDB = mFirebaseDB.getInstance();

        mFriendsDBRef = mFirebaseDB.getReference("users").child(mFirebaseUser.getUid()).child("friends");
        mUserDBRef = mFirebaseDB.getReference("users");

        addFriendListener();

        mFriendListAdapter = new FriendListAdapter();
        mRecyclerView.setAdapter(mFriendListAdapter);
        mRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), new RecyclerViewItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                final User friend = mFriendListAdapter.getItem(position);

                if (mFriendListAdapter.getSelectionMode() == FriendListAdapter.UNSELECTION_MODE) {
                    Snackbar.make(view, friend.getName()+"님과 대화를 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                            chatIntent.putExtra("uid", friend.getUid());
                            startActivity(chatIntent);
                        }
                    }).show();
                } else {
                    friend.setSelection(friend.isSelection() ? false : true);
                    int selectedUserCount = mFriendListAdapter.getSelectionUsersCount();
                    Snackbar.make(view, selectedUserCount+"명과 대화를 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                            chatIntent.putExtra("uids", mFriendListAdapter.getSelectedUids());
                            startActivity(chatIntent);
                        }
                    }).show();
                }

            }
        }));

        return friendView;
    }
    
    public void toggleSearchBar(){
        DLog.e( "id : " + mSearchArea.getId() + " is nll :" + mSearchArea.isActivated());
        mSearchArea.setVisibility(mSearchArea.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void toggleSelectionMode(){
        mFriendListAdapter
                .setSelectionMode(mFriendListAdapter.getSelectionMode() == mFriendListAdapter.SELECTION_MODE ? mFriendListAdapter.UNSELECTION_MODE : mFriendListAdapter.SELECTION_MODE);
    }

    public void addFriend(){

        final String inputEmail = mEditEmail.getText().toString();
        if ( inputEmail.isEmpty()) {
            Toast.makeText(context, "이메일을 입력해주세요. ", Toast.LENGTH_LONG).show();
            return;
        }
        if ( inputEmail.equals(mFirebaseUser.getEmail())) {
            Toast.makeText(context, "자기자신은 친구로 등록할 수 없습니다. ", Toast.LENGTH_LONG).show();
            return;
        }

        //나의 정보를 조회하여 이미 등록된 친구인지 확인
        mFriendsDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> friendsIteratble = dataSnapshot.getChildren();
                Iterator<DataSnapshot> friendsIterator = friendsIteratble.iterator();

                while ( friendsIterator.hasNext()) {
                    User user = friendsIterator.next().getValue(User.class);

                    if ( user.getEmail().equals(inputEmail)) {
                        Toast.makeText(context, "이미 등록된 친구입니다. ", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // 이미 등록된 친구가 아니라면
                mUserDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> userIterator = dataSnapshot.getChildren().iterator();
                        int userCount = (int)dataSnapshot.getChildrenCount(); //총 사용자의 인원
                        int loopCount = 1; //루프를 몇번 돌았는지

                        //다음번째 커서가 존재한다면 친구등록을 실행
                        while (userIterator.hasNext()) {

                            final User currentUser = userIterator.next().getValue(User.class); //현재 가리키고있는 유저

                            //입력한 유저의 정보와 현재가리키고있는 사용자가 같다면
                            if ( inputEmail.equals(currentUser.getEmail())) {
                                mFriendsDBRef.push().setValue(currentUser, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(final DatabaseError databaseError, DatabaseReference databaseReference) {

                                        //나에게도 친구 정보를 등록
                                        mUserDBRef.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                mUserDBRef.child(currentUser.getUid()).child("friends").push().setValue(user);
                                                Toast.makeText(context, " 친구등록이 완료되었습니다. ", Toast.LENGTH_LONG).show();
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });
                                    }
                                });
                            } else {
                                //입력한 유저의 정보와 현재 가리키고있는 사용자가 다르다면
                                if ( loopCount++ >= userCount ) {
                                    Toast.makeText(context, "가입하지 않은 친구입니다. ", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    } //add Friend


    private void addFriendListener() {
        mFriendsDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User friend = dataSnapshot.getValue(User.class);
                drawUI(friend);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void drawUI(User friend) {
        mFriendListAdapter.addItem(friend);
    }

}//FriendFragment
