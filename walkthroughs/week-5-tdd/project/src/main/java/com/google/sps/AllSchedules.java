package com.google.sps;
import java.util.HashMap;
import java.util.ArrayList;

public class AllSchedules {
    
    private HashMap<String, ArrayList<TimeRange>> mandatorySchedules;
    private HashMap<String, ArrayList<TimeRange>> optionalSchedules;

    public AllSchedules() {
        this.mandatorySchedules = new HashMap<String, ArrayList<TimeRange>>();
        this.optionalSchedules = new HashMap<String, ArrayList<TimeRange>>();
    }
    public HashMap<String, ArrayList<TimeRange>> getMandatorySchedules() {
        return this.mandatorySchedules;
    }
    public HashMap<String, ArrayList<TimeRange>> getOptionalSchedules() {
        return this.optionalSchedules;
    }
    public void setMandatorySchedules(HashMap<String, ArrayList<TimeRange>> updatedSchedules) {
        this.mandatorySchedules = updatedSchedules;
    }
    public void setOptionalSchedules(HashMap<String, ArrayList<TimeRange>> updatedSchedules) {
        this.optionalSchedules = updatedSchedules;
    }
    
}