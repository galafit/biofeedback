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
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), getMax(), pointsPerUnit(area), units, isHorizontal());

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
        LinearTickProvider tickProvider = new LinearTickProvider(getMin(), getMax(), pointsPerUnit(area), units, isHorizontal());

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

    protected double pointsPerUnit(double length) {
        double min = getMin();
        double max = getMax();

        if (max == min) {return Double.NaN;}
        return length / (max - min);
    }

    @Override
    public Double pointsPerUnit(Rectangle area) {
        Double axisLength = length;
        if(axisLength == null) {
            axisLength = isHorizontal() ? area.getWidth() : area.getHeight();
        }
        return pointsPerUnit(axisLength);
    }

    private double valueToPoint(double value, double origin, double length) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = origin;
        double max = origin + length;
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
        return point;
    }

    private double pointToValue(double point, double origin, double length) {
        double axisMin = getMin();
        double axisMax = getMax();
        double min = origin;
        double max = origin + length;
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


    @Override
    public double valueToPoint(double value, Rectangle area) {
       Double axisOrigin = origin;
       Double axisLength = length;
       if(axisOrigin == null) {
           if (isHorizontal()) {
               axisOrigin = area.getX();;
           }
           else  {
               axisOrigin = area.getMaxY();
           }
       }
       if(axisLength == null) {
           if (isHorizontal()) {
               axisLength = area.getMaxX() - axisOrigin;
           }
           else  {
               axisLength = area.getMinY() - axisOrigin;
           }
       }
        return valueToPoint(value, axisOrigin, axisLength);
    }




    @Override
    public double pointToValue(double point, Rectangle area) {
        Double axisOrigin = origin;
        Double axisLength = length;
        if(axisOrigin == null) {
            if (isHorizontal()) {
                axisOrigin = area.getX();;
            }
            else  {
                axisOrigin = area.getMaxY();
            }
        }
        if(axisLength == null) {
            if (isHorizontal()) {
                axisLength = area.getMaxX() - axisOrigin;
            }
            else  {
                axisLength = area.getMinY() - axisOrigin;
            }
        }
        return pointToValue(point, axisOrigin, axisLength);
    }

}
