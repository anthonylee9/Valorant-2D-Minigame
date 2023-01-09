
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    private Double hValue(Point current, Point end)
    {
        return Math.sqrt((current.x-end.x)*(current.x-end.x) + (current.y-end.y)*(current.y-end.y));
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Stack<Point> path = new Stack<>();

        List<Point> open_list = new ArrayList<>();
        List<Point> close_list = new ArrayList<>();

        Map<Point, Double> g = new HashMap<>();
        Map<Point, Double> f = new HashMap<>();
        Map<Point, Double> h = new HashMap<>();
        Map<Point, Point> trace = new HashMap<>();

        open_list.add(start);
        f.put(start, 0.0);
        g.put(start, 0.0);

        while(!open_list.isEmpty())
        {
            Double min = Double.MAX_VALUE;
            Point current = null;
            for (Point p: open_list)
                if(close_list.contains(p)==false && Double.compare(f.get(p),min) < 0)
                {
                    min = f.get(p);
                    current = p;
                }

            close_list.add(current);
            open_list.remove(current);

            if(withinReach.test(current, end))
            {
                trace.put(end, current);
                close_list.add(end);
                break;
            }

            List<Point> neighbors = potentialNeighbors.apply(current).filter(canPassThrough).toList();
            Double startTocurrent = g.get(current);

            for(Point p: neighbors)
                if(!close_list.contains(p))
                {
                    if(h.get(p)==null)
                        h.put(p, hValue(p, end));

                    if (f.get(p) == null || f.get(p) < startTocurrent + h.get(p) + 1.0)
                    {
                        g.put(p, startTocurrent + 1.0);
                        f.put(p, startTocurrent + h.get(p) + 1.0);
                        trace.put(p, current);
                        if(!open_list.contains(p))
                            open_list.add(p);
                    }
                }
        }

        if(close_list.contains(end))
        {
            Point current = end;
            while(!current.equals(start))
            {
                path.push(current);
                current = trace.get(current);
            }

        }

        return path;
    }
}
