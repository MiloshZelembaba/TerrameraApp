package com.example.miloshzelembaba.terramera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    private ActionableCardAdapter actionableCardAdapter;
    private LessonAdapter lessonAdapter;
    private boolean mainPage = true;




    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Set Transitions */
        Explode explode = new Explode();
        getWindow().setExitTransition(explode);

        Slide slide = new Slide(Gravity.RIGHT);
        getWindow().setReturnTransition(slide);

        /* creating the adapter and adding the cards */
        ActionableCard detectBedBugs = new ActionableCard(ActionableCardStrings.DETECTION_HEADER,
                ActionableCardStrings.DECETION_TITLE, this);
        addDetectionInstructions(detectBedBugs);
        ActionableCard treatBedBugs = new ActionableCard(ActionableCardStrings.TREATMENT_HEADER,
                ActionableCardStrings.TREATMENT_TITLE, this);
        addTreatmentInstructions(treatBedBugs);
        ActionableCard preventBedBugs = new ActionableCard(ActionableCardStrings.PREVENTION_HEADER,
                ActionableCardStrings.PREVENTION_TITLE, this);
        addPreventionInstructions(preventBedBugs);
        actionableCards.add(new Blurb());
        actionableCards.add(detectBedBugs);
        actionableCards.add(treatBedBugs);
        actionableCards.add(preventBedBugs);

        actionableCardAdapter = new ActionableCardAdapter(this, R.layout.actionable_card, actionableCards);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(actionableCardAdapter);


        //TODO: MAKE THIS SUPPORT BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setUpLesson(ActionableCard card){
        mainPage = false;
        card.changeLayoutColour(this);
        showLesson(card);
    }



    public void showLesson(ActionableCard card){
        Blurb blurb = new Blurb();
        blurb.setTitle(card.getTitle());
        ArrayList<Instruction> instructions = card.getInstructions();
        ArrayList<ArrayItem> items = new ArrayList<>();
        items.add(blurb);
        for (Instruction i: instructions){
            items.add(i);
        }

        lessonAdapter = new LessonAdapter(this, R.layout.minimal_lesson, items);

        card.getInstructionSet().setAdapter(lessonAdapter);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(lessonAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDetectionInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet(card);
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Turn on your flashlight", "", "Tap here", set);
        step1.setAction(step1.FLASHLIGHT);
        Instruction step2 = new Instruction("Go under your bed", "", set);
        Instruction step3 = new Instruction("Freak out", "", set);
        Instruction step4 = new Instruction("Call for help", "", set);
        Instruction step5 = new Instruction("Leave the house", "", set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);

        card.setInstructions(set);
    }

    private void addPreventionInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet(card);
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Turn on your flashlight", "", "Tap here", set);
        step1.setAction(step1.FLASHLIGHT);
        Instruction step2 = new Instruction("Go under your bed", "", set);
        Instruction step3 = new Instruction("Freak out", "", set);
        Instruction step4 = new Instruction("Call for help", "", set);
        Instruction step5 = new Instruction("Leave the house", "", set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);

        card.setInstructions(set);
    }

    private void addTreatmentInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet(card);
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Turn on your flashlight", "", "Tap here", set);
        step1.setAction(step1.FLASHLIGHT);
        Instruction step2 = new Instruction("Go under your bed", "", set);
        Instruction step3 = new Instruction("Freak out", "", set);
        Instruction step4 = new Instruction("Call for help", "", set);
        Instruction step5 = new Instruction("Leave the house", "", set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);


        card.setInstructions(set);
    }

    @Override
    public void onBackPressed() {
        if (!mainPage) {
            findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.headerGray));
            findViewById(R.id.content_main).setBackgroundColor(getResources().getColor(R.color.gravyGray));

            actionableCardAdapter = new ActionableCardAdapter(this, R.layout.actionable_card, actionableCards);

            ListView listView = (ListView) findViewById(R.id.actionable_card_list);
            listView.setAdapter(actionableCardAdapter);
            mainPage = true;
        } else {
            super.onBackPressed();
        }

    }
}
