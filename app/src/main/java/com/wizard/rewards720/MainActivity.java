package com.wizard.rewards720;

import static com.wizard.rewards720.Fragments.CoinFragment.POLLFISH_API_KEY;
import static com.wizard.rewards720.SplashScreenActivity.IMMEDIATE_REQUEST_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
//import com.prodege.Prodege;
//import com.prodege.builder.InitOptions;
//import com.prodege.listener.ProdegeException;
//import com.prodege.listener.ProdegeInitListener;
import com.wizard.rewards720.Fragments.CoinFragment;
import com.wizard.rewards720.Fragments.GiftFragment;
import com.wizard.rewards720.Fragments.Home.HomeFragment;
import com.wizard.rewards720.Fragments.MoreFragment;
import com.wizard.rewards720.Fragments.ReferFragment;
import com.wizard.rewards720.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment, coinFragment, referFragment, giftFragment, moreFragment, currentFragment;
    public static final String ROOT_FRAGMENT_TAG = "fragment_tag";
    int current_position = 1;
    NotificationCompat.Builder builder;
    MyReceiver myReceiver;
    ActivityMainBinding binding;
    private String unityGameID = "5369215";
    AppUpdateManager appUpdateManager;
    ActivityResultLauncher<IntentSenderRequest> updateLauncher;
    Bundle savedInstanceStateN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        savedInstanceStateN = savedInstanceState;

        // check in-app update....
        updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // handle callback
                        if (result.getResultCode() != RESULT_OK) {
                            Log.d("newUpdate", "onActivityResult: Update flow failed! Result code " + result.getResultCode());
                        }
                    }
                });

        checkForAppImmediateUpdate();

//        setTheme(androidx.appcompat.R.style.Base_Theme_AppCompat);

        View view = findViewById(R.id.mainContainer);
        bottomNavigationView = findViewById(R.id.bottomNav);
//        bottomNavigationView.setItemRippleColor(null);
        TextView statusText = findViewById(R.id.statusText);
        LinearLayout status = findViewById(R.id.status);

        myReceiver = new MyReceiver(statusText, status);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String androidId = null;
        try {

            androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (SecurityException exception) {
            Log.d("deviceId", "onCreate: Device Id exception: " + exception.getMessage());
        }

//        String androidId =Settings.Secure.ANDROID_ID;
        Log.d("deviceId", "onCreate: androidId: " + androidId);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            homeFragment = new HomeFragment(MainActivity.this);
            coinFragment = new CoinFragment();
            referFragment = new ReferFragment();
            giftFragment = new GiftFragment();
            moreFragment = new MoreFragment();

            bottomNavigationView.setClickable(false);

            fragmentTransaction.add(R.id.mainContainer, homeFragment, "homeFragment");

            fragmentTransaction.add(R.id.mainContainer, coinFragment, "coinFragment");
            fragmentTransaction.hide(coinFragment);

            fragmentTransaction.add(R.id.mainContainer, referFragment, "referFragment");
            fragmentTransaction.hide(referFragment);

            fragmentTransaction.add(R.id.mainContainer, giftFragment, "giftFragment");
            fragmentTransaction.hide(giftFragment);

            fragmentTransaction.add(R.id.mainContainer, moreFragment, "moreFragment");
            fragmentTransaction.hide(moreFragment);

            fragmentTransaction.commit();

            currentFragment = homeFragment;
        }else {
            homeFragment = getSupportFragmentManager().findFragmentByTag("homeFragment");
            coinFragment = getSupportFragmentManager().findFragmentByTag("coinFragment");
            referFragment = getSupportFragmentManager().findFragmentByTag("referFragment");
            giftFragment = getSupportFragmentManager().findFragmentByTag("giftFragment");
            moreFragment = getSupportFragmentManager().findFragmentByTag("moreFragment");

//            Log.d("saveCheck", "onSaveInstanceState: current Fragment tag in Else: "+ savedInstanceState.getString("currentFragmentTag"));
            String curFragTag = savedInstanceState.getString("currentFragmentTag");
            currentFragment = getSupportFragmentManager().findFragmentByTag(curFragTag);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            /*transaction.add(R.id.mainContainer,homeFragment);
            transaction.hide(homeFragment);

            transaction.add(R.id.mainContainer,coinFragment);
            transaction.hide(coinFragment);

            transaction.add(R.id.mainContainer,referFragment);
            transaction.hide(referFragment);

            transaction.add(R.id.mainContainer,giftFragment);
            transaction.hide(giftFragment);

            transaction.add(R.id.mainContainer,moreFragment);
            transaction.hide(moreFragment);*/

            transaction.show(currentFragment);

            transaction.commit();



        }


//        fetchUserDetails();
        /*if (savedInstanceState != null) {
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
        }*/

//    setFragment(homeFragment);

//        fragmentManager.beginTransaction().replace(R.id.mainContainer, homeFragment, ROOT_FRAGMENT_TAG).commit();

//        first Initialising HomeFragment;


//      Item Selected.............
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
//                        current_position = 1;

                        switchFragment(homeFragment);
                        break;
                    case R.id.menuDraw:
//                        current_position = 2;

                        switchFragment(coinFragment);
                        break;
                    case R.id.referNearn:
//                        current_position = 3;

                        switchFragment(referFragment);
                        break;
                    case R.id.gifts:
//                        current_position = 4;

                        switchFragment(giftFragment);
                        break;

                    case R.id.menuMore:
//                        current_position = 5;

                        switchFragment(moreFragment);
                        break;


                }
                return true;
            }
        });




        // Initialize Pollfish Proedge Sdk

//        String uid = ControlRoom.getInstance().getId(this);
//        if (uid != null && !uid.equals("")){
//            InitOptions.Builder optionsBuilder = new InitOptions.Builder()
//                    .testMode(false)
//                    .userId(uid);
//            InitOptions initOptions = optionsBuilder.build();
//
//            // Proedge Initialization.......
//
//            Prodege.initialize(this, POLLFISH_API_KEY, new ProdegeInitListener() {
//                @Override
//                public void onSuccess() {
//                    Log.d("ProedgeAds", "onSuccess: Initilization Proedge: ");
//
//                }
//
//                @Override
//                public void onError(@NonNull ProdegeException e) {
//                    Log.d("ProedgeAds", "onError: Initilization Proedge: "+ e.getMessage());
//                }
//            }, initOptions);
//        }






// oncreate finished here......
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        Log.d("saveCheck", "onSaveInstanceState: current Fragment tag: "+ currentFragment.getTag());

        outState.putString("currentFragmentTag", currentFragment.getTag());
    }


    private void switchFragment(Fragment targetFragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(currentFragment);
        transaction.show(targetFragment);
        transaction.commit();

        currentFragment = targetFragment;
    }

    public void setFragment(Fragment fragment) {

        if (savedInstanceStateN == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, fragment, "HOME_FRAGMENT");
//        fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


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

        if (requestCode == IMMEDIATE_REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                Log.d("AppUpdate", "onActivityResult: Failed to update the app." + resultCode);
            }
        }

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


    public void pushNotification() {

        String channelId = "fcm_fallback_notification_channel";
        Intent intent = new Intent();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {100, 1000, 100, 1000};

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


//        Notification.Builder builder = new Notification.Builder(this)
        builder = new NotificationCompat.Builder(getApplicationContext(), channelId).setSmallIcon(R.drawable.money).setContentTitle("Notify Title").setContentText("Notify text").setSubText("Home")
//                .setSound(uri)
                .setTicker("lastWarning").setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_ALL).setVibrate(pattern).setVibrate(new long[]{1000, 1000}).setAutoCancel(true);

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


    private void checkForAppImmediateUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Log.d("newUpdate", "appUpdateManager created");


        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        Log.d("newUpdate", "appUpdateInfoTask initialised " + appUpdateInfoTask.isComplete());

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                                                   @Override
                                                   public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                                       Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                                                       if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                                               // This example applies an immediate update. To apply a flexible update
                                                               // instead, pass in AppUpdateType.FLEXIBLE
                                                               && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                                           Log.d("newUpdate", "checkForAppImmediateUpdate: Update available");
                                                           // Request the update.

                                                           appUpdateManager.startUpdateFlowForResult(
                                                                   // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                                                   appUpdateInfo,
                                                                   // an activity result launcher registered via registerForActivityResult
                                                                   updateLauncher,
                                                                   // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                                                                   // flexible updates.
                                                                   AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                                                           .setAllowAssetPackDeletion(true)
                                                                           .build());
                                                       } else {

                                                           // Run the app normally


                                                       }
                                                   }

                                               }

        );

    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, updateLauncher, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
            }
        });
    }

}