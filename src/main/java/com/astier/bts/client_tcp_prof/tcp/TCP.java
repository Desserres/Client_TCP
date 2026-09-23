/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astier.bts.client_tcp_prof.tcp;


import com.astier.bts.client_tcp_prof.HelloController;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import OUTILS.exceptions.*;
import javafx.scene.paint.Color;


/**
 * @author Michael
 */
public class TCP extends Thread {
    int port;
    InetAddress serveur;
    Socket socket;
    boolean marche = false;
    boolean connection = false;
    OutputStream outBin;
    InputStream inBin;
    byte[] bufferEntreeBin = new byte[6535];
    HelloController fxmlCont;

    public TCP() {
    }

    public TCP(InetAddress serveur, int port, HelloController fxmlCont) {
        this.port = port;
        this.serveur = serveur;
        this.fxmlCont = fxmlCont;
        System.out.println("@ serveur: " + serveur + " port: " + port);
    }


    public void connection() {
        if (this.isAlive()) {
            return;
        }
        try {
            this.socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(serveur, port);
            this.socket.connect(socketAddress, 2000);
            this.socket.setSoTimeout(5000);
            outBin = socket.getOutputStream();
            inBin = socket.getInputStream();
            marche = true;
            start();

        } catch (IOException e) {
            DiagnosticException.afficheException(e);
        }
    }

    public void deconnection() throws InterruptedException {
        if (this.isAlive()) {
            return;
        }
        try {
            marche = false;
            fxmlCont.voyant.setFill(Color.RED);
            outBin.flush();
            Thread.sleep(1000);
            outBin.close();
            inBin.close();
            socket.close();
        } catch (Exception e) {
            DiagnosticException.afficheException(e);
        }
    }

    public void requette(String laRequette) throws IOException {
        if (marche) {
            outBin.write(laRequette.getBytes(StandardCharsets.UTF_8));
            outBin.flush();
            System.out.println("La requette envoyée: " + laRequette);
        }
    }

    public void run() {
        while (marche) {
            try {
                int nbLusBin = inBin.read(bufferEntreeBin);
                if (nbLusBin == -1) {
                    break;
                }
            } catch (IOException e) {
                DiagnosticException.afficheException(e);
            }

        }
    }


    /*
    Pour déclencher une opération graphique en dehors du thread graphique  utiliser
    javafx.application.Platform.runLater(java.lang.Runnable)
    Cette méthode permet d'éxécuter le code du runnable par le thread graphique de JavaFX.
    */
    protected void updateMessage(String message) {
        Platform.runLater(() -> fxmlCont.TextAreaReponses.appendText("    MESSAGE SERVEUR >  \n      " + message + "\n"));
    }
}