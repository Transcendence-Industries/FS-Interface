package link;

import engine.Manager;
import modules.Feature;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.DigitalReadChangeEvent;
import org.zu.ardulink.event.DigitalReadChangeListener;

import java.util.ArrayList;

public class TransceiverCOM extends Thread {

    private final String PORT;
    private final int BAUD_RATE;
    private final int DELAY;

    private boolean running;
    private Link link;
    private ArrayList<ControllerListener> controllerListeners;

    public TransceiverCOM(String port, int baudRate, int delay) throws Exception {
        this.PORT = port;
        this.BAUD_RATE = baudRate;
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

        for (ControllerListener listener : controllerListeners) {
            link.removeDigitalReadChangeListener(listener);
            controllerListeners.remove(listener);
        }
        link.disconnect();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(DELAY);
            } catch (InterruptedException e) {
                System.err.println("Interrupted COM-Transceiver!");
                // e.printStackTrace();
            }

            if (running) {
                for (Feature feature : Manager.getInstance().getFeatures()) {
                    if (feature.isActive() && feature.getType() == 0 && feature.getCurrentValue() != feature.getTargetValue()) {
                        sendData(feature);
                    }
                }
            }
        }
    }

    private void setup() throws Exception {
        link = Link.getDefaultInstance();
        link.connect(PORT, BAUD_RATE);
        sleep(3000);

        controllerListeners = new ArrayList<ControllerListener>();
        for (Feature feature : Manager.getInstance().getFeatures()) {
            if (feature.isActive() && feature.getType() == 1) {
                ControllerListener listener = new ControllerListener(feature.getPin());
                controllerListeners.add(listener);
                link.addDigitalReadChangeListener(listener);
            }
        }
        System.err.println("Prepared COM-Transceiver!");
    }

    private void sendData(Feature feature) {
        link.sendPowerPinSwitch(feature.getPin(), (int) feature.getTargetValue());
        feature.setCurrentValue(feature.getTargetValue());
        System.out.println("Sent COM-Data [Name: " + feature.getName() + " | Value: " + feature.getTargetValue() + "]");
    }

    private void receiveData(int pin, int value) {
        for (Feature feature : Manager.getInstance().getFeatures()) {
            if (feature.isActive() && feature.getPin() == pin && feature.getTargetValue() != value) {
                feature.setTargetValue(value);
                System.out.println("Received COM-Data [Name: " + feature.getName() + " | Value: " + value + "]");
            }
        }
    }

    private class ControllerListener implements DigitalReadChangeListener {
        private final int PIN;

        public ControllerListener(int pin) {
            this.PIN = pin;
        }

        @Override
        public void stateChanged(DigitalReadChangeEvent digitalReadChangeEvent) {
            receiveData(PIN, digitalReadChangeEvent.getValue());
        }

        @Override
        public int getPinListening() {
            return PIN;
        }
    }
}
