package com.wizard.rewards720.Fragments;

import static com.wizard.rewards720.Constants.AUTHORISATION;
import static com.wizard.rewards720.Constants.BEARER;
import static com.wizard.rewards720.Constants.COINS;
import static com.wizard.rewards720.Constants.CONTENT_TYPE;
import static com.wizard.rewards720.Constants.CONTENT_TYPE_VALUE;
import static com.wizard.rewards720.Constants.DIAMONDS;
import static com.wizard.rewards720.Constants.USER_API_URL;
import static com.wizard.rewards720.LoginActivity.accessToken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import ai.bitlabs.sdk.data.model.Survey;
import ai.bitlabs.sdk.util.OnResponseListener;
import ai.bitlabs.sdk.util.OnRewardListener;
import theoremreach.com.theoremreach.TheoremReach;
import theoremreach.com.theoremreach.TheoremReachRewardListener;


public class CoinFragment extends Fragment implements IUnityAdsShowListener {

    FragmentCoinBinding binding;
    ArrayList<BuyCoinModal> buyCoinList;
    BitLabs bitLabs;
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
    public final static String appLovinAdUnitIdInter = "0427e43674805933";
    public final static String appLovinAdUnitIdReward = "9aa2a13e957c630f";

    public static final String BITLAB_API_TOKEN = "5ad285b7-65e6-4c20-b3c9-5a0fa16bee54";

    public static final String CpxResearchAppId = "19743";  //old: 18268
    public static final String CpxResearchSecureHash = "vtDYDzglIj7HvnI8hRrANzxIjLoJBGCe";  // old: KNTw7a4l57W9ovHDXxprzj54ajdqGTEc

    public static final String AYET_APP_KEY = "5c4bb5912a9158d5b42c31de7d94c0b2";
    public static final String AYET_AD_SLOT_NAME = "Wizard Rewards";

    public static final String IN_BRAIN_CLIENT_ID = "ee1aac88-a0fe-4fc0-9ed9-631dec9d697a";
    public static final String IN_BRAIN_SECRET_KEY = "QP2JEgYFEO3yPCdKXxDfQc5IEwQh04Vjah/8vE4yhfThxwaed75vARPjfTu1CQIsjO1z9I7C9BYo9Tt0ox6Rog==";
    public static final String THEOREMREACH_API_KEY = "dfcdb0f44865db3fd154dd423108";

    public static final String POLLFISH_API_KEY = "edd0dc80-7137-4f4c-aa89-840ae6cf573d";
    private int retryAttempt=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoinBinding.inflate(inflater, container, false);

//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue, getContext().getTheme()));
        binding.drawToolbar.setTitle("Get Coins");
        binding.drawToolbar.setTitleTextColor(getResources().getColor(R.color.whiteOnly, requireContext().getTheme()));

        activity = getActivity();



//        swipe refresh
        binding.swipeRefreshCoinFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFragment();

            }
        });

        updateCoins();
//        binding.coinTxt.setText(COINS+"");
//        binding.diamondTv.setText(ControlRoom.getInstance().getDiamonds());

        account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());


//        buyCoinRv


        getBuyCoinsList();


//        Offerwalls starts here


        // BITLABS
        bitLabs = BitLabs.INSTANCE;
        bitLabs.init(BITLAB_API_TOKEN, ControlRoom.getInstance().getId());

        binding.bitlabOfferwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitLabs != null) {
                    bitLabs.launchOfferWall(getContext());
                } else
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();


            }
        });

        bitLabs.setOnRewardListener(new OnRewardListener() {
            @Override
            public void onReward(float coins) {
                int coinInt = (int) coins;
                int availableCoins = Integer.parseInt(binding.coinTxt.getText().toString());
                binding.coinTxt.setText(String.valueOf(availableCoins + coinInt));
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

        bitLabs.getSurveys(new OnResponseListener<List<Survey>>() {
            @Override
            public void onResponse(@Nullable List<Survey> surveys) {
                if (surveys == null) {
                    Log.d("BitlabsSurveys", "onResponse: gett surveyss");
                } else {
                    for (Survey survey :
                            surveys) {
                        Log.d("BitlabsSurveys", "onResponse: allSurveyy here: " + survey.getId());

                    }
                }
            }
        });


       /*The types of personal information we may collect via our surveys include your name, IP address, email address, zip code,
        address, phone number, demographic information (age, household income, family size, highest education completed),
        product preferences and any other information you choose to provide. In some cases, health information, information regarding
       race or ethnic origin, political opinions, religious beliefs, membership information, and gender may also be collected.*/

//      TheoremReach Rewards Center
//        Log.d("TheoremReach", "onReward: : "+ quantity);
        TheoremReach.initWithApiKeyAndUserIdAndActivityContext(THEOREMREACH_API_KEY,
                ControlRoom.getInstance().getId(), getActivity());
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
        Log.d("AyetSdk", "onCreateView: ayet userid: " + ControlRoom.getInstance().getId());
        AyetSdk.init(getActivity().getApplication(), ControlRoom.getInstance().getId(), new UserBalanceCallback() {
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

        Log.d("AyetSdk", "onCreateView: Ayet Pending Balance: " + AyetSdk.getPendingBalance(getContext()));


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

        String cpaUrl = "https://fastsvr.com/list/509844" + "&subid=" + ControlRoom.getInstance().getId();

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
                .userId(ControlRoom.getInstance().getId())
                .requestUUID(ControlRoom.getInstance().getId())
                .offerwallMode(true)
                .clickId(ControlRoom.getInstance().getId())
                .pollfishSurveyCompletedListener(new PollfishSurveyCompletedListener() {
                    @Override
                    public void onPollfishSurveyCompleted(@NonNull SurveyInfo surveyInfo) {
                        Toast.makeText(getContext(), "Pollfish Survey completed", Toast.LENGTH_SHORT).show();
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

//        CPX Researchs Offerwall
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideLeftSmall,
                "Click to open CPX Research Offerwall",
                14,
                "#ffffff",
                "#3295AC",
                true);

        CPXConfiguration config = new CPXConfigurationBuilder(CpxResearchAppId,
                ControlRoom.getInstance().getId(), CpxResearchSecureHash,
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

        InBrain.getInstance().setInBrain(getContext(), IN_BRAIN_CLIENT_ID, IN_BRAIN_SECRET_KEY, true,
                ControlRoom.getInstance().getUserName());     //Using UserName instead of UserId because it is required to use more than one character.
//          InBrain Initialization is done in SplashScreenActivity
        Log.d("InBrain", "onCreate: Username: " + ControlRoom.getInstance().getUserName());
        
        
    /*    InBrain.getInstance().addCallback(new InBrainCallback() {
            @Override
            public void surveysClosed(boolean b, List<InBrainSurveyReward> list) {
            }

            @Override
            protected void onDestroy() {
                InBrain.getInstance().removeCallback(th); // unsubscribe from events
                super.onDestroy();
            }
        });*/

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

        InBrain.getInstance().areSurveysAvailable(getContext(), new SurveysAvailableCallback() {
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


                InBrain.getInstance().showSurveys(getContext(), new StartSurveysCallback() {
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

//        Offer-walls ends here


//        show interstitial ad on click..........


        binding.spinWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SpinWheelActivity.class));
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
                startActivity(new Intent(getContext(), ScratchWinActivity.class));
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
                startActivity(new Intent(getContext(), CoinHistoryActivity.class));
            }
        });
//        Go to diamond Activity
        binding.diamondTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DiamondHistoryActivity.class));
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
//        binding.spinWin.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//        binding.spinWin.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
//        Spin btn check
        getSpinnerDataFrag();

//        Daily Check In
//        disable btn
        binding.dailyCheckInBtn.setClickable(false);
        if (activity != null && isAdded()) {
            binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
            binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
        }
//        binding.dailyCheckInBtn.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//        binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
//        check daily CheckIn
        getDailyCheckInDataFrag();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.daily_check_in_dailog, null);
        TextView claimBtn = view.findViewById(R.id.claimDailyReward);
        dailyCheckInDialog = new Dialog(getContext(), R.style.CustomDialog);
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
                claimDailyCheckin();
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


        return binding.getRoot();
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
        myRewardedAd = MaxRewardedAd.getInstance(appLovinAdUnitIdReward, getActivity());
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
                Toast.makeText(getContext(), "Ad loaded Failed", Toast.LENGTH_SHORT).show();

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
                                binding.buyCoinRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.buyCoinRv.setAdapter(new BuyCoinAdapter(buyCoinList, getContext()));
                                binding.buyCoinRv.hideShimmerAdapter();
                                binding.buyCoinRv.setNestedScrollingEnabled(false);


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
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
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
                        Log.d("AppLovin", "run: loadAd App Lovin: retry attempt: "+ retryAttempt);
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

                                Toast.makeText(getContext(), "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                                refreshFragment();


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
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


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
                                binding.watchVideioRv.setAdapter(new WatchVidAdapter(watchVideoList, getContext(), new ButtonClickListener() {
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
                                binding.watchVideioRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.watchVideioRv.setNestedScrollingEnabled(false);

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
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }

    private void claimDailyCheckin() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.DAILY_CHECK_IN_POST_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

                                Log.d("claimDailyCheckin", "onResponse: response Sucess: " + response.getString("data"));

                                Toast.makeText(getContext(), "" + response.getString("data"), Toast.LENGTH_SHORT).show();
                                dailyCheckInDialog.dismiss();

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CoinFragment()).commit();
//                                getActivity().getSupportFragmentManager().beginTransaction().attach(getParentFragment());
//                                startActivity(getActivity().getIntent());

                                /*FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                        .beginTransaction();
                                if (Build.VERSION.SDK_INT >= 26) {
                                    transaction.setReorderingAllowed(false);
                                }
                                transaction.detach(CoinFragment.this).attach
                                        (CoinFragment.this).commit();*/


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
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }

    private void getDailyCheckInDataFrag() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DAILY_CHECK_IN_GET_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {

//                                binding.frameLayout.setVisibility(View.GONE);
//                                binding.swipeRefreshBidHis.setRefreshing(false);

                                Log.d("getDailyCheckInDataFrag", "onResponse: response Sucess: " + response.getString("data"));

                                JSONObject data = response.getJSONObject("data");

                                boolean dailyCheckIn = data.getBoolean("dayily_limit");     //"dayily_limit" = does user have any attempt available

                                if (dailyCheckIn) {
                                    binding.dailyCheckInBtn.setClickable(true);
                                    if (activity != null && isAdded()) {
                                        binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.button_bg, activity.getTheme()));
                                        binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                    }
//                                    binding.dailyCheckInBtn.setBackground(getResources().getDrawable(R.drawable.button_bg, getActivity().getTheme()));
//                                    binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                                } else {
                                    binding.dailyCheckInBtn.setClickable(false);
                                    if (activity != null && isAdded()) {
                                        binding.dailyCheckInBtn.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.disable_button_bg, activity.getTheme()));
                                        binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                    }
//                                    binding.dailyCheckInBtn.setBackground(getResources().getDrawable(R.drawable.disable_button_bg, getActivity().getTheme()));
//                                    binding.dailyCheckInBtn.setTextColor(getResources().getColor(R.color.whiteOnly, requireActivity().getTheme()));
                                }


                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getDailyCheckInDataFrag", "onResponse: response Failed: " + response.getString("data"));

                            } else {
                                Log.d("getDailyCheckInDataFrag", "onResponse: something went wrong");

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getDailyCheckInDataFrag", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }

    private void getSpinnerDataFrag() {

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
                header.put(Constants.AUTHORISATION, BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
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
                header.put(Constants.AUTHORISATION, Constants.BEARER + accessToken);
                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
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

    private void refreshFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CoinFragment()).commit();

        binding.swipeRefreshCoinFrag.setRefreshing(false);
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
    }
}