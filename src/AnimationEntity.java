

import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActiveEntity {
    private int animationPeriod;
    private int repeatCount;
    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;

    }

    protected int getAnimationPeriod() { return animationPeriod; }
    protected void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

}
