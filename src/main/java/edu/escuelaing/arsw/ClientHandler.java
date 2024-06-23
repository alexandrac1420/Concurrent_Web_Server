package edu.escuelaing.arsw;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private static final String BASE_DIRECTORY = Paths.get("src/main/java/edu/escuelaing/arsw/resources/").toAbsolutePath().toString() + "/";
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleRequest(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        StringBuilder request = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            if (inputLine.isEmpty()) {
                break;
            }
            request.append(inputLine).append("\n");
        }

        System.out.println("Received: \n" + request.toString());

        String[] requestLines = request.toString().split("\n");
        if (requestLines.length > 0) {
            String getLine = requestLines[0];
            String[] getParts = getLine.split(" ");
            if (getParts.length >= 2) {
                String filePath = getParts[1].substring(1);

                filePath = BASE_DIRECTORY + filePath;
                File file = new File(filePath);
                if (file.exists() && !file.isDirectory()) {
                    String contentType = Files.probeContentType(file.toPath());
                    byte[] fileContent = Files.readAllBytes(file.toPath());

                    out.print("HTTP/1.1 200 OK\r\n");
                    out.print("Content-Type: " + contentType + "\r\n");
                    out.print("Content-Length: " + fileContent.length + "\r\n");
                    out.print("\r\n");
                    out.flush();

                    OutputStream dataOut = clientSocket.getOutputStream();
                    dataOut.write(fileContent, 0, fileContent.length);
                    dataOut.flush();
                } else {
                    String outputLine = "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta charset=\"UTF-8\">"
                            + "<title>404 Not Found</title>\n" + "</head>"
                            + "<body>"
                            + "File Not Found"
                            + "</body>"
                            + "</html>";
                    out.println(outputLine);
                }
            } else {
                System.err.println("Invalid request line: " + getLine);
            }
        } else {
            System.err.println("Empty request received");
        }

        out.close();
        in.close();
        clientSocket.close();
    }
}
