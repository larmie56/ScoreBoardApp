package com.sgaproject.scoreboardapp.model;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sgaproject.scoreboardapp.util.ScoreUtil;

public class GoalScoredModel extends ViewModel {

    private int mScoreA = 1;
    private int mScoreB = 1;

    private MutableLiveData<Integer> scoreTeamALive = new MutableLiveData<>();
    private MutableLiveData<Integer> scoreTeamBLive = new MutableLiveData<>();

    public LiveData<Integer> getScoreTeamALive() {
        return scoreTeamALive;
    }

    public void setScoreTeamALive() {
        scoreTeamALive.setValue(mScoreA);
    }

    public LiveData<Integer> getScoreTeamBLive() {
        return scoreTeamBLive;
    }

    public void setScoreTeamBLive() {
        scoreTeamBLive.setValue(mScoreB);
    }

    public void increaseA() {
        int currentScore = mScoreA;
        mScoreA = ScoreUtil.increaseScore(currentScore);

        scoreTeamALive.setValue(mScoreA);
    }

    public void increaseB() {
        int currentScore = mScoreB;
        mScoreB = ScoreUtil.increaseScore(currentScore);

        scoreTeamBLive.setValue(mScoreB);
    }

    public void resetScores() {
        mScoreA = 1;
        mScoreB = 1;

        scoreTeamALive.setValue(mScoreA);
        scoreTeamBLive.setValue(mScoreB);
    }

}
