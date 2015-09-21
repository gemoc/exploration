/**
 * Copyright (c) 2012-2016 GEMOC consortium.
 * 
 * http://www.gemoc.org
 * 
 * Contributors:
 *   Papa Issa Diallo - ENSTA Bretagne [papa_issa.diallo@ensta-bretagne.fr]
 *   
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * $Id$
 */

package org.gemoc.mocc.clocksystem.core.actions;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ClockSystemFileGen {
	
	ProcessBuilder pb;
	String tmpVMpath;
	String vmtype;
	OSDectect od;
	private Shell activeShell;
	
	public ClockSystemFileGen() throws URISyntaxException, IOException
	{
		tmpVMpath = System.getProperty("java.io.tmpdir");
		od = new OSDectect();
		if(od.curos.equals("Windows"))
		{
			vmtype = "win_vm";
		}
		else if (od.curos.equals("mac"))
		{
			vmtype = "mac_vm";
		}
		else if (od.curos.equals("linux"))
		{
			vmtype = "linux_vm";
		}
		System.out.println("====================================");
		System.out.println("Jar Name= " + ClockSystemFileGen.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		System.out.println("OS = " + od.curos +"\n"+"VM ="+  vmtype);
		System.out.println("====================================");
	}
	
	public void GenerateExploration(String filetab[], String dirtab[])
	{
		
		
		String currpath = tmpVMpath.replace("\\", "/");	
		String currpath_win = tmpVMpath.replace("\\", "\\\\");
		
		String image_pathwin = currpath_win + vmtype + "\\\\" + "ClockSystem.image";
		String image_pathother = currpath + "/" + vmtype + "/" + "ClockSystem.image";
		
		String runwinvm = currpath_win + vmtype + "\\\\" + "Pharo.exe";
		String runlinvm = currpath + "/" + vmtype + "/" + "pharo";
		String runmacvm = currpath + "/" + vmtype + "/" + "Contents" + "/" + "MacOS" + "/" + "Pharo";

		String locwin= "stream := '"+filetab[0]+"' asFileReference readStream. ";
		String locoth= "stream := '"+filetab[1]+"' asFileReference readStream. ";
		String arg1= "sys := Compiler evaluate: stream contents. ";
		String arg2= " ClockSystem4GeMoC ";
		String arg3= "explore: sys resultIn: ";
		
		String toEvaluateWin = locwin + arg1 + arg2 + arg3 + "'"+dirtab[0]+"'"+".";
		String toEvaluateOth = locoth + arg1 + arg2 + arg3 + "'"+dirtab[1]+"'"+".";
		
		System.out.println("linux path = " + runlinvm);
		System.out.println("output path = " + dirtab[1]);
		
		if(od.curos.equals("Windows"))
		{
			pb = new ProcessBuilder(runwinvm,"--headless",image_pathwin,"eval",toEvaluateWin);
		}
		else if (od.curos.equals("mac"))
		{
			pb = new ProcessBuilder(runmacvm,"-headless",image_pathother,"eval",toEvaluateOth);
		}
		else if (od.curos.equals("linux"))
		{
			pb = new ProcessBuilder(runlinvm,"-headless",image_pathother,"eval",toEvaluateOth);
		}
		runFileGen();
		//pb.directory(new File("C:\\OBPFiacreTests\\GenOutput"));
	}
	
	public void unzipClockSystemfromJar() throws URISyntaxException, IOException {
		
		URI uriJar = ClockSystemFileGen.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		File aFile = new File(uriJar);
		
		try {

				if(aFile.isDirectory())
				{
					System.err.println("This is NOT a Jar File");
				}	
				else
				{
					JarFile jar = new JarFile(aFile);
					extractJar(jar, "ClockSystemFileGen.class",tmpVMpath);				
				}
			} 
		catch (final FileNotFoundException e) {
		e.printStackTrace();
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
			}
		});
		
		} catch (final UnsupportedEncodingException e) {
		e.printStackTrace();
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
			}
		});
		}
	}

	private void extractJar(JarFile jar, String tmpdir, String temp_dir_path) {
		
		Enumeration<JarEntry> entries =  jar.entries();
		ArrayList<File> list = new ArrayList<File>();
		// Approximately takes 3 to 5 Minutes
		final String Message = "Calling <ClockSystem Exhaustive Exploration> might take some minutes the first time \n"
				+ "This is due to the Extraction of the ClockSystem Virtual Machine into the folder : \n"
				+"<"+vmtype+">" + " in " + temp_dir_path+"\n"
				+ "This step is only done once";
		while(entries.hasMoreElements())
	    {
		   String destdir = temp_dir_path;     //destination directory
	        
	       JarEntry entry = entries.nextElement();
	         
	       File fl = new File(destdir, entry.getName());
		    
	       if(!fl.exists())
	        {
	    	   
	    	   if( fl.toString().endsWith(vmtype)==true)
	    	   {
	    	       PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	    			    public void run() {
	    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    			    	MessageDialog.openInformation(activeShell, "ClockSystem File Generation and VM Extraction", Message);
	    				}
	    			});
	    		   fl.getParentFile().mkdirs();
	    		   if(list.contains(fl.getParentFile())==false)
	    			   list.add(fl.getParentFile());
	    		   fl = new File(destdir, entry.getName());
	    		   if(list.contains(fl)==false)
	    			   list.add(fl);
	    		   if(entry.isDirectory())
	   	        	{
	    			   continue;
	   	        	}
	   	       
	    		   InputStream jarStream;
	    		   FileOutputStream oStream;
	    		   try {
	    			   	jarStream = jar.getInputStream(entry);
	    			   	oStream = new java.io.FileOutputStream(fl);
	   	        
	    			   	while(jarStream.available()>0)
	    			   	{
	    			   		oStream.write(jarStream.read());
	    			   	}
	    			   	oStream.close();
	    			   	jarStream.close();  
	    		   } catch (final IOException e) {
					e.printStackTrace();
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	    			    public void run() {
	    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    			    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
	    				}
	    			});
					
				}
	    	   }
	    	   
	    	  if((fl.toString().endsWith(vmtype)==false) && (fl.getParentFile().toString().endsWith(vmtype)==true))
	    	   {
	   	       	  fl.getParentFile().mkdirs();
	   	       	  if(list.contains(fl.getParentFile())==false)
	   	       		  list.add(fl.getParentFile());
	   	       	  fl = new File(destdir, entry.getName());
	   	       	  if(list.contains(fl)==false)
	   	       		  list.add(fl);
	  
	    		   if(entry.isDirectory())
	   	        	{
	    			   //fl.setExecutable(true);
		    		   continue;
	   	        	}
	    		   InputStream jarStream;
	    		   FileOutputStream oStream;
				try {
	    		   jarStream = jar.getInputStream(entry);
	    		   oStream = new java.io.FileOutputStream(fl);
	   	       
	    		   while(jarStream.available()>0)
	   	        	{
	    			   oStream.write(jarStream.read());
	   	        	}
	    		   	oStream.close();
	   	        	jarStream.close();  
				} catch (final IOException e) {
					e.printStackTrace();
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	    			    public void run() {
	    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    			    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
	    				}
	    			});
				}
	    	   }
	    	 
	    	  if(vmtype.equals("linux_vm")==true)
	    	  {
	    		  if(( fl.toString().endsWith(vmtype)==false) && (fl.getParentFile().toString().contains(vmtype)==true)&& (fl.getParentFile().toString().endsWith("bin")==true))
	    		  {	  
		   	       	  fl.getParentFile().mkdirs(); 
		   	       	  if(list.contains(fl.getParentFile())==false)
		   	       		  list.add(fl.getParentFile());
		   	       	  fl = new File(destdir, entry.getName());
		   	       	  if(list.contains(fl)==false)
		   	       		  list.add(fl);
	    		  
		    		   if(entry.isDirectory())
		   	        	{
		    			   continue;
		   	        	}
		    		   InputStream jarStream;
		    		   FileOutputStream oStream;
					try {
		    		   jarStream = jar.getInputStream(entry);
		    		   oStream = new java.io.FileOutputStream(fl);
		   	       
		    		   while(jarStream.available()>0)
		   	        	{
		    			   oStream.write(jarStream.read());
		   	        	}
		    		   	oStream.close();
		   	        	jarStream.close();  
					} catch (final IOException e) {
						e.printStackTrace();
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    			    public void run() {
		    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    			    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
		    				}
		    			});
					}
	    		  }
	    		  if(( fl.toString().endsWith(vmtype)==false) && (fl.getParentFile().toString().contains(vmtype)==true)&& (fl.getParentFile().toString().endsWith("icons")==true))
	    		  {
		   	       	  fl.getParentFile().mkdirs();
		   	       	  if(list.contains(fl.getParentFile())==false)
		   	       		  list.add(fl.getParentFile());
		   	       	  fl = new File(destdir, entry.getName());
		   	       	  if(list.contains(fl)==false)
		   	       		  list.add(fl); 
		    		   
		   	       	  if(entry.isDirectory())
		   	        	{
		    			   continue;
		   	        	}
		    		   InputStream jarStream;
		    		   FileOutputStream oStream;
					try {
		    		   jarStream = jar.getInputStream(entry);
		    		   oStream = new java.io.FileOutputStream(fl);
		   	       
		    		   while(jarStream.available()>0)
		   	        	{
		    			   oStream.write(jarStream.read());
		   	        	}
		    		   	oStream.close();
		   	        	jarStream.close();  
					} catch (final IOException e) {
						e.printStackTrace();
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    			    public void run() {
		    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    			    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
		    				}
		    			});
					}
	    		  }
	    		  if(( fl.toString().endsWith(vmtype)==false) && (fl.getParentFile().toString().contains(vmtype)==true)&& (fl.getParentFile().toString().endsWith("shared")==true))
	    		  {
	    			  fl.getParentFile().mkdirs();
	    			  if(list.contains(fl.getParentFile())==false)
		    			   list.add(fl.getParentFile());
		    		   fl = new File(destdir, entry.getName());
		    		   if(list.contains(fl)==false)
		    			   list.add(fl);

		    		   if(entry.isDirectory())
		   	        	{
		    			   continue;
		   	        	}
		    		   InputStream jarStream;
		    		   FileOutputStream oStream;
					try {
		    		   jarStream = jar.getInputStream(entry);
		    		   oStream = new java.io.FileOutputStream(fl);
		   	       
		    		   while(jarStream.available()>0)
		   	        	{
		    			   oStream.write(jarStream.read());
		   	        	}
		    		   	oStream.close();
		   	        	jarStream.close();  
					} catch (final IOException e) {
						e.printStackTrace();
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    			    public void run() {
		    			    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    			    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
		    				}
		    			});
					}
	    		  }
	    	  }
	    	  if(vmtype.equals("mac_vm")==true)
	    	  {
	    		  //TODO parse repo
	    	  }
	        }
	    }
		for(File l:list)
		{
			l.setReadable(true, false);
			l.setWritable(true, false);
			if(l.isDirectory()==false)
			{
				if((l.toString().endsWith("pharo")) ||(l.toString().endsWith("Pharo")) || (l.toString().endsWith("Pharo.exe"))) 
				{
					l.setExecutable(true, false);
				}
			}
		}
	}

	public void runFileGen()
	{
		try 
		{
			//JOptionPane.showMessageDialog(null, pb.command(), "Command", JOptionPane.INFORMATION_MESSAGE);
			Process p = pb.start();
			LogStreamReader lsr = new LogStreamReader(p.getInputStream());
			Thread thread = new Thread(lsr, "LogStreamReader");
			thread.start();
		} 
		catch (final IOException e) 
		{
        e.printStackTrace();
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		    public void run() {
		    	activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		    	MessageDialog.openError(activeShell, "ERROR MESSAGE", e.toString());
			}
		});
		}
	}
}