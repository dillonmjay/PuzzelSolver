
import java.util.*;

public class PuzzleSolver {

    public List<String> findPath(Map<GridPosition, List<GridPosition>> net, GridPosition start, GridPosition goal) {
        Queue<GridPosition> frontier = new LinkedList<>();
        Map<GridPosition, GridPosition> ancestry = new HashMap<>();
        frontier.add(start);
        ancestry.put(start, null);

        while (!frontier.isEmpty()) {
            GridPosition current = frontier.poll();
            if (current.equals(goal)) {
                return reconstructPath(ancestry, start, goal);
            }

            for (GridPosition next : net.get(current)) {
                if (!ancestry.containsKey(next)) {
                    frontier.add(next);
                    ancestry.put(next, current);
                }
            }
        }

        return Collections.singletonList("Path not found.");
    }
}
