package de.kfru.stockui;

import de.kfru.stockclient.StockPrice;
import de.kfru.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
public class ChartController implements Consumer<StockPrice> {
    @FXML
    public LineChart<String, Double> chart;
    private WebClientStockClient webClientStockClient;
    private ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();

    public ChartController(WebClientStockClient webClientStockClient) {
        this.webClientStockClient = webClientStockClient;
    }

    @FXML
    public void initialize(){
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        data.add(new XYChart.Series<>(seriesData));
        chart.setData(data);

        webClientStockClient.pricesFor("Daimler").subscribe(this);
    }


    @Override
    public void accept(StockPrice stockPrice) {
        Platform.runLater(()->
                       // System.out.println(stockPrice.getPrice())
            seriesData.add(new XYChart.Data<>(String.valueOf(stockPrice.getTime()), stockPrice.getPrice()))
        );

    }
}
