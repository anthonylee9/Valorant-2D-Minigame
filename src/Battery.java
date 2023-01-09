
import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Battery extends NonAnimationEntity
{
    private final String BRIM_ID_PREFIX = "brim -- ";
    private final int BRIM_CORRUPT_MIN = 20000;
    private final int BRIM_CORRUPT_MAX = 30000;
    private final String BRIM_KEY = "brim";

    public Battery(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    @Override
    protected void executeActivity(WorldModel world, EventScheduler scheduler,
                                ImageStore imageStore)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent())
        {
            Random rand = new Random();
            ActiveEntity brim = new Brim(BRIM_ID_PREFIX + this.getId(),
                    openPt.get(), imageStore.getImageList(BRIM_KEY),
                    BRIM_CORRUPT_MIN +
                            rand.nextInt(BRIM_CORRUPT_MAX - BRIM_CORRUPT_MIN));
            world.addEntity(brim);
            brim.scheduleActions(world, scheduler,imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

}
