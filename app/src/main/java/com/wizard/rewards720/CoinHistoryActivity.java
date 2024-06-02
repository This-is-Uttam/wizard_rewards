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
import com.wizard.rewards720.Adapters.CoinHistoryAdapter;
import com.wizard.rewards720.Modals.HistoryModal;
import com.wizard.rewards720.databinding.ActivityCoinHistoryBinding;

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

public class CoinHistoryActivity extends AppCompatActivity {
    ActivityCoinHistoryBinding binding;
    ArrayList<HistoryModal> historyModals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoinHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.coinHistoryToolbar.customToolbar.setTitle("Coins History");
//        binding.coinHistoryToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black,getTheme()));
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

//      updating coins

        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        historyModals = new ArrayList<>();

        getCoinsHistoryList();

        /*coinHistoryModals.add(new CoinHistoryModal(
                "01 Jun 2024", "05:30 PM", "50500", true, "from Refferal"
        ));
        coinHistoryModals.add(new CoinHistoryModal(
                        "02 Jun 2024", "06:30 PM", "90", false, "from Bidding"
                ));
*/
        binding.coinHistoryRv.setAdapter(
                new CoinHistoryAdapter(historyModals, this)
        );

        binding.coinHistoryRv.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.coinHistoryRv.setNestedScrollingEnabled(false);

    }

    private void getCoinsHistoryList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.COINS_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){
                                binding.frameLayout.setVisibility(View.GONE);


                                Log.d("getCoinsHistoryList", "onResponse: response Sucessfull: "+ response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int hisCoins  = jsonObject.getInt("coins");
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
                                    String timeMain = hisTime;


                                    boolean isCredited;
                                    if (type.equals("DR")){
                                        isCredited = false;
                                    }else {
                                        isCredited = true;
                                    }

//                                    Date date = new Date(hisTime)
//                                    LocalDate localDate = LocalDate.parse(hisDate);
//
//                                    String sdf = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(localDate);

                                    HistoryModal historyModal = new HistoryModal(
                                      dateMain,timeMain,hisCoins+"",isCredited, hisMsg
                                    );
                                    historyModals.add(historyModal);


                                }

                                binding.coinHistoryRv.setAdapter(new CoinHistoryAdapter(historyModals,CoinHistoryActivity.this));
                                binding.coinHistoryRv.setLayoutManager(new LinearLayoutManager(CoinHistoryActivity.this));
                                binding.coinHistoryRv.setNestedScrollingEnabled(false);

                                ControlRoom.getInstance().setCoins(response.getInt("ava_coins")+"", CoinHistoryActivity.this);
                                binding.coinTxt.setText(ControlRoom.getInstance().getCoins(CoinHistoryActivity.this));

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getCoinsHistoryList", "onResponse: response Failed: "+ response.getString("data"));
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                String msg = response.getString("data");
                                if (msg.equals("No coin transaction found!.."))
                                    binding.message.setText("No Coins history found.");
                                else
                                    binding.message.setText(response.getString(msg));

                            }else {
                                Log.d("getCoinsHistoryList", "onResponse: Something went wrong ");
                                binding.frameLayout.setVisibility(View.VISIBLE);
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText("Something went wrong!");
                            }
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getCoinsHistoryList", "onResponse: error response: "+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(CoinHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }
}