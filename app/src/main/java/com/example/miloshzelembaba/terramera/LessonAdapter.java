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

public class LessonAdapter extends ArrayAdapter {


    Activity activity;
    ArrayList<ArrayItem> lesson = new ArrayList<>();
    LayoutInflater inflater;
    public final int VIEW_TYPE_COUNT = 2;
    public final int HEADER = 1;
    public final int LESSON_CARD = 0;


    public LessonAdapter(Context context, int LayoutRes, ArrayList<ArrayItem> cards){
        super(context, LayoutRes, cards);

        activity = (Activity) context;
        lesson = cards;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (lesson.get(position) instanceof Instruction){
            return LESSON_CARD;
        }

        return HEADER;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        int type = getItemViewType(position);
        if (v == null){
            if (type == LESSON_CARD){
                Instruction card = (Instruction) lesson.get(position);
                v = inflater.inflate(R.layout.minimal_lesson, parent, false);

                TextView stepNum = v.findViewById(R.id.step_num);
                stepNum.setText(card.getStepNum());
                TextView minmalInstruction = v.findViewById(R.id.minimal_instructions);
                minmalInstruction.setText(card.getMinimalInstruction());

//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((MainActivity)activity).setUpLesson(((ActionableCard)lesson.get(position)).getHeader(),
//                                ((ActionableCard)lesson.get(position)));
//                    }
//                });
            } else {
                Blurb blurb = (Blurb) lesson.get(position);
                v = inflater.inflate(R.layout.lesson_header, parent, false);
                TextView header = v.findViewById(R.id.lesson_title);
                header.setText(blurb.title);
            }
        }

        return v;

    }
}
