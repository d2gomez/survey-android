package br.com.futusteps.survey.ui.createsurvey;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.survey.Question;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.ui.base.BaseFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateQuestionFragment extends BaseFragment implements CreateQuestionContract.View {

    private static final String PARAM_POSITION = "PARAM_POSITION";

    private Question mQuestion;
    private int mPosition;

    private CreateQuestionContract.UserActionsListener mActionsListener;
    private SurveyRepository mRepository;

    public static CreateQuestionFragment newInstance(int position) {
        CreateQuestionFragment fragmentFirst = new CreateQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POSITION, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(PARAM_POSITION);
        dependencyInjection();


        setActionBarTitle("");
    }

    private void dependencyInjection() {
        MockService service = MockService.Builder.build();
        mRepository = SurveyRepositories.getInMemoryRepoInstance(service);
        mActionsListener = new CreateQuestionPresenter(this, mRepository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_question, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void showQuestion(Question question) {
    }

    @Override
    public void showNextQuestion(Question question, int position, int total) {
    }

    @Override
    public void showValidationError(@StringRes int error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.newQuestionButton)
    protected void clickNextButton() {
    }


    protected void replaceFragment(@IdRes int container, Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit);
        transaction.replace(container, fragment);
        transaction.commit();
    }

}