

import processing.core.PImage;

import java.util.List;

public class SkyeNotFull extends Skye {
    public SkyeNotFull(String id, Point position, List<PImage> images,
                       int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executeMovingActivity(WorldModel world,
                                              EventScheduler scheduler, ImageStore imageStore, Entity target)
    {
        this.addResourceCount();
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);

        return (this.transform(world, scheduler, imageStore));
    }

    @Override
    protected Skye newForm()
    {
        return new SkyeFull(this.getId(), this.getPosition(),
                this.getImages(), this.getResourceLimit(), this.getResourceCount(),
                this.getActionPeriod(), this.getAnimationPeriod());
    }

    @Override
    protected boolean isTarget(Entity entity) { return (entity instanceof Brim); }

}
