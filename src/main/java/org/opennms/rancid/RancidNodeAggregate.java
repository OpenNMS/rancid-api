package org.opennms.rancid;

import java.util.HashMap;

public class RancidNodeAggregate {
    
    // The list {Group, RancidNode}
    // They all have the same devicename 
    
    private HashMap<String, RancidNode> groupAggregate;
    
    public RancidNodeAggregate(){
        groupAggregate = new HashMap<String, RancidNode>();
    }
    
    public void addRancidAggregate(String group, RancidNode rancidNode) {
        groupAggregate.put(group, rancidNode);
    }
    
    public HashMap<String, RancidNode> getRancidAggregate(){
        return groupAggregate;
    }

}
