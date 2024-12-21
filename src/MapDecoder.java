
import java.util.*;

public class MapDecoder {

    private Map<GridPosition, List<GridPosition>> graph = new HashMap<>();
    private GridPosition entry = null;
    private GridPosition exit = null;

    public Map<GridPosition, List<GridPosition>> decodeFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        List<String> content = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String processedLine = scanner.nextLine().trim();
            if (!processedLine.isEmpty()) {
                content.add(processedLine);
            }
        }
        scanner.close();

        int maxWidth = content.stream().mapToInt(String::length).max().orElse(0);
        char[][] grid = new char[content.size()][maxWidth];

        for (int i = 0; i < content.size(); i++) {
            Arrays.fill(grid[i], '0'); // Fill row with obstacles
            String line = content.get(i);
            System.arraycopy(line.toCharArray(), 0, grid[i], 0, line.length());

            for (int j = 0; j < maxWidth; j++) {
                if (grid[i][j] != '0') {
                    GridPosition pos = new GridPosition(i, j);
                    graph.put(pos, new ArrayList<>());
                    if (grid[i][j] == 'S') {
                        entry = pos;
                    }
                    if (grid[i][j] == 'F') {
                        exit = pos;
                    }
                }
            }
        }

        for (GridPosition pos : graph.keySet()) {
            int i = pos.getX();
            int j = pos.getY();
            if (i > 0 && grid[i - 1][j] != '0') {
                graph.get(pos).add(new GridPosition(i - 1, j));
            }
            if (j > 0 && grid[i][j - 1] != '0') {
                graph.get(pos).add(new GridPosition(i, j - 1));
            }
            if (i < content.size() - 1 && grid[i + 1][j] != '0') {
                graph.get(pos).add(new GridPosition(i + 1, j));
            }
            if (j < maxWidth - 1 && grid[i][j + 1] != '0') {
                graph.get(pos).add(new GridPosition(i, j + 1));
            }
        }

        if (entry == null || exit == null) {
            throw new IllegalStateException("Entry or exit point not defined.");
        }
        return graph;
    }

    public GridPosition getEntry() {
        return entry;
    }

    public GridPosition getExit() {
        return exit;
    }
}
