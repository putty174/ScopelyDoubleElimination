package com.example.maxh1.scopelydoubleelimination;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by maxh1 on 11/20/2016.
 */

public class ImageDownload extends AsyncTask<String, String, Bitmap> {
    Context currentContext = null;
    String targetURL = "";
    ImageButton targetImage = null;

    ProgressDialog dialog = null;

    protected ImageDownload(Context activity, String URL, ImageButton target) {
        this.currentContext = activity;
        this.targetURL = URL;
        this.targetImage = target;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = ProgressDialog.show(currentContext, "Please wait...", "Loading Image...", false);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream inputStream = null;
        BufferedReader inputBufferedReader = null;
        try {
            URL imageURL = new URL(targetURL);
            inputStream = (InputStream) imageURL.getContent();
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            return image;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        super.onPostExecute(image);
        targetImage.setImageBitmap(image);
        dialog.dismiss();
    }
}
