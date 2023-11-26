package Program.Simulation.PFFSimulation;

import Program.Simulation.Simulation;
import Program.Simulation.SimulationStep;
import Program.Process;

public class PFFSimulation extends Simulation {
    private double high = 0.2;
    private double low = 0.05;
    private double unpauseTreshold = 0.3;
    private int historySize = 50;
    private int checkInterval = 20;
    public boolean log = false;

    private int processCount;
    private int t = 0;
    int s = 0;
    ProcessHistory[] histories;

    @Override
    public String getName() {
        return "PFF";
    }

    @Override
    public void Initialize(Process[] processes, int memSize) {
        super.Initialize(processes, memSize);
        processCount = processes.length;
        t = 0;
        histories = new ProcessHistory[processCount];
        for (int i = 0; i < processCount; i++)
            histories[i] = new ProcessHistory(historySize);

        DistribiuteMemoryProportionally();
        steps = 0;

    }

    private int steps = 0;

    @Override
    protected void onSimulationStep(SimulationStep result) {
        for (int i = 0; i < processCount; i++) {
            if (ProcessPaused(i))
                continue;

            if (result.pageFault[i] && memory.isFull(i))
                histories[i].Record(true);
            else
                histories[i].Record(false);
        }

        t++;
        if (t == checkInterval) {
            t = 0;
            TakeMemoryFromRich();
            GiveMemoryToStruggling();
            UnpauseProcesses();
        }
        steps++;
        if (log)
            debugLog();
    }

    private void TakeMemoryFromRich() {
        for (int i = 0; i < processCount; i++) {
            if (histories[i].faultPercentage <= low && memory.getSize(i) > 1) {
                memory.decrement(i, 1);
            }
        }
    }

    private void GiveMemoryToStruggling() {
        for (int i = 0; i < processCount; i++) {

            if (ProcessPaused(i) || ProcessFinished(i))
                continue;

            if (histories[i].faultPercentage >= high) {
                if (memory.hasFreeMemory()) {
                    memory.increment(i, 1);
                } else {
                    PauseProcess(i);
                }
            }
        }
    }

    private void UnpauseProcesses() {
        for (int i = 0; i < processCount; i++) {
            if (ProcessPaused(i) == false)
                continue;

            if (memory.getFreeMemory() > memory.getMaxMemory() * unpauseTreshold) {
                UnpauseProcess(i);
            }
        }
    }

    void debugLog() {
        String str = "[";

        for (int i = 0; i < processCount; i++) {
            String faults = Double.toString(histories[i].faultPercentage);
            while (faults.length() < 4) {
                faults += "0";
            }
            faults = faults.substring(0, 4);

            String assignedMemory = Integer.toString(memory.getSize(i));
            while (assignedMemory.length() < 3) {
                assignedMemory = " " + assignedMemory;
            }
            String paused = ProcessPaused(i) ? "*" : " ";
            String finishStr = ProcessFinished(i) ? "-" : " ";

            String processStr = assignedMemory + " | " + faults + paused + finishStr;
            str += processStr;
        }

        str += "]  Step: " + steps;
        System.out.println(str);
    }
}
