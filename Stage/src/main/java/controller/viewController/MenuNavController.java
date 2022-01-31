package controller.viewController;
import Main.InitializeApp;
import com.jfoenix.controls.JFXButton;
import dao.DaoFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import model.Enum.Origin;
import model.other.MainService;
import model.pojo.business.other.Titre;
import view.Dialog.Other.About;
import view.Dialog.SecurityDialog.AdminSecurity;
import view.Helper.Other.ExitButton;
import view.Helper.Other.IndisponibleFunctionality;
import view.Helper.Other.MenuButtonAnimation;
import view.Model.ViewObject.FolderForView;
import view.Model.ViewObject.UserForView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MenuNavController implements Initializable {
    public static MenuNavController getInstance() {
        return menuNavController;
    }
    @Override public void initialize(URL location, ResourceBundle resources) {
        menuNavController = this;
        initButton();
    }
    private void initButton(){
        initButtonAction();
        initButtonAnimation();
    }
    private void initButtonAction(){
        // AGENT
        agentBtn.setOnAction(event -> {
            if (!isToFront("userview"))
                AdminSecurity.show(Origin.SHOW_USER_PANEL);
        });
        //  DASHBOARD
        acceuilBtn.setOnAction(event -> showDashboardView(event));
        // AFFAIRE
        affairBtn.setOnAction(event -> showAffaireView(event));
        // TITRE
        titleBtn.setOnAction(event -> showTitleView(event));
        // ABOUT
        aboutBtn.setOnAction(event -> About.getInstance().show());
        // HELP
        new IndisponibleFunctionality(aideBtn);
        // STATISTIQUE
        new IndisponibleFunctionality(statBtn);
        // EXIT
        new ExitButton(exitBtn);
        // THEME
        new IndisponibleFunctionality(themeBtn);
        // SETTING
        new IndisponibleFunctionality(settingBtn);
    }
    private void initButtonAnimation(){
        // AGENT
        new MenuButtonAnimation(agentBtn);
        // THEME
        new MenuButtonAnimation(themeBtn);
        // SETTING
        new MenuButtonAnimation(settingBtn);
        // DASHBOARD
        new MenuButtonAnimation(acceuilBtn);
        // EXIT
        new MenuButtonAnimation(exitBtn);
        // AFFAIRE
        new MenuButtonAnimation(affairBtn);
        // STATISTIQUE
        new MenuButtonAnimation(statBtn);
        // AIDE
        new MenuButtonAnimation(aideBtn);
        // TITRE
        new MenuButtonAnimation(titleBtn);
        // ABOUT
        new MenuButtonAnimation(aboutBtn);
    }
    public void showDashboardView(ActionEvent actionEvent) {
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DashboardController.getInstance().initDashBoard();
                return null;
            }
        });
        MainController.getInstance().getDashboardView().toFront();
    }
    public void showAffaireView(ActionEvent actionEvent) {
        if(!isToFront("affaireView")){
            MainController.getInstance().getAffaireView().toFront();
            MainService.getInstance().launch(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (InitializeApp.getAffaires().isEmpty()) {
                        ObservableList<FolderForView> observableList = DaoFactory.getDossierDao().getAffaireBy(String.valueOf(LocalDate.now().getYear()));
                        InitializeApp.setAffaires(observableList);
                        Platform.runLater(() -> {
                            FolderViewController.getInstance().getTableView().getItems().addAll(observableList);
                        });
                    }
                    return null;
                }
            });
        }
    }

    private Boolean isToFront(String paneName){
        ObservableList<Node> children = MainController.getInstance().getMainStackPane().getChildren();
        int size = children.size();
        Node node = children.get(size-1);
        return node.getId().equals(paneName);
    }

    public void showTitleView(ActionEvent actionEvent){
        if (!isToFront("titleView")){
            MainController.getInstance().getTitleView().toFront();
            MainService.getInstance().launch(new Task<Void>() {
                @Override protected Void call() throws Exception {
                    if (InitializeApp.getTitres().isEmpty()) {
                        int year = LocalDate.now().getYear();
                        ObservableList<Titre> alltitle = DaoFactory.getTitreDao().getTitleBy(String.valueOf(year));
                        InitializeApp.setTitres(alltitle);
                        Platform.runLater(() -> {
                            TitleViewController.getInstance().getTableView().getItems().setAll(alltitle);
                        });
                    }
                    return null;
                }
            });
        }
    }

    public void showUserView(){
        MainController.getInstance().getUserview().toFront();
        MainService.getInstance().launch(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (InitializeApp.getUsers().isEmpty()) {
                    ObservableList<UserForView> allUsers = DaoFactory.getUserDao().getAllUsers();
                    Platform.runLater(() -> {
                        UserViewController.getInstance().getUserTilePane().getChildren().setAll(allUsers);
                    });
                    InitializeApp.setUsers(allUsers);
                }
                return null;
            }
        });
    }

    private static MenuNavController menuNavController;
    // JFXBUTTON
    @FXML private JFXButton acceuilBtn;
    @FXML private JFXButton affairBtn;
    @FXML private JFXButton titleBtn;
    @FXML private JFXButton themeBtn;
    @FXML private JFXButton agentBtn;
    @FXML private JFXButton settingBtn;
    @FXML private JFXButton exitBtn;
    @FXML private JFXButton aboutBtn;
    @FXML private JFXButton aideBtn;
    @FXML private JFXButton statBtn;
    @FXML private AnchorPane menuNav;
}
