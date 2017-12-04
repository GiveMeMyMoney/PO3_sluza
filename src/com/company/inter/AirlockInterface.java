package com.company.inter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interfejs sluzy powietrznej.
 *
 */
public interface AirlockInterface {
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
