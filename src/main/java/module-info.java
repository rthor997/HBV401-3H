module org.example.hbv4013h {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;

    opens org.example.hbv4013h to javafx.fxml;
    exports org.example.hbv4013h;
    exports org.example.hbv4013h.ui;
    opens org.example.hbv4013h.ui to javafx.fxml;

    opens software to javafx.fxml;
    exports software;
    exports software.ui;
    opens software.ui to javafx.fxml;
}
