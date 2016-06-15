package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.survey.Question;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.util.StringUtils;

public class CreateQuestionPresenter implements CreateQuestionContract.UserActionsListener{

    @NonNull
    private final CreateQuestionContract.View mLoginView;

    @NonNull
    private final SurveyRepository mRepository;

    public CreateQuestionPresenter(@NonNull CreateQuestionContract.View loginView,
                                   @NonNull SurveyRepository repository) {
        mLoginView = loginView;
        mRepository = repository;
    }

    @Override
    public void loadNextQuestion(Question question, int position, int total, String answer, List<Integer> alternatives) {

    }

}
