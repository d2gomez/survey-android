package br.com.futusteps.survey.data.repository;

import android.support.annotation.NonNull;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.futusteps.survey.SurveyApplication;
import br.com.futusteps.survey.core.surveranswer.Answer;
import br.com.futusteps.survey.core.surveranswer.SurveyAnswer;
import br.com.futusteps.survey.core.surveranswer.UserData;
import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.remote.MockService;

public class SurveyRepositoryImpl implements SurveyRepository {

    private final MockService mMockService;
    private Survey mCurrentSurvey;
    private SurveyAnswer mSurveyAnswer;

    public SurveyRepositoryImpl(MockService mockService) {
        mMockService = mockService;
    }

    @Override
    public void surveys(@NonNull String id, @NonNull String token, final SurveysCallback callback) {
        final List<Survey> surveys = new ArrayList<>();
        final Firebase ref = new Firebase(SurveyApplication.FIREBASE_URL);
        ref.child("userSurvey/" + id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null && (Boolean) dataSnapshot.getValue()) {
                    ref.child("surveys/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Survey survey = snapshot.getValue(Survey.class);
                            survey.setId(Integer.valueOf(dataSnapshot.getKey()));
                            surveys.add(survey);
                            callback.onSurveySuccess(surveys);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // ignore
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(final DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null && (Boolean) dataSnapshot.getValue()) {
                    ref.child("surveys/" + dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Survey survey = snapshot.getValue(Survey.class);
                            survey.setId(Integer.valueOf(dataSnapshot.getKey()));
                            int index = surveys.indexOf(survey);
                            if (index > -1) {
                                surveys.add(index, survey);
                                callback.onSurveySuccess(surveys);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // ignore
                        }
                    });
                }
            }

            @Override
            public void onChildRemoved (DataSnapshot dataSnapshot){
                //callback.onSurveySuccess(null);
            }

            @Override
            public void onChildMoved (DataSnapshot dataSnapshot, String s){

            }

            @Override
            public void onCancelled (FirebaseError firebaseError){
                //callback.onSurveySuccess(null);
            }
    }

    );
}

    @Override
    public void setCurrentSurvey(@NonNull Survey survey) {
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
        if (mSurveyAnswer.getAnswers() == null) {
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
        if (mSurveyAnswer != null) {
            mSurveyAnswer.setUserData(userData);
        }
    }

    @Override
    public void finishSurvey(FinishSurveysCallback callback) {
        if (mSurveyAnswer != null) {

            final Firebase ref = new Firebase(SurveyApplication.FIREBASE_URL);
            Firebase answersRef = ref.child("surveyAnswers");
            answersRef.push().setValue(mSurveyAnswer);

            callback.onFinishSuccess();
            //callback.onFinishFail();
        }
    }
}
