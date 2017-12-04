package com.company;

import com.company.inter.AirlockInterface;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Airlock airlock = new Airlock();
        airlock.setState(AirlockInterface.State.INTERNAL_DOOR_CLOSED);
        airlock.closeInternalDoor();
        System.out.println(airlock.newStates() + "\n");
        airlock.closeExternalDoor();
        airlock.openInternalDoor();
        airlock.closeInternalDoor();
        airlock.openExternalDoor();
        airlock.openInternalDoor(); //DISASTER!
        System.out.println(airlock.newStates() + "\n");
        airlock.openInternalDoor();
        airlock.closeExternalDoor();
        airlock.closeInternalDoor();
        System.out.println(airlock.newStates() + "\n");
        System.out.println(airlock.getState() + "\n");
        System.out.println(airlock.getHistory() + "\n");
        System.out.println(airlock.getUsageCounters() + "\n");


        airlock.setState(AirlockInterface.State.INTERNAL_DOOR_CLOSED);
        airlock.closeInternalDoor();
        System.out.println(airlock.newStates() + "\n");
        System.out.println(airlock.getState() + "\n");
        System.out.println(airlock.getHistory() + "\n");
        System.out.println(airlock.getUsageCounters() + "\n");

        airlock.setState(AirlockInterface.State.DISASTER);
        airlock.closeInternalDoor();airlock.closeInternalDoor();airlock.closeInternalDoor();
        System.out.println(airlock.newStates() + "\n");
        System.out.println(airlock.getState() + "\n");
        System.out.println(airlock.getHistory() + "\n");
        System.out.println(airlock.getUsageCounters() + "\n");
    }

}

class Airlock implements AirlockInterface {
    List<State> stateHistoryList;
    Map<State, Integer> usageCounterMap;

    public Airlock() {
        stateHistoryList = new ArrayList<>();
        usageCounterMap = new HashMap<>();
    }

    //AirlockInterface

    @Override
    public void setState(State state) {
        stateHistoryList = new ArrayList<>(Collections.singletonList(state));
        usageCounterMap = new HashMap<>();
        usageCounterMap.put(state, 1);
    }

    @Override
    public State getState() {
        return stateHistoryList.get(stateHistoryList.size() - 1);
    }

    @Override
    public Set<State> newStates() {
        State actualState = getState(); //stan aktualny
        Set<State> possibleStates = new HashSet<>();
        if (State.DISASTER == actualState) {
            //do nothing
        } else if (State.INTERNAL_DOOR_CLOSED == actualState) {
            possibleStates.addAll(Arrays.asList(State.INTERNAL_DOOR_OPENED, State.EXTERNAL_DOOR_CLOSED, State.EXTERNAL_DOOR_OPENED));
        } else if (State.INTERNAL_DOOR_OPENED == actualState) {
            possibleStates.addAll(Arrays.asList(State.INTERNAL_DOOR_CLOSED, State.EXTERNAL_DOOR_CLOSED, State.EXTERNAL_DOOR_OPENED, State.DISASTER));
        } else if (State.EXTERNAL_DOOR_CLOSED == actualState) {
            possibleStates.addAll(Arrays.asList(State.EXTERNAL_DOOR_OPENED, State.INTERNAL_DOOR_CLOSED, State.INTERNAL_DOOR_OPENED));
        } else if (State.EXTERNAL_DOOR_OPENED == actualState) {
            possibleStates.addAll(Arrays.asList(State.EXTERNAL_DOOR_CLOSED, State.INTERNAL_DOOR_CLOSED, State.INTERNAL_DOOR_OPENED, State.DISASTER));
        }
        return possibleStates;
    }

    @Override
    public List<State> getHistory() {
        return stateHistoryList;
    }

    @Override
    public Map<State, Integer> getUsageCounters() {
        return usageCounterMap;
    }

    private void setStateHistory(State state) {
        stateHistoryList.add(state);
        Integer count = usageCounterMap.get(state);
        usageCounterMap.put(state, count != null ? ++count : 1);
    }

    @Override
    public void closeInternalDoor() {
        State actualState = getState(); //stan aktualny
        if (State.DISASTER == actualState) {
            setStateHistory(State.DISASTER);
        } else {
            setStateHistory(State.INTERNAL_DOOR_CLOSED);
        }
    }

    @Override
    public void closeExternalDoor() {
        State actualState = getState(); //stan aktualny
        if (State.DISASTER == actualState) {
            setStateHistory(State.DISASTER);
        } else {
            setStateHistory(State.EXTERNAL_DOOR_CLOSED);
        }

    }

    @Override
    public void openInternalDoor() {
        State actualState = getState(); //stan aktualny
        if (State.DISASTER == actualState || State.EXTERNAL_DOOR_OPENED == actualState) {
            setStateHistory(State.DISASTER);
        } else {
            setStateHistory(State.INTERNAL_DOOR_OPENED);
        }
    }

    @Override
    public void openExternalDoor() {
        State actualState = getState(); //stan aktualny
        if (State.DISASTER == actualState || State.INTERNAL_DOOR_OPENED == actualState) {
            setStateHistory(State.DISASTER);
        } else {
            setStateHistory(State.EXTERNAL_DOOR_OPENED);
        }
    }
}
