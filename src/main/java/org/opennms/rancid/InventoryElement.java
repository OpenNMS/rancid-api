package org.opennms.rancid;

public class InventoryElement {

    private InventoryNode parent;
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
    
    
}
