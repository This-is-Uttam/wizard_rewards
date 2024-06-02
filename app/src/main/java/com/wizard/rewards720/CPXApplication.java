package com.wizard.rewards720;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CPXApplication extends Application {

    private CPXResearch cpxResearch;
    public static final String CpxResearchAppId = "18268";
    public static String ext_user_id;


    @Override
    public void onCreate() {

        super.onCreate();

        initCPX();
    }

    @NonNull
    public CPXResearch getCpxResearch() {
        return cpxResearch;
    }

    private void initCPX() {
        fetchUserDetailsCpx();
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideLeftSmall,
                "Click to open CPX Research Offerwall",
                14,
                "#ffffff",
                "#3295AC",
                true);


        CPXConfiguration config = new CPXConfigurationBuilder(CpxResearchAppId,
                "uttam592k", "KNTw7a4l57W9ovHDXxprzj54ajdqGTEc",
                style)
                //.withSubId1("subId1")
                //.withSubId2("subId2")
                //.withEmail("user@email.com")
                //.withExtraInfo(new String[]{"value1", "value2"})
                //.withCustomConfirmCloseDialogTexts("Title", "msg", "ok", "cancel")
                .build();



        cpxResearch = CPXResearch.Companion.init( config);

    }

    private void fetchUserDetailsCpx() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")&& response.getInt("code")==200){
                        Log.d("fetchUserDetailsCpx", "onResponse: Successful...:"+response.getString("data"));
                        JSONObject userJsonObject = response.getJSONObject("data");
                        ext_user_id = userJsonObject.getString("username");

                    }else
                        Log.d("fetchUserDetailsCpx", "onResponse: Failed..."+response.getString("data"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchUserDetailsCpx", "onResponse: error Response: "+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
                header.put(CONTENT_TYPE,CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION,BEARER+ControlRoom.getInstance().getAccessToken(CPXApplication.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

}
