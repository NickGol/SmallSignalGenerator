package Swing;

import java.util.LinkedList;
import java.util.List;

import java.util.*;
import javax.swing.SwingWorker;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTime {

    MySwingWorker mySwingWorker;
    SwingWrapper<XYChart> sw;
    XYChart chart;

    public static void main(String[] args) throws Exception {

        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime();
        swingWorkerRealTime.go();
    }

    private void go() {

        // Create Chart
        chart = QuickChart.getChart("SwingWorker XChart Real-time Demo", "Time", "Value", "randomWalk", new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        mySwingWorker = new MySwingWorker();
        mySwingWorker.execute();
    }

    private class MySwingWorker extends SwingWorker<Boolean, double[]> {

        LinkedList<Float> fifo = new LinkedList<Float>();
        SinusGenerator gen = new SinusGenerator(3600);

        public MySwingWorker() {
            fifo.add(0.0f);
        }

        @Override
        protected Boolean doInBackground() throws Exception {

            while (!isCancelled()) {

//                fifo.add(fifo.get(fifo.size() - 1) + Math.random() - .5);
                fifo.add(gen.getSignalData()[0]);
                if (fifo.size() > 100) {
                    fifo.removeFirst();
                }

                double[] array = new double[fifo.size()];
                for (int i = 0; i < fifo.size(); i++) {
                    array[i] = fifo.get(i);
                }
                publish(array);

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }

            }

            return true;
        }

        @Override
        protected void process(List<double[]> chunks) {

            System.out.println("number of chunks: " + chunks.size());

            double[] mostRecentDataSet = chunks.get(chunks.size() - 1);

            chart.updateXYSeries("randomWalk", null, mostRecentDataSet, null);
            sw.repaintChart();

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(40 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
            } catch (InterruptedException e) {
            }

        }
    }
}



/**
 * Creates virtual channel buffer and initializes it with sinusoidal data with random noise (every 100 elements).
 * Channel amplitude -1 .. +1
 * Channel period 360 elements
 */
 class SinusGenerator {

    private List<Float> signalRepository;

    /**
     * Constructs an object with initialized channel buffer.
     *
     * @param  capacity  the capacity of the channel buffer
     * @throws IllegalArgumentException if the specified initial capacity is zero or negative
     */
    public SinusGenerator(int capacity) {

        if (capacity > 0) {
            this.signalRepository = new LinkedList<Float>();
            initializeSignalRepository(capacity);
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
    }

    /**
     * Returns all data from inner channel buffer.
     *
     * @return All data from inner channel buffer.
     */
    public Float[] getSignalData() {

        return getSignalData(signalRepository.size());
    }

    /**
     * Returns from inner channel buffer the amount of points specified in quantity parameter.
     *
     * @param quantity the amount of points that you want to get.
     * @throws IllegalArgumentException if the specified capacity of inner channel buffer is smaller than the quantity parameter or negative.
     */
    public Float[] getSignalData(int quantity) {

        if (quantity < 0)
            throw new IllegalArgumentException("Requested quantity: " + quantity + " is negative");
        if (quantity > signalRepository.size())
            throw new IllegalArgumentException("Requested quantity: " + quantity + " exceeds inner channel buffer size: " + signalRepository.size());

        Float[] temporaryArray = signalRepository.subList(signalRepository.size() - quantity, signalRepository.size())
                .stream().toArray(Float[]::new);
        updateSignalRepository();
        return temporaryArray;
    }

    private void initializeSignalRepository(int capacity) {

        Random random = new Random();
        for (int i = 0; i < capacity; i++) {
            Float temporaryVariable = (float) Math.sin(Math.PI / 180 * i);
            if (i % 100 == 0) {
                temporaryVariable = temporaryVariable + random.nextFloat() - (float) 0.5;
            }
            signalRepository.add(temporaryVariable);
        }
    }

    private void updateSignalRepository() {

        Collections.rotate(signalRepository, -10);
    }
}