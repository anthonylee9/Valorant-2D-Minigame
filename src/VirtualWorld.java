import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;
import java.util.*;
/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet
{

   public static Viper viper;
   private boolean viper_spawned = false;
   private static final String OMEN_KEY = "omen";
   private static final int OMEN_NUM_PROPERTIES = 6;
   private static final int OMEN_ID = 1;
   private static final int OMEN_COL = 2;
   private static final int OMEN_ROW = 3;
   private static final int OMEN_ACTION_PERIOD = 4;
   private static final int OMEN_ANIMATION_PERIOD = 5;
   private static final String VIPER_KEY = "viper";
   private static final int VIPER_NUM_PROPERTIES = 6;
   private static final int VIPER_ID = 1;
   private static final int VIPER_COL = 2;
   private static final int VIPER_ROW = 3;
   private static final int VIPER_ACTION_PERIOD = 4;
   private static final int VIPER_ANIMATION_PERIOD = 5;
   private static final int TIMER_ACTION_PERIOD = 100;
   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   public static WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;

   }

   public void draw() {
      long time = System.currentTimeMillis();
      if (time >= next_time) {
         scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }
      if (viper_spawned == false) {
         Point p2 = new Point(7, 7);
         if (!world.isOccupied(p2)) {
            viper = new Viper("viper", p2, imageStore.getImageList(
                    VirtualWorld.VIPER_KEY),
                    500, 1000);
            world.addEntity(viper);
            viper_spawned = true;
         }
      }
      view.drawViewport();
      if (viper.dead == true) {
//         Point[] point = {new Point(1,1),
//                 new Point(1,2),
//                 new Point(2,2),
//                 new Point(2,3),
//                 new Point(2,3),
//         };
//
//         Background background = new Background("background", imageStore.getImageList("background"));
//         for (Point point2 : point) {
//            if (world.withinBounds(point2)) {
//               world.setBackgroundCell(point2, background);
//            }
            background(0);
            textSize(50);
            fill(150, 102, 0);
            text("YOU DIED \nGAME OVER!\nRESTART GAME", 170, 70);

      }
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;
         switch (keyCode)
         {
            case UP:
               dy = -1;
               viper.moveViperUp();
               break;
            case DOWN:
               dy = 1;
               viper.moveViperDown();
               break;
            case LEFT:
               dx = -1;
               viper.moveViperLeft();
               break;
            case RIGHT:
               dx = 1;
               viper.moveViperRight();
               break;
         }

         view.shiftView( dx, dy);
         view.drawViewport();
      }
   }

   private Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore,
                           PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void loadWorld(WorldModel world, String filename,
                          ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions(WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity instanceof ActiveEntity)
            ((ActiveEntity)entity).scheduleActions(world, scheduler, imageStore);
      }
   }

   private Point convertToPoint(int x, int y)
   {
      return new Point((x/TILE_WIDTH) + view.getViewport().getCol(), (y/TILE_HEIGHT) + view.getViewport().getRow());
   }
   public void mousePressed() {
      Point mouse = convertToPoint(mouseX, mouseY);
      Point[] test_tiles = {new Point(mouse.x, mouse.y),
              new Point(mouse.x + 1, mouse.y),
              new Point(mouse.x + 1, mouse.y + 1),
              new Point(mouse.x, mouse.y + 1),
      };

      Background test_background = new Background("test_background", imageStore.getImageList("test_background"));
      for (Point point : test_tiles) {
         if (world.withinBounds(point)) {
            world.setBackgroundCell(point, test_background);


         }
      }

      Point p = new Point(5, 5);
      if (!world.isOccupied(p)) {
         Omen omen = new Omen("omen", p,imageStore.getImageList(
                 VirtualWorld.OMEN_KEY),
                 500, 1000);
         world.addEntity(omen);
         omen.scheduleActions(world, scheduler, imageStore);
      }


      }



   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }


   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
