package main.chart.axis;

/**
 * Created by hdablin on 15.04.17.
 */
public enum AxisPosition {TOP, BOTTOM, LEFT, RIGHT;
    public boolean isTopOrBottom(){
        if (this == TOP || this == BOTTOM) {return true;}
        return false;
    }
    public boolean isLeftOrRight(){
        if (this == LEFT || this == RIGHT) {return true;}
        return false;
    }
}
