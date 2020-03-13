package Swing;

import java.util.LinkedList;
import java.util.List;

import java.util.*;
import javax.swing.SwingWorker;

import SinusGeneratorPackage.SinusGenerator;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTime{

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
        SinusGenerator gen = new SinusGenerator(5000);

        public MySwingWorker() {
            fifo.add(0.0f);
        }

        @Override
        protected Boolean doInBackground() throws Exception {

            while (!isCancelled()) {

//                fifo.add(fifo.get(fifo.size() - 1) + Math.random() - .5);
                Float[] arr = gen.getSignalData();
                for (int i = 0; i < 5000; i++) {
                    fifo.add(arr[i]);
                    if (fifo.size() > 5000) {
                        fifo.removeFirst();
                    }
                }
//                fifo.add(gen.getSignalData()[0]);
//                if (fifo.size() > 3600) {
//                    fifo.removeFirst();
//                }

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




