package com.example.android.bitcoinview.data;

/**
 * Created by Anon on 09.08.17.
 */

public class BcPreferences {

    private static final String DEFAULT_CURR = "EUR";
    private static final String DEFAULT_FINANCE_URL = "https://www.finanzen.net/devisen/bitcoin-euro-kurs";

    public static String getDefaultCurr(){
        return DEFAULT_CURR;
    }
    public static String getDefaultFinWeb() { return DEFAULT_FINANCE_URL; }
}
