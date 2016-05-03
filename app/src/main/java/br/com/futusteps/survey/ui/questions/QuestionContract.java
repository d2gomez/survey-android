package br.com.futusteps.survey.ui.questions;

import android.support.annotation.StringRes;

import br.com.futusteps.survey.core.survey.Question;
import java.util.List;

public interface QuestionContract {

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
