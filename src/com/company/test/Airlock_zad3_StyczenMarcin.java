import java.util.*;

/**
 * Interfejs sluzy powietrznej.
 *
 */
interface AirlockInterface {
    /**
     * Typ wyliczeniowy reprezentujacy wszystkie stany, ktore moze przyjac sluza.
     *
     */
    public enum State {
        INTERNAL_DOOR_CLOSED, EXTERNAL_DOOR_CLOSED, INTERNAL_DOOR_OPENED, EXTERNAL_DOOR_OPENED, DISASTER
    }

    /**
     * Ustawienie stanu. Historia {@link #getHistory()} uzycia jest kasowana i state
     * umieszczany jest jako jej pierwszy element. Statystyka uzycia
     * {@link #getUsageCounters()} jest takze kasowana, tylko state ma licznik
     * ustawiony na 1, wszystkie pozostale stany maja ustawione 0.
     *
     * @param state
     *            stan, w ktorym znajdzie sie sluza po wykonaniu metody.
     */
    public void setState(State state);

    /**
     * Metoda zwraca aktualny stan sluzy.
     *
     * @return aktualny stan sluzy
     */
    public State getState();

    /**
     * Metoda zwraca zbior stanow, ktore mozna osiagnac ze stanu aktualnego. Zbior
     * nie zawiera stanu, w ktorym sluza aktualnie sie znajduje.
     *
     * @return wszystkie inne stany, ktore sa osiagalne z aktualnego
     */
    public Set<State> newStates();

    /**
     * Historia stanow sluzy. Sasiednie pozycje listy moze zajmowac ten sam stan -
     * dlatego jest to lista, a nie zbior stanow, bo ten zawieralby maksymalnie 5
     * stanow.
     *
     * @return historia stanow sluzy od wykonania {@link #setState} po stan aktualny
     */
    public List<State> getHistory();

    /**
     * Statystyka uzycia stanow sluzy. Mapa, w ktorej kluczem jest stan a wartoscia
     * ilosc wystapien tego stanu od wykonania metody {@link #setState} po stan
     * aktualny wlacznie.
     *
     * @return statystyka uzycia stanow sluzy
     */
    public Map<State, Integer> getUsageCounters();

    /**
     * Zlecenie zamkniecia drzwi wewnetrznych
     */
    public void closeInternalDoor();

    /**
     * Zlecenie zamkniecia drzwi zewnetrznych
     */
    public void closeExternalDoor();

    /**
     * Zlecenie otwarcia drzwi wewnetrznych
     */
    public void openInternalDoor();

    /**
     * Zlecenie otwarcia drzwi zewnetrznych
     */
    public void openExternalDoor();
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
        usageCounterMap.put(AirlockInterface.State.DISASTER, 0);
        usageCounterMap.put(AirlockInterface.State.EXTERNAL_DOOR_CLOSED, 0);
        usageCounterMap.put(AirlockInterface.State.EXTERNAL_DOOR_OPENED, 0);
        usageCounterMap.put(AirlockInterface.State.INTERNAL_DOOR_CLOSED, 0);
        usageCounterMap.put(AirlockInterface.State.INTERNAL_DOOR_OPENED, 0);
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