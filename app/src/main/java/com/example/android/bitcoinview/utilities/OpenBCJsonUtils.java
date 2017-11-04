package com.example.android.bitcoinview.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.bitcoinview.data.BcPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Anon on 09.08.17.
 *
 * utility functions to handle blockchain json data
 */

public class OpenBCJsonUtils {

// currency codes
    private static final String BC_15M = "15m";
    private static final String BC_LAST = "last";
    private static final String BC_BUY = "buy";
    private static final String BC_SELL = "sell";
    private static final String BC_CURR_SYMBOL = "symbol";

    // preferred currency
    private String preferredCurr = BcPreferences.getDefaultCurr();


    public static JSONObject getBcJson(Context context, String BcJsonStr)
            throws JSONException {

        JSONObject bcJson = new JSONObject(BcJsonStr);
        return bcJson;
    }

}
