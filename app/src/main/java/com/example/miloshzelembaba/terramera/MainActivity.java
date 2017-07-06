package com.example.miloshzelembaba.terramera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    private ActionableCardAdapter actionableCardAdapter;


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = getWindow();
        Fade slide = new Fade();
        window.setAllowEnterTransitionOverlap(true);
        window.setAllowReturnTransitionOverlap(true);

        slide.setInterpolator(new AccelerateDecelerateInterpolator());
        //slide.setSlideEdge(Gravity.LEFT);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        window.setExitTransition(slide); // The Transition to use to move Views out of the scene when calling a new Activity.
        window.setReenterTransition(slide);

        /* creating the adapter and adding the cards */
        ActionableCard detectBedBugs = new ActionableCard(ActionableCardStrings.DETECTION_HEADER,
                ActionableCardStrings.DECETION_TITLE, this);
        ActionableCard treatBedBugs = new ActionableCard(ActionableCardStrings.TREATMENT_HEADER,
                ActionableCardStrings.TREATMENT_TITLE, this);
        ActionableCard preventBedBugs = new ActionableCard(ActionableCardStrings.PREVENTION_HEADER,
                ActionableCardStrings.PREVENTION_TITLE, this);
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

        setLogo();
    }

    public void setLogo(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_layout);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2A2A2A")))
    }

    public void setUpLesson(final ActionableCard card){
        showLesson(card);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void showLesson(ActionableCard card){
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("Title", card.getTitle());
        intent.putExtra("Header", card.getHeader());
        intent.putExtra("Colour", card.colour);

        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        overridePendingTransition(0, 0);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
        //return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return false;

    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void lessonTransition(){
//        ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id. card_layout);
//        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
//                .addTransition(new ChangeBounds())
//                .addTransition(new ChangeImageTransform()));
//
//        CardView card = (CardView) findViewById(R.id.cardView);
//        boolean expanded = true;
//        ViewGroup.LayoutParams params = card.getLayoutParams();
//        params.height = expanded ? ViewGroup.LayoutParams.MATCH_PARENT :
//                ViewGroup.LayoutParams.WRAP_CONTENT;
//        card.setLayoutParams(params);
//
//        imageView.setScaleType(expanded ? CardView.ScaleType.CENTER_CROP :
//                //ImageView.ScaleType.FIT_CENTER);
//    }

}
