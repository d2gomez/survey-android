package br.com.futusteps.survey.data.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.futusteps.survey.core.surveranswer.Answer;
import br.com.futusteps.survey.core.surveranswer.SurveyAnswer;
import br.com.futusteps.survey.core.surveranswer.UserData;
import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.remote.MockService;

public class SurveyRepositoryMockImpl implements SurveyRepository{

    private final MockService mMockService;
    private Survey mCurrentSurvey;
    private SurveyAnswer mSurveyAnswer;

    public SurveyRepositoryMockImpl(MockService mockService){
        mMockService = mockService;
    }

    @Override
    public void surveys(@NonNull String user, @NonNull String token, SurveysCallback callback) {
        callback.onSurveySuccess(mMockService.getSurveys(user, token));
        //callback.onSurveyFail();
    }

    @Override
    public void setCurrentSurvey(@NonNull Survey survey){
        mCurrentSurvey = survey;
        mSurveyAnswer = new SurveyAnswer();
        mSurveyAnswer.setSurveyId(mCurrentSurvey.getId());
    }

    @Override
    public Survey getCurrentSurvey() {
        return mCurrentSurvey;
    }


    @Override
    public void removeCurrentSurvey() {
        mCurrentSurvey = null;
        mSurveyAnswer = null;
    }

    @Override
    public void addAnswer(int idQuestion, String answerText, List<Integer> alternatives) {
        if(mSurveyAnswer.getAnswers() == null){
            mSurveyAnswer.setAnswers(new ArrayList<Answer>());
        }
        Answer answer = new Answer();
        answer.setIdQuestion(idQuestion);
        answer.setAnswerText(answerText);
        answer.setAlternatives(alternatives);
        mSurveyAnswer.getAnswers().add(answer);
    }

    @Override
    public SurveyAnswer getSurveyAnswer() {
        return mSurveyAnswer;
    }

    @Override
    public void setUserData(UserData userData) {
        if(mSurveyAnswer != null){
            mSurveyAnswer.setUserData(userData);
        }
    }

    @Override
    public void finishSurvey(FinishSurveysCallback callback) {
        if (mSurveyAnswer != null) {
            mMockService.saveSurvey(mSurveyAnswer);
            callback.onFinishSuccess();
            //callback.onFinishFail();
        }
    }
}
