
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        File folder = new File("test"); // Directory containing the puzzle files
        File[] listOfFiles = folder.listFiles((d, name) -> name.endsWith(".txt"));
        Map<String, Long> empiricalResults = new LinkedHashMap<>(); // To maintain insertion order

        if (listOfFiles == null) {
            System.out.println("No text files found.");
            return;
        }

        Arrays.sort(listOfFiles, Comparator.comparing(File::getName)); // Optional: Sort files by name

        for (File file : listOfFiles) {
            long startTime = System.currentTimeMillis(); // Start time for performance measurement
            System.out.println("Solving " + file.getName());
            try {
                MapDecoder decoder = new MapDecoder();
                Map<GridPosition, List<GridPosition>> net = decoder.decodeFile(file.getAbsolutePath());

                GridPosition start = decoder.getEntry();
                GridPosition end = decoder.getExit();

                if (start == null || end == null) {
                    System.out.println("Error: Start or end position not defined in " + file.getName());
                    empiricalResults.put(extractPuzzleSize(file.getName()), null);
                    continue;
                }

                PuzzleSolver solver = new PuzzleSolver();
                List<String> path = solver.findPath(net, start, end);
                long endTime = System.currentTimeMillis(); // End time for performance measurement

                path.forEach(System.out::println);
                empiricalResults.put(extractPuzzleSize(file.getName()), endTime - startTime); // Storing time taken
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + file.getName());
                empiricalResults.put(extractPuzzleSize(file.getName()), null);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + " in " + file.getName());
                e.printStackTrace();
                empiricalResults.put(extractPuzzleSize(file.getName()), null);
            }
            System.out.println("------------------------------------------------");
        }

        // Print empirical results after all puzzles have been processed
        System.out.println("Puzzle Size | Time (milliseconds)");
        System.out.println("---------------------------------");
        empiricalResults.forEach((size, time) -> {
            if (time != null) {
                System.out.println(size + " | " + time);
            } else {
                System.out.println(size + " | Error");
            }
        });
    }

    private static String extractPuzzleSize(String filename) {
        return filename.replaceAll("[^0-9]", "");  // Extracts digits from the filename, assumes format puzzle_N.txt
    }
}
