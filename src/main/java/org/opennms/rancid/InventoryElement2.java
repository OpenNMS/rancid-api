package org.opennms.rancid;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class InventoryElement2 {

    private List<Tuple> tupleList;
    private List<InventoryMemory> memoryList;
    private List<InventorySoftware> softwareList;   
    
    InventoryElement2(){
        this.tupleList = new ArrayList<Tuple>();
        this.memoryList = new ArrayList<InventoryMemory>();
        this.softwareList = new ArrayList<InventorySoftware>();


    }
    
    public String expand () {
        
        Iterator<Tuple> iter1 = tupleList.iterator();
        Iterator<InventoryMemory> iter2 = memoryList.iterator();
        Iterator<InventorySoftware> iter3 = softwareList.iterator();

        String tot="";
        while (iter1.hasNext()){
            Tuple tmp = iter1.next();
            tot = tot + "<" + tmp.getName() + ">" + tmp.getDescription() + "</" + tmp.getName() + ">\n";
        }
        while (iter2.hasNext()){
            InventoryMemory tmp = iter2.next();
            tot = tot + "<Memory>\n";
            tot = tot + "<Type>" + tmp.getType() + "</Type>\n";
            tot = tot + "<Size>" + tmp.getSize() + "</Size>\n";
        }
        while (iter3.hasNext()){
            InventorySoftware tmp = iter3.next();
            tot = tot + "<Software>\n";
            tot = tot + "<Type>" + tmp.getType() + "</Type>\n";
            tot = tot + "<Version>" + tmp.getVersion() + "</Version>\n";
        }
        return tot;

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
