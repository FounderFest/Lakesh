package com.mezcalab.therapedic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.altbeacon.beacon.BeaconManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.jar.JarException;

public class InfoColchon extends AppCompatActivity {
    private static BeaconManager beaconManager;
    private static MainActivity mainActivity;
    private static String idBeacon;
    private GetColchon mInfo = null;
    private TextView txtConfort;
    private TextView txtModelo;
    private TextView txtCaracteristicas;
    protected static final String TAG = "InfoColchonActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_colchon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtModelo = (TextView) findViewById(R.id.txtModelo);
        txtConfort = (TextView) findViewById(R.id.txtConfort);
        txtCaracteristicas = (TextView) findViewById(R.id.txtCaracteristicas);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        showIfnfoColchon();
    }

    public static void setBeaconManager(BeaconManager bm) {
        beaconManager = bm;
    }

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    public static void setIdBeacon(String id) {
        idBeacon = id.trim();
    }

    private void showIfnfoColchon() {
        if(mInfo != null) {
            return;
        }
        mInfo = new GetColchon(idBeacon);
        mInfo.execute((Void) null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            beaconManager.bind(mainActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        beaconManager.bind(mainActivity);
        super.onBackPressed();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GetColchon extends AsyncTask<Void, Void, Boolean> {
        private final String urlEndpoint = "http://kreelcarlos.cloudapp.net:8000/getColchonById";
        private String mId;
        private JSONObject colchon;

        GetColchon(String mId) {
            this.mId = mId;
            Log.d(TAG, mId);
        }

        private JSONObject getInfoColchon() throws IOException, JSONException {
            OkHttpClient client = new OkHttpClient();
            String tmpId = mId.equals("55277") ? "Y" : "X";
            Log.d(TAG, tmpId);
            Request request = new Request.Builder()
                    .url(urlEndpoint)
                    //.addHeader("Accept", "application/json")
                    .addHeader("Beacon", tmpId)
                    .build();
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                colchon = getInfoColchon();
            }
            catch (IOException ex){
                Log.d(TAG, "ERROR DE CONEXION");
                return false;
            }
            catch (JSONException ex) {
                Log.d(TAG, "ERROR DE JSON");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mInfo = null;
            if (success) {
                try {
                    txtModelo.setText(colchon.getString("modelo"));
                    txtConfort.setText(colchon.getString("confort"));
                    txtCaracteristicas.setText(colchon.getString("caracteristicas"));
                }
                catch (JSONException ex) {
                    Log.d(TAG, "JSON mal formado...");
                }
            } else {
                Log.d(TAG,"No encontre la info :(");
            }
        }

        @Override
        protected void onCancelled() {
            mInfo = null;
            //showProgress(false);
        }
    }
}
