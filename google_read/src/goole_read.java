import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.spi.DirStateFactory.Result;

public class goole_read implements Runnable  
{
	private volatile String string;
	
public void run() {
	string = "goole_read";
     
	try
	{
    	Class.forName("com.mysql.jdbc.Driver");  
    	Connection conn=DriverManager.getConnection("jdbc:mysql://192.168.0.111:3306/sample","root","root");
    	PreparedStatement ps=null;
    	ResultSet rs=null;
    	PreparedStatement ps1=null;
    	ResultSet rs1=null;
    	PreparedStatement ps2=null;
    	ResultSet rs2=null;
 /* please input */   	
    	Scanner sc=new Scanner(System.in);  
    	     
    	//System.out.println("Enter your your Text :");  
    	//String result=sc.next();  
    	//String result="java.pdf";
    	
    	String sql22="select * from new_it_skills where id between 158 and 1000";
    	ps2=conn.prepareStatement(sql22);
    	rs2=ps2.executeQuery();
    	while(rs2.next())
    	{
    	String skill=rs2.getString("skill_name");
    	
		Thread.sleep(50000);		
		String request = "https://www.google.com/=" + skill+".pdf &num=1000";
		System.out.println("Sending request..." + request);
		   try {
			   
					// need http protocol, set this as a Google bot agent :)
			    Document doc = Jsoup.connect(request).userAgent(
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					.timeout(5000).get();
			    Elements sss=doc.select(".r");
			    for(Element sdsd:sss)
			    {
			    	Thread.sleep(1000);
			    	String base_url =sdsd.select("a").attr("href").replace("/url?q=", "");
			    	String[] rel_url= base_url.split("&sa=U&ved=0");
			    	String orginal_url="";
			    	try
			    	{
			    		orginal_url=rel_url[0];
			    		System.out.println(orginal_url);
			    	}catch(Exception e){}
			    	
	String sql="select * from skill_wise_pdf_url where url=? and search_text=?";
	ps=conn.prepareStatement(sql);
	ps.setString(1, orginal_url);
	ps.setString(2, skill);
	rs=ps.executeQuery();
	if(rs.next())
	{
		
	}else
	{
		String sql1="insert into skill_wise_pdf_url(url,search_text) values(?,?)";
		 ps1=conn.prepareStatement(sql1);
		 ps1.setString(1, orginal_url);;
		 ps1.setString(2, skill);
		 ps1.executeUpdate();
	}
	}
		   
	} catch (IOException e) {
		e.printStackTrace();
	}	
    }
	}catch(Exception e){e.printStackTrace();
	}	
	
}

public static void main(String[] args) throws InterruptedException 
{
	goole_read g1=new goole_read();
	Thread t1=new Thread(g1);
	t1.start();		
}
}