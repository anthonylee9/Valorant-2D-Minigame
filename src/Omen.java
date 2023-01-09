import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Omen extends MovingEntity
{
    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Omen(String id, Point position, List<PImage> images,
                int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    protected boolean executeMovingActivity(WorldModel world,
                                            EventScheduler scheduler, ImageStore imageStore,
                                            Entity target)
    {
        long nextPeriod = this.getActionPeriod();
        Point tgtPos = target.getPosition();

        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);

        ActiveEntity quake = new Quake(QUAKE_ID, tgtPos,
                imageStore.getImageList(QUAKE_KEY),
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);

        world.addEntity(quake);
        nextPeriod += this.getActionPeriod();
        quake.scheduleActions(world, scheduler,imageStore);

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                nextPeriod);
        return true;
    }

    @Override
    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        Optional<Entity> occupant = world.getOccupant(newPos);
        return (occupant.isPresent() && !(occupant.get() instanceof Brim) && !(occupant.get() instanceof Skye)
                && !(occupant.get() instanceof Entity) && !(occupant.get() instanceof Kayo));
    }


    @Override
    protected boolean isTarget(Entity entity)
    {
        return (entity instanceof Spike);
    }

}
