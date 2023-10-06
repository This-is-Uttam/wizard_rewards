package com.wizard.rewards720;

public class Constants {

    public static final String BASE_URL = "https://easyreward.in";
    public static final String LOGIN_API_URL =  BASE_URL+"/api/login-verify";
    public static final String LOGOUT_API_URL =  BASE_URL+"/api/user/logout";
    public static final String UPDATE_FCM =  BASE_URL+"/api/update-fcm";
    public static final String USER_API_URL =  BASE_URL+"/api/user";
    public static final String BANNER_IMG_URL =  BASE_URL+"/banners/";
    public static final String BANNER_API_URL =  BASE_URL+"/api/banners";

    public static final String PRODUCT_IMG_URL =  BASE_URL+"/products/";

    public static final String PRODUCT_API_URL =  BASE_URL+"/api/product";
    public static final String PRODUCT_BID_API_URL =  BASE_URL+"/api/make-bid";
    public static final String CHECK_BID_API =  BASE_URL+"/api/check-bid-eligible";

    public static final String CHECK_REFERRAL_URL =  BASE_URL+"/api/check-referal";
    public static final String LEADERBOARD_URL =  BASE_URL+"/api/referal-leader-board";
    public static final String COINS_HISTORY_URL =  BASE_URL+"/api/get-coins-history";
    public static final String DIAMOND_HISTORY_URL =  BASE_URL+"/api/get-daimonds-history";
    public static final String BIDDING_HISTORY_URL =  BASE_URL+"/api/get-bidding-history";
    public static final String VOUCHER_BIDDING_HISTORY_URL =  BASE_URL+"/api/get-voucher-bidding-history";
    public static final String REDEEM_URL =  BASE_URL+"/api/redeem";
    public static final String REDEEM_IMG_URL =  BASE_URL+"/redeem/";
    public static final String REDEEM_REQUEST_URL =  BASE_URL+"/api/add-redeem-request";
    public static final String REDEEM_HISTORY_URL =  BASE_URL+"/api/get-redeem-request";
    public static final String VOUCHER_MAIN_URL =  BASE_URL+"/api/voucher";
    public static final String VOUCHER_IMG_URL =  BASE_URL+"/voucher/";
    public static final String CHECK_VOUCHER_BID_ELIGIBLE_API =  BASE_URL+"/api/check-voucher-bid-eligible";
    public static final String VOUCHER_BID_API =  BASE_URL+"/api/make-voucher-bid";
    public static final String UPDATE_USER_DETAIL_API =  BASE_URL+"/api/update-user-details";
    public static final String WINNER_ADDRESS_UPDATE_API =  BASE_URL+"/api/winner-address-update";
    public static final String SPINNER_DATA_API =  BASE_URL+"/api/get-spinner";
    public static final String SPINNER_POST_POSITION_API =  BASE_URL+"/api/spin-win-coins";
    public static final String SCRATCH_CARD_GET_API =  BASE_URL+"/api/get-scratch-card";
    public static final String SCRATCH_CARD_POST_API =  BASE_URL+"/api/scratch-win-coins";
    public static final String DAILY_CHECK_IN_GET_API =  BASE_URL+"/api/daily-check-in";
    public static final String DAILY_CHECK_IN_POST_API =  BASE_URL+"/api/claim-daily-check-in-reward";
    public static final String WATCH_VIDEO_GET_API =  BASE_URL+"/api/get-watch-video";
    public static final String WATCH_VIDEO_POST_API =  BASE_URL+"/api/claim-watch-video-reward";
    public static final String BUY_COINS_GET_API =  BASE_URL+"/api/get-coins-list";
    public static final String PRODUCT_WINNERS_GET_API =  BASE_URL+"/api/get-product-winners-list";
    public static final String VOUCHER_WINNERS_GET_API =  BASE_URL+"/api/get-voucher-winners-list";
    public static final String PHONEPE_SANDBOX_URL = "https://api-preprod.phonepe.com/apis/pg-sandbox/pg/v1/pay";

    public static final String PRIVACY_POLICY =  BASE_URL+"/privacy-policy";
    public static final String ABOUT_US =  BASE_URL+"/about-us";
    public static final String CONTACT_US =  BASE_URL+"/contact-us";
    public static final String REFUND_CANCELLATION =  BASE_URL+"/refund-cancellation";
    public static final String TERMS_N_CONDITION =  BASE_URL+"/trem-condition";
    public static final String SHOPPING_REFUND =  BASE_URL+"/shopping-refund";


    public static final String BEARER = "Bearer ";
    public static final String AUTHORISATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String  FCM_ID = "fcm_id";
    public static final String DEVICE_ID = "device_id";
    public static final String REFERRAL_CODE = "referal_code";
    public static int COINS;
    public static int DIAMONDS;
    public static final int COINS_ONLY_BID_VALUE = 0;
    public static final int COINS_DIAMOND_BID_VALUE = 1;
    public static final String INPUT_TYPE_TEXT = "text";
    public static final String INPUT_TYPE_EMAIL = "email";
    public static final String INPUT_TYPE_PHONE = "phone";
    public static final String INPUT_TYPE_NUMBER = "number";
}
