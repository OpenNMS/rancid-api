package org.opennms.rancid;

public class InventorySlot {
    
    private String slotName;
    private String slotType;
    private String hvers;
    private String part;
    private String sn;
    
    
    
    public InventorySlot(String slotName, String slotType, String hvers,
            String part, String sn) {
        super();
        this.slotName = slotName;
        this.slotType = slotType;
        this.hvers = hvers;
        this.part = part;
        this.sn = sn;
    }
    public String getSlotName() {
        return slotName;
    }
    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }
    public String getSlotType() {
        return slotType;
    }
    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }
    public String getHvers() {
        return hvers;
    }
    public void setHvers(String hvers) {
        this.hvers = hvers;
    }
    public String getPart() {
        return part;
    }
    public void setPart(String part) {
        this.part = part;
    }
    public String getSn() {
        return sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    
    public String expand(){
        return 
        "slotName [" + slotName + "] "+
        "slotType [" + slotType + "] "+
        "hvers [" + hvers + "] "+
        "part [" + part + "] "+
        "sn [" + sn + "] ";

    }
}
