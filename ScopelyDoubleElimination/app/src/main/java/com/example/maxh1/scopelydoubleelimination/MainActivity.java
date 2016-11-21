package com.example.maxh1.scopelydoubleelimination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static final int PICK_TEAM = 100;

    private JSONArray bracketArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String JSONString = "";
        URLDownload JSONfile = new URLDownload(MainActivity.this);
        try {
            JSONString = JSONfile.execute("", "", "").get();
            JSONObject bracketJSON = new JSONObject(JSONString);
            bracketArray = bracketJSON.optJSONArray("data");

            TextView team1 = (TextView) findViewById(R.id.team1);
            team1.setText("");
            for(int teamIndex = 0; teamIndex < bracketArray.length(); teamIndex++) {
                team1.setText(team1.getText() + bracketArray.getJSONObject(teamIndex).optString("name") + "\n");
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void PlayButtonClick(View v) {
        if(bracketArray != null) {
            try {
                String team1URL = bracketArray.getJSONObject(0).optString("image");
                String team2URL = bracketArray.getJSONObject(1).optString("image");

                Intent pickTeamIntent = new Intent(this, TeamSelect.class);
                pickTeamIntent.putExtra("team1logo", team1URL);
                pickTeamIntent.putExtra("team2logo", team2URL);
                startActivityForResult(pickTeamIntent, PICK_TEAM);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_TEAM) {
            if(resultCode == RESULT_OK) {
                TextView team2 = (TextView) findViewById(R.id.team2);
                team2.setText(Integer.toString(data.getIntExtra("winningteam", 0)));
            }
        }
    }
}
