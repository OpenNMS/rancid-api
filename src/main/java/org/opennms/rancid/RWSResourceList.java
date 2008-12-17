package org.opennms.rancid;

import java.util.List;

public interface RWSResourceList {

    String getResource(int i); // will disappear
    
    List<String> getResource();
}
