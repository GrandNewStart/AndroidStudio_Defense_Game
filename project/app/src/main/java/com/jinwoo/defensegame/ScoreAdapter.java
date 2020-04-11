package com.jinwoo.defensegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ScoreAdapter extends BaseAdapter {

    ArrayList<Score> scoreList;
    LayoutInflater inflater;

    public ScoreAdapter (ArrayList<Score> scoreList, Context context) {
        this.scoreList = scoreList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.score, parent, false);

        TextView rank = (TextView) convertView.findViewById(R.id.txt_rank);
        TextView id = (TextView) convertView.findViewById(R.id.txt_id);
        TextView score = (TextView) convertView.findViewById(R.id.txt_score);

        String r = scoreList.get(position).rank + "";
        String i = scoreList.get(position).id;
        String s = scoreList.get(position).score + "";
        rank.setText(r);
        id.setText(i);
        score.setText(s);

        return convertView;
    }
}
