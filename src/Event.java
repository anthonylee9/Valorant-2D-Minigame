


final class Event
{
   private final Action action;
   private final long time;
   private final Entity entity;

   protected Action getAction(){
      return action;
   }

   protected long getTime(){
      return time;
   }
   protected Entity getEntity() {
      return entity;
   }
   public Event(Action action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
