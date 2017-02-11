package com.daose.htninterview.retrofit;

import com.daose.htninterview.models.Hacker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HTNService {
    @GET("users.json")
    Call<List<Hacker>> listHackers();
}
