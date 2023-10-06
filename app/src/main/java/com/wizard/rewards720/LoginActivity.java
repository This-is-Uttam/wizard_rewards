package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DEVICE_ID;
import static com.wizard.rewards720.Constants.FCM_ID;
import static com.wizard.rewards720.Constants.LOGIN_API_URL;
import static com.wizard.rewards720.Constants.UPDATE_FCM;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.Fragments.MoreFragment.WEB_CLIENT_ID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wizard.rewards720.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    GoogleSignInClient gsc;
    FirebaseAuth firebaseAuth;
    String fcmToken;
    String email;
    public static String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(WEB_CLIENT_ID)
                .requestProfile()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);
        firebaseAuth = FirebaseAuth.getInstance();


        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                            if (task.getResult() != null) {
                                Log.d("ooo", "onActivityResult: Login Activityy token " + task.getResult().getIdToken() + " profile " + task.getResult().getPhotoUrl());

                                firebaseAuthWithGoogle(task);
                            } else {
                                Log.d("ooo", "onActivityResult: Login Acitivity Null account");
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "No Account Selected", Toast.LENGTH_SHORT).show();
                            binding.progressBar2.setVisibility(View.GONE);
                            binding.googleSignIn.setClickable(true);
                            binding.googleSignIn.setEnabled(true);
                        }
                    }
                });

        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (account != null) {
            binding.googleSignIn.setClickable(false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else
            binding.googleSignIn.setClickable(true);*/

        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsc.getSignInIntent();
                launcher.launch(intent);
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.googleSignIn.setClickable(false);
                binding.googleSignIn.setEnabled(false);
            }
        });
    }

    private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task) {
        GoogleSignInAccount account = task.getResult();
        Log.d("ooo", "firebaseAuthWithGoogle: name: " + account.getDisplayName());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String name = firebaseUser.getDisplayName();
                        email = firebaseUser.getEmail();
                        firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    String idMainToken = task.getResult().getToken();
                                    authLogin(idMainToken, account.getDisplayName());

//                                    String fcmToken = getFcmToken();
                                    Log.d("firebaseAuthWithGoogle", "onComplete: task is successful, Your IdToken: " + idMainToken);
                                } else {
                                    Log.d("firebaseAuthWithGoogle", "onComplete: task is not successful for IdToken");
                                }
                            }
                        });

                    } else {
                        Log.d("ooo", "onComplete: firebaseUser is null");
                    }

                } else
                    Log.d("ooo", "onComplete: task is not successful" + task.getException());
            }
        });
    }

    private void authLogin(String idMainToken, String displayName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put(DEVICE_ID, getDeviceId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_API_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.getInt("code") == 201 && !response.getBoolean("status")){

                                    Log.d("authLogin", "onResponse: response is Failed " + response.getString("data"));
                                    gsc.signOut();
                                    binding.progressBar2.setVisibility(View.GONE);
//                                    Toast.makeText(LoginActivity.this, ""+response.getString("data"), Toast.LENGTH_LONG).show();

//                                    Error Dialog..........
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialog);

                                    View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.alert_dialog_layout, null);
                                    ImageView dialogImg = view.findViewById(R.id.dialogImg);
                                    TextView dialogTitle = view.findViewById(R.id.dialogTitle);
                                    TextView dialogSubtitle = view.findViewById(R.id.dialogSubtitle);
                                    TextView positiveBtn = view.findViewById(R.id.positiveBtn);
                                    TextView negativeBtn = view.findViewById(R.id.negativeBtn);

                                    dialogImg.setImageDrawable(getResources().getDrawable(R.drawable.baseline_error_24,getTheme()));
                                    dialogTitle.setText("Error!");
                                    dialogSubtitle.setText(response.getString("data"));
                                    positiveBtn.setText("Okay");
                                    negativeBtn.setVisibility(View.GONE);

                                    dialogBuilder.setView(view);
                                    dialogBuilder.setCancelable(false);
                                    AlertDialog dialog = dialogBuilder.create();

                                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();

                                }
                                else {
                                    Log.d("authLogin", "onResponse: response is Successfull " + response.getString("data"));
                                    accessToken = response.getString("data");
                                    Log.d("asdf", "onResponse: Shubham access token "+ accessToken);
                                    ControlRoom.getInstance().setAccessToken(accessToken);

                                    getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE).edit()
                                            .putString("accessToken", accessToken)
                                            .apply();

                                    getFcmToken();
                                    Toast.makeText(getApplicationContext(), "Welcome " + displayName, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("authLogin", "onResponse: error response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + idMainToken);
                return header;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public String getDeviceId() {
//        This may throw error somewhere...(Play Store)
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("getDeviceId", "getDeviceId: DeviceId: " + deviceId);
        return deviceId;
    }


    public String getFcmToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    fcmToken = task.getResult();
                    Log.d("getFcmToken", "onComplete: fcmToken: " + fcmToken);
                    updateFcmToken(fcmToken);
                } else
                    Log.d("getFcmToken", "onComplete: Failed to get FCM Token...");
            }
        });
        return fcmToken;

    }

    private void updateFcmToken(String fcmToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FCM_ID, fcmToken);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_FCM, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {

                        Log.d("updateFcmToken", "onResponse: ResPonse SucCess: " + response.getString("data"));
                        binding.progressBar2.setVisibility(View.GONE);

//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
                        checkReferral();

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("updateFcmToken", "onResponse: ResPonse FaiLed: " + response.getString("data"));
                    } else
                        Log.d("updateFcmToken", "onResponse: Something went wrong: " + response.getString("data"));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateFcmToken", "onResponse: error ResPonse:  " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + accessToken);
                Log.d("oooo", "getHeaders: access tokennnnnnn: "+ accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void checkReferral() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200){
                       int referStatus = response.getJSONObject("data").getInt("refer_status");

                       if (referStatus == 0){
//                           show referral activity..
                           Log.d("checkReferral", "onResponse: No Referral Code..refer_status: "+ referStatus);
                           startActivity(new Intent(LoginActivity.this, ReferralActivity.class));
                           finish();
                       }else {
                           Log.d("checkReferral", "onResponse: Referral Code Available..refer_status: "+ referStatus);

                           startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           finish();
                       }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}