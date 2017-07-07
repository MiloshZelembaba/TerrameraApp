package com.example.miloshzelembaba.terramera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        if (true){
            if (type == ACTION_CARD){
                ActionableCard card = (ActionableCard) actionableCards.get(position);
                v = inflater.inflate(R.layout.actionable_card, parent, false);

                TextView cardHeader = v.findViewById(R.id.card_header);
                cardHeader.setText(card.getHeader());
                TextView cardTitle = v.findViewById(R.id.card_title);
                cardTitle.setText(card.getTitle());

//                if (card.completed){
//                    TextView completionStatus = v.findViewById(R.id.completion_status);
//                    completionStatus.setText("Completed!");
//                }

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ((MainActivity)activity).setUpLesson(((ActionableCard)actionableCards.get(position)));
                        expand(view.findViewById(R.id.cardView), view.findViewById(R.id.card_layout),activity,((ActionableCard)actionableCards.get(position)));
                    }
                });
                v.findViewById(R.id.begin_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)activity).setUpLesson(((ActionableCard)actionableCards.get(position)));
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
            ((CardView)v.findViewById(R.id.cardView)).setCardBackgroundColor(activity.getResources().getColor(R.color.haloBlue));
            card.setColour(R.color.haloBlue);
        } else if (title.equals(ActionableCardStrings.TREATMENT_HEADER)){
            ((CardView)v.findViewById(R.id.cardView)).setCardBackgroundColor(activity.getResources().getColor(R.color.turqoise));
            card.setColour(R.color.turqoise);
        } else if (title.equals(ActionableCardStrings.DETECTION_HEADER)){
            ((CardView)v.findViewById(R.id.cardView)).setCardBackgroundColor(activity.getResources().getColor(R.color.greenCard));
            card.setColour(R.color.greenCard);
        }
    }


    public static void expand(final View v, final View frame, final Context context, final ActionableCard card) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int baseHeight = v.getHeight();
        final int height = getSize(context).y - baseHeight;
        final int baseWidth = v.getWidth();
        final int width = getSize(context).x - baseWidth;
        final float ogX = v.getX();
        final float ogY = v.getY();

        final float ogXF = frame.getX();
        final float ogYF = frame.getY();

        final View header = v.findViewById(R.id.card_header);
        final View title = v.findViewById(R.id.card_title);
        final View beginBtn = v.findViewById(R.id.begin_button);
        final View fab = ((Activity)context).findViewById(R.id.fab);



        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);



        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int)(baseHeight + (height * interpolatedTime));
                v.getLayoutParams().width = (int)(baseWidth + (width * interpolatedTime));
                v.setX(ogX * (1 - interpolatedTime));
                v.setY(ogY * (1 - interpolatedTime));
                frame.setX(ogXF * (1 - interpolatedTime));
                frame.setY(ogYF * (1 - interpolatedTime));
                header.setAlpha(1 - interpolatedTime);
                title.setAlpha(1 - interpolatedTime);
                beginBtn.setAlpha(1 - interpolatedTime);
                fab.setAlpha(1 - interpolatedTime);

                if (interpolatedTime == 1){
                    ((MainActivity)context).findViewById(R.id.actionable_card_list).setVisibility(View.INVISIBLE);
                    ((MainActivity)context).setUpLesson(card);
                }

                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(500);
        v.startAnimation(a);
    }


    public static Point getSize(Context context){
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

}
