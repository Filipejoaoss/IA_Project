package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

import java.util.*;

public class RecombinationCycle<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {


    public RecombinationCycle(double probability) {
        super(probability);
    }
    private int[] child1;
    private int[] child2;

    @Override
    public void recombine(I ind1, I ind2) {
        int size = ind1.getNumGenes();
        this.child1 = new int[size];
        this.child2 = new int[size];
        fulfill(this.child1, ind2);
        fulfill(this.child2, ind1);

        Set<Integer> visitedIndices = new HashSet<>(size);
        List<Integer> indices = new ArrayList<>(size);

        int idx = GeneticAlgorithm.random.nextInt(size);
        int cycle = 1;
        while (visitedIndices.size() < size) {
            indices.add(Integer.valueOf(idx));
            int item = ind2.getGene(idx);
            idx = ind1.getIndexof(item);
            while (idx != ((Integer)indices.get(0)).intValue()) {
                indices.add(Integer.valueOf(idx));
                item = ind2.getGene(idx);
                idx = ind1.getIndexof(item);
            }
            if (cycle++ % 2 != 0)
                for (Iterator<Integer> iterator = indices.iterator(); iterator.hasNext(); ) {
                    int i = ((Integer)iterator.next()).intValue();
                    int aux = this.child1[i];
                    this.child1[i] = this.child2[i];
                    this.child2[i] = aux;
                }
            visitedIndices.addAll(indices);
            idx = (((Integer)indices.get(0)).intValue() + 1) % size;
            while (visitedIndices.contains(Integer.valueOf(idx)) && visitedIndices.size() < size) {
                idx++;
                if (idx >= size)
                    idx = 0;
            }
            indices.clear();
        }
        changeInd(this.child1, ind1);
        changeInd(this.child2, ind2);
    }

    public void fulfill(int[] child, I ind) {
        for (int i = 0; i < ind.getNumGenes(); i++)
            child[i] = ind.getGene(i);
    }

    public void changeInd(int[] child, I ind) {
        for (int i = 0; i < child.length; i++)
            ind.setGene(i, child[i]);
    }

    @Override
    public String toString(){
        return "Recombination Cycle";
    }
}