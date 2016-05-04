package br.com.futusteps.survey.ui.questions;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.futusteps.R;
import br.com.futusteps.survey.core.survey.Alternative;
import br.com.futusteps.survey.core.survey.Question;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.ui.base.BaseFragment;
import br.com.futusteps.survey.ui.userdata.UserDataFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionFragment extends BaseFragment implements QuestionContract.View {

    private static final String PARAM_POSITION = "PARAM_POSITION";
    private static final String PARAM_TOTAL = "PARAM_TOTAL";

    private Question mQuestion;
    private int mPosition;
    private int mTotal;

    @Bind(R.id.answerLayout)
    protected LinearLayout mAnswerLayout;

    @Bind(R.id.description)
    protected TextView mDescription;

    private QuestionContract.UserActionsListener mActionsListener;
    private SurveyRepository mRepository;

    public static QuestionFragment newInstance(int position, int total) {
        QuestionFragment fragmentFirst = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_POSITION, position);
        args.putInt(PARAM_TOTAL, total);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(PARAM_POSITION);
        mTotal = getArguments().getInt(PARAM_TOTAL);
        dependencyInjection();

        mQuestion = mRepository.getCurrentSurvey().getQuestions().get(mPosition);

        setActionBarTitle(getString(R.string.title_fragment_question, mQuestion.getOrder()));
    }

    private void dependencyInjection() {
        MockService service = MockService.Builder.build();
        mRepository = SurveyRepositories.getInMemoryRepoInstance(service);
        mActionsListener = new QuestionPresenter(this, mRepository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);

        String description = mQuestion.getOrder() + " - " + mQuestion.getDescription();
        mDescription.setText(description);

        showQuestion(mQuestion);

        return view;
    }

    @Override
    public void showQuestion(Question question) {
        if (question.getType().equals("text")) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText answer = new EditText(getContext());
            answer.setTag("text");
            answer.setLayoutParams(lParams);
            answer.setHint(R.string.anser_hint);

            mAnswerLayout.addView(answer);
        } else if (question.getType().equals("single")) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            RadioGroup group = new RadioGroup(getContext());
            group.setTag("single");
            group.setLayoutParams(lParams);

            for (Alternative alternative : question.getAlternatives()) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(alternative.getDescription());
                radioButton.setId(alternative.getId());
                radioButton.setLayoutParams(lParams);

                group.addView(radioButton);
            }
            mAnswerLayout.addView(group);
        }
    }

    @Override
    public void showNextQuestion(Question question, int position, int total) {
        replaceFragment(R.id.mainLayout, QuestionFragment.newInstance(mPosition + 1, mTotal));
    }

    @Override
    public void showUserDataForm() {
        replaceFragment(R.id.mainLayout, UserDataFragment.newInstance());
    }

    @Override
    public void showValidationError(@StringRes int error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.nextButton)
    protected void clickNextButton() {
        if (mQuestion.getType().equals("text")) {
            EditText editText = (EditText) mAnswerLayout.findViewWithTag("text");

            mActionsListener.loadNextQuestion(
                                            mQuestion,
                                            mPosition,
                                            mTotal,
                                            editText.getText().toString(),
                                            null);
        } else if (mQuestion.getType().equals("single")) {
            RadioGroup radioGroup = (RadioGroup) mAnswerLayout.findViewWithTag("single");
            final Integer selectedAlt = radioGroup.getCheckedRadioButtonId();
            mActionsListener.loadNextQuestion(
                    mQuestion,
                    mPosition,
                    mTotal,
                    null,
                    new ArrayList<Integer>(){{add(selectedAlt);}});

        }
    }


    protected void replaceFragment(@IdRes int container, Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit);
        transaction.replace(container, fragment);
        transaction.commit();
    }

}