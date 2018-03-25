package it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;

import java.awt.Font;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Test {

	public static void main(String args[]) {

		LinkedInPlugin linkedInPlugin = new LinkedInPlugin();
		try {
			
			DateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			dateformat.setTimeZone(TimeZone.getTimeZone("CET"));
			
			Date from = dateformat.parse(("Sun Dec 25 18:00:00 CEST 2011").replaceAll("\\p{Cntrl}", "")); 
			Date to = dateformat.parse(("Tue Nov 26 18:05:33 CET 2013").replaceAll("\\p{Cntrl}", ""));
			
			List<AggregatorPost> list = linkedInPlugin.getPosts(
					"https://www.linkedin.com/groups?home=&gid=150030",
					"group_posts", from);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL POSTS FROM A SINGLE GROUP ( Min Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts(
					"https://www.linkedin.com/groups?home=&gid=150030",
					"group_posts", from, to);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL POSTS FROM A SINGLE GROUP ( Min and Max Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts(null, "user_groups_posts", from);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL POSTS FROM ALL USER RELATED GROUPS ( Min Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts(null, "user_groups_posts", from, to);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL POSTS FROM ALL USER RELATED GROUPS ( Min and Max Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts("http://www.linkedin.com/company/1337", "company_updates", from);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL STATUS UPDATES POSTS FROM A COMPANY ( Min Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts("http://www.linkedin.com/company/1337", "company_updates", from, to);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL STATUS UPDATES POSTS FROM A COMPANY ( Min and Max Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts(null, "user_companies_updates", from);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL STATUS UPDATES POSTS FROM A ALL COMPANIES RELATED TO USER ( Min Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
			list = linkedInPlugin.getPosts(null, "user_companies_updates", from, to);
			if(!list.isEmpty()){
				System.out.println();
				System.out.println("ALL STATUS UPDATES POSTS FROM A ALL COMPANIES RELATED TO USER ( Min and Max Date Limit )");
				System.out.println();
				for(AggregatorPost element : list){
					System.out.println("Title : " + element.getTitle());
					System.out.println("Text : " + element.getText());
					System.out.println("Author : " + element.getAuthor());
					System.out.println("Date : " + element.getDate());
					System.out.println("Visibility : " + element.getVisibility());
					System.out.println("Source : " + element.getSource());
					System.out.println("Link : " + element.getLink());
					System.out.println();
				}
			}
			
		} catch (QuotaExceededException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
