package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.BiddingHistoryAdapter;
import com.wizard.rewards720.Modals.BiddingHistoryModal;
import com.wizard.rewards720.databinding.ActivityBiddingHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BiddingHistoryActivity extends AppCompatActivity {
    ActivityBiddingHistoryBinding binding;
    ArrayList<BiddingHistoryModal> biddingHistoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBiddingHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.bidHisToolbar.customToolbar.setTitle("Product Shopping History");
//        binding.bidHisToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black, getTheme()));
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

        biddingHistoryList = new ArrayList<>();

        getBiddinghistoryList();
        binding.swipeRefreshBidHis.setRefreshing(true);

        binding.swipeRefreshBidHis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBiddinghistoryList();
            }
        });

    }

    private void getBiddinghistoryList() {
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        biddingHistoryList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.BIDDING_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                binding.frameLayout.setVisibility(View.GONE);
                                binding.swipeRefreshBidHis.setRefreshing(false);

                                Log.d("getBiddinghistoryList", "onResponse: response Sucess: " + response.getString("data"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    BiddingHistoryModal bidHisModal = new BiddingHistoryModal();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                imgList from imageString
                                    String imageStr = jsonObject.getString("images");
                                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(imageStr.split(",")));

                                    String name = jsonObject.getString("name");


//                                date and time

                                    String dateTime = jsonObject.getString("bidding_date_time");
                                    Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                                            .parse(dateTime);

                                    String mainDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                                    String mainTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
                                    Log.d("getBiddinghistoryList", "onResponse: date format " + mainDate + " time " + mainTime);

                                    String bidCoin = jsonObject.getString("bidi_coin");
                                    String bidDiamond = jsonObject.getString("bid_amt"); // bid_amt is Diamond //

//                                    winner decision....
                                    int iswinner = jsonObject.getInt("is_winner");
                                    int winnDiamond = jsonObject.getInt("winning_daimond");
                                    String winningStatus;
                                    if (iswinner == 0 && winnDiamond > 0) {
                                        winningStatus = "You have won " + winnDiamond + " Diamonds";
                                        bidHisModal.setBidWinningStatus(winningStatus);
                                        bidHisModal.setWinner(2);
                                    } else if (iswinner != 0 && winnDiamond == 0) {
                                        winningStatus = "You are Winner of this Shopping";
                                        bidHisModal.setBidWinningStatus(winningStatus);
                                        bidHisModal.setWinner(1);
                                    } else {
                                        winningStatus = "Winner is not announced yet";
                                        bidHisModal.setBidWinningStatus(winningStatus);
                                        bidHisModal.setWinner(0);
                                    }

                                    bidHisModal.setBidProductImage(imgList.get(0));
                                    bidHisModal.setBidProductName(name);
                                    bidHisModal.setBidCoin(bidCoin);
                                    bidHisModal.setBidDiamond(bidDiamond);
                                    bidHisModal.setBidDate(mainDate);
                                    bidHisModal.setBidTime(mainTime);
                                    bidHisModal.setWinningDiamond(winnDiamond + "");
                                    bidHisModal.setBidId(jsonObject.getInt("bid_id"));
                                    bidHisModal.setWinnerAddress(jsonObject.getString("winner_address"));

                                    Log.d("getBiddinghistoryList", "onResponse: bidid: " + bidHisModal.getBidId());

                                    biddingHistoryList.add(bidHisModal);

                                }
                                binding.biddingHistoryRv.setAdapter(new BiddingHistoryAdapter(biddingHistoryList, BiddingHistoryActivity.this));
                                binding.biddingHistoryRv.setLayoutManager(new LinearLayoutManager(BiddingHistoryActivity.this));
                                binding.biddingHistoryRv.setNestedScrollingEnabled(false);

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getBiddinghistoryList", "onResponse: response Failed: " + response.getString("data"));
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.INVISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText(response.getString("data"));
                            } else {
                                Log.d("getBiddinghistoryList", "onResponse: something went wrong");

                            }
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getBiddinghistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
