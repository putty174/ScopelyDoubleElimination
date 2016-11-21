package com.example.maxh1.scopelydoubleelimination;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static final int PICK_TEAM = 100;

    private JSONArray bracketArray = null;
    private int roundIndex = 0;
    private int teamSetIndex = 0;
    private ArrayList<ArrayList<JSONObject>> bracketRounds = new ArrayList<ArrayList<JSONObject>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeBracket();
    }

    protected void PlayButtonClick(View v) {
        if (bracketArray != null && roundIndex < bracketRounds.size() - 1) {
            String team1URL = bracketRounds.get(roundIndex).get(teamSetIndex + 0).optString("image");
            String team2URL = bracketRounds.get(roundIndex).get(teamSetIndex + 1).optString("image");

            Intent pickTeamIntent = new Intent(this, TeamSelect.class);
            pickTeamIntent.putExtra("team1logo", team1URL);
            pickTeamIntent.putExtra("team2logo", team2URL);
            startActivityForResult(pickTeamIntent, PICK_TEAM);
        }
        else {
            showResetDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_TEAM) {
            if (resultCode == RESULT_OK) {
                switch (data.getIntExtra("winningteam", 0)) {
                    case R.id.team1logo:
                        bracketRounds.get(roundIndex + 1).add(bracketRounds.get(roundIndex).get(teamSetIndex + 0));
                        teamSetIndex += 2;
                        break;
                    case R.id.team2logo:
                        bracketRounds.get(roundIndex + 1).add(bracketRounds.get(roundIndex).get(teamSetIndex + 1));
                        teamSetIndex += 2;
                        break;
                }
                int id = getResources().getIdentifier(String.format("round%d", roundIndex + 2), "id", this.getPackageName());
                TextView nextRound = (TextView) findViewById(id);
                nextRound.setText(String.format("Round%d", roundIndex + 2) + "\n");
                for (JSONObject winningTeam : bracketRounds.get(roundIndex + 1)) {
                    nextRound.setText(nextRound.getText() + winningTeam.optString("name") + "\n");
                }

                if (teamSetIndex >= bracketRounds.get(roundIndex).size()) {
                    teamSetIndex = 0;
                    roundIndex++;
                    if (roundIndex >= bracketRounds.size() - 1) {
                        showResetDialog();
                    }
                }
            }
        }
    }

    private void InitializeBracket() {
        bracketArray = null;
        roundIndex = 0;
        teamSetIndex = 0;
        bracketRounds = new ArrayList<ArrayList<JSONObject>>();

        String JSONString = "";
        URLDownload JSONfile = new URLDownload(MainActivity.this);
        try {
            JSONString = JSONfile.execute("", "", "").get();
            JSONObject bracketJSON = new JSONObject(JSONString);
            bracketArray = bracketJSON.optJSONArray("data");

            for (int roundSize = bracketArray.length(); roundSize > 0; roundSize /= 2) {
                bracketRounds.add(new ArrayList<JSONObject>(roundSize));
            }

            for (int teamIndex = 0; teamIndex < bracketArray.length(); teamIndex++) {
                bracketRounds.get(roundIndex).add(bracketArray.getJSONObject(teamIndex));
            }
            Collections.shuffle(bracketRounds.get(roundIndex));

            TextView round1 = (TextView) findViewById(R.id.round1);
            round1.setText(getString(R.string.round1) + "\n");
            TextView round2 = (TextView) findViewById(R.id.round2);
            round2.setText(getString(R.string.round2) + "\n");
            TextView round3 = (TextView) findViewById(R.id.round3);
            round3.setText(getString(R.string.round3) + "\n");
            TextView round4 = (TextView) findViewById(R.id.round4);
            round4.setText(getString(R.string.round4) + "\n");

            for (JSONObject team : bracketRounds.get(roundIndex)) {
                round1.setText(round1.getText() + team.optString("name") + "\n");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(getString(R.string.resetprompt), bracketRounds.get(roundIndex).get(0).optString("name")))
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InitializeBracket();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}
