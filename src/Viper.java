import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Viper extends MovingEntity {
    protected boolean dead;
    protected Viper(String id, Point position, List<PImage> images,
                    int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    protected void setConsumed (boolean bool){
        dead = bool;
    }


    @Override
    protected boolean executeMovingActivity(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Entity target) {
        return true;
    }

    protected boolean canNotMoveTo(WorldModel world, Point newPos)
    {
        Optional<Entity> occupant = world.getOccupant(newPos);
        return (occupant.isPresent());
    }

    @Override
    protected boolean isTarget(Entity entity) {
        return true;
    }


    protected void suicide(){
        dead = true;
    }
    protected void moveViperUp() {
        int prevX = this.getPosition().getX();
        int prevY = this.getPosition().getY();

        Point newPos = new Point(prevX, prevY-1);
        if (canNotMoveTo(VirtualWorld.world, newPos))
        {
            suicide();
        }
        VirtualWorld.world.moveEntity(this, newPos);

    }

    protected void moveViperDown() {
        int prevX = this.getPosition().getX();
        int prevY = this.getPosition().getY();

        Point newPos = new Point(prevX, prevY+1);
        if (canNotMoveTo(VirtualWorld.world, newPos))
        {
            suicide();
        }
        VirtualWorld.world.moveEntity(this, newPos);

    }

    protected void moveViperRight() {
        int prevX = this.getPosition().getX();
        int prevY = this.getPosition().getY();

        Point newPos = new Point(prevX+1, prevY);
        if (canNotMoveTo(VirtualWorld.world, newPos))
        {
            suicide();
        }
        VirtualWorld.world.moveEntity(this, newPos);

    }

    protected void moveViperLeft() {
        int prevX = this.getPosition().getX();
        int prevY = this.getPosition().getY();

        Point newPos = new Point(prevX-1, prevY);
        if (canNotMoveTo(VirtualWorld.world, newPos))
        {
            suicide();
        }
        VirtualWorld.world.moveEntity(this, newPos);

    }
}