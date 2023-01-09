
import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Brim extends NonAnimationEntity {
    private final String KAYO_KEY = "kayo";
    private final String KAYO_ID_SUFFIX = " -- kayo";
    private final int KAYO_PERIOD_SCALE = 4;
    private final int KAYO_ANIMATION_MIN = 50;
    private final int KAYO_ANIMATION_MAX = 150;
    private final Random rand = new Random();

    public Brim(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        Point pos = this.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        MovingEntity kayo = new Kayo(this.getId() + KAYO_ID_SUFFIX,
                pos, imageStore.getImageList(KAYO_KEY),
                this.getActionPeriod() / KAYO_PERIOD_SCALE,
                KAYO_ANIMATION_MIN +
                        rand.nextInt(KAYO_ANIMATION_MAX - KAYO_ANIMATION_MIN));

        world.addEntity(kayo);
        kayo.scheduleActions(world, scheduler,imageStore);
    }

}
