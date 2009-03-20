package org.opennms.rancid;

public class InventorySoftware {

    private String type;
    private String version;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String expand(){
        return "Type ["+type+"] Version["+version+"]";
    }
}
