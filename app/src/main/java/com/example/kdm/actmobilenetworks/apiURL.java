package com.example.kdm.actmobilenetworks;

import com.example.kdm.actmobilenetworks.CountryListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiURL {
    @GET("countries")
    Call<CountryListResponse> getCountries();
}
