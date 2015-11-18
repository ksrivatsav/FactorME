package com.group9.factormebud;

/**
 * Created by NaveenJetty on 11/6/2015.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FactornoMap {
    public static int TILES_DISAPPEARED=0;
    public static final String TAG = "FactorMap";
    public static final int MAP_X_SIZE = 7;
    public static final int MAP_Y_SIZE = 7;
    //	private int[][] map;
    public int[][] map;

    public FactornoMap() {
        map = new int[MAP_X_SIZE][MAP_Y_SIZE];
        this.resetMap();
    }

    public FactornoMap(int x, int y) {
        //TODO implement this for any map size
    }

    public void resetMap() {
        for(int x = 0; x < MAP_X_SIZE; x++) {
            for(int y = 0; y < MAP_Y_SIZE;y++) {
                map[x][y] = 0;
            }
        }
    }

    /**
     * This function puts the factorno on the map
     * and check collision with the other objects on a map
     * @param shape - factorno to put
     * @return true if puts successful otherwise false
     */
    public boolean putFactornoOnMap(Factorno shape) {
        for(int col = 0; col < shape.getSize(); col++){
            for(int row = 0; row < shape.getSize(); row++) {
                if (shape.sMap[col][row] != TileView.BLOCK_EMPTY) {
                    if(shape.getXPos() + col >= 0 && shape.getXPos() + col < FactornoMap.MAP_X_SIZE &&
                            shape.getYPos() + row >= 0 && shape.getYPos() + row < FactornoMap.MAP_Y_SIZE &&
                            (map[shape.getXPos() + col][shape.getYPos() + row] == TileView.BLOCK_EMPTY ||
                                    map[shape.getXPos() + col][shape.getYPos() + row] == TileView.BLOCK_GHOST))
                        map[shape.getXPos()+col][shape.getYPos()+row] = shape.sMap[col][row];
                    else
                        return false;
                }
                if(Factorno.ghostEnabled && !shape.onGhost()) {
                    if(shape.gMap[col][row] != TileView.BLOCK_EMPTY)
                        map[shape.getGhostXPos()+col][shape.getGhostYPos()+row] = shape.gMap[col][row];
                }

            }
        }
        return true;
    }


    public int getMapValue(int x, int y) {
        return map[x][y];
    }

    public void copyFrom(FactornoMap srcMap) {
        for (int x = 0; x < FactornoMap.MAP_X_SIZE; x++) {
            for(int y = 0; y < FactornoMap.MAP_Y_SIZE; y++) {
                this.map[x][y] = srcMap.getMapValue(x, y);
            }
        }

    }

    /**
     * This function checks if there is a common factor
     */

    public int GCD(int a, int b)
    {
        //Check for the wildcard tile
        if (a == 103)
            return b;
        else if (b==103)
            return a;

        while (b > 0)
        {
            int temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    public int GCD(int prNum, int[] input)
    {
        int result = prNum;
        for(int i = 0; i < input.length; i++) result = GCD(result, input[i]);
        return result;
    }

    public int GCDarr(int array1[],int size)
    {
        int gcdval = array1[0];
        for(int i = 1; i < size; i++)
            gcdval = GCD(gcdval, array1[i]);
        return gcdval;
    }

    public int[][] getLeftNeighbours(int i,int j) //XY
    {
        int[][] left=new int[3][2];

        if(i>=0 && i<7 && (j-1)>=0 && (j-1)<7 && map[i][j-1]!=0)
        {
            left[0][0]=i;
            left[0][1]=j-1;
        }
        if((i-1)>=0 && (i-1)<7 && j>=0 && j<7 && map[i-1][j]!=0)
        {
            left[1][0]=i-1;
            left[1][1]=j;
        }
        if(i>=0 && i<7 && (j+1)>=0 && (j+1)<7 && map[i][j+1]!=0)
        {
            left[2][0]=i;
            left[2][1]=j+1;
        }

        return left;
    }

    public int[][] getDownNeighbours(int i,int j)
    {
        int[][] down=new int[3][2];

        if((i-1)>=0 && (i-1)<7 && j>=0 && j<7 && map[i-1][j]!=0)
        {
            down[0][0]=i-1;
            down[0][1]=j;
        }
        if(i>=0 && i<7 && (j+1)>=0 && (j+1)<7 && map[i][j+1]!=0)
        {
            down[1][0]=i;
            down[1][1]=j+1;
        }
        if((i+1)>=0 && (i+1)<7 && j>=0 && j<7 && map[i+1][j]!=0)
        {
            down[2][0]=i+1;
            down[2][1]=j;
        }


        return (down);
    }

    public int[][] getRightNeighbours(int i,int j)
    {
        int[][] right=new int[3][2];

        if(i>=0 && i<7 && (j-1)>=0 && (j-1)<7 && map[i][j-1]!=0)
        {
            right[0][0]=i;
            right[0][1]=j-1;
        }
        if((i+1)>=0 && (i+1)<7 && j>=0 && j<7 && map[i+1][j]!=0)
        {
            right[1][0]=i+1;
            right[1][1]=j;
        }
        if(i>=0 && i<7 && (j+1)>=0 && (j+1)<7 && map[i][j+1]!=0)
        {
            right[2][0]=i;
            right[2][1]=j+1;
        }
        return (right);
    }

    public void blankTilePowerup(int r, int c)
    {
        int x=r;
        int y=c;
        int[][] cpos;
        int[][] npos;
        int gcdval=0;
        cpos = getDownNeighbours(x, y);
        int lneighx=cpos[0][0];
        int lneighy=cpos[0][1];
        int dneighx=cpos[1][0];
        int dneighy=cpos[1][1];
        int rneighx=cpos[2][0];
        int rneighy=cpos[1][1];
        boolean noleftneigh=false;
        boolean norightneigh=false;
        boolean nodownneigh=false;
        if(lneighy+lneighx==0) noleftneigh=true;
        if(rneighy+rneighx==0) norightneigh=true;
        if(dneighy+dneighx==0) nodownneigh=true;

        // if left neigh of downtile exists
        if(!noleftneigh)
        {int elem1=map[lneighx][lneighy];
            npos=getLeftNeighbours(lneighx, lneighy);
            for(int i=0; i<3;i++)
            {
                if(map[npos[i][0]][npos[i][1]]!=0) {
                    gcdval = GCD(elem1, map[npos[i][0]][npos[i][1]]);
                    if (gcdval > 1)
                        break;
                }
            }
        }
        // if down->down exists
        else if(!nodownneigh)
        {int elem1=map[dneighx][dneighy];
            npos=getDownNeighbours(dneighx, dneighy);
            for(int i=0; i<3;i++)
            {
                if(map[npos[i][0]][npos[i][1]]!=0) {
                    gcdval = GCD(elem1, map[npos[i][0]][npos[i][1]]);
                    if (gcdval > 1)
                        break;
                }
            }
        }
        else if(!norightneigh)
        {int elem1=map[rneighx][rneighy];
            npos=getDownNeighbours(rneighx, rneighy);
            for(int i=0; i<3;i++)
            {
                if(map[npos[i][0]][npos[i][1]]!=0) {
                    gcdval = GCD(elem1, map[npos[i][0]][npos[i][1]]);
                    if (gcdval > 1)
                        break;
                }
            }
        }
        else
        {
            //if no neighbours
            Random rand=new Random();
            gcdval = rand.nextInt(100 - 4) + 4;
        }
        if(gcdval==1)
        {
            if(!noleftneigh) gcdval=map[lneighx][lneighy];
            else if(!norightneigh) gcdval=map[rneighx][rneighy];
            else gcdval=map[dneighx][dneighy];

        }
        map[x][y]=gcdval;
        Log.d(TAG,"x"+x+"y"+y+"gcdval"+gcdval);

    }//end of blank tile powerup

    public int bombTilePowerup(int x, int y)
    {
        int deltilecount=0;
        map[x][y]=0;
        if((x-1)>=0 && (x-1)<7 && map[x - 1][y]!= 0) {
            map[x - 1][y] = 0;
            deltilecount++;
            int col = x - 1;
            for (int row = y; row > 0; row--) {
                map[col][row] = map[col][row - 1];
            }
        }
        if((y+1)>=0&&(y+1)<7 && map[x][y + 1]!= 0) {
            map[x][y + 1] = 0;
            deltilecount++;
            int col = x;
            for (int row = y+1; row > 0; row--) {
                map[col][row] = map[col][row - 1];
            }
        }
        if((x+1)>=0&&(x+1)<7 && map[x + 1][y]!= 0) {
            map[x + 1][y] = 0;
            deltilecount++;
            int col = x + 1;
            for (int row = y; row > 0; row--) {
                map[col][row] = map[col][row - 1];
            }
        }

        return deltilecount;
    }

    public int[][] patternCheck(int r, int c) {
        int x = r;
        int y = c;



        int[] finalArray = new int[10];
        int[][] finalArraypos = new int[10][2];
        int[] n1, n2, n3 = new int[3];
        int[][] n1pos, n2pos, n3pos = new int[3][2];
        int count = 0;
        int cur = map[x][y];
//	int[] c = new int[3];
        int[][] cpos;
//	PatternMatch pm=new PatternMatch();
        cpos = getDownNeighbours(x, y);
        finalArray[count] = cur;
        finalArraypos[count][0] = x;
        finalArraypos[count][1] = y;
        count++;
        int x1pos = cpos[0][0];
        int y1pos = cpos[0][1];
        if ((x1pos + y1pos != 0) && GCD(map[x1pos][y1pos], finalArray) > 1) {
            //finalArray[count]=c[1];
            finalArray[count] = map[x1pos][y1pos];
            finalArraypos[count][0] = x1pos;
            finalArraypos[count][1] = y1pos;
            count++;
            n1pos = getLeftNeighbours(x1pos, y1pos);
            int n1xpos;
            int n1ypos;
            for (int i = 0; i < n1pos.length; i++) {
                n1xpos = n1pos[i][0];
                n1ypos = n1pos[i][1];
                if ((n1xpos + n1ypos != 0) && GCD(map[n1xpos][n1ypos], finalArray) > 1) {
                    finalArray[count] = map[n1xpos][n1ypos];
                    finalArraypos[count][0] = n1xpos;
                    finalArraypos[count][1] = n1ypos;
                    count++;
                }
            }
        }

        int x2pos = cpos[1][0];
        int y2pos = cpos[1][1];
        if ((x2pos + y2pos != 0) && GCD(map[x2pos][y2pos], finalArray) > 1) {
            //finalArray[count++]=c[2];
            finalArray[count] = map[x2pos][y2pos];
            finalArraypos[count][0] = x2pos;
            finalArraypos[count][1] = y2pos;
            count++;
            n2pos = getDownNeighbours(x2pos, y2pos);
            int n2xpos;
            int n2ypos;
            for (int i = 0; i < n2pos.length; i++) {
                n2xpos = n2pos[i][0];
                n2ypos = n2pos[i][1];
                if ((n2xpos + n2ypos != 0) && GCD(map[n2xpos][n2ypos], finalArray) > 1) {
                    //finalArray[count++]=n2[i];
                    finalArray[count] = map[n2xpos][n2ypos];
                    finalArraypos[count][0] = n2xpos;
                    finalArraypos[count][1] = n2ypos;
                    count++;
                }
            }
        }
        int x3pos = cpos[2][0];
        int y3pos = cpos[2][1];
        if ((x3pos + y3pos != 0) && GCD(map[x3pos][y3pos], finalArray) > 1) {
            //finalArray[count++]=c[3];
            finalArray[count] = map[x3pos][y3pos];
            finalArraypos[count][0] = x3pos;
            finalArraypos[count][1] = y3pos;
            count++;
            n3pos = getRightNeighbours(x3pos, y3pos);
            int n3xpos;
            int n3ypos;
            for (int i = 0; i < n3pos.length; i++) {
                n3xpos = n3pos[i][0];
                n3ypos = n3pos[i][1];
                if ((n3xpos + n3ypos != 0) && GCD(map[n3xpos][n3ypos], finalArray) > 1) {
                    //finalArray[count++]=n3[i];
                    finalArray[count] = map[n3xpos][n3ypos];
                    finalArraypos[count][0] = n3xpos;
                    finalArraypos[count][1] = n3ypos;
                    count++;
                }
            }
        }
        //    if (count >= 3) {
        return (finalArraypos);
    }

    public int clearPattern(int[][] finalArraypos)
    {
        TILES_DISAPPEARED=0;
        boolean isConsecutive = false;
        boolean isContinuous = false;
        int isConsecutiveCounter = 0;
        int isContinuousCounter = 0;
        ArrayList<Integer> removedelem = new ArrayList<Integer>();

        int[][] removePos;
        int[][] sortremovePos = new int[10][2];
        int removecounter = 0;

        removePos = finalArraypos;

        //sort the positions row-wise in ascending order
        for (int rr = 1; rr < 7; rr++) {
            for (int r1 = 0; r1 < 10; r1++) {
                if (removePos[r1][0] == 0 && removePos[r1][1] == 0) {

                    break;
                } else {
                    if (removePos[r1][1] == rr) {
                        sortremovePos[removecounter][0] = removePos[r1][0];
                        sortremovePos[removecounter][1] = removePos[r1][1];
                        int remove_x=sortremovePos[removecounter][0];
                        int remove_y=sortremovePos[removecounter][1];
                        removedelem.add(map[remove_x][remove_y]);
                        TILES_DISAPPEARED++;
                        removecounter++;

                    }
                }

            }

        }
        if(removedelem.size()>2) {
            int diff=Math.abs(removedelem.get(1)-removedelem.get(0));
            for (int i = 1; i < removedelem.size(); i++)//Check for patterns
            {
                if(removedelem.get(i)==removedelem.get(i-1))
                {
                    isContinuous=true;
                    isContinuousCounter++;
                } else isContinuous=false;
                if (Math.abs(removedelem.get(i)-removedelem.get(i-1))==diff)
                {
                    isConsecutive=true;
                    isConsecutiveCounter++;

                }else isConsecutive=false;

            }
        }
        for(int k=0;k<removecounter;k++)
        {
            Log.d(TAG, "X" + sortremovePos[k][0] + "Y" + sortremovePos[k][1]);
        }

        for (int i = 0; i < removecounter; i++) {
            int col = sortremovePos[i][0];
            for (int row = sortremovePos[i][1]; row > 0; row--) {
                map[col][row] = map[col][row - 1];
            }

        }


        //   }

        for(int k=0;k<7;k++) {
            for (int l = 0; l < 7; l++) {
                Log.d(TAG,"-"+k+","+l+"-"+map[k][l]);
            }
        }
        if(isConsecutive) return 50;
        else if(isContinuous) return 100;
        else return TILES_DISAPPEARED;
    }/*function pattern closing brace*/

    public void jumble()
    {
        List<List<Integer>> elempos = new ArrayList<List<Integer>>();
        ArrayList<Integer> elem = new ArrayList<Integer>();
        int pos[][]= new int[7][7];

        int elemcount=0;

        for(int row=6;row>=0;row--)
        {
            for(int col=0;col<7;col++)
            {
                if (map[row][col]>=4)
                {
                    elempos.add(Arrays.asList(row, col));
                    elem.add(map[row][col]);
                    elemcount++;
                    //                  System.out.print("-"+elem.get(elemcount-1));
                    pos[row][col]=1;
                }
            }
        }

        //     System.out.println();
        Collections.shuffle(elem);
        //      Collections.shuffle(elem);

//        for (int k=0;k<elemcount;k++)
        //      {
        //        System.out.print("-"+elem.get(k));
        //  }


/*	for(int point=0;point<elempos.size();point++)
		{
		    System.out.println("-x"+elempos.get(point).get(0)+"-y"+elempos.get(point).get(1));
			map1[elempos.get(point).get(0)][elempos.get(point).get(1)] = elem.get(point);
		}
*/
        int counter=0;
        for(int row=0;row<7;row++)
        {
            for(int col=0;col<7;col++)
            {
                if (pos[row][col]!=0)
                {
                    map[row][col]=elem.get(counter);
                    counter++;
                }
            }
        }

    }



    public void setMapValue(int i, int j, int value) {
        // TODO Auto-generated method stub
        map[i][j] = value;
    }
}
