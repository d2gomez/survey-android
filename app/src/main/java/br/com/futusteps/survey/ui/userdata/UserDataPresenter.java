package br.com.futusteps.survey.ui.userdata;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.Calendar;

import br.com.futusteps.survey.core.surveranswer.UserData;
import br.com.futusteps.survey.data.repository.SurveyRepository;
import br.com.futusteps.survey.util.DateUtils;
import br.com.futusteps.survey.util.Masks;
import br.com.futusteps.survey.util.StringUtils;
import br.com.futusteps.survey.util.Validator;
import br.com.futusteps.R;

public class UserDataPresenter implements UserDataContract.UserActionsListener{

    public enum ValidationUserData {

        CPF_INVALID(R.string.error_invalid_cpf),
        BIRTH_DATE_INVALID(R.string.error_invalid_birth_date),
        PREFIX_INVALID(R.string.error_invalid_prefix),
        PHONE_INVALID(R.string.error_invalid_phone),
        PHONE_TYPE_INVALID(R.string.error_invalid_phone_type),
        SALARY_INVALID(R.string.error_invalid_salary);
        public final int mErrorMessage;

        ValidationUserData(@StringRes int errorMessage){
            mErrorMessage = errorMessage;
        }
    }

    @NonNull
    private final UserDataContract.View mUserDataView;

    @NonNull
    private final SurveyRepository mRepository;

    public UserDataPresenter(@NonNull UserDataContract.View userDataView,
                             @NonNull SurveyRepository repository) {
        mUserDataView = userDataView;
        mRepository = repository;
    }

    @Override
    public void submitSurvey(String cpf, String birthDate, String prefix,
                             String phone, int phoneType, String salary) {

        mUserDataView.clearErrors();

        Calendar bDate = DateUtils.getCalendar("dd/MM/yyyy", birthDate);

        if(!Validator.isValidCPF(Masks.unmask(cpf))){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.CPF_INVALID);
        }else if(!Validator.isValidBirthDate(bDate)){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.BIRTH_DATE_INVALID);
        }else if(StringUtils.isBlank(prefix)){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.PREFIX_INVALID);
        }else if(StringUtils.isBlank(phone) || phone.length() < 9){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.PHONE_INVALID);
        }else if(phoneType < 0){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.PHONE_TYPE_INVALID);
        }else if(StringUtils.isBlank(salary)){
            mUserDataView.showInvalidFieldErrors(ValidationUserData.SALARY_INVALID);
        }else{
            mUserDataView.showProgress(true);
            UserData userData = new UserData();
            userData.setCpf(Masks.unmask(cpf));
            userData.setPhone(prefix + phone);
            userData.setPhoneType(phoneType);
            userData.setSalary(Float.valueOf(salary));

            mRepository.setUserData(userData);

            mRepository.finishSurvey(new SurveyRepository.FinishSurveysCallback() {
                @Override
                public void onFinishSuccess() {
                    mUserDataView.showProgress(false);
                    mUserDataView.showFinishSurveySuccess();
                    mRepository.removeCurrentSurvey();
                }

                @Override
                public void onFinishFail() {
                    // show error message
                    mUserDataView.showProgress(false);
                    mUserDataView.showSubmitSurveyError(SURVEY_ERROR);
                }
            });
        }
    }

    @Override
    public void leaveSurvey() {

    }
}
