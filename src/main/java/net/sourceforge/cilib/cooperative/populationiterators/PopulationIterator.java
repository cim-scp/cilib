package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.Algorithm;

public interface PopulationIterator extends ListIterator<Algorithm> {
	
	public PopulationIterator clone();
	
	public void setPopulations(List<Algorithm> participants);
	
}