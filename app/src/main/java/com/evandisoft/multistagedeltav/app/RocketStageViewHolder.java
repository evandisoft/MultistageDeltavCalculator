package com.evandisoft.multistagedeltav.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;

class RocketStageViewHolder extends RecyclerView.ViewHolder {
    private TextView deltaV;
    private TextView mass;
    private TextView name;
    private Button removeStageButton;

    public RocketStageViewHolder(@NonNull View itemView) {
        super(itemView);

        deltaV=itemView.findViewById(R.id.deltaVTextView);
        mass=itemView.findViewById(R.id.massTextView);
        name=itemView.findViewById(R.id.stageNameTextView);
        removeStageButton=itemView.findViewById(R.id.removeStageButton);
    }

    public void set(RocketStagesRecyclerAdapter rsra, final MainActivity mainActivity, Rocket rocket, int i){
        RocketStage rocketStage=rocket.get(i);
        deltaV.setText(String.format("%s", rocket.getDeltaVUpThrough(i)));
        mass.setText(String.format("%s", rocketStage.effectiveFullMass()));
        name.setText(String.format("%s", rocketStage.name));

        final RocketStagesRecyclerAdapter finalRsra=rsra;
        final int finalI=i;
        removeStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalRsra.rocket.remove(finalI);
                finalRsra.notifyDataSetChanged();
            }
        });
        this.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent editIntent=new Intent(mainActivity,EditStage.class);
                editIntent.putExtra("stageNumber",finalI);
                mainActivity.startActivity(editIntent);
            }
        });
    }
}
