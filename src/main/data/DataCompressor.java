package main.data;

public class DataCompressor implements DataSeries {
    private DataSeries inputData;
    private CompressionType compressionType;
    private double compression = 1;

    public DataCompressor(DataSeries inputData, CompressionType compressionType, double compression) {
        this.inputData = inputData;
        this.compressionType = compressionType;
        this.compression = compression;
    }


    public DataCompressor(DataSeries inputData, CompressionType compressionType) {
       this(inputData, compressionType, 1);
    }


    public void setCompression(double compression) {
        this.compression = compression;
    }

    public void setSamplingRate(double samplingRate) {
          if(inputData.getScaling() != null) {
              compression = 1 / (samplingRate * inputData.getScaling().getSamplingInterval());
          }
    }

    public void setSamplingInterval(double samplingInterval) {
        if(inputData.getScaling() != null) {
            compression = samplingInterval / inputData.getScaling().getSamplingInterval();
        }
    }


    @Override
    public int get(long index) {
        if(compression == 1) {
            return inputData.get(index);
        }
        if(compression < 1) {
            //int indexNew = Math.min((int)inputData.size() - 1, (int) (compression * index));
            //return inputData.get(indexNew);
            double x = index * compression / sampleRate()  + start();

            return (int)inputData.value(x);
        }

        long result = 0;
        int indexStart =  (int)((index) * compression);
        int indexEnd = Math.min((int)inputData.size(), (int)((index+1) * compression));
        for(int i = indexStart; i < indexEnd; i++) {
            if(compressionType == CompressionType.BOOLEAN) {
                if(inputData.get(i) == 0) {
                    return 0;
                }
            }
            else if(compressionType == CompressionType.MAX) {
                result = Math.max(result, Math.abs(inputData.get(i)));
            }
            else if(compressionType == CompressionType.AVERAGE || compressionType == CompressionType.SUM) {
                result += inputData.get(i);
            }
        }
        if(compressionType == CompressionType.AVERAGE) {
            result = result/(indexEnd - indexStart);
        }
        if(compressionType == CompressionType.BOOLEAN) {
            result = 1;
        }
        return (int)result;
    }

    @Override
    public long size() {
        return (int)(inputData.size() / compression);
    }

    @Override
    public Scaling getScaling() {
        Scaling scalingInput = inputData.getScaling();
        if(scalingInput != null) {
            ScalingImpl scalingOutput = new ScalingImpl(scalingInput);
            scalingOutput.setSamplingInterval(scalingInput.getSamplingInterval() * compression);
            return scalingOutput;
        }
        return null;
    }

    @Override
    public double start() {
        return inputData.start();
    }

    @Override
    public double sampleRate() {
        return 1.0 / (inputData.getScaling().getSamplingInterval() * compression);
    }
}
