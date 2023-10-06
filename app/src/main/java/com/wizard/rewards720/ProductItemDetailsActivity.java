package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.ProductItemImageAdapter;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.databinding.ActivityProductItemDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProductItemDetailsActivity extends AppCompatActivity {

    ActivityProductItemDetailsBinding binding;

    ArrayList<Integer> productItemImgList;
    int position = 0;
    ArrayList<TrendingModal> trendingItemList;
    String trendItemId;
    int pricePerSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

//        loader
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.message.setVisibility(View.GONE);
        binding.errorImg.setVisibility(View.GONE);





//      bidnow disable..
        binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
        binding.bidNow.setClickable(false);
        binding.bidNow.setEnabled(false);
        binding.bidNow.setText("Please Wait");

        Intent intent = getIntent();
        trendItemId = intent.getStringExtra("TrendItemId");
        Log.d("qwert", "onCreate: TrendItemId" + trendItemId);
        productItemImgList = new ArrayList<>();

        getProductItem();

        binding.bidNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCoinDialog();

            }
        });


    }

    private void buyCoinDialog() {
        AlertDialog buyCoinDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialog);

        //   diamondsRequired = 10% of pricePerSpot
        int diamondsRequired = (int) Math.floor ((pricePerSpot *10)/100);
//        coinsAfterDiamondsDeduct is coins - diamondsRequired;
        int coinsAfterDiamondsDeduct = pricePerSpot - diamondsRequired;

        View dialogView = getLayoutInflater().inflate(R.layout.buy_coin_dialog_layout, null);
        RadioButton coinsOnlyRadio = dialogView.findViewById(R.id.onlyCoinsRadio);
        RadioButton coinsNdiamondRadio = dialogView.findViewById(R.id.coinsNdiamondRadio);
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView continueBtn = dialogView.findViewById(R.id.positiveBtn);
        TextView cancelBtn = dialogView.findViewById(R.id.negativeBtn);

        dialogBuilder.setView(dialogView);
        coinsOnlyRadio.setText("Use only coins ("+ pricePerSpot+ " Coins)");
        coinsNdiamondRadio.setText("Use "+ coinsAfterDiamondsDeduct + " Coins + "+diamondsRequired+" Diamonds");
        buyCoinDialog = dialogBuilder.create();

        int userCoins = Integer.parseInt(ControlRoom.getInstance().getCoins());
        int userDiamonds = Integer.parseInt(ControlRoom.getInstance().getDiamonds());


        boolean isCoinsOnlyBidding;

        //code subham
        Log.d("subham", "buyCoinDialog: price per spot ="+ pricePerSpot+" percent diamond="+diamondsRequired+" coin after deduction=" + coinsAfterDiamondsDeduct);
        if(userCoins >= pricePerSpot && userDiamonds >= diamondsRequired){
            //means user has enough coins and diamond to bid
            coinsNdiamondRadio.setEnabled(true);
            coinsOnlyRadio.setEnabled(true);
            coinsOnlyRadio.setChecked(true);

        }else if(userCoins >= coinsAfterDiamondsDeduct &&  userDiamonds >= diamondsRequired){
            //means user has to use coins with diamond only
            coinsOnlyRadio.setEnabled(false);
            coinsNdiamondRadio.setChecked(true);

        } else if (userCoins >= pricePerSpot && userDiamonds < diamondsRequired) {
            //means user has only coins to bid
            coinsNdiamondRadio.setEnabled(false);
            coinsOnlyRadio.setChecked(true);
        }else{
            //not enough coin and diamond to bid
            coinsNdiamondRadio.setEnabled(false);
            coinsOnlyRadio.setEnabled(false);

        }



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coinsOnlyRadio.isChecked()){
                    bidding(Constants.COINS_ONLY_BID_VALUE);
                    buyCoinDialog.dismiss();
                    finish();
                } else if (coinsNdiamondRadio.isChecked()) {
                    bidding(Constants.COINS_DIAMOND_BID_VALUE);
                    buyCoinDialog.dismiss();
                    finish();

                }else {
                    Toast.makeText(ProductItemDetailsActivity.this, "No option is selected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCoinDialog.dismiss();
            }
        });


        buyCoinDialog.show();
    }

    private void bidding(int radioValue) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.PRODUCT_BID_API_URL + "/" + trendItemId +"/"+radioValue, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("bidding", "onResponse: response Sucessfull: " + response.getString("data"));

                                binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                                binding.bidNow.setClickable(false);
                                Toast.makeText(ProductItemDetailsActivity.this, "" + response.getString("data"), Toast.LENGTH_SHORT).show();

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("bidding", "onResponse: response Failed: " + response.getString("data"));
                                Toast.makeText(ProductItemDetailsActivity.this, "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("bidding", "onResponse: Something went wrong here");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("bidding", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void getProductItem() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.PRODUCT_API_URL + "/" + trendItemId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getProductItem", "onResponse: response Sucess: " + response.getString("data"));


                        JSONArray productJsonArray = response.getJSONArray("data");


                        binding.frameLayout.setVisibility(View.GONE);

                        JSONObject productJsonObject = productJsonArray.getJSONObject(0);


                        String image = productJsonObject.getString("images");
                        ArrayList<String> imageList = new ArrayList<>(Arrays.asList(image.split(",")));

                        Log.d("getProductItemImage", "onResponse: imageList: " + imageList.get(0));

                        pricePerSpot = (int) Float.parseFloat(productJsonObject.getString("price_per_spot"));

                        binding.itemName.setText(productJsonObject.getString("name"));
                        binding.pricePerSpot.setText(pricePerSpot + "/spot");
                        binding.mrp2.setText(productJsonObject.getString("mrp"));
                        binding.spotLeftText.setText(productJsonObject.getString("empty_spot"));
                        binding.progressBar.setMax(Integer.parseInt(productJsonObject.getString("total_spot")));
                        binding.progressBar.setProgress(Integer.parseInt(productJsonObject.getString("empty_spot")));
                        binding.totalSpotText.setText("out of " + productJsonObject.getString("total_spot"));
                        binding.shortDescription.setText(productJsonObject.getString("short_desc"));
                        binding.longDetails.setText(productJsonObject.getString("details"));

                        binding.imageViewpager.setAdapter(new ProductItemImageAdapter(imageList, ProductItemDetailsActivity.this));

                        binding.dot.attachTo(binding.imageViewpager);

                        int spotFull = productJsonObject.getInt("full_status");

//                        checking for first bidding or bidding again

                        if (spotFull == 1){
                            //      bidnow disable..
                            binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                            binding.bidNow.setClickable(false);
                            binding.bidNow.setEnabled(false);
                            binding.bidNow.setText("No Available Spot");

                        }else {
                            checkEligibleBidding();

                            /*binding.bidNow.setVisibility(View.VISIBLE);
                            binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                            binding.bidNow.setClickable(true);
                            binding.bidNow.setEnabled(true);
                            binding.bidNow.setText("Buy Now");*/
                        }



                            /*if (productJsonObject.getInt("full_status") == 0)
                                productModal.setSpotFull(false);
                            else
                                productModal.setSpotFull(true);*/


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getProductItem", "onResponse: response Failed: " + response.getString("data"));
                        binding.message.setText("Item not found or might be blocked.");
                        binding.message.setVisibility(View.VISIBLE);
                        binding.errorImg.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);
                    } else {
                        Log.d("getProductItem", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getProductItem", "onResponse: error Response " + error.getMessage());
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

    private void checkEligibleBidding() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.CHECK_BID_API + "/" + trendItemId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("checkEligibleBidding", "onResponse: response Sucess: " + response.getString("data"));

//                      First bidding...........
                        binding.bidOver.setVisibility(View.GONE);

                        binding.bidNow.setVisibility(View.VISIBLE);
                        binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                        binding.bidNow.setClickable(true);
                        binding.bidNow.setEnabled(true);
                        binding.bidNow.setText("Buy Now");

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("checkEligibleBidding", "onResponse: response Failed: " + response.getString("data"));

//                        Winner Announced or Product not found..........
                        binding.bidOver.setVisibility(View.VISIBLE);
                        binding.bidOver.setText(response.getString("data"));

                        binding.bidNow.setVisibility(View.GONE);
                        binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                        binding.bidNow.setClickable(false);
                        binding.bidNow.setEnabled(false);
                        binding.bidNow.setText("Can't Buy");

                    } else if (response.getBoolean("status") && response.getInt("code") == 202) {
                        Log.d("checkEligibleBidding", "onResponse: response new: " + response.getString("data"));

//                        Bidding Again..........
                        binding.bidOver.setVisibility(View.GONE);

                        binding.bidNow.setVisibility(View.VISIBLE);
                        binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.TertiaryColor));
                        binding.bidNow.setClickable(true);
                        binding.bidNow.setEnabled(true);
                        binding.bidNow.setText("Buy Again");
                    } else {
                        Log.d("checkEligibleBidding", "onResponse: something went wrong");
                        binding.bidOver.setVisibility(View.VISIBLE);
                        binding.bidOver.setText("Something went wrong");

                        binding.bidNow.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonDisableColor));
                        binding.bidNow.setClickable(false);
                        binding.bidNow.setEnabled(false);
                        binding.bidNow.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkEligibleBidding", "onResponse: error Response " + error.getMessage());
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


}