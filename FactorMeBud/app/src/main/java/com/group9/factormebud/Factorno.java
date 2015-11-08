package com.group9.factormebud;

/**
 * Created by NaveenJetty on 11/6/2015.
 */
import android.graphics.Point;

public abstract class Factorno {
    public static final int SIZE = 1;
    public static boolean ghostEnabled = true;
    public int[][] sMap;
    //...	public int[] sMap;
    public int[][] gMap;//ghost map of tetrino
    //...	public int[] gMap;
    //public int[][] shadowMap;
    private Point pos;
    protected Point ghostPos;

    public int getGhostXPos() {
        return ghostPos.x;
    }

    public int getGhostYPos() {
        return ghostPos.y;
    }

    public Factorno(int val,int x, int y) {
        sMap = new int[SIZE][SIZE];
//...		sMap = TileView.BLOCK_EMPTY;;
        gMap = new int[SIZE][SIZE];
//...	gMap = TileView.BLOCK_EMPTY;;
        initFactorno(val, SIZE);
        pos = new Point(x,y);
        ghostPos = new Point(x,y);
    }

    private void initFactorno(int val,int tetrinoSize) {
        for(int col = 0; col < tetrinoSize; col++) {
            for(int row = 0; row < tetrinoSize; row++) {
                //sMap[row][col] = TileView.BLOCK_EMPTY;
                //gMap[row][col] = TileView.BLOCK_EMPTY;//ghost Map
                sMap[row][col] = val;
                gMap[row][col] = val;//ghost Map
            }
        }

    }

    protected void initGhost() {
        copyTetrinoMap(sMap, gMap, Factorno.SIZE);
        ghostPos.set(pos.x, pos.y);
        setGhostY();
    }

    protected void resetGhost(int size) {
        for(int col = 0; col < size; col++) {
            for(int row = 0; row < size; row++)
                gMap[col][row] = TileView.BLOCK_EMPTY;//ghost Map
        }
    }

    /**
     * @return the pos
     */
    public Point getPos() {
        return pos;
    }

    /**
     * @param //pos the pos to set
     */
    public boolean setPos(int x, int y, FactornoMap map) {
        if(x >= 0 && x < FactornoMap.MAP_X_SIZE) {
            for(int col = 0; col < this.getSize(); col++){
                for(int row = 0; row < this.getSize(); row++) {
                    if (sMap[col][row] != TileView.BLOCK_EMPTY) {
                        if (x + col >= FactornoMap.MAP_X_SIZE || x + col < 0 ||
                                y + row >= FactornoMap.MAP_Y_SIZE ||
                                map.getMapValue(x + col, y + row) != TileView.BLOCK_EMPTY)
                            return false;
                    }
                }
            }
        }
        this.pos.x = x;
        this.pos.y = y;
        return true;
    }

    protected boolean isColusionY(int newY, int newX, int[][] tMap,FactornoMap map, boolean isGhost) {
        // TODO Auto-generated method stub
        if(newY < FactornoMap.MAP_Y_SIZE) {
            for(int col = 0; col < this.getSize(); col++){
                for(int row = 0; row < this.getSize(); row++) {
                    if (tMap[col][row] != TileView.BLOCK_EMPTY) {
                        if (isGhost) {//TODO need to think about if condition
                            if ((newX + col) >= 0 && (newX + col) < FactornoMap.MAP_X_SIZE) {
                                if (newY + row >= FactornoMap.MAP_Y_SIZE ||
                                        map.getMapValue(newX + col, newY + row) != TileView.BLOCK_EMPTY)
                                    return true;
                            }
                        }
                        else {
                            if ((newX + col) >= 0 && (newX + col) < FactornoMap.MAP_X_SIZE) {
                                if (newY + row >= FactornoMap.MAP_Y_SIZE ||
                                        map.getMapValue(newX + col, newY + row) != TileView.BLOCK_EMPTY)
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        else
            return true;
        //if no collisions
        return false;
    }
    /**
     * This function move tetrino down by 1
     * @param map - to check if possible
     * @return true is success else false
     */
    public boolean moveDown(FactornoMap map) {
        if(!isColusionY(this.pos.y+1, this.pos.x, sMap, map, false)) {
            this.pos.y++;
            return true;
        }
        else
            return false;
    }

    protected boolean isColusionX(int newX, int[][] tMap,FactornoMap map) {
        // TODO Auto-generated method stub
        if(newX >= -1 && newX < FactornoMap.MAP_X_SIZE) {
            for(int col = 0; col < this.getSize(); col++){
                for(int row = 0; row < this.getSize(); row++) {
                    if (tMap[col][row] != TileView.BLOCK_EMPTY) {
                        if (newX + col >= FactornoMap.MAP_X_SIZE || newX + col < 0 ||
                                map.getMapValue(newX + col, this.pos.y + row) != TileView.BLOCK_EMPTY)
                            return true;
                    }
                }
            }
        }
        else
            return true;
        //if no collisions
        return false;
    }


    public boolean moveLeft(FactornoMap map) {
        if(!isColusionX(this.pos.x-1, sMap, map)) {
            this.pos.x--;
            this.ghostPos.x--;
            setGhostY();
            return true;
        }
        return false;
    }

    public boolean moveRight(FactornoMap map) {
        if(!isColusionX(this.pos.x+1, sMap, map)) {
            this.pos.x++;
            this.ghostPos.x++;
            setGhostY();
            return true;
        }
        return false;
    }

    public void drop(FactornoMap map) {
        if(ghostEnabled)
            this.pos.y = this.ghostPos.y;
        else
            for (int y = 0; y < FactornoMap.MAP_Y_SIZE && !isColusionY(y, this.pos.x, sMap, map, false); y++)
                this.pos.y = y;
    }

    /**
     * @return the x position
     */
    public int getXPos() {
        return pos.x;
    }

    /**
     * @return the y position
     */
    public int getYPos() {
        return pos.y;
    }

    public int getSize() {
        // TODO Auto-generated method stub
        return SIZE;
    }

    protected void copyTetrinoMap(int[][] srcMap, int[][] destMap, int size) {
        for(int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (srcMap[x][y] != TileView.BLOCK_EMPTY)
                    destMap[x][y] = TileView.BLOCK_GHOST;
            }
        }
    }

    protected void setGhostY() {
        for (ghostPos.y = this.pos.y;
             !isColusionY(this.ghostPos.y+1, this.ghostPos.x, gMap, MainMap.mapOld, true);
             this.ghostPos.y++);
    }

    public boolean onGhost() {
        if(pos.y == ghostPos.y)
            return true;
        return false;
    }

}
