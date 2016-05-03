package br.com.futusteps.survey.ui.userdata;

public class UserDataContract {

    interface View {
        void showSubmitSurveyError(int errorCode);
        void showInvalidFieldErrors(UserDataPresenter.ValidationUserData validationError);
        void showProgress(boolean show);
        void showPhoneTypeDialog();
        void showFinishSurveySuccess();
        void showLeaveSurveyDialog();
        void clearErrors();
    }

    interface UserActionsListener {

        int SURVEY_ERROR = 1;

        void submitSurvey(String cpf, String birthDate,
                          String prefix, String phone,
                          int phoneType, String salary);
        void leaveSurvey();
    }
}
