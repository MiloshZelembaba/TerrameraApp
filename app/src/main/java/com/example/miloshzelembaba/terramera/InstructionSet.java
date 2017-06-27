package com.example.miloshzelembaba.terramera;

import java.util.ArrayList;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class  InstructionSet {

    ArrayList<Instruction> instructions = new ArrayList<>();
    ActionableCard card;
    LessonAdapter lessonAdapter;

    public InstructionSet(ActionableCard card){
        this.card = card;
    }


    public void add(Instruction instruction){
        instructions.add(instruction);

        updateStepNumbers();
    }

    private void updateStepNumbers(){
        for (int i = 0; i < instructions.size(); ++i){
            instructions.get(i).stepNumber = "Step " + (i + 1) + " of " + instructions.size();
        }
    }

    public ArrayList<Instruction> getInstructions(){
        return instructions;
    }

    public void stepCompleted(){
        for (Instruction i: instructions){
            if (!i.completed){
                return;
            }
        }

        card.finish();
    }

    public void setAdapter(LessonAdapter lessonAdapter){
        this.lessonAdapter = lessonAdapter;
    }

    public void invalidateAdapter(){
        lessonAdapter.notifyDataSetChanged();
    }

}
