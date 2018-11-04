package com.evandisoft.multistagedeltav.app;

import android.text.Editable;
import android.text.TextWatcher;

import com.evandisoft.multistagedeltav.app.R;

import org.json.JSONException;
import org.json.JSONObject;

class RocketStage {
    private static final float standardGravity = 9.80665f;
    private Rocket container;
    private double deltaV;
    private double dryMass;
    public TextWatcher dryMassWatcher;
    private double fullMass;
    public TextWatcher fullMassWatcher;
    private double isp;
    public TextWatcher ispWatcher;
    public String name;
    public TextWatcher nameWatcher;
    private RocketStage parent;


    protected void setContainer(Rocket container) {
        this.container = container;
    }

    public double getFullMass() {
        return this.fullMass;
    }

    public double getDryMass() {
        return this.dryMass;
    }

    public double getIsp() {
        return this.isp;
    }

    public double getDeltaV() {
        return this.deltaV;
    }

    protected void setParent(RocketStage parent) {
        this.parent = parent;
    }

    public void setFullMass(double fullMass) {
        this.fullMass = fullMass;
        calculateDeltaV();
    }

    public void setDryMass(double dryMass) {
        this.dryMass = dryMass;
        calculateDeltaV();
    }

    public void setIsp(double isp) {
        this.isp = isp;
        calculateDeltaV();
    }

    public RocketStage(String stageName) {
        this(stageName, 0.0d, 0.0d, 0.0d);
    }

    public RocketStage() {
        this("", 0.0d, 0.0d, 0.0d);
    }

    public RocketStage(String name, double fullMass, double dryMass, double isp) {
        this.name = "";
        this.parent = null;
        this.nameWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RocketStage.this.name = s.toString();
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.fullMassWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    RocketStage.this.fullMass = Double.parseDouble(s.toString().replace(",", ""));
                } catch (Exception e) {
                    if (s.toString().equals("")) {
                        RocketStage.this.fullMass = 0.0d;
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.dryMassWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    RocketStage.this.dryMass = Double.parseDouble(s.toString().replace(",", ""));
                } catch (Exception e) {
                    if (s.toString().equals("")) {
                        RocketStage.this.dryMass = 0.0d;
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.ispWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    RocketStage.this.isp = Double.parseDouble(s.toString().replace(",", ""));
                } catch (Exception e) {
                    if (s.toString().equals("")) {
                        RocketStage.this.isp = 0.0d;
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.name = name;
        this.fullMass = fullMass;
        this.dryMass = dryMass;
        this.isp = isp;
        calculateDeltaV();
    }

    public double effectiveFullMass() {
        double mass = this.fullMass;
        if (this.parent != null) {
            return mass + this.parent.effectiveFullMass();
        }
        return mass;
    }

    public double effectiveDryMass() {
        double mass = this.dryMass;
        if (this.parent != null) {
            return mass + this.parent.effectiveFullMass();
        }
        return mass;
    }

    protected void calculateDeltaV() {
        double deltaV;
        if (this.fullMass == 0.0d || this.dryMass == 0.0d || this.isp == 0.0d) {
            deltaV = 0.0d;
        } else {
            deltaV = (this.isp * 9.806650161743164d) * Math.log(effectiveFullMass() / effectiveDryMass());
        }
        this.deltaV = deltaV;
    }

    public void copyValues(RocketStage rocketStage) {
        this.name = rocketStage.name;
        this.fullMass = rocketStage.fullMass;
        this.dryMass = rocketStage.dryMass;
        this.isp = rocketStage.isp;
    }

    static RocketStage fromJSON(JSONObject jsonObject) {
        return new RocketStage(jsonObject.optString("name"), jsonObject.optDouble("fullMass"), jsonObject.optDouble("dryMass"), jsonObject.optDouble("isp"));
    }

    public void fromString(String string) {
        try {
            copyValues(fromJSON(new JSONObject(string)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        try {
            return toJSON().toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", this.name);
            jsonObject.put("fullMass", this.fullMass);
            jsonObject.put("dryMass", this.dryMass);
            jsonObject.put("isp", this.isp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
