package main;

import java.util.List;

public interface Simulation {

	/**
	 * Typ wyliczeniowy umoĹźliwiajÄcy wskazania wzoru stosowanego w obliczeniach.
	 */
	public enum ProbabilityFormula {
		GLAUBER, METROPOLIS;
	}

	/**
	 * Interfejs zawierajÄcy informacje o parametrach sieci.
	 */
	public interface LatticeParameters {
		/**
		 * Energia caĹkowita ukĹadu magnesĂłw
		 * 
		 * @return energia caĹkowita
		 */
		public double totalEnergy();

		/**
		 * Parametr uporzÄdkowania
		 * 
		 * @return porzÄdek
		 */
		public double orderParameter();

		/**
		 * Parametr uporzÄdkowania dla najbliĹźszych sÄsiadĂłw.
		 * 
		 * @return uporzÄdkowanie najbliĹźszych sÄsiadĂłw
		 */
		public double nearestNeighbourOrder();

		/**
		 * Stan sieci magnesĂłw. Kierunki zapisane w tablicy mogÄ zawieraÄ siÄ wyĹÄcznie
		 * w zakresie od 0 do states-1.
		 * 
		 * @return stan magnesĂłw
		 */
		public int[][] lattice();
	}

	/**
	 * Metoda ustawia poczÄtkowy stan sieci magnesĂłw oraz liczbÄ stanĂłw (kierunkĂłw),
	 * ktĂłre moĹźe przyjÄÄ kaĹźdy z magnesĂłw. SieÄ jest sieciÄ kwadratowÄ. Metoda
	 * powinna przekazanÄ tablicÄ skopiowaÄ.
	 * 
	 * @param lattice stan sieci
	 * @param states  liczba stanĂłw jakie moĹźe przyjÄÄ kierunek magnesu
	 */
	public void setLattice(int[][] lattice, int states);

	/**
	 * Ustawienie parametrĂłw oddziaĹywania magnesĂłw. Lista zawiera na pozycji o
	 * indeksie 0 parametr oddziaĹywania z zewnÄtrznym polem. Na pozycjach od 1 do
	 * parameters.size()-1 parametry oddziaĹywania z sÄsiadami poziomu 1 i
	 * kolejnymi.
	 * 
	 * @param parameters        parametry oddziaĹywania
	 * @param externaFieldAngle kÄt ustawienia zewnÄtrznego pola. WartoĹÄ w zakresie
	 *                          od 0 do 2xPI.
	 */
	public void setEnergyParameters(List<Double> parameters, double externaFieldAngle);

	/**
	 * WybĂłr formuĹy uĹźywanej do wyznaczania prawdopodobieĹstwa zmiany stanu ukĹadu.
	 * 
	 * @param formula nazwa uĹźywanej formuĹy
	 */
	public void setProbabilityFormula(ProbabilityFormula formula);

	/**
	 * Ustalenie aktualnej temperatury.
	 * 
	 * @param TkB temperatura w postaci iloczynu T i staĹej Boltzmanna.
	 */
	public void setTemperatureBoltzmannConstant(double TkB);

	/**
	 * Zlecenie wykonania okreĹlonej liczby krokĂłw MC. Stan poczÄtkowy sieci
	 * magnesĂłw to albo stan ustawiony za pomocÄ setLattice lub stan po
	 * wczeĹniejszym wykonaniu executeMCSteps.
	 * 
	 * @param steps liczba krokĂłw do wykonania.
	 */
	public void executeMCSteps(int steps);

	/**
	 * Pobranie stanu sieci magnesĂłw.
	 * 
	 * @return obiekt zgodny z interfejsem LatticeParameters.
	 */
	public LatticeParameters getState();
}
