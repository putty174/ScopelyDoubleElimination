package com.example.maxh1.scopelydoubleelimination;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by maxh1 on 11/20/2016.
 */

public class URLDownload extends AsyncTask<String, String, String> {
    MainActivity mainActivity;
    String JSONString = "";
    ProgressDialog dialog = null;


    protected URLDownload(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = ProgressDialog.show(mainActivity, "Please Wait...", "Loading proecompiled list...", false);
    }

    @Override
    protected String doInBackground(String... params){
        InputStream inputStream = null;
        BufferedReader inputBufferedReader = null;
        try {
            URL targetURL = new URL("https://s3.amazonaws.com/misc-withbuddies.com/ClientChallenge/client-data-file.json");
            inputStream = new BufferedInputStream(targetURL.openStream());

            StringBuffer inputStringBuffer = new StringBuffer();
            inputBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String incomingString = "";
            while((incomingString = inputBufferedReader.readLine()) != null) {
                inputStringBuffer = inputStringBuffer.append(incomingString + "\n");
            }

            JSONString = inputStringBuffer.toString();

            if(dialog != null) {
                dialog.dismiss();
            }
            return JSONString;
        }
        catch(MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(inputBufferedReader != null) {
                try {
                    inputBufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
