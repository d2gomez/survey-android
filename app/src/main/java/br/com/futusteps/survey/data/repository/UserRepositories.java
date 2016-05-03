package br.com.futusteps.survey.data.repository;

public class UserRepositories {

    private UserRepositories() {
        // no instance
    }

    private static UserRepository repository = null;

    public synchronized static UserRepository getInMemoryRepoInstance() {
        if (null == repository) {
            repository = new UserRepositoryImpl();
        }
        return repository;
    }
}
