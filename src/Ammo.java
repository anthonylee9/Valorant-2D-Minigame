
import processing.core.PImage;

import java.util.List;

public class Ammo extends AnimationEntity {
    private static final int AMMO_ANIMATION_REPEAT_COUNT = 7;

    public Ammo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    @Override
    protected void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new AnimationAction(this, AMMO_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }


}
