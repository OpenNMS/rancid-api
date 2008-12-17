package org.opennms.rancid;

import java.util.Date;
import java.util.List;

public class InventoryNode {

    private RancidNode parent;
    private String versionId;
    private Date   creationDate;
    private Date   expirationDate;

	// Inventory Data variables
    // this node
	private InventoryElement node;
	// node elements
    private List<InventoryElement> nodeElements;

	// this is from software management
	private String softwareVersion;
	private String softwareImageUrl;

	// this are for Configuration management but it is to be used cvsweb integrated in the opennms GUI
	private String configurationUrl;
	private String rootConfigurationUrl;

	public String getVersionId() {
        return versionId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
	
	public List<InventoryElement> getNodeElements() {
		return nodeElements;
	}
	public void setNodeElements(List<InventoryElement> nodeElements) {
		this.nodeElements = nodeElements;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getSoftwareImageUrl() {
		return softwareImageUrl;
	}

	public void setSoftwareImageUrl(String softwareImageUrl) {
		this.softwareImageUrl = softwareImageUrl;
	}

	public String getConfigurationUrl() {
		return configurationUrl;
	}
	public void setConfigurationUrl(String configurationUrl) {
		this.configurationUrl = configurationUrl;
	}
	public String getRootConfigurationUrl() {
		return rootConfigurationUrl;
	}
	public void setRootConfigurationUrl(String rootConfigurationUrl) {
		this.rootConfigurationUrl = rootConfigurationUrl;
	}

    public InventoryElement getNode() {
        return node;
    }

    public void setNode(InventoryElement node) {
        this.node = node;
    }

    public RancidNode getParent() {
        return parent;
    }

    public void setParent(RancidNode parent) {
        this.parent = parent;
    }

    public InventoryNode(RancidNode parent) {
        super();
        this.parent = parent;
    }
	
	
}
