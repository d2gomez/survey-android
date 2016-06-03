package br.com.futusteps.survey.ui.userdata;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.data.remote.MockService;
import br.com.futusteps.survey.data.repository.SurveyRepositories;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.ui.base.BaseFragment;
import br.com.futusteps.survey.ui.view.Alert;
import br.com.futusteps.survey.util.Masks;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDataFragment extends BaseFragment implements UserDataContract.View {

    @Bind(R.id.cpf)
    EditText cpfEdt;

    @Bind(R.id.birthDate)
    EditText birthDateEdt;

    @Bind(R.id.prefix)
    EditText prefixEdt;

    @Bind(R.id.phone)
    EditText phoneEdt;

    @Bind(R.id.phoneType)
    EditText phoneTypeEdt;

    @Bind(R.id.salary)
    EditText salaryEdt;

    @Bind(R.id.mainContainer)
    View mainContainer;

    @Bind(R.id.progress)
    View progress;

    @Bind(R.id.cpfTil)
    TextInputLayout cpfTil;

    @Bind(R.id.birthDateTil)
    TextInputLayout birthDateTil;

    @Bind(R.id.prefixTil)
    TextInputLayout prefixTil;

    @Bind(R.id.phoneTil)
    TextInputLayout phoneTil;

    @Bind(R.id.phoneTypeTil)
    TextInputLayout phoneTypeTil;

    @Bind(R.id.salaryTil)
    TextInputLayout salaryTil;

    private UserDataPresenter actionsListener;

    public static UserDataFragment newInstance() {
        Bundle args = new Bundle();
        UserDataFragment fragment = new UserDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dependencyInjection();

        setActionBarTitle(getString(R.string.title_user_data_question));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        ButterKnife.bind(this, view);

        cpfEdt.addTextChangedListener(Masks.insertCPFMask(cpfEdt));
        birthDateEdt.addTextChangedListener(Masks.insert("##/##/####", birthDateEdt));
        phoneEdt.addTextChangedListener(Masks.insertPhoneMask(phoneEdt));

        return view;
    }

    @Override
    public void showSubmitSurveyError(int errorCode) {
        if (errorCode == UserDataContract.UserActionsListener.SURVEY_ERROR) {
            Toast.makeText(getContext(), R.string.error_surveys, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showInvalidFieldErrors(UserDataPresenter.ValidationUserData validationError) {
        switch (validationError) {
            case CPF_INVALID:
                cpfTil.setError(getString(validationError.mErrorMessage));
                break;
            case BIRTH_DATE_INVALID:
                birthDateTil.setError(getString(validationError.mErrorMessage));
                break;
            case PREFIX_INVALID:
                prefixTil.setError(getString(validationError.mErrorMessage));
                break;
            case PHONE_INVALID:
                phoneTil.setError(getString(validationError.mErrorMessage));
                break;
            case PHONE_TYPE_INVALID:
                phoneTypeTil.setError(getString(validationError.mErrorMessage));
                break;
            case SALARY_INVALID:
                salaryTil.setError(getString(validationError.mErrorMessage));
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        showProgress(show, mainContainer, progress);
    }


    @Override
    public void showPhoneTypeDialog() {
        final String[] types = getResources().getStringArray(R.array.phone_types);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.select_type));
        builder.setItems(types, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                phoneTypeEdt.setText(types[item]);
            }
        });
        builder.create().show();
    }

    @Override
    public void showLeaveSurveyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.leave_survey));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.create().show();
    }

    @Override
    public void showFinishSurveySuccess() {
        Alert.showMessage(getContext(),
                getString(R.string.finish_survey),
                getString(R.string.success_finish_survey),
                getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void clearErrors() {
        cpfTil.setError(null);
        birthDateTil.setError(null);
        prefixTil.setError(null);
        phoneTil.setError(null);
        phoneTypeTil.setError(null);
        salaryTil.setError(null);
    }

    @OnClick(R.id.finishButton)
    void clickButtonFinish() {

        int phoneType = Arrays.asList(getResources().getStringArray(R.array.phone_types)).indexOf(phoneTypeEdt.getText().toString());

        actionsListener.submitSurvey(
                cpfEdt.getText().toString(),
                birthDateEdt.getText().toString(),
                prefixEdt.getText().toString(),
                phoneEdt.getText().toString(),
                phoneType,
                salaryEdt.getText().toString());

    }

    @OnClick(R.id.phoneType)
    void clickPhoneType() {
        showPhoneTypeDialog();
    }


    private void dependencyInjection() {
        MockService mockService = MockService.Builder.build();
        SurveyRepository repository = SurveyRepositories.getInMemoryRepoInstance(mockService);
        actionsListener = new UserDataPresenter(this, repository);
    }

}
