
import java.util.Scanner;

/*
Functions - everything our virtual world is doing right now - is this a good design?
 */

final class Functions {

    protected static final String SKYE_KEY = "skye";
    private static final int SKYE_NUM_PROPERTIES = 7;
    private static final int SKYE_ID = 1;
    private static final int SKYE_COL = 2;
    private static final int SKYE_ROW = 3;
    private static final int SKYE_LIMIT = 4;
    private static final int SKYE_ACTION_PERIOD = 5;
    private static final int SKYE_ANIMATION_PERIOD = 6;



    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String BRIM_KEY = "brim";
    private static final int BRIM_NUM_PROPERTIES = 5;
    private static final int BRIM_ID = 1;
    private static final int BRIM_COL = 2;
    private static final int BRIM_ROW = 3;
    private static final int BRIM_ACTION_PERIOD = 4;

    private static final String AMMO_KEY = "ammo";
    private static final int AMMO_NUM_PROPERTIES = 4;
    private static final int AMMO_ID = 1;
    private static final int AMMO_COL = 2;
    private static final int AMMO_ROW = 3;

    private static final String BATTERY_KEY = "battery";
    private static final int BATTERY_NUM_PROPERTIES = 5;
    private static final int BATTERY_ID = 1;
    private static final int BATTERY_COL = 2;
    private static final int BATTERY_ROW = 3;
    private static final int BATTERY_ACTION_PERIOD = 4;

    private static final String SPIKE_KEY = "spike";
    private static final int SPIKE_NUM_PROPERTIES = 5;
    private static final int SPIKE_ID = 1;
    private static final int SPIKE_COL = 2;
    private static final int SPIKE_ROW = 3;
    private static final int SPIKE_ACTION_PERIOD = 4;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final int PROPERTY_KEY = 0;

    protected static void load(Scanner in, WorldModel world, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    protected static boolean processLine(String line, WorldModel world,
                                         ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, world, imageStore);
                case SKYE_KEY:
                    return parseSkye(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, world, imageStore);
                case BRIM_KEY:
                    return parseBrim(properties, world, imageStore);
                case AMMO_KEY:
                    return parseAMMO(properties, world, imageStore);
                case BATTERY_KEY:
                    return parseBattery(properties, world, imageStore);
                case SPIKE_KEY:
                    return parseSpike(properties, world, imageStore);
            }
        }

        return false;
    }

    protected static boolean parseBackground(String[] properties,
                                             WorldModel world, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    protected static boolean parseSkye(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
        if (properties.length == SKYE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SKYE_COL]),
                    Integer.parseInt(properties[SKYE_ROW]));
            Entity entity = new SkyeNotFull(properties[SKYE_ID], pt,
                    imageStore.getImageList(SKYE_KEY),
                    Integer.parseInt(properties[SKYE_LIMIT]), 0,
                    Integer.parseInt(properties[SKYE_ACTION_PERIOD]),
                    Integer.parseInt(properties[SKYE_ANIMATION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == SKYE_NUM_PROPERTIES;
    }




    protected static boolean parseObstacle(String[] properties, WorldModel world,
                                           ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = new Obstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    protected static boolean parseBrim(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
        if (properties.length == BRIM_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BRIM_COL]),
                    Integer.parseInt(properties[BRIM_ROW]));
            Entity entity = new Brim(properties[BRIM_ID],
                    pt, imageStore.getImageList(BRIM_KEY),
                    Integer.parseInt(properties[BRIM_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == BRIM_NUM_PROPERTIES;
    }

    protected static boolean parseAMMO(String[] properties, WorldModel world,
                                           ImageStore imageStore) {
        if (properties.length == AMMO_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[AMMO_COL]),
                    Integer.parseInt(properties[AMMO_ROW]));
            Entity entity = new Ammo(properties[AMMO_ID],
                    pt, imageStore.getImageList(AMMO_KEY), 0,0);
            world.tryAddEntity(entity);
        }

        return properties.length == AMMO_NUM_PROPERTIES;
    }



    protected static boolean parseBattery(String[] properties, WorldModel world,
                                      ImageStore imageStore) {
        if (properties.length == BATTERY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BATTERY_COL]),
                    Integer.parseInt(properties[BATTERY_ROW]));
            Entity entity = new Battery(properties[BATTERY_ID], pt,
                    imageStore.getImageList(BATTERY_KEY),
                    Integer.parseInt(properties[BATTERY_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == BATTERY_NUM_PROPERTIES;
    }

    protected static boolean parseSpike(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
        if (properties.length == SPIKE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SPIKE_COL]),
                    Integer.parseInt(properties[SPIKE_ROW]));
            Entity entity = new Spike(properties[SPIKE_ID], pt,
                    imageStore.getImageList(SPIKE_KEY),
                    Integer.parseInt(properties[SPIKE_ACTION_PERIOD]));
            world.tryAddEntity(entity);
        }

        return properties.length == SPIKE_NUM_PROPERTIES;
    }
}
