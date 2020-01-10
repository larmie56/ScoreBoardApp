package com.sgaproject.scoreboardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int GOAL_SCORED = 5;
    private TextView mTextTeamAScore;
    private TextView mTextTeamBScore;
    private static final String TEAM_A_SCORE = "com.sgaproject.scoreboardapp.TEAM_A_SCORE";
    private static final String TEAM_B_SCORE = "com.sgaproject.scoreboardapp.TEAM_B_SCORE";
    private GoalScoredView mGoalScoredViewA;
    private GoalScoredView mGoalScoredViewB;
    private int mScoreA = 1;
    private int mScoreB = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextTeamAScore = findViewById(R.id.textView_score_team_a);
        mTextTeamBScore = findViewById(R.id.textView_score_team_b);
        mGoalScoredViewA = findViewById(R.id.goal_scored_a);
        mGoalScoredViewB = findViewById(R.id.goal_scored_b);

        initializeFields();

        setupGoalScoredView();
    }

    private void initializeFields() {
        mTextTeamAScore.setText(String.valueOf(mScoreA));
        mTextTeamBScore.setText(String.valueOf(mScoreB));
    }

    private void setupGoalScoredView() {
        int[] goalScoredArrayA = new int[GOAL_SCORED];
        mGoalScoredViewA.setUpGoalScoredView(goalScoredArrayA);

        int[] goalScoredArrayB = new int[GOAL_SCORED];
        mGoalScoredViewB.setUpGoalScoredView(goalScoredArrayB);
    }

    public void increaseA(View view) {
        int currentScore = Integer.parseInt((mTextTeamAScore.getText().toString()));
        mScoreA = ScoreUtil.increaseScore(currentScore);
        mTextTeamAScore.setText(String.valueOf(mScoreA));
    }

    public void increaseB(View view) {
        int currentScore = Integer.parseInt(mTextTeamBScore.getText().toString());
        mScoreB = ScoreUtil.increaseScore(currentScore);
        mTextTeamBScore.setText(String.valueOf(mScoreB));
    }

    public void resetScores(View view) {
        mTextTeamAScore.setText(String.valueOf(1));
        mTextTeamBScore.setText(String.valueOf(1));

        mScoreA = 1;
        mScoreB = 1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TEAM_A_SCORE,mScoreA);
        outState.putInt(TEAM_B_SCORE, mScoreB);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mScoreA = savedInstanceState.getInt(TEAM_A_SCORE);
        mScoreB = savedInstanceState.getInt(TEAM_B_SCORE);

        initializeFields();
    }
}