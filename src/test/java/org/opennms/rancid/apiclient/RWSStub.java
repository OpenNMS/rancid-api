package org.opennms.rancid.apiclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import org.opennms.rancid.ConnectionProperties;
import org.opennms.rancid.RancidApiException;
import org.opennms.rancid.RancidNode;
import org.opennms.rancid.RancidNodeAuthentication;
import org.opennms.rancid.RWSClientApi;
import org.opennms.rancid.RWSResourceList;
import org.opennms.rancid.InventoryNode;
import org.opennms.rancid.InventoryElement2;
import org.opennms.rancid.Tuple;

import org.opennms.rancid.RWS_MT_ClientApi;


import org.restlet.Client;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;

public class RWSStub {

    /**
     * @param args
     */
    public static void main(String[] args) {

        boolean threaded = false;
        
        if(!threaded){
        
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("Configuration start");
            
            RWSClientApi.init();
            
            try {
    
            System.out.println("Configuration end");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            
            System.out.println("Factory started");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println(" START RWSClientApi.encode_test(); ");
            //RWSClientApi.encode_test();
            System.out.println(" END RWSClientApi.encode_test(); ");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
    
            String url = new String("http://www.rionero.com/rws-current");
            int test = 8;
            
            if (test == 1) {
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("Group and node provision");
//    
//            
//    
//                
//                RWSClientApi.createRWSGroup(url, "disasterrecovery");
//                
//                RancidNode rn = new RancidNode("disasterrecovery", "GIC_26GENN09");
//                rn.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
//                rn.setComment("Dic2 1759");
//                RWSClientApi.createRWSRancidNode(url,rn);
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
            }
            else if (test == 2){
                    
                
                System.out.println("Factory Loading Resources list");
                
                RWSResourceList ResList1,ResList2,ResList3,ResList4,ResList5,ResList6;
        
        
                ResList1 = RWSClientApi.getRWSResourceServicesList(url);
        
                ResList2 = RWSClientApi.getRWSResourceRAList(url);
        
                ResList3 = RWSClientApi.getRWSResourceGroupsList(url);
                
                ResList4 = RWSClientApi.getRWSResourceDeviceList(url,"demo");
                //ResList5 = RWSF.getRWSResourceDeviceVersionList("demo", "gugli");
                
                ResList6 = RWSClientApi.getRWSResourceLoginPatternList("http://www.rionero.com/rws-current");
        
                System.out.println("ResList1.getResource(0) /rws/: " + ResList1.getResource(0));
                System.out.println("ResList2.getResource(0) /rws/rancid/: " + ResList2.getResource(0));
                System.out.println("ResList3.getResource(0) /rws/rancid/groups/: " + ResList3.getResource(0));
                System.out.println("ResList4.getResource(0) /rws/rancid/groups/demo/: " + ResList4.getResource(0));
        ////        System.out.println("ResList5.getResource(0) /rws/rancid/groups/demo/gugli/: " + ResList5.getResource(0));
                System.out.println("ResList6.getResource(0) /rws/rancid/clogin/: " + ResList6.getResource(0));
        
                List<String> relist1 = ResList1.getResource();
                System.out.println("ResList1.getResource(): " + relist1.get(0));
                List<String> relist2 = ResList2.getResource();
                System.out.println("ResList2.getResource(): " + relist2.get(0));
                List<String> relist3 = ResList3.getResource();
                System.out.println("ResList3.getResource(): " + relist3.get(0));
                List<String> relist4 = ResList4.getResource();
                System.out.println("ResList4.getResource(): " + relist4.get(0));
        ////       List<String> relist5 = ResList5.getResource();
        ////        System.out.println("ResList3.getResource(): " + relist5.get(0));
                List<String> relist6 = ResList6.getResource();
                System.out.println("ResList6.getResource(): " + relist6.get(0));
        
                System.out.println("Factory Loading Lists end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory GetNode start");
        
                RancidNode rn3 = RWSClientApi.getRWSRancidNode("http://www.rionero.com/rws-current","demo", "EDGE-MI0");
                System.out.println("rn3 " + rn3.getDeviceName()  +" "+ rn3.getDeviceType()+" "+rn3.getState()+" "+ rn3.getComment());
                
                System.out.println("Factory GetNode end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory CLOGIN get start");
        
                RancidNodeAuthentication rn5 = RWSClientApi.getRWSAuthNode("http://www.rionero.com/rws-current","EDGE-MI0");
                System.out.println("rn5 EDGE-MI0 " + rn5.getUser() +" "+ rn5.getPassword()+" "+rn5.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN get end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                //tested ok
                System.out.println("Factory Provisioning start");
                
                RancidNode rn = new RancidNode("demo", "gugli_DIC2_1759");
                rn.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn.setComment("Dic2 1759");
                
                
                RWSClientApi.createRWSRancidNode("http://www.rionero.com/rws-current",rn);
                System.out.println("Factory Provisioning end");
        
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory Update start");
                
                RancidNode rn7 = new RancidNode("demo", "gugli_DIC2_1759");
                rn7.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn7.setComment("Dic2 1759");
                rn7.setStateUp(false);
                
                RWSClientApi.updateRWSRancidNode("http://www.rionero.com/rws-current",rn7);
                System.out.println("Factory Update end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory Delete start");
                
                RancidNode rn8 = new RancidNode("demo", "gugli_DIC2_1759");
                rn8.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn8.setComment("Dic2 1759");
                rn8.setStateUp(false);
                
                RWSClientApi.deleteRWSRancidNode("http://www.rionero.com/rws-current",rn8);
                System.out.println("Factory Delete end");
        
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory CLOGIN provisioning start");
        
                RancidNode rn9 = new RancidNode("demo", "gugli__clogin_DIC2_1805");
                rn9.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn9.setComment("Clogin Dic2 1805");
                RWSClientApi.createRWSRancidNode("http://www.rionero.com/rws-current",rn9);
        
                System.out.println("*************************************************************");
        
                RancidNodeAuthentication rn4 = new RancidNodeAuthentication();
                
                rn4.setUser("gugli_DIC2_1706");
                rn4.setPassword("ciccio");
                rn4.setConnectionMethod("telnet");
                RWSClientApi.createOrUpdateRWSAuthNode("http://www.rionero.com/rws-current",rn4);
                System.out.println("rn4 " + rn4.getUser() + rn4.getPassword()+rn4.getConnectionMethodString());
                
                System.out.println("*************************************************************");
        
                
                RancidNodeAuthentication rn15 = RWSClientApi.getRWSAuthNode("http://www.rionero.com/rws-current","gugli_DIC2_1706");
                System.out.println("rn15 gugli_DIC2_1706 " + rn15.getUser() +" "+ rn15.getPassword()+" "+rn15.getConnectionMethodString());
        
                
                System.out.println("Factory CLOGIN provisioning end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                
                System.out.println("Factory CLOGIN update start");
        
                RancidNodeAuthentication rn10 = new RancidNodeAuthentication();
                
                rn10.setUser("gugli_DIC2_1706");
                rn10.setPassword("cicciobello");
                rn10.setConnectionMethod("telnet");
                RWSClientApi.createOrUpdateRWSAuthNode("http://www.rionero.com/rws-current",rn10);
                System.out.println("rn10 " + rn10.getUser() + rn10.getPassword()+rn10.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN update end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                
                System.out.println("Factory CLOGIN delete start");
        
                RancidNodeAuthentication rn11 = new RancidNodeAuthentication();
                
                rn11.setUser("gugli_DIC2_1706");
                rn11.setPassword("cicciobello");
                rn11.setConnectionMethod("ssh");
                RWSClientApi.deleteRWSAuthNode("http://www.rionero.com/rws-current",rn11);
                System.out.println("rn11 " + rn11.getUser() + rn11.getPassword()+rn11.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN delete end");
            }
            if (test == 3) {
                System.out.println("*********************************************************");
                System.out.println("*****Configuration***************************************");
                System.out.println("*********************************************************");
                RWSResourceList ResList7;
                
                ResList7 = RWSClientApi.getRWSResourceConfigList(url,"laboratorio","7206PED.wind.lab");
                
                List<String> configlist = ResList7.getResource();
                
                Iterator iter1 = configlist.iterator();
                
                String tmpg1;
                
                while (iter1.hasNext()) {
                    tmpg1 = (String)iter1.next();
                    System.out.println("Version " + tmpg1);
                }
                System.out.println("*********************************************************");
                System.out.println("*****Inventory*******************************************");
                System.out.println("*********************************************************");
                
                RancidNode rn12 = RWSClientApi.getRWSRancidNode(url,"laboratorio", "7206PED.wind.lab");
                System.out.println("RancidNode deviceName " + rn12.getDeviceName());
                System.out.println("RancidNode deviceType " + rn12.getDeviceType());
                System.out.println("RancidNode stateUp " + rn12.getState());
                System.out.println("RancidNode TotalRevisions " + rn12.getTotalRevisions());
                System.out.println("RancidNode HeadRevision " + rn12.getHeadRevision());
                System.out.println("RancidNode rootConfigurationUrl " + rn12.getRootConfigurationUrl());
                
                
                InventoryNode in1 = RWSClientApi.getRWSInventoryNode(rn12, url, "1.2");
                
                System.out.println("InventoryNode Date " + in1.getCreationDate());
                System.out.println("InventoryNode Url " + in1.getConfigurationUrl());
                
                System.out.println("*********************************************************");
                System.out.println("*****Rancid FULL*****************************************");
                System.out.println("*********************************************************");
                RancidNode rn13 = RWSClientApi.getRWSRancidNodeInventory(url,"laboratorio", "7206PED.wind.lab");
                
                HashMap<String, InventoryNode> hm = rn13.getNodeVersions();
                
                InventoryNode in2 = hm.get("1.1");
                InventoryNode in3 = hm.get("1.2");
                
                System.out.println("InventoryNode Vs " + in2.getVersionId());
                System.out.println("InventoryNode Date " + in2.getCreationDate());
                System.out.println("InventoryNode Url " + in2.getConfigurationUrl());
                System.out.println("InventoryNode Vs " + in3.getVersionId());
                System.out.println("InventoryNode Date " + in3.getCreationDate());
                System.out.println("InventoryNode Url " + in3.getConfigurationUrl());
                
                System.out.println("*********************************************************");
                System.out.println("*****clogin**********************************************");
                System.out.println("*********************************************************");  
                
                RancidNodeAuthentication rn4 = new RancidNodeAuthentication();
              
        
                RancidNodeAuthentication rn5 = RWSClientApi.getRWSAuthNode(url,"7206PED.wind.lab");
                System.out.println("rn5 " + rn5.getUser() + rn5.getPassword()+rn5.getConnectionMethodString());
            }
            
            else if (test == 4){
                
                ConnectionProperties cp = new ConnectionProperties(url, "/rws", 10);
                
                System.out.println("Usage of connection properties");

                System.out.println("Factory Loading Resources list");
                
                RWSResourceList ResList1,ResList2,ResList3,ResList4,ResList5,ResList6;
        
        
                ResList1 = RWSClientApi.getRWSResourceServicesList(cp);
        
                ResList2 = RWSClientApi.getRWSResourceRAList(cp);
        
                ResList3 = RWSClientApi.getRWSResourceGroupsList(cp);
                
                ResList4 = RWSClientApi.getRWSResourceDeviceList(cp,"laboratorio");
                //ResList5 = RWSF.getRWSResourceDeviceVersionList("demo", "gugli");
                
                ResList6 = RWSClientApi.getRWSResourceLoginPatternList(cp);
        
                System.out.println("ResList1.getResource(0) /rws/: " + ResList1.getResource(0));
                System.out.println("ResList2.getResource(0) /rws/rancid/: " + ResList2.getResource(0));
                System.out.println("ResList3.getResource(0) /rws/rancid/groups/: " + ResList3.getResource(0));
                System.out.println("ResList4.getResource(0) /rws/rancid/groups/demo/: " + ResList4.getResource(0));
        ////        System.out.println("ResList5.getResource(0) /rws/rancid/groups/demo/gugli/: " + ResList5.getResource(0));
                System.out.println("ResList6.getResource(0) /rws/rancid/clogin/: " + ResList6.getResource(0));
        
                List<String> relist1 = ResList1.getResource();
                System.out.println("ResList1.getResource(): " + relist1.get(0));
                List<String> relist2 = ResList2.getResource();
                System.out.println("ResList2.getResource(): " + relist2.get(0));
                List<String> relist3 = ResList3.getResource();
                System.out.println("ResList3.getResource(): " + relist3.get(0));
                List<String> relist4 = ResList4.getResource();
                System.out.println("ResList4.getResource(): " + relist4.get(0));
        ////       List<String> relist5 = ResList5.getResource();
        ////        System.out.println("ResList3.getResource(): " + relist5.get(0));
                List<String> relist6 = ResList6.getResource();
                System.out.println("ResList6.getResource(): " + relist6.get(0));
        
                System.out.println("Factory Loading Lists end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory GetNode start");
        
                RancidNode rn3 = RWSClientApi.getRWSRancidNode(cp, "laboratorio", "7206PED.wind.lab");
                System.out.println("rn3 " + rn3.getDeviceName()  +" "+ rn3.getDeviceType()+" "+rn3.getState()+" "+ rn3.getComment());
                
                System.out.println("Factory GetNode end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory CLOGIN get start");
        
                RancidNodeAuthentication rn5 = RWSClientApi.getRWSAuthNode(cp,"7206PED.wind.lab");
                System.out.println("rn5 EDGE-MI0 " + rn5.getUser() +" "+ rn5.getPassword()+" "+rn5.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN get end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                //tested ok
                System.out.println("Factory Provisioning start");
                
                RancidNode rn = new RancidNode("laboratorio", "gugli_DIC2_1759");
                rn.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn.setComment("Dic2 1759");
                
                
                RWSClientApi.createRWSRancidNode(cp,rn);
                System.out.println("Factory Provisioning end");
        
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory Update start");
                
                RancidNode rn7 = new RancidNode("laboratorio", "gugli_DIC2_1759");
                rn7.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn7.setComment("Dic2 1759");
                rn7.setStateUp(false);
                
                RWSClientApi.updateRWSRancidNode(cp,rn7);
                System.out.println("Factory Update end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory Delete start");
                
                RancidNode rn8 = new RancidNode("laboratorio", "gugli_DIC2_1759");
                rn8.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn8.setComment("Dic2 1759");
                rn8.setStateUp(false);
                
                RWSClientApi.deleteRWSRancidNode(cp,rn8);
                System.out.println("Factory Delete end");
        
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory CLOGIN provisioning start");
        
                RancidNode rn9 = new RancidNode("laboratorio", "gugli__clogin_DIC2_1805");
                rn9.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn9.setComment("Clogin Dic2 1805");
                RWSClientApi.createRWSRancidNode(cp,rn9);
        
                System.out.println("*************************************************************");
        
                RancidNodeAuthentication rn4 = new RancidNodeAuthentication();
                
                rn4.setUser("gugli_DIC2_1706");
                rn4.setPassword("ciccio");
                rn4.setConnectionMethod("telnet");
                RWSClientApi.createOrUpdateRWSAuthNode(cp,rn4);
                System.out.println("rn4 " + rn4.getUser() + rn4.getPassword()+rn4.getConnectionMethodString());
                
                System.out.println("*************************************************************");
        
                
                RancidNodeAuthentication rn15 = RWSClientApi.getRWSAuthNode(cp,"gugli_DIC2_1706");
                System.out.println("rn15 gugli_DIC2_1706 " + rn15.getUser() +" "+ rn15.getPassword()+" "+rn15.getConnectionMethodString());
        
                
                System.out.println("Factory CLOGIN provisioning end");
                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                
                System.out.println("Factory CLOGIN update start");
        
                RancidNodeAuthentication rn10 = new RancidNodeAuthentication();
                
                rn10.setUser("gugli_DIC2_1706");
                rn10.setPassword("cicciobello");
                rn10.setConnectionMethod("telnet");
                RWSClientApi.createOrUpdateRWSAuthNode(cp,rn10);
                System.out.println("rn10 " + rn10.getUser() + rn10.getPassword()+rn10.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN update end");
        
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                
                System.out.println("Factory CLOGIN delete start");
        
                RancidNodeAuthentication rn11 = new RancidNodeAuthentication();
                
                rn11.setUser("gugli_DIC2_1706");
                rn11.setPassword("cicciobello");
                rn11.setConnectionMethod("ssh");
                RWSClientApi.deleteRWSAuthNode(cp,rn11);
                System.out.println("rn11 " + rn11.getUser() + rn11.getPassword()+rn11.getConnectionMethodString());
                
                System.out.println("Factory CLOGIN delete end");
            }
            if (test == 5) {
                System.out.println("*********************************************************");
                System.out.println("*****Configuration***************************************");
                System.out.println("*********************************************************");
                RWSResourceList ResList7;
                
                ResList7 = RWSClientApi.getRWSResourceConfigList(url,"laboratorio","7206PED.wind.lab");
                
                List<String> configlist = ResList7.getResource();
                
                Iterator iter1 = configlist.iterator();
                
                String tmpg1;
                
                while (iter1.hasNext()) {
                    tmpg1 = (String)iter1.next();
                    System.out.println("Version " + tmpg1);
                }
                System.out.println("*********************************************************");
                System.out.println("*****Inventory*******************************************");
                System.out.println("*********************************************************");
                
                RancidNode rn12 = RWSClientApi.getRWSRancidNode(url,"laboratorio", "7206PED.wind.lab");
                System.out.println("RancidNode deviceName " + rn12.getDeviceName());
                System.out.println("RancidNode deviceType " + rn12.getDeviceType());
                System.out.println("RancidNode stateUp " + rn12.getState());
                System.out.println("RancidNode TotalRevisions " + rn12.getTotalRevisions());
                System.out.println("RancidNode HeadRevision " + rn12.getHeadRevision());
                System.out.println("RancidNode rootConfigurationUrl " + rn12.getRootConfigurationUrl());
                
                
                InventoryNode in1 = RWSClientApi.getRWSInventoryNode(rn12, url, "1.2");
                
                System.out.println("InventoryNode Date " + in1.getCreationDate());
                System.out.println("InventoryNode Url " + in1.getConfigurationUrl());
                
                System.out.println("*********************************************************");
                System.out.println("*****Rancid FULL*****************************************");
                System.out.println("*********************************************************");
                RancidNode rn13 = RWSClientApi.getRWSRancidNodeInventory(url,"laboratorio", "7206PED.wind.lab");
                
                HashMap<String, InventoryNode> hm = rn13.getNodeVersions();
                
                InventoryNode in2 = hm.get("1.1");
                InventoryNode in3 = hm.get("1.2");
                
                System.out.println("InventoryNode Vs " + in2.getVersionId());
                System.out.println("InventoryNode Date " + in2.getCreationDate());
                System.out.println("InventoryNode Url " + in2.getConfigurationUrl());
                System.out.println("InventoryNode Vs " + in3.getVersionId());
                System.out.println("InventoryNode Date " + in3.getCreationDate());
                System.out.println("InventoryNode Url " + in3.getConfigurationUrl());
                
                System.out.println("*********************************************************");
                System.out.println("*****clogin**********************************************");
                System.out.println("*********************************************************");  
                
                RancidNodeAuthentication rn4 = new RancidNodeAuthentication();
              
        
                RancidNodeAuthentication rn5 = RWSClientApi.getRWSAuthNode(url,"7206PED.wind.lab");
                System.out.println("rn5 " + rn5.getUser() + rn5.getPassword()+rn5.getConnectionMethodString());
            }
            else if (test == 6){
                
                ConnectionProperties cp = new ConnectionProperties(url, "/rws", 60);
                
                System.out.println("Node does not exist test error");

                
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
        
                System.out.println("Factory GetNode start");
        
                RancidNode rn3 = RWSClientApi.getRWSRancidNode(cp, "laboratorio", "7206PED.wind.lab");
                System.out.println("rn3 " + rn3.getDeviceName()  +" "+ rn3.getDeviceType()+" "+rn3.getState()+" "+ rn3.getComment());
                RancidNode rn4 = RWSClientApi.getRWSRancidNodeTLO(cp, "laboratorio", "node4Anto");
                System.out.println("rn4 " + rn4.getDeviceName()  +" "+ rn4.getDeviceType()+" "+rn4.getState()+" "+ rn4.getComment());

                RancidNode rn8 = new RancidNode("laboratorio", "node4Anto");
                //rn8.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn8.setComment("Dic2 1759");
                rn8.setStateUp(false);
                RWSClientApi.createOrUpdateRWSRancidNode(cp, rn8);
                //RWSClientApi.updateRWSRancidNode(cp, rn8);
                System.out.println("andata...");


            }
            else if (test == 7){
                
                ConnectionProperties cp = new ConnectionProperties(url, "/rws", 60);
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                System.out.println("*************************************************************");

                System.out.println("Server busy test");
                
                if (RWSClientApi.isRWSAvailable(cp)) { 
                    System.out.println("free");
                } else {
                    System.out.println("busy");
                }


            }
            else if (test == 8) {
                
//                url = "http://www.rionero.com/auth/rws-devel";
//
//                ConnectionProperties cp = new ConnectionProperties("opennms","MyRancidApi",url, "/rws", 60);
                
                url = "http://www.rionero.com/rws-devel";

                ConnectionProperties cp = new ConnectionProperties(url, "/rws", 60);

                System.out.println("*************************************************************");
                System.out.println("************INVENTORY****************************************");
                System.out.println("*************************************************************");

                System.out.println("*************************************************************");
                System.out.println("*************************************************************");
                RancidNode rn8 = RWSClientApi.getRWSRancidNodeTLO(cp, "laboratorio", "7206ped.wind.lab");
                List<InventoryElement2> ie1 = RWSClientApi.getRWSRancidNodeInventoryElement2(cp, rn8, "1.18");
                Iterator<InventoryElement2> iter1 = ie1.iterator();
                while (iter1.hasNext()){
                    System.out.println("<item>");
                    System.out.println(iter1.next().expand());
                    System.out.println("</item>");
                }

//                System.out.println("*************************************************************");
//                System.out.println("*************************************************************");
//                RancidNode rn9 = RWSClientApi.getRWSRancidNodeTLO(cp, "laboratorio", "7206ped.wind.lab");
//                List<InventoryElement2> ie2 = RWSClientApi.getRWSRancidNodeInventoryElement(cp, rn9, "1.17");
//                Iterator<InventoryElement2> iter2 = ie2.iterator();
//                while (iter2.hasNext()){
//                    System.out.println("--------");
//                    System.out.println(iter2.next().expand());
//                    System.out.println("--------");
//                }

                System.out.println("*************************************************************");
                System.out.println("************INVENTORY****************************************");
                System.out.println("*************************************************************");


            }
            }
            catch(RancidApiException e) {
                System.out.println("STUB exception " + e.getMessage());
            
            }
                
        //        System.out.println("*********************************************************");
        //        System.out.println("*****RancidAggregate*************************************");
        //        System.out.println("*********************************************************");      
        //        
        //        RancidNodeAggregate rna = new RancidNodeAggregate();
        //        rna = RWSClientApi.getRancidNodeAggregate("http://www.rionero.com/rws-current","7206PED.wind.lab");
        //        
        //        HashMap<String, RancidNode> rnamap = rna.getRancidAggregate();
        //        
        //        List<String> groupList = ResList3.getResource();
        //        
        //        Iterator iter = groupList.iterator();
        //        
        //        String tmpg;
        //        
        //        while (iter.hasNext()) {
        //            tmpg = (String)iter.next();
        //            System.out.println("Group " + tmpg);
        //            System.out.println("Rancid Node Aggregate " + tmpg + " " + rnamap.get(tmpg).getDeviceName());
        //        }
        //        
        //        System.out.println("*******************END***************************************");
            
   

        }
        else {
            
            System.out.println("*************************************************************");
            System.out.println("*************************************************************");
            System.out.println("Threaded rancid start");
            
            ConnectionProperties cp = new ConnectionProperties("http://www.rionero.com/rws-current", "/rws", 10);

            
            RWS_MT_ClientApi t1 = new RWS_MT_ClientApi();
            RWS_MT_ClientApi t2 = new RWS_MT_ClientApi();
            
            try {
                t1.init();
                t1.start();
                t2.init();
                t2.start();
                
                RancidNode rn9 = new RancidNode("laboratorio", "gugli_thread");
                rn9.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn9.setComment("threaded");
                RancidNode rn8 = new RancidNode("laboratorio", "gugli_thread_2");
                rn8.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
                rn8.setComment("threaded");
                //RWSClientApi.createRWSRancidNode("http://www.rionero.com/rws-current",rn9);
                
                t1.addNode(rn9, cp);
                t2.addNode(rn8, cp);
            }
            catch(RancidApiException e) {
                System.out.println("eccezione " + e.getMessage());
            
            }
            catch(InterruptedException e) {
                System.out.println(e.getMessage());
            
            }

        }
////     // Outputting the content of a Web page
//        //Client client = new Client(Protocol.HTTP);
////        Client client2 = new Client(Protocol.HTTP);
////        Client client3 = new Client(Protocol.HTTP);
//
//        //try {
//        //    client.get("http://www.restlet.org").getEntity().write(System.out);
//        //} catch (IOException e) {
//            // TODO Auto-generated catch block
//         //   e.printStackTrace();
//        //}
////        Reference rwsUri = new Reference("http://localhost:8182/rws");
////        Reference rwsServicesUri = new Reference("http://localhost:8182/rws/");
////        Reference rwsServiceRancidUri = new Reference("http://localhost:8182/rws/rancid");
////        Reference rwsRancidUri = new Reference("http://localhost:8182/rws/rancid/");
////        Reference rwsRancidDevicesUri = new Reference("http://localhost:8182/rws/rancid/devices");
////        Reference rwsRancidDeviceListUri = new Reference("http://localhost:8182/rws/rancid/devices/");
////        Reference rwsTest0= new Reference("http://www.rionero.com/rws-beta-test/rws");
////        Reference rwsTest1= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid");
////        Reference rwsTest2= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid/devices");
////        Reference rwsTest3= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid/devices/");
//        Reference rwsTest= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid/groups/demo/EDGE-MI0");
//
//
//        try {
////            get(client, rwsTest0);
////            get(client, rwsTest1);
////            get(client, rwsTest2);
////            get(client, rwsTest3);
//
//            //gug
//            System.out.println("client start");
//            client.start();
//            System.out.println("reponse client get");
//            Response response=client.get(rwsTest);
//            
//            RancidNode node = new RancidNode();
//            
////            System.out.println("response.getEntity() ");
////            response.getEntity().write(System.out);
//            System.out.println("response.getEntityAsDom() ");
//            DomRepresentation dmr = response.getEntityAsDom();
//            
//            System.out.println("dmr.getDocument()");
//            dmr.write(System.out);
//            //ok fino a qui
//
//            Document doc = dmr.getDocument();
////            String s1 = doc.getElementsByTagName("*").item(0).getNodeName();
////            String s2 = doc.getElementsByTagName("*").item(1).getNodeName();
//            String s3 = doc.getElementsByTagName("*").item(2).getNodeName();
//            String s4 = doc.getElementsByTagName("*").item(3).getNodeName();
////            String t1 = doc.getElementsByTagName("*").item(0).getTextContent();
////            String t2 = doc.getElementsByTagName("*").item(1).getTextContent();
//            String t3 = doc.getElementsByTagName("*").item(2).getTextContent();
//            String t4 = doc.getElementsByTagName("*").item(3).getTextContent();
//
//
//            System.out.println("s3 s4 " +s3+s4);
//            System.out.println("t3 t4 " +t3+t4);
//            //Ok fino a qui
//            
//            String gug = doc.getElementsByTagName("deviceName").item(0).getTextContent();
//            System.out.println("\ndeviceName " + gug);
//            //OK!!!!
//            
//
//            /*
//            //gug
//            Reference rwsTest2= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid/groups/demo/");
//
//            System.out.println("*****************************************************************");
//            client2.start();
//            System.out.println("reponse client get");
//            Response response2=client2.get(rwsTest2);
//            
////            RancidNode node = new RancidNode();
//            
////            System.out.println("response.getEntity() ");
////            response.getEntity().write(System.out);
//            System.out.println("response.getEntityAsDom() ");
//            DomRepresentation dmr2 = response2.getEntityAsDom();
//            
//            System.out.println("dmr.getDocument()");
//            dmr2.write(System.out);
//            //ok fino a qui
//
//            Document doc2 = dmr2.getDocument();
////            String s1 = doc.getElementsByTagName("*").item(0).getNodeName();
////            String s2 = doc.getElementsByTagName("*").item(1).getNodeName();
//            String s23 = doc2.getElementsByTagName("ResourceURI").item(0).getNodeName();
//            String s24 = doc2.getElementsByTagName("Resource").item(0).getNodeName();
////            String t1 = do2c.getElementsByTagName("*").item(0).getTextContent();
////            String t2 = doc.getElementsByTagName("*").item(1).getTextContent();
//            String t23 = doc2.getElementsByTagName("ResourceURI").item(0).getTextContent();
//            String t24 = doc2.getElementsByTagName("Resource").item(0).getTextContent();
//            String t25 = doc2.getElementsByTagName("Resource").item(1).getTextContent();
//            String t26 = doc2.getElementsByTagName("Resource").item(2).getTextContent();
//
//
//            System.out.println("s3 s4 " +s23+s24);
//            System.out.println("t3 t4 t5 t6" +t23+t24+t25+t26);
//            //Ok fino a qui
//            
//            String gug2 = doc2.getElementsByTagName("deviceName").item(0).getTextContent();
//            System.out.println("\ndeviceName " + gug2);
//            //OK!!!!
//            */
//            //PROVISIONING
//            System.out.println("******PROVISIONING***********************************************************");
//
//            Reference rwsTest3= new Reference("http://www.rionero.com/rws-beta-test/rws/rancid/groups/demo/GUGLIDEVICE");
//            client3.start();
//            System.out.println("client put start");            
//
//            Document doc3 = doc;
//            DomRepresentation drep= new DomRepresentation(MediaType.APPLICATION_ALL);
//            
//            doc3.getElementsByTagName("deviceName").item(0).setTextContent("GUGLIDEVICE");
//
//            drep.setDocument(doc3);
//            drep.write(System.out);
//            Response response3=client3.put(rwsTest3,drep);
//
//            
//            
//            /*
//            System.out.println(representation.get);
//            try {
//                Document d = representation.getDocument();
//                System.out.println(d);
//                if (d == null) return null;
//                node.setDeviceName(d.getElementsByTagName("deviceName").item(0).getNodeValue());
//                node.setDeviceType(d.getElementsByTagName("deviceType").item(0).getNodeValue());
//                if (d.getElementsByTagName("state") ==  null) return null;
//                System.out.println(d.getElementsByTagName("state").item(0).getNodeValue());
//                node.setStateUp(d.getElementsByTagName("state").item(0).getNodeValue().equals("up"));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }*/
//
//            
////            RancidNode node = getNode(client, rwsTest);
////            System.out.println("node.getDeviceName() "+node.getDeviceName());       
//            
//            
//            
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //Reference status = addTest(client, rwsTest,"Antonio", "Russo", "Sano di Mente");
        
        //System.out.println(status);
        
 /*      try {
            get(client, rwsTest3);
            get(client, rwsTest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    public static Reference addTest(Client client, Reference nodeUri, String nome, String cognome, String stato) {
        Form form = new Form();
        form.add("Nome", nome);
        form.add("Cognome", cognome);
        form.add("Stato",stato);
        
/*        try {
            form.encode(CharacterSet.ISO_8859_1);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/
        
        Representation rep = form.getWebRepresentation();

        //rep.setMediaType(MediaType.APPLICATION_XHTML_XML);
        // Launch the request
        Response response = client.put(nodeUri, rep);
        try {
            response.getEntity().write(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (response.getStatus().isSuccess()) {
            return response.getEntity().getIdentifier();
        }

        return new Reference("prova");
        
    }
    
    public static Reference provideNode(Client client, RancidNode node, Reference nodeUri){
        Form form = new Form();
        form.add("deviceName", node.getDeviceName());
        form.add("deviceType", node.getDeviceType());
        form.add("state",node.getState());

        Representation rep = form.getWebRepresentation();

        // Launch the request
        Response response = client.post(nodeUri, rep);
        if (response.getStatus().isSuccess()) {
            return response.getEntity().getIdentifier();
        }

        return null;
    }
    
    public static void get(Client client, Reference reference)
    throws IOException {
        Response response = client.get(reference);
        if (response.getStatus().isSuccess()) {
            if (response.isEntityAvailable()) {
                   response.getEntity().write(System.out);
            }
        }
    }
//??????? che serve????
    public static boolean updateNode(RancidNode node, Client client, Reference nodeUri) {
        // Gathering informations into a Web form.
        Form form = new Form();
        form.add("deviceName", node.getDeviceName());
        form.add("deviceType", node.getDeviceType());
        form.add("state",node.getState());
        form.add("comment",node.getState());
//        form.encode(CharacterSet.ISO_8859_1);
        Representation rep = form.getWebRepresentation();

        // Launch the request
        Response response = client.put(nodeUri, rep);
        return response.getStatus().isSuccess();
    }

    /**
     * Try to delete an item.
     * 
     * @param client
     *                the Restlet HTTP client.
     * @param itemUri
     *                the resource's URI.
     */
    public static boolean deleteItem(Client client, Reference nodeUri) {
        // Launch the request
        Response response = client.delete(nodeUri);
        return response.getStatus().isSuccess();
    }
    
    //public Lig> getNodes(Response response) {
    //    return null;
    //}

    public static RancidNode getNode(Client client, Reference nodeUri)  throws IOException {
        return parse(client.get(nodeUri));
    }

    public static RancidNode parse(Response response) throws IOException {
        RancidNode node = new RancidNode();
        /*if (response.getStatus().isSuccess()) {
            if (response.isEntityAvailable()) {
                   response.getEntity().write(System.out);
            }
        }*/
        
        System.out.println("response.getEntityAsDom() "+response.getEntityAsDom());
           
        DomRepresentation representation = response.getEntityAsDom();
        ;
        System.out.println(representation.getAvailableSize());
        /*
        System.out.println(representation.get);
        try {
            Document d = representation.getDocument();
            System.out.println(d);
            if (d == null) return null;
            node.setDeviceName(d.getElementsByTagName("deviceName").item(0).getNodeValue());
            node.setDeviceType(d.getElementsByTagName("deviceType").item(0).getNodeValue());
            if (d.getElementsByTagName("state") ==  null) return null;
            System.out.println(d.getElementsByTagName("state").item(0).getNodeValue());
            node.setStateUp(d.getElementsByTagName("state").item(0).getNodeValue().equals("up"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
            return node;
    
    }

}
