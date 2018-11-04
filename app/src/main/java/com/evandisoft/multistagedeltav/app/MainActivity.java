package com.evandisoft.multistagedeltav.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    RecyclerView rocketStagesRecyclerView;
    RocketStagesRecyclerAdapter rocketStagesRecyclerAdapter;
    RecyclerView.LayoutManager rocketStagesRecyclerLayoutManager;
    Rocket rocket;
    AutoCompleteTextView rocketNameTextField;
    //RocketStagesAdapter rocketStagesAdapter;
    ArrayAdapter<String> rocketNameAutoTextAdapter;
    ArrayAdapter<String> stageNameAutoTextAdapter;
    App app;
    boolean firstStartup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        firstStartup=!App.instanciated();
        app=App.getInstance();

        rocketNameTextField = findViewById(R.id.rocketNameTextField);
        this.rocketNameAutoTextAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);

        this.autoText = rocketNameTextField;
        this.autoText.setAdapter(this.rocketNameAutoTextAdapter);
        this.autoText.setThreshold(1);

        this.stageNameAutoTextAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);

        this.rocket = app.rocket;

        rocketStagesRecyclerAdapter=new RocketStagesRecyclerAdapter(rocket,this);
        rocketStagesRecyclerLayoutManager =new LinearLayoutManager(this);
        rocketStagesRecyclerView =findViewById(R.id.rocketStagesRecyclerView);
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
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    protected void onStart() {
        super.onStart();
        rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    protected void onStop() {
        super.onStop();
        autosave();
    }

    public void autosave() {
        FileIO.writeStringToFile(this, "autosave.json", this.rocket.toString());
    }

    public void autoload() {
        String string = FileIO.readStringFromFile(this, "autosave.json");
        if (string != null) {
            this.rocket.fromString(string);
            this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
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
        loadAppFiles();
    }

    public void onNewButtonClicked(View view) {
        this.rocket.clear();
        this.rocket.add(new RocketStage());
        this.rocketNameTextField.setText(getString(R.string.DefaultRocketName));
        this.rocketStagesRecyclerAdapter.notifyDataSetChanged();
    }

    public void loadAppFiles() {
        this.appFiles = FileIO.getAppFiles(this);

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

    public void testFun33(View view) {
        int a=3;
    }
}
