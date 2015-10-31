package itsmap.SV;

public class ATask {

    //inspired by the Planet in http://stackoverflow.com/questions/32466008/select-items-in-listview-by-id

    String description;
    String time;
    String whoWith;
    boolean selected = false;

    public ATask(String time, String whoWith, String description) {
        super();
        this.time = time;
        this.whoWith = whoWith;
        this.description = description;

    }

    public String getEventDes() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getCoworker(){
        return whoWith;
    }
}