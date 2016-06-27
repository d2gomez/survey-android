package br.com.futusteps.survey.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import br.com.futusteps.survey.MainActivity;
import br.com.futusteps.survey.R;
import br.com.futusteps.survey.ui.base.BaseActivity;
import br.com.futusteps.survey.ui.login.LoginActivity;
import br.com.futusteps.survey.ui.register.RegisterActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    public static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent;
        if (auth.getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
//                            .setTheme(getSelectedTheme())
//                            .setLogo(getSelectedLogo())
                            .setProviders(getSelectedProviders())
//                            .setTosUrl(getSelectedTosUrl())
                            .build(),
                    RC_SIGN_IN);
        }else{
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @MainThread
    private String[] getSelectedProviders() {
        ArrayList<String> selectedProviders = new ArrayList<>();

        selectedProviders.add(AuthUI.EMAIL_PROVIDER);
        //selectedProviders.add(AuthUI.FACEBOOK_PROVIDER);
        selectedProviders.add(AuthUI.GOOGLE_PROVIDER);

        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }else{

        }
    }

}
