package com.evandisoft.multistagedeltav.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;

import java.text.DecimalFormat;

class RocketStageViewHolder extends RecyclerView.ViewHolder {
    private DecimalFormat decimalFormat = new DecimalFormat();

    private TextView massView;
    private TextView deltavView;

    private TextView totalDownDeltavView;
    private TextView totalDownMassView;
    private TextView totalUpDeltavView;
    private TextView totalUpMassView;
    private TextView nameView;
    private TextView stageNumberView;
    private TableLayout tableLayout;

    private TableRow titleRow;
    private TableRow stageRow;
    private TableRow totalUpRow;
    private TableRow totalDownRow;

    private Button removeStageButton;

    public RocketStageViewHolder(@NonNull View itemView) {
        super(itemView);
        //decimalFormat.

        massView =itemView.findViewById(R.id.rocketStageGroupMassView);
        deltavView =itemView.findViewById(R.id.rocketStageGroupDeltavView);
        totalDownDeltavView =itemView.findViewById(R.id.rocketStageGroupTotalDownDeltavView);
        totalDownMassView =itemView.findViewById(R.id.rocketStageGroupTotalDownMassView);
        totalUpDeltavView =itemView.findViewById(R.id.rocketStageGroupTotalUpDeltavView);
        totalUpMassView =itemView.findViewById(R.id.rocketStageGroupTotalUpMassView);
        nameView =itemView.findViewById(R.id.rocketStageGroupStageNameView);
        stageNumberView =itemView.findViewById(R.id.rocketStageGroupStageNumber);
        removeStageButton=itemView.findViewById(R.id.rocketStageGroupRemoveButton);
        tableLayout=itemView.findViewById(R.id.rocketStageGroupTableLayout);
        totalUpRow=itemView.findViewById(R.id.rocketStageGroupTotalUpRow);
        totalDownRow=itemView.findViewById(R.id.rocketStageGroupTotalDownRow);
    }

    public void set(RocketStagesRecyclerAdapter rsra, final MainActivity mainActivity, Rocket rocket, int i){
        RocketStage rocketStage=rocket.get(i);
        double mass=rocketStage.getFullMass();
        massView.setText(this.decimalFormat.format(mass));
        double deltav=rocketStage.getDeltaV();
        deltavView.setText(this.decimalFormat.format(deltav));
        double deltavUpThrough=rocket.getDeltaVUpThrough(i);
        totalUpDeltavView.setText(this.decimalFormat.format(deltavUpThrough));
        double massUpThrough=rocket.getMassUpThrough(i);
        totalUpMassView.setText(this.decimalFormat.format(massUpThrough));
        double deltavDownThrough=rocket.getDeltaVDownThrough(i);
        totalDownDeltavView.setText(this.decimalFormat.format(deltavDownThrough));
        double massDownThrough=rocket.getMassDownThrough(i);
        totalDownMassView.setText(this.decimalFormat.format(massDownThrough));
        nameView.setText(String.format(" %s",rocketStage.name));
        stageNumberView.setText(String.format(" %d",i));


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
