package main.chart.axis;

import java.awt.*;
import java.util.List;

/**
 * Created by hdablin on 08.04.17.
 */
public class LinearAxisData extends AxisData {
    private Double roundMin = null;
    private Double roundMax = null;

    @Override
    public List<Tick> getTicks(Rectangle area) {
        roundMax = null;
        roundMin = null;
        String units = getUnits();
        units = getAxisViewSettings().isUnitsVisible() ? units : null;
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), getMax(), pointsPerUnit(area), units);

        double tickInterval = getTicksSettings().getTickInterval();
        int ticksAmount = getTicksSettings().getTicksAmount();
        if(ticksAmount > 0) {
            tickProvider.setTicksAmount(ticksAmount);
        }
        else if( tickInterval > 0) {
            tickProvider.setTicksInterval(tickInterval);
        } else {
            tickProvider.setTickPixelInterval(getTicksSettings().getTickPixelInterval());
        }
        List<Tick> ticks = tickProvider.getTicks();
        if(ticks.size() > 1) {
            roundMin = ticks.get(0).getValue();
            roundMax = ticks.get(ticks.size() - 1).getValue();
        }
        return ticks;
    }

    @Override
    public List<Tick> getTicks(Rectangle area, double minTickPixelInterval) {
        roundMax = null;
        roundMin = null;
        String units = getUnits();
        units = getAxisViewSettings().isUnitsVisible() ? units : null;
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), getMax(), pointsPerUnit(area), units);

        double tickInterval = getTicksSettings().getTickInterval();
        int ticksAmount = getTicksSettings().getTicksAmount();
        if(ticksAmount > 0) {
            tickProvider.setTicksAmount(ticksAmount);
        }
        else if(tickInterval > 0) {
            tickProvider.setTicksInterval(tickInterval);
        } else {
            tickProvider.setMinTickPixelInterval(minTickPixelInterval);
        }
        List<Tick> ticks = tickProvider.getTicks();

        if(ticks.size() > 1) {
            roundMin = ticks.get(0).getValue();
            roundMax = ticks.get(ticks.size() - 1).getValue();
        }

        return ticks;
    }

    @Override
    public Double getMin() {
        if (isEndOnTick() && roundMin != null){
            return roundMin;
        }
        return super.getMin();
    }

    @Override
    public Double getMax() {
        if (isEndOnTick() && roundMax != null){
            return roundMax;
        }
        return super.getMax();
    }

    @Override
    public Double pointsPerUnit(Rectangle area) {
        double min = getMin();
        double max = getMax();

        if (max == min) {return Double.NaN;}

        if (isHorizontal()) {
            return (area.getWidth()) / (max - min);
        }
        return (area.getHeight())/ (max - min);
    }

    @Override
    public int valueToPoint(double value, Rectangle area) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = 0;
        double max = 0;
        if (isHorizontal()) {
            min = area.getX();
            max = area.getMaxX();
        }
        else  {
            max = area.getMinY();
            min = area.getMaxY();
        }

        double point;
        if (axisMin == axisMax){
           point = min + (max - min) / 2;
        } else {
            if (isInverted()) {
                point = max - ((value - axisMin) / (axisMax - axisMin)) * (max - min);
            }
            else {
                point = min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
            }
        }
        Long l =  Math.round(point);
        return l.intValue();
    }

    @Override
    public double pointsToValue(int point, Rectangle area) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = 0;
        double max = 0;
        if (isHorizontal()) {
            min = area.getX();
            max = area.getMaxX();
        }
        else  {
            max = area.getMinY();
            min = area.getMaxY();
        }

        if (axisMin == axisMax){
            return axisMax;
        }
        if (isInverted()) {
            return axisMax - (point - min) / (max - min) * (axisMax - axisMin);
        }
        else {
            return axisMin + (point - min) / (max - min) * (axisMax - axisMin);
        }
    }

}
