package com.example.android.bitcoinview;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bitcoinview.data.BcPreferences;
import com.example.android.bitcoinview.utilities.BcJsonObject;
import com.example.android.bitcoinview.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<BcJsonObject> {

    private static final String TAG = MainActivity.class.getSimpleName();

    // constrained layouts containing the loading indicator and the primary layout
    private ConstraintLayout mLoaderLayout;
    private ConstraintLayout mPrimaryLayout;

    // references to text views that will display the data
    private TextView mCurrPriceTextView;
    private TextView mBuyPriceTextView;
    private TextView mSellPriceTextView;

    // button that redirects to finance website
    private Button mRedirectButton;


    private static final int BC_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find constraint layouts by id
        mLoaderLayout = (ConstraintLayout) findViewById(R.id.loader_pb);
        mPrimaryLayout = (ConstraintLayout) findViewById(R.id.primary_info);

        //find text views by id
        mCurrPriceTextView = (TextView) findViewById(R.id.current_price);
        mBuyPriceTextView = (TextView) findViewById(R.id.buy_price);
        mSellPriceTextView = (TextView) findViewById(R.id.sell_price);

        //find button by id and set on click listener
        mRedirectButton = (Button) findViewById(R.id.web_info_button);
        mRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFinanceWebsite();
            }
        });


        LoaderManager.LoaderCallbacks<BcJsonObject> callback = MainActivity.this;

        getSupportLoaderManager().initLoader(BC_LOADER_ID, null, callback);

        Log.d(TAG, "onCreate: registering preference changed listener");

    }

    @Override
    public Loader<BcJsonObject> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<com.example.android.bitcoinview.utilities.BcJsonObject>(this) {

            // BC json object that will hold the data
            BcJsonObject mBcData = null;

            @Override
            protected void onStartLoading() {
                    showLoader();
                    forceLoad();
            }

            @Override
            public BcJsonObject loadInBackground() {
                URL bcRequestUrl = NetworkUtils.buildUrl();
                try {
                    String jsonBcResponse = NetworkUtils.getResponseFromHttpUrl(bcRequestUrl);
                    BcJsonObject bcData = new BcJsonObject(jsonBcResponse);
                    return bcData;
                }
                catch (IOException|JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(BcJsonObject data) {
                mBcData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<BcJsonObject> loader, BcJsonObject data) {
        if (data == null) {
            showErrorDialog();
            mLoaderLayout.setVisibility(View.INVISIBLE);
        }
        else {
            setBcData(data);
            showPrimary();
        }

    }

    @Override
    public void onLoaderReset(Loader<BcJsonObject> loader) {
    // is not needed right now
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportLoaderManager().restartLoader(BC_LOADER_ID, null, this);
    }

    // inflates a menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }
    // handles refresh button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_refresh:
                getSupportLoaderManager().restartLoader(BC_LOADER_ID, null, this);
                return true;
            case R.id.action_about:
                Intent openAboutIntent = new Intent(this, AboutActivity.class);
                startActivity(openAboutIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method sets the text of the text views according to the data
     * stored in the BcJsonObject
     *
     * @param bcData loaded blockchain data
     *
     */
    private void setBcData(BcJsonObject bcData) {
        String symbol = " " + bcData.getSymbol();
        mCurrPriceTextView.setText(bcData.getLast() + symbol);
        mBuyPriceTextView.setText(bcData.getBuy() + symbol);
        mSellPriceTextView.setText(bcData.getSell() + symbol);
    }



    /**
     * This method will show an AlertDialog when it was not possible to load
     * the data
     * */
    private void showErrorDialog(){
        AlertDialog errorDialog = new AlertDialog.Builder(this)
                .setTitle("No Connection")
                .setMessage("The data could not be loaded. Check your internet connection and hit refresh.")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        errorDialog.show();
    }
    // sets visibilities of the constraint layouts
    private void showLoader() {
        mLoaderLayout.setVisibility(View.VISIBLE);
        mPrimaryLayout.setVisibility(View.GONE);
    }

    private void showPrimary() {
        mLoaderLayout.setVisibility(View.GONE);
        mPrimaryLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method will start a new activity where a browser is opened
     * and the user is redirected to a finance website to receive more
     * details on the current bitcoin stock price
     */
    private void openFinanceWebsite() {
        String webString = BcPreferences.getDefaultFinWeb();
        Uri website = Uri.parse(webString);
        Intent openWebpageIntent = new Intent(Intent.ACTION_VIEW, website);
        if (openWebpageIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openWebpageIntent);
        }
    }


}
