package com.example.miloshzelembaba.terramera;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListView;


import java.util.ArrayList;

import static com.example.miloshzelembaba.terramera.LessonAdapter.decodeSampledBitmapFromResource;


public class MainActivity extends AppCompatActivity {

    private final String EXPAND = "Tap to expand";
    private ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    private ActionableCardAdapter actionableCardAdapter;
    private LessonAdapter lessonAdapter;
    private InstructionSet set;
    boolean mainPage = true;
    ActionableCard detectBedBugs;
    ActionableCard treatBedBugs;
    ActionableCard preventBedBugs;


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        /* creating the adapter and adding the cards */
        detectBedBugs = new ActionableCard(ActionableCardStrings.DETECTION_HEADER,
                ActionableCardStrings.DECETION_TITLE, this);
        treatBedBugs = new ActionableCard(ActionableCardStrings.TREATMENT_HEADER,
                ActionableCardStrings.TREATMENT_TITLE, this);
        preventBedBugs = new ActionableCard(ActionableCardStrings.PREVENTION_HEADER,
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
        /////
        Blurb blurb = new Blurb();
        blurb.setTitle(card.getTitle());
        set = getInstructions(card.getHeader());
        ArrayList<Instruction> instructions = set.getInstructions();
        ArrayList<ArrayItem> items = new ArrayList<>();
        items.add(blurb);
        for (Instruction i: instructions){
            items.add(i);
        }

        lessonAdapter = new LessonAdapter(this, R.layout.minimal_lesson, items, card.getHeader());

        set.setAdapter(lessonAdapter);

        final ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setVisibility(View.INVISIBLE);
        listView.setAlpha(0);
        listView.setAdapter(lessonAdapter);


        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                listView.setAlpha(interpolatedTime);
                listView.requestLayout();

                if (interpolatedTime == 1){
                    findViewById(R.id.fab).setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(750);
        listView.startAnimation(a);
        mainPage = false;

        /////

    }

    private InstructionSet getInstructions(String header){
        if (header.equals(ActionableCardStrings.DETECTION_HEADER)){
            return getDetectionInstructions();
        } else if (header.equals(ActionableCardStrings.PREVENTION_HEADER)){
            return getPreventionInstructions();
        } else if (header.equals(ActionableCardStrings.TREATMENT_HEADER)){
            return getTreatmentInstructions();
        }

        return null;
    }

    private InstructionSet getDetectionInstructions(){
        InstructionSet set = new InstructionSet();
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Pull your bed away from the wall", "", set);
        Instruction step2 = new Instruction("Turn on your flashlight", "", "Tap to toggle", set);
        step2.setContext(this);
        step2.setAction(step2.FLASHLIGHT);
        Instruction step3 = new Instruction("With your flashlight, check all dark crevices around the bed",
                "Some reccomended places to check are: piping/seams of the mattress, where the mattress meets the box spring" +
                        ", any sort of gaps, joints where two pieces of wood meet, screw holes, and headboards", EXPAND, set);
        Instruction step4 = new Instruction("You're going to look for eggs, feces, cast skins and potentially the bugs themselves", "This is what they look like",
                EXPAND, set);
        step4.image = decodeSampledBitmapFromResource(getResources(), R.drawable.bed_bug_signs, 300, 300);
        Instruction step5 = new Instruction("As a tip, swiping a credit card or something thin through gaps will expose the bugs", "", set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);

        return set;
    }

    private InstructionSet getPreventionInstructions(){
        InstructionSet set = new InstructionSet();
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Bed bugs can only walk (no hopping/flying), eliminate bed bug travel routes", "It is good practice to move your bed and nightstand slightly away " +
                "from walls, drapes or other items that bed bugs might use to travel onto your bed. Proof Universal interceptors may also " +
                "be placed under each bed leg to stop bugs from climbing up your bed from the floor. Ensure that bed bugs cannot climb up bed sheets and covers by " +
                "keeping sheets and covers out of contact with the floor.",
                EXPAND, set);
        Instruction step2 = new Instruction("Apply diatomaceous earth", "Diatomaceous earth is a naturally occurring, chalk-dust like powder that slowly kills bed bugs that walk over it. It is " +
                "recommended to apply a light dusting of diatomaceous earth along the room perimeter where the wall meets the " +
                "floor, and inside cracks of bed frames, furniture, and all other tight areas where bed bugs typically hide.",
                EXPAND, set);
        Instruction step3 = new Instruction("Regular clean ups", "Regularly clean up loose items from floors, launder clothing and bedding and vacuum floors. This reduces the " +
                "number of bed bug hiding spots in the room.",
                EXPAND, set);
        Instruction step4 = new Instruction("When bringing items home...", "It is " +
                "good practice to seal the furniture and other items into plastic bags along with an appropriate number of [Proof " +
                "Mattress Rescue or Stuff Saver pads] for 48 hours.",
                EXPAND, set);
        Instruction step5 = new Instruction("When travelling, do an inspection", "Inspect mattress seams, head-boards, bed frames, electrical sockets and night stands around " +
                "the bed for bed bugs or bed bug droppings",
                EXPAND, set);
        Instruction step6 = new Instruction("When travelling, luggage and clothing should initially be placed in a safe area", "Place luggage and other items away from the bed and off the floor, inside a [bed bug-Proof laundry " +
                "bag] or inside a sealed plastic bag to prevent bugs from entering the luggage and clothing inside. Placing luggage " +
                "and clothing inside a bathtub will also reduce the chance of infestation because bed bugs can’t climb up the " +
                "slippery surface.",
                EXPAND, set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);
        set.add(step6);

        return set;
    }

    private InstructionSet getTreatmentInstructions(){
        InstructionSet set = new InstructionSet();
        set.setAdapter(lessonAdapter);
        Instruction step1 = new Instruction("Get the necessary equipment", "Obtain Proof Stuff Saver bags, Proof Mattress Rescue bags and plastic garbage bags or Proof bed-bug laundry bags", EXPAND, set);
        Instruction step2 = new Instruction("Remove personal items", "Remove all personal items from bedroom closet(s), drawers, nightstands and other furniture, and seal all " +
                "launderable items into garbage bags or [Proof laundry bags]. Seal all non-launderable items (electronics, books, " +
                "etc.) into [Proof Stuff Saver bags].", EXPAND, set);
        step2.image = decodeSampledBitmapFromResource(getResources(), R.drawable.bedroom_pre_treatment_diagrams_letters, 175, 175);
        Instruction step3 = new Instruction("Place everything effected into Proof Laundry Bags", "Strip the bed(s) and seal all linens, pillowcases, mattress pads, blankets and drapes into [Proof laundry bags] or " +
                "garbage bags.", EXPAND, set);
        step3.image = decodeSampledBitmapFromResource(getResources(), R.drawable.bedroom_bb_post_treatment_diagram_balloons, 175, 175);
        Instruction step4 = new Instruction("Vacuum all visible bugs", "Use a [Proof insect vacuum filter] fitted to your vacuum to suck up all bed bugs and debris " +
                "from floors, cracks and crevices, including under furniture, under bed legs, around the room and " +
                "wherever the wall meets the floor. Once the insect vacuum filter is full of debris/insects, repeat the process. " +
                "After finishing, dispose of " +
                "the filter in the garbage.",
                EXPAND, set);
        Instruction step5 = new Instruction("Pull all furtniture away from the walls", "Once the room is free of all debris and lose items (other than furniture and room fixtures), pull the furniture (beds, " +
                "nightstands, chairs, etc.) away from the walls.",
                EXPAND, set);
        Instruction step6 = new Instruction("Spray all floor surfaces", "Spray all floor surfaces (including the floor underneath furniture), the room’s perimeter and all other cracks and " +
                "crevices with an insecticide labeled for use against bed bugs and bed bug eggs [eg. Proof bed bug spray]. Note: " +
                "Proof has a distinctive odor that can linger for many days. Do not use if you cannot tolerate the odor.",
                EXPAND,  set);
        Instruction step7 = new Instruction("Take apart furniture", "Separate drawers from furniture, and separate mattresses from box-springs and bed frames.",
                EXPAND, set);
        Instruction step8 = new Instruction("Seal all small furniture", "Seal all small furniture (nightstands, drawers, small chairs, etc.) into a [Proof Mattress Saver bag] along with 4 " +
                "opened [Proof Mattress Saver pads] and leave sealed for 48 hours to kill all bed bugs and eggs present in those " +
                "items. Warning: vapor inside the [Proof Mattress Saver bag] is flammable, do not expose bag or vapor to flames, " +
                "spark or heat sources.",
                EXPAND, set);
        step8.image = decodeSampledBitmapFromResource(getResources(), R.drawable.vapor_pad_garbage_bag_instructions, 175, 175);
        Instruction step9 = new Instruction("Seal all bed parts", "Seal all mattresses, box-springs and bed-frames into a [Proof Mattress Saver bag] along with 4 opened [Proof " +
                "Mattress Saver pads] and leave sealed for 48 hours to kill all bed bugs and eggs present in those items. Warning: " +
                "vapor inside the [Proof Mattress Saver bag] is flammable, do not expose the bag or vapor to open flames, sparks or " +
                "heat sources during treatment.",
                EXPAND, set);
        Instruction step10 = new Instruction("Furnitue too large to seal will require special attention", "Any furniture that is too large or heavy to seal into a [Proof Mattress Saver bag] should be sprayed with an " +
                "insecticide labeled for use against bed bugs and bed bug eggs [eg. Proof bed bug spray]. Pay special attention to " +
                "spraying all cracks and crevices on these items and always spray a small, inconspicuous test area to ensure the " +
                "spray does not damage the furniture.",
                EXPAND, set);
        Instruction step11 = new Instruction("Transfer all laundrable items to a dryer", "Transfer all launderable items from sealed garbage bags [or sealed inside Proof laundry bags] to a clothes dryer. " +
                "Empty the launderable items into the dryer [or place the sealed Proof laundry bags directly into the dryer] and dry " +
                "on high heat for 30 minutes or longer to kill all bed bugs and eggs present in those items.",
                EXPAND, set);
        Instruction step12 = new Instruction("Open the bags containing the non-laundrable items and place Proof's Stuff Saver Pads inside", "Open the [Proof Stuff Saver bags] containing all non-launderable items and place 2 opened [Proof Stuff Saver " +
                "pads] inside each bag - leave sealed for 48 hours to kill all bed bugs and eggs present in those items. Warning: " +
                "vapor inside the [Proof Stuff Saver bag] is flammable, do not expose the bag or vapor to open flames, sparks or " +
                "heat sources during treatment.",
                EXPAND, set);
        Instruction step13 = new Instruction("Apply a light dusting of diatomaceous earth", "8-24 hours after the treatment has been applied it is good practice to apply a light dusting of diatomaceous earth " +
                "in all cracks and crevices including under furniture, under bed legs and around the room’s perimeter wherever the " +
                "wall meets the floor. This dusting slowly kills any new bed bugs that try to enter the treated area.",
                EXPAND, set);
        set.add(step1);
        set.add(step2);
        set.add(step3);
        set.add(step4);
        set.add(step5);
        set.add(step6);
        set.add(step7);
        set.add(step8);
        set.add(step9);
        set.add(step10);
        set.add(step11);
        set.add(step12);
        set.add(step13);




        return set;
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


    @Override
    public void onBackPressed() {
        if (mainPage){
            super.onBackPressed();
        } else {
            mainPage = true;
            actionableCardAdapter = new ActionableCardAdapter(this, R.layout.actionable_card, actionableCards);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);


            final ListView listView = (ListView) findViewById(R.id.actionable_card_list);
            final View content = findViewById(R.id.content_main);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    listView.setAlpha(1 - interpolatedTime);
                    content.setAlpha(1 - interpolatedTime);
                    listView.requestLayout();

                    if (interpolatedTime == 1){
                        finishLoading();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration(100);
            listView.startAnimation(a);
        }
    }

    public void finishLoading(){
        final ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        final View content = findViewById(R.id.content_main);

        findViewById(R.id.content_main).setBackgroundColor(getResources().getColor(R.color.gravyGray));
        listView.setAdapter(actionableCardAdapter);
        final View fab = findViewById(R.id.fab);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                listView.setAlpha(interpolatedTime);
                content.setAlpha(interpolatedTime);
                fab.setAlpha(interpolatedTime);
                listView.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(500);
        listView.startAnimation(a);
    }


}
