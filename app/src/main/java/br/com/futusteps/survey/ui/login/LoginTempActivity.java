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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.futusteps.survey.MainActivity;
import br.com.futusteps.survey.R;
import br.com.futusteps.survey.core.login.User;
import br.com.futusteps.survey.data.repository.UserRepositories;
import br.com.futusteps.survey.data.repository.UserRepository;
import br.com.futusteps.survey.ui.base.BaseActivity;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginTempActivity extends BaseActivity implements LoginContract.View,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 9001;

    private static final String TAG = LoginTempActivity.class.getSimpleName();
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

    private GoogleApiClient googleApiClient;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dependencyInjection();
        setActionBarTitle(R.string.title_activity_login);
        prepareView();
    }

    private void dependencyInjection() {
        repository = UserRepositories.getInMemoryRepoInstance();
        actionsListener = new LoginPresenter(this, repository);
        prepareFacebookSdk();
        prepareGoogleAuth();
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
                            passwordEdt.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void prepareFacebookSdk() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "FacebookCallback - onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("exception", exception.toString());
                        Toast.makeText(LoginTempActivity.this, R.string.error_try_again,
                                Toast.LENGTH_LONG).show();
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
            }
        };
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginTempActivity.this, R.string.error_auth,
                                    Toast.LENGTH_SHORT).show();
                            showMainScreen();
                        }else{
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User();
                            user.setDisplayName(firebaseUser.getDisplayName());
                            user.setEmail(firebaseUser.getEmail());
                            user.setId(firebaseUser.getUid());
                            if(firebaseUser.getPhotoUrl() != null){
                                user.setProfileImageURL(firebaseUser.getPhotoUrl().toString());
                            }
                            user.setToken(user.getToken());
                            repository.saveUser(user);
                            showMainScreen();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginTempActivity.this, R.string.error_auth,
                                    Toast.LENGTH_SHORT).show();
                            showMainScreen();
                        }else{
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User();
                            user.setDisplayName(firebaseUser.getDisplayName());
                            user.setEmail(firebaseUser.getEmail());
                            user.setId(firebaseUser.getUid());
                            if(firebaseUser.getPhotoUrl() != null){
                                user.setProfileImageURL(firebaseUser.getPhotoUrl().toString());
                            }
                            user.setToken(user.getToken());
                            repository.saveUser(user);
                            showMainScreen();
                        }
                    }
                });
    }

    @OnClick(R.id.signIn)
    public void doLogin() {
        actionsListener.login(
                userNameEdt.getText().toString(),
                passwordEdt.getText().toString());
    }

    @OnClick(R.id.facebookBtn)
    public void doLoginFacebook() {
        actionsListener.loginFacebook(this);
    }

    @OnClick(R.id.googleBtn)
    public void doLoginGoogle() {
        actionsListener.loginGoogle(this, googleApiClient);
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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.error_google_play, Toast.LENGTH_SHORT).show();
    }
}

