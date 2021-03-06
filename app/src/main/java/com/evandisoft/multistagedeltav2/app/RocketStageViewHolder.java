package com.evandisoft.multistagedeltav2.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

class RocketStageViewHolder extends RecyclerView.ViewHolder {
    private final DecimalFormat decimalFormat = new DecimalFormat();
    private final App app;


    private final TextView massView;
    private final TextView deltavView;
    private final TextView totalDownDeltavView;
    private final TextView totalDownMassView;
    private final TextView totalUpDeltavView;
    private final TextView totalUpMassView;
    private final TextView nameView;
    private final TextView stageNumberView;
    private final TableLayout tableLayout;

//    private final TableRow titleRow;
//    private final TableRow stageRow;
//    private final TableRow totalUpRow;
//    private final TableRow totalDownRow;

    private final Button removeStageButton;

    // --Commented out by Inspection (11/4/18 11:36 PM):HorizontalScrollView rocketStageGroupScroll;

    RocketStageViewHolder(@NonNull View itemView) {
        super(itemView);
        //decimalFormat.
        app=App.getInstance();
        app.deltavFormat.setMaximumFractionDigits(0);

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
//        titleRow=itemView.findViewById(R.id.rocketStageGroupTitleRow);
//        stageRow=itemView.findViewById(R.id.rocketStageGroupStageRow);
//        totalUpRow=itemView.findViewById(R.id.rocketStageGroupTotalUpRow);
//        totalDownRow=itemView.findViewById(R.id.rocketStageGroupTotalDownRow);

        //rocketStageGroupScroll =itemView.findViewById(R.id.rocketStageGroupScroll);
        //rocketStageGroupScroll.setClickable(true);
    }

    public void set(RocketStagesRecyclerAdapter rsra, final MainActivity mainActivity, Rocket rocket, int i){
        RocketStage rocketStage=rocket.get(i);
        double mass=rocketStage.getFullMass();
        massView.setText(this.decimalFormat.format(mass));
        double deltav=rocketStage.getDeltaV();
        deltavView.setText(app.deltavFormat.format(deltav));
        double deltavUpThrough=rocket.getDeltaVUpThrough(i);
        totalUpDeltavView.setText(app.deltavFormat.format(deltavUpThrough));
        double massUpThrough=rocket.getMassUpThrough(i);
        totalUpMassView.setText(this.decimalFormat.format(massUpThrough));
        double deltavDownThrough=rocket.getDeltaVDownThrough(i);
        totalDownDeltavView.setText(app.deltavFormat.format(deltavDownThrough));
        double massDownThrough=rocket.getMassDownThrough(i);
        totalDownMassView.setText(this.decimalFormat.format(massDownThrough));
        nameView.setText(String.format(Locale.getDefault()," %s",rocketStage.name));
        stageNumberView.setText(String.format(Locale.getDefault()," %d",i));


//        TableRow[] setVisibleRows={titleRow,stageRow,totalUpRow,totalDownRow};
//        for(TableRow row:setVisibleRows){
//            row.setVisibility(View.VISIBLE);
//        }
//
//        if(mass==massDownThrough){
//            totalDownRow.setVisibility(View.GONE);
//        }
//
//        if(mass==massUpThrough){
//            totalUpRow.setVisibility(View.GONE);
//        }
//
//        if(mass==0){
//            TableRow[] goneRows={totalUpRow,totalDownRow};
//            for(TableRow row:goneRows){
//                row.setVisibility(View.GONE);
//            }
//        }

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
