package controller.detailsController;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.pojo.business.other.Avis;
import view.Helper.Other.ControllerUtils;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ChargeInfoController implements Initializable, ControllerUtils {

    @FXML private JFXButton newChargeBtn;
    @FXML private JFXButton delChargeBtn;
    @FXML private TableView<Avis> chargeTableView;
    @FXML private TableColumn<Avis,String> origineColumn;
    @FXML private TableColumn<Avis,String> nomCharge;
    @FXML private TableColumn<Avis,String> numeroCharge;
    @FXML private TableColumn<Avis, Date> dateCharge;
    @FXML private Tab pieceJointeTab;

    private void initTableView(){
        origineColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avis, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Avis, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override public String getValue() {
                        return null;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        nomCharge.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avis, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Avis, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        numeroCharge.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avis, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Avis, String> param) {
                return new ObservableValue<String>() {
                    @Override
                    public void addListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super String> listener) {

                    }

                    @Override
                    public String getValue() {
                        return null;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
        dateCharge.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avis, Date>, ObservableValue<Date>>() {
            @Override public ObservableValue<Date> call(TableColumn.CellDataFeatures<Avis, Date> param) {
                return new ObservableValue<Date>() {
                    @Override
                    public void addListener(ChangeListener<? super Date> listener) {

                    }

                    @Override
                    public void removeListener(ChangeListener<? super Date> listener) {

                    }

                    @Override
                    public Date getValue() {
                        return null;
                    }

                    @Override
                    public void addListener(InvalidationListener listener) {

                    }

                    @Override
                    public void removeListener(InvalidationListener listener) {

                    }
                };
            }
        });
    }
    @Override public void initialize(URL location, ResourceBundle resources) {
         initTableView();
         initButton();
         initBinding();
    }
    @Override public boolean isOk(String result) {
        return false;
    }
    @Override public void initBinding() {

    }
    @Override public void initButton() {

    }
}
