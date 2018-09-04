package com.sksis.GeoserverBackup;

import java.nio.channels.NonWritableChannelException;
import java.util.Scanner;

public class GBmain {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String url,username ="",password ="",targetfolder ="",interval="0",confirm,backupurl = "";
		confirm="N";
		while (confirm.equals("N")||confirm.equals("n")) {
			Scanner reader=new Scanner(System.in);
			System.out.println("Please input as asked");
			System.out.println("Geosever URL:");
			url=reader.nextLine();
			backupurl=url + "/rest/br/backup";
			System.out.println("username:");
			username=reader.nextLine();
			System.out.println("password:");
			password=reader.nextLine();
			System.out.println("Backup Target folder:");
			targetfolder=reader.nextLine();
			System.out.println("Interval days:");
			interval=reader.nextLine();
			String confirminfo= "Backup URL:" + backupurl + "\n" +
					"username:" + username +
					"\npassword:" + password +
					"\nBackup Target folder:" + targetfolder +
					"\nInterval days:" + interval +
					"\nSettings information is correct? (Y/N)";
			System.out.println(confirminfo);
			confirm=reader.nextLine();
			System.out.println(confirm);
			while (!(confirm.equals("Y")||confirm.equals("y")||confirm.equals("N")||confirm.equals("n"))) {
				System.out.println("Please enter Y or N");
				System.out.println(confirminfo);
				confirm=reader.nextLine();
			}
		}
		while (true) {
			ReqBackup backuper=new ReqBackup();
			int intervalnumber=Integer.parseInt(interval);
			backuper.Setinterval(backupurl, username, password, targetfolder, intervalnumber);
		}
		
		
		
	}

}
