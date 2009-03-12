package org.opennms.rancid;

public class InventoryItem {
    
    private String name;
    private String description;
    private String pid;
    private String vid;
    private String sn;
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
    public String getPid() {
        return pid;
    }
    public InventoryItem(String name, String description, String pid,
            String vid, String sn) {
        super();
        this.name = name;
        this.description = description;
        this.pid = pid;
        this.vid = vid;
        this.sn = sn;
    }
    public InventoryItem(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getSn() {
        return sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    
    public String expand(){
        return
        "name [" + name + "] "+
        "description [" + description + "] "+
        "pid [" + pid + "] "+
        "vid [" + vid + "] "+
        "sn [" + sn + "] ";

    }
 }
