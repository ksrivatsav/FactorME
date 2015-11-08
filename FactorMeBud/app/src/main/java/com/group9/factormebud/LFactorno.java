package com.group9.factormebud;

/**
 * Created by NaveenJetty on 11/6/2015.
 */
public class LFactorno extends Factorno {
    //	private static final int BLOCK_TYPE = TileView.BLOCK_WITHNUMB;
    public LFactorno(int val,int x, int y) {
        super(val,x, y);
        initFactorno(val);
        if(ghostEnabled)
            initGhost();
    }

    private void initFactorno(int val) {
        this.sMap[0][0] = val;

    }
}
