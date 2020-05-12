package gui;

import engine.Manager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Connection extends JFrame {

    private final JPanel contentPane;
    private final JCheckBox checkBoxActiveUDP;
    private final JLabel labelStatusUDP;
    private final JSlider sliderDelayUDP;
    private final JTextField textFieldIP;
    private final JTextField textFieldPortUDP;
    private final JButton buttonRefreshUDP;
    private final JCheckBox checkBoxActiveCOM;
    private final JLabel labelStatusCOM;
    private final JSlider sliderDelayCOM;
    private final JTextField textFieldPortCOM;
    private final JTextField textFieldBaudCOM;
    private final JButton buttonRefreshCOM;

    public Connection() {
        setTitle("FS-Interface Connection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        addWindowListener(new ExitListener());

        JPanel panelNorth = new JPanel();
        contentPane.add(panelNorth, BorderLayout.NORTH);

        Component verticalStrutNorth = Box.createVerticalStrut(25);
        panelNorth.add(verticalStrutNorth);

        JPanel panelWest = new JPanel();
        panelWest.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "UDP",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        contentPane.add(panelWest, BorderLayout.WEST);
        panelWest.setLayout(new GridLayout(5, 2, 0, 25));

        checkBoxActiveUDP = new JCheckBox("Active");
        checkBoxActiveUDP.setFont(new Font("Tahoma", Font.BOLD, 14));
        checkBoxActiveUDP.addActionListener(event -> activate("UDP", checkBoxActiveUDP));
        panelWest.add(checkBoxActiveUDP);

        labelStatusUDP = new JLabel();
        labelStatusUDP.setHorizontalAlignment(SwingConstants.CENTER);
        labelStatusUDP.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelWest.add(labelStatusUDP);

        JLabel labelDelayUDP = new JLabel("Delay: 1000ms");
        labelDelayUDP.setHorizontalAlignment(SwingConstants.CENTER);
        labelDelayUDP.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelWest.add(labelDelayUDP);

        sliderDelayUDP = new JSlider();
        sliderDelayUDP.setSnapToTicks(true);
        sliderDelayUDP.setPaintTicks(true);
        sliderDelayUDP.setPaintLabels(true);
        sliderDelayUDP.setMinorTickSpacing(500);
        sliderDelayUDP.setMinimum(0);
        sliderDelayUDP.setMaximum(5000);
        sliderDelayUDP.setValue(1000);
        sliderDelayUDP.setFont(new Font("Tahoma", Font.PLAIN, 12));
        sliderDelayUDP.addChangeListener(event -> labelDelayUDP.setText("Delay: " + sliderDelayUDP.getValue() + "ms"));
        panelWest.add(sliderDelayUDP);

        JLabel labelIP = new JLabel("IP:");
        labelIP.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelWest.add(labelIP);

        textFieldIP = new JTextField("127.0.0.1");
        textFieldIP.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelWest.add(textFieldIP);

        JLabel labelPortUDP = new JLabel("Port:");
        labelPortUDP.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelWest.add(labelPortUDP);

        textFieldPortUDP = new JTextField("49000");
        textFieldPortUDP.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelWest.add(textFieldPortUDP);

        buttonRefreshUDP = new JButton("Connect");
        buttonRefreshUDP.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonRefreshUDP.addActionListener(event -> refresh("UDP"));
        panelWest.add(buttonRefreshUDP);

        JPanel panelEast = new JPanel();
        panelEast.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "COM",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        contentPane.add(panelEast, BorderLayout.EAST);
        panelEast.setLayout(new GridLayout(5, 2, 0, 25));

        checkBoxActiveCOM = new JCheckBox("Active");
        checkBoxActiveCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        checkBoxActiveCOM.addActionListener(event -> activate("COM", checkBoxActiveCOM));
        panelEast.add(checkBoxActiveCOM);

        labelStatusCOM = new JLabel();
        labelStatusCOM.setHorizontalAlignment(SwingConstants.CENTER);
        labelStatusCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelEast.add(labelStatusCOM);

        JLabel labelDelayCOM = new JLabel("Delay: 1000ms");
        labelDelayCOM.setHorizontalAlignment(SwingConstants.CENTER);
        labelDelayCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelEast.add(labelDelayCOM);

        sliderDelayCOM = new JSlider();
        sliderDelayCOM.setSnapToTicks(true);
        sliderDelayCOM.setPaintTicks(true);
        sliderDelayCOM.setPaintLabels(true);
        sliderDelayCOM.setMinorTickSpacing(500);
        sliderDelayCOM.setMinimum(0);
        sliderDelayCOM.setMaximum(5000);
        sliderDelayCOM.setValue(1000);
        sliderDelayCOM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        sliderDelayCOM.addChangeListener(event -> labelDelayCOM.setText("Delay: " + sliderDelayCOM.getValue() + "ms"));
        panelEast.add(sliderDelayCOM);

        JLabel labelPortCOM = new JLabel("Port:");
        labelPortCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelEast.add(labelPortCOM);

        textFieldPortCOM = new JTextField("COM3");
        textFieldPortCOM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelEast.add(textFieldPortCOM);

        JLabel labelBaudCOM = new JLabel("Baud-Rate:");
        labelBaudCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelEast.add(labelBaudCOM);

        textFieldBaudCOM = new JTextField("115200");
        textFieldBaudCOM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelEast.add(textFieldBaudCOM);

        buttonRefreshCOM = new JButton("Connect");
        buttonRefreshCOM.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonRefreshCOM.addActionListener(event -> refresh("COM"));
        panelEast.add(buttonRefreshCOM);

        JPanel panelSouth = new JPanel();
        contentPane.add(panelSouth, BorderLayout.SOUTH);

        Component verticalStrutSouth = Box.createVerticalStrut(25);
        panelSouth.add(verticalStrutSouth);

        Component rigidAreaCenter = Box.createRigidArea(new Dimension(50, 50));
        contentPane.add(rigidAreaCenter, BorderLayout.CENTER);

        lockActivation("UDP", true);
        lockActivation("COM", true);
        setStatus("UDP", "Destructed!");
        setStatus("COM", "Destructed!");
        setVisible(true);
    }

    public void setStatus(String type, String status) {
        if (type.equals("UDP")) {
            labelStatusUDP.setText("Status: " + status);
        } else if (type.equals("COM")) {
            labelStatusCOM.setText("Status: " + status);
        }
    }

    public void lockActivation(String type, boolean b) {
        if (type.equals("UDP")) {
            checkBoxActiveUDP.setEnabled(!b);
        } else if (type.equals("COM")) {
            checkBoxActiveCOM.setEnabled(!b);
        }
    }

    private void lockSettings(String type, boolean b) {
        if (type.equals("UDP")) {
            sliderDelayUDP.setEnabled(!b);
            textFieldIP.setEnabled(!b);
            textFieldPortUDP.setEnabled(!b);
            buttonRefreshUDP.setEnabled(!b);
        } else if (type.equals("COM")) {
            sliderDelayCOM.setEnabled(!b);
            textFieldPortCOM.setEnabled(!b);
            textFieldBaudCOM.setEnabled(!b);
            buttonRefreshCOM.setEnabled(!b);
        }
    }

    private void activate(String type, JCheckBox checkBox) {
        if (checkBox.isSelected()) {
            lockSettings(type, true);

            if (type.equals("UDP")) {
                Manager.getInstance().getConnector().activateUDP();
            } else if (type.equals("COM")) {
                Manager.getInstance().getConnector().activateCOM();
            }
        } else {
            if (type.equals("UDP")) {
                Manager.getInstance().getConnector().deactivateUDP();
            } else if (type.equals("COM")) {
                Manager.getInstance().getConnector().deactivateCOM();
            }
            lockSettings(type, false);
        }
    }

    private void refresh(String type) {
        lockActivation(type, true);
        lockSettings(type, true);

        if (type.equals("UDP")) {
            Manager.getInstance().getConnector().refreshUDP(textFieldIP.getText(), Integer.parseInt(textFieldPortUDP.getText()), sliderDelayUDP.getValue());
        } else if (type.equals("COM")) {
            Manager.getInstance().getConnector().refreshCOM(textFieldPortCOM.getText(), Integer.parseInt(textFieldBaudCOM.getText()), sliderDelayCOM.getValue());
        }

        lockSettings(type, false);
        lockActivation(type, false);
    }

    private static class ExitListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            Manager.getInstance().exit();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }
}
