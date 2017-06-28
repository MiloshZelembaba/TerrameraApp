package com.example.miloshzelembaba.terramera;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    private ActionableCardAdapter actionableCardAdapter;
    private LessonAdapter lessonAdapter;
    private Map<String, Boolean> lessonCompletion = new HashMap<>();


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Set Transitions */
//        Explode explode = new Explode();
//        getWindow().setExitTransition(explode);

        Window window = getWindow();
        Fade slide = new Fade();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAllowEnterTransitionOverlap(true);
        window.setAllowReturnTransitionOverlap(true);
        slide.setInterpolator(new AccelerateDecelerateInterpolator());
        //slide.setSlideEdge(Gravity.LEFT);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        window.setExitTransition(slide); // The Transition to use to move Views out of the scene when calling a new Activity.
        window.setReenterTransition(slide);

        updateLessonCompletion(getIntent());

        /* creating the adapter and adding the cards */
        ActionableCard detectBedBugs = new ActionableCard(ActionableCardStrings.DETECTION_HEADER,
                ActionableCardStrings.DECETION_TITLE, this, lessonCompletion.containsKey(ActionableCardStrings.PREVENTION_HEADER));
        ActionableCard treatBedBugs = new ActionableCard(ActionableCardStrings.TREATMENT_HEADER,
                ActionableCardStrings.TREATMENT_TITLE, this, lessonCompletion.containsKey(ActionableCardStrings.PREVENTION_HEADER));
        ActionableCard preventBedBugs = new ActionableCard(ActionableCardStrings.PREVENTION_HEADER,
                ActionableCardStrings.PREVENTION_TITLE, this, lessonCompletion.containsKey(ActionableCardStrings.PREVENTION_HEADER));
        //actionableCards.add(new Blurb());
        actionableCards.add(detectBedBugs);
        actionableCards.add(treatBedBugs);
        actionableCards.add(preventBedBugs);

        actionableCardAdapter = new ActionableCardAdapter(this, R.layout.actionable_card, actionableCards);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(actionableCardAdapter);


        //TODO: MAKE THIS SUPPORT BUTTON
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setBackgroundDrawable(getDrawable(R.drawable.chat_icon));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void updateLessonCompletion(Intent intent){
        if (getIntent().hasExtra(ActionableCardStrings.DETECTION_HEADER)){
            lessonCompletion.put(ActionableCardStrings.DETECTION_HEADER, true);
        } else if (getIntent().hasExtra(ActionableCardStrings.TREATMENT_HEADER)){
            lessonCompletion.put(ActionableCardStrings.TREATMENT_HEADER, true);
        } else if (getIntent().hasExtra(ActionableCardStrings.PREVENTION_HEADER)){
            lessonCompletion.put(ActionableCardStrings.PREVENTION_HEADER, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setUpLesson(ActionableCard card){
        showLesson(card);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void showLesson(ActionableCard card){
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("Title", card.getTitle());
        intent.putExtra("Header", card.getHeader());
        intent.putExtra("Colour", card.colour);

        if (lessonCompletion.containsKey(ActionableCardStrings.PREVENTION_HEADER)){
            intent.putExtra(ActionableCardStrings.PREVENTION_HEADER, true);
        }
        if (lessonCompletion.containsKey(ActionableCardStrings.TREATMENT_HEADER)){
            intent.putExtra(ActionableCardStrings.TREATMENT_HEADER, true);
        }
        if (lessonCompletion.containsKey(ActionableCardStrings.DETECTION_HEADER)){
            intent.putExtra(ActionableCardStrings.DETECTION_HEADER, true);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

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
}
