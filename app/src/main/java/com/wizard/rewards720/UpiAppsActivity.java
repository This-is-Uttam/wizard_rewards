package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.wizard.rewards720.Adapters.PayModeAdapter;
import com.wizard.rewards720.Modals.PayModeModal;
import com.wizard.rewards720.databinding.ActivityUpiAppsBinding;

import java.util.ArrayList;
import java.util.List;

public class UpiAppsActivity extends AppCompatActivity {
    ActivityUpiAppsBinding binding;
    private String upiAppPackageName;
    float coins;
    int coinPrice;
    public static final String UPI_APP_PACKAGE_INTENT = "UpiPackageIntent";
    ArrayList<PayModeModal> payModelist;
    private int coinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpiAppsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.upiAppActivityToolbar.customToolbar.setTitle("Buy Coins");

        upiAppPackageName = getIntent().getStringExtra(UPI_APP_PACKAGE_INTENT);
        coins = getIntent().getFloatExtra("COINS", 0f);
        coinPrice = getIntent().getIntExtra("PRICE", 0);
        coinId = getIntent().getIntExtra("BUY_COIN_ID", 0);

        binding.noOfCoins2.setText((int) coins+"");
        binding.coinPrice2.setText("₹"+coinPrice);


        binding.payModeRv.setAdapter(new PayModeAdapter(getListOfUpiApps(),this, coins, coinPrice, coinId));
        binding.payModeRv.setLayoutManager(new GridLayoutManager(this,4));
    }

    ArrayList<PayModeModal> getListOfUpiApps(){
        payModelist = new ArrayList<>();

        Intent upiIntent = new Intent(Intent.ACTION_MAIN);
        upiIntent.addCategory(Intent.CATEGORY_DEFAULT);
        upiIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        upiIntent.setAction(Intent.ACTION_VIEW);    //Intent.ACTION_MAIN remove
        Uri uri = new Uri.Builder().scheme("upi").authority("pay").build();
        upiIntent.setData(uri);

        List<ResolveInfo> upiApps = this.getPackageManager().queryIntentActivities(upiIntent,0);

        for(ResolveInfo info : upiApps){
            Drawable appDrawable = info.loadIcon(getPackageManager());
            String appName = (String) info.loadLabel(getPackageManager());
            String appPackageName = info.activityInfo.packageName;

            PayModeModal modeModal = new PayModeModal(appDrawable,appName,appPackageName);
            payModelist.add(modeModal);
//            binding.imageView8.setImageDrawable(drawable);
//            Log.d("getListOfUpiApps", "getListOfUpiApps: "+ appPackageName);  //info.resolvePackageName gives null
        }
        return payModelist;


    }
}