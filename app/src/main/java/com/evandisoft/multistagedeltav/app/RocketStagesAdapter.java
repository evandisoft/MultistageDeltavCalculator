package com.evandisoft.multistagedeltav.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evandisoft.multistagedeltav.R;
import com.evandisoft.saneandroidutils.lib.FileIO;

import java.text.DecimalFormat;

class RocketStagesAdapter extends BaseExpandableListAdapter {
    private Context context;
    private DecimalFormat decimalFormat = new DecimalFormat();
    int i = 0;
    private LayoutInflater layoutInflater;
    public Rocket rocket;

    public RocketStagesAdapter(MainActivity mainActivity, Rocket rocket) {
        this.context = mainActivity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rocket = rocket;
        rocket.rsa = this;
    }

    @Override
    public int getGroupCount() {
        return this.rocket.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.rocket.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.rocket.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long) groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final int index = groupPosition;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.rocket_stage_group_view, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.stageNumberTextView)).setText("" + groupPosition);
        ((TextView) convertView.findViewById(R.id.stageNameTextView)).setText(((RocketStage) this.rocket.get(groupPosition)).name);
        ((TextView) convertView.findViewById(R.id.massTextView)).setText("" + this.decimalFormat.format(((RocketStage) this.rocket.get(groupPosition)).effectiveFullMass()));
        ((TextView) convertView.findViewById(R.id.deltaVTextView)).setText("" + this.decimalFormat.format(this.rocket.getDeltaVUpThrough(groupPosition)));
        Button removeStageButton = (Button) convertView.findViewById(R.id.removeStageButton);
        removeStageButton.setFocusable(false);
        removeStageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RocketStagesAdapter.this.rocket.remove(index);
                RocketStagesAdapter.this.notifyDataSetChanged();
            }
        });
        parent.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        return convertView;
    }

    // The tool used to rebuild the source didn't seem to generate this completely
    private class ChildViewHolder {
        public TextView deltaV;
        public TextView dryMass;
        public TextView fullMass;
        public TextView isp;
        public TextView name;
        public RocketStage rocketStage;

        private ChildViewHolder() {
        }

        // Changed "AnonymousClass" that was in second parameter, to Object
        ChildViewHolder(RocketStagesAdapter x0, Object x1) {
            this();
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        final RocketStage rocketStage = (RocketStage) this.rocket.get(groupPosition);
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.rocket_stage_view, parent, false);

            childViewHolder = new ChildViewHolder(this, null);
            childViewHolder.rocketStage = rocketStage;
            childViewHolder.name = (TextView) convertView.findViewById(R.id.stageNameTextField);

            ((AutoCompleteTextView) convertView.findViewById(R.id.stageNameTextField)).setAdapter(((MainActivity) this.context).stageNameAutoTextAdapter);
            childViewHolder.fullMass = (TextView) convertView.findViewById(R.id.fullMassTextField);
            childViewHolder.dryMass = (TextView) convertView.findViewById(R.id.dryMassTextField);
            childViewHolder.isp = (TextView) convertView.findViewById(R.id.ispTextField);
            childViewHolder.deltaV = (TextView) convertView.findViewById(R.id.deltaVTextView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
            childViewHolder.name.removeTextChangedListener(childViewHolder.rocketStage.nameWatcher);
            childViewHolder.fullMass.removeTextChangedListener(childViewHolder.rocketStage.fullMassWatcher);
            childViewHolder.dryMass.removeTextChangedListener(childViewHolder.rocketStage.dryMassWatcher);
            childViewHolder.isp.removeTextChangedListener(childViewHolder.rocketStage.ispWatcher);
        }
        childViewHolder.name.setText(rocketStage.name);
        if (rocketStage.getFullMass() == 0.0d) {
            childViewHolder.fullMass.setText("");
        } else {
            childViewHolder.fullMass.setText(this.decimalFormat.format(rocketStage.getFullMass()));
        }
        if (rocketStage.getDryMass() == 0.0d) {
            childViewHolder.dryMass.setText("");
        } else {
            childViewHolder.dryMass.setText(this.decimalFormat.format(rocketStage.getDryMass()));
        }
        if (rocketStage.getIsp() == 0.0d) {
            childViewHolder.isp.setText("");
        } else {
            childViewHolder.isp.setText(this.decimalFormat.format(rocketStage.getIsp()));
        }
        childViewHolder.deltaV.setText(this.decimalFormat.format(rocketStage.getDeltaV()));
        childViewHolder.name.addTextChangedListener(rocketStage.nameWatcher);
        childViewHolder.fullMass.addTextChangedListener(rocketStage.fullMassWatcher);
        childViewHolder.dryMass.addTextChangedListener(rocketStage.dryMassWatcher);
        childViewHolder.isp.addTextChangedListener(rocketStage.ispWatcher);
        childViewHolder.rocketStage = rocketStage;


        final int finalGroupPosition=groupPosition;
        convertView.findViewById(R.id.stageClearButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RocketStagesAdapter.this.clearRocketStage((RocketStage) RocketStagesAdapter.this.rocket.get(finalGroupPosition));
            }
        });
        convertView.findViewById(R.id.stageSaveButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RocketStagesAdapter.this.saveRocketStage((RocketStage) RocketStagesAdapter.this.rocket.get(finalGroupPosition));
            }
        });
        convertView.findViewById(R.id.stageLoadButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RocketStagesAdapter.this.loadRocketStage(rocketStage);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.rocket.isEmpty();
    }

    public void saveRocketStage(RocketStage rocketStage) {
        FileIO.writeStringToFile(this.context, "stage_" + rocketStage.name + ".json", rocketStage.toString());
        this.rocket.calculateRocketCharacteristics();
        notifyDataSetChanged();
        ((MainActivity) this.context).loadAppFiles();
    }

    public void loadRocketStage(RocketStage rocketStage) {
        String string = FileIO.readStringFromFile(this.context, "stage_" + rocketStage.name + ".json");
        if (string != null) {
            rocketStage.fromString(string);
            this.rocket.calculateRocketCharacteristics();
            notifyDataSetChanged();
        }
    }

    public void clearRocketStage(RocketStage rocketStage) {
        String name = rocketStage.name;
        rocketStage.copyValues(new RocketStage());
        rocketStage.name = name;
        this.rocket.calculateRocketCharacteristics();
        notifyDataSetChanged();
    }
}
