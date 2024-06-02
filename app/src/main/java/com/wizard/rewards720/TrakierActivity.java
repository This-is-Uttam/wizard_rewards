package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.TRAKIER_CAMPAIGN_API;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.TrakierMainListAdapter;
import com.wizard.rewards720.Modals.TrakierMainListModal;
import com.wizard.rewards720.databinding.ActivityTrakierBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrakierActivity extends AppCompatActivity {
    ActivityTrakierBinding binding;
    ArrayList<TrakierMainListModal> trakierMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrakierBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchAllCampaigns();




    }

    private void fetchAllCampaigns() {
        binding.trakierProgress.setVisibility(View.VISIBLE);
        trakierMainList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, TRAKIER_CAMPAIGN_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")){

                        binding.trakierProgress.setVisibility(View.GONE);

                        JSONObject data = response.getJSONObject("data");


                        JSONArray campaigns = data.getJSONArray("campaigns");

                        for (int i = 0; i< campaigns.length(); i++){
                            int totalCoins = 0;
                            JSONObject campaignData = campaigns.getJSONObject(i);
                            int campaignId = campaignData.getInt("id");
                            try {


                                String campaignTitle = campaignData.getString("title");
                                String campaignDesc = campaignData.getString("description");
                                String campaignKpi = campaignData.getString("kpi");
                                String campaignIcon = campaignData.getString("thumbnail");



                                String campaignPosterImg = campaignData.getJSONArray("creatives")
                                        .getJSONObject(0)
                                        .getString("full_url");



                                JSONArray goals = campaignData.getJSONArray("goals");

                                for (int j= 0; j< goals.length(); j++){
                                    JSONObject singleGoal = goals.getJSONObject(j);

                                    int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");

                                    totalCoins = totalCoins + payout;

                                }



                                TrakierMainListModal trakierMainModal = new TrakierMainListModal(
                                        campaignPosterImg,campaignIcon,campaignTitle,campaignDesc,
                                        String.valueOf(totalCoins),""
                                );
                                trakierMainModal.setAdId(campaignId);

                                trakierMainList.add(trakierMainModal);
                            } catch (JSONException e) {

                                Log.d("fetchAllCampaigns", "onResponse: Something unexpected at Campaign Id: "+campaignId+" Message: "+ e.getMessage());

                            }

                        }

                        if (trakierMainList.size() == 0){
                            binding.trakierMainListRv.setVisibility(View.GONE);
                            binding.emptyCampaignTxt.setVisibility(View.VISIBLE);
                        }else {
                            binding.trakierMainListRv.setVisibility(View.VISIBLE);
                            binding.emptyCampaignTxt.setVisibility(View.GONE);

                            binding.trakierMainListRv.setLayoutManager(new LinearLayoutManager(TrakierActivity.this));
                            binding.trakierMainListRv.setAdapter(new TrakierMainListAdapter(trakierMainList,TrakierActivity.this));
                        }


                    }else {
                        Log.d("fetchAllCampaigns", "onResponse : Something went wrong: "
                                + response.getString("data"));
                    }
                } catch (JSONException e) {
                    Log.d("fetchAllCampaigns", "onResponse Failed : Json Exception: "+ e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchAllCampaigns", "onResponse Failed : VolleyError: "+ error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}