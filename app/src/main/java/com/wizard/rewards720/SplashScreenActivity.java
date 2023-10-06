package com.wizard.rewards720;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;
import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.wizard.rewards720.databinding.ActivitySplashScreenBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    //    String accessToken;
    private String unityGameID = "5369215";
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    public static final int IMMEDIATE_REQUEST_CODE = 106;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent, getTheme()));

        binding.refreshActivity.setVisibility(View.GONE);
        binding.splashProgress.setVisibility(View.GONE);

        binding.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.my_animation));
        accessToken = getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE).getString("accessToken", "niHaiToken shared Preference");
        Log.d("splash Token", "Splash Screen onCreate: My Access Token: " + accessToken);


//        All Initialisations are shifted to MainActivity ////////////////////////////////////////////

//       AppLovin Ad Initialisation ...............

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration appLovinSdkConfiguration) {
                Log.d("AppLovin", "onSdkInitialized: AppLovin Sdk initialised successfully");
            }
        });

//        Unity Ad Initialisation.......................
        UnityAds.initialize(this, unityGameID, false, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.d("UnityAd", "onInitializationComplete: Unity Ads");
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.d("UnityAd", "onInitializationFailed: Unity Ads " + error.toString());
            }
        });
/*
//        InBrain Initialization........................
        InBrain.getInstance().setInBrain(this, IN_BRAIN_CLIENT_ID, IN_BRAIN_SECRET_KEY, true, ControlRoom.getInstance().getUserName());
        Log.d("InBrain", "onCreate: Username: "+ ControlRoom.getInstance().getUserName());*/


        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (ControlRoom.isNetworkConnected(SplashScreenActivity.this)) {

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.USER_API_URL,
                            null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            binding.splashProgress.setVisibility(View.GONE);
                            try {
                                if (response.getBoolean("status") && response.getInt("code") == 200) {
//                                    String s = response.getString("data");
//                                    JSONObject object = response.getJSONObject("data");
                                    JSONObject userJsonObject = response.getJSONObject("data");

                                    Log.d("checkUserData", "onResponse: data: " + userJsonObject + "// id: " + userJsonObject.getString("id"));
                                    ControlRoom.getInstance().setId(userJsonObject.getString("id"));

                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                    finish();
                                } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                    Log.d("splash token", "splash token: " + accessToken);

                                    Log.d("checkUserData", "onResponse: data: " + response.getString("data"));
                                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Log.d("checkUserData", "Something went wrong..");
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.splashScreenRoot), "Something went wrong.", LENGTH_INDEFINITE)
                                            .setAction("Retry", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                    finish();
                                                }
                                            });
                                    snackbar.show();

                                }


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("checkUserData", "onResponse: error Response SSA" + error.getMessage());
                            Toast.makeText(SplashScreenActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            binding.splashProgress.setVisibility(View.GONE);

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.splashScreenRoot), "Something went wrong.", LENGTH_INDEFINITE)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            finish();
                                        }
                                    });
                            snackbar.show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Log.d("splash token", "splash token header: " + accessToken);
                            HashMap<String, String> header = new HashMap<>();
                            header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                            header.put(AUTHORISATION, BEARER + accessToken);
                            return header;
                        }


                    };

                    Volley.newRequestQueue(SplashScreenActivity.this).add(jsonObjectRequest);
                } else {
                    Log.d("isNetworkConnected", "run: No Internet available..");
                    Toast.makeText(SplashScreenActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    binding.refreshActivity.setVisibility(View.VISIBLE);
                    binding.splashProgress.setVisibility(View.GONE);
                }


            }
        };

        handler.postDelayed(runnable, 300);

        binding.refreshActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.splashProgress.setVisibility(View.VISIBLE);
                runnable.run();
            }
        });

//        Register for activity result [Immediate Update].
       /* activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // handle callback
                        int resultCode = result.getResultCode();
                        if (resultCode != RESULT_OK) {
                            Log.d("AppUpdate", "onActivityResult: Failed to update the app." + resultCode);
                            // If the update is cancelled or fails,
                            // you can request to start the update again.
                        }
                    }
                });*/


    }


   /* @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                (IntentSenderForResultStarter) activityResultLauncher,
                                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                                IMMEDIATE_REQUEST_CODE

                        );
                    } catch (IntentSender.SendIntentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
*/

}