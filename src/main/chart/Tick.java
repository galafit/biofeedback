package main.chart;

/**
 * Created by hdablin on 08.04.17.
 */
public class Tick {
    private int point;
    private String label;

    public Tick(int point, String label) {
        this.point = point;
        this.label = label;
    }

    public int getPoint() {
        return point;
    }

    public String getLabel() {
        return label;
    }
}
