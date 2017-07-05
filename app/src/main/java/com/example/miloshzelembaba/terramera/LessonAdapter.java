package com.example.miloshzelembaba.terramera;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private int debug = -1;


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
        ViewHolder holder = null;

        int type = getItemViewType(position);
        if (convertView == null){
            if (type == LESSON_CARD){
                /* inflate the lesson card view */
                convertView = inflater.inflate(R.layout.minimal_lesson, parent, false);

                /* create and populate the viewholder */
                holder = new ViewHolder();
                holder.visualInstructions = convertView.findViewById(R.id.visual_representation);
                holder.minimalInstructions = convertView.findViewById(R.id.minimal_instructions);
                holder.detailedInstructions= convertView.findViewById(R.id.detailed_instructions);
                holder.note = convertView.findViewById(R.id.note);
                holder.stepNum= convertView.findViewById(R.id.step_num);

                /* set the viewHolder to the view */
                convertView.setTag(holder);

                /* set onClick listener */
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewHolder holder = (ViewHolder) view.getTag();
                        int currentPosition = holder.position;

                        final Instruction card = (Instruction) lesson.get(currentPosition);

                        if (card.action.isEmpty() && (card.note.equals("Tap to expand") || card.note.equals("Tap to minimize"))) {

                            card.onClick();
                            holder.note.setText(card.note); // update the note text
                            //holder.note.setGravity(Gravity.END);
                            updateDebug(currentPosition);
                            if (card.image != null) {
                                holder.visualInstructions.invalidate();
                                holder.visualInstructions.setImageBitmap(card.image);
                            }

                            //holder.updateVisibilities(card);

                            if (!card.minimal) { // card is still minimal but onClick JUST changed it. ugly i know
                                view.findViewById(R.id.minimal_instructions).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.detailed_instructions).setVisibility(View.GONE);
                                view.findViewById(R.id.visual_representation).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.minimal_instructions).setVisibility(View.GONE);
                                view.findViewById(R.id.detailed_instructions).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.visual_representation).setVisibility(View.INVISIBLE);
                            }

                            final int tmpHeight = view.getHeight();


                            TextView text = view.findViewById(R.id.detailed_instructions);
                            text.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                                    View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST));
                            final int targetHeight = text.getMeasuredHeight();

                            int imageHeight = 0;
                            if (currentPosition == 2 || currentPosition == 3 || currentPosition == 8) {
                                ImageView image = view.findViewById(R.id.visual_representation);
                                image.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                                        View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST));
                                imageHeight = image.getMeasuredHeight();
                            } else {
                                view.findViewById(R.id.visual_representation).setVisibility(View.GONE);
                            }

                            if (!card.minimal) {
                                card.maximalHeight = targetHeight + imageHeight;
                                expand(view, card, targetHeight + imageHeight, tmpHeight);
                            } else {
                                shrink(view, card, card.minimalHeight);
                            }
                        } else {
                            card.performAction();
                        }

//                        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
//                                View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST));
//                        System.out.println("final height: " + view.getMeasuredHeight());

                    }
                });

            } else {
                Blurb blurb = (Blurb) lesson.get(position);
                convertView = inflater.inflate(R.layout.lesson_header, parent, false);
                TextView header = convertView.findViewById(R.id.lesson_title);
                header.setText(blurb.title);
            }
        } else {
            if (type == LESSON_CARD) {
                holder = (ViewHolder) convertView.getTag();
            }
        }


        if (type == LESSON_CARD){
            Instruction card = (Instruction) lesson.get(position);

            /* fill up the viewHolder's views */
            holder.detailedInstructions.setText(card.getDetailedInstruction());
            holder.minimalInstructions.setText(card.getMinimalInstruction());
            holder.note.setText(card.note);
            //holder.note.setGravity(Gravity.END);
            holder.stepNum.setText(card.getStepNum());
            holder.position = position;

//            if (card.image != null) {
//                if (position == 2 || position == 3 || position == 8 ) {
//                    holder.visualInstructions.setImageBitmap(card.image);
//                } else {
//                    card.action += "";
//                }
//            } else {
//                holder.visualInstructions = new ImageView(activity);
//            }

            convertView.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;

            holder.updateVisibilities(card);
            debug = -1;
        }

        return convertView;
    }

    public void updateDebug(int i){
        debug = i;
    }

    public static void shrink(final View v, final Instruction card, final int height) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int baseHeight = v.getHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        //v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation aaa = new AlphaAnimation(1.0f, 0.0f);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(baseHeight - ((baseHeight - height) * interpolatedTime));

                if (interpolatedTime == 1){
                    v.findViewById(R.id.minimal_instructions).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.detailed_instructions).setVisibility(View.GONE);
                    v.findViewById(R.id.visual_representation).setVisibility(View.GONE);
                    card.inTransition = false;
                }

                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        aaa.setDuration(250);
        ///v.findViewById(R.id.detailed_instructions).startAnimation(aaa);
        a.setStartOffset(250);
        aa.setStartOffset(250);
        a.setDuration(750);
        aa.setDuration(750);
        v.findViewById(R.id.minimal_instructions).startAnimation(aa);
        v.startAnimation(a);
    }

    public static void expand(final View v, final Instruction card, final int height, final int baseHeight) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        card.minimalHeight = baseHeight;

        AlphaAnimation aaa = new AlphaAnimation(1.0f, 0.0f);
        aaa.setDuration(250);
        //v.findViewById(R.id.minimal_instructions).startAnimation(aaa);

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                v.getLayoutParams().height = interpolatedTime == 1
//                        ? ViewGroup.LayoutParams.WRAP_CONTENT
//                        : (int)(baseHeight + (height * interpolatedTime));

                v.getLayoutParams().height =
                        (int)(baseHeight + (height * interpolatedTime)); // gonna leave this for now....

                if (interpolatedTime == 1){
                    v.findViewById(R.id.detailed_instructions).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.minimal_instructions).setVisibility(View.GONE);
                    v.findViewById(R.id.visual_representation).setVisibility(View.VISIBLE);
//                    v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
//                            View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST));
//                    System.out.println("final height: " + v.getMeasuredHeight());
                    card.inTransition = false;
                }

                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setStartOffset(250);
        aa.setStartOffset(250);
        a.setDuration(750);
        aa.setDuration(1250);
        v.findViewById(R.id.detailed_instructions).startAnimation(aa);
        v.startAnimation(a);
    }

    private static class ViewHolder {
        private TextView minimalInstructions;
        private TextView detailedInstructions;
        private ImageView visualInstructions;
        private TextView stepNum;
        private TextView note;
        public int position = -1;

        public void updateVisibilities(Instruction card){
            if (card.inTransition){
                return;
            }

            if (card.minimal){
                minimalInstructions.setVisibility(View.VISIBLE);
                detailedInstructions.setVisibility(View.GONE);
                visualInstructions.setVisibility(View.GONE);
            } else {
                minimalInstructions.setVisibility(View.GONE);
                detailedInstructions.setVisibility(View.VISIBLE);
                visualInstructions.setVisibility(View.VISIBLE);
            }
        }
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

}
