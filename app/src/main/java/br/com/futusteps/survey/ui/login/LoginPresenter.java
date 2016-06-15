package br.com.futusteps.survey.ui.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.Collection;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.login.User;
import br.com.futusteps.survey.data.repository.UserRepository;
import br.com.futusteps.survey.util.Validator;

/**
 * Listens to user actions from the UI ({@link LoginActivity}), retrieves the data and updates
 * the UI as required.
 */
public class LoginPresenter implements LoginContract.UserActionsListener {

    public enum ValidationLogin {

        USER_INVALID(R.string.error_invalid_email),
        PASS_INVALID(R.string.error_invalid_password);

        public final int mErrorMessage;

        ValidationLogin(@StringRes int errorMessage){
            mErrorMessage = errorMessage;
        }
    }

    @NonNull
    private final LoginContract.View mLoginView;

    @NonNull
    private final UserRepository mRepository;

    public LoginPresenter(@NonNull LoginContract.View loginView,
                          @NonNull UserRepository repository){
        mLoginView = loginView;
        mRepository = repository;
    }

    @Override
    public void createUser(String user, String pass, int provider) {

        mLoginView.showProgress(true);

        // move to repository
//        Firebase ref = new Firebase(SurveyApplication.FIREBASE_URL);
//        ref.createUser(user, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
//            @Override
//            public void onSuccess(Map<String, Object> result) {
//                mLoginView.showMainScreen();
//            }
//
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                mLoginView.showProgress(false);
//                mLoginView.showLoginError(firebaseError.getMessage());
//            }
//        });
    }


    @Override
    public void login(String email, String pass) {

        mLoginView.showProgress(true);

        if(email.isEmpty() || !Validator.isValidEmail(email)){
            mLoginView.showInvalidFieldErrors(ValidationLogin.USER_INVALID);
        }else if(pass.isEmpty() || pass.length() < 4) {
            mLoginView.showInvalidFieldErrors(ValidationLogin.PASS_INVALID);
        }else{
            mLoginView.showProgress(true);
            mRepository.login(email, pass, new UserRepository.LoginCallback() {
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

    @Override
    public void loginFacebook(Activity activity) {
        mLoginView.showProgress(true);
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    @Override
    public void loginGoogle(Activity activity, GoogleApiClient googleApiClient) {
        mLoginView.showProgress(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, LoginActivity.RC_SIGN_IN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStop() {

    }
}
