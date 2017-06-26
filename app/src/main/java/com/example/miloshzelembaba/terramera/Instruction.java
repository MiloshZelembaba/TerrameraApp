package com.example.miloshzelembaba.terramera;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class Instruction extends ArrayItem {

    String stepNumber = "";
    String minimalInstruction = "";
    String detailedInstruction = "";

    public Instruction(String m, String d){
        minimalInstruction = m;
        detailedInstruction = d;
    }

    public String getMinimalInstruction(){
        return minimalInstruction;
    }

    public String getStepNum(){
        return stepNumber;
    }

}
