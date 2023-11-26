package Program.Simulation.ProportionalSim;

import Program.Process;
import Program.Simulation.Simulation;

public class ProportionalSimulation extends Simulation {

    @Override
    public String getName() {
        return "Proportional";
    }

    @Override
    public void Initialize(Process[] processes, int memSize) {
        super.Initialize(processes, memSize);
        DistribiuteMemoryProportionally();
        return;
    }

}
