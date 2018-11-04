package com.evandisoft.multistagedeltav.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;

import java.text.DecimalFormat;

class RocketStageViewHolder extends RecyclerView.ViewHolder {
    private DecimalFormat decimalFormat = new DecimalFormat();

    private TextView mass;
    private TextView deltav;

    private TextView totalDownDeltav;
    private TextView totalDownMass;
    private TextView totalUpDeltav;
    private TextView totalUpMass;
    private TextView name;
    private TextView stageNumber;
    private TableLayout tableLayout;

    private Button removeStageButton;

    public RocketStageViewHolder(@NonNull View itemView) {
        super(itemView);
        //decimalFormat.

        mass=itemView.findViewById(R.id.rocketStageGroupMassView);
        deltav=itemView.findViewById(R.id.rocketStageGroupDeltavView);
        totalDownDeltav =itemView.findViewById(R.id.rocketStageGroupTotalDownDeltavView);
        totalDownMass =itemView.findViewById(R.id.rocketStageGroupTotalDownMassView);
        totalUpDeltav =itemView.findViewById(R.id.rocketStageGroupTotalUpDeltavView);
        totalUpMass =itemView.findViewById(R.id.rocketStageGroupTotalUpMassView);
        name=itemView.findViewById(R.id.rocketStageGroupStageNameView);
        stageNumber=itemView.findViewById(R.id.rocketStageGroupStageNumber);
        removeStageButton=itemView.findViewById(R.id.rocketStageGroupRemoveButton);
        tableLayout=itemView.findViewById(R.id.rocketStageGroupTableLayout);
    }

    public void set(RocketStagesRecyclerAdapter rsra, final MainActivity mainActivity, Rocket rocket, int i){
        RocketStage rocketStage=rocket.get(i);
        mass.setText(this.decimalFormat.format(rocketStage.getFullMass()));
        deltav.setText(this.decimalFormat.format(rocketStage.getDeltaV()));
        totalUpDeltav.setText(this.decimalFormat.format(rocket.getDeltaVUpThrough(i)));
        totalUpMass.setText(this.decimalFormat.format(rocket.getMassUpThrough(i)));
        totalDownDeltav.setText(this.decimalFormat.format(rocket.getDeltaVDownThrough(i)));
        totalDownMass.setText(this.decimalFormat.format(rocket.getMassDownThrough(i)));
        name.setText(String.format(" %s",rocketStage.name));
        stageNumber.setText(String.format(" %d",i));

        final RocketStagesRecyclerAdapter finalRsra=rsra;
        final int finalI=i;
        removeStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalRsra.rocket.remove(finalI);
                finalRsra.notifyDataSetChanged();
            }
        });
        this.tableLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent editIntent=new Intent(mainActivity,EditStage.class);
                editIntent.putExtra("stageNumber",finalI);
                mainActivity.startActivity(editIntent);
            }
        });
    }
}
