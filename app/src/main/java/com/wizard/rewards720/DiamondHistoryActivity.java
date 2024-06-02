package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.DiamondHistoryAdapter;
import com.wizard.rewards720.Modals.HistoryModal;
import com.wizard.rewards720.databinding.ActivityDiamondHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DiamondHistoryActivity extends AppCompatActivity {
    ActivityDiamondHistoryBinding binding;

    ArrayList<HistoryModal> diamondHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiamondHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.diamondHistoryToolbar.customToolbar.setTitle("Diamonds History");
//        binding.diamondHistoryToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black,getTheme()));
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

        /*binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);*/

        diamondHistoryList = new ArrayList<>();


        getDiamondHistoryList();

        /*diamondHistoryList.add(new HistoryModal("25 Jun 2023","03:00 PM","25",
                "Debited for bidding Noise Pluse Smartwatch",true));

        binding.diamondHistoryRv.setAdapter(new DiamondHistoryAdapter(diamondHistoryList, this));
        binding.diamondHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        binding.diamondHistoryRv.setNestedScrollingEnabled(false);*/
    }

    private void getDiamondHistoryList() {

        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DIAMOND_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.frameLayout.setVisibility(View.GONE);


                                Log.d("getDiamondHistoryList", "onResponse: response Sucessfull: " + response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int hisDiamonds = jsonObject.getInt("daimonds");
                                    String hisMsg = jsonObject.getString("tran_type");
                                    String type = jsonObject.getString("type");
                                    String hisDate = jsonObject.getString("tran_date");
                                    String hisTime = jsonObject.getString("tran_time");


//                                    date format
                                    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(hisDate);
                                    String dateMain = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);

//                                    time format
//                                    Date time = new SimpleDateFormat("hh:mm:ssa", Locale.getDefault()).parse(hisTime);
//                                    String timeMain = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(time);
                                    String timeMain= hisTime;

                                    boolean isCredited;
                                    if (type.equals("DR")) {
                                        isCredited = false;
                                    } else {
                                        isCredited = true;
                                    }

//                                    Date date = new Date(hisTime)
//                                    LocalDate localDate = LocalDate.parse(hisDate);
//
//                                    String sdf = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(localDate);

                                    HistoryModal historyModal = new HistoryModal(
                                            dateMain, timeMain, hisDiamonds + "", isCredited, hisMsg
                                    );
                                    diamondHistoryList.add(historyModal);


                                }

                                binding.diamondHistoryRv.setAdapter(new DiamondHistoryAdapter(diamondHistoryList, DiamondHistoryActivity.this));
                                binding.diamondHistoryRv.setLayoutManager(new LinearLayoutManager(DiamondHistoryActivity.this));
                                binding.diamondHistoryRv.setNestedScrollingEnabled(false);

//                                ControlRoom.getInstance().setCoins(response.getInt("ava_coins")+"");
                                binding.diamondTv.setText(ControlRoom.getInstance().getDiamonds(DiamondHistoryActivity.this));

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getDiamondHistoryList", "onResponse: response Failed: " + response.getString("data"));
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                String msg = response.getString("data");
                                if (msg.equals("No coin transaction found!.."))
                                    binding.message.setText("No Diamonds history found.");
                                else
                                    binding.message.setText(response.getString(msg));


                            } else {
                                Log.d("getDiamondHistoryList", "onResponse: Something went wrong ");
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText("Something went wrong!");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getDiamondHistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(DiamondHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }
}