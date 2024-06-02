package com.wizard.rewards720.Fragments;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.PRODUCT_WINNERS_GET_API;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.ProductAllWinnersAdapter;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.Modals.ProductWinModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.FragmentProductWinBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ProductWinFragment extends Fragment {
    FragmentProductWinBinding binding;
    ArrayList<ProductWinModal> productWinnerList;
    ValueAnimator valueAnimator;

    public ProductWinFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductWinBinding.inflate(inflater, container, false);
        // do your code.....

        getProductWinnerList();

//        binding.prodWinAnim.setAnimation(R.raw.animation_2);
//        binding.prodWinAnim.playAnimation();




        return binding.getRoot();
    }

    private void getProductWinnerList() {
        binding.progressBarFragProd.setVisibility(View.VISIBLE);
        productWinnerList = new ArrayList<>();

        // get list from server.....
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PRODUCT_WINNERS_GET_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        binding.progressBarFragProd.setVisibility(View.GONE);
                        Log.d("getProductWinnerList", "onResponse: Sucessfull...:" + response.getString("data"));

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String imageList = jsonObject.getString("productImages");
                            String[] imgs = imageList.split(",");
                            String firstImg = imgs[0];

                            ProductWinModal productWinModal = new ProductWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("productName"),
                                    firstImg,
                                    jsonObject.getInt("mrp")+""

                            );
                            productWinModal.setProductWinnerCount(i+1);

                            // Winning monnth
                            int monthNum = jsonObject.getInt("winning_month");
                            String month = "";

                            switch (monthNum){
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
                            productWinModal.setWinMonth(month);

                            productWinnerList.add(productWinModal);

                        }
                        startValueAnimation(productWinnerList.size());

                        // Empty List Handle

                        if (productWinnerList.size() == 0) {
                            binding.winnersListRv.setVisibility(View.GONE);
                            binding.emptyTxtWinner.setVisibility(View.VISIBLE);
                        } else {
//                            reverse list to descending order
                            Collections.reverse(productWinnerList);

                            binding.winnersListRv.setAdapter(new ProductAllWinnersAdapter(productWinnerList,getContext(), true));
                            binding.winnersListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.winnersListRv.setNestedScrollingEnabled(false);
                        }


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201){

                        Log.d("getProductWinnerList", "onResponse: Failed..." + response.getString("data"));
                    }else {
                        Log.d("getProductWinnerList", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getProductWinnerList", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }

    private void startValueAnimation(int size) {

        valueAnimator = ValueAnimator.ofInt(0, productWinnerList.size());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                binding.prodWinCount.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }
}