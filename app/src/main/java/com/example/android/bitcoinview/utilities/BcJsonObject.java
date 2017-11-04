package com.example.android.bitcoinview.utilities;

import com.example.android.bitcoinview.data.BcPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anon on 09.08.17.
 */

public class BcJsonObject {

    // currency codes
    private static final String BC_15M = "15m";
    private static final String BC_LAST = "last";
    private static final String BC_BUY = "buy";
    private static final String BC_SELL = "sell";
    private static final String BC_CURR_SYMBOL = "symbol";

    // preferred currency
    private String preferredCurr = BcPreferences.getDefaultCurr();

    //json object with bc info
    private JSONObject bcData;


    public BcJsonObject(String jsonString) throws JSONException {
        if (jsonString == null) {
            bcData = null;
        }
        else {
            bcData = new JSONObject(jsonString);
            //select the data corresponding to the preferred currency
            bcData = bcData.getJSONObject(preferredCurr);
        }
    }

    public JSONObject getBcData() {
        return bcData;
    }

    public String getLast(){
        String last_val = null;
        try {
            last_val = bcData.getString(BC_LAST);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return last_val;
    }

    public String getBuy(){
        String buy_val = null;
        try {
            buy_val = bcData.getString(BC_BUY);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return buy_val;
    }

    public String getSell(){
        String sell_val = null;
        try {
            sell_val = bcData.getString(BC_SELL);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return sell_val;
    }

    public String getSymbol(){
        String symbol_val = null;
        try {
            symbol_val = bcData.getString(BC_CURR_SYMBOL);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return symbol_val;
    }



}
