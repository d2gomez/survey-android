package br.com.futusteps.survey.ui.createsurvey;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.ui.base.BaseFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSurveyFragment extends BaseFragment implements NewSurveyContract.View {

    @Bind(R.id.surveyName)
    protected EditText surveyName;

    private NewSurveyContract.UserActionsListener actionsListener;
    private SurveyRepository mRepository;

    public static NewSurveyFragment newInstance() {
        NewSurveyFragment fragmentFirst = new NewSurveyFragment();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dependencyInjection();


        setActionBarTitle(getString(R.string.survey_details));
    }

    private void dependencyInjection() {
        MockService service = MockService.Builder.build();
        mRepository = SurveyRepositories.getInMemoryRepoInstance(service);
        actionsListener = new NewSurveyPresenter(this, mRepository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_survey, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void showAddQuestion() {

    }

    @Override
    public void showValidationError(@StringRes int error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.addQuestionButton)
    protected void clickNextButton() {
        replaceFragment(R.id.mainLayout, CreateQuestionFragment.newInstance(0));

    }


    protected void replaceFragment(@IdRes int container, Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit);
        transaction.replace(container, fragment);
        transaction.commit();
    }

}