package com.example.maxh1.scopelydoubleelimination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private JSONObject bracketJSON = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String JSONString = "";
        URLDownload JSONfile = new URLDownload(MainActivity.this);
        try {
            JSONString = JSONfile.execute("", "", "").get();
            bracketJSON = new JSONObject(JSONString);
            JSONArray teamArray = bracketJSON.optJSONArray("data");

            TextView team1 = (TextView) findViewById(R.id.team1);
            team1.setText("");
            for(int teamIndex = 0; teamIndex < teamArray.length(); teamIndex++) {
                team1.setText(team1.getText() + teamArray.getJSONObject(teamIndex).optString("name") + "\n");
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
