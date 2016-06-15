package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.StringRes;

import java.util.List;

import br.com.futusteps.survey.core.survey.Question;

public interface CreateQuestionContract {

    interface View {

        void showAddAlternatives(boolean show);

        void showAddOtherQuestion(Question question, int position, int total);

        void showValidationError(@StringRes int error);

        void showCreateSurveySuccess();

        void showCreateSurveyFail();
    }

    interface UserActionsListener {

        void addOtherQuestion();

        void finishSurvey();
    }
}
