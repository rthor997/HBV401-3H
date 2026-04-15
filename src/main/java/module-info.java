module org.example.hbv4013h {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.hbv4013h to javafx.fxml;
    exports org.example.hbv4013h;
}