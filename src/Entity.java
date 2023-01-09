

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity
{
   private String id;
   private Point position;
   private int imageIndex;
   private List<PImage> images;


   public Entity(String id, Point position, List<PImage> images)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

   protected String getId() {
      return id;}
   protected Point getPosition()  { return position;}
   protected void setPosition(Point pos)  { this.position = pos;}
   protected int getImageIndex() {
      return imageIndex;
   }
   protected List<PImage> getImages() {
      return images;
   }

   protected void setImageIndex(int imageIndex) {
      this.imageIndex = imageIndex;}

   protected PImage getCurrentImage() { return (images.get(imageIndex));}
}
