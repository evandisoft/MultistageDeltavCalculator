package com.evandisoft.multistagedeltav.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;

class RocketStagesRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter {
    Rocket rocket;

    public RocketStagesRecyclerAdapter(Rocket rocket) {
        this.rocket=rocket;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rocket_stage_group_view, viewGroup, false);
        // TODO set the textview v's values properly with respect to the ith rocket stage

        RocketStageViewHolder vh=new RocketStageViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((RocketStageViewHolder)viewHolder).set(this,this.rocket,i);
    }

    @Override
    public int getItemCount() {
        return this.rocket.size();
    }
}
