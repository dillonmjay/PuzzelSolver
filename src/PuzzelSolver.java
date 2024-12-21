
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

    private List<String> reconstructPath(Map<GridPosition, GridPosition> ancestry, GridPosition start, GridPosition goal) {
        LinkedList<String> route = new LinkedList<>();
        GridPosition step = goal;

        while (step != null && !step.equals(start)) {
            GridPosition prior = ancestry.get(step);
            String movement = describeMovement(prior, step);
            route.addFirst(movement);
            step = prior;
        }
        route.addFirst("Commence at " + start);
        route.add("Terminate!");

        return route;
    }

    private String describeMovement(GridPosition from, GridPosition to) {
        if (from.getX() == to.getX()) {
            return "Move " + (from.getY() < to.getY() ? "right" : "left") + " to " + to;
        } else {
            return "Move " + (from.getX() < to.getX() ? "down" : "up") + " to " + to;
        }
    }
}
