package com.evandisoft.multistagedeltav.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

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
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
