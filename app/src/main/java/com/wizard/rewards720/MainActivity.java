package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.LoginActivity.accessToken;
import static com.wizard.rewards720.SplashScreenActivity.IMMEDIATE_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.wizard.rewards720.Fragments.CoinFragment;
import com.wizard.rewards720.Fragments.GiftFragment;
import com.wizard.rewards720.Fragments.HomeFragment;
import com.wizard.rewards720.Fragments.MoreFragment;
import com.wizard.rewards720.Fragments.ReferFragment;
import com.wizard.rewards720.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment, drawFragment, referFragment, giftFragment, moreFragment;
    public static final String ROOT_FRAGMENT_TAG = "fragment_tag";
    int current_position = 1;
    NotificationCompat.Builder builder;
    MyReceiver myReceiver;
    ActivityMainBinding binding;
    private String unityGameID = "5369215";
    AppUpdateManager appUpdateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // check in-app update....
        checkForAppImmediateUpdate();

//        setTheme(androidx.appcompat.R.style.Base_Theme_AppCompat);

        View view = findViewById(R.id.mainContainer);
        bottomNavigationView = findViewById(R.id.bottomNav);
        TextView statusText = findViewById(R.id.statusText);
        LinearLayout status = findViewById(R.id.status);

        myReceiver = new MyReceiver(statusText, status);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String androidId = null;
        try {

            androidId = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (SecurityException exception) {
            Log.d("deviceId", "onCreate: Device Id exception: " + exception.getMessage());
        }

//        String androidId =Settings.Secure.ANDROID_ID;
        Log.d("deviceId", "onCreate: androidId: " + androidId);

        if (!(networkInfo != null && networkInfo.isConnected())) {

            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE).show();

        } else {
            fetchUserDetails();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        drawFragment = new CoinFragment();
        referFragment = new ReferFragment();
        giftFragment = new GiftFragment();
        moreFragment = new MoreFragment();

        if (savedInstanceState != null) {
            current_position = savedInstanceState.getInt("FRAG_POSITION", 1);
            switch (current_position) {
                case 1:
                    setFragment(homeFragment);
                    break;
                case 2:
                    setFragment(drawFragment);
                    break;
                case 3:
                    setFragment(referFragment);
                    break;
                case 4:
                    setFragment(giftFragment);
                    break;
                case 5:
                    setFragment(moreFragment);
                    break;

            }
        } else {
            setFragment(homeFragment);
        }


//        fragmentManager.beginTransaction().replace(R.id.mainContainer, homeFragment, ROOT_FRAGMENT_TAG).commit();

//        first Initialising HomeFragment;


//      Item Selected.............
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        current_position = 1;

                        setFragment(homeFragment);
                        break;
                    case R.id.menuDraw:
                        current_position = 2;

                        setFragment(drawFragment);
                        break;
                    case R.id.referNearn:
                        current_position = 3;

                        setFragment(referFragment);
                        break;
                    case R.id.gifts:
                        current_position = 4;

                        setFragment(giftFragment);
                        break;

                    case R.id.menuMore:
                        current_position = 5;

                        setFragment(moreFragment);
                        break;


                }
                return true;
            }
        });


//        Snackbar snackbar = Snackbar.make(findViewById(R.id.activityMainId), "Something went wrong.", LENGTH_INDEFINITE)
//                .setAction("Retry", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
//                finish();
//            }
//        });
//        snackbar.show();



/*//        InBrain Initialization........................
        InBrain.getInstance().setInBrain(this, IN_BRAIN_CLIENT_ID, IN_BRAIN_SECRET_KEY, true, ControlRoom.getInstance().getUserName());
        Log.d("InBrain", "onCreate: Username: "+ ControlRoom.getInstance().getUserName());*/

    }

    private void fetchUserDetails() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("fetchUserDetails", "onResponse: Sucessfull...:" + response.getString("data"));
                        JSONObject userJsonObject = response.getJSONObject("data");
                        ControlRoom.getInstance().setUserData(userJsonObject);

                    } else
                        Log.d("fetchUserDetails", "onResponse: Failed..." + response.getString("data"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchUserDetails", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String finalImg;
        Uri finalUri;
        if (resultCode == 2 && requestCode == 1) {
            if (data != null) {
                finalImg = data.getStringExtra("FINAL_URI");
                finalUri = Uri.parse(finalImg);

            }

        } else if (resultCode == 2 && requestCode == 10) {
            if (data != null) {
                finalImg = data.getStringExtra("FINAL_URI");
                finalUri = Uri.parse(finalImg);

            }
        }

        if (requestCode == IMMEDIATE_REQUEST_CODE){

            if (resultCode != RESULT_OK){
                Log.d("AppUpdate", "onActivityResult: Failed to update the app." + resultCode);
            }
        }

    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment, "HOME_FRAGMENT");
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (binding.bottomNav.getSelectedItemId() == R.id.menuHome) {
//            finish();
            AlertDialog alertDialog;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialog);

            View dialogLayout = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
            TextView dialogTitle = dialogLayout.findViewById(R.id.dialogTitle);
            TextView yesExit = dialogLayout.findViewById(R.id.positiveBtn);
            TextView dontExit = dialogLayout.findViewById(R.id.negativeBtn);
            dialogBuilder.setView(dialogLayout);

            alertDialog = dialogBuilder.create();

            yesExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();

                }
            });
            dontExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.setCancelable(true);
            alertDialog.show();
        } else {
            binding.bottomNav.setSelectedItemId(R.id.menuHome);
        }


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("FRAG_POSITION", current_position);
    }

    public void pushNotification() {

        String channelId = "fcm_fallback_notification_channel";
        Intent intent = new Intent();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent,
                PendingIntent.FLAG_IMMUTABLE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {100, 1000, 100, 1000};

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


//        Notification.Builder builder = new Notification.Builder(this)
        builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.money)
                .setContentTitle("Notify Title")
                .setContentText("Notify text")
                .setSubText("Home")
//                .setSound(uri)
                .setTicker("lastWarning")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(pattern)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "push_Noti22", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.setShowBadge(true);
            notificationChannel.setShowBadge(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.canBubble();
            }

            int visibility = NotificationCompat.VISIBILITY_PUBLIC;
            notificationChannel.setLockscreenVisibility(visibility);

            manager.createNotificationChannel(notificationChannel);

            builder.setChannelId(channelId);
        }
        manager.notify(1, builder.build());

    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(myReceiver, intentFilter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }
    private void checkForAppImmediateUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Log.d("newUpdate", "appUpdateManager created");


        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        Log.d("newUpdate", "appUpdateInfoTask initialised " + appUpdateInfoTask.isComplete());

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(
                new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                // This example applies an immediate update. To apply a flexible update
                                // instead, pass in AppUpdateType.FLEXIBLE
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                            // Request the update.

                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                        appUpdateInfo,
                                        // an activity result launcher registered via registerForActivityResult
                                        AppUpdateType.IMMEDIATE
                                        , MainActivity.this,
                                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                                        // flexible updates.
                                        IMMEDIATE_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                throw new RuntimeException(e);
                            }
                        }else {

                            // Run the app normally


                        }
                    }

                }

        );

    }


}