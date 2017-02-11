package com.daose.htninterview.helpers;


import com.daose.htninterview.BuildConfig;
import com.daose.htninterview.R;
import com.daose.htninterview.models.Hacker;
import com.daose.htninterview.models.Skill;
import com.daose.htninterview.retrofit.HTNService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HackTheNorth {
    public HackTheNorth() {}

    public static void updateUsersAsync() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HTN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HTNService service = retrofit.create(HTNService.class);
        Call<List<Hacker>> call = service.listHackers();
        call.enqueue(new Callback<List<Hacker>>() {
            @Override
            public void onResponse(Call<List<Hacker>> call, Response<List<Hacker>> response) {
                saveToRealm(response.body());
            }

            @Override
            public void onFailure(Call<List<Hacker>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void saveToRealm(final List<Hacker> hackers){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(hackers);
            }
        });
        realm.close();
    }

    public static HashMap<String, Float> getValidatedSkills(List<Skill> skills) {
        HashMap<String, Float> skillMap = new HashMap<String, Float>();
        for(Skill skill : skills) {
            skillMap.put(skill.name, skill.rating);
        }
        return skillMap;
    }
}
