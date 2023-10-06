package com.wizard.rewards720;

import static com.wizard.rewards720.LoginActivity.accessToken;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.databinding.ActivityMyInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MyInfoActivity extends AppCompatActivity {

    ActivityMyInfoBinding binding;
    String fullName, phone, dob, address, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.myInfoToolbar.customToolbar.setTitle("My Information");
//        binding.myInfoToolbar.customToolbar.setTitleTextColor(getResources().getColor(R.color.black, getTheme()));
        setSupportActionBar(binding.myInfoToolbar.customToolbar);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getTheme()));

        binding.myInfoProgress.setVisibility(View.GONE);
//        binding.submitButton.setClickable(false);


        binding.nameInputLayout.getEditText().setText(ControlRoom.getInstance().getFullName());
        setUserDetails();


        Calendar calendar = Calendar.getInstance();
        binding.dobTexInpLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MyInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        String dob = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(calendar.getTime());
                        binding.dobTexInpLayout.getEditText().setText(dob);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                dialog.show();
            }
        });

        binding.dobTexInpLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MyInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        String dob = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(calendar.getTime());
                        binding.dobTexInpLayout.getEditText().setText(dob);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                dialog.show();
            }
        });


        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = binding.nameInputLayout.getEditText().getText().toString();
                phone = binding.phoneInputLayout.getEditText().getText().toString();
                dob = binding.dobTexInpLayout.getEditText().getText().toString();
                address = binding.addInputLayout.getEditText().getText().toString();
                if (binding.maleRadio.isChecked())
                    gender = binding.maleRadio.getText().toString();
                else if (binding.femaleRadio.isChecked())
                    gender = binding.femaleRadio.getText().toString();
                else
                    gender = binding.othersRadio.getText().toString();

                checkValidation();
            }
        });


    }

    private void setUserDetails() {
        binding.myInfoProgress.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.USER_API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("setUserDetails", "onResponse: response is Successful " + response.getString("data"));
                                binding.myInfoProgress.setVisibility(View.GONE);
                                JSONObject jsonObject = response.getJSONObject("data");

                                String fullName = jsonObject.getString("name");

                                binding.nameInputLayout.getEditText().setText(fullName);
                                String phone = jsonObject.getString("phone");
                                if (phone.equals("null")){
                                    binding.phoneInputLayout.getEditText().setText("");
                                }else
                                    binding.phoneInputLayout.getEditText().setText(phone);

//                              dob
                                String dob = jsonObject.getString("d_o_b");
                                if (dob.equals("null")) {

                                    binding.dobTexInpLayout.getEditText().setText("");
                                } else {
                                    binding.dobTexInpLayout.getEditText().setText(dob);
                                }

//                               gender
                                String gender = jsonObject.getString("gender");
                                if (gender.equals("Male")) {
                                    binding.maleRadio.setChecked(true);
                                } else if (gender.equals("Female")) {
                                    binding.femaleRadio.setChecked(true);
                                } else if (gender.equals("Others")) {
                                    binding.othersRadio.setChecked(true);
                                }

//                              address
                                String address = jsonObject.getString("address");
                                if (address.equals("null")) {

                                    binding.addInputLayout.getEditText().setText("");
                                } else {
                                    binding.addInputLayout.getEditText().setText(address);
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("setUserDetails", "onResponse: response is Failed " + response.getString("data"));

                            } else {
                                Log.d("setUserDetails", "onResponse: something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setUserDetails", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + accessToken);
                return header;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void checkValidation() {
        if (phone.equals("")) {
            binding.phoneInputLayout.setError("This field is required.");
        } else {
            if (!phone.matches("[6789][0-9]{9}")) {
                binding.phoneInputLayout.setError("Please Enter Valid Phone Number.");
            } else {
                if (fullName.equals("")) {
                    binding.nameInputLayout.setError("This field cannot be empty");
                } else if (fullName.length() >=30) {
                    binding.nameInputLayout.setError("Name cannot exceeds 30 characters");
                } else {
//                    Toast.makeText(this, "Submitted Data", Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                    binding.myInfoProgress.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    private void updateUserInfo() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", fullName);
            jsonObject.put("phone", phone);
            jsonObject.put("d_o_b", dob);
            jsonObject.put("gender", gender);
            jsonObject.put("address", address);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.UPDATE_USER_DETAIL_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateUserInfo", "onResponse: response is Successful " + response.getString("data"));
                        binding.myInfoProgress.setVisibility(View.GONE);

                        Toast.makeText(MyInfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(MyInfoActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("updateUserInfo", "onResponse: response is Failed " + response.getString("data"));
                        binding.myInfoProgress.setVisibility(View.GONE);
                        Toast.makeText(MyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("updateUserInfo", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateUserInfo", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + accessToken);
                return header;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void updateMyInfo(View view) {

        if (fullName.isEmpty()) {
            binding.nameInputLayout.setError("This field can't be empty");


        } else if (phone.isEmpty()) {
            binding.phoneInputLayout.setError("This field can't be empty");
        } else {
            updateAllDetails();

            Toast.makeText(MyInfoActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateAllDetails() {
        JSONObject updateInfo = new JSONObject();
        try {
            updateInfo.put("name", fullName);
            updateInfo.put("phone", phone);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest()
    }
}