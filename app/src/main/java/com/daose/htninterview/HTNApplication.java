package com.daose.htninterview;


import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HTNApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build());

        Fresco.initialize(this);
    }
}
