package br.com.futusteps.survey;

import android.app.Application;

import com.firebase.client.Firebase;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SurveyApplication extends Application {

    public static final String FIREBASE_URL = "https://glaring-torch-1315.firebaseio.com";


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Firebase.setAndroidContext(this);
    }


}
