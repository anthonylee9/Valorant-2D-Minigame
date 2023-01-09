
import processing.core.PImage;

import java.util.List;

public abstract class NonAnimationEntity extends ActiveEntity
{
    public NonAnimationEntity(String id, Point position,
                              List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    @Override
    protected void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
