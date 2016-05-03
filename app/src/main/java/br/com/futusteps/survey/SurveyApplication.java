package br.com.futusteps.survey;

import android.app.Application;

import com.firebase.client.Firebase;

public class SurveyApplication extends Application {

    public static final String FIREBASE_URL = "https://glaring-torch-1315.firebaseio.com";


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }


}
