
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Player extends MovingEntity
{
    private String id = "player";
    private Point position;

    public Player(String id, Point position, List<PImage> images,
                int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void changePosition(int dx, int dy){
        int x = position.getX();
        int y = position.getY();
        this.position = new Point(x+dx, y+dy);
    }
    @Override
    protected boolean executeMovingActivity(WorldModel world,
                                            EventScheduler scheduler, ImageStore imageStore,
                                            Entity target)
    {
        return true;
    }

    @Override
    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        return true;
    }

    @Override
    protected boolean isTarget(Entity entity)
    {
        return true;
    }

}