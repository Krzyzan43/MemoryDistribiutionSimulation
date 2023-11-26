package Program.Simulation.ConstSizeSim;

import Program.Process;
import Program.Simulation.Simulation;

public class ConstSizeSimulation extends Simulation {

    @Override
    public String getName() {
        return "ConstSize";
    }

    @Override
    public void Initialize(Process[] processes, int memSize) {
        super.Initialize(processes, memSize);
        DistribiuteMemoryEvenly();
        return;
    }
}
