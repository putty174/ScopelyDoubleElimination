package com.example.maxh1.scopelydoubleelimination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TeamSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_select);

        Intent teamSelectIntent = getIntent();
        String team1URL = teamSelectIntent.getStringExtra("team1logo");
        String team2URL = teamSelectIntent.getStringExtra("team2logo");

        ImageButton team1Image = (ImageButton) findViewById(R.id.team1logo);
        ImageButton team2Image = (ImageButton) findViewById(R.id.team2logo);

        ImageDownload team1Thread = new ImageDownload(this, team1URL, team1Image);
        team1Thread.execute();
        ImageDownload team2Thread = new ImageDownload(this, team2URL, team2Image);
        team2Thread.execute();
    }

    protected void TeamSelect(View v) {
        Intent intentResult = new Intent();
        intentResult.putExtra("winningteam", v.getId());
        setResult(RESULT_OK, intentResult);
        finish();
    }
}
