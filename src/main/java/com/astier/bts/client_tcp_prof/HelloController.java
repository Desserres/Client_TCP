package com.astier.bts.client_tcp_prof;

import com.astier.bts.client_tcp_prof.tcp.TCP;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class HelloController implements Initializable {
    public Button button;
    public Button connecter;
    public Button deconnecter;
    public TextField TextFieldIP;
    public TextField TextFieldPort;
    public TextField TextFieldRequette;
    public Circle voyant;
    public TextArea TextAreaReponses;
    static public TCP tcp;
    static boolean enRun = false;
    String adresse, port;
    Socket socket;
    BufferedReader in;
    PrintStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldPort.setText("4000");
        TextFieldIP.setText("127.0.0.1");

        connecter.setOnAction(event -> {
            port = TextFieldPort.getText();
            adresse = TextFieldIP.getText();
            try {
                connecter();
            } catch (UnknownHostException e) {
                enRun = false;
                throw new RuntimeException(e);
            }
        });

        deconnecter.setOnAction(event -> {
            try {
                deconnecter();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        button.setOnAction(event -> {
            try {
                envoyer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //todo
        });
    }


    private void envoyer() throws InterruptedException, IOException {
        String requete = TextFieldRequette.getText();
        out.println(requete);
        System.out.println("La requête : " + requete);
        TextAreaReponses.appendText(TextFieldRequette.getText() + "\n\t- " + in.readLine() + "\n");
    }

    private void deconnecter() throws InterruptedException, IOException {
        enRun = false;
        voyant.setFill(RED);
        //todo
        out.close();
        in.close();
        socket.close();
    }

    private void connecter() throws UnknownHostException {
        //todo OUI!
        if (enRun) {
            return;
        }
        voyant.setFill(GREEN);


        try {
            int port = Integer.parseInt(TextFieldPort.getText());

            socket = new Socket(InetAddress.getLoopbackAddress(), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream(), true);

            try {
                socket.setSoTimeout(5000);
                String message = in.readLine();
                if (message == null) {
                    System.out.println("Le serveur a fermé la connexion.");
                }
            } finally {
            }

        } catch (Exception Ignored) {
            enRun = false;
        }
    }
}