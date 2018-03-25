package it.disim.tlp.webreputation;

/** @author Aldo D'Eramo, Lorenzo Addazi */

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TestMe {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Writer resultWriter = null;
		DateFormat yearMonthDayDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat completeDateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy");
		try {
		/* OGGI SCIENZA BLOG TEST */
			/* HOMEPAGE FROM DATE (TO DATE) 
			String resource = "http://oggiscienza.wordpress.com/";
			String type = "homepage";
			Date from = yearMonthDayDateFormat.parse("2014/07/01");
			Date to = yearMonthDayDateFormat.parse("2014/07/07");
			*/
			/* SINGLE TOPIC PAGE FROM DATE (TO DATE) */
			String resource = "http://oggiscienza.wordpress.com/category/ricerca-2/";
			String type = "single topic"; 
			Date from = yearMonthDayDateFormat.parse("2014/06/15"); 
			Date to = yearMonthDayDateFormat.parse("2014/07/07");
		
			/* SINGLE ARTICLE PAGE FROM DATE (TO DATE)  
			String resource = "http://oggiscienza.wordpress.com/2014/07/01/lepidemia-di-ebola-non-accenna-a-fermarsi/";
			String type = "single article"; 
			Date from = yearMonthDayDateFormat.parse("2013/02/15"); 
			Date to = yearMonthDayDateFormat.parse("2014/07/07");
			*/
		/* IL CENTRO NEWSPAPER TEST */
			/* HOMEPAGE FROM DATE (TO DATE)  
			String resource = "http://ilcentro.gelocal.it/pescara"; 
			String type = "homepage";
			Date from = new Date(); 
			Date to = new Date();
			*/ 
			/* SINGLE ARTICLE PAGE FROM DATE (TO DATE) 
			String resource = "http://ilcentro.gelocal.it/pescara/cronaca/2014/07/03/news/pescara-scontro-toto-alessandrini-sulla-teodoro-assessore-1.9532727"; 
			String type = "single article"; 
			Date from = new Date(); 
			Date to = new Date();
			*/
		/* GOOGLE PLUS SOCIAL NETWORK TEST */
			/* POSTS 
			String resource =  "https://plus.google.com/+AdrianByrne/posts"; 
			String type = "posts"; 
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(2013, 5, 15);
			Date from =  calendar.getTime();
			calendar.set(2014, 5, 15);
			Date to = calendar.getTime(); */
		/* LINKEDIN SOCIAL NETWORK TEST */
			/* POSTS FROM GROUP 
			String resource = "https://www.linkedin.com/groups?gid=150030"; 
			String type = "group_posts"; 
			Date from = completeDateFormat.parse(("Sun Dec 25 18:00:00 CEST 2011").replaceAll("\\p{Cntrl}", ""));
			Date to = completeDateFormat.parse(("Tue Nov 26 18:05:33 CET 2013").replaceAll("\\p{Cntrl}", ""));
			*/
			/* POSTS FROM USER RELATED GROUPS  	
			String resource = "https://www.linkedin.com/profile/view?id=340195237"; 	
			String type = "user_groups_posts" ;
			Date from = completeDateFormat.parse(("Sun Dec 25 18:00:00 CEST 2011").replaceAll("\\p{Cntrl}", "")); 
			Date to = completeDateFormat.parse(("Tue Nov 26 18:05:33 CET 2013").replaceAll("\\p{Cntrl}", ""));
			*/
			/* STATUS UPDATES FROM COMPANY 
			 String resource = "https://www.linkedin.com/company/1337"; 
			 String resource = "https://www.linkedin.com/company/university-of-pavia"; 
			 String type = "company_updates"; 
			 Date from = completeDateFormat.parse(("Sun Dec 25 18:00:00 CEST 2011").replaceAll("\\p{Cntrl}", "")); 
			 Date to = completeDateFormat.parse(("Tue Nov 26 18:05:33 CET 2013").replaceAll("\\p{Cntrl}", ""));
			 */
			/* STATUS UPDATES FROM USER RELATED COMPANIES  
			 String resource = "https://www.linkedin.com/profile/view?id=340195237"; 
			 String type = "user_companies_updates"; 
			 Date from = completeDateFormat.parse(("Sun Dec 25 18:00:00 CEST 2011").replaceAll("\\p{Cntrl}", ""));
			 Date to = completeDateFormat.parse(("Tue Nov 26 18:05:33 CET 2013").replaceAll("\\p{Cntrl}", ""));
			 */

			resultWriter = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"/Users/lorenzoaddazi/Documents/EclipseWorkspace/TlpProjectWorkspace/WebReputation/src/it/disim/tlp/webreputation/results.txt")));

			WebReputationController testController = new WebReputationController();
			/*
			 * FROM AND TO for(AggregatorPost aggregatorPost :
			 * testController.getPosts(resource, type, from, to)){
			 */
			/* FROM ONLY */
			for (AggregatorPost aggregatorPost : testController.getPosts(resource, type, from)) {
				resultWriter.write("TITOLO : " + StringUtils.replaceChars(aggregatorPost.getTitle(), "àèéìòù","aeeiou") + "\n");
				resultWriter.write("TESTO : " + StringUtils.replaceChars(aggregatorPost.getText(), "àèéìòù","aeeiou") + "\n");
				resultWriter.write("AUTORE : " + StringUtils.replaceChars(aggregatorPost.getAuthor(), "àèéìòù","aeeiou") + "\n");
				resultWriter.write("DATA : " + aggregatorPost.getDate() + "\n");
				resultWriter.write("VISIBILITA : " + aggregatorPost.getVisibility() + "\n");
				resultWriter.write("SORGENTE : " + aggregatorPost.getSource() + "\n");
				resultWriter.write("LINK : " + aggregatorPost.getLink() + "\n");
				resultWriter.write("\n");
			}
			resultWriter.close();
		//} catch (QuotaExceededException | IOException e) {			
		} catch (QuotaExceededException | IOException | ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != resultWriter) {
					resultWriter.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
