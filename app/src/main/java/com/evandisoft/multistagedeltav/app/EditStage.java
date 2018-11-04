package com.evandisoft.multistagedeltav.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;
import com.evandisoft.saneandroidutils.lib.FileIO;

import java.text.DecimalFormat;

public class EditStage extends AppCompatActivity {
    private DecimalFormat decimalFormat = new DecimalFormat();

    Intent thisIntent;
    int stageNumber;
    RocketStage rocketStage;
    EditText fullMassEdit;
    EditText dryMassEdit;
    EditText ispEdit;
    TextView deltavView;
    AutoCompleteTextView stageNameAutoComplete;
    Rocket rocket;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stage);


        app=App.getInstance();

        thisIntent=getIntent();
        stageNumber=thisIntent.getIntExtra("stageNumber",-1);
        if ((stageNumber == -1)) throw new AssertionError();

        rocket=app.rocket;
        rocketStage=rocket.get(stageNumber);

        fullMassEdit=findViewById(R.id.fullMassTextField);
        fullMassEdit.addTextChangedListener(rocketStage.fullMassWatcher);

        dryMassEdit=findViewById(R.id.dryMassTextField);
        dryMassEdit.addTextChangedListener(rocketStage.dryMassWatcher);

        ispEdit=findViewById(R.id.ispTextField);
        ispEdit.addTextChangedListener(rocketStage.ispWatcher);

        stageNameAutoComplete =findViewById(R.id.stageNameAutoComplete);
        stageNameAutoComplete .addTextChangedListener(rocketStage.nameWatcher);

        deltavView=findViewById(R.id.deltaVTextView);

        update();

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(fullMassEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private void update(){
        fullMassEdit.setText(this.decimalFormat.format(rocketStage.getFullMass()));
        dryMassEdit.setText(this.decimalFormat.format(rocketStage.getDryMass()));
        ispEdit.setText(this.decimalFormat.format(rocketStage.getIsp()));
        stageNameAutoComplete.setText(rocketStage.name);
        deltavView.setText(this.decimalFormat.format(rocketStage.getDeltaV()));
    }

    public void stageSaveOnClick(View view) {
        FileIO.writeStringToFile(this.getApplicationContext(), "stage_" + rocketStage.name + ".json", rocketStage.toString());
        this.rocket.calculateRocketCharacteristics();
        //MainActivity.mainActivity.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        //MainActivity.mainActivity.loadAppFiles();
        // update();
        this.finish();
    }

    public void updateOnClick(View view) {
          this.rocket.calculateRocketCharacteristics();
        update();
    }

    public void stageLoadOnClick(View view) {
        String string = FileIO.readStringFromFile(this.getApplicationContext(), "stage_" + rocketStage.name + ".json");
        if (string != null) {
            rocketStage.fromString(string);
            this.rocket.calculateRocketCharacteristics();
            //MainActivity.mainActivity.rocketStagesRecyclerAdapter.notifyDataSetChanged();
            update();
        }

    }

    public void stageClearOnClick(View view) {
        String name = rocketStage.name;
        rocketStage.copyValues(new RocketStage());
        rocketStage.name = name;
        this.rocket.calculateRocketCharacteristics();
        //MainActivity.mainActivity.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        update();
    }
}
