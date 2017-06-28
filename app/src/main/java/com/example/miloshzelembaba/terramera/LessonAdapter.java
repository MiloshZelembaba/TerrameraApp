package com.example.miloshzelembaba.terramera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        if (true){ // TODO: should properly utilize viewHolder
            if (type == LESSON_CARD){
                final Instruction card = (Instruction) lesson.get(position);
                v = inflater.inflate(R.layout.minimal_lesson, parent, false);

                TextView stepNum = v.findViewById(R.id.step_num);
                stepNum.setText(card.getStepNum());
                TextView minmalInstruction = v.findViewById(R.id.minimal_instructions);
                minmalInstruction.setText(card.getMinimalInstruction());


                if (card.image != -1) {
                    ImageView img = v.findViewById(R.id.visual_representation);
                    //img.setImageResource(R.drawable.bedroom_bb_post_treatment_diagram_balloons);

                    img.setImageBitmap(
                            decodeSampledBitmapFromResource(activity.getResources(), card.image, 175, 175));
                }


                if (!card.note.equals("")){
                    TextView tmp = (TextView) v.findViewById(R.id.note);
                    tmp.setText(card.note);
                }

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        card.setContext(activity);
                        if (card.minimal && !card.getDetailedInstruction().isEmpty()) {
                            TextView minmalInstruction = view.findViewById(R.id.minimal_instructions);
                            minmalInstruction.setText(card.getDetailedInstruction());

                            TextView minimize = view.findViewById(R.id.note);
                            if (minimize.getText() == "Tap to expand") {
                                minimize.setText("Tap to minimize");
                                ImageView img = view.findViewById(R.id.visual_representation);
                                img.setVisibility(View.VISIBLE);

                                card.minimal = false;
                            }

                            //expand(view,card,baseHeight);
                        } else {
                            TextView minmalInstruction = view.findViewById(R.id.minimal_instructions);
                            minmalInstruction.setText(card.getMinimalInstruction());

                            TextView minimize = view.findViewById(R.id.note);
                            if (minimize.getText() == "Tap to minimize") {
                                minimize.setText("Tap to expand");
                                ImageView img = view.findViewById(R.id.visual_representation);
                                img.setVisibility(View.GONE);

                                card.minimal = true;
                            }
                        }
                        card.performAction();
                    }
                });
            } else {
                Blurb blurb = (Blurb) lesson.get(position);
                v = inflater.inflate(R.layout.lesson_header, parent, false);
                TextView header = v.findViewById(R.id.lesson_title);
                header.setText(blurb.title);
            }
        }

        return v;

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static void expand(final View v, Instruction card, final int baseHeight) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        System.out.println(baseHeight);
        System.out.println("t: " + targetHeight);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(baseHeight + ((targetHeight - baseHeight) * interpolatedTime));
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(1000);
        v.startAnimation(a);
    }

}
