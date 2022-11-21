package com.example.kdm.actmobilenetworks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdm.actmobilenetworks.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements AdapterCallBacks{

    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Constants.DATABASE_NAME, MODE_PRIVATE);

        Country current = getDatabase();
        if (current != null){
            onItemClicked(current);
        }

        binding.cvRegion.setOnClickListener((v) -> {
            binding.cvRegion.setClickable(false);
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.bottom_pick);
            RecyclerView recyclerView = dialog.findViewById(R.id.rcCountries);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setOnDismissListener(dialogInterface -> binding.cvRegion.setClickable(true));

            Networking.retrofit().getCountries().enqueue(new Callback<CountryListResponse>() {
                @Override
                public void onResponse(Call<CountryListResponse> call, Response<CountryListResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            MainAdapter adapter = new MainAdapter(response.body().getResult(), MainActivity.this);

                            if (recyclerView == null){
                                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            dialog.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryListResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onItemClicked(Country country) {
        binding.txtCountry.setText(country.getName());
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.ic_flag)
                .load(Constants.FLAG_BASE_URL + country.getCode())
                .into(binding.imgFlag);

        setDatabase(country);
    }

    private void setDatabase(Country country){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Constants.MAIN_COUNTRY_KEY, new Gson().toJson(country));
        myEdit.commit();
    }

    private Country getDatabase(){
        String countryStr = sharedPreferences.getString(Constants.MAIN_COUNTRY_KEY, "");
        return countryStr.equals("") ? null : new Gson().fromJson(countryStr, Country.class);
    }
}