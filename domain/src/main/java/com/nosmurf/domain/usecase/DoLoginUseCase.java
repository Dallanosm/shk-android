package com.nosmurf.domain.usecase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Sergio on 01/12/2016.
 */

public class DoLoginUseCase extends UseCase {

    private final Repository repository;

    private GoogleSignInAccount account;

    @Inject
    public DoLoginUseCase(Repository repository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.repository = repository;
    }

    public void execute(GoogleSignInAccount account, Subscriber<Void> subscriber){
        this.account = account;

        super.execute(subscriber);
    }

    @Override
    protected Observable getObservable() {
        return repository.doLogin(account);
    }
}