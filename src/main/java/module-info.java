module org.example.hbv4013h {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens org.example.hbv4013h to javafx.fxml;
    exports org.example.hbv4013h;
    exports org.example.hbv4013h.ui;
    opens org.example.hbv4013h.ui to javafx.fxml;
}