package edu.eci.arep.webserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;

public class HTTPServer {

    public static void main(String[] args) {
        int port = 35000;
        System.out.println("Starting server on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(int port) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            handleClient(serverSocket.accept());
        }
        serverSocket.close();
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Request: " + requestLine);
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;

            String method = parts[0]; // GET o POST
            String path = parts[1];

            if (method.equals("POST") && path.equals("/process")) {
                // Manejar la ruta /process para POST
                StringBuilder payload = new StringBuilder();
                while (in.ready()) {
                    payload.append((char) in.read());
                }
                String data = parsePostData(payload.toString()); // Extraer los datos del cuerpo de la solicitud
                String processedData = processData(data); // Procesar los datos

                // Enviar respuesta al cliente
                String response = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\"message\":\"Data processed successfully\", \"data\":\"" + processedData + "\"}";
                out.write(response.getBytes());
                out.flush();
            } else if (method.equals("GET") && path.startsWith("/search")) {
                // Manejar la ruta /search para GET
                String query = path.contains("?") ? path.split("\\?")[1] : "";
                String filename = parseQuery(query); // Extraer el nombre del archivo
                File file = findFileInResources(filename); // Buscar el archivo en resources

                if (file != null && file.exists()) {
                    sendResponse(out, file); // Enviar el archivo si se encuentra
                } else {
                    sendNotFound(out); // Enviar error 404 si no se encuentra
                }
            } else {
                // Manejar otras rutas
                String filePath = path.equals("/") ? "/index.html" : path;
                File file = new File("src/main/resources" + filePath);
                if (file.exists() && !file.isDirectory()) {
                    sendResponse(out, file);
                } else {
                    sendNotFound(out);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parsePostData(String postData) {
        // Ejemplo de datos POST: "data=example"
        String[] keyValue = postData.split("=");
        if (keyValue.length == 2 && keyValue[0].equals("data")) {
            return keyValue[1]; // Retorna los datos
        }
        return "";
    }

    public static String processData(String data) {
        // Aquí puedes procesar los datos como desees
        // Por ejemplo, convertir los datos a mayúsculas
        return data.toUpperCase();
    }
    private static boolean createFileInResources(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false; // No se puede crear un archivo sin nombre
        }

        File resourcesDir = new File("src/main/resources");
        File newFile = new File(resourcesDir, filename);

        try {
            return newFile.createNewFile(); // Crea el archivo y retorna true si se creó correctamente
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna false si hubo un error al crear el archivo
        }
    }
    public static String parseQuery(String query) {
        if (query.isEmpty()) return "";
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("filename")) {
                return keyValue[1]; // Retorna el nombre del archivo
            }
        }
        return "";
    }

    private static File findFileInResources(String filename) {
        // Buscar en la carpeta resources
        File resourcesDir = new File("src/main/resources");
        File file = searchFileInDirectory(resourcesDir, filename);

        // Si no se encuentra, buscar en la subcarpeta images
        if (file == null) {
            File imagesDir = new File("src/main/resources/images");
            file = searchFileInDirectory(imagesDir, filename);
        }

        return file;
    }

    private static File searchFileInDirectory(File directory, String filename) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().equals(filename)) {
                    return file; // Retorna el archivo si coincide
                }
            }
        }
        return null; // Retorna null si no se encuentra
    }

    private static void sendResponse(OutputStream out, File file) throws IOException {
        String contentType = getContentType(file.getName());
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        PrintWriter pw = new PrintWriter(out);
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type: " + contentType);
        pw.println("Content-Length: " + fileBytes.length);
        pw.println();
        pw.flush();

        out.write(fileBytes);
        out.flush();
    }

    private static void sendNotFound(OutputStream out) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n<h1>404 Not Found</h1>";
        out.write(response.getBytes());
        out.flush();
    }

    public static String getContentType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }

    private String parseRequest(BufferedReader in) throws IOException {
        String inputLine;
        boolean isFirstLine = true;
        String file = "";

        while ((inputLine = in.readLine()) != null) {
            if (isFirstLine) {
                file = inputLine.split(" ")[1];
                isFirstLine = false;
            }
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        return file;
    }

    private String helloRestService(String path, String query) {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + "{\"name\":\"John\", \"age\":30, \"car\":null}";
    }

    private String getHtmlResponse() {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("index.html");

            if (inputStream == null) {
                return "HTTP/1.1 404 Not Found\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<h1>404 Not Found</h1><p>El archivo index.html no fue encontrado.</p>";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            return "HTTP/1.1 500 Internal Server Error\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<h1>500 Internal Server Error</h1><p>Error al leer el archivo index.html.</p>";
        }

        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + contentBuilder.toString();
    }

}
