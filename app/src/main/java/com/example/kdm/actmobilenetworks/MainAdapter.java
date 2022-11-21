package com.example.kdm.actmobilenetworks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kdm.actmobilenetworks.databinding.CountryItemBinding;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final List<Country> countries;
    private Context context;
    private CountryItemBinding binding;

    private int checkIndex = -1;
    AdapterCallBacks callBacks;
    public MainAdapter(List<Country> countries, AdapterCallBacks callBacks){
        this.countries = countries;
        this.callBacks = callBacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(DataBindingUtil
                .inflate(LayoutInflater
                                .from(parent.getContext())
                        , R.layout.country_item
                        , parent
                        , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Country model = countries.get(position);
        holder.txtCountryName.setText(model.getName());

        if (position == checkIndex){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener((v)->{
            int pos = checkIndex;
            checkIndex = position;
            if (pos != -1){
                notifyItemChanged(pos);
            }
            holder.checkBox.setVisibility(View.VISIBLE);

            callBacks.onItemClicked(model);
        });

        Glide.with(context)
                .asBitmap()
                .placeholder(R.drawable.ic_flag)
                .load(Constants.FLAG_BASE_URL + model.getCode())
                .into(holder.imgFlag);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountryName;
        ImageView imgFlag;
        ImageView checkBox;

        public ViewHolder(@NonNull CountryItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            txtCountryName = binding.txtCountry;
            imgFlag = binding.imgFlag;
            checkBox = binding.imgChecked;
        }
    }
}
