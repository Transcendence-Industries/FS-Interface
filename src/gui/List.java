package gui;

import engine.Manager;
import modules.Feature;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class List extends JFrame {

    private final JPanel contentPane;
    private final JComboBox<String> comboBoxFile;
    private final JTable table;

    public List() {
        setTitle("FS-Interface List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        addWindowListener(new ExitListener());

        JPanel panelNorth = new JPanel();
        panelNorth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(panelNorth, BorderLayout.NORTH);

        comboBoxFile = new JComboBox<>();
        comboBoxFile.setEditable(true);
        comboBoxFile.setMaximumRowCount(5);
        comboBoxFile.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelNorth.add(comboBoxFile);

        JButton buttonLoad = new JButton("Load");
        buttonLoad.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonLoad.addActionListener(event -> loadConfig());
        panelNorth.add(buttonLoad);

        JButton buttonSave = new JButton("Save");
        buttonSave.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonSave.addActionListener(event -> saveConfig());
        panelNorth.add(buttonSave);

        JSeparator separator1 = new JSeparator();
        panelNorth.add(separator1);

        JButton buttonAdd = new JButton("New");
        buttonAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonAdd.addActionListener(event -> addFeature());
        panelNorth.add(buttonAdd);

        JButton buttonRemove = new JButton("Delete");
        buttonRemove.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonRemove.addActionListener(event -> removeFeature());
        panelNorth.add(buttonRemove);

        JButton buttonSort = new JButton("Sort");
        buttonSort.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonSort.addActionListener(event -> sortFeatures());
        panelNorth.add(buttonSort);

        JSeparator separator2 = new JSeparator();
        panelNorth.add(separator2);

        JButton buttonRefresh = new JButton("Refresh");
        buttonRefresh.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonRefresh.addActionListener(event -> updateTable());
        panelNorth.add(buttonRefresh);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        panelSouth.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(panelSouth, BorderLayout.SOUTH);

        JButton buttonActivateAll = new JButton("Activate all");
        buttonActivateAll.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonActivateAll.addActionListener(event -> editAll(true));
        panelSouth.add(buttonActivateAll);

        JButton buttonDeactivateAll = new JButton("Deactivate all");
        buttonDeactivateAll.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonDeactivateAll.addActionListener(event -> editAll(false));
        panelSouth.add(buttonDeactivateAll);

        JSeparator separator3 = new JSeparator();
        panelSouth.add(separator3);

        JButton buttonActivation = new JButton("Toggle");
        buttonActivation.setFont(new Font("Tahoma", Font.BOLD, 14));
        buttonActivation.addActionListener(event -> editActivation());
        panelSouth.add(buttonActivation);

        updateComboBox();
        updateTable();
        setVisible(true);
    }

    private void updateComboBox() {
        comboBoxFile.setModel(new DefaultComboBoxModel<>(Manager.getInstance().getConfig().listFiles()));
    }

    private void updateTable() {
        TableModel model = new DefaultTableModel(Manager.getInstance().getTableData(),
                new String[]{"ID", "Name", "Path", "Value", "Type (0; 1)", "Board", "Pin", "Code", "Active"});
        model.addTableModelListener(event -> editFeature(event.getColumn(), event.getFirstRow()));
        table.setModel(model);
        // Manager.getInstance().printFeatures();
    }

    private void loadConfig() {
        Manager.getInstance().getConfig().loadFile(String.valueOf(comboBoxFile.getSelectedItem()));
        updateTable();
    }

    private void saveConfig() {
        Manager.getInstance().getConfig().saveFile(String.valueOf(comboBoxFile.getSelectedItem()));
        updateComboBox();
    }

    private void sortFeatures() {
        Manager.getInstance().sortIDs();
        updateTable();
    }

    private void addFeature() {
        Manager.getInstance().getFeatures().add(new Feature(Manager.getInstance().getNextID(), "?", "?", 0, -1, 0, 0, 0));
        updateTable();
    }

    private void editFeature(int column, int row) {
        if (column != 0 && column != 8) {
            Feature feature = Manager.getInstance().getFeatures().get(row);
            String data = String.valueOf(table.getModel().getValueAt(row, column));

            if (column == 1) {
                feature.setName(data);
            } else if (column == 2) {
                feature.setPath(data);
            } else if (column == 3) {
                feature.setTargetValue(Float.parseFloat(data));
            } else if (column == 4) {
                feature.setType(Integer.parseInt(data));
            } else if (column == 5) {
                feature.setBoard(Integer.parseInt(data));
            } else if (column == 6) {
                feature.setPin(Integer.parseInt(data));
            } else if (column == 7) {
                feature.setCode(Integer.parseInt(data));
            }
        }
        updateTable();
    }

    private void removeFeature() {
        if (!table.getSelectionModel().isSelectionEmpty()) {
            Manager.getInstance().getFeatures().remove(table.getSelectedRow());
            updateTable();
        }
    }

    private void editActivation() {
        if (!table.getSelectionModel().isSelectionEmpty()) {
            Feature feature = Manager.getInstance().getFeatures().get(table.getSelectedRow());

            if (feature.isActive() || feature.checkConfiguration()) {
                feature.setActive(!feature.isActive());
                updateTable();
            }
        }
    }

    private void editAll(boolean active) {
        for (Feature feature : Manager.getInstance().getFeatures()) {
            if (!active || feature.checkConfiguration()) {
                feature.setActive(active);
            }
        }
        updateTable();
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
