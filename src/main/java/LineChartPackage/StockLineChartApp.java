/*
 * Copyright (c) 2008, 2016, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package LineChartPackage;


import SinusGeneratorPackage.SinusGenerator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.NumberAxis.DefaultFormatter;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

//--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
/**
 * A simulated stock line chart.
 *
 * @sampleName Stock Line Chart
 * @preview preview.png
 * @docUrl https://docs.oracle.com/javafx/2/charts/jfxpub-charts.htm Using JavaFX Charts Tutorial
 * @related /Charts/Area/Area Chart
 * @related /Charts/Line/Category Line Chart
 * @related /Charts/Line/Line Chart
 * @related /Charts/Scatter/Scatter Chart
 * @see LineChart
 * @see NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class StockLineChartApp extends Application {

    private LineChart<Number, Number> chart;
    private Series<Number, Number> sinusDataSeries;
    private NumberAxis xAxis;
    private Timeline animation;
    private double hours = 0;
    private double minutes = 0;
    private double timeInHours = 0;
    private double prevY = 10;
    private double y = 10;
    private int amountOfPoints = 5000;

    private SinusGenerator sinusGenerator = new SinusGenerator(amountOfPoints);

    public StockLineChartApp() {
        // 6 minutes data per frame
        final KeyFrame frame =
                new KeyFrame(Duration.millis(1000 / 60),
                        (ActionEvent actionEvent) -> {
                            //Date date = new Date();
                            //System.out.println("before: " + date.getTime());
                            for (int count = 0; count < 1; count++) {
                                //nextTime();
                                updatePlot();
                            }
                        });
        // create timeline to add new data every 60th of second
        animation = new Timeline();
        animation.getKeyFrames().add(frame);
        animation.setCycleCount(Animation.INDEFINITE);
    }

    public Parent createContent() {
        xAxis = new NumberAxis(0, amountOfPoints + 10, amountOfPoints / 10);
        final NumberAxis yAxis = new NumberAxis(-1.5, 1.5, 0.5);
        chart = new LineChart<>(xAxis, yAxis);
        // setup chart
        chart.getStylesheets().add("LineChart/StockLineChart.css");
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setTitle("Sinusoidal graph");
        xAxis.setLabel("Just to see how fast it updates");
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("Amplitude");
        yAxis.setTickLabelFormatter(new DefaultFormatter(yAxis, "", null));
        // add starting data
        sinusDataSeries = new Series<>();
        sinusDataSeries.setName("Sinus Data");

        List<Data<Number, Number>> list = new ArrayList<>();
        for (double m = 0; m < amountOfPoints; m++) {
            list.add(new Data<Number, Number>(m, prevY));
        }
        sinusDataSeries.getData().addAll(list);
        chart.getData().add(sinusDataSeries);
        return chart;
    }

    private void updatePlot() {

        double x = xAxis.getLowerBound() + 1;
        Float[] arr = sinusGenerator.getSignalData();
        final ObservableList<Data<Number, Number>> minuteList =
                sinusDataSeries.getData();
        for (int i = 0; i < amountOfPoints; i++) {
            minuteList.get(i).setYValue(arr[i]);
            minuteList.get(i).setXValue(i + x);
        }
        xAxis.setLowerBound(xAxis.getLowerBound() + 1);
        xAxis.setUpperBound(xAxis.getUpperBound() + 1);
    }

    public void play() {
        animation.play();
    }

    @Override
    public void stop() {
        animation.pause();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
        play();
    }

    /**
     * Java main for when running without JavaFX launcher
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
