package controller.viewController;

import dao.DaoFactory;
import model.other.MainService;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public DashboardController() {

    }

    public static DashboardController getInstance() {
        return dashboardController;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dashboardController = this;
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() {
                initDashBoard();
                return null;
            }
        });
        initDashboardCard();
        initializeAreaChart();
        initializePieChart();
        initializeBarChart();
        initializeStackPane();
        initializeFilter();
        initCarousel();
    }

    private void initDashboardCard(){
        totalAffaire.textProperty().bind(fullTotal.asString() );
        totalTitre.textProperty().bind(totalTitle.asString());
        totalDa.textProperty().bind(acquisition.asString());
        totalPa.textProperty().bind(fullTotal.subtract(acquisition).asString() );
    }

    public void initDashBoard(){
        ArrayList<Integer> dataForDashboard = DaoFactory.getAffaireDao().getDataForDashboard(String.valueOf(LocalDate.now().getYear()));
        ObservableList<XYChart.Series<String, Number>> nbAffaireByDateForChart = DaoFactory.getAffaireDao().getNbAffaireByDateForChart();
        Platform.runLater(() -> series.setAll(nbAffaireByDateForChart));
        if (!dataForDashboard.isEmpty()) {
            fullTotal.set(dataForDashboard.get(0));
            acquisition.set(dataForDashboard.get(1));
            totalTitle.set(dataForDashboard.get(2));
        }
    }

    private void initCarousel() {
        next.setOnAction(event -> showChartPanel());
        prev.setOnAction(event -> showChartPanel());
    }

    private void showChartPanel() {
        ObservableList<Node> nodes = carrouselView.getChildren();
        if (!nodes.get(1).getId().equals("chart2")) {
            showChart(chart2, Direction.TO_RIGHT);
        } else {
            showChart(chart1, Direction.TO_LEFT);
        }
    }

    private void showChart(AnchorPane pane, Direction direction) {
        if (direction.equals(Direction.TO_RIGHT)) {
            new SlideInRight(pane).play();
            pane.toFront();
        } else {
            new SlideInLeft(pane).play();
            pane.toFront();
        }
    }
    private void initializePieChart(){
        PieChart.Data aData = new PieChart.Data("Demande d'acquisition",1);
        aData.pieValueProperty().bind(acquisition);
        PieChart.Data paData = new PieChart.Data("Prescription acquisitive",1);
        paData.pieValueProperty().bind(fullTotal.subtract(acquisition));
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                aData, paData
        );
        piechart.setAnimated(true);
        piechart.setLegendVisible(true);
        piechart.setData(list);
    }
    private void initializeStackPane() {
        chart1.toFront();
    }
    private void initializeBarChart() {
        barChart.setBarGap(5);
        barChart.setAnimated(true);
        barChart.setCursor(Cursor.HAND);
        barChart.setLegendVisible(true);
        barChart.dataProperty().bind(simpleXYChartProperty);
    }
    private void initializeAreaChart() {
        areaChart.setFocusTraversable(true);
        areaChart.setCursor(Cursor.HAND);
        areaChart.setLegendVisible(true);
        areaChart.setTitle(" Repartition entre demande d'acquisition et prescription acquisitive par anneé ");
        areaChart.setCreateSymbols(true);
        areaChart.dataProperty().bind(simpleXYChartProperty);
    }
    private void initializeFilter() {
        ObservableList<String> tmp = FXCollections.observableArrayList("January", "February", "March", "April", "Mai", "June", "July", "September", "November", "December");
        monthFilter.getItems().setAll(tmp);
        ObservableList<String> list = DaoFactory.getAffaireDao().groupeAffaireByDate();
        yearFilter.getItems().setAll(list);
        if (!list.isEmpty())
            yearFilter.setValue(list.get(0));
        monthFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (yearFilter.getValue() != null) {
            } else {
            }
        });
        yearFilter.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (monthFilter.getValue() != null) {
            } else {
            }
        });
    }
    private static DashboardController dashboardController;
    private static SimpleIntegerProperty acquisition = new SimpleIntegerProperty(0) ;
    private static SimpleIntegerProperty fullTotal = new SimpleIntegerProperty(0);
    private static SimpleIntegerProperty totalTitle = new SimpleIntegerProperty(0);
    private static ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
    private static SimpleObjectProperty simpleXYChartProperty = new SimpleObjectProperty(series);
    public AnchorPane getDashboard() {
        return dashboard;
    }
    public void setDashboard(AnchorPane dashboard) {
        this.dashboard = dashboard;
    }
    public StackPane getCarrouselView() {
        return carrouselView;
    }
    public void setCarrouselView(StackPane carrouselView) {
        this.carrouselView = carrouselView;
    }
    public JFXButton getNext() {
        return next;
    }
    public void setNext(JFXButton next) {
        this.next = next;
    }
    public JFXButton getPrev() {
        return prev;
    }
    private enum Direction {TO_RIGHT, TO_LEFT}
    public static ObservableList<XYChart.Series<String, Number>> getSeries() {
        return series;
    }
    public static void setSeries(ObservableList<XYChart.Series<String, Number>> series) { DashboardController.series = series; }


    @FXML private AnchorPane chart2;
    @FXML private AnchorPane chart1;
    @FXML private PieChart piechart;
    @FXML private AnchorPane dashboard;
    @FXML private StackPane carrouselView;
    @FXML private JFXButton next;
    @FXML private JFXButton prev;
    @FXML private Text totalAffaire;
    @FXML private Text totalDa;
    @FXML private Text totalPa;
    @FXML private Text totalTitre;
    @FXML private BarChart<String, Number> barChart;
    @FXML private AreaChart<String, Number> areaChart;
    @FXML private JFXComboBox<String> yearFilter;
    @FXML private JFXComboBox<String> monthFilter;
}
