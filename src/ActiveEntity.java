
import processing.core.PImage;
import java.util.List;

public abstract class ActiveEntity extends Entity {
    private int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }
    protected abstract void executeActivity(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
    protected int getActionPeriod() { return actionPeriod; }

    protected abstract void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

}
