package org.opennms.rancid;

import java.util.HashMap;

public class RancidNode {


    public static String DEVICE_TYPE_ALTEON = "alteon";
    //An Alteon WebOS switches.
    public static String DEVICE_TYPE_BAYNET="baynet";
    //A Bay Networks router.
    public static String DEVICE_TYPE_CISCO_CATOS="cat5";
    //A Cisco catalyst series 5000 and 4000 switches (i.e.: running the catalyst OS, not IOS).
    public static String DEVICE_TYPE_CISCO_IOS="cisco";
    //A Cisco router, PIX, or switch such as the 3500XL or 6000 running IOS (or IOS-like) OS.
    public static String DEVICE_TYPE_CISCO_CSS="css";
    //A Cisco content services switch.
    public static String DEVICE_TYPE_ENTERASYS="enterasys";
    //An enterasys NAS. This is currently an alias for the riverstone device type.
    public static String DEVICE_TYPE_JUNOS="erx";
    //A Juniper E-series edge router.
    public static String DEVICE_TYPE_EXTREME="extreme";
    //An Extreme switch.
    public static String DEVICE_TYPE_EZT3="ezt3";
    //An ADC-Kentrox EZ-T3 mux.
    public static String DEVICE_TYPE_FORCE10="force10";
    //A Force10 router.
    public static String DEVICE_TYPE_FOUNDRY="foundry";
    //A Foundry router, switch, or router-switch. This includes HP Procurve switches that are OEMs of Foundry products, such as the HP9304M.
    public static String DEVICE_TYPE_HITACHI="hitachi";
    //A Hitachi routers.
    public static String DEVICE_TYPE_HPPROCURVE="hp";
    //A HP Procurve switch such as the 2524 or 4108 procurve switches. Also see the foundry type.
    public static String DEVICE_TYPE_JUNIPER="juniper";
    //A Juniper router.
    public static String DEVICE_TYPE_MRTD="mrtd";
    //A host running the (merit) MRTd daemon.
    public static String DEVICE_TYPE_NETSCALAR="netscalar";
    //A Netscalar load balancer.
    public static String DEVICE_TYPE_NETSCREEN="netscreen";
    //A Netscreen firewall.
    public static String DEVICE_TYPE_REDBACK="redback";
    //A Redback router, NAS, etc.
    public static String DEVICE_TYPE_RIVERSTONE="riverstone";
    //A Riverstone NAS or Cabletron (starting with version ~9.0.3) router.
    public static String DEVICE_TYPE_TNT="tnt";
    //A lucent TNT.
    public static String DEVICE_TYPE_ZEBRA="zebra";
    //Zebra routing software.   
    
    // For provisioning only
    private String deviceName;
    private String deviceType;
    private boolean stateUp = true;
    private String comment;
    
    private String group;
    
    // The list pf downloaded versions
    private HashMap<String, InventoryNode> nodeVersions;

    
    public RancidNode() {
        super();
        nodeVersions = new HashMap<String, InventoryNode>();
    }
    public RancidNode(String group, String deviceName) {
        super();
        
        this.group = group;
        this.deviceName = deviceName;

        nodeVersions = new HashMap<String, InventoryNode>();
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public boolean isStateUp() {
        return stateUp;
    }
    public void setStateUp(boolean stateUp) {
        this.stateUp = stateUp;
    }

    public String getState() {
        if (isStateUp()) return "up";
        return "down";
    }
    public HashMap<String, InventoryNode> getNodeVersions() {
        return nodeVersions;
    }
    public void setNodeVersions(HashMap<String, InventoryNode> nodeVersions) {
        this.nodeVersions = nodeVersions;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
        
}
