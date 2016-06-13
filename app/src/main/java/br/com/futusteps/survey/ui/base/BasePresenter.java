package br.com.futusteps.survey.ui.base;

import android.os.Bundle;

public interface BasePresenter {

    void onCreate(Bundle savedInstanceState);

    void onStop();
}
