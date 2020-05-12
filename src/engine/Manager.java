package engine;

import gui.Connection;
import gui.List;
import modules.Feature;

import java.util.ArrayList;
import java.util.Collections;

public class Manager {

    private static Manager instance;

    private ArrayList<Feature> features;
    private final Config config;
    private final Connector connector;
    private final Connection guiConnection;
    private final List guiList;

    public Manager() {
        instance = this;
        this.features = new ArrayList<>();
        this.config = new Config();
        this.connector = new Connector();
        this.guiConnection = new Connection();
        this.guiList = new List();
    }

    public static Manager getInstance() {
        return instance;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }

    public Config getConfig() {
        return config;
    }

    public Connector getConnector() {
        return connector;
    }

    public Connection getGuiConnection() {
        return guiConnection;
    }

    public String[][] getTableData() {
        String[][] data = new String[features.size()][9];

        for (int i = 0; i < features.size(); i++) {
            data[i][0] = String.valueOf(features.get(i).getId());
            data[i][1] = features.get(i).getName();
            data[i][2] = features.get(i).getPath();
            data[i][3] = String.valueOf(features.get(i).getTargetValue());

            if (features.get(i).getType() == 0) {
                data[i][4] = "Output";
            } else if (features.get(i).getType() == 1) {
                data[i][4] = "Input";
            } else {
                data[i][4] = "?";
            }

            if (features.get(i).getBoard() == 0) {
                data[i][5] = "?";
            } else {
                data[i][5] = String.valueOf(features.get(i).getBoard());
            }

            if (features.get(i).getPin() <= 1) {
                data[i][6] = "?";
            } else {
                data[i][6] = String.valueOf(features.get(i).getPin());
            }

            if (features.get(i).getType() == 1 && features.get(i).getCode() == 0) {
                data[i][7] = "";
            } else if (features.get(i).getCode() == 0) {
                data[i][7] = "?";
            } else {
                data[i][7] = String.valueOf(features.get(i).getCode());
            }

            data[i][8] = String.valueOf(features.get(i).isActive());
        }
        return data;
    }

    public int getNextID() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Feature feature : features) {
            ids.add(feature.getId());
        }
        Collections.sort(ids);

        int index = 1;
        while (ids.contains(index)) {
            index++;
        }
        return index;
    }

    public void sortIDs() {
        for (int i = 0; i < features.size(); i++) {
            features.get(i).setId(i + 1);
        }
    }

    public void printFeatures() {
        for (Feature feature : features) {
            System.out.println(feature.getId() +
                    "   |   " + feature.getName() +
                    "   |   " + feature.getPath() +
                    "   |   " + feature.getTargetValue() +
                    "   |   " + feature.getType() +
                    "   |   " + feature.getBoard() +
                    "   |   " + feature.getPin() +
                    "   |   " + feature.getCode() +
                    "   |   " + feature.isActive());
        }
        System.out.println();
    }

    public void exit() {
        connector.deactivateUDP();
        connector.destructUDP();
        connector.deactivateCOM();
        connector.destructCOM();
    }
}
