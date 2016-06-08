package br.com.futusteps.survey.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import br.com.futusteps.survey.R;
import br.com.futusteps.survey.ui.base.BaseActivity;
import br.com.futusteps.survey.ui.login.LoginActivity;
import br.com.futusteps.survey.ui.register.RegisterActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    @OnClick(R.id.signInBtn)
//    public void doSignIn(){
//        startActivity(new Intent(this, LoginActivity.class));
//    }
//
//    @OnClick(R.id.registerBtn)
//    public void doRegistration(){
//        startActivity(new Intent(this, RegisterActivity.class));
//    }

}
