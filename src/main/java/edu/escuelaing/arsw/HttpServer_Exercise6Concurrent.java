package edu.escuelaing.arsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents a concurrent HTTP server that handles client connections using a thread pool.
 */
public class HttpServer_Exercise6Concurrent {
    private static final int PORT = 35000;
    private static final int THREAD_POOL_SIZE = 5;
    private static ServerSocket serverSocket;
    private static ExecutorService executorService;

    /**
     * Starts the HTTP server on the specified port, accepting client connections
     * and processing them using a fixed thread pool.
     * @param port The port number on which the server will listen for incoming connections.
     * @throws IOException If an I/O error occurs while starting the server.
     */
    public static void startServer(int port) throws IOException {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        serverSocket = new ServerSocket(port);

        System.out.println("Ready to receive on port " + port + "...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executorService.submit(new ClientHandler(clientSocket));
        }
    }

    /**
     * Stops the HTTP server and shuts down the thread pool.
     * Closes the server socket and terminates all active client connections.
     */
    public static void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdownNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method that starts the HTTP server on the predefined port.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            startServer(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
