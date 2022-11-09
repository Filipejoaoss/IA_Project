package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

import java.util.Arrays;

public class RecombinationOneCut<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {

    private int[] child1, child2, segment1, segment2;

    private int cut1;
    private int cut2;

    public RecombinationOneCut(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2) {
        int size = ind1.getNumGenes();

        child1 = new int[size];
        child2 = new int[size];

        cut1 = GeneticAlgorithm.random.nextInt(2);
        switch (cut1){
            case 1:
                cut1 = 1;
            case 2:
                cut1 = ind1.getNumGenes();
        }

        do {
            cut2 = GeneticAlgorithm.random.nextInt(size);
        } while (cut1 == cut2);

        if (cut1 > cut2) {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        create_Segments(cut1, cut2, ind1, ind2);

        crossover(child1, segment2, ind1);
        crossover(child2, segment1, ind2);

        for (int i = 0; i < ind1.getNumGenes(); i++) {
            ind1.setGene(i, child1[i]);
            ind2.setGene(i, child2[i]);
        }
    }

    private void create_Segments(int cutPoint1, int cutPoint2, I ind1, I ind2) {
        int capacity_ofSegments = (cutPoint2 - cutPoint1);
        segment1 = new int[capacity_ofSegments];
        segment2 = new int[capacity_ofSegments];
        int segment1and2i = 0;
        for (int i = cutPoint1; i < cutPoint2; i++) {
            int x = ind1.getGene(i);
            int y = ind2.getGene(i);
            segment1[segment1and2i] = x;
            segment2[segment1and2i] = y;
            segment1and2i++;
        }
    }

    public void crossover(int child[], int[] segment, I ind){
        int auxSize = 0;
        int i;
        for(i = 0; auxSize < cut1 ; i++){
            int currentGene = ind.getGene(i);
            if (!checkDuplicates(currentGene, segment)) {
                child[auxSize] = ind.getGene(i);
                auxSize++;
            }
        }

        for (int k : segment) {
            child[auxSize] = k;
            auxSize++;
        }

        for( int j = i; j < ind.getNumGenes(); j++){
            int currentGene = ind.getGene(j);
            if (!checkDuplicates(currentGene, segment)) {
                child[auxSize] = ind.getGene(j);
                auxSize++;
            }
        }
    }

    public boolean checkDuplicates(int gene, int segment[]){
        for(int j = 0; j < segment.length; j++) {
            if (gene == segment[j]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "One Cut";
    }
}