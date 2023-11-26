package Program.Simulation.WSSSim;

import Program.Process;
import Program.Simulation.Simulation;
import Program.Simulation.SimulationStep;

public class WSSSimulation extends Simulation {
    private History[] history;
    private int processCount;
    private int checkTimer;
    private int totalWSS;
    private int steps = 0;

    private int setSize = 150; // keep high, otherwise it keeps pausing and unpausing processes, losing memory
    private int checkInterval = 50;
    private int initialCheckTime = 20;

    @Override
    public String getName() {
        return "WSS";
    }

    @Override
    public void Initialize(Process[] processes, int memSize) {
        super.Initialize(processes, memSize);
        DistribiuteMemoryProportionally();

        processCount = processes.length;
        history = new History[processCount];
        for (int i = 0; i < processCount; i++) {
            history[i] = new History(setSize);
        }
        checkTimer = -initialCheckTime + checkInterval;

    }

    @Override
    protected void onSimulationStep(SimulationStep result) {
        for (int i = 0; i < processCount; i++) {
            int page = result.pageReferenced[i];
            if (page != -1) {
                history[i].Record(page);
            }
        }

        checkTimer++;
        steps++;
        if (checkTimer >= checkInterval) {
            checkTimer = 0;

            CalculateWSS();
            while (WSSExceedsMemory() && GetRunningProcesses().length > 1) {
                int processToPause = GetRunningProcesses()[0];
                PauseProcess(processToPause);
                pauses++;
                CalculateWSS();
            }

            RedistribiuteMemory();

            if (memory.hasFreeMemory()) {
                int processToUnpause = FindProcessToUnpause();
                if (processToUnpause != -1)
                    UnpauseProcess(processToUnpause);
            }
        }

        // debugLog();
    }

    private void RedistribiuteMemory() {
        int[] memoryAssigned = new int[processCount];
        for (int process : GetRunningProcesses()) {
            memoryAssigned[process] = history[process].GetWSS();
        }
        memory.distribiute(memoryAssigned);
    }

    private void CalculateWSS() {
        totalWSS = 0;
        for (int i = 0; i < processCount; i++) {
            if (ProcessRunning(i))
                totalWSS += history[i].GetWSS();
        }
    }

    private boolean WSSExceedsMemory() {
        return memory.getMaxMemory() < totalWSS;
    }

    public int pauses = 0;

    private int FindProcessToUnpause() {
        int[] pausedProcesses = GetPausedProcesses();

        for (int i : pausedProcesses) {
            if (history[i].GetWSS() < memory.getFreeMemory()) {
                return i;
            }
        }

        return -1;
    }

    void debugLog() {
        String str = "[";

        for (int i = 0; i < processCount; i++) {
            String setSize = Integer.toString(history[i].GetWSS());
            while (setSize.length() < 2) {
                setSize = " " + setSize;
            }
            setSize = setSize.substring(0, 2);

            String assignedMemory = Integer.toString(memory.getSize(i));
            while (assignedMemory.length() < 3) {
                assignedMemory = " " + assignedMemory;
            }
            String paused = ProcessPaused(i) ? "*" : " ";
            String finishStr = ProcessFinished(i) ? "-  " : "   ";

            String processStr = assignedMemory + " | " + setSize + paused + finishStr;
            str += processStr;
        }

        str += "]  Step: " + steps;
        System.out.println(str);
    }
}
