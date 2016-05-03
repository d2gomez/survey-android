package br.com.futusteps.survey.ui.surveylist;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.futusteps.survey.core.survey.Survey;

public class SurveyListContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showSurveys(List<Survey> surveys);

        void showSurvey();

        void showError();
    }

    interface UserActionsListener {

        void loadSurveys(boolean forceUpdate);

        void startSurvey(@NonNull Survey requestedSurvey);
    }
}
