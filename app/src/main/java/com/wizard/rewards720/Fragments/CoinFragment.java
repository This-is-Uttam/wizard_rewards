package com.wizard.rewards720.Fragments;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.COINS;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DIAMONDS;
import static com.wizard.rewards720.Constants.USER_API_URL;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.ayetstudios.publishersdk.AyetSdk;
import com.ayetstudios.publishersdk.interfaces.UserBalanceCallback;
import com.ayetstudios.publishersdk.messages.SdkUserBalance;
//import com.prodege.Prodege;
//import com.prodege.builder.AdRequest;
//import com.prodege.listener.ProdegeException;
//import com.prodege.listener.ProdegeRewardedInfo;
//import com.prodege.listener.ProdegeRewardedLoadListener;
import com.pubscale.sdkone.offerwall.OfferWall;
import com.pubscale.sdkone.offerwall.OfferWallConfig;
import com.pubscale.sdkone.offerwall.models.OfferWallInitListener;
import com.pubscale.sdkone.offerwall.models.OfferWallListener;
import com.pubscale.sdkone.offerwall.models.Reward;
import com.pubscale.sdkone.offerwall.models.errors.InitError;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;
import com.wizard.rewards720.Adapters.BuyCoinAdapter;
import com.wizard.rewards720.Adapters.WatchVidAdapter;
import com.wizard.rewards720.ButtonClickListener;
import com.wizard.rewards720.CoinHistoryActivity;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ControlRoom;
import com.wizard.rewards720.DiamondHistoryActivity;
import com.wizard.rewards720.Modals.BuyCoinModal;
import com.wizard.rewards720.Modals.WatchVidModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.ScratchWinActivity;
import com.wizard.rewards720.SpinWheelActivity;
import com.wizard.rewards720.TrakierActivity;
import com.wizard.rewards720.databinding.FragmentCoinBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.inbrain.sdk.InBrain;
import com.inbrain.sdk.callback.StartSurveysCallback;
import com.inbrain.sdk.callback.SurveysAvailableCallback;
import com.inbrain.sdk.config.StatusBarConfig;
import com.inbrain.sdk.config.ToolBarConfig;
import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;
import com.makeopinion.cpxresearchlib.models.TransactionItem;
import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.callback.PollfishSurveyCompletedListener;
import com.pollfish.callback.SurveyInfo;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ai.bitlabs.sdk.BitLabs;
//import ai.bitlabs.sdk.util.OnExceptionListener;
import ai.bitlabs.sdk.util.OnResponseListener;
import theoremreach.com.theoremreach.TheoremReach;
import theoremreach.com.theoremreach.TheoremReachRewardListener;


public class CoinFragment extends Fragment implements IUnityAdsShowListener {


    FragmentCoinBinding binding;
    ArrayList<BuyCoinModal> buyCoinList;
    BitLabs bitLabs;
    private static String uID;
    GoogleSignInAccount account;
    MaxInterstitialAd myInterstitialAd;
    Dialog dailyCheckInDialog;
    ArrayList<WatchVidModal> watchVideoList;
    MaxRewardedAd myRewardedAd;
    CPXResearch cpxResearch;
    int videoId;
    public final static String unityAdUnitIdInter = "Interstitial_Android";
    public final static String unityAdUnitIdReward = "Rewarded_Android";
    Activity activity;

    //    AppLovin AdUnit Id:
    public final static String appLovinAdUnitIdInter = "87a13cc4487613ac";//"0427e43674805933";
    public final static String appLovinAdUnitIdReward = "80f208aa816ebd9c";//"9aa2a13e957c630f";

    public static final String BITLAB_API_TOKEN = "5ad285b7-65e6-4c20-b3c9-5a0fa16bee54";

    public static final String CpxResearchAppId = "19743";  //old: 18268
    public static final String CpxResearchSecureHash = "vtDYDzglIj7HvnI8hRrANzxIjLoJBGCe";  // old: KNTw7a4l57W9ovHDXxprzj54ajdqGTEc

    public static final String AYET_APP_KEY = "5c4bb5912a9158d5b42c31de7d94c0b2";
    public static final String AYET_AD_SLOT_NAME = "Wizard Rewards";

    public static final String IN_BRAIN_CLIENT_ID = "ee1aac88-a0fe-4fc0-9ed9-631dec9d697a";
    public static final String IN_BRAIN_SECRET_KEY = "QP2JEgYFEO3yPCdKXxDfQc5IEwQh04Vjah/8vE4yhfThxwaed75vARPjfTu1CQIsjO1z9I7C9BYo9Tt0ox6Rog==";
    public static final String THEOREMREACH_API_KEY = "dfcdb0f44865db3fd154dd423108";

    public static final String POLLFISH_API_KEY = "edd0dc80-7137-4f4c-aa89-840ae6cf573d";
    public static final String POLLFISH_SURVEY_OFFERS_PLAC_ID = "388d2e91-c052-4ba7-b661-302e60093837";
    public static final String POLLFISH_PROEDGE_REWARD_AD_PLAC_ID = "4823a0b7-fb3c-470c-8da7-731ec41adfe5";

    StartAppAd startIoVideoAd;

    public static final String PUBSCALE_APP_ID = "38344024";
    private int retryAttempt = 0;

    public CoinFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCoinBinding.inflate(inflater, container, false);

//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getContext().getTheme()));
        binding.drawToolbar.setTitle("Get Coins");
        activity = requireActivity();
        binding.drawToolbar.setTitleTextColor(getResources().getColor(R.color.whiteOnly, activity.getTheme()));



//        swipe refresh
        binding.swipeRefreshCoinFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshFragment();

                updateCoins();
                getBuyCoinsList();
                getSpinnerDataFrag();
                getScratchDetailsFromServerFrag();
                getDailyCheckInDataEligibilityFrag();
                getWatchVideoList();

                //all eligibility
                checkStartIoEligibility();
//                checkProedgeEligible();

            binding.swipeRefreshCoinFrag.setRefreshing(false);
            }
        });

        updateCoins();

        account = GoogleSignIn.getLastSignedInAccount(activity);

//        buyCoinRv
        getBuyCoinsList();


//        Offerwalls starts here


        // BITLABS
        String uid =ControlRoom.getInstance().getId(activity);
        Log.d("idid", "onCreateView: idid: "+ uid);
        bitLabs = BitLabs.INSTANCE;
        if (uid != null){
            bitLabs.init(BITLAB_API_TOKEN, uid);
        }

        binding.bitlabOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitLabs != null) {
                    bitLabs.launchOfferWall(activity);
                } else
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();


            }
        });


        bitLabs.checkSurveys(new OnResponseListener<Boolean>() {
            @Override
            public void onResponse(@Nullable Boolean isSurvey) {
                Log.d("checkSurvey", "onResponse:check survey is running " + isSurvey);
                try {

                    if (isSurvey) {
                        binding.checkSurvery.setText("Survey Available");
                    } else
                        binding.checkSurvery.setText("Survey Not Available");
                } catch (Exception e) {

                    Log.d("BitlabsException", "onResponsee: " + e.getMessage());
                }
            }
        });

//        Trakier Offers
        binding.trakierOffer.setOnClickListener(view -> {
            startActivity(new Intent(activity, TrakierActivity.class));
        });

       /*The types of personal information we may collect via our surveys include your name, IP address, email address, zip code,
        address, phone number, demographic information (age, household income, family size, highest education completed),
        product preferences and any other information you choose to provide. In some cases, health information, information regarding
       race or ethnic origin, political opinions, religious beliefs, membership information, and gender may also be collected.*/

//      TheoremReach Rewards Center
        TheoremReach.initWithApiKeyAndUserIdAndActivityContext(THEOREMREACH_API_KEY,
                ControlRoom.getInstance().getId(activity), getActivity());
        binding.theoremReachCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TheoremReach.getInstance().showRewardCenter();
            }
        });

        boolean surveyAvailable = TheoremReach.getInstance().isSurveyAvailable();
        if (surveyAvailable) {
            binding.checkSurvery2.setText("Survey Available");
        } else
            binding.checkSurvery2.setText("Survey not Available");

        TheoremReach.getInstance().setTheoremReachRewardListener(new TheoremReachRewardListener() {
            @Override
            public void onReward(int quantity) {
                Log.d("TheoremReach", "onReward: Coins: " + quantity);

            }
        });

//        AyetStudios
        Log.d("AyetSdk", "onCreateView: ayet userid: " + ControlRoom.getInstance().getId(activity));
        AyetSdk.init(getActivity().getApplication(), ControlRoom.getInstance().getId(activity), new UserBalanceCallback() {
            @Override
            public void userBalanceChanged(SdkUserBalance sdkUserBalance) {
                Log.d("AyetSdk", "userBalanceChanged - available balance: " + sdkUserBalance.getAvailableBalance()); // this is the new total available balance for the user

            }

            @Override
            public void userBalanceInitialized(SdkUserBalance sdkUserBalance) {
                Log.d("AyetSdk", "ayet SDK initialization successful");
                Log.d("AyetSdk", "userBalanceInitialized - available balance: " + sdkUserBalance.getAvailableBalance()); // this is the total available balance for the user
                Log.d("AyetSdk", "userBalanceInitialized - spent balance: " + sdkUserBalance.getSpentBalance()); // this is the total amount spent with "AyetSdk.deductUserBalance(..)"
                Log.d("AyetSdk", "userBalanceInitialized - pending balance: " + sdkUserBalance.getPendingBalance()); // this is the amount currently pending for conversion (e.g. user still has offer requirements to meet)

            }

            @Override
            public void initializationFailed() {
                Log.d("AyetSdk", "initializationFailed - please check APP KEY & internet connectivity");
            }
        }, AYET_APP_KEY);



        binding.ayetOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AyetSdk.isInitialized()) {
                    Log.d("AyetSdk", "onCreateView: AyetSdk is initialised successfully");
                    AyetSdk.showOfferwall(getActivity().getApplication(), AYET_AD_SLOT_NAME);
                } else
                    Log.d("AyetSdk", "onCreateView: AyetSdk is Not initialised");

            }
        });

//        Cpa Lead offerwall

        String cpaUrl = "https://fastsvr.com/list/509844" + "&subid=" + ControlRoom.getInstance().getId(activity);

        binding.cpaOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cpaUrl));
                startActivity(browserIntent);

            }
        });

//        Pollfish offerwall
        Params params = new Params.Builder(POLLFISH_API_KEY)
                .rewardMode(true)
                .releaseMode(true)
                .userId(ControlRoom.getInstance().getId(activity))
                .requestUUID(ControlRoom.getInstance().getId(activity))
                .placementId(POLLFISH_SURVEY_OFFERS_PLAC_ID)
                .clickId(ControlRoom.getInstance().getId(activity))
                .offerwallMode(true)
                .pollfishSurveyCompletedListener(new PollfishSurveyCompletedListener() {
                    @Override
                    public void onPollfishSurveyCompleted(@NonNull SurveyInfo surveyInfo) {
                        Toast.makeText(activity, "Pollfish Survey completed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        Pollfish.initWith(getActivity(), params);

        binding.pollfishOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pollfish.show();
            }
        });

        // Proedge Rewarded Videos.
        // Proedge Initialization is done in MainActivity.java

        //proedge check
//        Pollfish Video Ad
//        checkProedgeEligible();

//        loadProedge();
//        binding.pollfishVidAd.setVisibility(View.VISIBLE);

        binding.pollfishVidAd.setOnClickListener(view -> {
//            showStartIoRewardedVideo();

//            Prodege.showPlacement(POLLFISH_PROEDGE_REWARD_AD_PLAC_ID, new ProdegeShowListener() {
//                @Override
//                public void onOpened(@NonNull String s) {
//                    Log.d("ProedgeAds", "onOpened: ");
//
//                }
//
//                @Override
//                public void onClosed(@NonNull String s) {
//                    Log.d("ProedgeAds", "onClosed: ");
//                    loadProedge();
////                    updateCoins();
//                    claimProedge();
//                }
//
//                @Override
//                public void onShowFailed(@NonNull String s, @NonNull ProdegeException e) {
//                    Log.d("ProedgeAds", "onShowFailed: " + e.getMessage());
//                    Toast.makeText(getContext(), "Failed to show Ad, Try again later.", Toast.LENGTH_SHORT).show();
//                }
//            }, null);


        });


        startIoVideoAd = new StartAppAd(requireContext());
        // check start.io eligibility
        checkStartIoEligibility();

        // Start.io Ad
        binding.startAppAd.setOnClickListener(view -> {
            showStartIoRewardedVideo();
        });



//        CPX Researchs Offerwall
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideLeftSmall,
                "Click to open CPX Research Offerwall",
                14,
                "#ffffff",
                "#3295AC",
                true);

        CPXConfiguration config = new CPXConfigurationBuilder(CpxResearchAppId,
                ControlRoom.getInstance().getId(activity), CpxResearchSecureHash,
                style)
                .build();
//        CPXHash.Companion.md5()


        cpxResearch = CPXResearch.Companion.init(config);

        cpxResearch.activateAutomaticCheckForSurveys();

//        check Cpx survey..........
        cpxResearch.registerListener(new CPXResearchListener() {
            @Override
            public void onSurveysUpdated() {
                binding.cpxCheckSurvery.setText("Surveys Updated, click to check availability");

            }

            @Override
            public void onTransactionsUpdated(@NonNull List<TransactionItem> list) {
                Log.d("Cpx", "onTransactionsUpdated: ");
            }

            @Override
            public void onSurveysDidOpen() {
                Log.d("Cpx", "onSurveysDidOpen: ");
            }

            @Override
            public void onSurveysDidClose() {
                Log.d("Cpx", "onSurveysDidClose: ");
            }

            @Override
            public void onSurveyDidOpen() {
                Log.d("Cpx", "onSurveyDidOpen: ");
            }

            @Override
            public void onSurveyDidClose() {
                Log.d("Cpx", "onSurveyDidClose: ");
            }
        });


        cpxResearch.setSurveyVisibleIfAvailable(false, getActivity());
        binding.cpxResearchsOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpxResearch.setSurveyVisibleIfAvailable(true, getActivity());
                boolean bannerCpx = cpxResearch.getShowBannerIfSurveysAreAvailable();
                Log.d("Cpx", "onClick: bannershow: " + bannerCpx);
                if (!bannerCpx) {
                    Toast.makeText(activity, "Survey not available, try again in few seconds later", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Wait few seconds to show CPX Banner", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        InBrain Offerwall

        InBrain.getInstance().setInBrain(activity, IN_BRAIN_CLIENT_ID, IN_BRAIN_SECRET_KEY, true,
                ControlRoom.getInstance().getUserName(requireContext()));     //Using UserName instead of UserId because it is required to use more than one character.
//          InBrain Initialization is done in SplashScreenActivity
        Log.d("InBrain", "onCreate: Username: " + ControlRoom.getInstance().getUserName(requireContext()));
        


        ToolBarConfig toolBarConfig = new ToolBarConfig();
        toolBarConfig.setTitle("InBrain Offerwall"); // set title

        boolean useResourceId = false;
        if (useResourceId) {
            toolBarConfig.setToolbarColorResId(R.color.TertiaryColor) // set toolbar color with status bar
                    .setBackButtonColorResId(android.R.color.white) // set icon color
                    .setTitleColorResId(android.R.color.white); //  set toolbar text
        } else {
            toolBarConfig.setToolbarColor(getResources().getColor(R.color.TertiaryColor))
                    .setBackButtonColor(getResources().getColor(android.R.color.white))
                    .setTitleColor(getResources().getColor(android.R.color.white));
        }
        toolBarConfig.setElevationEnabled(false);
        InBrain.getInstance().setToolbarConfig(toolBarConfig);

        StatusBarConfig statusBarConfig = new StatusBarConfig();
        if (useResourceId) {
            statusBarConfig.setStatusBarColorResId(R.color.TertiaryColor)
                    .setStatusBarIconsLight(false);
        } else {
            statusBarConfig.setStatusBarColor(getResources().getColor(R.color.TertiaryColor, activity.getTheme()))
                    .setStatusBarIconsLight(false);
        }
        InBrain.getInstance().setStatusBarConfig(statusBarConfig);

        InBrain.getInstance().areSurveysAvailable(activity, new SurveysAvailableCallback() {
            @Override
            public void onSurveysAvailable(boolean surveyAvailable) {
                if (surveyAvailable) {
                    binding.inBrainCheckSurvery.setText("Survey Available");
                    Log.d("InBrain", "onSurveysAvailable: " + surveyAvailable);
                } else {
                    binding.inBrainCheckSurvery.setText("Survey Not Available");
                    Log.d("InBrain", "onSurveysAvailable: " + surveyAvailable);
                }

            }
        });

        binding.inBrainOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Are Surveys Available..?


                InBrain.getInstance().showSurveys(activity, new StartSurveysCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("InBrain", "onSuccess: InBrain Offerwall is showing");
                    }

                    @Override
                    public void onFail(String s) {
                        Log.d("InBrain", "onSuccess: InBrain Offerwall is NOT showing: " + s);
                        Toast.makeText(activity, "InBrain Survey Not Available", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

//        // Pubscale Offerwall
//        OfferWallConfig offerWallConfig =
//                new OfferWallConfig.Builder(activity, PUBSCALE_APP_ID)
//                        .setUniqueId(ControlRoom.getInstance().getId(activity)) //optional, used to represent the user of your application
////                        .setLoaderBackgroundBitmap(backgroundBitmap)//optional
////                        .setLoaderForegroundBitmap(foregroundBitmap)//optional
//                        .setFullscreenEnabled(false)//optional
//                        .build();
//
//        // Pubscale Initialization
//        OfferWall.init(offerWallConfig, new OfferWallInitListener() {
//            @Override
//            public void onInitSuccess() {
//                Log.d("pubscale", "onInitSuccess: Pubscale init success");
//            }
//
//            @Override
//            public void onInitFailed(@NonNull InitError error) {
//                Log.d("pubscale", "onInitFailed: Pubscale init Failed: "+ error.getMessage());
//            }
//        });

        binding.pubscaleOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferWall.launch(activity, new OfferWallListener() {
                    @Override
                    public void onOfferWallShowed() {
                        
                    }

                    @Override
                    public void onOfferWallClosed() {

                    }

                    @Override
                    public void onRewardClaimed(Reward reward) {
                        Log.d("pubscale", "onRewardClaimed: currency: " + reward.getCurrency()+ "components: "+reward.component1()+reward.component2()+reward.component3());
                        Toast.makeText(activity, "Pubscale Reward Coins added successfully! "+reward.getAmount()+" coins" , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(String s) {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        Offer-walls ends here


//        show interstitial ad on click..........


        binding.spinWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, SpinWheelActivity.class));
                if (myInterstitialAd.isReady()) {

                    myInterstitialAd.showAd();
                    Log.d("AppLovin", "onClick: AppLovin Ad is showing");
                } else {
                    Log.d("AppLovin", "onClick: AppLovin Ad wasn't not ready");
                    UnityAds.show(getActivity(), unityAdUnitIdInter, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            Log.d("UnityAd", "onUnityAdsShowFailure: failed: " + message);
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {
                            Log.d("UnityAd", "onUnityAdsShowStart: ");
                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {
                            Log.d("UnityAd", "onUnityAdsShowClick: ");

                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            Log.d("UnityAd", "onUnityAdsShowComplete: ");

                        }
                    });
                }
            }
        });

        binding.scratchWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ScratchWinActivity.class));
                if (myInterstitialAd.isReady()) {

                    myInterstitialAd.showAd();
                } else {
                    Log.d("myIntertitialAd", "onClick: AppLovin Ad wasn't not ready");
                    UnityAds.show(getActivity(), unityAdUnitIdInter, new UnityAdsShowOptions(), CoinFragment.this);

                }
            }
        });

//        Go to coin Activity
        binding.coinTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, CoinHistoryActivity.class));
            }
        });
//        Go to diamond Activity
        binding.diamondTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, DiamondHistoryActivity.class));
            }
        });

//        scratch button disable
        binding.scratchWin.setClickable(false);
        if (activity != null && isAdded()) {
            binding.scratchWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
            binding.scratchWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
        }

//        scratch check
        getScratchDetailsFromServerFrag();

//        spin btn disable
        binding.spinWin.setClickable(false);
        if (activity != null && isAdded()) {
            binding.spinWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
            binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
        }
//        Spin btn check
        getSpinnerDataFrag();

//        Daily Check In
//        disable btn
        binding.dailyCheckInBtn.setClickable(false);
        if (activity != null && isAdded()) {
            binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
            binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
        }
//        check daily CheckIn
        getDailyCheckInDataEligibilityFrag();

        View view = LayoutInflater.from(activity).inflate(R.layout.daily_check_in_dailog, null);
        TextView claimBtn = view.findViewById(R.id.claimDailyReward);
        dailyCheckInDialog = new Dialog(activity, R.style.CustomDialog);
        dailyCheckInDialog.setContentView(view);
        binding.dailyCheckInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyCheckInDialog.show();
            }
        });
//        claimBtn click
        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                claimDailyCheckin();
                UnityAds.show(activity, unityAdUnitIdReward, new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {

                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {

                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {

                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        claimDailyCheckin();
                        UnityAds.load(unityAdUnitIdReward, new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {

                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {

                            }
                        });
                    }
                });
            }
        });


//                Mobile Ad Initialisation....

        Log.d("AppLovin", "onCreateView: coin fragment");
//  Interstitial AppLovin Ad
        loadInterstitialAd();

//        Rewarded AppLovin Ad
        loadRewardedAd();

//        Interstitial Unity Ad
        loadUnityInterstitialAd();

//        Rewarded Unity Ad
        loadUnityRewardedAd();

//        watch Video....
        getWatchVideoList();

        // Start.io Ad initialization in SSA;
//        loadStartIoAd();   this loads after cheking eligibility


//        Banner startAppBanner = binding.startAppBanner;
//        startAppBanner.loadAd(startAppBanner.getWidthInDp(), startAppBanner.getHeightInDp());
//        startAppBanner.showBanner();


        return binding.getRoot();
    }

    private void loadStartIoAd() {

        startIoVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                Log.d("startio", "onReceiveAd: start io loaded ad"+" Is ad ready? "+ startIoVideoAd.isReady());
//                startIoVideoAd.showAd();
//                Toast.makeText(activity, "Start.io Ad is Ready", Toast.LENGTH_SHORT).show();
                // send data to server here
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                // Can't show Start.io rewarded video Ad.
                Log.d("startio", "onReceiveAd: start io Not loaded ad"+ ad.getErrorMessage()+" Is ad ready? "+ startIoVideoAd.isReady());
//                Toast.makeText(activity, "Start.io Ad is NOT Ready", Toast.LENGTH_SHORT).show();
                //UnityAd

//                UnityAds.show(getActivity(), unityAdUnitIdReward, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
//                    @Override
//                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
//                        Log.d("UnityAd", "onUnityAdsShowFailure: failed: " + message);
//                    }
//
//                    @Override
//                    public void onUnityAdsShowStart(String placementId) {
//                        Log.d("UnityAd", "onUnityAdsShowStart: ");
//                    }
//
//                    @Override
//                    public void onUnityAdsShowClick(String placementId) {
//                        Log.d("UnityAd", "onUnityAdsShowClick: ");
//
//                    }
//
//                    @Override
//                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
//                        Log.d("UnityAd", "onUnityAdsShowComplete: ");
//                        loadUnityRewardedAd();
//                        // send data to server here
//                        // and after that refresh coins
//                        claimStartIoReward();
//
//
//                    }
//                });

            }
        });

        startIoVideoAd.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                // Grant the reward to user
                // Toast.makeText(activity, "Video completed", Toast.LENGTH_SHORT).show();
                claimStartIoReward();
            }
        });
    }

    private void checkStartIoEligibility() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.START_IO_ELIGIBILITY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {


                                binding.startAppAd.setVisibility(View.VISIBLE);
                                loadStartIoAd();


                                Log.d("checkStartIoEligibility", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                boolean dailyCheckIn = data.getBoolean("dayily_limit");     //"dayily_limit" = does user have any attempt available

                                if (dailyCheckIn) {
                                    binding.startAppAd.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.startAppAd.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.startAppAd.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(activity.getResources(),R.color.orangeDark, activity.getTheme())));
                                        binding.startAppAd.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
                                } else {
                                    binding.startAppAd.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.startAppAd.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.startAppAd.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(activity.getResources(),R.color.orange_dull_2, activity.getTheme())));

                                        binding.startAppAd.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
                                }

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("checkStartIoEligibility", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("checkStartIoEligibility", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkStartIoEligibility", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    public void showStartIoRewardedVideo() {


        if (startIoVideoAd.isReady()){
                startIoVideoAd.showAd();

        }else {
            //UnityAd

                UnityAds.show(getActivity(), unityAdUnitIdReward, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        Log.d("UnityAd", "onUnityAdsShowFailure: failed: " + message);
                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {
                        Log.d("UnityAd", "onUnityAdsShowStart: ");
                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {
                        Log.d("UnityAd", "onUnityAdsShowClick: ");

                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        Log.d("UnityAd", "onUnityAdsShowComplete: ");
                        loadUnityRewardedAd();
                        // send data to server here
                        // and after that refresh coins
                        claimStartIoReward();


                    }
                });
        }

//        startIoVideoAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
//            @Override
//            public void onReceiveAd(Ad ad) {
//                Log.d("startio", "onReceiveAd: start io loaded ad");
////                startIoVideoAd.showAd();
//                Toast.makeText(activity, "Start.io Ad is Ready", Toast.LENGTH_SHORT).show();
//                // send data to server here
//            }
//
//            @Override
//            public void onFailedToReceiveAd(Ad ad) {
//                // Can't show Start.io rewarded video Ad.
//                Log.d("startio", "onReceiveAd: start io Not loaded ad");
//                Toast.makeText(activity, "Start.io Ad is NOT Ready", Toast.LENGTH_SHORT).show();
//                //UnityAd
//
////                UnityAds.show(getActivity(), unityAdUnitIdReward, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
////                    @Override
////                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
////                        Log.d("UnityAd", "onUnityAdsShowFailure: failed: " + message);
////                    }
////
////                    @Override
////                    public void onUnityAdsShowStart(String placementId) {
////                        Log.d("UnityAd", "onUnityAdsShowStart: ");
////                    }
////
////                    @Override
////                    public void onUnityAdsShowClick(String placementId) {
////                        Log.d("UnityAd", "onUnityAdsShowClick: ");
////
////                    }
////
////                    @Override
////                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
////                        Log.d("UnityAd", "onUnityAdsShowComplete: ");
////                        loadUnityRewardedAd();
////                        // send data to server here
////                        // and after that refresh coins
////                        claimStartIoReward();
////
////
////                    }
////                });
//
//            }
//        });
    }

    private void claimStartIoReward() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.START_IO_CLAIM, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("claimStartIoReward", "onResponse: response Sucess: " + response.getString("data"));

//                                if (response.getString("data").equals(response.getString("data"))) {
//
//                                }
                                    Toast.makeText(activity, "Coins added for Play and Earn!", Toast.LENGTH_SHORT).show();
                                    checkStartIoEligibility();
                                    updateCoins();


//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CoinFragment()).commit();


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("claimStartIoReward", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("claimStartIoReward", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("claimStartIoReward", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    private void claimProedge() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.POLLFISH_PROEDGE_CLAIM, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("claimProedge", "onResponse: response Sucess: " + response.getString("data"));

                                if (response.getString("data").equals(response.getString("data"))) {

                                    Toast.makeText(activity, "Coins added for Pollfish Video Ad", Toast.LENGTH_SHORT).show();
                                }


//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CoinFragment()).commit();
                                checkProedgeEligible();


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("claimProedge", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("claimProedge", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("claimProedge", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    private void checkProedgeEligible() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.POLLFISH_PROEDGE_ELIGIBILITY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {


                                binding.pollfishVidAd.setVisibility(View.VISIBLE);

                                Log.d("checkProedgeEligible", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                boolean dailyCheckIn = data.getBoolean("dayily_limit");     //"dayily_limit" = does user have any attempt available

                                if (dailyCheckIn) {
                                    binding.pollfishVidAd.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.pollfishVidAd.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.pollfishVidAd.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(activity.getResources(),R.color.orangeDark, activity.getTheme())));
                                        binding.pollfishVidAd.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
                                } else {
                                    binding.pollfishVidAd.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.pollfishVidAd.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.pollfishVidAd.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(activity.getResources(),R.color.orange_dull_2, activity.getTheme())));

                                        binding.pollfishVidAd.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
                                }

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("checkProedgeEligible", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("checkProedgeEligible", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkProedgeEligible", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    private void loadProedge() {

//        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
//        AdRequest adRequest = adRequestBuilder
//                .clickId(ControlRoom.getInstance().getId(activity))
//                .requestUuid(ControlRoom.getInstance().getId(activity))
//                .build();

        /*Prodege.loadRewardedAd(POLLFISH_PROEDGE_REWARD_AD_PLAC_ID, new ProdegeRewardedLoadListener() {
            @Override
            public void onRewardedLoaded(@NonNull String s, @NonNull ProdegeRewardedInfo prodegeRewardedInfo) {
                Log.d("ProedgeAds", "onRewardedLoaded: coins: " + prodegeRewardedInfo.getPoints());
            }

            @Override
            public void onRewardedLoadFailed(@NonNull String s, @NonNull ProdegeException e) {
                Log.d("ProedgeAds", "onRewardedLoadFailed: " + e.getMessage());

            }
        }, adRequest);*/
    }

    private void loadUnityRewardedAd() {

        UnityAds.load(unityAdUnitIdReward, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Rewarded Ad loaded successfully");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.d("UnityAd", "onUnityAdsAdLoaded:Rewarded Ad loaded Failed " + message);
            }
        });
    }

    private void loadUnityInterstitialAd() {

        UnityAds.load(unityAdUnitIdInter, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Ad loaded successfully");
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.d("UnityAd", "onUnityAdsAdLoaded: Ad loaded Failed " + message);
            }
        });
    }

    private void loadRewardedAd() {
        myRewardedAd = MaxRewardedAd.getInstance(appLovinAdUnitIdReward, activity);
        myRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {

            }

            @Override
            public void onRewardedVideoStarted(MaxAd maxAd) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd maxAd) {
                loadRewardedAd();
                binding.coinFragConstrLayout.setVisibility(View.GONE);
                sendWatchVidDetailToServer(videoId);
            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("AppLovin", "onAdLoaded: Applovin Rewarded is loaded");

            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {

            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                Toast.makeText(activity, "Ad loaded Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.d("ccc", "onAdDisplayFailed: " + maxError.getMessage());

            }
        });
        myRewardedAd.loadAd();
    }

    private void getBuyCoinsList() {
        binding.buyCoinRv.showShimmerAdapter();
        buyCoinList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.BUY_COINS_GET_API,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("getBuyCoinsList", "onResponse: response Sucess: " + response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    int id = jsonObject.getInt("id");
                                    int prize = jsonObject.getInt("rate");
                                    int noOfCoins = jsonObject.getInt("coins");


                                    BuyCoinModal buyCoinModal = new BuyCoinModal(prize, noOfCoins, id);
                                    buyCoinList.add(buyCoinModal);


                                }

//                        checking buyCoin list empty.
                                if (buyCoinList.size() == 0) {
                                    binding.buyCoinRv.setVisibility(View.GONE);
                                    binding.emptyTxtBuyCoin.setVisibility(View.VISIBLE);
                                } else {
                                    binding.buyCoinRv.setVisibility(View.VISIBLE);
                                    binding.emptyTxtBuyCoin.setVisibility(View.GONE);


                                    binding.buyCoinRv.setLayoutManager(new LinearLayoutManager(activity));
                                    binding.buyCoinRv.setAdapter(new BuyCoinAdapter(buyCoinList, activity));
                                    binding.buyCoinRv.hideShimmerAdapter();
                                    binding.buyCoinRv.setNestedScrollingEnabled(false);
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getBuyCoinsList", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getBuyCoinsList", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getBuyCoinsList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    private void loadInterstitialAd() {

        myInterstitialAd = new MaxInterstitialAd(appLovinAdUnitIdInter, requireActivity());
        myInterstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("AppLovin", "onAdLoaded: Applovin Interstitial ad");
                // Reset retry attempt
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {
//                loadInterstitialAd();

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {
                myInterstitialAd.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                // Interstitial ad failed to load 
                // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myInterstitialAd.loadAd();
                        Log.d("AppLovin", "run: loadAd App Lovin: retry attempt: " + retryAttempt);
                    }
                }, delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.d("myIntertitialAd", "onAdDisplayFailed: " + maxError.getMessage());
            }
        });
        myInterstitialAd.loadAd();
    }

    private void sendWatchVidDetailToServer(int videoId) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", videoId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.WATCH_VIDEO_POST_API,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.coinFragConstrLayout.setVisibility(View.VISIBLE);
                                Log.d("sendWatchVidDetailToServer", "onResponse: response Sucess: " + response.getString("data"));

                                Toast.makeText(activity, "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                                getWatchVideoList();
                                updateCoins();




                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("sendWatchVidDetailToServer", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("sendWatchVidDetailToServer", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendWatchVidDetailToServer", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);


    }

    private void getWatchVideoList() {
        watchVideoList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.WATCH_VIDEO_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("getWatchVideoList", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                JSONArray jsonArray = data.getJSONArray("watch_video");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String btnTitle = object.getString("title");
                                    int usedLimit = object.getInt("user_limit");    //userLimit = used Limit Of Claim
                                    int attempts = object.getInt("attempts");    //attempts = Total Limit Of Claim
                                    int vidId = object.getInt("id");
                                    boolean isButtonEnable;
                                    if (usedLimit == attempts) {
                                        isButtonEnable = false;
                                    } else {
                                        isButtonEnable = true;
                                    }
                                    WatchVidModal modal = new WatchVidModal(btnTitle, vidId, isButtonEnable);

                                    watchVideoList.add(modal);

                                }

                                if (watchVideoList.size() != 0) {
                                    binding.watchVideioRv.setAdapter(new WatchVidAdapter(watchVideoList, activity, new ButtonClickListener() {
                                        @Override
                                        public void onButtonClick(int vidId) {
                                            videoId = vidId;
                                            if (myRewardedAd.isReady()) {
                                                myRewardedAd.showAd();
                                            } else if (UnityAds.isInitialized()) {

                                                UnityAds.show(getActivity(), unityAdUnitIdReward, new UnityAdsShowOptions(),
                                                        CoinFragment.this);
                                            } else {
                                                Toast.makeText(activity, "No Ads Available", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }));
                                    binding.watchVideioRv.setLayoutManager(new LinearLayoutManager(activity));
                                    binding.watchVideioRv.setNestedScrollingEnabled(false);
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getWatchVideoList", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getWatchVideoList", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getWatchVideoList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    private void claimDailyCheckin() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.DAILY_CHECK_IN_POST_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("claimDailyCheckin", "onResponse: response Sucess: " + response.getString("data"));

                                Toast.makeText(activity, "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                                dailyCheckInDialog.dismiss();
                                updateCoins();

//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CoinFragment()).commit();
                                    getDailyCheckInDataEligibilityFrag();

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("claimDailyCheckin", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("claimDailyCheckin", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("claimDailyCheckin", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    private void getDailyCheckInDataEligibilityFrag() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DAILY_CHECK_IN_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {


                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                boolean dailyCheckIn = data.getBoolean("dayily_limit");     //"dayily_limit" = does user have any attempt available

                                if (dailyCheckIn) {
                                    binding.dailyCheckInBtn.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
                                } else {
                                    binding.dailyCheckInBtn.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getDailyCheckInDataEligibilityFrag", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);

    }

    public void getSpinnerDataFrag() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SPINNER_DATA_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

//                                binding.frameLayout.setVisibility(View.GONE);
//                                binding.swipeRefreshBidHis.setRefreshing(false);

                                Log.d("getSpinnerDataFrag", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");
                                JSONObject spinner = data.getJSONObject("spinner");
                                boolean attemptLimitSpin = data.getBoolean("attempt_limit");
                                Log.d("zzz", "onResponse: attemptLimit spin: " + attemptLimitSpin);
                                if (attemptLimitSpin) {
                                    binding.spinWin.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.spinWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.spinWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
//                                    binding.spinWin.setBackground(getResources().getDrawable(R.drawable.button_bg, getActivity().getTheme()));
//                                    binding.spinWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));

                                } else {
                                    binding.spinWin.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.spinWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
//                                    binding.spinWin.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//                                    binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));

                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getSpinnerDataFrag", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getSpinnerDataFrag", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getSpinnerDataFrag", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    private void getScratchDetailsFromServerFrag() {
//        scratchCoinList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SCRATCH_CARD_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("getScratchDetailsFromServerFrag", "onResponse: respose success " +
                                        response.getString("data"));
                                JSONObject data = response.getJSONObject("data");
                                JSONObject scratchCard = data.getJSONObject("scratchCard");

                                boolean attemptLimit = data.getBoolean("attempt_limit");
                                Log.d("zzz", "onResponse: attemptLimit scratch: " + attemptLimit);
                                if (attemptLimit == true) {
                                    binding.scratchWin.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.scratchWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.scratchWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
//                                    binding.scratchWin.setBackground(getResources().getDrawable(R.drawable.button_bg, getActivity().getTheme()));
//                                    binding.scratchWin.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                } else {
                                    binding.scratchWin.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.scratchWin.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.scratchWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
//                                    binding.scratchWin.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//                                    binding.scratchWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getScratchDetailsFromServerFrag", "onResponse: respose Failed " + response.getString("data"));

                            } else {
                                Log.d("getScratchDetailsFromServerFrag", "onResponse: something went wrong ");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getScratchDetailsFromServerFrag", "onResponse: error response " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        TheoremReach.getInstance().onResume(getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        TheoremReach.getInstance().onPause();
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
                        ControlRoom.getInstance().setCoins(response.getJSONObject("data").getInt("points") + "", activity);
                        ControlRoom.getInstance().setDiamonds(DIAMONDS + "",activity);

                        JSONObject data = response.getJSONObject("data");
                        uID = data.getString("id");


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
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(activity));
                return header;
            }
        };
        Volley.newRequestQueue(activity).add(jsonObjectRequest);
    }

    @Override
    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
        Log.d("UnityAd", "onUnityAdsShowFailure: failed: " + message);
    }

    @Override
    public void onUnityAdsShowStart(String placementId) {
        Log.d("UnityAd", "onUnityAdsShowStart: ");
    }

    @Override
    public void onUnityAdsShowClick(String placementId) {
        Log.d("UnityAd", "onUnityAdsShowClick: ");

    }

    @Override
    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
        Log.d("UnityAd", "onUnityAdsShowComplete: ");
        sendWatchVidDetailToServer(videoId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cpxResearch.deactivateAutomaticCheckForSurveys();
        cpxResearch.setSurveyVisibleIfAvailable(false, getActivity());
        // pubscale destroy..
        OfferWall.destroy();
    }
}