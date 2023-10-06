package com.wizard.rewards720.Fragments;

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
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wizard.rewards720.Adapters.ProductAllWinnersAdapter;
import com.wizard.rewards720.Adapters.VoucherAllWinnersAdapter;
import com.wizard.rewards720.Adapters.VoucherMainAdapter;
import com.wizard.rewards720.CoinHistoryActivity;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.DiamondHistoryActivity;
import com.wizard.rewards720.Modals.ProductWinModal;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.Modals.VoucherWinModal;
import com.wizard.rewards720.R;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Log.d("TAG", "onCreateView: is called");

//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.transparent_blue, getActivity().getTheme()));


        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);

        if (ControlRoom.isNetworkConnected(getContext())) {
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


        updateCoins();

//        promotionRv

        getPromotionImage();


//        trendingRv= productRv
        trendingItemList = new ArrayList<>();
        getAllProducts();


//        voucherRv
        voucherMainList = new ArrayList<>();
        getVoucherMainList();


//        winnerRv
        getProductWinnerList();

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

       /* String winnerDesc= String.valueOf(Html.fromHtml("<b>Uttam</b> from Madhya Pradesh wins MI Smart Watch Black worth ₹ 2999"));
        String winnerDesc2= String.valueOf(Html.fromHtml("<b>Aman</b> from Kerela wins MI Smart Watch Black worth ₹ 2999"));
        String winnerDesc3= String.valueOf(Html.fromHtml("<b>Mohan</b> from Kerela wins Alien Earpods Blue worth ₹ 2499"));
        String winnerDesc4= String.valueOf(Html.fromHtml("<b>Karan</b> from Kerela wins HP Pavilion Laptop worth ₹ 49999"));
        String winnerDesc5= String.valueOf(Html.fromHtml("<b>Aashish</b> from Kerela wins Phillips Trimmer worth ₹ 599"));
        winnerList.add(new WinnerModal("#97","U",winnerDesc,"",R.drawable.mi_watch));
        winnerList.add(new WinnerModal("#98","A",winnerDesc2,"",R.drawable.mi_watch));
        winnerList.add(new WinnerModal("#99","M",winnerDesc3,"",R.drawable.earpods));
        winnerList.add(new WinnerModal("#100","K",winnerDesc4,"",R.drawable.hp_pavilion));
        winnerList.add(new WinnerModal("#101","A",winnerDesc5,"",R.drawable.trimmer_phillips));*/

        /*Collections.reverse(winnerList);
        binding.winnerRv.setAdapter(new WinnerAdapter(winnerList,getContext()));
        binding.winnerRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.winnerRv.setNestedScrollingEnabled(false);*/

//        viewAllVoucher
        binding.viewAllVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voucherIntent = new Intent(getContext(), VoucherDetailActivity.class);

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

        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_anim);
        binding.dreampotLogo.startAnimation(slideUp);

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*getAllProducts();
                getPromotionImage();
                updateCoins();*/
                Fragment fragment = new HomeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContainer, fragment);
                ft.commit();

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
        binding.winnerRv.setForeground(getContext().getDrawable(R.color.white));

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
                            binding.winnerRv.setNestedScrollingEnabled(false);

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
                header.put(AUTHORISATION, BEARER + accessToken);
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
                            binding.winnerRv.setNestedScrollingEnabled(false);
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
                header.put(AUTHORISATION, BEARER + accessToken);
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
                        showPromotionsBanner(response);

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
                header.put(AUTHORISATION, "Bearer " + accessToken);
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


        promotionAdapter = new PromotionAdapter(promotionList, getContext());
        binding.promotionRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.promotionRv.setNestedScrollingEnabled(false);
        binding.promotionRv.setAdapter(promotionAdapter);
        binding.promotionRv.hideShimmerAdapter();
        promotionAdapter.notifyDataSetChanged();
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
                        ControlRoom.getInstance().setCoins(response.getJSONObject("data").getInt("points") + "");
                        ControlRoom.getInstance().setDiamonds(DIAMONDS + "");

                        binding.coinTxt.setText(COINS + "");
                        binding.diamondTv.setText(DIAMONDS + "");
                        System.out.println("Coins from ControlRoom: " + ControlRoom.getInstance().getCoins());
                        System.out.println("dIAMONDS from ControlRoom: " + ControlRoom.getInstance().getDiamonds() + "DIAMONDS: " + DIAMONDS);
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
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);
    }

    private void getAllProducts() {
        binding.trendingRv.showShimmerAdapter();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.PRODUCT_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                trendingItemList.clear();
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

//
                            binding.trendingRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                            binding.trendingRv.setNestedScrollingEnabled(false);
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
                header.put(AUTHORISATION, BEARER + accessToken);
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
                                    if (Integer.parseInt(full_status) == 0) {
                                        full_status_bool = false;
                                    } else {
                                        full_status_bool = true;
                                    }

                                    Log.d("getVoucherMainList", "onResponse: Value " + id);

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

                                    binding.voucherMainRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                                    binding.voucherMainRv.setNestedScrollingEnabled(false);
                                    binding.voucherMainRv.setAdapter(new VoucherMainAdapter(voucherMainList, getContext()));
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
                header.put(AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!isFirstTime){
//            Fragment fragment = new HomeFragment();
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.mainContainer, fragment);
//            ft.commit();
//
//        }else {
//            isFirstTime = false;
//        }
//
//    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        Fragment fragment = new HomeFragment();
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.mainContainer, fragment);
//        ft.commit();
//    }


}