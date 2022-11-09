package stockingproblem;

import algorithms.IntVectorIndividual;
import ga.GeneticAlgorithm;

import java.util.ArrayList;

import java.util.Collections;

public class StockingProblemIndividual extends IntVectorIndividual<StockingProblem, StockingProblemIndividual> {
    private int tamanhoMaximoSuperficie;
    private int numCortes;
    private ArrayList<Integer>[] material;
    private int materialWidth;

    public StockingProblemIndividual(StockingProblem problem, int size) {
        super(problem, size);

        ArrayList<Integer> aux = new ArrayList<>();
        for (int i=0; i<size; i++) {
            aux.add(i);
        }

        Collections.shuffle(aux, GeneticAlgorithm.random);

        for (int i = 0; i < genome.length; i++) {
            genome[i] = aux.get(i);
        }
    }

    public StockingProblemIndividual(StockingProblemIndividual original) {
        super(original);
        tamanhoMaximoSuperficie = original.tamanhoMaximoSuperficie;
        numCortes = original.numCortes;
        material = original.material;
        materialWidth = original.materialWidth;
    }

    private void materialZeroColumn(){
        for(int i = 0; i < material.length; i++) {
            material[i].add(materialWidth, 0);
        }
    }

    private void createMaterial(){
        for (int i : genome) {
            Item item = problem.getItems().get(i);
            boolean drawn = false;
            for (int w = 0; w <= materialWidth; w++) {
                for (int h = 0; h <= material.length; h++) {
                    drawn = drawItem(item, h, w);
                    if (drawn) {
                        h = material.length;
                        w = materialWidth;
                    }
                }
            }
        }

        numCortes = calculateCuts();
    }

    private boolean drawItem(Item item, int lineIndex, int columnIndex){
        while(item.getColumns()> materialWidth){
            materialWidth++;
            materialZeroColumn();
        }
        if(!checkValidPlacement(item,lineIndex,columnIndex)){
            return false;
        }
        //Se for possivel vai desenhar a peça na posição recebida
        int[][] itemMatrix = item.getMatrix();
        for (int i = 0; i < itemMatrix.length; i++) {
            for (int j = 0; j < itemMatrix[i].length; j++) {
                //Verificação para onde é preciso desenhar
                if (itemMatrix[i][j] != 0) {
                    int rep = item.getRepresentation();
                    material[lineIndex + i].set(columnIndex + j, rep);
                    tamanhoMaximoSuperficie = Math.max(tamanhoMaximoSuperficie, columnIndex + j + 1);
                }
            }
        }

        return true;
    }

    private int calculateCuts() {
        int totalCortes = 0;

        for(int i = 0; i < material.length; i++){
            for(int j = 0; j < materialWidth; j++){
                int atual = material[i].get(j);
                if(i+1 < material.length){
                    totalCortes += (atual == material[i+1].get(j) ? 0 : 1);
                }
                if(j+1 < materialWidth){
                    totalCortes += (atual == material[i].get(j+1) ? 0 : 1);
                }
            }
        }
        return totalCortes;
    }

    @Override
    public double computeFitness() {

        material = new ArrayList[problem.getMaterialHeight()];
        for (int i = 0; i < material.length; i++) {
            material[i] = new ArrayList<>();
        }
        materialWidth = 0;
        materialZeroColumn();

        createMaterial();
        fitness = numCortes *0.2 + tamanhoMaximoSuperficie *0.8;

        return fitness;

    }

    private boolean checkValidPlacement(Item item, int lineIndex, int columnIndex) {
        int[][] itemMatrix = item.getMatrix();
        for (int i = 0; i < itemMatrix.length; i++) {
            for (int j = 0; j < itemMatrix[i].length; j++) {
                if((columnIndex + j)  >= materialWidth){
                    materialWidth++;
                    materialZeroColumn();
                }
                if (itemMatrix[i][j] != 0) {
                    if ((lineIndex + i) >= material.length
                            || material[lineIndex + i].get(columnIndex + j) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genome: ");
        for (int i = 0; i < genome.length; i++) {
            sb.append(genome[i] + " ");
        }
        sb.append("\nGenome Tamanho: ");
        sb.append(genome.length);
        sb.append("\nFitness: ");
        sb.append(this.fitness);
        sb.append("\nTamanho máximo superficie :");
        sb.append(this.tamanhoMaximoSuperficie);
        sb.append("\nNúmero de cortes: ");
        sb.append(this.numCortes);
        sb.append("\nMaterial: ");
        sb.append("\nDimensões: "+material.length +"x" + materialWidth);
        for (int i = 0; i < material.length; i++ ){
            sb.append("\n");
            for (int j = 0; j < materialWidth; j++){
                sb.append(material[i].get(j) == 0 ? "|" : Character.toString(material[i].get(j)));
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * @param i
     * @return 1 if this object is BETTER than i, -1 if it is WORST than I and
     * 0, otherwise.
     */
    @Override
    public int compareTo(StockingProblemIndividual i) {
        return (this.fitness == i.getFitness()) ? 0 : (this.fitness < i.getFitness()) ? 1 : -1;
    }

    @Override
    public StockingProblemIndividual clone() {
        return new StockingProblemIndividual(this);
    }
}