package com.mwongela.regionalsustainabilitynetwork.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Almodad on 8/15/2016.
 */
public class Common {
//    public static final String API_URL = "http://consoshrine.nexbip.com/api.php?action=";
//    public static final String API_URL = "http://consoshrine.infomacks.com/api.php?action=";
//    public static final String RESOURCE_URL = "http://consoshrine.nexbip.com/appimages/";
    public static final String API_URL = "http://asap.co.ke/tibaapi/?action_type=";
    public static final String RESOURCE_URL = "http://138.197.119.49:7070/appimages/";
    public static final String ROOT_URL = "http://138.197.119.49:7070/";
    public static final String TAG_RESP_CODE= "code", TAG_USER_DETAILS= "message",
                        APP_UPDATE_SERVER_URL = "http://www.infomacks.com/mservices/updater/index.php";
    public static final String SMS_ORIGIN = "AFRICASTKNG";
    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String SMS_DELIMITER = ":";
    public static final String HELP_URL = "http://138.197.119.49:7070/help.php";
    public static final String ABOUT_URL = "http://138.197.119.49:7070/about.php";
    public static final String TERMSURL = "dukaterms.html";

    // PayPal app configuration
    //sandbox
//    public static final String PAYPAL_CLIENT_ID = "AbQy0MDKx7RCb4CJUmQWLp7VITB4NLeNdQ5CIXWCPQ0Icmr99EHTMvTv70AT5btqayIO1KQOA9RIMVwc";
    //live
    public static final String PAYPAL_CLIENT_ID =   "AaA5SMTEHB_qWxFuddj5xCdZ-D79sp04LF34z0JQtyv9zzNBMt0pwN0OfI_wPtzhsbdfPFSYxCNhmPir";
    public static final String PAYPAL_CLIENT_SECRET = "";

    // PayPal server urls
    public static final String URL_PRODUCTS = ROOT_URL+"pay/v1/products";
    public static final String URL_VERIFY_PAYMENT = ROOT_URL+"pay/v1/verifyPayment";

    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.app.consoshrine";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isAvailable = false;
        try {
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }
        }catch (NullPointerException npex){

        }
        return isAvailable;
    }
}
