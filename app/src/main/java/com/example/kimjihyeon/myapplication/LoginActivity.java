package com.example.kimjihyeon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private SignInButton mSignInbtn;
    private GoogleApiClient mGoogleAPIClient;
    private GoogleSignInOptions mGoogleSignInoptions;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;

    private static int GOOGLE_LOGIN_OPEN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);
        mSignInbtn = (SignInButton)findViewById(R.id.google_sign_in_btn);
        mAuth = FirebaseAuth.getInstance();
        if ( mAuth.getCurrentUser() != null ){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GoogleSignInOptions mGoogleSignInoptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //연결 실패
                        Log.e("jh","구글 API Client 연결 실패");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInoptions)
                .build();

        mSignInbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { signIn(); }
        });
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == GOOGLE_LOGIN_OPEN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                // Google Sign In was successful, authentication with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {
                // Google Sign In failed
                Log.e("jh","구글 로그인 실패");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
                            if (task.isSuccessful()){
                                //로그인이 성공했을 때만 사용자를 불러온다.
                                FirebaseUser user = task.getResult().getUser();
                                Bundle eventBundle = new Bundle();
                                eventBundle.putString("email", user.getEmail());
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);
                            }else {
                                //로그인이 실패했을 때
                                Log.e("jh", "사용자를 불러올 수 없음");
                            }
                        }
                    }
                });
    }


}
