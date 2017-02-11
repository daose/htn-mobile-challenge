package com.daose.htninterview.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Hacker extends RealmObject {

    @Index
    private String name;

    @PrimaryKey
    private String email;

    private String picture;
    private String company;
    private String phone;
    private Float latitude;
    private Float longitude;
    private RealmList<Skill> skills;

    @Ignore
    public static final String PRIMARY_KEY = "email";
    @Ignore
    public static final String NAME = "name";

    public String getPrimaryValue() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasLocation(){
        return (latitude != null && longitude != null);
    }

    public String getPicture() {
        return picture;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public RealmList<Skill> getSkills() {
        return skills;
    }
}
