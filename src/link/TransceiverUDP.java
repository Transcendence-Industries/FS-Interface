package link;

import engine.Manager;
import modules.Feature;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TransceiverUDP extends Thread {

    private final String SEND_COMMAND = "DREF";
    private final String RECEIVE_COMMAND = "RREF";
    private final int SEND_LENGTH = 509;
    private final int RECEIVE_LENGTH = 413;
    private final int INTERVAL_START = 1;
    private final int INTERVAL_STOP = 0;
    private final byte ZERO = 0;

    private final InetAddress ADDRESS;
    private final int PORT;
    private final int DELAY;

    private boolean running;
    private DatagramSocket socket;
    private ArrayList<SimListener> simListeners;

    public TransceiverUDP(String address, int port, int delay) throws Exception {
        this.ADDRESS = InetAddress.getByName(address);
        this.PORT = port;
        this.DELAY = delay;
        setup();
        this.start();
        running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void close() {
        running = false;
        this.interrupt();

        for (SimListener listener : simListeners) {
            listener.close();
            simListeners.remove(listener);
        }
        socket.close();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(DELAY);
            } catch (InterruptedException e) {
                System.err.println("Interrupted UDP-Transceiver!");
                // e.printStackTrace();
            }

            if (running) {
                for (Feature feature : Manager.getInstance().getFeatures()) {
                    if (feature.isActive() && feature.getType() == 1 && feature.getCurrentValue() != feature.getTargetValue()) {
                        sendData(feature);
                    }
                }
            }
        }
    }

    private void setup() throws Exception {
        socket = new DatagramSocket();
        sleep(3000);

        simListeners = new ArrayList<SimListener>();
        for (Feature feature : Manager.getInstance().getFeatures()) {
            if (feature.isActive() && feature.getType() == 0) {
                simListeners.add(new SimListener(feature.getPath()));
            }
        }
        System.err.println("Prepared UDP-Transceiver!");
    }

    private void sendData(Feature feature) {
        ByteBuffer buffer = ByteBuffer.allocate(SEND_LENGTH);
        buffer.put(SEND_COMMAND.getBytes(StandardCharsets.UTF_8));
        buffer.put(ZERO);
        buffer.order(ByteOrder.LITTLE_ENDIAN).putFloat(feature.getTargetValue());
        buffer.put(feature.getPath().getBytes(StandardCharsets.UTF_8));
        buffer.put(ZERO);
        byte[] data = buffer.array();

        DatagramPacket packet = new DatagramPacket(data, data.length, ADDRESS, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        feature.setCurrentValue(feature.getTargetValue());
        System.out.println("Sent UDP-Data [Name: " + feature.getName() + " | Value: " + feature.getTargetValue() + "]");
    }

    private void receiveData(String path, float value) {
        for (Feature feature : Manager.getInstance().getFeatures()) {
            if (feature.isActive() && feature.getPath().equals(path) && feature.getTargetValue() != value) {
                feature.setTargetValue(value);
                System.out.println("Received UDP-Data [Name: " + feature.getName() + " | Value: " + value + "]");
            }
        }
    }

    private class SimListener extends Thread {
        private final String PATH;
        private final DatagramSocket socket;

        public SimListener(String path) throws Exception {
            this.PATH = path;

            socket = new DatagramSocket();
            changeInterval(INTERVAL_START);
            System.err.println("Prepared UDP-Transceiver (Subthread)!");

            this.start();
            running = false;
        }

        public void close() {
            this.interrupt();
            changeInterval(INTERVAL_STOP);
            socket.close();
        }

        @Override
        public void run() {
            while (true) {
                byte[] receiveData = new byte[13];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    socket.receive(receivePacket);
                } catch (IOException e) {
                    // System.err.println("Interrupted UDP-Transceiver (Subthread)!");
                    e.printStackTrace();
                }

                ByteBuffer receiveBuffer = ByteBuffer.wrap(receivePacket.getData());
                float value = receiveBuffer.order(ByteOrder.LITTLE_ENDIAN).getFloat(9);
                receiveData(PATH, value);
            }
        }

        private void changeInterval(int interval) {
            ByteBuffer buffer = ByteBuffer.allocate(RECEIVE_LENGTH);
            buffer.put(RECEIVE_COMMAND.getBytes(StandardCharsets.UTF_8));
            buffer.put(ZERO);
            buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(interval);
            buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(11); // TODO: Check which value needs to be inserted here
            buffer.put(PATH.getBytes(StandardCharsets.UTF_8));
            buffer.put(ZERO);
            byte[] data = buffer.array();

            DatagramPacket requestPacket = new DatagramPacket(data, data.length, ADDRESS, PORT);
            try {
                socket.send(requestPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // System.out.println("Sent UDP-Interval-Change [Path: " + feature.getPath() + " | Code: " + feature.getCode() + " | Interval: " + INTERVAL_START + "]");
        }
    }
}
