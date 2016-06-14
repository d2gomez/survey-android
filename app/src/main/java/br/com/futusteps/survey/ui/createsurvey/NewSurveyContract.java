package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.StringRes;

import java.util.List;

import br.com.futusteps.survey.core.survey.Question;

public interface NewSurveyContract {

    interface View {

        void showQuestion(Question question);

        void showNextQuestion(Question question, int position, int total);

        void showUserDataForm();

        void showValidationError(@StringRes int error);
    }

    interface UserActionsListener {

        void loadNextQuestion(Question question, int position, int total, String answer, List<Integer> alternatives);
    }
}
