package edu.eci.arep.webserver;

import java.net.MalformedURLException;
import java.net.URL;

public class URLParser {
    public static void main(String[] args) throws MalformedURLException {
        URL myURL = new URL("https://ldbn.is.escuelaing.edu.co:8976/index.html?val=90&t=56#events");
        System.out.println("Protocol: " + myURL.getProtocol());
        System.out.println("Authority: " + myURL.getAuthority());
        System.out.println("Host: " + myURL.getHost());
        System.out.println("Port: " + myURL.getPort());
        System.out.println("Path: " + myURL.getPath());
        System.out.println("Query: " + myURL.getQuery());
        System.out.println("File: " + myURL.getFile());
        System.out.println("Ref: " + myURL.getRef());

    }
}
