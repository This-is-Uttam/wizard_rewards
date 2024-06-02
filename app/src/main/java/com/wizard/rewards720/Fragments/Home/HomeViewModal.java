package com.wizard.rewards720.Fragments.Home;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.TrendingAdapter;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.PromotionModal;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.Modals.UserModal;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.Modals.VoucherWinModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeViewModal extends ViewModel {
    private HomeRepository homeRepository;

    public HomeViewModal() {
        homeRepository = new HomeRepository();
    }

    public LiveData<ArrayList<PromotionModal>> getBannersList(){
        return homeRepository.getBannersList();
    }

    public void fetchBannersDataFromApi(Context context){
        homeRepository.fetchBannersDataFromApi(context);
    }

    public LiveData<ArrayList<VoucherMainModal>> getVouchersList(){
        return homeRepository.getVouchersList();
    }

    public void fetchVouchersDataFromApi(Context context){
        homeRepository.fetchVouchersDataFromApi(context);
    }

    public LiveData<ArrayList<TrendingModal>> getProductsList(){
        return homeRepository.getProductList();
    }

    public void fetchProductsDataFromApi(Context context) {
        homeRepository.fetchProductsDataFromApi(context);
    }

    public LiveData<ArrayList<VoucherWinModal>> getVoucherWinsList() {
        return homeRepository.getVoucherWinsList();
    }

    public void fetchVoucherWinsDataFromApi(Context context){
        homeRepository.fetchVoucherWinsDataFromApi(context);
    }

    public LiveData<UserModal> getUserData() {return homeRepository.getUserData();}

    public void fetchUserDataFromApi(Context context) {
        homeRepository.fetchUserDataFromApi(context);
    }


}
