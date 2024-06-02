package com.wizard.rewards720;

import static com.wizard.rewards720.LoginActivity.accessToken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.databinding.ActivityClaimUserDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ClaimUserDetailActivity extends AppCompatActivity {
    ActivityClaimUserDetailBinding binding;
    int bid_id;
    String fullAdd, pincode, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClaimUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.claimUDetailToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black,getTheme()));
        binding.claimUDetailToolbar.customToolbar.setTitle("Claim Details");
        binding.claimUDetailProgress.setVisibility(View.GONE);

        bid_id = getIntent().getIntExtra("BID_ID",0);

        binding.submitClaimDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullAdd = binding.addInputLayout.getEditText().getText().toString();
                phone = binding.phoneInputLayout.getEditText().getText().toString();

                pincode = binding.pinCodeInputLayout.getEditText().getText().toString();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.RESULT_HIDDEN);

                checkValidation();
            }
        });

    }

    private void checkValidation() {
        if (fullAdd.equals("")){
            binding.addInputLayout.setError("This field can't be empty.");
        }else {
            if (pincode.equals("")){
                binding.pinCodeInputLayout.setError("This field can't be empty.");
            }else {
                if (phone.equals("")) {
                    binding.phoneInputLayout.setError("This field is required.");
                } else {
                    if (!phone.matches("[6789][0-9]{9}")) {
                        binding.phoneInputLayout.setError("Please Enter Valid Phone Number.");
                    } else {
                        if (!pincode.matches("[0-9]{6}")){
                            binding.pinCodeInputLayout.setError("Please enter valid pin code.");
                        }else {
                            String winnerAddress = ControlRoom.getInstance().getFullName(ClaimUserDetailActivity.this)+"\n"+fullAdd + "\n"+ phone+ "\n"+ pincode;
                            submitClaimDetails(winnerAddress);
                            binding.claimUDetailProgress.setVisibility(View.VISIBLE);
                        }
                    }
                }


            }
        }
    }

    private void submitClaimDetails(String winnerAddress) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", bid_id);
            jsonObject.put("winner_address", winnerAddress);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.WINNER_ADDRESS_UPDATE_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("submitClaimDetails", "onResponse: response is Successful " + response.getString("data"));
                        binding.claimUDetailProgress.setVisibility(View.GONE);

                        Toast.makeText(ClaimUserDetailActivity.this, "Details Submitted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(ClaimUserDetailActivity.this, BiddingHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("submitClaimDetails", "onResponse: response is Failed " + response.getString("data"));
                        binding.claimUDetailProgress.setVisibility(View.GONE);
                        Toast.makeText(ClaimUserDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("submitClaimDetails", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("submitClaimDetails", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(ClaimUserDetailActivity.this));
                return header;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}