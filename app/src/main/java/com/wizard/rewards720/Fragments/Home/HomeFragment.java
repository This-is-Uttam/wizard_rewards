package com.wizard.rewards720.Fragments.Home;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BANNER_API_URL;
import static com.wizard.rewards720.Constants.BANNER_IMG_URL;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.COINS;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DIAMONDS;
import static com.wizard.rewards720.Constants.PRODUCT_WINNERS_GET_API;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.Constants.VOUCHER_MAIN_URL;
import static com.wizard.rewards720.Constants.VOUCHER_WINNERS_GET_API;
import static com.wizard.rewards720.Fragments.CoinFragment.PUBSCALE_APP_ID;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.OfferWallConfig;
import com.pubscale.sdkone.offerwall.models.OfferWallInitListener;
import com.pubscale.sdkone.offerwall.models.OfferWallListener;
import com.pubscale.sdkone.offerwall.models.Reward;
import com.pubscale.sdkone.offerwall.models.errors.InitError;
import com.wizard.rewards720.Adapters.ProductAllWinnersAdapter;
import com.wizard.rewards720.Adapters.VoucherAllWinnersAdapter;
import com.wizard.rewards720.Adapters.VoucherMainAdapter;
import com.wizard.rewards720.CoinHistoryActivity;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.DiamondHistoryActivity;
import com.wizard.rewards720.Modals.ProductWinModal;
import com.wizard.rewards720.Modals.UserModal;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.Modals.VoucherWinModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.SplashScreenActivity;
import com.wizard.rewards720.TrendingDetailActivity;
import com.wizard.rewards720.VoucherDetailActivity;
import com.wizard.rewards720.WinnerDetailActivity;
import com.wizard.rewards720.Adapters.PromotionAdapter;
import com.wizard.rewards720.Adapters.TrendingAdapter;
import com.wizard.rewards720.Modals.PromotionModal;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.databinding.FragmentHomeBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    ArrayList<PromotionModal> promotionList;

    ArrayList<VoucherMainModal> voucherMainList;


    ArrayList<TrendingModal> trendingItemList;

    ArrayList<ProductWinModal> productWinnerList;
    ArrayList<VoucherWinModal> voucherWinList;

    boolean isError = false;
    PromotionAdapter promotionAdapter;
    boolean isFirstTime = true;
    Context mainContext;
    HomeViewModal homeViewModal;


    public HomeFragment(Context context) {
        mainContext = context;
        Log.d("saveCheck", "HomeFragment: custom constructor: "+ mainContext);
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Log.d("TAG", "onCreateView: is called");
        if (mainContext == null){
            mainContext = requireActivity();
        }

//        if (savedInstanceState == null){
//            startActivity(new Intent(mainContext, SplashScreenActivity.class));
//            finishAffinity(requireActivity());
//        }

        homeViewModal = new ViewModelProvider((ViewModelStoreOwner) mainContext).get(HomeViewModal.class);
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);

        if (ControlRoom.isNetworkConnected(requireContext())) {
            binding.frameLayout.setVisibility(View.GONE);
            binding.progressBar3.setVisibility(View.GONE);
            binding.errorImg.setVisibility(View.GONE);
            binding.message.setVisibility(View.GONE);
        } else {
            binding.progressBar3.setVisibility(View.GONE);
            binding.errorImg.setVisibility(View.VISIBLE);
            binding.message.setText("No Network Connection!");
            binding.message.setVisibility(View.VISIBLE);
        }


//        updateCoins();
        homeViewModal.getUserData().observe((LifecycleOwner) mainContext, new Observer<UserModal>() {
            @Override
            public void onChanged(UserModal userModal) {
                binding.coinTxt.setText(userModal.getCoins());
                binding.diamondTv.setText(userModal.getDiamonds());

                ControlRoom.getInstance().setCoins(userModal.getCoins(),mainContext);
                ControlRoom.getInstance().setDiamonds(userModal.getDiamonds(), mainContext);
            }
        });

        if (homeViewModal.getUserData().getValue() == null){
            homeViewModal.fetchUserDataFromApi(mainContext);
        }

//        promotionRv
//        getPromotionImage();
        homeViewModal.getBannersList().observe((LifecycleOwner) mainContext, new Observer<ArrayList<PromotionModal>>() {
            @Override
            public void onChanged(ArrayList<PromotionModal> promotionModals) {
                if (promotionModals.size() == 0) {
                    binding.promotionRv.setVisibility(View.GONE);
                } else {
                    promotionAdapter = new PromotionAdapter(promotionModals, getContext());
                    binding.promotionRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//                    binding.promotionRv.setNestedScrollingEnabled(false);
                    binding.promotionRv.setAdapter(promotionAdapter);
                    binding.promotionRv.hideShimmerAdapter();
                }
            }
        });

        if (homeViewModal.getBannersList().getValue() == null){
            binding.promotionRv.showShimmerAdapter();
            homeViewModal.fetchBannersDataFromApi(mainContext);
        }

        //Pubscale Card

        // Pubscale Offerwall
        OfferWallConfig offerWallConfig =
                new OfferWallConfig.Builder(getActivity(), PUBSCALE_APP_ID)
                        .setUniqueId(ControlRoom.getInstance().getId(getActivity())) //optional, used to represent the user of your application
//                        .setLoaderBackgroundBitmap(backgroundBitmap)//optional
//                        .setLoaderForegroundBitmap(foregroundBitmap)//optional
                        .setFullscreenEnabled(false)//optional
                        .build();

        // Pubscale Initialization
        OfferWall.init(offerWallConfig, new OfferWallInitListener() {
            @Override
            public void onInitSuccess() {
                Log.d("pubscale", "onInitSuccess: Pubscale init success");
                binding.pubCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onInitFailed(@NonNull InitError error) {
                Log.d("pubscale", "onInitFailed: Pubscale init Failed: "+ error.getMessage());
            }
        });


        binding.pubCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferWall.launch(getActivity(), new OfferWallListener() {
                    @Override
                    public void onOfferWallShowed() {

                    }

                    @Override
                    public void onOfferWallClosed() {

                    }

                    @Override
                    public void onRewardClaimed(Reward reward) {
                        Log.d("pubscale", "onRewardClaimed: currency: " + reward.getCurrency()+ "components: "+reward.component1()+reward.component2()+reward.component3());
                        Toast.makeText(getActivity(), "Pubscale Reward Coins added successfully! "+reward.getAmount()+" coins" , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(String s) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //        voucherRv
//        voucherMainList = new ArrayList<>();
//        getVoucherMainList();
        homeViewModal.getVouchersList().observe((LifecycleOwner) mainContext, new Observer<ArrayList<VoucherMainModal>>() {
            @Override
            public void onChanged(ArrayList<VoucherMainModal> voucherMainModals) {
                //               checking voucher winners list empty.
                                if (voucherMainModals.size() == 0) {
                                    binding.voucherMainRv.setVisibility(View.GONE);
                                    binding.emptyTxtVoucher.setVisibility(View.VISIBLE);
                                } else {
                                    binding.voucherMainRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtVoucher.setVisibility(View.GONE);
                                    //show the arrayList...
                                    binding.voucherMainRv.setAdapter(new VoucherMainAdapter(getSortedArrayList(voucherMainModals), getContext()));
                                    binding.voucherMainRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//                                    binding.voucherMainRv.setNestedScrollingEnabled(false);
                                    binding.voucherMainRv.hideShimmerAdapter();
                                }
            }
        });

        if (homeViewModal.getVouchersList().getValue() == null){
            binding.voucherMainRv.showShimmerAdapter();
            homeViewModal.fetchVouchersDataFromApi(mainContext);
        }

//        trendingRv= productRv
//        trendingItemList = new ArrayList<>();
//        getAllProducts();
        homeViewModal.getProductsList().observe((LifecycleOwner) mainContext, new Observer<ArrayList<TrendingModal>>() {
            @Override
            public void onChanged(ArrayList<TrendingModal> trendingModals) {
//                    checking product list empty.
                if (trendingModals.size() == 0) {
                    binding.trendingRv.setVisibility(View.GONE);
                    binding.emptyTxtProduct.setVisibility(View.VISIBLE);
                } else {
                    binding.trendingRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//                    binding.trendingRv.setNestedScrollingEnabled(false);
                    binding.trendingRv.setAdapter(new TrendingAdapter(trendingModals, getContext()));
                    binding.trendingRv.hideShimmerAdapter();
                }
            }
        });

        if (homeViewModal.getProductsList().getValue() == null) {
            binding.trendingRv.showShimmerAdapter();
            homeViewModal.fetchProductsDataFromApi(mainContext);
        }


//        winnerRv
//        getVoucherWinnersList();
        homeViewModal.getVoucherWinsList().observe((LifecycleOwner) mainContext, new Observer<ArrayList<VoucherWinModal>>() {
            @Override
            public void onChanged(ArrayList<VoucherWinModal> voucherWinModals) {
//                        checking voucher winners list empty.
                        if (voucherWinModals.size() == 0) {
                            binding.winnerRv.setVisibility(View.GONE);
                            binding.emptyTxtWinner.setVisibility(View.VISIBLE);
                        } else {
                            binding.winnerRv.setVisibility(View.VISIBLE);
                            binding.emptyTxtWinner.setVisibility(View.GONE);

                            binding.winProgressBar.setVisibility(View.GONE);
                            binding.winnerRv.setForeground(null);

                            binding.winnerRv.setAdapter(new VoucherAllWinnersAdapter(voucherWinModals, getContext(), false));
                            binding.winnerRv.setLayoutManager(new LinearLayoutManager(getContext()));
//                            binding.winnerRv.setNestedScrollingEnabled(false);
                        }
            }
        });

        if (homeViewModal.getVoucherWinsList().getValue() == null){
            binding.winProgressBar.setVisibility(View.VISIBLE);
            binding.winnerRv.setForeground(getContext().getDrawable(R.color.white));

            homeViewModal.fetchVoucherWinsDataFromApi(mainContext);
        }

//      Vouchers and Products Radio Buttons
        binding.productRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProductWinnerList();

                binding.productRadio.setEnabled(false);
                binding.voucherRadio.setEnabled(true);
            }
        });
        binding.voucherRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVoucherWinnersList();
                binding.productRadio.setEnabled(true);
                binding.voucherRadio.setEnabled(false);
            }
        });


//        viewAllVoucher
        binding.viewAllVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voucherIntent = new Intent(requireContext(), VoucherDetailActivity.class);

                startActivity(voucherIntent);
            }
        });

//        viewAllProduct
        binding.viewAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TrendingDetailActivity.class));
            }
        });
//        viewAllWinner
        binding.viewAllWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WinnerDetailActivity.class));
            }
        });

//         Swipe Refresh Listener
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*getAllProducts();
                getPromotionImage();
                updateCoins();*/
//                Fragment fragment = new HomeFragment(mainContext);
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.mainContainer, fragment);
//                ft.commit();

                // fetching BannersList
                binding.promotionRv.showShimmerAdapter();
                homeViewModal.fetchBannersDataFromApi(mainContext);

                // fetching VouchersList
                binding.voucherMainRv.showShimmerAdapter();
                homeViewModal.fetchVouchersDataFromApi(mainContext);

                // fetching ProductList
                binding.trendingRv.showShimmerAdapter();
                homeViewModal.fetchProductsDataFromApi(mainContext);

                // fetching Voucher Winners
                binding.winProgressBar.setVisibility(View.VISIBLE);
                binding.winnerRv.setForeground(getContext().getDrawable(R.color.white));
                homeViewModal.fetchVoucherWinsDataFromApi(mainContext);
                binding.voucherRadio.setChecked(true);
                binding.voucherRadio.setEnabled(false);
                binding.productRadio.setEnabled(true);

                // fetching Coins
                homeViewModal.fetchUserDataFromApi(mainContext);

                binding.swipeRefresh.setRefreshing(false);
            }
        });

        binding.coinTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CoinHistoryActivity.class));
            }
        });

        binding.diamondTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DiamondHistoryActivity.class));
            }
        });

        return binding.getRoot();
    }


    private void getProductWinnerList() {
        binding.winProgressBar.setVisibility(View.VISIBLE);
        binding.winnerRv.setForeground(ResourcesCompat.getDrawable(getResources(), R.color.white, getContext().getTheme()));

        productWinnerList = new ArrayList<>();

        // get list from server.....
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PRODUCT_WINNERS_GET_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        binding.winProgressBar.setVisibility(View.GONE);
                        binding.winnerRv.setForeground(null);
                        Log.d("getProductWinnerList", "onResponse: Sucessfull...:" + response.getString("data"));

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String imageList = jsonObject.getString("productImages");
                            String[] imgs = imageList.split(",");
                            String firstImg = imgs[0];

                            ProductWinModal productWinModal = new ProductWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("productName"),
                                    firstImg,
                                    jsonObject.getInt("mrp") + ""

                            );
                            productWinModal.setProductWinnerCount(i + 1);

                            // Winning monnth
                            int monthNum = jsonObject.getInt("winning_month");
                            String month = "";

                            switch (monthNum) {
                                case 1:
                                    month = "January";
                                    break;
                                case 2:
                                    month = "February";
                                    break;
                                case 3:
                                    month = "March";
                                    break;
                                case 4:
                                    month = "April";
                                    break;
                                case 5:
                                    month = "May";
                                    break;
                                case 6:
                                    month = "June";
                                    break;
                                case 7:
                                    month = "July";
                                    break;
                                case 8:
                                    month = "August";
                                    break;
                                case 9:
                                    month = "September";
                                    break;
                                case 10:
                                    month = "October";
                                    break;
                                case 11:
                                    month = "November";
                                    break;
                                case 12:
                                    month = "December";
                                    break;

                            }
                            productWinModal.setWinMonth(month);

                            productWinnerList.add(productWinModal);

                        }
//                         checking product winners list empty.
                        if (productWinnerList.size() == 0) {
                            binding.winnerRv.setVisibility(View.GONE);
                            binding.emptyTxtWinner.setVisibility(View.VISIBLE);
                        } else {
                            binding.winnerRv.setVisibility(View.VISIBLE);
                            binding.emptyTxtWinner.setVisibility(View.GONE);

//                        reverse list to descending order
                            Collections.reverse(productWinnerList);

                            binding.winnerRv.setAdapter(new ProductAllWinnersAdapter(productWinnerList, getContext(), false));
                            binding.winnerRv.setLayoutManager(new LinearLayoutManager(getContext()));
//                            binding.winnerRv.setNestedScrollingEnabled(false);

                        }


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("getProductWinnerList", "onResponse: Failed..." + response.getString("data"));
                    } else {
                        Log.d("getProductWinnerList", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getProductWinnerList", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }

    private void getVoucherWinnersList() {
        binding.winProgressBar.setVisibility(View.VISIBLE);
        binding.winnerRv.setForeground(getContext().getDrawable(R.color.white));

        voucherWinList = new ArrayList<>();

        // get list from server.....
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_WINNERS_GET_API,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        binding.winProgressBar.setVisibility(View.GONE);
                        binding.winnerRv.setForeground(null);
                        Log.d("getVoucherWinnersList", "onResponse: Sucessfull...:" + response.getString("data"));

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            VoucherWinModal voucherWinModal = new VoucherWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("voucherName"),
                                    jsonObject.getString("voucherImage"),
                                    jsonObject.getString("mrp")

                            );
                            voucherWinModal.setVoucherWinnerCount(i + 1);
                            // Winning monnth
                            int monthNum = jsonObject.getInt("winning_month");
                            String month = "";

                            switch (monthNum) {
                                case 1:
                                    month = "January";
                                    break;
                                case 2:
                                    month = "February";
                                    break;
                                case 3:
                                    month = "March";
                                    break;
                                case 4:
                                    month = "April";
                                    break;
                                case 5:
                                    month = "May";
                                    break;
                                case 6:
                                    month = "June";
                                    break;
                                case 7:
                                    month = "July";
                                    break;
                                case 8:
                                    month = "August";
                                    break;
                                case 9:
                                    month = "September";
                                    break;
                                case 10:
                                    month = "October";
                                    break;
                                case 11:
                                    month = "November";
                                    break;
                                case 12:
                                    month = "December";
                                    break;

                            }
                            voucherWinModal.setWinnMonth(month);

                            voucherWinList.add(voucherWinModal);

                        }

//                        checking voucher winners list empty.
                        if (voucherWinList.size() == 0) {
                            binding.winnerRv.setVisibility(View.GONE);
                            binding.emptyTxtWinner.setVisibility(View.VISIBLE);
                        } else {
                            binding.winnerRv.setVisibility(View.VISIBLE);
                            binding.emptyTxtWinner.setVisibility(View.GONE);


//                        reverse list to descending order
                            Collections.reverse(voucherWinList);

                            binding.winnerRv.setAdapter(new VoucherAllWinnersAdapter(voucherWinList, getContext(), false));
                            binding.winnerRv.setLayoutManager(new LinearLayoutManager(getContext()));
//                            binding.winnerRv.setNestedScrollingEnabled(false);
                        }


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("getVoucherWinnersList", "onResponse: Failed..." + response.getString("data"));
                    } else {
                        Log.d("getVoucherWinnersList", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherWinnersList", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }


    // promotion = banner
    private void getPromotionImage() {
        binding.promotionRv.showShimmerAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BANNER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getPromotionImage", "onResponse: response Sucessfull: " + response.getString("data"));
//                        showPromotionsBanner(response);

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getPromotionImage", "onResponse: response Failed: " + response.getString("data"));
                        isError = true;
                    } else {
                        Log.d("getPromotionImage", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getPromotionImage", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void showPromotionsBanner(JSONObject response) {
        promotionList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("data");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Log.d("showPromotionsBanner", "showPromotionsBanner: image: " + jsonObject.getString("image"));

                PromotionModal promotionModal = new PromotionModal(BANNER_IMG_URL + jsonObject.getString("image"),
                        jsonObject.getString("banner_url"));
                promotionList.add(promotionModal);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (promotionList.size() == 0) {
            binding.promotionRv.setVisibility(View.GONE);
        } else {


            promotionAdapter = new PromotionAdapter(promotionList, getContext());
            binding.promotionRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//            binding.promotionRv.setNestedScrollingEnabled(false);
            binding.promotionRv.setAdapter(promotionAdapter);
            binding.promotionRv.hideShimmerAdapter();
            promotionAdapter.notifyDataSetChanged();
        }

    }

    public void updateCoins() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateCoins", "onResponse: Sucessfull...:" + response.getString("data"));
                        COINS = response.getJSONObject("data").getInt("points");
                        DIAMONDS = response.getJSONObject("data").getInt("daimond");
                        ControlRoom.getInstance().setCoins(response.getJSONObject("data").getInt("points") + "", mainContext);
                        ControlRoom.getInstance().setDiamonds(DIAMONDS + "", mainContext);

                        binding.coinTxt.setText(COINS + "");
                        binding.diamondTv.setText(DIAMONDS + "");
                    } else
                        Log.d("updateCoins", "onResponse: Failed..." + response.getString("data"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateCoins", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }

    private void getAllProducts() {
        binding.trendingRv.showShimmerAdapter();
        trendingItemList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.PRODUCT_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getAllProductsHomeFrag", "onResponse: response Sucess: " + response.getString("data"));

                        JSONArray productJsonArray = response.getJSONArray("data");
                        for (int i = 0; i < productJsonArray.length(); i++) {
                            JSONObject productJsonObject = productJsonArray.getJSONObject(i);

                            String image = productJsonObject.getString("images");
                            ArrayList<String> imageList = new ArrayList<>(Arrays.asList(image.split(",")));
                            Log.d("getAllProductsImage", "onResponse: imageList: " + imageList.get(0));
                            TrendingModal productModal = new TrendingModal("2",
                                    productJsonObject.getString("empty_spot"),
                                    productJsonObject.getString("name"),
                                    productJsonObject.getString("price_per_spot"),
                                    imageList.get(0),
                                    productJsonObject.getString("mrp"),
                                    productJsonObject.getString("total_spot"));

                            productModal.setTrendItemId(productJsonObject.getString("id"));
                            productModal.setShortDescription(productJsonObject.getString("short_desc"));
                            productModal.setDetails(productJsonObject.getString("details"));
                            if (productJsonObject.getInt("full_status") == 0)
                                productModal.setSpotFull(false);
                            else
                                productModal.setSpotFull(true);

                            trendingItemList.add(productModal);
                        }

//                        checking product list empty.
                        if (trendingItemList.size() == 0) {
                            binding.trendingRv.setVisibility(View.GONE);
                            binding.emptyTxtProduct.setVisibility(View.VISIBLE);
                        } else {
                            binding.trendingRv.setVisibility(View.VISIBLE);
                            binding.emptyTxtProduct.setVisibility(View.GONE);


                            Collections.reverse(trendingItemList);


                            binding.trendingRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//                            binding.trendingRv.setNestedScrollingEnabled(false);
                            binding.trendingRv.setAdapter(new TrendingAdapter(trendingItemList, getContext()));
                            binding.trendingRv.hideShimmerAdapter();
                        }

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getAllProductsHomeFrag", "onResponse: response Failed: " + response.getString("data"));
                        binding.viewAllProduct.setVisibility(View.GONE);
                        binding.productTxt.setVisibility(View.GONE);
                        binding.trendingRv.setVisibility(View.GONE);
                        isError = true;

                    } else {
                        Log.d("getAllProductsHomeFrag", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAllProductsHomeFrag", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void getVoucherMainList() {
        binding.voucherMainRv.showShimmerAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("getVoucherMainList", "onResponse: response Sucessfull: " + response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String mrp = jsonObject.getString("mrp");
                                    int price_per_spot = jsonObject.getInt("price_per_spot");
                                    String total_spot = jsonObject.getString("total_spot");
                                    String empty_spot = jsonObject.getString("empty_spot");
                                    String winnig_code = jsonObject.getString("winnig_code");
                                    String winnig_daimonds = jsonObject.getString("winnig_daimonds");
                                    String short_desc = jsonObject.getString("short_desc");
                                    String details = jsonObject.getString("details");
                                    String images = jsonObject.getString("images");
                                    String full_status = jsonObject.getString("full_status");
                                    boolean full_status_bool;
                                    if (Integer.parseInt(full_status) == 0) {
                                        full_status_bool = false;
                                    } else {
                                        full_status_bool = true;
                                    }


                                    VoucherMainModal voucherMainModal = new VoucherMainModal(
                                            "2", empty_spot, total_spot, name, price_per_spot, short_desc, details, id, images,
                                            full_status_bool, winnig_code);

                                    voucherMainModal.setMrp(jsonObject.getString("mrp"));

                                    voucherMainList.add(voucherMainModal);
                                }
//                        checking voucher winners list empty.
                                if (voucherMainList.size() == 0) {
                                    binding.voucherMainRv.setVisibility(View.GONE);
                                    binding.emptyTxtVoucher.setVisibility(View.VISIBLE);
                                } else {
                                    binding.voucherMainRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtVoucher.setVisibility(View.GONE);
                                    //show the arrayList...


                                    binding.voucherMainRv.setAdapter(new VoucherMainAdapter(getSortedArrayList(voucherMainList), getContext()));
                                    binding.voucherMainRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//                                    binding.voucherMainRv.setNestedScrollingEnabled(false);
                                    binding.voucherMainRv.hideShimmerAdapter();
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getVoucherMainList", "onResponse: response Failed: " + response.getString("data"));
                                isError = true;
                            } else {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(requireActivity()).add(jsonObjectRequest);

    }

    public static ArrayList<VoucherMainModal> getSortedArrayList(ArrayList<VoucherMainModal> voucherMainList) {
        ArrayList<VoucherMainModal> voucherNewList = new ArrayList<>();
        ArrayList<VoucherMainModal> voucherEmptyList = new ArrayList<>();
        ArrayList<VoucherMainModal> voucherNewList2 = new ArrayList<>();

        // Empty spot sorting

        for (int i = 0; i < voucherMainList.size(); i++) {
            VoucherMainModal voucher = voucherMainList.get(i);

            if (voucher.getSpotLeftText().equals("0")) {

                voucherEmptyList.add(voucher);
            } else {
                voucherNewList.add(voucher);

            }
        }

        // Price sorting;
        voucherNewList.sort(new Comparator<VoucherMainModal>() {
            @Override
            public int compare(VoucherMainModal voucherMainModal, VoucherMainModal t1) {
                if (voucherMainModal.getVouPricePerSpot() == t1.getVouPricePerSpot())
                    return 0;
                else if (voucherMainModal.getVouPricePerSpot() > t1.getVouPricePerSpot())
                    return 1;
                else
                    return -1;
            }
        });
//        for (int i=0; i<voucherNewList.size()-1; i++){
//
////            Log.d("sorting", "getSortedArrayList: loop running times: "+i+" listsize: "+ (voucherMainList.size()-1));
//
//
//            VoucherMainModal voucherCurr = voucherNewList.get(i);
//            VoucherMainModal vouNext = voucherNewList.get(i+1);
//
//            int vouCurrSpotPrice = Integer.parseInt(voucherCurr.getVouPricePerSpot());
//            int vouNxtSpotPrice = Integer.parseInt(vouNext.getVouPricePerSpot());
//
//
//
//
//
////                boolean check = vouNxtSpotPrice > vouCurrSpotPrice;
////                Log.d("sorting", "getSortedArrayList: sort : "+check);
////            if (check){
//////                Collections.swap(voucherNewList,i,(i+1));
////                Log.d("sorting", "getSortedArrayList: voucherNew list : "+voucherNewList);
////            }
//
//
//
//           /* if ((voucherNewList.size()-1)< (i+1)){
//                // limit exceeds of arrayList
//                Log.d("sorting", "getSortedArrayList: limit exceeds of array");
//
//            }else {*/
//                // Price sorting;
//
//                /*int vouCurrSpotPrice = Integer.parseInt(voucherCurr.getVouPricePerSpot());
//
//                for (int j=0; j<voucherNewList.size(); j++){
//                    VoucherMainModal vouNext = voucherNewList.get(j);
//                    int vouNxtSpotPrice = Integer.parseInt(vouNext.getVouPricePerSpot());
//
//                    if (vouCurrSpotPrice > vouNxtSpotPrice){
////                        voucherNewList2.add(voucherNew);
//
////                        Log.d("sorting", "getSortedArrayList: voucherSpotIndex: "+i+ " voucherNextIndex: "+ j);
//                        Log.d("sorting", "getSortedArrayList: voucherSpotPrice: "+vouCurrSpotPrice+ " voucherNext: "+ vouNxtSpotPrice);
////                    Collections.swap(voucherNewList,i,j);
//                    }
//                }*/
//
//
////            }
//        }


        voucherNewList.addAll(voucherEmptyList);
        Log.d("getSortedArrayList", "getSortedArrayList: vMlistsize: " + voucherMainList.size());
        return voucherNewList;
    }


}