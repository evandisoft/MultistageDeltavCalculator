package com.evandisoft.multistagedeltav.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.evandisoft.saneandroidutils.lib.FileIO;

import com.evandisoft.multistagedeltav.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    TextView addIndexTextField;
    RadioGroup addStageGroup;
    File[] appFiles;
    AutoCompleteTextView autoText;
    ExpandableListView expandableListView;
    Rocket rocket;
    TextView rocketNameTextField;
    RocketStagesAdapter rocketStagesAdapter;
    ArrayAdapter<String> rocketNameAutoTextAdapter;
    ArrayAdapter<String> stageNameAutoTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO I can't remember this stuff
        this.rocketNameAutoTextAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        this.stageNameAutoTextAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        this.rocket = new Rocket();
        this.expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        this.rocketNameTextField = (TextView) findViewById(R.id.rocketNameTextField);
        this.rocketNameTextField.addTextChangedListener(this.rocket.nameWatcher);

        this.rocketStagesAdapter = new RocketStagesAdapter(this, this.rocket);
        this.expandableListView.setAdapter(this.rocketStagesAdapter);
        this.addStageGroup = (RadioGroup) findViewById(R.id.addStageGroup);
        this.addIndexTextField = (TextView) findViewById(R.id.addIndexTextField);
        this.addIndexTextField.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.addStageGroup.check(R.id.addIndexRadioButton);
                return false;
            }
        });

        loadAppFiles();
    }

    public void onUpdateButtonClicked(View view) {
        try {
            this.rocket.calculateRocketCharacteristics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddStageClick(View view) {
        RocketStage newStage = new RocketStage();
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
        this.rocketStagesAdapter.notifyDataSetChanged();
    }

    public void clearAddFocus(View view) {
        this.addIndexTextField.clearFocus();
    }

    public void setAddFocus(View view) {
        this.addIndexTextField.requestFocus();
    }

    public void onClearClicked(View view) {
        this.rocket.clear();
        this.rocketStagesAdapter.notifyDataSetChanged();
    }

    public void onLoadRocketClicked(View view) {
        String string = FileIO.readStringFromFile(this, "rocket_" + this.rocket.name + ".json");
        if (string != null) {
            this.rocket.fromString(string);
            this.rocketStagesAdapter.notifyDataSetChanged();
        }
    }

    public void onSaveRocketClicked(View view) {
        FileIO.writeStringToFile(this, "rocket_" + this.rocket.name + ".json", this.rocket.toString());
        this.rocketStagesAdapter.notifyDataSetChanged();
        loadAppFiles();
    }

    public void onNewButtonClicked(View view) {
        this.rocket.clear();
        this.rocket.add(new RocketStage());
        this.rocketNameTextField.setText("Default Rocket Name");
        this.rocketStagesAdapter.notifyDataSetChanged();
    }

    public void loadAppFiles() {
        this.appFiles = FileIO.getAppFiles(this);
        // TODO Must fix this when I figure out what this is about
        this.stageNameAutoTextAdapter.clear();
        this.rocketNameAutoTextAdapter.clear();
        for (File file : this.appFiles) {
            String name = file.getName().replace(".json", "");
            if (name.startsWith("stage_")) {
                this.stageNameAutoTextAdapter.add(name.replace("stage_", ""));
            } else if (name.startsWith("rocket_")) {
                this.rocketNameAutoTextAdapter.add(name.replace("rocket_", ""));
            }
        }
    }
}
