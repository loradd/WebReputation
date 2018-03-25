package it.disim.tlp.webreputation;

/** @author Lorenzo Addazi */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPlugin;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.googleplus.GooglePlusPlugin;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin.LinkedInPlugin;
import it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.websitepages.IlCentroHomePage;
import it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.websitepages.IlCentroSingleArticlePage;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websitepages.OggiScienzaBlogHomePage;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websitepages.OggiScienzaBlogSingleArticlePage;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websitepages.OggiScienzaBlogSingleTopicArticlesPage;

public class WebReputationController implements AggregatorPlugin{

	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from) throws QuotaExceededException {
		
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		
		if(resource != null){
			/* LINKEDIN SOCIAL NETWORK */
			if(resource.startsWith("https://www.linkedin.com")){ // LINKEDIN
				LinkedInPlugin linkedInPlugin = new LinkedInPlugin();
				List<AggregatorPost> linkedInResponse = linkedInPlugin.getPosts(resource, type, from);
				for(AggregatorPost linkedInAggregatorPost : linkedInResponse){
					if(!result.contains(linkedInAggregatorPost)){
						result.add(linkedInAggregatorPost);
					}
				}
			/* GOOGLE PLUS SOCIAL NETWORK */
			} else if(resource.startsWith("https://plus.google.com")){ // GOOGLE PLUS
				GooglePlusPlugin googlePlusPlugin = new GooglePlusPlugin();
				List<AggregatorPost> googlePlusResponse = googlePlusPlugin.getPosts(resource, type, from);
				for(AggregatorPost googlePlusAggregatorPost : googlePlusResponse){
					if(!result.contains(googlePlusAggregatorPost)){
						result.add(googlePlusAggregatorPost);
					}
				}
			/* OGGI SCIENZA BLOG */
			} else if(resource.startsWith("http://oggiscienza.wordpress.com/")){
				if(resource.equals("http://oggiscienza.wordpress.com/")){
					OggiScienzaBlogHomePage homepage = new OggiScienzaBlogHomePage();
					for(AggregatorPost aggregatorPost : homepage.getPosts(resource, type, from)){
						if(!result.contains(aggregatorPost)){
							result.add(aggregatorPost);
						}
					}
				} else {
					String[] urlSections = resource.split("/");
					if(null != urlSections){
						if(urlSections[3].equals("category")){  
							OggiScienzaBlogSingleTopicArticlesPage singleTopicArticlePage = new OggiScienzaBlogSingleTopicArticlesPage();
							for(AggregatorPost aggregatorPost : singleTopicArticlePage.getPosts(resource, type, from)){
								if(!result.contains(aggregatorPost)){
									result.add(aggregatorPost);
								}
							}
						} else if(urlSections[3].matches("[0-9]{4}")){  
							OggiScienzaBlogSingleArticlePage singleArticlePage = new OggiScienzaBlogSingleArticlePage();
							for(AggregatorPost aggregatorPost : singleArticlePage.getPosts(resource, type, from)){
								if(!result.contains(aggregatorPost)){
									result.add(aggregatorPost);
								}
							}
						}
					}
				}
			/* IL CENTRO NEWSPAPER */	
			} else if(resource.startsWith("http://ilcentro.gelocal.it/")){
				String[] urlSections = resource.split("/");
				if(urlSections != null){
					if(urlSections.length == 3 && (urlSections[3].equals("laquila") || urlSections[3].equals("chieti") || urlSections[3].equals("pescara") || urlSections[3].equals("teramo"))){
						IlCentroHomePage homepage = new IlCentroHomePage();
						for(AggregatorPost aggregatorPost : homepage.getPosts(resource, type, from)){
							if(!result.contains(aggregatorPost)){
								result.add(aggregatorPost);
							}
						}
					} else {
						IlCentroSingleArticlePage singleArticlePage = new IlCentroSingleArticlePage();
						for(AggregatorPost aggregatorPost : singleArticlePage.getPosts(resource, type, from)){
							if(!result.contains(aggregatorPost)){
								result.add(aggregatorPost);
							}
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from, Date to) throws QuotaExceededException {
		
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		if(resource != null){
			/* LINKEDIN SOCIAL NETWORK */
			if(resource.startsWith("http://www.linkedin.com")){ // LINKEDIN
				LinkedInPlugin linkedInPlugin = new LinkedInPlugin();
				List<AggregatorPost> linkedInResponse = linkedInPlugin.getPosts(resource, type, from, to);
				for(AggregatorPost linkedInAggregatorPost : linkedInResponse){
					if(!result.contains(linkedInAggregatorPost)){
						result.add(linkedInAggregatorPost);
					}
				}
			/* GOOGLE PLUS SOCIAL NETWORK */
			} else if(resource.startsWith("https://plus.google.com")){ // GOOGLE PLUS 
				GooglePlusPlugin googlePlusPlugin = new GooglePlusPlugin();
				List<AggregatorPost> googlePlusResponse = googlePlusPlugin.getPosts(resource, type, from, to);
				for(AggregatorPost googlePlusAggregatorPost : googlePlusResponse){
					if(!result.contains(googlePlusAggregatorPost)){
						result.add(googlePlusAggregatorPost);
					}
				}
			/* OGGI SCIENZA BLOG */	
			} else if(resource.startsWith("http://www.oggiscienza.wordpress.com/")){
				if(resource.equals("http://www.oggiscienza.wordpress.com")){
					OggiScienzaBlogHomePage homepage = new OggiScienzaBlogHomePage();
					for(AggregatorPost aggregatorPost : homepage.getPosts(resource, type, from, to)){
						if(!result.contains(aggregatorPost)){
							result.add(aggregatorPost);
						}
					}
				} else {
					String[] urlSections = resource.split("/");
					if(null != urlSections){
						if(urlSections[1].equals("category")){  
							OggiScienzaBlogSingleTopicArticlesPage singleTopicArticlePage = new OggiScienzaBlogSingleTopicArticlesPage();
							for(AggregatorPost aggregatorPost : singleTopicArticlePage.getPosts(resource, type, from, to)){
								if(!result.contains(aggregatorPost)){
									result.add(aggregatorPost);
								}
							}
						} else if(urlSections[1].matches("[0-9]{4}")){  
							OggiScienzaBlogSingleArticlePage singleArticlePage = new OggiScienzaBlogSingleArticlePage();
							for(AggregatorPost aggregatorPost : singleArticlePage.getPosts(resource, type, from, to)){
								if(!result.contains(aggregatorPost)){
									result.add(aggregatorPost);
								}
							}
						}
					}
				}
			/* IL CENTRO NEWSPAPER */	
			} else if(resource.startsWith("http://ilcentro.gelocal.it/")){
				String[] urlSections = resource.split("/");
				if(urlSections != null){
					if(urlSections[1].equals("laquila") || urlSections[1].equals("chieti") || urlSections[1].equals("pescara") || urlSections[1].equals("teramo")){
						IlCentroHomePage homepage = new IlCentroHomePage();
						for(AggregatorPost aggregatorPost : homepage.getPosts(resource, type, from, to)){
							if(!result.contains(aggregatorPost)){
								result.add(aggregatorPost);
							}
						}
					} else {
						IlCentroSingleArticlePage singleArticlePage = new IlCentroSingleArticlePage();
						for(AggregatorPost aggregatorPost : singleArticlePage.getPosts(resource, type, from, to)){
							if(!result.contains(aggregatorPost)){
								result.add(aggregatorPost);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
}
