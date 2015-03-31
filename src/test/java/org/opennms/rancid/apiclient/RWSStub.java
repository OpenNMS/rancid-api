package org.opennms.rancid.apiclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.rancid.ConnectionProperties;
import org.opennms.rancid.InventoryElement2;
import org.opennms.rancid.InventoryNode;
import org.opennms.rancid.RWSClientApi;
import org.opennms.rancid.RWSResourceList;
import org.opennms.rancid.RWS_MT_ClientApi;
import org.opennms.rancid.RancidApiException;
import org.opennms.rancid.RancidNode;
import org.opennms.rancid.RancidNodeAuthentication;

import static org.junit.Assert.assertTrue;
public class RWSStub {

    @Before
    public void setUp() {
        System.out.println("RWS Client Api initialization");
        
        RWSClientApi.init();

        System.out.println("RWS Client Api Initialized");
    }
    
    @Test
    @Ignore
    public void testRWSOnRioneroDotComRWSResourceList() {
        System.out.println("Factory Loading Resources list");

        String url = "http://www.rionero.com/rws-current";
        ConnectionProperties cp = new ConnectionProperties(url, "/rws", 60);

        RWSResourceList resList1 = null,resList2 = null,resList3 = null,resList4 = null;

        try {
            RWSClientApi.isRWSAvailable(cp);
        } catch (RancidApiException e) {
            return;
        }

        try {
            resList1 = RWSClientApi.getRWSResourceServicesList(cp);
            resList2 = RWSClientApi.getRWSResourceRAList(cp);
            resList3 = RWSClientApi.getRWSResourceGroupsList(cp);
            resList4 = RWSClientApi.getRWSResourceLoginPatternList(cp);
            
        } catch (RancidApiException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        System.out.println("ResList1.getResource(0) /rws/: " + resList1.getResource(0));
        System.out.println("ResList2.getResource(0) /rws/rancid/: " + resList2.getResource(0));
        System.out.println("ResList3.getResource(0) /rws/rancid/groups/: " + resList3.getResource(0));
        System.out.println("ResList6.getResource(0) /rws/rancid/clogin/: " + resList4.getResource(0));

        List<String> relist1 = resList1.getResource();
        System.out.println("ResList1.getResource(): " + relist1.get(0));
        List<String> relist2 = resList2.getResource();
        System.out.println("ResList2.getResource(): " + relist2.get(0));
        List<String> relist3 = resList3.getResource();
        System.out.println("ResList3.getResource(): " + relist3.get(0));
        List<String> relist4 = resList4.getResource();
        System.out.println("ResList4.getResource(): " + relist4.get(0));
    }

    @Test
    @Ignore
    public void testRWSOnRioneroDotComRancidGroupLaboratorio() {

        String url = "http://www.rionero.com/rws-current";
        ConnectionProperties cp = new ConnectionProperties(url, "/rws", 10);

        RWSResourceList resList4 = null, resList7 = null;

        RancidNode rn10 = new RancidNode("laboratorio", "gugli_DIC2_1759");
        rn10.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
        rn10.setComment("Dic2 1759");

        RancidNode rn11 = new RancidNode("laboratorio", "gugli_DIC2_1759");
        rn11.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
        rn11.setComment("Dic2 1759");
        rn11.setStateUp(false);        
        
        RancidNode rn12 = new RancidNode("laboratorio", "gugli_DIC2_1759");
        rn12.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
        rn12.setComment("Dic2 1759");
        rn12.setStateUp(false);

        RancidNode rn2 = new RancidNode("laboratorio", "gugli__clogin_DIC2_1805");
        rn2.setDeviceType(RancidNode.DEVICE_TYPE_BAYNET);
        rn2.setComment("Clogin Dic2 1805");

        RancidNodeAuthentication rna1 = new RancidNodeAuthentication();
        rna1.setUser("gugli_DIC2_1706");
        rna1.setPassword("ciccio");
        rna1.setConnectionMethod("telnet");
        RancidNodeAuthentication rna2 = null;
        
        RancidNode c7206 = null;
        RancidNode c7206full = null;
        InventoryNode c7206inv = null;
        try {
            if (!RWSClientApi.isRWSAvailable(cp))
                return;
            resList4 = RWSClientApi.getRWSResourceDeviceList(url,"laboratorio");
            resList7 = RWSClientApi.getRWSResourceConfigList(url,"laboratorio","7206PED.wind.lab");
            c7206 = RWSClientApi.getRWSRancidNode(url,"laboratorio", "7206PED.wind.lab");            
            rna2 = RWSClientApi.getRWSAuthNode(url,"7206PED.wind.lab");
            c7206inv = RWSClientApi.getRWSInventoryNode(c7206, url, "1.2");
            c7206full = RWSClientApi.getRWSRancidNodeInventory(url,"laboratorio", "7206PED.wind.lab");

            RWSClientApi.createRWSRancidNode("http://www.rionero.com/rws-current",rn10);
            RWSClientApi.createRWSRancidNode("http://www.rionero.com/rws-current",rn2);

            RWSClientApi.updateRWSRancidNode("http://www.rionero.com/rws-current",rn11);
            
            
            RWSClientApi.createOrUpdateRWSAuthNode("http://www.rionero.com/rws-current",rna1);
            
            RWSClientApi.deleteRWSRancidNode("http://www.rionero.com/rws-current",rn12);
            RWSClientApi.deleteRWSRancidNode("http://www.rionero.com/rws-current",rn2);
            

        } catch (RancidApiException e) {
            e.printStackTrace();
            assertTrue(false);
        }


        List<String> relist4 = resList4.getResource();
        System.out.println("ResList4.getResource(): " + relist4.get(0));

        List<String> relist7 = resList7.getResource();
        System.out.println("ResList7.getResource(): " + relist7.get(0));
        Iterator<String> iter1 = relist7.iterator();
                
        while (iter1.hasNext()) {
            System.out.println("Version " + iter1.next());
        }

        System.out.println("7206PED.wind.lab deviceName " + c7206.getDeviceName());
        System.out.println("7206PED.wind.lab deviceType " + c7206.getDeviceType());
        System.out.println("7206PED.wind.lab stateUp " + c7206.getState());
        System.out.println("7206PED.wind.lab TotalRevisions " + c7206.getTotalRevisions());
        System.out.println("7206PED.wind.lab HeadRevision " + c7206.getHeadRevision());
        System.out.println("7206PED.wind.lab rootConfigurationUrl " + c7206.getRootConfigurationUrl());

        System.out.println("7206PED.wind.lab InventoryNode Date " + c7206inv.getCreationDate());
        System.out.println("7206PED.wind.lab InventoryNode Url " + c7206inv.getConfigurationUrl());

        HashMap<String, InventoryNode> hm = c7206full.getNodeVersions();
        
        InventoryNode in2 = hm.get("1.1");
        InventoryNode in3 = hm.get("1.2");
        
        System.out.println("InventoryNode Vs " + in2.getVersionId());
        System.out.println("InventoryNode Date " + in2.getCreationDate());
        System.out.println("InventoryNode Url " + in2.getConfigurationUrl());
        System.out.println("InventoryNode Vs " + in3.getVersionId());
        System.out.println("InventoryNode Date " + in3.getCreationDate());
        System.out.println("InventoryNode Url " + in3.getConfigurationUrl());

        System.out.println("7206PED.wind.lab: authentication: " + rna2.getUser() +" "+ rna2.getPassword()+" "+rna2.getConnectionMethodString());

        try {
            RancidNode rn3 = RWSClientApi.getRWSRancidNode(cp, "laboratorio", "7206PED.wind.lab");
            System.out.println("rn3 " + rn3.getDeviceName()  +" "+ rn3.getDeviceType()+" "+rn3.getState()+" "+ rn3.getComment());
            RancidNode rn4 = RWSClientApi.getRWSRancidNodeTLO(cp, "laboratorio", "node4Anto");
            System.out.println("rn4 " + rn4.getDeviceName()  +" "+ rn4.getDeviceType()+" "+rn4.getState()+" "+ rn4.getComment());

            RancidNode rn8 = new RancidNode("laboratorio", "node4Anto");
            rn8.setComment("Dic2 1759");
            rn8.setStateUp(false);
            RWSClientApi.createOrUpdateRWSRancidNode(cp, rn8);
            List<InventoryElement2> ie1 = RWSClientApi.getRWSRancidNodeInventoryElement2(cp, rn8, "1.18");
            Iterator<InventoryElement2> iter2 = ie1.iterator();
            while (iter2.hasNext()){
                System.out.println("<item>");
                System.out.println(iter2.next().expand());
                System.out.println("</item>");
            }
        } catch (RancidApiException e) {
            e.printStackTrace();
            assertTrue(false);
        }
   
    }
            
    @Test
    public void testLocalRwsServer() {
        String url="http://localhost";
        ConnectionProperties cp = new ConnectionProperties(url, "/rws", 60);

        try {
        RancidNode mikrotik = RWSClientApi.getRWSRancidNode(cp, "ars", "mikrotik");
        
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        System.out.println("deviceName: " + mikrotik.getDeviceName());
        System.out.println("deviceType: " + mikrotik.getDeviceType());
        System.out.println("deviceState: " + mikrotik.getState());
        System.out.println("comment: " + mikrotik.getComment());
        System.out.println("headRevision: " + mikrotik.getHeadRevision());
        System.out.println("totalRevisions: " + mikrotik.getTotalRevisions());
        System.out.println("rootConfiguratinUrl: " + mikrotik.getRootConfigurationUrl());
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        

        RancidNode prova = new RancidNode("ars", "prova");
        prova.setDeviceType(RancidNode.DEVICE_TYPE_JUNIPER);
        prova.setStateUp(true);
        prova.setComment("aggiunto da antonio per fare un test");

        RancidNode provaA = new RancidNode("ars", "provaA");
        provaA.setDeviceType("adtran");
        provaA.setStateUp(true);
        provaA.setComment("aggiunto da antonio per fare un altro test");

        RWSClientApi.createOrUpdateRWSRancidNode(cp, prova);
        RWSClientApi.createRWSRancidNode(cp, provaA);
        
        
        RancidNode provarws = RWSClientApi.getRWSRancidNodeTLO(cp, "ars", "prova");
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        System.out.println("deviceName: " + provarws.getDeviceName());
        System.out.println("deviceType: " + provarws.getDeviceType());
        System.out.println("deviceState: " + provarws.getState());
        System.out.println("comment: " + provarws.getComment());
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");

        provarws = RWSClientApi.getRWSRancidNodeTLO(cp, "ars", "provaA");
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        System.out.println("deviceName: " + provarws.getDeviceName());
        System.out.println("deviceType: " + provarws.getDeviceType());
        System.out.println("deviceState: " + provarws.getState());
        System.out.println("comment: " + provarws.getComment());
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");

        prova.setStateUp(false);
        prova.setComment("changed per provare update prova");

        provaA.setStateUp(false);
        provaA.setComment("changed per provare update provaA");

        RWSClientApi.createOrUpdateRWSRancidNode(cp, prova);
        RWSClientApi.updateRWSRancidNode(cp, provaA);
        
        provarws = RWSClientApi.getRWSRancidNodeTLO(cp, "ars", "prova");
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        System.out.println("deviceName: " + provarws.getDeviceName());
        System.out.println("deviceType: " + provarws.getDeviceType());
        System.out.println("deviceState: " + provarws.getState());
        System.out.println("comment: " + provarws.getComment());
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        
        provarws = RWSClientApi.getRWSRancidNodeTLO(cp, "ars", "provaA");
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        System.out.println("deviceName: " + provarws.getDeviceName());
        System.out.println("deviceType: " + provarws.getDeviceType());
        System.out.println("deviceState: " + provarws.getState());
        System.out.println("comment: " + provarws.getComment());
        System.out.println("*************************************************************");
        System.out.println("************NODE****************************************");
        System.out.println("*************************************************************");
        RWSClientApi.deleteRWSRancidNode(cp, prova);
        RWSClientApi.deleteRWSRancidNode(cp, provaA);

        } catch (RancidApiException e) {
            e.printStackTrace();
            assertTrue(false);
        }
   
    }
                
    @Test
    @Ignore
    public void ThreadedTest() {

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
}
