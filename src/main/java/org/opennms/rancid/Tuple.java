package org.opennms.rancid;

public class Tuple implements Expandable {
    private String name;
    private String description;
    
    public Tuple(String name, String description){
        this.name = name;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String expand(){
        return "name [" + name + "] "+
               "description [" + description + "] ";
    }
}
