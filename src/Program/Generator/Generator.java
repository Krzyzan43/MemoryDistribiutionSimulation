package Program.Generator;

import java.util.ArrayList;
import java.util.Random;

import Program.Process;

public class Generator {
    Random random = new Random(0);
    ArrayList<Process> processes = new ArrayList<>();

    public Range groupCount;
    public Range groupSizeRange;
    public Range maxPageRange;
    public Range groupSpreadRange;
    public int processCount = 5;

    public Generator() {
        groupCount = new Range(100, 0);
        groupSizeRange = new Range(10, 0);
        maxPageRange = new Range(5, 10);
        groupSpreadRange = new Range(2, 0);
    }

    public void generate() {
        processes.clear();

        for (int processIndex = 0; processIndex < processCount; processIndex++) {
            ArrayList<Integer> pages = new ArrayList<>();
            int maxPage = maxPageRange.random(random);
            int groupSpread = groupSpreadRange.random(random);
            int groupSize = groupSizeRange.random(random);

            for (int i = 0; i < groupCount.random(random); i++) {
                int middlePage = random.nextInt(maxPage);

                for (int j = 0; j < groupSize; j++) {
                    int page = Math.min(maxPage, middlePage + (int) (random.nextExponential() * groupSpread));
                    pages.add(page);
                }
            }
            processes.add(new Process(pages));
        }
        return;
    }

    public ArrayList<Process> getProcesses() {
        ArrayList<Process> copy = new ArrayList<>();
        for (Process process : processes) {
            copy.add(process.copy());
        }
        return copy;
    }
}
