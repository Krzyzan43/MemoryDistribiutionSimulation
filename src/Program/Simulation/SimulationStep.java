package Program.Simulation;

public class SimulationStep {
    public int[] pageReferenced;
    public boolean[] pageFault;

    public SimulationStep(int size) {
        pageReferenced = new int[size];
        pageFault = new boolean[size];
    }
}