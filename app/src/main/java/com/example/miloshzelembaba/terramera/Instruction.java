package com.example.miloshzelembaba.terramera;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class Instruction extends ArrayItem {

    String stepNumber = "";
    String minimalInstruction = "";
    String detailedInstruction = "";
    boolean completed = false;
    InstructionSet instructionSet;

    public Instruction(String m, String d, InstructionSet is){
        minimalInstruction = m;
        detailedInstruction = d;
        instructionSet = is;
    }

    public String getMinimalInstruction(){
        return minimalInstruction;
    }

    public String getStepNum(){
        return stepNumber;
    }

    public void finish(){
        completed = true;
        instructionSet.stepCompleted();
    }

}
