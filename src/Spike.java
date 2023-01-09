
import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Spike extends NonAnimationEntity {
    private final String OMEN_ID_PREFIX = "omen -- ";
    private final int OMEN_CORRUPT_MIN = 20000;
    private final int OMEN_CORRUPT_MAX = 30000;
    private final String OMEN_KEY = "omen";

    public Spike(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


}
