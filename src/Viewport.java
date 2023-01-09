/*
Viewport ideally helps control what part of the world we are looking at for drawing only what we see
Includes helpful helper functions to map between the viewport and the real world
 */


final class Viewport
{
   private int row;
   private int col;
   private final int numRows;
   private final int numCols;

   protected int getRow(){
      return row;
   }

   protected int getCol(){
      return col;
   }
   protected int getNumRows(){
      return numRows;
   }

   protected int getNumCols(){
      return numCols;
   }
   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }
   protected boolean contains(Point p)
   {
      return p.y >= this.row && p.y < this.row + this.numRows &&
              p.x >= this.col && p.x < this.col + this.numCols;
   }
   protected Point worldToViewport(int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }

   protected Point viewportToWorld(int col, int row)
   {
      return new Point(col + this.getCol(), row + this.getRow());
   }

   protected void shift( int col, int row)
   {
      this.col = col;
      this.row = row;
   }

}