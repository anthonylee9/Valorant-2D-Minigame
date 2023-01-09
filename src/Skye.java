
import processing.core.PImage;

import java.util.List;

public abstract class Skye extends MovingEntity
{
    private int resourceCount;
    private int resourceLimit;

    public Skye(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    protected int getResourceCount() { return resourceCount; }
    protected int getResourceLimit() { return resourceLimit; }
    protected void addResourceCount() {resourceCount += 1;}
    public boolean transform(WorldModel world, EventScheduler scheduler,
                             ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            Skye skye = newForm();

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(skye);
            skye.scheduleActions(world, scheduler,imageStore);

            return true;
        }

        return false;
    }

    @Override
    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        return (world.isOccupied(newPos));
    }

    protected abstract Skye newForm();
}
