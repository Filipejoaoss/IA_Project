package random;

import algorithms.Algorithm;
import algorithms.AlgorithmEvent;
import algorithms.Individual;
import algorithms.Problem;

import java.util.Random;

public class RandomAlgorithm<I extends Individual, P extends Problem<I>> extends Algorithm<I, P> {

    public RandomAlgorithm(int maxIterations, Random rand) {
        super(maxIterations, rand);
    }

    @Override
    public I run(P problem) {
        t = 0;

        /*Cria um novo individuo, como é o unico é o melhor*/
        globalBest = problem.getNewIndividual();
        globalBest.computeFitness();
        fireIterationEnded(new AlgorithmEvent(this));

        while (t < maxIterations && !stopped) {
            /*Cria novo individuo*/
            I newIndividual = problem.getNewIndividual();
            /*compara os dois individuos e guarda o melhor para a proxima geração*/
            globalBest = computeBest(newIndividual);

            t++;
            fireIterationEnded(new AlgorithmEvent(this));
        }
        fireRunEnded(new AlgorithmEvent(this));
        return globalBest;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public I computeBest(I individual){
            individual.computeFitness();
        return globalBest.compareTo(individual) > 0 ? globalBest : individual;
    }

}
