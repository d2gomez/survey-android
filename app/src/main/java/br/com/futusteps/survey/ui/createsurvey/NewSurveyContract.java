package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.StringRes;

public interface NewSurveyContract {

    interface View {

        void showAddQuestion();

        void showValidationError(@StringRes int error);
    }

    interface UserActionsListener {

        void addQuestion(String surveyName, String surveyDescription);
    }
}
