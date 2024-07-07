package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try(RandomAccessFile file = new RandomAccessFile("teste.txt", "r"))
        {
            FileChannel fileChannel = file.getChannel();

            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9092));

            long fileSize = fileChannel.size();

            // Transfer file contents directly from disk to network
            long bytesTransferred = fileChannel.transferTo(0, fileSize, socketChannel);

            System.out.println("Bytes transferred: " + bytesTransferred);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}