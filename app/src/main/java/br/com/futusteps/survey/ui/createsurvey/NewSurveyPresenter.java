package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.futusteps.survey.core.survey.Question;
import br.com.futusteps.survey.data.repository.SurveyRepository;

public class NewSurveyPresenter implements NewSurveyContract.UserActionsListener{

    @NonNull
    private final NewSurveyContract.View mLoginView;

    @NonNull
    private final SurveyRepository mRepository;

    public NewSurveyPresenter(@NonNull NewSurveyContract.View view,
                              @NonNull SurveyRepository repository) {
        mLoginView = view;
        mRepository = repository;
    }

    @Override
    public void loadNextQuestion(Question question, int position, int total, String answer, List<Integer> alternatives) {

    }


}
