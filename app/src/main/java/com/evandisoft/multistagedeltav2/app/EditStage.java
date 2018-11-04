package com.evandisoft.multistagedeltav2.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.evandisoft.saneandroidutils.lib.FileIO;

import java.io.File;
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
    private ArrayAdapter stageNameAutoTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stage);

        this.stageNameAutoTextAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);


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

        stageNameAutoComplete.setAdapter(stageNameAutoTextAdapter);
        stageNameAutoComplete.setThreshold(1);

        deltavView=findViewById(R.id.deltaVTextView);

        update();
        fullMassEdit.requestFocus();


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        loadAutoComplete();
    }

    public void loadAutoComplete() {
        File[] appFiles = FileIO.getAppFiles(this);

        this.stageNameAutoTextAdapter.clear();
        for (File file : appFiles) {
            String name = file.getName().replace(".json", "");
            if (name.startsWith("stage_")) {
                this.stageNameAutoTextAdapter.add(name.replace("stage_", ""));
            }
        }
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
        //MainActivity.mainActivity.loadAutoComplete();
        update();
        loadAutoComplete();
        //
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

    public void stageDoneOnClick(View view) {
        String name = rocketStage.name;
        this.rocket.calculateRocketCharacteristics();
        //MainActivity.mainActivity.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        update();
  //      this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
 //       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyboard(this);
        this.finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
