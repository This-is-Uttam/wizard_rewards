package com.wizard.rewards720.Fragments.Home;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BANNER_API_URL;
import static com.wizard.rewards720.Constants.BANNER_IMG_URL;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.COINS;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DIAMONDS;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.Constants.VOUCHER_MAIN_URL;
import static com.wizard.rewards720.Constants.VOUCHER_WINNERS_GET_API;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.PromotionAdapter;
import com.wizard.rewards720.Adapters.VoucherAllWinnersAdapter;
import com.wizard.rewards720.Adapters.VoucherMainAdapter;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.Modals.PromotionModal;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.Modals.UserModal;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.Modals.VoucherWinModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeRepository {
    private MutableLiveData<ArrayList<PromotionModal>> banners = new MutableLiveData<>();
    private MutableLiveData<ArrayList<VoucherMainModal>> vouchers = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TrendingModal>> products = new MutableLiveData<>();
    private MutableLiveData<ArrayList<VoucherWinModal>> voucherWins = new MutableLiveData<>();
    private MutableLiveData<UserModal> user = new MutableLiveData<>();

    public LiveData<ArrayList<PromotionModal>> getBannersList() {
        return banners;
    }

    public LiveData<ArrayList<VoucherMainModal>> getVouchersList(){return vouchers;}

    public LiveData<ArrayList<TrendingModal>> getProductList() {
        return products;
    }

    public LiveData<ArrayList<VoucherWinModal>> getVoucherWinsList(){
        return voucherWins;
    }

    public LiveData<UserModal> getUserData() {return user;}


    //    Promotion/Banners List
    public void fetchBannersDataFromApi(Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BANNER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getPromotionImage", "onResponse: response Sucessfull: " + response.getString("data"));

                        ArrayList<PromotionModal> promotionList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Log.d("showPromotionsBanner", "showPromotionsBanner: image: " + jsonObject.getString("image"));

                            PromotionModal promotionModal = new PromotionModal(BANNER_IMG_URL + jsonObject.getString("image"),
                                    jsonObject.getString("banner_url"));
                            promotionList.add(promotionModal);
                        }

                        Collections.reverse(promotionList);
                        banners.setValue(promotionList);


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getPromotionImage", "onResponse: response Failed: " + response.getString("data"));
                    } else {
                        Log.d("getPromotionImage", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getPromotionImage", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    //    Vouchers List
    public void fetchVouchersDataFromApi(Context context){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("getVoucherMainList", "onResponse: response Sucessfull: " + response.getString("data"));
                                ArrayList<VoucherMainModal> voucherMainList = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String mrp = jsonObject.getString("mrp");
                                    int price_per_spot = jsonObject.getInt("price_per_spot");
                                    String total_spot = jsonObject.getString("total_spot");
                                    String empty_spot = jsonObject.getString("empty_spot");
                                    String winnig_code = jsonObject.getString("winnig_code");
                                    String winnig_daimonds = jsonObject.getString("winnig_daimonds");
                                    String short_desc = jsonObject.getString("short_desc");
                                    String details = jsonObject.getString("details");
                                    String images = jsonObject.getString("images");
                                    String full_status = jsonObject.getString("full_status");
                                    boolean full_status_bool;
                                    if (Integer.parseInt(full_status) == 0) {
                                        full_status_bool = false;
                                    } else {
                                        full_status_bool = true;
                                    }


                                    VoucherMainModal voucherMainModal = new VoucherMainModal(
                                            "2", empty_spot, total_spot, name, price_per_spot, short_desc, details, id, images,
                                            full_status_bool, winnig_code);

                                    voucherMainModal.setMrp(jsonObject.getString("mrp"));

                                    voucherMainList.add(voucherMainModal);
                                }

//                                Collections.reverse(voucherMainList);
                                vouchers.setValue(voucherMainList);


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getVoucherMainList", "onResponse: response Failed: " + response.getString("data"));
                            } else {
                                Log.d("getVoucherMainList", "onResponse: Something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherMainList", "onResponse: error ResPonse:  " + error.getMessage());
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

    //    Trending/Products List
    public void fetchProductsDataFromApi(Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.PRODUCT_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getAllProductsHomeFrag", "onResponse: response Sucess: " + response.getString("data"));
                        ArrayList<TrendingModal> productArrayList = new ArrayList<>();
                        JSONArray productJsonArray = response.getJSONArray("data");

                        for (int i = 0; i < productJsonArray.length(); i++) {
                            JSONObject productJsonObject = productJsonArray.getJSONObject(i);

                            String image = productJsonObject.getString("images");
                            ArrayList<String> imageList = new ArrayList<>(Arrays.asList(image.split(",")));
                            Log.d("getAllProductsImage", "onResponse: imageList: " + imageList.get(0));
                            TrendingModal productModal = new TrendingModal("2",
                                    productJsonObject.getString("empty_spot"),
                                    productJsonObject.getString("name"),
                                    productJsonObject.getString("price_per_spot"),
                                    imageList.get(0),
                                    productJsonObject.getString("mrp"),
                                    productJsonObject.getString("total_spot"));

                            productModal.setTrendItemId(productJsonObject.getString("id"));
                            productModal.setShortDescription(productJsonObject.getString("short_desc"));
                            productModal.setDetails(productJsonObject.getString("details"));
                            if (productJsonObject.getInt("full_status") == 0)
                                productModal.setSpotFull(false);
                            else
                                productModal.setSpotFull(true);

                            productArrayList.add(productModal);
                        }

                        Collections.reverse(productArrayList);
                        products.setValue(productArrayList);


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getAllProductsHomeFrag", "onResponse: response Failed: " + response.getString("data"));

                    } else {
                        Log.d("getAllProductsHomeFrag", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAllProductsHomeFrag", "onResponse: error Response " + error.getMessage());
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

    //     Voucher Winners List
    public void fetchVoucherWinsDataFromApi(Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_WINNERS_GET_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getVoucherWinnersList", "onResponse: Sucessfull...:" + response.getString("data"));

                        ArrayList<VoucherWinModal> voucherWinList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            VoucherWinModal voucherWinModal = new VoucherWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("voucherName"),
                                    jsonObject.getString("voucherImage"),
                                    jsonObject.getString("mrp")

                            );
                            voucherWinModal.setVoucherWinnerCount(i + 1);
                            // Winning monnth
                            int monthNum = jsonObject.getInt("winning_month");
                            String month = "";

                            switch (monthNum) {
                                case 1:
                                    month = "January";
                                    break;
                                case 2:
                                    month = "February";
                                    break;
                                case 3:
                                    month = "March";
                                    break;
                                case 4:
                                    month = "April";
                                    break;
                                case 5:
                                    month = "May";
                                    break;
                                case 6:
                                    month = "June";
                                    break;
                                case 7:
                                    month = "July";
                                    break;
                                case 8:
                                    month = "August";
                                    break;
                                case 9:
                                    month = "September";
                                    break;
                                case 10:
                                    month = "October";
                                    break;
                                case 11:
                                    month = "November";
                                    break;
                                case 12:
                                    month = "December";
                                    break;

                            }
                            voucherWinModal.setWinnMonth(month);

                            voucherWinList.add(voucherWinModal);

                        }
//                        reverse list to descending order
                        Collections.reverse(voucherWinList);
                        voucherWins.setValue(voucherWinList);

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("getVoucherWinnersList", "onResponse: Failed..." + response.getString("data"));
                    } else {
                        Log.d("getVoucherWinnersList", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherWinnersList", "onResponse: error Response: " + error.getMessage());
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

    //      User Data
    public void fetchUserDataFromApi(Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateCoins", "onResponse: Sucessfull...:" + response.getString("data"));
                        UserModal userData = new UserModal();
                        JSONObject userObj = response.getJSONObject("data");
                        userData.setCoins(userObj.getString("points"));
                        userData.setDiamonds(userObj.getString("daimond"));

//                        COINS = response.getJSONObject("data").getInt("points");
//                        DIAMONDS = response.getJSONObject("data").getInt("daimond");
//                        ControlRoom.getInstance().setCoins(response.getJSONObject("data").getInt("points") + "", mainContext);
//                        ControlRoom.getInstance().setDiamonds(DIAMONDS + "", mainContext);

//                        binding.coinTxt.setText(COINS + "");
//                        binding.diamondTv.setText(DIAMONDS + "");
                        user.setValue(userData);

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
