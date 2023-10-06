package com.wizard.rewards720;

import static com.wizard.rewards720.UpiAppsActivity.UPI_APP_PACKAGE_INTENT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wizard.rewards720.databinding.ActivityPaymentBinding;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.UPIApplicationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityPaymentBinding binding;
    List<UPIApplicationInfo> upiAppsList;
    String apiEndPoint = "/pg/v1/pay";
    String merchantId = "PGTESTPAYUAT71";
    float coins;
    int coinPrice;
    private String packageName;
    private String appName;
    public static final String PHONEPE_PAYLOAD_PHONE = "9999988888";
    public static String merchanTxnId;
    public static final String RAZORPAY_API_KEY = "";


//    MID: PGTESTPAYUAT71
//    Salt Index: 1
//    Salt Key: 3540d200-f7aa-4701-95b1-6f93be0cc7c4

    private static final int B2B_PG_REQUEST_CODE = 777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.payToolbar.customToolbar.setTitle("Payment");
//        binding.payToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black, getTheme()));
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

        PhonePe.init(this);

        coins = getIntent().getFloatExtra("COINS", 0f);
        coinPrice = getIntent().getIntExtra("PRICE", 0);
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
        getNewMerchantTxnId();

        for (UPIApplicationInfo info : upiAppsList) {
            Log.d("getNewMerchantTxnId", "  UPI apps Name: " + info.getApplicationName());
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject deviceContext = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        JSONObject accCons = new JSONObject();
        try {
            accCons.put("accountNumber", "420200001892");
            accCons.put("ifsc", "ICIC0000041");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        jsonArray.put(accCons);

        //      Payment Instrument.............
        JSONObject payIns = new JSONObject();
        try {
            payIns.put("type", "UPI_INTENT");
            payIns.put("targetApp", packageName);//packageName
            payIns.put("accountConstraints", jsonArray);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        try {
            deviceContext.put("deviceOS", "ANDROID");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        merchanTxnId = "WR" + new Date().getTime() + "";
        try {
            jsonObject.put("merchantId", merchantId);//PGTESTPAYUAT71
            jsonObject.put("merchantTransactionId", merchanTxnId);
            jsonObject.put("merchantUserId", ControlRoom.getInstance().getUserName());
            jsonObject.put("amount", coinPrice * 100);  //must multiply by 100
            jsonObject.put("callbackUrl", "https://google.com/");
            jsonObject.put("mobileNumber", PHONEPE_PAYLOAD_PHONE);
            jsonObject.put("deviceContext", deviceContext);
            jsonObject.put("paymentInstrument", payIns);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("Payloaddd", "onCreate: JsonPayload: " + jsonObject);

        String base64Payload = convertJsonToBase64(jsonObject);

        Log.d("Base64", "onCreate: base64: new : " + base64Payload);

        String diggest = SHA256Util.generateSHA256Hash(base64Payload + "/pg/v1/pay" + "3540d200-f7aa-4701-95b1-6f93be0cc7c4");

        String x_token = diggest + "###1";

//        callApi(x_token, base64Payload);

        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                .setData(base64Payload)
                .setChecksum(x_token)
                .setUrl(apiEndPoint)
                .build();

        /*ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == B2B_PG_REQUEST_CODE ){
                    Log.d("Phonnnn", "onActivityResult: Completion of UI flow of Phonepe");
                }
            }
        });*/

//        Checkout.preload(getApplicationContext());

        binding.startPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(PhonePe.getImplicitIntent(PaymentActivity.this, b2BPGRequest,
                            packageName), B2B_PG_REQUEST_CODE);
                    binding.startPayment.setClickable(false);
                    binding.startPayment.setEnabled(false);
                    binding.payProgressBar.setVisibility(View.VISIBLE);
                    binding.startPayment.setVisibility(View.GONE);
                } catch (PhonePeInitException e) {
                    e.printStackTrace();
                    Log.d("Phonnnn", "onCreate: PhonePe Exception: " + e.getMessage());

                }

//              Razorpay payment
//                startPayment();

            }
        });

               /* {
                "merchantId": "PGTESTPAYUAT",
                "merchantTransactionId": "SU78777",
                "merchantUserId": "SUB62033",
                "amount": 10000,
                "callbackUrl": "https://google.com/",
                "mobileNumber": "9999999999",
                "deviceContext": {
            "deviceOS": "ANDROID"
        },
        "paymentInstrument": {
            "type": "UPI_INTENT",
                    "targetApp": "com.phonepe.app",
                    "accountConstraints": [{
                "accountNumber": "420200001892",
                        "ifsc": "ICIC0000041"
            }]
        }*/
//        list= (ArrayList) getListOfUpiApps();


    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(RAZORPAY_API_KEY);
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.app_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        merchanTxnId = "WR" + new Date().getTime() + "";

        try {
            JSONObject options = new JSONObject();

            options.put("name", getString(R.string.app_name));
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("order_id", merchanTxnId);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(coinPrice * 100));//pass amount in currency subunits
            options.put("prefill.email", ControlRoom.getInstance().getEmail());
            options.put("prefill.contact","9999999999");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("RazorPay", "Error in starting Razorpay Checkout", e);
        }
    }

    private String getNewMerchantTxnId() {       //max 38 chars may cause error
        String id = UUID.randomUUID().toString();

        String merchantTxnId = "mtxnid_" + id.substring(0, 24);
        Log.d("getNewMerchantTxnId", "getNewMerchantTxnId: txnid: " + merchantTxnId + "  UPI apps: " + upiAppsList.size());
        return merchantTxnId;
    }

    private String convertJsonToBase64(JSONObject object) {
        return Base64.encodeToString(object.toString().getBytes(), Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) {
            binding.startPayment.setClickable(true);
            binding.startPayment.setEnabled(true);
            binding.payProgressBar.setVisibility(View.GONE);
            binding.startPayment.setVisibility(View.VISIBLE);

            // SHA
            Log.d("Payloaddd", "onActivityResult: merchant Txn id: " + merchanTxnId);
            String diggest = SHA256Util.generateSHA256Hash("/pg/v1/status/" + merchantId + "/" + merchanTxnId + "3540d200-f7aa-4701-95b1-6f93be0cc7c4") + "###1";
            Log.d("Payloaddd", "onActivityResult: sha status: " + diggest);


            // RESPONSE

            if (resultCode == RESULT_OK){
                assert data != null;
                Log.d("Payloaddd", "onActivityResult: "+ data.getDataString());
            }

            /*{
                "success": true,
                    "code": "PAYMENT_SUCCESS",
                    "message": "Your payment is successful.",
                    "data": {
                        "merchantId": "PGTESTPAYUAT71",
                        "merchantTransactionId": "WR1695990513045",
                        "transactionId": "T2309291758366258429025",
                        "amount": 50000,
                        "state": "COMPLETED",
                        "responseCode": "SUCCESS",
                        "paymentInstrument": {
                            "type": "UPI",
                            "vpa": null,
                            "maskedAccountNumber": "XXXXXXXXXX890125",
                            "ifsc": "AABF0009009",
                            "utr": "206850679072",
                            "upiTransactionId": "AXLd8ee55a8fd50452da92639907560b6cd",
                            "accountHolderName": "Rajesh Kumar"
                }
            }
            }*/
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successfull: "+s , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed: "+s , Toast.LENGTH_SHORT).show();
    }
}