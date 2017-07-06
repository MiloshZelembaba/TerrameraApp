package com.example.miloshzelembaba.terramera;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.miloshzelembaba.terramera.LessonAdapter.decodeSampledBitmapFromResource;

public class LessonActivity extends AppCompatActivity {

    private LessonAdapter lessonAdapter;
    private InstructionSet set;
    private String header;
    private Map<String, Boolean> lessonCompletion = new HashMap<>();
    private final String EXPAND = "Tap to expand";

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        header = intent.getStringExtra("Header");
        setTitle(header);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = intent.getStringExtra("Title");
        int colour = intent.getIntExtra("Colour",0);

        updateLessonCompletion(getIntent());

        Window window = getWindow();
        Fade slide = new Fade();
        window.setAllowEnterTransitionOverlap(true);
        window.setAllowReturnTransitionOverlap(true);
        slide.setInterpolator(new LinearInterpolator());
        //slide.setSlideEdge(Gravity.RIGHT);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        window.setEnterTransition(slide); // The Transition to use to move Views into the initial Scene.
        window.setReturnTransition(slide);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gravyGray)));

        //////////
        findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(colour));
        findViewById(R.id.content_main).setBackgroundColor(getResources().getColor(colour));

        Blurb blurb = new Blurb();
        blurb.setTitle(title);
        set = getInstructions(header);
        ArrayList<Instruction> instructions = set.getInstructions();
        ArrayList<ArrayItem> items = new ArrayList<>();
        items.add(blurb);
        for (Instruction i: instructions){
            items.add(i);
        }

        lessonAdapter = new LessonAdapter(this, R.layout.minimal_lesson, items);

        set.setAdapter(lessonAdapter);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(lessonAdapter);
        ////////

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
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
        Instruction step4 = new Instruction("Vacuum up all visible bugs", "Use a [Proof insect vacuum filter] fitted to your vacuum hose, suck up all bed bugs and debris " +
                "from floors, cracks and crevices, including under furniture, under bed legs, and around the room " +
                "wherever the wall meets the floor. Once the insect vacuum filter is full of debris/insects, repeat the process. " +
                "After you have finished vacuuming, dispose of " +
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
    @TargetApi(Build.VERSION_CODES.M)
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        if (set.completed && !lessonCompletion.containsKey(header)) {
            lessonCompletion.put(header, set.completed);
        }

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
        //return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
