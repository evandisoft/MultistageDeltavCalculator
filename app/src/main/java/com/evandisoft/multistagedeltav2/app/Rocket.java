package com.evandisoft.multistagedeltav2.app;

import android.text.Editable;
import android.text.TextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

class Rocket extends ArrayList<RocketStage> {
    private double deltaV;
    private double mass;
    public String name;
    public TextWatcher nameWatcher;
    //public RocketStagesAdapter rsa;
    //public RocketStagesRecyclerAdapter rocketStagesRecyclerAdapter;

    public double getDeltaV() {
        return this.deltaV;
    }

    public double getMass() {
        return this.mass;
    }

    public Rocket() {
        this("Default Rocket Name");
    }

    public Rocket(String name) {
        this.name = "";
        this.deltaV = 0.0d;
        this.mass = 0.0d;
        this.nameWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Rocket.this.name = s.toString();
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.name = name;
    }

    public boolean add(RocketStage rocketStage) {
        add(size(), rocketStage);
        return true;
    }

    public RocketStage set(int index, RocketStage object) {
        RocketStage prev = remove(index);
        add(index, object);
        return prev;
    }

    public void add(int index, RocketStage rocketStage) {
        index = Math.min(Math.max(index, 0), size());
        rocketStage.setContainer(this);
        super.add(index, rocketStage);
        if (size() - index > 1) {
            ((RocketStage) get(index + 1)).setParent(rocketStage);
        }
        if (index > 0) {
            rocketStage.setParent((RocketStage) get(index - 1));
        }
        calculateRocketCharacteristics();
    }

    public RocketStage remove(int index) {
        index = Math.min(Math.max(index, 0), size() - 1);
        if (isEmpty()) {
            return null;
        }
        RocketStage parent = null;
        if (index - 1 >= 0) {
            parent = (RocketStage) get(index - 1);
        }
        if (index + 1 < size()) {
            ((RocketStage) get(index + 1)).setParent(parent);
        }
        RocketStage prev = (RocketStage) super.remove(index);
        calculateRocketCharacteristics();
        return prev;
    }

    public double getMassDownThrough(int index) {
        double mass = 0.0d;
        for (int i = 0; i <= index; i++) {
            mass += ((RocketStage) get(i)).getFullMass();
        }
        return mass;
    }

    public double getMassUpThrough(int index) {
        double mass = 0.0d;
        for (int i = this.size()-1; i >= index; i--) {
            mass += ((RocketStage) get(i)).getFullMass();
        }
        return mass;
    }

    public double getDeltaVDownThrough(int index) {
        double deltaV = 0.0d;
        for (int i = 0; i <= index; i++) {
            deltaV += ((RocketStage) get(i)).getDeltaV();
        }
        return deltaV;
    }

    public double getDeltaVUpThrough(int index) {
        double deltaV = 0.0d;
        for (int i = this.size()-1; i >= index; i--) {
            deltaV += ((RocketStage) get(i)).getDeltaV();
        }
        return deltaV;
    }

    private void sumDeltaV() {
        double deltaV = 0.0d;
        Iterator it = iterator();
        while (it.hasNext()) {
            deltaV += ((RocketStage) it.next()).getDeltaV();
        }
        this.deltaV = deltaV;
    }

    private void sumMass() {
        if (isEmpty()) {
            this.mass = 0.0d;
        } else {
            this.mass = ((RocketStage) get(size() - 1)).effectiveFullMass();
        }
    }

    public void calculateRocketCharacteristics() {
        for (int i = 0; i < size(); i++) {
            ((RocketStage) get(i)).calculateDeltaV();
        }
        sumMass();
        sumDeltaV();
    }

    public JSONObject toJSON() {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("name", this.name);
            mainObject.put("length", size());
            JSONArray jsonArray = new JSONArray();
            Iterator it = iterator();
            while (it.hasNext()) {
                jsonArray.put(((RocketStage) it.next()).toJSON());
            }
            mainObject.put("array", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainObject;
    }

    public String toString() {
        try {
            return toJSON().toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fromJSON(JSONObject jsonObject) {
        clear();
        this.name = jsonObject.optString("name");
        int length = jsonObject.optInt("length");
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("array");
            for (int i = 0; i < length; i++) {
                add(RocketStage.fromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        calculateRocketCharacteristics();
    }

    public void fromString(String string) {
        try {
            fromJSON(new JSONObject(string));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
