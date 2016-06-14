package br.com.futusteps.survey.data.repository;

import android.support.annotation.NonNull;

import br.com.futusteps.survey.data.remote.MockService;

public class SurveyRepositories {

    private SurveyRepositories() {
        // no instance
    }

    private static SurveyRepository repository = null;

    public synchronized static SurveyRepository getInMemoryRepoInstance(@NonNull MockService service) {
        if (null == repository) {
            repository = new SurveyRepositoryImpl(service);
        }
        return repository;
    }
}
