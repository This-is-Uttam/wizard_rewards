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
import com.wizard.rewards720.databinding.ActivityReferralBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferralActivity extends AppCompatActivity {
    ActivityReferralBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.submitButton.setClickable(true);

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ReferralActivity.this, "Referral Code Submitted...", Toast.LENGTH_SHORT).show();
//                Check referral code validity...
                binding.submitButton.setClickable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                String referCode = binding.referInputLayout.getEditText().getText().toString();
                if (referCode.equals("")){
                    binding.referInputLayout.setError("Please enter code first...");
                }else {

                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    sendReferCodeToServer(referCode);
                }

                
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReferralActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void sendReferCodeToServer(String referCode) {
        binding.referProgress.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.REFERRAL_CODE, referCode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.CHECK_REFERRAL_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){

                            Log.d("sendReferCodeToServer", "onResponse: response Sucessfull: "+ response.getString("data"));
                                startActivity(new Intent(ReferralActivity.this, MainActivity.class));
                                binding.referProgress.setVisibility(View.GONE);
                                finish();
                            }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                                Log.d("sendReferCodeToServer", "onResponse: response Failed: "+ response.getString("data"));
                                binding.referProgress.setVisibility(View.GONE);
                                Toast.makeText(ReferralActivity.this, ""+ response.getString("data"), Toast.LENGTH_SHORT).show();
                                binding.submitButton.setClickable(true);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendReferCodeToServer", "onResponse: error response: ");
                binding.referProgress.setVisibility(View.GONE);
                Toast.makeText(ReferralActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                binding.submitButton.setClickable(true);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE,Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER+ accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}