package br.com.futusteps.survey.data.repository;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.futusteps.survey.core.surveranswer.Answer;
import br.com.futusteps.survey.core.surveranswer.SurveyAnswer;
import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.remote.MockService;

public class SurveyRepositoryImpl implements SurveyRepository {

    private final MockService mMockService;
    private Survey mCurrentSurvey;
    private SurveyAnswer mSurveyAnswer;
    private FirebaseDatabase database;

    public SurveyRepositoryImpl(MockService mockService) {
        mMockService = mockService;
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void surveys(@NonNull final String id, @NonNull String token, final SurveysCallback callback) {
        final List<Survey> surveys = new ArrayList<>();
        final DatabaseReference ref = database.getReference("userSurvey");
        final DatabaseReference surveysRef = database.getReference("surveys");
        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    callback.onSurveySuccess(surveys);
                }else{
                    ref.child(id).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue() != null && (Boolean) dataSnapshot.getValue()) {
                                surveysRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Survey survey = snapshot.getValue(Survey.class);
                                        survey.setId(Integer.valueOf(dataSnapshot.getKey()));
                                        surveys.add(survey);
                                        callback.onSurveySuccess(surveys);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        callback.onSurveyFail();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            callback.onSurveyFail();
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            callback.onSurveyFail();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            callback.onSurveyFail();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onSurveyFail();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onSurveyFail();
            }
        });

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
    public void finishSurvey(FinishSurveysCallback callback) {
        if (mSurveyAnswer != null) {
            DatabaseReference answersRef = database.getReference("surveyAnswers");
            answersRef.push().setValue(mSurveyAnswer);
            //TODO check how to deal with error
            callback.onFinishSuccess();
            //callback.onFinishFail();
        }
    }
}
