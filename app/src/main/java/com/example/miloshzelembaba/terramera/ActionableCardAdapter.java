package com.example.miloshzelembaba.terramera;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class ActionableCardAdapter extends ArrayAdapter {


    Activity activity;
    ArrayList<ActionableCard> actionableCards = new ArrayList<>();
    LayoutInflater inflater;


    public ActionableCardAdapter(Context context, int LayoutRes, ArrayList<ActionableCard> cards){
        super(context, LayoutRes, cards);

        activity = (Activity) context;
        actionableCards = cards;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null){
            v = inflater.inflate(R.layout.actionable_card, parent, false);

            TextView cardHeader = v.findViewById(R.id.card_header);
            cardHeader.setText(actionableCards.get(position).getHeader());
            TextView cardTitle = v.findViewById(R.id.card_title);
            cardTitle.setText(actionableCards.get(position).getTitle());
        }

        setColour(v, actionableCards.get(position).getHeader());

        return v;

    }

    private void setColour(View v, String title){
        if (title.equals(ActionableCardStrings.PREVENTION_HEADER)){
            v.setBackgroundColor(activity.getResources().getColor(R.color.haloBlue));
        } else if (title.equals(ActionableCardStrings.TREATMENT_HEADER)){
            v.setBackgroundColor(activity.getResources().getColor(R.color.turqoise));
        } else if (title.equals(ActionableCardStrings.DETECTION_HEADER)){
            v.setBackgroundColor(activity.getResources().getColor(R.color.greenCard));
        }
    }
}
