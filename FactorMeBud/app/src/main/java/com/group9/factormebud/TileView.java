package com.group9.factormebud;

/**
 * Created by NaveenJetty on 11/6/2015.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TileView extends View {
    public static final String TAG = "Factor Me";
    public static int SCORE=0;
    public static int PENALTY=50;

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    protected static final int BLOCK_EMPTY = 0;
    protected static final int BLOCK_WITHNUMB = 1;
    protected static final int BLOCK_GHOST = 1;
    protected static final int NUM_OF_TILES = 7;

    protected static final int RANGE = 100;
    /**
     * Parameters controlling the size of the tiles and their range within view.
     * Width/Height are in pixels, and Drawables will be scaled to fit to these
     * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
     */
    protected static final double mXRatio = 0.725; //This is ratio of the tetris map to size of view
    protected static int mTileSize;

    protected static final int mXTileCount = 7;
    protected static final int mYTileCount = 7;

    protected static int mXOffset;
    protected static int mYOffset;

    Bitmap shuffleimage= BitmapFactory.decodeResource(getResources(), R.drawable.shuffle);
    Bitmap pauseimage= BitmapFactory.decodeResource(getResources(), R.drawable.pause);


    /**
     * A hash that maps integer handles specified by the subclasser to the
     * drawable that will be used for that reference
     */
    private Bitmap[] mTileArray;

//    private Bitmap[] mNextTetrinoArr;
    /**
     * A two-dimensional array of integers in which the number represents the
     * index of the tile that should be drawn at that locations
     */
    private int[][] mTileGrid;
    public int[][] highlightGrid;

    public static boolean drawhighlight=false;
    public static boolean enablejumble=false;

    private final Paint mPaint = new Paint();

    protected int mCurNext;
    protected int mCurNextNext;

    //Constructors
    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTileGrid = new int[mXTileCount][mYTileCount];
        highlightGrid = new int[mXTileCount][mYTileCount];
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTileGrid = new int[mXTileCount][mYTileCount];
        highlightGrid = new int[mXTileCount][mYTileCount];
    }


    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and
     * sets the maximum index of tiles to be inserted
     *
     * @param tilecount
     */

    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateTileSize(w, h);
        Resources r = this.getContext().getResources();
        loadAllTiles(RANGE, r.getDrawable(R.drawable.tile));
        loadTile(BLOCK_GHOST, r.getDrawable(R.drawable.block_ghost2));
        loadTile(103, r.getDrawable(R.drawable.tile));
        loadTile(107, r.getDrawable(R.drawable.bombtile));
        loadTile(907, r.getDrawable(R.drawable.highlight1));
        clearTiles();
    }

    protected void calculateTileSize(int w, int h) {
        Log.d(TAG, "OnSize changed, w = " + Integer.toString(w) + "h = " + Integer.toString(h));
        mTileSize = (int)Math.floor((w*mXRatio)*1.1/mXTileCount);
        mXOffset = mTileSize;
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

    }

    /**
     * Function to set the specified Drawable as the tile for a particular
     * integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);
        //      String strkey=Integer.toString(key);
        //      Paint paint = new Paint();
        //      paint.setStyle(Paint.Style.STROKE);
        //      paint.setTextSize((int) (20));
        //      canvas.drawText(strkey, canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
        mTileArray[key] = bitmap;
    }

    public void loadAllTiles(int range, Drawable tile) {
        for (int i = 4; i <= range; i++) {
            Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            tile.setBounds(0, 0, mTileSize, mTileSize);
            tile.draw(canvas);
            String strkey = Integer.toString(i);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextSize((int) (28));
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(strkey,(float)(canvas.getWidth()/2),(float)(canvas.getHeight()/2), paint);
            mTileArray[i] = bitmap;
        }
    }

    /**
     * Resets all tiles to BLOCK_EMPTY
     *
     */
    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(BLOCK_EMPTY, x, y);
            }
        }
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced
     * by an integer) should be drawn at the given x/y coordinates during the
     * next invalidate/draw cycle.
     *
     * @param tileindex
     * @param x
     * @param y
     */
    public void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    public void setPENALTY(int Level){
        switch (Level){
            case 1:
                PENALTY = 50;
                break;
            case 2:
                PENALTY = 75;
                break;
            case 3:
                PENALTY = 75;
                break;
            default:
                PENALTY = 100;
                break;
        }
    }


    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {

                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]],
                            mXOffset + x * mTileSize,
                            mYOffset + y * mTileSize,
                            mPaint);//TODO play with mPaint
                }
            }
        }
        if(drawhighlight==true) {
            for (int x = 0; x < mXTileCount; x += 1) {
                for (int y = 0; y < mYTileCount; y += 1) {
                    if (highlightGrid[x][y] == 907) {

                        canvas.drawBitmap(mTileArray[907],
                                mXOffset + x * mTileSize,
                                mYOffset + y * mTileSize,
                                mPaint);//TODO play with mPaint
                    }
                }
            }
            drawhighlight=false;
            highlightGrid=new int[mXTileCount][mYTileCount];
        }
        canvas.drawBitmap(mTileArray[mCurNext], 350, 130, mPaint);
        canvas.drawBitmap(mTileArray[mCurNextNext], 500, 130, mPaint);
        if (SCORE>=250 && enablejumble==true) {
            Bitmap jumble = Bitmap.createScaledBitmap(shuffleimage, mTileSize, mTileSize, false);
            canvas.drawBitmap(jumble, 100, 140, mPaint);
        }
        Bitmap pause = Bitmap.createScaledBitmap(pauseimage, mTileSize, mTileSize, false);
        canvas.drawBitmap(pause, 300, 50, mPaint);
        String strscore=Integer.toString(SCORE);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50);
        canvas.drawText(strscore, 130, 80, mPaint);
    }

}
