package br.com.futusteps.survey.ui.register;


import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.SurveyApplication;
import br.com.futusteps.survey.core.login.User;
import br.com.futusteps.survey.data.repository.UserRepository;
import br.com.futusteps.survey.util.Validator;

/**
 * Listens to user actions from the UI ({@link RegisterActivity}), retrieves the data and updates
 * the UI as required.
 */
public class RegisterPresenter implements RegisterContract.UserActionsListener {

    public enum ValidationLogin {

        USER_INVALID(R.string.error_invalid_email),
        PASS_INVALID(R.string.error_invalid_password);

        public final int mErrorMessage;

        ValidationLogin(@StringRes int errorMessage){
            mErrorMessage = errorMessage;
        }
    }

    @NonNull
    private final RegisterContract.View mLoginView;

    @NonNull
    private final UserRepository mRepository;

    public RegisterPresenter(@NonNull RegisterContract.View loginView,
                             @NonNull UserRepository repository){
        mLoginView = loginView;
        mRepository = repository;
    }


    @Override
    public void createUser(String user, String pass, int provider) {

        mLoginView.showProgress(true);

        // move to repository
        Firebase ref = new Firebase(SurveyApplication.FIREBASE_URL);
        ref.createUser(user, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mLoginView.showMainScreen();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mLoginView.showProgress(false);
                mLoginView.showLoginError(firebaseError.getMessage());
            }
        });
    }


    @Override
    public void login(String email, String pass, int provider) {

        mLoginView.showProgress(true);

        if(email.isEmpty() || !Validator.isValidEmail(email)){
            mLoginView.showInvalidFieldErrors(ValidationLogin.USER_INVALID);
        }else if(pass.isEmpty() || pass.length() < 4) {
            mLoginView.showInvalidFieldErrors(ValidationLogin.PASS_INVALID);
        }else{
            mLoginView.showProgress(true);
            mRepository.login(email, pass, provider, new UserRepository.LoginCallback() {
                @Override
                public void onLoginSuccess(User user) {
                    mLoginView.showMainScreen();
                }

                @Override
                public void onLoginFail(String error) {
                    mLoginView.showProgress(false);
                    mLoginView.showLoginError(error);
                }
            });
        }
    }
}
