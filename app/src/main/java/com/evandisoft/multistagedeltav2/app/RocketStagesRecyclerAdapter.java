package com.evandisoft.multistagedeltav2.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class RocketStagesRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter {
    private final MainActivity mainActivity;
    final Rocket rocket;


    RocketStagesRecyclerAdapter(Rocket rocket, MainActivity mainActivity) {
        this.rocket=rocket;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rocket_stage_group_view, viewGroup, false);

        return new RocketStageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((RocketStageViewHolder)viewHolder).set(this,mainActivity,this.rocket,i);
    }

    @Override
    public int getItemCount() {
        return this.rocket.size();
    }
}
