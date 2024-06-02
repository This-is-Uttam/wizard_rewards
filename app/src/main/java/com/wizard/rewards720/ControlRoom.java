package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.COINS;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DIAMONDS;
import static com.wizard.rewards720.Constants.USER_API_URL;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ControlRoom extends Application {



    private static ControlRoom INSTANCE = null;
    private String coins; //coins should be int
//    private String accessToken;
    private String fullName, userName, email, profilePic, userFirstLetter, phone, referCode, diamonds,id;


    private ControlRoom(){
        // not a single instance can be created from the other classes.
    }

    public static ControlRoom getInstance(){
        if (INSTANCE==null){
            synchronized (ControlRoom.class){
                if (INSTANCE==null){
                    INSTANCE = new ControlRoom();
                }
            }
        }
        return INSTANCE;
    }

    public String getId(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("id","");
    }


    public String getCoins(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("points","0");
    }

    public void setCoins(String coins, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("points",coins);
        editor.apply();
    }

    public String getDiamonds(Context context) {
       return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
               .getString("daimond","0");
    }

    public void setDiamonds(String diamonds, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("daimond",diamonds);
        editor.apply();
    }


    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public String getFullName(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("name","");
    }


    public String getUserName(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("username","");
    }

    public String getEmail(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("email","");
    }

    public String getProfilePic(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("image","");
    }

    public String getPhone(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("phone","");
    }

    public String getReferCode(Context context) {
        return context.getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("refer","");
    }

    public String getAccessToken(Context context) {
        return context.getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE)
                .getString("accessToken", "no_access_token");
    }

    public void setAccessToken(Context context, String accessToken) {
        context.getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE).edit()
                .putString("accessToken", accessToken)
                .apply();
    }

    public void setUserData(JSONObject jsonUserData, Context context){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("id", jsonUserData.getString("id"));
            editor.putString("name", jsonUserData.getString("name"));
            editor.putString("username", jsonUserData.getString("username"));
            editor.putString("email", jsonUserData.getString("email"));
            editor.putString("phone", jsonUserData.getString("phone"));
            editor.putString("image", jsonUserData.getString("image"));
            editor.putString("points", jsonUserData.getString("points"));
            editor.putString("daimond", jsonUserData.getString("daimond"));
            editor.putString("refer", jsonUserData.getString("refer"));
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateCoins(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateCoins", "onResponse: Sucessfull...:" + response.getString("data"));
                        int coins = response.getJSONObject("data").getInt("points");
                        int diamonds = response.getJSONObject("data").getInt("daimond");
                        ControlRoom.getInstance().setCoins(coins + "", context);
                        ControlRoom.getInstance().setDiamonds(diamonds + "",context);

                    } else
                        Log.d("updateCoins", "onResponse: Failed..." + response.getString("data"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateCoins", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
