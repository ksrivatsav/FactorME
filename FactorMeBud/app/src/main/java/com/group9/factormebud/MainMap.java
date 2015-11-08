package com.group9.factormebud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;


public class MainMap extends TileView{
//	public static final int	L_TYPE = 109;
	public static final int	BLANK_TYPE = 103;
	public static final int	BOMB_TYPE = 107;
//	public static final int J_TYPE = 1;
//	public static final int	T_TYPE = 2;
//	public static final int	Z_TYPE = 3;
//	public static final int	S_TYPE = 4;
//	public static final int	O_TYPE = 5;
//	public static final int	I_TYPE = 6;

	public static int LEVEL = 1;
    public static int ranRange = 60;
	public static final int PTileInterval=10;
    public int score =0;
	public int scorefactor=10;

	public int tempCount;
	/**
	 * mSnakeTrail: a list of Coordinates that make up the snake's body
	 * mAppleList: the secret location of the juicy apples the snake craves.
	 */

	//private TetrisShape myShape;

	/**
	 * Create a simple handler that we can use to cause animation to happen.  We
	 * set ourselves as a target and we can use the sleep()
	 * function to cause an update/invalidate to occur at a later date.
	 */
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	/**
	 * This is speed parameter of the game
	 */

	public long mMoveDelay;
	public int random;

	private int mGameState = PAUSE;
	//private boolean noShape = true;
	private Factorno curFactorno;

	/**
	 * Two dimensional arrays hold the tetris map
	 * - mapCur - hold the current map
	 * - mapOld - hold the map without current factorno
	 * - mapLast - hold the map before last move of tetrino 
	 */
	private static FactornoMap mapCur;
	public static FactornoMap mapOld;
	private static FactornoMap mapLast;

	private static int[] randArr = {-1,-1};

	private static int[] randomArray = getRandomFromArray();

	private static int currElemIndex = 0;

	/**
	 *  This parameter is the flag that indicate that Action_Down event 
	 *  was occur and factorno was moved left or right
	 */
	private boolean wasMoved;
	private boolean pausePressed = false;

	/**
	 * Initial coordinate of the Action_Down event 
	 */
	private int xInitRaw;
	private int yInitRaw;
	private int yInitDrop;
	private long initTime;
	private static final long deltaTh = 300;//threshold time for drop
	/**
	 * X move sensitivity
	 */
	private static final int xMoveSens = 20;

	/**
	 * Rotate sensitivity
	 */
	private static final int rotateSens = 10;
	/**
	 * Drop down sensitivity
	 */
	private static final int dropSensativity = 30;//~30*3.5
	public static final int READY = 1;
	public static final int PAUSE = 0;


	private class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(mGameState == READY) {
				if(LEVEL==1) scorefactor=10;
				else if(LEVEL==2) scorefactor=15;
				else if(LEVEL==3) scorefactor=20;
				else scorefactor=25;
				if(msg.what == 1) {//TODO change to final Name ... LAST TILE PLACED, CREATE NEW TILE
					int x,y;


					Log.d(TAG, "msgwhat1 ");

					mapCur.copyFrom(mapLast);
					for(int row=0;row<7;row++)
					{
						for(int col=0;col<7;col++){
							if(mapCur.map[row][col]!=mapOld.map[row][col]){
								x = row;
								y = col;
								Log.d(TAG, "x " + x + "y" + y);
								if(mapCur.map[row][col]==103) {

									mapCur.blankTilePowerup(x, y);
//									update();

//									mapCur.patternCheckAndClear(x, y);
								}
								if(mapCur.map[row][col]==107) {

									mapCur.bombTilePowerup(x, y);
									update();

//									mapCur.patternCheckAndClear(x, y);
								}

	//							else if(mapCur.map[row][col]==107)
	//								mapCur.bombTilePowerup(x, y);
//								else
								int rawscore=	mapCur.patternCheckAndClear(x, y);
								if (rawscore==50||rawscore==100) score=rawscore;
								else score=rawscore*scorefactor;
								SCORE=score;
	//							mapOld.copyFrom(mapCur);
	//							mapLast.copyFrom(mapCur);
	//							update();
	//							MainMap.this.invalidate();
							}
						}
					}
//..					int i = mapCur.lineCheckAndClear();
					//Log.d(TAG, "Cleared " + Integer.toString(i) + " lines!");
					mapOld.copyFrom(mapCur);
					if(currElemIndex>=1000)
					{
						randomArray = getRandomFromArray();
						currElemIndex=0;
						Log.d(TAG,"Next: " + Integer.toString(mCurNext));
					}
					//Create currentFactorno power Up Tile at every PTileInterval

						curFactorno = newFactorno(randomArray[currElemIndex], 3, 0);//curFactorno = newFactorno(getRandomFromArr(), 4, 0);//TODO check this

					tempCount++;

						Log.d(TAG,"current elem: " + randomArray[currElemIndex]);
						Log.d(TAG,"next elem: " + randomArray[currElemIndex+1]);
						currElemIndex++;

						mCurNext = randomArray[currElemIndex];
						mCurNextNext = randomArray[currElemIndex+1];
						Log.d(TAG,"Next: " + Integer.toString(mCurNext));

					if(!mapCur.putFactornoOnMap(curFactorno)) {
						Log.d(TAG, "Game Over!");
						initNewGame();
						mGameState = PAUSE;
					}
					mRedrawHandler.sleep(mMoveDelay);
				}
				else {
					clearTiles();
					updateMap();
					mapCur.resetMap();
					mapCur.copyFrom(mapOld);
					gameMove();//TODO insert this function to the Factorno
					MainMap.this.invalidate();
				}

			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendEmptyMessageDelayed(0, delayMillis);
		}
	};


	/**
	 * Constructs a MainMap View based on inflation from XML
	 *
	 * @param context
	 * @param attrs
	 */
	public MainMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "MainMap constructor");
		initMainMap();
	}



	public MainMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d(TAG, "MainMap constructor defStyle");
		initMainMap();
	}

	/**
	 * Initialize MainMap Tail icons from drawable 
	 *
	 */
	private void initMainMap() {
		setFocusable(true);
		mapCur = new FactornoMap();
		mapOld = new FactornoMap();
		mapLast = new FactornoMap();
		resetTiles(1000);//resetTiles(NUM_OF_TILES + 7);//TODO fix this
	}


	public void initNewGame() {
		//mTileList.clear();
		Log.d(TAG, "game init");
		mMoveDelay = 1200;//delay [ms]
		mapCur.resetMap();
		mapOld.resetMap();
		mapLast.resetMap();
		//noShape = true;
		tempCount = 0;
		mRedrawHandler.sendEmptyMessage(1);//TODO change to final name ... START BY PLACING NEW FACTORNO
	}

	private Factorno newFactorno(int val, int x, int y) {
			return new LFactorno(val,x, y);
	}

	/**
	 * Given a ArrayList of coordinates, we need to flatten them into an array of
	 * ints before we can stuff them into a map for flattening and storage.
	 *
	 * @param //pointsList : a ArrayList of Coordinate objects
	 * @return : a simple array containing the x/y values of the coordinates
	 * as [x1,y1,v1,x2,y2,v2,x3,y3,v3...]
	 */
	private int[] coordArrayListToArray(FactornoMap map) {
		int[] rawArray = new int[FactornoMap.MAP_X_SIZE*FactornoMap.MAP_Y_SIZE];
		for (int row = 0; row < FactornoMap.MAP_Y_SIZE; row++) {
			for (int col = 0; col < FactornoMap.MAP_X_SIZE; col++) {
				rawArray[row*FactornoMap.MAP_X_SIZE+col] = map.getMapValue(col, row);
			}
		}
		return rawArray;
	}

	/**
	 * Save game state so that the user does not lose anything
	 * if the game process is killed while we are in the 
	 * background.
	 *
	 * @return a Bundle with this view's state
	 */
	public Bundle saveState() {
		Bundle map = new Bundle();
		map.putIntArray("mapCur", coordArrayListToArray(mapCur));
		map.putIntArray("mapLast", coordArrayListToArray(mapLast));
		map.putIntArray("mapOld", coordArrayListToArray(mapOld));
		map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		return map;
	}

	/**
	 * Given a flattened array of ordinate pairs, we reconstitute them into a
	 * ArrayList of Coordinate objects
	 *
	 * @param rawArray : [x1,y1,x2,y2,...]
	 * @return a ArrayList of Coordinates
	 */
	private FactornoMap coordArrayToArrayList(int[] rawArray) {
		FactornoMap tMap = new FactornoMap();//TODO change to get map from argument
		int arrSize = rawArray.length;
		for (int i = 0; i < arrSize; i++) {
			tMap.setMapValue(i%FactornoMap.MAP_X_SIZE,(i/FactornoMap.MAP_Y_SIZE),rawArray[i]);
		}
		return tMap;
	}

	/**
	 * Restore game state if our process is being relaunched
	 *
	 * @param icicle a Bundle containing the game state
	 */
	public void restoreState(Bundle icicle) {
		setMode(PAUSE);
		mapCur = coordArrayToArrayList(icicle.getIntArray("mapCur"));
		mapLast = coordArrayToArrayList(icicle.getIntArray("mapLast"));
		mapOld = coordArrayToArrayList(icicle.getIntArray("mapOld"));
		mMoveDelay = icicle.getLong("mMoveDelay");
	}

	/*
	 * touch recognition
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//This prevents touchscreen events from flooding the main thread
		synchronized (event)
		{
			try
			{
				//Waits 16ms.
				event.wait(16);

				//when user touches the screen
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					initTime = SystemClock.uptimeMillis();
					xInitRaw = (int) Math.floor(event.getRawX());
					yInitRaw = (int) Math.floor(event.getRawY());
					yInitDrop = yInitRaw;
                    Log.d(TAG, "xInitRaw = " + Integer.toString(xInitRaw));
                    Log.d(TAG, "yInitRaw = " + Integer.toString(yInitRaw));
					wasMoved = false;
					if(xInitRaw > 275 && xInitRaw < 450 && yInitRaw > 25 && yInitRaw < 160) {
						pausePressed = true;
						if(mGameState == READY) {
                            mGameState = PAUSE;
//                            Context context = getContext();
//                            Intent intent = new Intent(context,PauseGame.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
                        }
						else
							mGameState = READY;
					}

                    if(xInitRaw > 100 && xInitRaw < 200 && yInitRaw > 150 && yInitRaw < 300) {
                        mapCur.jumble();
                    }
				}

				if(event.getAction() == MotionEvent.ACTION_MOVE && mGameState == READY && !pausePressed) {
					int xCurRaw = (int) Math.floor(event.getRawX());
					int yCurRaw = (int)Math.floor(event.getRawY());
					if ((xInitRaw - xCurRaw) > xMoveSens && Math.abs(yInitRaw - yCurRaw) < dropSensativity) {
						int q = (xInitRaw - xCurRaw)/xMoveSens;
						if(q > 1)
							Log.d(TAG, "move left q = " + Integer.toString(q));
						wasMoved = true;
						xInitRaw = xCurRaw;
						mapCur.resetMap();
						mapCur.copyFrom(mapOld);
						for (int i = 0; i < q; i++) {
							if (curFactorno.moveLeft(mapCur) &&
									!curFactorno.isColusionY(curFactorno.getYPos()+1, curFactorno.getXPos(), curFactorno.sMap, mapCur, false)) {
								if (mRedrawHandler.hasMessages(1) == true) {//TODO change to final Name
									mRedrawHandler.removeMessages(1);
									mRedrawHandler.sendEmptyMessageDelayed(0, 400);//TODO convert to parameter and change to final Name
								}
							}

							mapCur.putFactornoOnMap(curFactorno);
						}
						update();
					}
					else if((xCurRaw - xInitRaw) > xMoveSens && Math.abs(yInitRaw - yCurRaw) < dropSensativity) {
						int q = (xCurRaw - xInitRaw)/xMoveSens;
						if(q > 1)
							Log.d(TAG, "move left q = " + Integer.toString(q));
						wasMoved = true;
						xInitRaw = xCurRaw;
						mapCur.resetMap();
						mapCur.copyFrom(mapOld);
						for (int i = 0; i < q; i++) {
							if(curFactorno.moveRight(mapCur) &&
									!curFactorno.isColusionY(curFactorno.getYPos()+1, curFactorno.getXPos(), curFactorno.sMap, mapCur, false)) {
								if (mRedrawHandler.hasMessages(1) == true) {//TODO change to final Name
									mRedrawHandler.removeMessages(1);
									mRedrawHandler.sendEmptyMessageDelayed(0, 400);//TODO convert to parameter and change to final Name
								}
							}
							mapCur.putFactornoOnMap(curFactorno);
						}
						update();

					}
					if ((yCurRaw - yInitRaw) > xMoveSens) {
						long timeDelta = Math.abs(initTime - SystemClock.uptimeMillis());
						if(timeDelta > deltaTh) {
							yInitDrop = yCurRaw;
							initTime = SystemClock.uptimeMillis();
						}
						wasMoved = true;
						yInitRaw = yCurRaw;
						//yInitDrop = yInitRaw;
						mapCur.resetMap();
						mapCur.copyFrom(mapOld);
						curFactorno.moveDown(mapCur);
						mapCur.putFactornoOnMap(curFactorno);
						update();

					}
				}

				//when screen is released
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					long timeDelta = Math.abs(initTime - SystemClock.uptimeMillis());
					if(mGameState == READY && !pausePressed){
						int yCurRaw = (int) Math.floor(event.getRawY());
						if(yCurRaw - yInitDrop > dropSensativity && timeDelta < deltaTh) {
							mapCur.resetMap();
							mapCur.copyFrom(mapOld);
							curFactorno.drop(mapCur);
							mapCur.putFactornoOnMap(curFactorno);
							update();
							mRedrawHandler.removeMessages(0);
							mRedrawHandler.sendEmptyMessage(1);//TODO change to final name
						}
					}
					else
						pausePressed = false;
				}
			}
			catch (InterruptedException e)
			{
				return true;
			}
		}
		return true;
	}

	private static int[] getRandomFromArray() {
		Random r = new Random();
		int arr[] = new int[1000];
		for (int i=0; i<1000; i++) {

			if (i >= PTileInterval && i % PTileInterval == 0) {
				Random random = new Random();
				int c = random.nextBoolean() ? BLANK_TYPE : BOMB_TYPE;
				arr[i]=c;
			} else {
				arr[i] = r.nextInt(ranRange - 4) + 4;

			}
		}
		return arr;
	}


	private void gameMove() {
		if(curFactorno.moveDown(mapCur)){
			mapCur.putFactornoOnMap(curFactorno);
			mRedrawHandler.sleep(mMoveDelay);
		}
		else {//TODO convert to parametr and convert to final name
			mRedrawHandler.sendEmptyMessageDelayed(1, 1000);
		}
	}

	/**
	 * Handles the basic update loop, checking to see if we are in the running
	 * state, determining if a move should be made, updating the snake's location.
	 */
	public void update() {
		if(mGameState == READY) {
			clearTiles();
			updateMap();
			MainMap.this.invalidate();
		}
	}

	private void updateMap() {
		mapLast.copyFrom(mapCur);
		for(int col = 0; col < FactornoMap.MAP_X_SIZE; col++){
			for(int row = 0; row < FactornoMap.MAP_Y_SIZE; row++) {
				setTile(mapLast.getMapValue(col, row), col, row);
			}
		}

	}

	public void setMode(int state) {
		// TODO Auto-generated method stub
		mGameState = state;
	}



}
