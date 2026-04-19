module org.example.hbv4013h {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;


    opens software to javafx.fxml;
    exports software;
    exports software.ui;
    opens software.ui to javafx.fxml;
}