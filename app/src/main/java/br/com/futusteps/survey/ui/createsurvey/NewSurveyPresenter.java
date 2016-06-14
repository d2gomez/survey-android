package br.com.futusteps.survey.ui.createsurvey;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.survey.Question;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.util.StringUtils;

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
        if(validateQuestion(question, answer, alternatives)) {
            if (question.getType().equalsIgnoreCase("text")){
                mRepository.addAnswer(question.getId(), answer, null);
            }else if(question.getType().equalsIgnoreCase("single")){
                mRepository.addAnswer(question.getId(), null, alternatives);
            }
            if (position < total - 1) {
                mLoginView.showNextQuestion(question, position, total);
            } else {
                mLoginView.showUserDataForm();
            }
        }
    }

    private boolean validateQuestion(Question question, String answer, List<Integer> alternatives){
        if (question.getType().equalsIgnoreCase("text") && StringUtils.isBlank(answer)) {
            mLoginView.showValidationError(R.string.error_empty_answer);
            return false;
        }else if(question.getType().equalsIgnoreCase("single") &&
                (alternatives == null ||
                alternatives.get(0) == null ||
                alternatives.get(0) < 1)){
            mLoginView.showValidationError(R.string.error_no_alternative);
            return false;
        }

        return true;
    }
}
