

public class AnimationAction extends Action {
    private AnimationEntity entity;
    private int repeatCount;

    public AnimationAction(AnimationEntity entity, int repeatCount)
    {
        this.entity = entity;

        this.repeatCount = repeatCount;
    }

    @Override
    protected void executeAction(EventScheduler scheduler)
    {
        this.executeAnimationAction(scheduler);
    }

    protected void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    new AnimationAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
