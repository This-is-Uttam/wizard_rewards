package com.wizard.rewards720.Fragments;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.VOUCHER_WINNERS_GET_API;
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
import com.wizard.rewards720.Adapters.VoucherAllWinnersAdapter;
import com.wizard.rewards720.Modals.VoucherWinModal;
import com.wizard.rewards720.databinding.FragmentVoucherWinBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class VoucherWinFragment extends Fragment {
    FragmentVoucherWinBinding binding;

    ArrayList<VoucherWinModal> voucherWinList;
    ValueAnimator valueAnimator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVoucherWinBinding.inflate(inflater, container, false);
        // do you code///
        getVoucherWinnersList();



        return binding.getRoot();
    }

    private void getVoucherWinnersList() {
        binding.progressBarVoucFrag.setVisibility(View.VISIBLE);
        voucherWinList = new ArrayList<>();

        // get list from server.....
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_WINNERS_GET_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        binding.progressBarVoucFrag.setVisibility(View.GONE);
                        Log.d("getVoucherWinnersList", "onResponse: Sucessfull...:" + response.getString("data"));

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            VoucherWinModal voucherWinModal = new VoucherWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("voucherName"),
                                    jsonObject.getString("voucherImage"),
                                    jsonObject.getString("mrp")

                            );
                            voucherWinModal.setVoucherWinnerCount(i+1);

                            voucherWinList.add(voucherWinModal);

                        }
                        startValueAnimation(voucherWinList.size());


//                        reverse list to descending order
                        Collections.reverse(voucherWinList);

                        binding.vouchersListRv.setAdapter(new VoucherAllWinnersAdapter(voucherWinList,getContext(),true));
                        binding.vouchersListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.vouchersListRv.setNestedScrollingEnabled(false);


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201){

                        Log.d("getVoucherWinnersList", "onResponse: Failed..." + response.getString("data"));
                    }else {
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
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }

    private void startValueAnimation(int size) {

        valueAnimator = ValueAnimator.ofInt(0, voucherWinList.size());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                binding.voucWinCount.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }

}