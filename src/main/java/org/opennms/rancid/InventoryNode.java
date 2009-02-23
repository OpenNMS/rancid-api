package org.opennms.rancid;

import java.util.Date;
import java.util.List;

public class InventoryNode {

    private RancidNode parent;
    private String versionId;
    private Date   creationDate;

	// Inventory Data variables
    // this node
	private InventoryElement node;
	// node elements
    private List<InventoryElement> nodeElements;

	// this is from software management
	private String softwareVersion;
	private String softwareImageUrl;

    //http://www.rionero.com/rws-current/rws/rancid/groups/laboratorio/7206ped.wind.lab/configs/1.15
	// this are for Configuration management but it is to be used cvsweb integrated in the opennms GUI
	private String configurationUrl;

	public String getVersionId() {
        return versionId;
    }
    
    public void setVersionId(String version) {
        versionId = version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
    public void setConfigurationUrl(String configurationUrl) {
        this.configurationUrl = configurationUrl;
    }
	public String getConfigurationUrl() {
		return configurationUrl;
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
