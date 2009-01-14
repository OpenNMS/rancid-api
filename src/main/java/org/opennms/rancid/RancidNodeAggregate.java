package org.opennms.rancid;

import java.util.HashMap;
import java.util.List;

public class RancidNodeAggregate {
    
    // The list {Group, RancidNode}
    // They all have the same devicename 
    
    private HashMap<String, RancidNode> groupAggregate;
    private List<String> groups;
    
    public RancidNodeAggregate(){
        groupAggregate = new HashMap<String, RancidNode>();
    }
    
    public void addRancidAggregate(String group, RancidNode rancidNode) {
        groupAggregate.put(group, rancidNode);
    }
    
    public HashMap<String, RancidNode> getRancidAggregate(){
        return groupAggregate;
    }
    
    public List<String> getGroups() {
        return groups;
    }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
