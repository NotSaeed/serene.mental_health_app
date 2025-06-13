package com.example.serene;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;

public class ChallengeAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] challenges;
    private boolean[] checkedStates;

    public ChallengeAdapter(Context context, String[] challenges) {
        super(context, R.layout.challenge_item, challenges);
        this.context = context;
        this.challenges = challenges;
        this.checkedStates = new boolean[challenges.length];

        SharedPreferences prefs = context.getSharedPreferences("SelfCarePrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < challenges.length; i++) {
            checkedStates[i] = prefs.getBoolean("challenge_" + i, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.challenge_item, parent, false);

        TextView title = row.findViewById(R.id.challengeText);
        CheckBox checkBox = row.findViewById(R.id.challengeCheckBox);

        title.setText(challenges[position]);
        checkBox.setChecked(checkedStates[position]);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedStates[position] = isChecked;
            SharedPreferences.Editor editor = context.getSharedPreferences("SelfCarePrefs", Context.MODE_PRIVATE).edit();
            editor.putBoolean("challenge_" + position, isChecked);
            editor.apply();
        });

        return row;
    }
}
