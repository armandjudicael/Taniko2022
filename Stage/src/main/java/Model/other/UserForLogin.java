package Model.other;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserForLogin {

    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleIntegerProperty userId;
    private SimpleStringProperty userType;

    public UserForLogin(SimpleStringProperty username, SimpleStringProperty password, SimpleIntegerProperty userId, SimpleStringProperty userType) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserType() {
        return userType.get();
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }
}
