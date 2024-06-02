package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.TRAKIER_CAMPAIGN_API;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.wizard.rewards720.Adapters.TrakierMainListAdapter;
import com.wizard.rewards720.Adapters.TrakierTasksAdapter;
import com.wizard.rewards720.Modals.TrakierMainListModal;
import com.wizard.rewards720.Modals.TrakierTasksModal;
import com.wizard.rewards720.databinding.ActivityTrakierCampaignDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrakierCampaignDetailActivity extends AppCompatActivity {
    ActivityTrakierCampaignDetailBinding binding;
    ArrayList<TrakierTasksModal> trakierTasksModalList;
    TrakierMainListModal trakierMainModal;
    public static final String CAMPAIGN_ID = "campaignId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrakierCampaignDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int campaignUid = intent.getIntExtra(CAMPAIGN_ID,0);
        Log.d("testTcda", "onResponse: TCDA: campaign Uid: "+campaignUid);
        getCampaignDetails(campaignUid);


    }


    private void getCampaignDetails(int campaignUid) {
        trakierTasksModalList = new ArrayList<>();


        binding.campaignDetailProgressBar.setVisibility(View.VISIBLE);
        binding.campaignDetailLayout.setVisibility(View.GONE);
        binding.emptyCampaignDetailTxt.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, TRAKIER_CAMPAIGN_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")){
                        binding.campaignDetailProgressBar.setVisibility(View.GONE);
                        binding.campaignDetailLayout.setVisibility(View.VISIBLE);
                        binding.emptyCampaignDetailTxt.setVisibility(View.GONE);

                        JSONObject data = response.getJSONObject("data");

                        JSONArray campaigns = data.getJSONArray("campaigns");



                        for (int i = 0; i< campaigns.length(); i++){
                            int totalCoins = 0;
                            JSONObject campaignData = campaigns.getJSONObject(i);
                            int campaignId = campaignData.getInt("id");
                            Log.d("testTcda", "onResponse: TCDA: campaign id: "+campaignId+"campaign Uid: "+campaignUid);
                            if (campaignId == campaignUid){

                                try {


                                    String campaignTitle = campaignData.getString("title");
                                    String campaignDesc = campaignData.getString("description");
                                    String campaignKpi = campaignData.getString("kpi");
                                    String campaignIcon = campaignData.getString("thumbnail");



                                    String campaignPosterImg = campaignData.getJSONArray("creatives")
                                            .getJSONObject(0)
                                            .getString("full_url");



                                    JSONArray goals = campaignData.getJSONArray("goals");
                                    int totalTasks = goals.length();

                                    for (int j= 0; j< goals.length(); j++){
                                        JSONObject singleGoal = goals.getJSONObject(j);

                                        int payout = singleGoal.getJSONArray("payouts").getJSONObject(0).getInt("payout");
                                        totalCoins = totalCoins + payout;

                                        String taskTitle = singleGoal.getString("title");
                                        TrakierTasksModal tasksModal = new TrakierTasksModal(String.valueOf(j+1),taskTitle,false);

                                        trakierTasksModalList.add(tasksModal);
                                    }

                                     trakierMainModal = new TrakierMainListModal(
                                            campaignPosterImg,campaignIcon,campaignTitle,campaignDesc,
                                            String.valueOf(totalCoins),"");
                                    trakierMainModal.setTotalTasks(totalTasks);

                                    setCampaignData(trakierMainModal, trakierTasksModalList);



                                } catch (JSONException e) {

                                    Log.d("getCampaignDetailss", "onResponse: Something unexpected at Campaign Id: "+campaignId+" Message: "+ e.getMessage());

                                }

                            }else if (i == (campaigns.length()-1) && campaignId != campaignUid){
                                binding.campaignDetailLayout.setVisibility(View.GONE);
                                binding.campaignDetailProgressBar.setVisibility(View.GONE);
                                binding.emptyCampaignDetailTxt.setVisibility(View.VISIBLE);
                            }


                        }




                    }else {
                        Log.d("getCampaignDetailss", "onResponse : Something went wrong: "
                                + response.getString("data"));
                    }
                } catch (JSONException e) {
                    Log.d("getCampaignDetailss", "onResponse Failed : Json Exception: "+ e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getCampaignDetailss", "onResponse Failed : VolleyError: "+ error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }

    private void setCampaignData(TrakierMainListModal trakierMainModal, ArrayList<TrakierTasksModal> trakierTasksModalList) {

        binding.adTitle.setText(trakierMainModal.getAdTitle());
        binding.adDesc.setText(trakierMainModal.getAdDesc());
        binding.adRewardCoin.setText(trakierMainModal.getAdRewardCoin());
        Picasso.get()
                .load(trakierMainModal.getAdIcon())
                .placeholder(R.drawable.placeholder)
                .into(binding.adIcon);
        Picasso.get()
                .load(trakierMainModal.getAdPosterImg())
                .placeholder(R.drawable.placeholder)
                .into(binding.adPosterImg);
        binding.tasksCount.setText("0/"+trakierMainModal.getTotalTasks());

        binding.trakierTaskRv.setAdapter(new TrakierTasksAdapter(trakierTasksModalList,this));
        binding.trakierTaskRv.setLayoutManager(new LinearLayoutManager(this));

    }
}