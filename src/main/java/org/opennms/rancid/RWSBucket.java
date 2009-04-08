package org.opennms.rancid;

import java.util.Date;
import java.util.List;

public class RWSBucket {

	List<BucketItem> m_bucketItem;
	String m_bucketName;
	
	public RWSBucket(String bucketName) {
		m_bucketName = bucketName;
	}
	
	
	public class BucketItem {
		String m_name;
		int m_size;
		Date m_lastModified;
		
		protected BucketItem() {
			
		}
		
		public String getName() {
			return m_name;
		}
		public void setName(String name) {
			m_name = name;
		}
		public int getSize() {
			return m_size;
		}
		public void setSize(int size) {
			m_size = size;
		}
		public Date getLastModified() {
			return m_lastModified;
		}
		public void setLastModified(Date lastModified) {
			m_lastModified = lastModified;
		}
		
	}

	public List<BucketItem> getBucketItem() {
		return m_bucketItem;
	}


	public void setBucketItem(List<BucketItem> bucketItem) {
		m_bucketItem = bucketItem;
	}


	public String getBucketName() {
		return m_bucketName;
	}


	public void setBucketName(String bucketName) {
		m_bucketName = bucketName;
	}
	
	public void setBucket(int i, String itemName, int itemSize, Date itemDate) {
		BucketItem bi = new BucketItem();
		bi.setName(itemName);
		bi.setSize(itemSize);
		bi.setLastModified(itemDate);
		m_bucketItem.add(i, bi);
	}
}
