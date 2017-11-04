package com.example.android.bitcoinview.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Anon on 09.08.17.
 *
 * these utilities will be used to communicate with the blockchain
 * website for BC stock information
 */

public final class NetworkUtils {

    // static webaddress to the blockchain server
    private static final String STATIC_BC_BASE_URL = "https://blockchain.info";

    // static webaddress to ticker
    private static final String STATIC_BC_TICKER = "https://blockchain.info/de/ticker";

    /**
     * Builds the url talking to the blockchain service
     * @param urlString String that the url is built from
     * @return URL to use to query the data
     *
    */
    public static URL buildUrl(){
        URL url = null;
        try {
            url = new URL(STATIC_BC_TICKER);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
