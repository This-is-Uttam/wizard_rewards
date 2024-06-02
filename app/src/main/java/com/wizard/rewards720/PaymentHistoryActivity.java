package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.PHONEPE_INITIATE_PAY_API;
import static com.wizard.rewards720.LoginActivity.accessToken;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.BiddingHistoryAdapter;
import com.wizard.rewards720.Adapters.PaymentHistoryAdapter;
import com.wizard.rewards720.Adapters.TrendingAdapter;
import com.wizard.rewards720.Modals.BiddingHistoryModal;
import com.wizard.rewards720.Modals.PaymentHistoryModal;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.databinding.ActivityPaymentHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentHistoryActivity extends AppCompatActivity {
    ActivityPaymentHistoryBinding binding;
    ArrayList<PaymentHistoryModal> paymentHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.payHisToolbar.customToolbar.setTitle("Payment History");

        getPaymentHistoryList();

        binding.payHisSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPaymentHistoryList();
            }
        });

    }

    private void getPaymentHistoryList() {
        paymentHistoryList = new ArrayList<>();
        binding.payHistprogressBar.setVisibility(View.VISIBLE);
        binding.payHisEmptyTxt.setVisibility(View.GONE);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.PAYMENT_HISTORY_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.payHistprogressBar.setVisibility(View.GONE);
                                binding.payHisEmptyTxt.setVisibility(View.GONE);
                                binding.payHisSwipeRefresh.setRefreshing(false);

                                Log.d("getPaymentHistoryList", "onResponse: response Sucess: " + response.getString("data"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PaymentHistoryModal paymentHistoryModal = new PaymentHistoryModal();

                                    String purchaseAmt = jsonObject.getString("amt");
                                    if (!purchaseAmt.equals("null"))
                                        paymentHistoryModal.setPurchaseAmt(purchaseAmt);
                                    else {
                                        paymentHistoryModal.setPurchaseAmt("0");
                                    }

                                    String purchaseCoin = jsonObject.getString("coins");
                                    if (!purchaseCoin.equals("null"))
                                        paymentHistoryModal.setPurchaseCoin(purchaseCoin);
                                    else {
                                        paymentHistoryModal.setPurchaseCoin("0");
                                    }

                                    // date and time

                                    String dateTime = jsonObject.getString("created_at");
                                    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                                            .parse(dateTime);

                                    assert date != null;
                                    String mainDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                                    String mainTime = new SimpleDateFormat("hh:mma", Locale.getDefault()).format(date);
                                    //2023-10-14T06:48:36.000000Z
                                    //yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ
                                    String purchaseDateNtime = mainDate;
                                    if (!purchaseDateNtime.equals("null"))
                                        paymentHistoryModal.setPurchaseDateTime(purchaseDateNtime);
                                    else {
                                        paymentHistoryModal.setPurchaseDateTime("No data");
                                    }

                                    String purchaseTxnId = jsonObject.getString("trnx_id");
                                    if (!purchaseTxnId.equals("null"))
                                        paymentHistoryModal.setPurchaseTxnId(purchaseTxnId);
                                    else {
                                        paymentHistoryModal.setPurchaseTxnId("No data");
                                    }
                                    // Payment Status..........

                                    int purchaseStatus = jsonObject.getInt("pay_status");
                                    paymentHistoryModal.setPurchaseStatus(purchaseStatus);

                                    paymentHistoryList.add(paymentHistoryModal);

                                }
                                // checking product list empty.
                                if (paymentHistoryList.size() == 0) {
                                    binding.payHistRv.setVisibility(View.GONE);
                                    binding.payHisEmptyTxt.setVisibility(View.VISIBLE);
                                } else {
                                    binding.payHistRv.setVisibility(View.VISIBLE);
                                    binding.payHisEmptyTxt.setVisibility(View.GONE);

                                    Collections.reverse(paymentHistoryList);
                                    binding.payHistRv.setAdapter(new PaymentHistoryAdapter(paymentHistoryList, PaymentHistoryActivity.this));
                                    binding.payHistRv.setLayoutManager(new LinearLayoutManager(PaymentHistoryActivity.this));
                                    binding.payHistRv.setNestedScrollingEnabled(false);
                                }



                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                String respons = response.getString("data");
                                Log.d("getPaymentHistoryList", "onResponse: response Failed: " + respons);
                                binding.payHistprogressBar.setVisibility(View.GONE);
                                binding.payHisEmptyTxt.setVisibility(View.VISIBLE);
                                binding.payHisEmptyTxt.setText(respons);
                                binding.payHisSwipeRefresh.setRefreshing(false);
                            } else {
                                Log.d("getPaymentHistoryList", "onResponse: something went wrong");
                                Toast.makeText(PaymentHistoryActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                binding.payHisSwipeRefresh.setRefreshing(false);
                            }
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getPaymentHistoryList", "onResponse: error response: " + error.getMessage());
                binding.payHistprogressBar.setVisibility(View.GONE);
                binding.payHisEmptyTxt.setVisibility(View.VISIBLE);
                binding.payHisEmptyTxt.setText("Something went wrong.");
                Toast.makeText(PaymentHistoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                binding.payHisSwipeRefresh.setRefreshing(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(PaymentHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}