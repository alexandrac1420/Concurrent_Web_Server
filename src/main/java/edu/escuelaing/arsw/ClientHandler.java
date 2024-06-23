package edu.escuelaing.arsw;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles client connections and processes HTTP requests.
 * Implements the Runnable interface to enable concurrent execution.
 */
public class ClientHandler implements Runnable {
    private static final String BASE_DIRECTORY = Paths.get("src/main/java/edu/escuelaing/arsw/resources/")
                                                     .toAbsolutePath().toString() + "/";
    private Socket clientSocket;

    /**
     * Constructs a new ClientHandler instance with the given client socket.
     * @param clientSocket The socket representing the client connection.
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Runs the thread to handle incoming client requests.
     * Reads HTTP requests, processes them, and sends appropriate responses.
     */
    @Override
    public void run() {
        try {
            handleRequest(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP request received from the client.
     * Reads request headers, determines requested file path, and sends response.
     * @param clientSocket The socket representing the client connection.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    private void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        StringBuilder request = new StringBuilder();

        // Read HTTP request line by line
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.isEmpty()) {
                break;
            }
            request.append(inputLine).append("\n");
        }

        System.out.println("Received: \n" + request.toString());

        // Parse the request to retrieve the requested file path
        String[] requestLines = request.toString().split("\n");
        if (requestLines.length > 0) {
            String getLine = requestLines[0];
            String[] getParts = getLine.split(" ");
            if (getParts.length >= 2) {
                String filePath = getParts[1].substring(1);

                filePath = BASE_DIRECTORY + filePath;
                File file = new File(filePath);

                // Check if the requested file exists
                if (file.exists() && !file.isDirectory()) {
                    String contentType = Files.probeContentType(file.toPath());
                    byte[] fileContent = Files.readAllBytes(file.toPath());

                    // Send HTTP response with file content
                    out.print("HTTP/1.1 200 OK\r\n");
                    out.print("Content-Type: " + contentType + "\r\n");
                    out.print("Content-Length: " + fileContent.length + "\r\n");
                    out.print("\r\n");
                    out.flush();

                    OutputStream dataOut = clientSocket.getOutputStream();
                    dataOut.write(fileContent, 0, fileContent.length);
                    dataOut.flush();
                } else {
                    // Send HTTP 404 response for file not found
                    String outputLine = "HTTP/1.1 404 Not Found\r\n"
                                        + "Content-Type: text/html\r\n"
                                        + "\r\n"
                                        + "<!DOCTYPE html>"
                                        + "<html>"
                                        + "<head>"
                                        + "<meta charset=\"UTF-8\">"
                                        + "<title>404 Not Found</title>\n"
                                        + "</head>"
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

        // Close resources
        out.close();
        in.close();
        clientSocket.close();
    }
}
