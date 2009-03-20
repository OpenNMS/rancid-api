package org.opennms.rancid;

public class InventoryMemory {

    private String type;
    private String size;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    
    public String expand () {
        return "Type ["+type+"] Size["+size+"]";
    }
}
