package Program.Memory;

import java.util.List;

// Some static algorithms to distribiute memory, don't mind this
// Assures all memory is distribiuted and no process is left without memory
public class MemoryDistributor {
    public static int[] DistribiuteProportionally(List<Integer> memoryNeeded, int memorySize) {
        int[] array = memoryNeeded.stream().mapToInt(i -> i).toArray();
        return DistribiuteProportionally(array, memorySize);
    }

    public static int[] DistribiuteProportionally(int[] memoryNeeded, int memorySize) {
        int nonZeroCount = 0;
        for (int i : memoryNeeded) {
            if (i != 0)
                nonZeroCount++;
        }

        int[] positiveMemory = new int[nonZeroCount];
        for (int i = 0, j = 0; i < memoryNeeded.length; i++) {
            if (memoryNeeded[i] != 0) {
                positiveMemory[j] = memoryNeeded[i];
                j++;
            }
        }

        int[] nonZeroSplit = Proportional(positiveMemory, memorySize);
        int[] result = new int[memoryNeeded.length];

        for (int i = 0, j = 0; i < memoryNeeded.length; i++) {
            if (memoryNeeded[i] != 0) {
                result[i] = nonZeroSplit[j];
                j++;
            } else {
                result[i] = 0;
            }

        }

        return result;
    }

    private static int[] Proportional(int[] memoryNeeded, int memorySize) {
        if (memoryNeeded.length > memorySize)
            throw new IllegalArgumentException("Memory waay to smol");

        int totalMemoryNeeded = 0;
        int[] assignedMemory = new int[memoryNeeded.length];

        for (int m : memoryNeeded) {
            totalMemoryNeeded += m;
        }

        if (totalMemoryNeeded <= memorySize) { // Enough memory to give to all processes
            // Assign to each process exact memory it needs
            for (int i = 0; i < memoryNeeded.length; i++) {
                assignedMemory[i] = memoryNeeded[i];
            }
            CheckMemoryIsNotEmpty(assignedMemory);
            return assignedMemory;
        } else { // Not enough memory
            int memoryLeft = memorySize;

            for (int i = 0; i < memoryNeeded.length; i++) { // Split memory proportionally, floor in case of a fraction
                assignedMemory[i] = (int) Math.floor(1.0 * memoryNeeded[i] / totalMemoryNeeded * memorySize);
                memoryLeft -= assignedMemory[i];
            }

            int i = 0; // If memory is 0 and there is unassigned memory, increment it
            while (memoryLeft > 0 && i < memoryNeeded.length) {
                if (assignedMemory[i] == 0) {
                    assignedMemory[i]++;
                    memoryLeft--;
                }
                i++;
            }

            i = 0; // There is still empty memory, take from biggest memory holder
            while (anyMemoryEmpty(assignedMemory)) {
                int biggest = findBiggets(assignedMemory);
                if (assignedMemory[biggest] <= 1)
                    throw new IllegalStateException("What");
                int empty = findFirstEmpty(assignedMemory);

                assignedMemory[biggest]--;
                assignedMemory[empty]++;
            }

            // Spend all remaining memory from top to bottom
            i = 0;
            while (memoryLeft > 0) {
                assignedMemory[i]++;
                i++;
                memoryLeft--;
            }

            if (memoryLeft != 0) { // Sanity check
                throw new RuntimeException("what");
            }
            CheckMemoryIsNotEmpty(assignedMemory);

            return assignedMemory;
        }
    }

    private static boolean anyMemoryEmpty(int[] memory) {
        for (int i : memory) {
            if (i <= 0)
                return true;
        }
        return false;
    }

    private static int findBiggets(int[] memory) {
        int largest = memory[0];
        int index = 0;

        for (int i = 1; i < memory.length; i++) {
            if (memory[i] > largest) {
                largest = memory[i];
                index = i;
            }
        }
        return index;
    }

    private static int findFirstEmpty(int[] memory) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == 0)
                return i;
        }
        throw new IllegalArgumentException("What");
    }

    public static int[] DistribiuteEvenly(int count, int memorySize) {
        if (count > memorySize)
            throw new IllegalArgumentException("Memory waay to smol");

        int[] assignedMemory = new int[count];
        int memoryLeft = memorySize;
        int memoryPerProcess = Math.floorDiv(memorySize, count);

        for (int i = 0; i < count; i++) {
            assignedMemory[i] = memoryPerProcess;
            memoryLeft -= memoryPerProcess;
        }

        for (int i = 0; i < count && memoryLeft > 0; i++) {
            if (assignedMemory[i] == 0) {
                assignedMemory[i]++;
                memoryLeft--;
            }
        }

        for (int i = 0; i < count && memoryLeft > 0; i++) {
            assignedMemory[i]++;
            memoryLeft--;
        }

        if (memoryLeft != 0) { // Sanity check
            throw new RuntimeException("what");
        }
        CheckMemoryIsNotEmpty(assignedMemory);

        return assignedMemory;
    }

    private static void CheckMemoryIsNotEmpty(int[] memory) {
        for (int i : memory) {
            if (i <= 0)
                throw new RuntimeException("Cock");
        }
    }
}
