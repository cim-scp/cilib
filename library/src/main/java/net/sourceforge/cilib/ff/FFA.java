/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.ff.iterationstrategies.StandardFireflyIterationStrategy;
import net.sourceforge.cilib.ff.firefly.Firefly;
import net.sourceforge.cilib.ff.firefly.StandardFirefly;

/**
 * <p>
 * An implementation of the standard Firefly algorithm.
 * </p>
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li>Yang, Xin-She. "Firefly algorithms for multimodal optimization."
 * Stochastic algorithms: foundations and applications (2009): 169-178.
 * </li>
 * </ul>
 * </p>
 */
public class FFA extends SinglePopulationBasedAlgorithm {

    private Topology<Firefly> topology;
    private IterationStrategy<FFA> iterationStrategy;

    /**
     * Creates a new instance of the <code>Firefly</code> algorithm.
     * All fields are initialised to reasonable defaults.
     */
    public FFA() {
        topology = new GBestTopology<Firefly>();

        iterationStrategy = new StandardFireflyIterationStrategy();

        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new StandardFirefly());
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public FFA(FFA copy) {
        super(copy);
        this.topology = copy.topology.getClone();
        this.iterationStrategy = copy.iterationStrategy;
        this.initialisationStrategy = copy.initialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FFA getClone() {
        return new FFA(this);
    }

    /**
     * Perform the required initialisation for the algorithm. Create the fireflies
     * and add them to the specified topology.
     */
    @Override
    public void algorithmInitialisation() {
        Iterable<Firefly> particles = (Iterable<Firefly>) this.initialisationStrategy.initialise(this.getOptimisationProblem());
        topology.clear();
        topology.addAll(Lists.<Firefly>newLinkedList(particles));

        for (Firefly p : topology) {
            p.calculateFitness();
        }
    }

    /**
     * Perform the iteration of the Firefly algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * Get the best current solution.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Firefly bestEntity = Topologies.getBestEntity(topology, new DescendingFitnessComparator<Firefly>());
        return new OptimisationSolution(bestEntity.getPosition(), bestEntity.getFitness());
    }

    /**
     * Sets the firefly topology used. The default is {@link GBestTopology}.
     * @param topology A class that implements the {@link Topology} interface.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * Accessor for the topology being used.
     * @return The {@link Topology} being used.
     */
    @Override
    public Topology<Firefly> getTopology() {
        return topology;
    }

    /**
     * Get the <code>IterationStrategy</code> of the Firefly algorithm.
     * @return Returns the iterationStrategy.
     */
    public IterationStrategy<FFA> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the <code>IterationStrategy</code> to be used.
     * @param iterationStrategy The iterationStrategy to set.
     */
    public void setIterationStrategy(IterationStrategy<FFA> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the collection of best solutions.
     * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (Firefly e : Topologies.getNeighbourhoodBestEntities(topology, new DescendingFitnessComparator<Firefly>())) {
            solutions.add(new OptimisationSolution(e.getPosition(), e.getFitness()));
        }
        return solutions;
    }
}