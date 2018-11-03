package com.evandisoft.multistagedeltav.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.evandisoft.multistagedeltav.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onUpdateButtonClicked(View view) {
        try {
            this.rocket.calculateRocketCharacteristics();
            this.rocketStagesAdapter.notifyDataSetChanged();
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
}
