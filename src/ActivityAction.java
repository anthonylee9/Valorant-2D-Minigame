
public class ActivityAction extends Action {
    private ActiveEntity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(ActiveEntity entity, WorldModel world,
                          ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    protected void executeAction(EventScheduler scheduler)
    {
        this.executeActivityAction(scheduler);
    }

    protected void executeActivityAction(EventScheduler scheduler)
    {
        this.entity.executeActivity(this.world, scheduler, this.imageStore);
    }
}
