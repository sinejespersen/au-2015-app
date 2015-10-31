package itsmap.SV;

class AnEvent {

    String name;
    String time;
    String location;
    boolean selected = false;

    //inspired by the Planet in http://stackoverflow.com/questions/32466008/select-items-in-listview-by-id

    public AnEvent(String name, String time, String location) {
        super();
        this.name = name;
        this.time = time;
        this.location = location;
    }

    public String getEventName() {
        return name;
    }

    public String getTime() {
        return time;
    }
    public String getLocation(){
        return location;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
