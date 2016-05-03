package br.com.futusteps.survey.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.futusteps.R;
import br.com.futusteps.survey.MainActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dependencyInjection();
        setActionBarTitle(R.string.title_activity_login);
        prepareView();
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

    @OnClick(R.id.signIn)
    public void doLogin() {
        actionsListener.login(
                userNameEdt.getText().toString(),
                passwordEdt.getText().toString(),
                0);
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
}

