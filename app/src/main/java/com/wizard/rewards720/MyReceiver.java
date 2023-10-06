package com.wizard.rewards720;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyReceiver extends BroadcastReceiver {
    TextView statusText;
    LinearLayout status;
    MyReceiver(TextView statusText, LinearLayout status){
        this.statusText = statusText;
        this.status = status;
    }
    @Override
    public void onReceive(Context context, Intent intent) {



        if (!ControlRoom.isNetworkConnected(context)){
            Animation slideUp = AnimationUtils.loadAnimation(context,R.anim.slide_up_anim);
            status.startAnimation(slideUp);
                status.setVisibility(View.VISIBLE);
                status.setBackgroundColor(Color.RED);
                statusText.setText("No Network...");
//            Toast.makeText(context, "No Network is Connected", Toast.LENGTH_LONG).show();
        }else {
            Animation slideDown = AnimationUtils.loadAnimation(context,R.anim.slide_down_anim);
            status.startAnimation(slideDown);
            status.setVisibility(View.GONE);
            status.setBackgroundColor(Color.GREEN);
            statusText.setText(" Network... Connected");

//            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        }

    }
}
