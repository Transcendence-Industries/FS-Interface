package engine;

import link.TransceiverCOM;
import link.TransceiverUDP;

public class Connector {

    private String addressUDP;
    private int portUDP;
    private int delayUDP;
    private String portCOM;
    private int baudCOM;
    private int delayCOM;

    private TransceiverUDP transceiverUDP;
    private TransceiverCOM transceiverCOM;

    public Connector() {
        // Nothing
    }

    public void setupUDP() {
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Connecting...");
        try {
            transceiverUDP = new TransceiverUDP(addressUDP, portUDP, delayUDP);
            Manager.getInstance().getGuiConnection().setStatus("UDP", "Ready!");
        } catch (Exception e) {
            Manager.getInstance().getGuiConnection().setStatus("UDP", "Fail!");
            e.printStackTrace();
        }
    }

    public void destructUDP() {
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Disconnecting...");
        if (transceiverUDP != null) {
            transceiverUDP.close();
        }
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Destructed!");
    }

    public void activateUDP() {
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Activating...");
        if (transceiverUDP != null) {
            transceiverUDP.setRunning(true);
        }
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Activated!");
    }

    public void deactivateUDP() {
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Deactivating...");
        if (transceiverUDP != null) {
            transceiverUDP.setRunning(false);
        }
        Manager.getInstance().getGuiConnection().setStatus("UDP", "Deactivated!");
    }

    public void refreshUDP(String address, int port, int delay) {
        this.addressUDP = address;
        this.portUDP = port;
        this.delayUDP = delay;

        destructUDP();
        setupUDP();
    }

    public void setupCOM() {
        Manager.getInstance().getGuiConnection().setStatus("COM", "Connecting...");
        try {
            transceiverCOM = new TransceiverCOM(portCOM, baudCOM, delayCOM);
            Manager.getInstance().getGuiConnection().setStatus("COM", "Ready!");
        } catch (Exception e) {
            Manager.getInstance().getGuiConnection().setStatus("COM", "Fail!");
            e.printStackTrace();
        }
    }

    public void destructCOM() {
        Manager.getInstance().getGuiConnection().setStatus("COM", "Disconnecting...");
        if (transceiverCOM != null) {
            transceiverCOM.close();
        }
        Manager.getInstance().getGuiConnection().setStatus("COM", "Destructed!");
    }

    public void activateCOM() {
        Manager.getInstance().getGuiConnection().setStatus("COM", "Activating...");
        if (transceiverCOM != null) {
            transceiverCOM.setRunning(true);
        }
        Manager.getInstance().getGuiConnection().setStatus("COM", "Activated!");
    }

    public void deactivateCOM() {
        Manager.getInstance().getGuiConnection().setStatus("COM", "Deactivating...");
        if (transceiverCOM != null) {
            transceiverCOM.setRunning(false);
        }
        Manager.getInstance().getGuiConnection().setStatus("COM", "Deactivated!");
    }

    public void refreshCOM(String port, int baudRate, int delay) {
        this.portCOM = port;
        this.baudCOM = baudRate;
        this.delayCOM = delay;

        destructCOM();
        setupCOM();
    }
}
