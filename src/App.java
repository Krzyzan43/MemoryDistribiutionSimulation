import Program.Generator.Generator;
import Program.Generator.Range;
import Program.Simulation.Simulation;
import Program.Simulation.ConstSizeSim.ConstSizeSimulation;
import Program.Simulation.PFFSimulation.PFFSimulation;
import Program.Simulation.ProportionalSim.ProportionalSimulation;
import Program.Simulation.WSSSim.WSSSimulation;
import Program.Process;

public class App {
    public static void main(String[] args) throws Exception {
        Generator generator = new Generator();

        generator.groupCount = new Range(60, 100);
        generator.groupSizeRange = new Range(5, 15);
        generator.maxPageRange = new Range(5, 25);
        generator.groupSpreadRange = new Range(1, 3);
        generator.processCount = 10;
        int memorySize = 150;
        int totalIterations = 1;

        ConstSizeSimulation constSizeSimulation = new ConstSizeSimulation();
        ProportionalSimulation proportionalSimulation = new ProportionalSimulation();
        PFFSimulation pffSimulation = new PFFSimulation();
        pffSimulation.log = false;
        WSSSimulation wssSimulation = new WSSSimulation();

        Simulation[] sims = { constSizeSimulation, proportionalSimulation, pffSimulation, wssSimulation };
        int[] faults = new int[sims.length];
        int[] steps = new int[sims.length];

        for (int i = 0; i < totalIterations; i++) {
            generator.generate();
            for (int simIndex = 0; simIndex < sims.length; simIndex++) {
                Simulation simulation = sims[simIndex];
                simulation.Initialize(generator.getProcesses().toArray(new Process[0]), memorySize);
                simulation.run();
                faults[simIndex] += simulation.getPageFaults();
                steps[simIndex] += simulation.getSteps();
            }
        }

        for (int i = 0; i < sims.length; i++) {
            System.out.println("Algorithm: " + sims[i].getName());
            System.out.println("Faults: " + (faults[i]));
            System.out.println("Steps: " + steps[i]);
            System.out.println();
        }
        // System.out.println(wssSimulation.pauses);
    }
}
