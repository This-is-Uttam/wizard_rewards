package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.TrendingAdapter;
import com.wizard.rewards720.Adapters.TrendingDetailAdapter;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.databinding.ActivityTrendingDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TrendingDetailActivity extends AppCompatActivity {

    ArrayList<TrendingModal> trendingItemList;
    ActivityTrendingDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrendingDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue,getTheme()));

        binding.trendingDetailToolbar.setTitle("Product Details");
//        binding.trendingDetailToolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(binding.trendingDetailToolbar);

        binding.progressBar4.setVisibility(View.VISIBLE);
        binding.message.setVisibility(View.GONE);

        trendingItemList = new ArrayList<>();
        getAllProducts();



    }

    private void getAllProducts() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.PRODUCT_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")&& response.getInt("code")==200){
                        Log.d("getAllProducts", "onResponse: response Sucess: " + response.getString("data"));
                        binding.progressBar4.setVisibility(View.GONE);
                        JSONArray productJsonArray = response.getJSONArray("data");
                        for (int i = 0; i<productJsonArray.length(); i++){
                            JSONObject productJsonObject = productJsonArray.getJSONObject(i);

                            String image = productJsonObject.getString("images");
                            ArrayList<String> imageList = new ArrayList<>(Arrays.asList(image.split(",")));
                            Log.d("getAllProductsImage", "onResponse: imageList: "+ imageList.get(0));
                            TrendingModal productModal  = new TrendingModal("2",
                                    productJsonObject.getString("empty_spot"),
                                    productJsonObject.getString("name"),
                                    productJsonObject.getString("price_per_spot"),
                                    imageList.get(0),
                                    productJsonObject.getString("mrp"),
                                    productJsonObject.getString("total_spot"));

                            productModal.setShortDescription(productJsonObject.getString("short_desc"));
                            productModal.setDetails(productJsonObject.getString("details"));
                            if (productJsonObject.getInt("full_status") == 0)
                                productModal.setSpotFull(false);
                            else
                                productModal.setSpotFull(true);

                            productModal.setTrendItemId(productJsonObject.getString("id"));

                            trendingItemList.add(productModal);
                        }

 //                        checking product list empty.
                        if (trendingItemList.size() == 0) {
                            binding.trendingDetailRv.setVisibility(View.GONE);
                            binding.emptyTxtProduct.setVisibility(View.VISIBLE);
                        } else {
                            binding.trendingDetailRv.setVisibility(View.VISIBLE);
                            binding.emptyTxtProduct.setVisibility(View.GONE);

//
                            binding.trendingDetailRv.setAdapter(new TrendingDetailAdapter(trendingItemList,TrendingDetailActivity.this));
                            binding.trendingDetailRv.setLayoutManager(new LinearLayoutManager(TrendingDetailActivity.this));
                            binding.trendingDetailRv.setNestedScrollingEnabled(false);
                        }


                    }


                    else if (!response.getBoolean("status")&& response.getInt("code")==201) {
                        Log.d("getAllProducts", "onResponse: response Failed: " + response.getString("data"));
                        binding.progressBar4.setVisibility(View.GONE);
                        binding.message.setVisibility(View.VISIBLE);
                        binding.message.setText(response.getString("data"));
                    }else {
                        Log.d("getAllProducts", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAllProducts", "onResponse: error Response " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(TrendingDetailActivity.this));
                return header;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}