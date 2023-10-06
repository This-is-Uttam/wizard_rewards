package com.wizard.rewards720.Fragments;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.LeaderBoardAdapter;
import com.wizard.rewards720.BuildConfig;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.LeaderboardDetailActivity;
import com.wizard.rewards720.Modals.LeaderBoardModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.FragmentReferBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReferFragment extends Fragment {

    FragmentReferBinding binding;
    ArrayList<LeaderBoardModal> leaderBoardList;
    Context context;
    String referCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReferBinding.inflate( inflater, container, false);
        context= container.getContext();

//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue,getContext().getTheme()));

        binding.notificationToolbar.setTitle("Refer & Earn");
//        binding.notificationToolbar.setTitleTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
//        getUserData();

        referCode = ControlRoom.getInstance().getReferCode();
        binding.referCode.setText(referCode);

        getLeaderBoardWinners();


        SpannableString referEarnIntro = SpannableString.valueOf(binding.referEarnIntro.getText().toString());
        referEarnIntro.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(getContext(), LeaderboardDetailActivity.class));
            }
        },100,112, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        referEarnIntro.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.TertiaryColor, getContext()
                .getTheme())),100,112, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        referEarnIntro.setSpan(new StyleSpan(Typeface.BOLD),100,112,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.referEarnIntro.setText(referEarnIntro);
        binding.referEarnIntro.setMovementMethod(LinkMovementMethod.getInstance());

//        refer text click event
        binding.referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.refer_earn_txt)+"https://play.google.com/store/apps/details?id=" +BuildConfig.APPLICATION_ID +"\n\nReferral Code: *"+  referCode+"*");
                startActivity(Intent.createChooser(shareIntent,"Share to your friends"));
            }
        });

        binding.copyReferCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Refer Code",binding.referCode.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Refer Code Copied..", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void getLeaderBoardWinners() {
        leaderBoardList = new ArrayList<>();
        binding.leaderboardProgress.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.LEADERBOARD_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){
                                binding.leaderboardProgress.setVisibility(View.GONE);

                                Log.d("getLeaderBoardWinners", "onResponse: response Sucessfull: "+ response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String name  = jsonObject.getString("name");
                                    String image = jsonObject.getString("image");
                                    int totalRefer = jsonObject.getInt("m_valid_refer");    // monthly_valid_refer_count
                                    boolean showCrown =false;
                                    if (i<5){
                                        showCrown=true;
                                    }

                                    LeaderBoardModal leaderBoardModal = new LeaderBoardModal(showCrown,i+1,name,image,totalRefer);
                                    leaderBoardList.add(leaderBoardModal);
                                }

                                // checking Gifts list empty.
                                if (leaderBoardList.size() == 0) {
                                    binding.leaderBoardRv.setVisibility(View.GONE);
                                    binding.emptyTxtLeaderboard.setVisibility(View.VISIBLE);
                                } else {
                                    binding.leaderBoardRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtLeaderboard.setVisibility(View.GONE);

                                    binding.leaderBoardRv.setAdapter(new LeaderBoardAdapter(leaderBoardList,getContext()));
                                    binding.leaderBoardRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                    binding.leaderBoardRv.setNestedScrollingEnabled(false);
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getLeaderBoardWinners", "onResponse: response Failed: "+ response.getString("data"));
                            }else {
                                Log.d("getLeaderBoardWinners", "onResponse: Something went wrong ");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getLeaderBoardWinners", "onResponse: error response: "+error.getMessage());
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
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);



    }

}