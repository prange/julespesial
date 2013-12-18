package js;

import java.util.ArrayList;

public class Sekk1 {
    private String name;
    private ArrayList<Gave1> gaver = new ArrayList<>(  );

    public ArrayList<Gave1> getGaver() {
        return gaver;
    }

    public void setGaver(ArrayList<Gave1> gaver) {
        this.gaver = gaver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
