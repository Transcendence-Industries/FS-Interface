package modules;

public class Feature {

    private int id;
    private String name;
    private String path;
    private float currentValue;
    private float targetValue;
    private int type;
    private int board;
    private int pin;
    private int code;
    private boolean active;

    public Feature(int id, String name, String path, float value, int type, int board, int pin, int code) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.currentValue = -1;
        this.targetValue = value;
        this.type = type;
        this.board = board;
        this.pin = pin;
        this.code = code;
        this.active = false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean checkConfiguration() {
        if (!name.isEmpty() && !name.equals("?") && !path.isEmpty() && !path.equals("?") && board != 0 && pin > 1) {
            if (type == 1 || (type == 0 && code != 0)) {
                return true;
            }
        }
        return false;
    }
}
