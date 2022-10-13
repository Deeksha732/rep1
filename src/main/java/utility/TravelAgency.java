package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.DBHandler;
import model.Package;

public class TravelAgency {
	public boolean validate(String packageId) throws InvalidPackageIdException {
		Pattern p = Pattern.compile("^[0-9a-zA-Z]{3}[/]{1}[0-9a-zA-Z]{3}$");
		Matcher m = p.matcher(packageId);
		boolean b = m.matches();
		if(b==true) {
			return b;
		}
		else {
			throw new InvalidPackageIdException("Invalid Package Id");
		}
		
	}
	
	public List<Package> generatePackageCost(String filePath) throws InvalidPackageIdException, FileNotFoundException{
		List<Package> plist = new ArrayList();
		double packageCost;
		Scanner sc = new Scanner(new File(filePath));
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] s = line.split(",");
			String packageId = s[0];
			String sourcePlace = s[1];
			String destPlace = s[2];
			double basicFare = Double.parseDouble(s[3]);
			int noOfDays = Integer.parseInt(s[4]);

						
			if(validate(packageId)) {
				Package p = new Package();
				p.setPackageId(packageId);
				p.setSourcePlace(sourcePlace);
				p.setDestinationPlace(destPlace);
				p.setBasicFare(basicFare);
				p.setNoOfDays(noOfDays);
				p.calculatePackageCost();
	
				plist.add(p);
				
			}
			
			}
			return plist;
		}
	
	public List<Package> findPackagesWithMinimumNumberOfDays(){
		List<Package> list = new ArrayList(); 
		try {
			Connection connection = DBHandler.connect();
			PreparedStatement ps = connection.prepareStatement("select * from package_details where no_of_days =(select min(no_of_days) from package_details)");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Package p = new Package();
				String pid = rs.getString(1);
				String splace = rs.getString(2);
				String dplace = rs.getString(3);
				int ndays = rs.getInt(4);
				double pcost = rs.getDouble(5);
				
				p.setPackageId(pid);
				p.setSourcePlace(splace);
				p.setDestinationPlace(dplace);
				p.setNoOfDays(ndays);
				p.setPackageCost(pcost);
				
				list.add(p);
				
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
		
	}

}
