package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        int port = 9092;
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for a client ...");
                try (SocketChannel clientChannel = serverSocketChannel.accept()) {
                    System.out.println("Client accepted");

                    ByteBuffer buffer = ByteBuffer.allocate(8192);
                    long totalBytesRead = 0;
                    int bytesRead;

                    System.out.println("Received file contents:");

                    int chunkCount = 0;
                    while ((bytesRead = clientChannel.read(buffer)) != -1) {
                        chunkCount++;
                        if (bytesRead == 0) continue;
                        buffer.flip();
                        String content = StandardCharsets.UTF_8.decode(
                                buffer.slice(0, buffer.limit())
                        ).toString();
                        System.out.print(content);
                        System.out.println("\n--- Chunk " + chunkCount + " read: " + bytesRead + " bytes ---");
                        totalBytesRead += bytesRead;
                        buffer.clear();
                    }

                    System.out.println("\nTotal bytes received: " + totalBytesRead);
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }
}