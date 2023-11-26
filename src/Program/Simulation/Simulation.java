package Program.Simulation;

import java.util.ArrayList;

import Program.Process;
import Program.Memory.Memory;
import Program.Memory.MemoryDistributor;

// Responsible for pausing and unpausing processes, running simulation,
// counting page faults and some stats
// Also has some helper functions for its subclasses

// Subclasses are responsible for memory distribiution,
// deciding when process should be paused and so on

// When simulation is run, it counts page faults, skips paused processes and so on
// After each simulation step it calls subclass by 'onSimulationStep' with info about the step
// From onSimulationStep subclass can distribiute memory however it likes
public abstract class Simulation {
    private int pageFaults = 0;
    private int processCount;
    private int memorySize;

    private Process[] processes;
    private boolean[] paused;
    private int steps = 0;
    protected Memory memory;

    public abstract String getName();

    public void Initialize(Process[] processes, int memSize) {
        pageFaults = 0;
        processCount = processes.length;
        memorySize = memSize;

        this.processes = processes;
        paused = new boolean[processCount];
        memory = new Memory(processCount, memSize);
        steps = 0;
        for (int i = 0; i < processCount; i++) {
            paused[i] = false;
        }
    }

    public void run() {
        while (allProcessesFinished() == false) {
            SimulationStep stepResult = new SimulationStep(processCount);

            for (int i = 0; i < processCount; i++) {
                Process process = processes[i];
                stepResult.pageFault[i] = false;
                stepResult.pageReferenced[i] = -1;

                if (!process.hasNextPage())
                    memory.setToZero(i);
                if (!process.hasNextPage() || paused[i]) {
                    continue;
                }

                int page = process.nextPage();
                stepResult.pageReferenced[i] = page;
                if (memory.retrieve(i, page) == false) {
                    stepResult.pageFault[i] = true;
                    pageFaults++;
                }
            }
            steps++;
            onSimulationStep(stepResult);
        }
    }

    protected void onSimulationStep(SimulationStep result) {

    }

    protected boolean allProcessesFinished() {
        for (Process process : processes) {
            if (process.hasNextPage())
                return false;
        }
        return true;
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public int getSteps() {
        return steps;
    }

    protected void DistribiuteMemoryProportionally() {
        int[] memNeeded = new int[processCount];
        for (int i = 0; i < processCount; i++)
            memNeeded[i] = processes[i].memoryNeeded;
        int[] memAssigned = MemoryDistributor.DistribiuteProportionally(memNeeded, memorySize);

        memory.distribiute(memAssigned);
    }

    protected void DistribiuteMemoryEvenly() {
        int[] memAssigned = MemoryDistributor.DistribiuteEvenly(processCount, memorySize);

        memory.distribiute(memAssigned);
    }

    protected int GetMemoryNeeded(int index) {
        return processes[index].memoryNeeded;
    }

    protected void PauseProcess(int index) {
        paused[index] = true;
        memory.setToZero(index);
    }

    protected void UnpauseProcess(int index) {
        if (memory.hasFreeMemory() == false)
            throw new IllegalStateException();
        paused[index] = false;
        int freeMemory = memory.getFreeMemory();
        int neededMemory = processes[index].memoryNeeded;

        memory.increment(index, Math.min(freeMemory, neededMemory));
    }

    protected boolean ProcessPaused(int index) {
        return paused[index];
    }

    protected boolean ProcessFinished(int index) {
        return !processes[index].hasNextPage();
    }

    protected int[] GetPausedProcesses() {
        ArrayList<Integer> processes = new ArrayList<>();
        for (int i = 0; i < processCount; i++) {
            if (paused[i] && this.processes[i].hasNextPage())
                processes.add(i);
        }
        return processes.stream().mapToInt(e -> e).toArray();
    }

    protected int[] GetRunningProcesses() {
        ArrayList<Integer> processes = new ArrayList<>();
        for (int i = 0; i < processCount; i++) {
            if (!paused[i] && this.processes[i].hasNextPage())
                processes.add(i);
        }
        return processes.stream().mapToInt(e -> e).toArray();
    }

    protected boolean ProcessRunning(int i) {
        return !paused[i] && processes[i].hasNextPage();
    }
}