package com.evandisoft.multistagedeltav2.app;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.evandisoft.saneandroidutils.lib.FileIO;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView addIndexTextField;
    private RadioGroup addStageGroup;
    private RocketStagesRecyclerAdapter rocketStagesRecyclerAdapter;
    private RecyclerView.LayoutManager rocketStagesRecyclerLayoutManager;
    private Rocket rocket;
    private AutoCompleteTextView rocketNameTextField;
    //RocketStagesAdapter rocketStagesAdapter;
    private ArrayAdapter<String> rocketNameAutoTextAdapter;
    private App app;
    private boolean firstStartup;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        firstStartup=!App.instantiated();
        app=App.getInstance();
        this.rocket=app.rocket;

        rocketNameTextField = findViewById(R.id.rocketNameTextField);
        this.rocketNameAutoTextAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        rocketNameTextField.setAdapter(rocketNameAutoTextAdapter);
        rocketNameTextField.setThreshold(1);



        rocketStagesRecyclerAdapter=new RocketStagesRecyclerAdapter(rocket,this);
        rocketStagesRecyclerLayoutManager =new LinearLayoutManager(this);
        RecyclerView rocketStagesRecyclerView = findViewById(R.id.rocketStagesRecyclerView);
        rocketStagesRecyclerView.setLayoutManager(rocketStagesRecyclerLayoutManager);
        rocketStagesRecyclerView.setAdapter(rocketStagesRecyclerAdapter);

        //this.expandableListView.setDescendantFocusability();

        this.rocketNameTextField.addTextChangedListener(this.rocket.nameWatcher);

        //this.rocketStagesAdapter = new RocketStagesAdapter(this, this.rocket);

        this.addStageGroup = findViewById(R.id.addStageGroup);
        this.addIndexTextField = findViewById(R.id.addIndexTextField);
        this.addIndexTextField.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.addStageGroup.check(R.id.addIndexRadioButton);
                v.performClick();
                return false;
            }
        });

        if(firstStartup){
            autoload();
        }

        loadAutoComplete();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

// --Commented out by Inspection START (11/4/18 11:35 PM):
//    public void onUpdateButtonClicked(View view) {
//        try {
//            this.rocket.calculateRocketCharacteristics();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
// --Commented out by Inspection STOP (11/4/18 11:35 PM)

    public void onAddStageClick(View view) {
        String stageName=getResources().getString(R.string.default_stage_name);
        RocketStage newStage = new RocketStage(stageName);

        switch (this.addStageGroup.getCheckedRadioButtonId()) {
            case R.id.addStartRadioButton:
                this.rocket.add(0, newStage);
                break;
            case R.id.addEndRadioButton:
                this.rocket.add(newStage);
                break;
            case R.id.addIndexRadioButton:
                try {
                    int index = Integer.parseInt(this.addIndexTextField.getText().toString());
                    if (index > this.rocket.size()) {
                        index = this.rocket.size();
                    }
                    this.rocket.add(index, newStage);
                    break;
                } catch (Exception e) {
                    break;
                }
        }
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    protected void onResume(){
        super.onResume();
        rocketNameTextField.clearFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    protected void onStart() {
        super.onStart();
        rocketStagesRecyclerAdapter.notifyDataSetChanged();

    }

    protected void onStop() {
        super.onStop();
        autosave();
    }

    private void autosave() {
        FileIO.writeStringToFile(this, "autosave.json", this.rocket.toString());
    }

    private void autoload() {
        String string = FileIO.readStringFromFile(this, "autosave.json");
        if (string != null) {
            this.rocket.fromString(string);
            this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        }
        else{
            this.rocket.name=getResources().getString(R.string.default_rocket_name);
        }
        this.rocketNameTextField.setText(this.rocket.name);
    }

    public void clearAddFocus(View view) {
        this.addIndexTextField.clearFocus();
    }

    public void setAddFocus(View view) {
        this.addIndexTextField.requestFocus();
    }

    public void onClearClicked(View view) {
        this.rocket.clear();
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    public void onLoadRocketClicked(View view) {
        String string = FileIO.readStringFromFile(this, "rocket_" + this.rocket.name + ".json");
        if (string != null) {
            this.rocket.fromString(string);
            this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void onSaveRocketClicked(View view) {
        FileIO.writeStringToFile(this, "rocket_" + this.rocket.name + ".json", this.rocket.toString());
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
        loadAutoComplete();
    }

    public void onNewButtonClicked(View view) {
        this.rocket.clear();
        this.rocket.add(new RocketStage());
        this.rocketNameTextField.setText(getString(R.string.default_rocket_name));
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    private void loadAutoComplete() {
        File[] appFiles = FileIO.getAppFiles(this);

        this.rocketNameAutoTextAdapter.clear();
        for (File file : appFiles) {
            String name = file.getName().replace(".json", "");
            if (name.startsWith("rocket_")) {
                this.rocketNameAutoTextAdapter.add(name.replace("rocket_", ""));
            }
        }
    }
}
