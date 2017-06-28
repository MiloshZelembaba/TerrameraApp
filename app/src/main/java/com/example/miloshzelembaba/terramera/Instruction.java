package com.example.miloshzelembaba.terramera;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.health.PackageHealthStats;
import android.widget.Toast;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class Instruction extends ArrayItem {

    String stepNumber = "";
    String minimalInstruction = "";
    String detailedInstruction = "";
    String note = "";
    boolean completed = false;
    InstructionSet instructionSet;
    private String action = "";
    private Context context;
    private boolean toggle = false;
    public boolean minimal = true;
    public int image = -1;
    public int detailedHeight = 0;

    /* ACTION STRINGS */
    public static final String FLASHLIGHT = "FLASHLIGHT";

    public Instruction(String m, String d, InstructionSet is){
        minimalInstruction = m;
        detailedInstruction = d;
        instructionSet = is;
    }

    public Instruction(String m, String d, String n, InstructionSet is){
        minimalInstruction = m;
        detailedInstruction = d;
        instructionSet = is;
        note = n;
    }

    public String getMinimalInstruction(){
        return minimalInstruction;
    }

    public String getDetailedInstruction() { return detailedInstruction; }

    public String getStepNum(){
        return stepNumber;
    }

    public void finish(){
        completed = true;
        instructionSet.invalidateAdapter();
        instructionSet.stepCompleted();

        if (!action.isEmpty()){
            performAction();
        }
    }

    public void setAction(String action){
        this.action = action;
    }

    public void performAction(){
        if (action.equals(FLASHLIGHT)){
            toggleFlashLight();
        }
    }

    public void setContext(Context c){
        context = c;
    }



    @TargetApi(Build.VERSION_CODES.M)
    public void toggleFlashLight() {
        toggle = !toggle;
        try {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

            for (String id : cameraManager.getCameraIdList()) {

                // Turn on the flash if camera has one
                if (cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {

                    cameraManager.setTorchMode(id, toggle);

                }
            }

        } catch (Exception e2) {
            Toast.makeText(context, "Torch Failed: " + e2.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
