package br.com.futusteps.survey.data.repository;


import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import br.com.futusteps.survey.SurveyApplication;
import br.com.futusteps.survey.core.login.User;


public class UserRepositoryImpl implements UserRepository{

    private User mUser;


    public void login(String email, String password, final LoginCallback callback){

        Firebase ref = new Firebase(SurveyApplication.FIREBASE_URL);
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                mUser = new User();
                mUser.setId(authData.getUid());
                mUser.setToken(authData.getToken());
                mUser.setEmail(authData.getProviderData().get("email").toString());
                mUser.setProfileImageURL(authData.getProviderData().get("profileImageURL").toString());
                callback.onLoginSuccess(mUser);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                callback.onLoginFail(firebaseError.getMessage());
            }
        });

    }

    @Override
    public void saveUser(User user) {
        if(user != null){
            mUser = user;
        }
    }

    @Override
    public User getUser() {
        return mUser;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        mUser = null;
    }

}
