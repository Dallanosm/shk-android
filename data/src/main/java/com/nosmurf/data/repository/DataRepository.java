package com.nosmurf.data.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.data.repository.network.NetworkDataSource;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DataRepository implements Repository {

    private FirebaseDataSource firebaseDataSource;

    private NetworkDataSource networkDataSource;

    @Inject
    public DataRepository(FirebaseDataSource firebaseDataSource, NetworkDataSource networkDataSource) {
        this.firebaseDataSource = firebaseDataSource;
        this.networkDataSource = networkDataSource;
    }

    @Override
    public Observable<Void> uploadPhoto(String imagePath) {
        return Observable
                .zip(firebaseDataSource.uploadPhoto(imagePath), firebaseDataSource.getGroupId(), firebaseDataSource.getPersonId(),
                        (imageUrl, groupId, personId) -> new ImageReference(groupId, personId, imageUrl))
                .flatMap(imageReference -> networkDataSource.addFaceOnMicrosoftFaceAPI(imageReference));
    }

    @Override
    public Observable<Void> doLogin(GoogleSignInAccount account) {
        return firebaseDataSource.doLogin(account);
    }


}
