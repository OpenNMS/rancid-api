package org.opennms.rancid;

import java.util.ArrayList;
import java.util.List;

public class InventoryElement2 implements Expandable {

    private List<Tuple> tupleList;
    private List<InventoryMemory> memoryList;
    private List<InventorySoftware> softwareList;   
    
    InventoryElement2(){
        this.tupleList = new ArrayList<Tuple>();
        this.memoryList = new ArrayList<InventoryMemory>();
        this.softwareList = new ArrayList<InventorySoftware>();


    }
    
    public String expand () {
    	final StringBuffer sb = new StringBuffer();
    	
    	for (final Tuple t : tupleList) {
            sb.append("<").append(t.getName()).append(">").append(t.getDescription()).append("</").append(t.getName()).append(">\n");
    	}
    	for (final InventoryMemory i : memoryList) {
    		sb.append("<Memory>\n");
    		sb.append("<Type>").append(i.getType()).append("</Type>\n");
    		sb.append("<Size>").append(i.getSize()).append("</Size>\n");
    		sb.append("</Memory>\n");
    	}
    	for (final InventorySoftware i : softwareList) {
    		sb.append("<Software>\n");
    		sb.append("<Type>").append(i.getType()).append("</Type>\n");
    		sb.append("<Version>").append(i.getVersion()).append("</Version>\n");
    		sb.append("</Software>\n");
    	}

    	return sb.toString();
    }

    public List<Tuple> getTupleList() {
        return tupleList;
    }

    public void setTupleList(List<Tuple> tupleList) {
        this.tupleList = tupleList;
    }

    public List<InventoryMemory> getMemoryList() {
        return memoryList;
    }

    public void setMemoryList(List<InventoryMemory> memoryList) {
        this.memoryList = memoryList;
    }

    public List<InventorySoftware> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<InventorySoftware> softwareList) {
        this.softwareList = softwareList;
    }
    
    public void addTuple(Tuple tuple){
        this.tupleList.add(tuple);
    }
    public void addMemory(InventoryMemory memory){
        this.memoryList.add(memory);
    }
    public void addSoftware(InventorySoftware software){
        this.softwareList.add(software);
    }


}
