package engine;

import modules.Feature;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Loads and stores template files used by the GUI list.
 */
public class Config {

    private static final String CONFIG_DIR = "./config";

    private final Ini ini;

    public Config() {
        this.ini = new Ini();
    }

    public String[] listFiles() {
        File dir = new File(CONFIG_DIR);
        String[] files = dir.list();
        return files == null ? new String[0] : files;
    }

    public void loadFile(String name) {
        File file = new File(CONFIG_DIR + "/" + name);

        try {
            ini.load(file);
            Manager.getInstance().setFeatures(loadData());
            System.out.println("Loaded INI file '" + name + "'.");
        } catch (IOException e) {
            System.err.println("INI file '" + name + "' could not be loaded!");
        }
    }

    public void saveFile(String name) {
        storeData(Manager.getInstance().getFeatures());
        File file = new File(CONFIG_DIR + "/" + name);

        try {
            ini.store(file);
            System.out.println("Saved INI file '" + name + "'.");
        } catch (IOException e) {
            System.err.println("INI file '" + name + "' could not be saved!");
        }
    }

    private ArrayList<Feature> loadData() {
        ArrayList<Feature> data = new ArrayList<Feature>();
        Set<String> keys = ini.keySet();

        for (String s : keys) {
            data.add(new Feature(Integer.parseInt(s),
                    ini.get(s, "name"),
                    ini.get(s, "path"),
                    Float.parseFloat(ini.get(s, "value")),
                    Integer.parseInt(ini.get(s, "type")),
                    Integer.parseInt(ini.get(s, "board")),
                    Integer.parseInt(ini.get(s, "pin")),
                    Integer.parseInt(ini.get(s, "code"))));
        }
        return data;
    }

    private void storeData(ArrayList<Feature> data) {
        ini.clear();
        for (Feature feature : data) {
            ini.put(String.valueOf(feature.getId()), "name", feature.getName());
            ini.put(String.valueOf(feature.getId()), "path", feature.getPath());
            ini.put(String.valueOf(feature.getId()), "value", feature.getTargetValue());
            ini.put(String.valueOf(feature.getId()), "type", feature.getType());
            ini.put(String.valueOf(feature.getId()), "board", feature.getBoard());
            ini.put(String.valueOf(feature.getId()), "pin", feature.getPin());
            ini.put(String.valueOf(feature.getId()), "code", feature.getCode());
        }
    }
}
