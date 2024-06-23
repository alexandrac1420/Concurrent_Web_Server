package edu.escuelaing.arsw;

import junit.framework.TestCase;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentWebServerTest extends TestCase {

    private static final String BASE_DIRECTORY = Paths.get("src/main/java/edu/escuelaing/arsw/resources/").toAbsolutePath().toString() + "/";
    private static final int PORT = 35000;
    private static final String HOST = "localhost";
    private static final int NUMBER_OF_CLIENTS = 5;
    private ExecutorService executorService;

    protected void setUp() throws Exception {
        super.setUp();
        executorService = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);

        new Thread(() -> {
            try {
                HttpServer_Exercise6Concurrent.startServer(PORT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        executorService.shutdownNow();
        HttpServer_Exercise6Concurrent.stopServer();
    }

    public void testConcurrentRequests() throws IOException {
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            executorService.submit(() -> {
                try (Socket socket = new Socket(HOST, PORT);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println("GET /index.html HTTP/1.1");
                    out.println("Host: " + HOST);
                    out.println("Connection: close");
                    out.println();

                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append("\n");
                    }

                    System.out.println("Response: \n" + response.toString());

                    assertTrue(response.toString().contains("HTTP/1.1 200 OK"));
                    assertTrue(response.toString().contains("Content-Type: "));
                    assertTrue(response.toString().contains("index.html"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
