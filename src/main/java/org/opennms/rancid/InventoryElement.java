package org.opennms.rancid;

import java.util.List;
import java.util.ArrayList;

public class InventoryElement implements Expandable {

    private InventoryNode parent;
    
    //New RWS structure March 2 2009
    //Global
    private String chassis;
    private String cpu;
    private String processorId;
    private String dRam;
    private String nvRam;
    private String bootFlash;
    private String pcmciaName;
    private String pcmciaSize;
    
    private List<Tuple> power;
    
    private String os;
    private String romBootstarp;
    private String bootLoader;
    
    private List<Tuple> nwInterface;
    //End of Global section
    
    private List<InventorySlot> slot;
    
    private List<InventoryItem> inventoryItem;
    //End of new structure March 2 2009
    
    
	private String elementName;
    private int elementId;
	private String vendor;
	private String sysOid;
	private String modelType;
	private String serialNumber;
	private String productPartNumber;
	private String hardwareVersion;
	private int ramSize;
	private int nwRamSize;

	public InventoryElement(){
	    super();
	    
	    power = new ArrayList<Tuple>();
	    nwInterface = new ArrayList<Tuple>();
	    slot = new ArrayList<InventorySlot>();
	    inventoryItem = new ArrayList<InventoryItem>();
	    
	}
	
	public String getElementName() {
		return elementName;
	}

	public String getVendor() {
		return vendor;
	}

	public String getSysOid() {
		return sysOid;
	}

	public String getModelType() {
		return modelType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getProductPartNumber() {
		return productPartNumber;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public int getRamSize() {
		return ramSize;
	}

	public int getNwRamSize() {
		return nwRamSize;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void setSysOid(String sysOid) {
		this.sysOid = sysOid;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setProductPartNumber(String productPartNumber) {
		this.productPartNumber = productPartNumber;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public void setRamSize(int ramSize) {
		this.ramSize = ramSize;
	}

	public void setNwRamSize(int nwRamSize) {
		this.nwRamSize = nwRamSize;
	}

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public void setParent(InventoryNode parent) {
        this.parent = parent;
    }

    public InventoryNode getParent() {
        return parent;
    }

    public InventoryElement(InventoryNode parent) {
        this.parent = parent;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }

    public String getDRam() {
        return dRam;
    }

    public void setDRam(String ram) {
        dRam = ram;
    }

    public String getNvRam() {
        return nvRam;
    }

    public void setNvRam(String nvRam) {
        this.nvRam = nvRam;
    }

    public String getBootFlash() {
        return bootFlash;
    }

    public void setBootFlash(String bootFlash) {
        this.bootFlash = bootFlash;
    }

    public String getPcmciaName() {
        return pcmciaName;
    }

    public void setPcmciaName(String pcmciaName) {
        this.pcmciaName = pcmciaName;
    }

    public String getPcmciaSize() {
        return pcmciaSize;
    }

    public void setPcmciaSize(String pcmciaSize) {
        this.pcmciaSize = pcmciaSize;
    }

    public List<Tuple> getPower() {
        return power;
    }

    public void setPower(List<Tuple> power) {
        this.power = power;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRomBootstarp() {
        return romBootstarp;
    }

    public void setRomBootstarp(String romBootstarp) {
        this.romBootstarp = romBootstarp;
    }

    public String getBootLoader() {
        return bootLoader;
    }

    public void setBootLoader(String bootLoader) {
        this.bootLoader = bootLoader;
    }

    public List<Tuple> getNwInterface() {
        return nwInterface;
    }

    public void setNwInterface(List<Tuple> nwInterface) {
        this.nwInterface = nwInterface;
    }

    public List<InventorySlot> getSlot() {
        return slot;
    }

    public void setSlot(List<InventorySlot> slot) {
        this.slot = slot;
    }

    public List<InventoryItem> getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(List<InventoryItem> inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    private String expandTuple(final List<? extends Expandable> expandable) {
    	final StringBuffer sb = new StringBuffer();
    	if (expandable != null) {
	    	for (Expandable e : expandable) {
	    		sb.append(e.expand());
	    	}
    	}
    	return sb.toString();
    }

    public String expand() {

        return
        "chassis [" + chassis + "] "+
        "cpu [" + cpu + "] "+
        "processorId [" + processorId + "] "+
        "dRam [" + dRam + "] "+
        "nvRam [" + nvRam + "] "+
        "bootFlash [" + bootFlash + "] "+
        "bootFlash [" + pcmciaName + "] "+
        "pcmciaSize [" + pcmciaSize + "] "+
        expandTuple(power) +
        "os [" + os + "] "+
        "romBootstarp [" + romBootstarp + "] "+
        "bootLoader [" + bootLoader + "] "+
        expandTuple(nwInterface) + 
        expandTuple(slot) + 
        expandTuple(inventoryItem);

    }
    
}
