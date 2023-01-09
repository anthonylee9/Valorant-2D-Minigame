

import processing.core.PImage;

import java.util.*;

public abstract class MovingEntity extends AnimationEntity
{
    private Optional<Entity> current_target = Optional.empty();
    private Queue<Point> target_path = new ArrayDeque<>();
    private PathingStrategy strategy = new AStarPathingStrategy();
    private final int total_time = 2;
    private int current_waiting_time = 0;

    public MovingEntity(String id, Point position, List<PImage> images,
                        int actionPeriod, int animationPeriod)
    {

        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this, 0),
                this.getAnimationPeriod());

    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
       if( !current_target.isPresent()|| current_target.get().getPosition().equals(new Point(-1,-1)))
        {
            current_target = this.findNearest(world);
            if(current_target.isPresent())
            {
                target_path.clear();
                current_waiting_time = 0;
                if (!this.computePath(world))
                    current_target = Optional.empty();
            }

        }

        if ( !current_target.isPresent() ||
             !this.moveTo(world, current_target.get(), scheduler) ||
             !executeMovingActivity(world, scheduler, imageStore, current_target.get()))
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }

    }

    private boolean moveTo(WorldModel world, Entity target,
                          EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world);

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                    scheduler.unscheduleAllEvents(occupant.get());

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private void giveUpOnTarget()
    {
        target_path.clear();
        current_target = Optional.empty();
    }

    private Point nextPosition(WorldModel world)
    {
        Point newPos = target_path.peek();
        if(canNotMoveTo(world, newPos))
        {
            newPos = this.getPosition();

            if (current_waiting_time > total_time)
                giveUpOnTarget();
            else current_waiting_time++;

        }
        else target_path.poll();

        return newPos;
    }

    private Point nextPosition2(WorldModel world, Point destPos) {
        List<Point> points;
        Point point = this.getPosition();
        Point goal = current_target.get().getPosition();
        int numOfRowsWorld = world.getNumRows();
        int numOfColsWorld = world.getNumCols();

        points = strategy.computePath(point, goal,
                p -> PathingStrategy.withinBounds(p, numOfRowsWorld, numOfColsWorld) &&  !canNotMoveTo(world, p),
                (p1, p2) -> PathingStrategy.neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if(points.size() == 0) { // no path found
            return this.getPosition();
        }
        return points.get(0);
    }
    private boolean computePath(WorldModel world)
    {
        List<Point> points;
        Point point = this.getPosition();
        Point goal = current_target.get().getPosition();

        int numOfRowsWorld = world.getNumRows();
        int numOfColsWorld = world.getNumCols();

        points = strategy.computePath(point, goal,
                p -> PathingStrategy.withinBounds(p, numOfRowsWorld, numOfColsWorld) &&  !canNotMoveTo(world, p),
                (p1, p2) -> PathingStrategy.neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        int numsOfPoints = points.size();
        for(int i = numsOfPoints-1; i>=0; i--)
            this.target_path.add(points.get(i));

        return this.target_path.size()!=0 ? true : false;
    }


    protected Optional<Entity> findNearest(WorldModel world)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (isTarget(entity))
            {
                ofType.add(entity);
            }
        }

        return this.nearestEntity(ofType);
    }

    private Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(this.getPosition());

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(this.getPosition());

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    protected abstract boolean executeMovingActivity(WorldModel world,
                                                       EventScheduler scheduler, ImageStore imageStore, Entity target);

    protected abstract boolean canNotMoveTo(WorldModel world, Point newPos);

    protected abstract boolean isTarget(Entity entity);

}
