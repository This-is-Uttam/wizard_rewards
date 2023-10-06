package com.wizard.rewards720;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class ControlRoom extends Application {



    private static ControlRoom INSTANCE = null;
    private String coins; //coins should be int
    private String accessToken;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(String diamonds) {
        this.diamonds = diamonds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserFirstLetter() {
        return userFirstLetter;
    }

    public void setUserFirstLetter(String userFirstLetter) {
        this.userFirstLetter = userFirstLetter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public void setUserData(JSONObject jsonUserData){
        try {
            setFullName(jsonUserData.getString("name"));
            setUserName(jsonUserData.getString("username"));
            setEmail(jsonUserData.getString("email"));
            setPhone(jsonUserData.getString("phone"));
            setProfilePic(jsonUserData.getString("image"));
            setCoins(jsonUserData.getString("points"));
            setDiamonds(jsonUserData.getString("daimond"));
            setReferCode(jsonUserData.getString("refer"));
            setId(jsonUserData.getString("id"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
