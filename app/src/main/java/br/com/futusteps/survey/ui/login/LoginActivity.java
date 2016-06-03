package br.com.futusteps.survey.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collection;

import br.com.futusteps.survey.MainActivity;
import br.com.futusteps.survey.R;
import br.com.futusteps.survey.data.repository.UserRepositories;
import br.com.futusteps.survey.data.repository.UserRepository;
import br.com.futusteps.survey.ui.base.BaseActivity;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Bind(R.id.user_name)
    protected EditText userNameEdt;

    @Bind(R.id.password)
    protected EditText passwordEdt;

    @Bind(R.id.login_progress)
    protected View progressView;

    @Bind(R.id.login_form)
    protected View loginFormView;

    @Bind(R.id.userTil)
    protected TextInputLayout userTil;

    @Bind(R.id.passwordTil)
    protected TextInputLayout passwordTil;

    private LoginContract.UserActionsListener actionsListener;

    private CallbackManager callbackManager;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dependencyInjection();
        setActionBarTitle(R.string.title_activity_login);
        prepareView();
        prepareFacebookSdk();
    }

    private void dependencyInjection() {
        UserRepository repository = UserRepositories.getInMemoryRepoInstance();
        actionsListener = new LoginPresenter(this, repository);
    }

    private void prepareView() {
        //FIXME: just for test
        userNameEdt.setText("doug.marques@gmail.com");
        passwordEdt.setText("teste1234");
        //

        passwordEdt = (EditText) findViewById(R.id.password);
        passwordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    userNameEdt.setError(null);
                    passwordEdt.setError(null);

                    actionsListener.login(
                            userNameEdt.getText().toString(),
                            passwordEdt.getText().toString(),
                            0);
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareFacebookSdk() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("facebook", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.e("exception","");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("exception",exception.toString());
//                        Toast.makeText(LoginActivity.this, R.string.error_try_again,
//                                Toast.LENGTH_LONG).show();
                    }
                });


        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                }
                // ...

            }
        };
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("hfat", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("hfat - o", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("hfat - o is", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @OnClick(R.id.signIn)
    public void doLogin() {
        actionsListener.login(
                userNameEdt.getText().toString(),
                passwordEdt.getText().toString(),
                0);
    }

    @OnClick(R.id.facebookBtn)
    public void doLoginFacebook() {
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    @Override
    public void showProgress(final boolean show) {
        showProgress(show, loginFormView, progressView);
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInvalidFieldErrors(LoginPresenter.ValidationLogin validation) {
        switch (validation) {
            case USER_INVALID:
                userTil.setError(getString(validation.mErrorMessage));
                break;
            case PASS_INVALID:
                passwordTil.setError(getString(validation.mErrorMessage));
                break;
            default:
                break;
        }
    }


    @Override
    public void showTerms() {

    }

    @Override
    public void showMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

