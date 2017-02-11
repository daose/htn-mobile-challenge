package com.daose.htninterview.models;


import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;

public class Skill extends RealmObject {
    @Index
    public String name;
    public float rating;

    @Ignore
    public static final int MAX = 10;
    @Ignore
    public static final String NAME = "name";
    @Ignore
    public static final String RATING = "rating";
}
