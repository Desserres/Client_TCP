module com.astier.bts.client_tcp_prof {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.net.http;
    requires jdk.security.jgss;


    opens com.astier.bts.client_tcp_prof to javafx.fxml;
    exports com.astier.bts.client_tcp_prof;
}