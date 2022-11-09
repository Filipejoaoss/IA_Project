package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class MutationSwap<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationSwap(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind) {
        int indSize = ind.getNumGenes();
        int posSwap1 = GeneticAlgorithm.random.nextInt(indSize);
        int posSwap2;

        do {
            posSwap2 = GeneticAlgorithm.random.nextInt(indSize);
        } while (posSwap1 != posSwap2);

        int aux = ind.getGene(posSwap1);
        ind.setGene(posSwap1, ind.getGene(posSwap2));
        ind.setGene(posSwap2, aux);

    }

    @Override
    public String toString() {
        return "Swap mutation (" + this.probability + ")";
    }
}