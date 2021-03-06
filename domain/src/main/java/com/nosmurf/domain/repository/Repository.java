package com.nosmurf.domain.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.Access;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import java.util.List;

import rx.Observable;

public interface Repository {

    Observable<Void> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account, String parentEmail);

    Observable<TokenHashed> getHashedToken();

    Observable<Key> getKey();

    Observable<Boolean> hasCurrentUser();

    Observable<List<Access>> getAccess();
}
