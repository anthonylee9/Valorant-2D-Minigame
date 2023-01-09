

import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationEntity
{
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
        boolean bool = true;
        VirtualWorld.viper.setConsumed(bool);
    }

    @Override
    protected void scheduleActions(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());

        scheduler.scheduleEvent(this,
                new AnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
