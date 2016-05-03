package br.com.futusteps.survey.ui.surveylist;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.futusteps.survey.core.survey.Survey;
import br.com.futusteps.survey.data.repository.SurveyRepository;

public class SurveyListPresenter implements SurveyListContract.UserActionsListener{

    @NonNull
    private final SurveyListContract.View mView;

    @NonNull
    private final SurveyRepository mRepository;

    public SurveyListPresenter(@NonNull SurveyListContract.View view,
                                @NonNull SurveyRepository repository){
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadSurveys(boolean forceUpdate) {
        mView.setProgressIndicator(true);
//        if (forceUpdate) {
//            mRepository.refreshData();
//        }

        mRepository.surveys("", "", new SurveyRepository.SurveysCallback() {
            @Override
            public void onSurveySuccess(List<Survey> surveys) {
                mView.setProgressIndicator(false);
                mView.showSurveys(surveys);
            }

            @Override
            public void onSurveyFail() {
                mView.setProgressIndicator(false);
                mView.showError();
            }
        });

    }

    @Override
    public void startSurvey(@NonNull Survey requestedSurvey) {
        mRepository.setCurrentSurvey(requestedSurvey);
        mView.showSurvey();
    }
}
