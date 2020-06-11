// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import com.google.sps.*;
import java.util.*;
public final class FindMeetingQuery {

  /* Given all events, return a HashMap that contains the schedule of mandatory attandees and optional attandees 
  param: @events: all events containing time and attandees list 
         @request: Meeting requests containing attandees list
  return: @allSchedules a hashmap with 2 key: 'mandatory' and 'optional', the values are another HashMap whose key is attandee and 
          value is the schedule of that attandee
  */
  public static HashMap<String, HashMap<String, ArrayList<TimeRange>>> getSchedules(Collection<Event> events, MeetingRequest request) {
    Collection<String> mandatoryAttandeeSet = request.getAttendees();
    Collection<String> optionalAttandeeSet = request.getOptionalAttendees();
    HashMap<String, HashMap<String, ArrayList<TimeRange>>> allSchedules = new HashMap<String, HashMap<String, ArrayList<TimeRange>>>();
    for (Event event: events) {
        TimeRange timeRange = event.getWhen();
        Set<String> allAttandees = event.getAttendees();
        for (String attandee : allAttandees) {
            if (mandatoryAttandeeSet.contains(attandee)) {
                HashMap<String, ArrayList<TimeRange>> mandatorySchedules = allSchedules.getOrDefault("mandatory", new HashMap<String, ArrayList<TimeRange>>());
                ArrayList<TimeRange> attandeeSchedule = mandatorySchedules.getOrDefault(attandee, new ArrayList<TimeRange>());
                attandeeSchedule.add(timeRange);
                mandatorySchedules.put(attandee, attandeeSchedule);
                allSchedules.put("mandatory", mandatorySchedules);
            }
            else if (optionalAttandeeSet.contains(attandee)){
                HashMap<String, ArrayList<TimeRange>> optionalSchedules = allSchedules.getOrDefault("optional", new HashMap<String, ArrayList<TimeRange>>());
                ArrayList<TimeRange> attandeeSchedule = optionalSchedules.getOrDefault(attandee, new ArrayList<TimeRange>());
                attandeeSchedule.add(timeRange);
                optionalSchedules.put(attandee, attandeeSchedule);
                allSchedules.put("optional", optionalSchedules);
            }
            
        }
    }
    return allSchedules;
  }
  /*Given two overlapped TimeRanges, return the merged TimeRange
  param: @one and @two: overlapped TimeRanges
  return: @mergeTimeSlot: merge TimeRange
  */
  public static TimeRange mergeTwoOverlappedTimeRanges(TimeRange one, TimeRange two) {
    int start = (one.start() < two.start())? one.start() : two.start();
    int end = (one.end() > two.end())? one.end() : two.end();
    TimeRange mergedTimeSlot = TimeRange.fromStartEnd(start, end, false);
    return mergedTimeSlot;
  }

  /*Given a List of acummulated TimeRange and a TimeRange to add (assuming the TimeRange can overlap with the last TimeRange in the list)
  param: @accum: the list of accumuated TimeRange
         @newTimeRange: to be added
  return @accum the updated accumulated list
  */
  public static ArrayList<TimeRange> mergeTimeRangeWithAccumulatedTimeRanges(ArrayList<TimeRange> accum, TimeRange newTimeRange) {
    TimeRange lastTimeRange = (accum.size() == 0)? null: accum.get(accum.size() - 1);
    if (lastTimeRange == null) {
        ArrayList<TimeRange> newAccum = new ArrayList<>();
        newAccum.add(newTimeRange);
        return newAccum;
    }
    else {
        if (lastTimeRange.overlaps(newTimeRange)) {
            accum.remove(accum.size() - 1);
            TimeRange mergedTimeRange = mergeTwoOverlappedTimeRanges(lastTimeRange, newTimeRange);
            accum.add(mergedTimeRange);
        }
        else {
            accum.add(newTimeRange);
        }
        return accum;
    }    
  }

  /* Given a list of schedules and an optional list of pre-existing TimeRange, return a new list of TimeRange that merges all the schedules and pre-existing TimeRange
  param: @allSchedules: a hashMap whose key is attandee, value is the schedule of that attandee
         @preExistingTimeRanges: a list of pre-existing TimeRange or null
  return: @possibleSlots: a list of possible TimeRange to fit all mandatory attandees
  */
  public static List<TimeRange> mergeAllSchedules(HashMap<String, ArrayList<TimeRange>> allSchedules, List<TimeRange> preExistingTimeRanges) {
    // Merge all schedules of attandees using merging two sorted list algorithm
    List<TimeRange> mergeSchedule = (preExistingTimeRanges != null)? preExistingTimeRanges: new ArrayList<>();

    for (String attandee: allSchedules.keySet()) {
        ArrayList<TimeRange> attandeeSchedule = allSchedules.get(attandee);
        Comparator<TimeRange> comparatorByStart = TimeRange.ORDER_BY_START;
        Collections.sort(attandeeSchedule, comparatorByStart);
        allSchedules.put(attandee, attandeeSchedule);
        
        System.out.println(attandeeSchedule); 
        int index1 = 0;
        int index2 = 0;
        ArrayList<TimeRange> newMergeSchedule = new ArrayList<>();
        while (index1 < mergeSchedule.size() && index2 < attandeeSchedule.size()) {
            TimeRange mergeSlot = mergeSchedule.get(index1);
            TimeRange attandeeSlot = attandeeSchedule.get(index2);

            if (mergeSlot.overlaps(attandeeSlot)) {
                TimeRange newMergeSlot = mergeTwoOverlappedTimeRanges(mergeSlot, attandeeSlot);
                newMergeSchedule = mergeTimeRangeWithAccumulatedTimeRanges(newMergeSchedule, newMergeSlot);
                index1++;
                index2++;
            }
            else {
                if (mergeSlot.start() < attandeeSlot.start()) {
                    newMergeSchedule = mergeTimeRangeWithAccumulatedTimeRanges(newMergeSchedule, mergeSlot);
                    index1++;
                } else {
                    newMergeSchedule = mergeTimeRangeWithAccumulatedTimeRanges(newMergeSchedule, attandeeSlot);
                    index2++;
                }
            }

        }
        while (index1 < mergeSchedule.size()) {
            TimeRange mergeSlot = mergeSchedule.get(index1);
            newMergeSchedule = mergeTimeRangeWithAccumulatedTimeRanges(newMergeSchedule, mergeSlot);
            index1++;
        }
        while (index2 < attandeeSchedule.size()) {
            TimeRange attandeeSlot = attandeeSchedule.get(index2);
            newMergeSchedule = mergeTimeRangeWithAccumulatedTimeRanges(newMergeSchedule, attandeeSlot);
            index2++;
        }
        mergeSchedule = newMergeSchedule;
    }
    return mergeSchedule;
  }
  /* Consider if each 'missing' TimeRange in the merged schedule will be long enough to fit the meeting in
  param: @mergeSchedule: a list of TimeRange that merges all schedule
         @request: a MeetingRequest contains the duration of the meeting
  return: return a list possible TimeRange that fits all given schedules
  */
  public static List<TimeRange> fitAllSchedules(List<TimeRange> mergeSchedule, MeetingRequest request) {
    ArrayList<TimeRange> possibleSlots = new ArrayList<>();

    if (mergeSchedule.size() == 0) {
        if (TimeRange.END_OF_DAY - TimeRange.START_OF_DAY >= request.getDuration()) {
            possibleSlots.add(TimeRange.WHOLE_DAY);
        }
    }
    else {
        for (int i = -1; i < mergeSchedule.size(); i++) {
            if (i == -1) {
                TimeRange next = mergeSchedule.get(i + 1);
                if (next.start() - TimeRange.START_OF_DAY >= request.getDuration()) {
                    possibleSlots.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, next.start(), false));
                }
                
            }
            else if (i == mergeSchedule.size() - 1) {
                TimeRange curr = mergeSchedule.get(i);
                if (TimeRange.END_OF_DAY - curr.end() >= request.getDuration()) {
                    possibleSlots.add(TimeRange.fromStartEnd(curr.end(), TimeRange.END_OF_DAY, true));
                }
                
            }
            else {
                TimeRange curr = mergeSchedule.get(i);
                TimeRange next = mergeSchedule.get(i + 1);
                if (next.start() - curr.end() >= request.getDuration()) {
                    possibleSlots.add(TimeRange.fromStartEnd(curr.end(), next.start(), false));
                }
            }
        }
    }
    
    return possibleSlots;
  }
  /* Given mandatory attandees' schedule, return a list of possible TimeRange to fit all mandatory attandees
  param: @mandatorySchedules: a hashMap whose key is attandee, value is the schedule of that attandee
  return: @possibleSlots: a list of possible TimeRange to fit all mandatory attandees
  */
  public static List<TimeRange> fitAllMandatory(HashMap<String, ArrayList<TimeRange>> mandatorySchedules, MeetingRequest request) {
    List<TimeRange> mergeSchedule = mergeAllSchedules(mandatorySchedules, null);
    List<TimeRange> fitAllMandatory = fitAllSchedules(mergeSchedule, request);
    return fitAllMandatory;
  }

  /* Given allSchedules, return a list of possible TimeRange that satisfies following condition:
   "if one or more time slots exists so that both mandatory and optional attendees can attend, return those time slots. Otherwise, 
   return the time slots that fit just the mandatory attendees."
  param: @allSchedules a hashmap with 2 key: 'mandatory' and 'optional', the values are another HashMap whose key is attandee and 
          value is the schedule of that attandee
         @request: a MeetingRequest contains the duration of the meeting
  return: @fitMandatoryAndOptional or @fitAllMandatory
  */
  
  public static List<TimeRange> basicFitOptional (HashMap<String, HashMap<String, ArrayList<TimeRange>>> allSchedules, MeetingRequest request) {
    // Merge all schedules of mandatory and optional attandees
    HashMap<String, ArrayList<TimeRange>> mandatorySchedules = allSchedules.getOrDefault("mandatory", new HashMap<String, ArrayList<TimeRange>>());
    HashMap<String, ArrayList<TimeRange>> optionalSchedules = allSchedules.getOrDefault("optional", new HashMap<String, ArrayList<TimeRange>>());

    List<TimeRange> mergeMandatorySchedules = mergeAllSchedules(mandatorySchedules, null);
    List<TimeRange> fitAllMandatory = fitAllSchedules(mergeMandatorySchedules, request);

    List<TimeRange> mergeMandatoryAndOptionalSchedules = mergeAllSchedules(optionalSchedules, mergeMandatorySchedules);
    List<TimeRange> fitMandatoryAndOptional = fitAllSchedules(mergeMandatoryAndOptionalSchedules, request);
    
    return (request.getAttendees().size() != 0 && fitMandatoryAndOptional.size() == 0)? fitAllMandatory : fitMandatoryAndOptional;
  }

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    HashMap<String, HashMap<String, ArrayList<TimeRange>>> allSchedules = getSchedules(events, request);
    List<TimeRange> basicFitOptional = basicFitOptional(allSchedules, request);
    return basicFitOptional;
  }
}