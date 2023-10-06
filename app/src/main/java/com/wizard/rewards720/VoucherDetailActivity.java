package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.VOUCHER_MAIN_URL;
import static com.wizard.rewards720.LoginActivity.accessToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.wizard.rewards720.Adapters.VoucherDetailAdapter;
import com.wizard.rewards720.Adapters.VoucherMainAdapter;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.databinding.ActivityVoucherDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VoucherDetailActivity extends AppCompatActivity {
    ActivityVoucherDetailBinding binding;
    Toolbar voucherDetailToolbar;
    ArrayList<VoucherMainModal> voucherMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        voucherDetailToolbar = findViewById(R.id.voucherDetailToolbar);
        voucherDetailToolbar.setTitle("Vouchers");

        binding.progressBar5.setVisibility(View.VISIBLE);
        binding.message2.setVisibility(View.GONE);

        getVoucherMainList();

    }

    private void getVoucherMainList() {
        binding.progressBar5.setVisibility(View.VISIBLE);
        voucherMainList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){
                                Log.d("getVoucherMainList", "onResponse: response Sucessfull: "+ response.getString("data"));
                                binding.progressBar5.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String mrp = jsonObject.getString("mrp");
                                    String price_per_spot = jsonObject.getString("price_per_spot");
                                    String total_spot = jsonObject.getString("total_spot");
                                    String empty_spot = jsonObject.getString("empty_spot");
                                    String winnig_code = jsonObject.getString("winnig_code");
                                    String winnig_daimonds = jsonObject.getString("winnig_daimonds");
                                    String short_desc = jsonObject.getString("short_desc");
                                    String details = jsonObject.getString("details");
                                    String images = jsonObject.getString("images");
                                    String full_status = jsonObject.getString("full_status");
                                    boolean full_status_bool;
                                    if (Integer.parseInt(full_status)==0){
                                        full_status_bool = false;
                                    }else {
                                        full_status_bool= true;
                                    }

                                    Log.d("getVoucherMainList", "onResponse: Value "+id);

                                    VoucherMainModal voucherMainModal = new VoucherMainModal(
                                            "2",empty_spot,total_spot,name,price_per_spot,short_desc,details,id,images,
                                            full_status_bool,winnig_code);

                                    voucherMainModal.setMrp(jsonObject.getString("mrp"));

                                    voucherMainList.add(voucherMainModal);
                                }

//                        checking voucher winners list empty.
                                if (voucherMainList.size() == 0) {
                                    binding.voucherDetailRv.setVisibility(View.GONE);
                                    binding.emptyTxtVoucher.setVisibility(View.VISIBLE);
                                } else {
                                    binding.voucherDetailRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtVoucher.setVisibility(View.GONE);

                                    VoucherDetailAdapter voucherDetailAdapter  = new VoucherDetailAdapter(voucherMainList, VoucherDetailActivity.this);
                                    LinearLayoutManager layoutManager = new GridLayoutManager(VoucherDetailActivity.this,2);
                                    binding.voucherDetailRv.setAdapter(voucherDetailAdapter);
                                    binding.voucherDetailRv.setLayoutManager(layoutManager);
                                    binding.voucherDetailRv.setNestedScrollingEnabled(false);
                                }



                            }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                                Log.d("getVoucherMainList", "onResponse: response Failed: "+ response.getString("data"));
                                binding.progressBar5.setVisibility(View.GONE);
                                binding.message2.setVisibility(View.VISIBLE);
                                binding.message2.setText(response.getString("data"));
                            }else {
                                Log.d("getVoucherMainList", "onResponse: Something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherMainList", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(VoucherDetailActivity.this).add(jsonObjectRequest);

    }

}