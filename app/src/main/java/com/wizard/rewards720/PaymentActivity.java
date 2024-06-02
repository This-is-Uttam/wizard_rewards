package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.PHONEPE_INITIATE_PAY_API;
import static com.wizard.rewards720.Constants.PHONEPE_CALLBACK_URL;
import static com.wizard.rewards720.LoginActivity.accessToken;
import static com.wizard.rewards720.UpiAppsActivity.UPI_APP_PACKAGE_INTENT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;
import com.wizard.rewards720.databinding.ActivityPaymentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    List<UPIApplicationInfo> upiAppsList;
    String apiEndPoint = "/pg/v1/pay";
    final String merchantId = "M1QKZY1HYEHN"; //Production MID
    float coins;
    int coinPrice;
    private String packageName;
    private String appName;
    public static final String PHONEPE_PAYLOAD_PHONE = "7004357686";
    public static String merchanTxnId;
    public static final String SALT_KEY = "368a7412-2311-4cba-a3e4-942ec2e1c738";
    public static final String SALT_INDEX = "1";
    public static final String APP_ID_PHONEPE = "1e2b3f076a2c44608a778713f9d45b0a"; //"85605410435e4e79a8d730b94bcbfe3f";

//    Sandbox -
//    MID: PGTESTPAYUAT71
//    Salt Index: 1
//    Salt Key: 3540d200-f7aa-4701-95b1-6f93be0cc7c4

    private static final int B2B_PG_REQUEST_CODE = 777;
    private int coinId;
    private B2BPGRequest b2BPGRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.payToolbar.customToolbar.setTitle("Payment");

        PhonePe.init(this, PhonePeEnvironment.RELEASE, merchantId, APP_ID_PHONEPE);

        coins = getIntent().getFloatExtra("COINS", 0f);
        coinPrice = getIntent().getIntExtra("PRICE", 0);
        coinId = getIntent().getIntExtra("BUY_COIN_ID", 0);
        packageName = getIntent().getStringExtra(UPI_APP_PACKAGE_INTENT);
        appName = getIntent().getStringExtra("APP_NAME");

        binding.noOfCoins.setText((int) coins + "");
        binding.coinPrice.setText("₹" + coinPrice + "");
        binding.upiApp.setText(appName);


        try {
            upiAppsList = PhonePe.getUpiApps();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (UPIApplicationInfo info : upiAppsList) {
            Log.d("getNewMerchantTxnId", "  UPI apps Name: " + info.getApplicationName());
        }

        JSONObject jsonObject = new JSONObject();



        //      Payment Instrument.............
        JSONObject payIns = new JSONObject();
        try {
            payIns.put("type", "PAY_PAGE");
            payIns.put("targetApp",packageName );//packageName"com.phonepe.app"

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject deviceContext = new JSONObject();
        try {
            deviceContext.put("deviceOS", "ANDROID");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        merchanTxnId = "WR" + new Date().getTime() + "";
        try {
            jsonObject.put("merchantId", merchantId);//sandbox MID PGTESTPAYUAT71
            jsonObject.put("merchantTransactionId", merchanTxnId);
            jsonObject.put("merchantUserId", ControlRoom.getInstance().getUserName(PaymentActivity.this));
            jsonObject.put("amount", coinPrice * 100);  //must multiply by 100
            jsonObject.put("callbackUrl", PHONEPE_CALLBACK_URL);
            jsonObject.put("mobileNumber", PHONEPE_PAYLOAD_PHONE);
            jsonObject.put("deviceContext", deviceContext);
            jsonObject.put("paymentInstrument", payIns);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("Payloaddd", "onCreate: JsonPayload: " + jsonObject);

        String base64Payload = convertJsonToBase64(jsonObject);

        Log.d("Base64", "onCreate: base64: new : " + base64Payload);

        String diggest = SHA256Util.generateSHA256Hash(base64Payload + "/pg/v1/pay" + SALT_KEY);// 3540d200-f7aa-4701-95b1-6f93be0cc7c4

        String x_token = diggest + "###" + SALT_INDEX;

        Log.d("xtoken", "onCreate: digest: " + diggest);

//        callApi(x_token, base64Payload);

        b2BPGRequest = new B2BPGRequestBuilder()
                .setData(base64Payload)
                .setChecksum(x_token)
                .setUrl(apiEndPoint)
                .build();


        String string_signature = PhonePe.getPackageSignature();
        Log.d("sign", "onClick: phonepe Sign: " + string_signature);
        System.out.println("Signature: "+ string_signature);

        binding.startPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string_signature = PhonePe.getPackageSignature();
                Log.d("sign", "onClick: phonepe Sign: " + string_signature);
                // Calling initiate payment api and send payment required data...

                initPayment();


            }
        });

    }

    private void initPayment() {
        binding.payProgressBar.setVisibility(View.VISIBLE);
        binding.startPayment.setVisibility(View.GONE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pg_amount", coinPrice * 100);
            jsonObject.put("amt", coinPrice);
            jsonObject.put("coins_id", coinId);
            jsonObject.put("coins", coins);
            jsonObject.put("m_trnx_id", merchanTxnId);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PHONEPE_INITIATE_PAY_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("initPayment", "onResponse: response Sucessfull: " + response.getString("data"));

                        // start Payment here...
                        startPaymentPhonePe();

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("initPayment", "onResponse: response Failed: " + response.getString("data"));
                        binding.payProgressBar.setVisibility(View.GONE);
                        binding.startPayment.setVisibility(View.VISIBLE);
                        Toast.makeText(PaymentActivity.this, "Payment Request Failed", Toast.LENGTH_SHORT).show();

                    } else Log.d("initPayment", "onResponse: Something went wrong");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("initPayment", "onResponse: error ResPonse:  " + error.getMessage());
                binding.payProgressBar.setVisibility(View.GONE);
                binding.startPayment.setVisibility(View.VISIBLE);
                Toast.makeText(PaymentActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(PaymentActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    public void startPaymentPhonePe() {
        try {
            startActivityForResult(Objects.requireNonNull(PhonePe.getImplicitIntent(PaymentActivity.this, b2BPGRequest,
                    packageName)), B2B_PG_REQUEST_CODE);
            binding.startPayment.setClickable(false);
            binding.startPayment.setEnabled(false);
        } catch (PhonePeInitException e) {
            e.printStackTrace();
            Log.d("Phonnnn", "onCreate: PhonePe Exception: " + e.getMessage());

        }

    }



    private String convertJsonToBase64(JSONObject object) {
        return Base64.encodeToString(object.toString().getBytes(), Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
            finishAffinity();

            // SHA
            Log.d("Payloaddd", "onActivityResult: merchant Txn id: " + merchanTxnId);
            String diggest = SHA256Util.generateSHA256Hash("/pg/v1/status/" + merchantId + "/" + merchanTxnId + SALT_KEY) + "###1";
            Log.d("Payloaddd", "onActivityResult: sha status: " + diggest);


            // RESPONSE

            if (resultCode == RESULT_OK) {
                assert data != null;
                Log.d("Payloaddd", "onActivityResult: " + data.getDataString());
            }

        }
    }


}