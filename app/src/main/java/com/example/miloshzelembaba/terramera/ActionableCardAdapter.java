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
    ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    LayoutInflater inflater;
    public final int VIEW_TYPE_COUNT = 2;
    public final int TEXT = 1;
    public final int ACTION_CARD = 0;


    public ActionableCardAdapter(Context context, int LayoutRes, ArrayList<ArrayItem> cards){
        super(context, LayoutRes, cards);

        activity = (Activity) context;
        actionableCards = cards;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (actionableCards.get(position) instanceof ActionableCard){
            return ACTION_CARD;
        }

        return TEXT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        int type = getItemViewType(position);
        if (v == null){
            if (type == ACTION_CARD){
                ActionableCard card = (ActionableCard) actionableCards.get(position);
                v = inflater.inflate(R.layout.actionable_card, parent, false);

                TextView cardHeader = v.findViewById(R.id.card_header);
                cardHeader.setText(card.getHeader());
                TextView cardTitle = v.findViewById(R.id.card_title);
                cardTitle.setText(card.getTitle());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)activity).setUpLesson(((ActionableCard)actionableCards.get(position)).getHeader(),
                                ((ActionableCard)actionableCards.get(position)));
                    }
                });
            } else {
                v = inflater.inflate(R.layout.blurb, parent, false);
            }
        }

        if (actionableCards.get(position) instanceof ActionableCard) {
            ActionableCard card = (ActionableCard) actionableCards.get(position);
            setColour(v, card.getHeader(), card);

        }

        return v;

    }

    private void setColour(View v, String title, ActionableCard card){
        if (title.equals(ActionableCardStrings.PREVENTION_HEADER)){
            v.findViewById(R.id.cardView).setBackgroundColor(activity.getResources().getColor(R.color.haloBlue));
            card.setColour(R.color.haloBlue);
        } else if (title.equals(ActionableCardStrings.TREATMENT_HEADER)){
            v.findViewById(R.id.cardView).setBackgroundColor(activity.getResources().getColor(R.color.turqoise));
            card.setColour(R.color.turqoise);
        } else if (title.equals(ActionableCardStrings.DETECTION_HEADER)){
            v.findViewById(R.id.cardView).setBackgroundColor(activity.getResources().getColor(R.color.greenCard));
            card.setColour(R.color.greenCard);
        }


    }
}
