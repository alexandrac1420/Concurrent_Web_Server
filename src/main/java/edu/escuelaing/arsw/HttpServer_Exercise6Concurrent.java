package edu.escuelaing.arsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer_Exercise6Concurrent {
    private static final int PORT = 35000;
    private static final int THREAD_POOL_SIZE = 5;
    private static ServerSocket serverSocket;
    private static ExecutorService executorService;

    public static void startServer(int port) throws IOException {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        serverSocket = new ServerSocket(port);

        System.out.println("Listo para recibir en el puerto " + port + "...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executorService.submit(new ClientHandler(clientSocket));
        }
    }

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
}
