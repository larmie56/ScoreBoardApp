package com.sgaproject.scoreboardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sgaproject.scoreboardapp.databinding.ActivityMainBinding;
import com.sgaproject.scoreboardapp.model.GoalScoredModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;

    private int GOAL_SCORED_A;
    private int GOAL_SCORED_B;
    private TextView mTextTeamAScore;
    private TextView mTextTeamBScore;
    private GoalScoredView mGoalScoredViewA;
    private GoalScoredView mGoalScoredViewB;
    private boolean mShowScores = false;

    GoalScoredModel mViewModel;
    Observer<Integer> mGoalScoredAObs = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            mTextTeamAScore.setText(String.valueOf(integer));
            GOAL_SCORED_A = integer;

        }
    };

    Observer<Integer> mGoalScoredBObs = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            mTextTeamBScore.setText(String.valueOf(integer));
            GOAL_SCORED_B = integer;
        }
    };
    private ConstraintLayout mFragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(GoalScoredModel.class);

        mTextTeamAScore = findViewById(R.id.textView_score_team_a);
        mTextTeamBScore = findViewById(R.id.textView_score_team_b);
        mGoalScoredViewA = findViewById(R.id.goal_scored_a);
        mGoalScoredViewB = findViewById(R.id.goal_scored_b);
        mFragmentContainer = mBinding.fragmentContainer;

        initializeFields();

        //setupGoalScoredView();
    }

    private void initializeFields() {
        mFragmentContainer.setVisibility(View.GONE);

        mViewModel.setScoreTeamALive();
        mViewModel.setScoreTeamBLive();

        mViewModel.getScoreTeamALive().observe(this, mGoalScoredAObs);
        mViewModel.getScoreTeamBLive().observe(this, mGoalScoredBObs);
    }

    private void setupGoalScoredView() {
        int[] goalScoredArrayA = new int[GOAL_SCORED_A];
        mGoalScoredViewA.setUpGoalScoredView(goalScoredArrayA);

        int[] goalScoredArrayB = new int[GOAL_SCORED_B];
        mGoalScoredViewB.setUpGoalScoredView(goalScoredArrayB);
    }

    public void increaseA(View view) {
        mViewModel.increaseA();

        mViewModel.getScoreTeamALive().observe(this, mGoalScoredAObs);
    }

    public void increaseB(View view) {
        mViewModel.increaseB();

        mViewModel.getScoreTeamBLive().observe(this, mGoalScoredBObs);
    }

    public void resetScores(View view) {
        mViewModel.resetScores();

        mViewModel.getScoreTeamALive().observe(this, mGoalScoredAObs);
        mViewModel.getScoreTeamBLive().observe(this, mGoalScoredBObs);
    }
    public void showHideScores(View view) {
        if (GOAL_SCORED_A == 1 || GOAL_SCORED_B == 1) {
            return;
        }
        mShowScores = !mShowScores;
        setVisibility();
        setupGoalScoredView();
    }

    private void setVisibility() {
        if (mShowScores) {
            mFragmentContainer.setVisibility(View.VISIBLE);
        }
        else {
            mFragmentContainer.setVisibility(View.GONE);
        }
    }
}