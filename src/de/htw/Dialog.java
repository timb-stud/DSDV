package de.htw;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.LinkedList;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.simulation.Simulation;
  
public class Dialog {

	private LinkedList<DsdvService> dsdvList;
    enum Function {ENDE, PRINTTABLE, REACHABLE, NEXTHOP}
                     
    private String menue;

    public Dialog() {
        StringBuffer sb = new StringBuffer();
        for (Function f : Function.values()) {
            //sb.append(f.ordinal()).append(": ")
            //.append(f).append("; \n");
            sb.append(f.ordinal() + ":" + selected(f))
            .append("\n");
            
        }
        sb.append("--> ");
        menue = sb.toString();
    }
    

    public static String selected(Function f) {
        String ret = "?";
        switch (f) {
           case REACHABLE: ret = "Reachable"; break;
           case NEXTHOP: ret = "getNextHop"; break;
           case PRINTTABLE: ret = "Ausgabe des HashSet"; break;
           case ENDE: ret = "Programm beenden"; break;
        }
        return ret;
    }
    
    public void start() {
        //lager1 = new Lager("Java-Lager", 10); 
        Function function = null;
    
        while (function != Function.ENDE) {
            try {
               function = chooseFunction();
               exexuteChoosenFunction(function);
           } catch (AssertionError e) {
               System.out.println("Ausnahme gefangen: " + e);
           } catch (RuntimeException e) {
               System.out.println("Ausnahme gefangen: " + e);
           } catch (Exception e) {
               System.out.println(e);
               e.printStackTrace();
           }    
        } 
    }    
     
    private Function chooseFunction() throws IOException {
        System.out.print(menue);
        int input = System.in.read() -48;
        if (input >= 0 && input < Function.values().length)
            return Function.values()[input];
        else
            throw new RuntimeException("Falsche Funktion: " + input);
    }
    
    
    private void exexuteChoosenFunction(Function funktion) {
       switch (funktion){
	       case NEXTHOP:{
	    	
/*	       	int i = 0;
	       	for(DsdvService d : dsdvList){
	       		i++;
	       		//System.out.println(i + " - " d.);
	       	}
	       	System.out.print("Bitte Zielknoten Adresse eingeben: ");
	       	int input = System.in.read() -48;
	       	DsdvService[] a = dsdvList.toArray()	       	
	       	a[input].getNextHop(destination);*/
	        break;
	     }
	     case REACHABLE:{
	        	int i = 0;
	        	for(DsdvService d : dsdvList){
	        		i++;
	        		System.out.println("Service Nr.: " + i);
	        		System.out.println(d.getAllReachableDevices());
	        	}
	         break;
	      }
          case PRINTTABLE: {
        	int i = 0;
        	for(DsdvService d : dsdvList){
        		i++;
        		System.out.println("Service Nr.: " + i);
        		System.out.println(d.printTable());
        	}
            break;  
          }
          case ENDE:{
        	System.out.println("Programm wird beendet");
        	System.exit(0);
            break;
          }
          default: {
            System.out.println("Falsche Funktion");
            break;
          }
        }
    }

    public static void main (String[] args) {
        try {
    		JaneSim simulation = new JaneSim();
    		new Thread(simulation).start();
            Dialog ed = new Dialog();
            ed.dsdvList = simulation.getDsdvServiceList();
            ed.start();
        } catch(Throwable e) {
            System.out.println("main: Ausnahme gefangen: " + e);
        }
    }
}
