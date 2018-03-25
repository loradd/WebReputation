package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singletopicarticlespageparser;

/** @author Lorenzo Addazi */

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser.WebsiteParserResponse;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.outlineparser.OggiScienzaBlogOutlinePageParser;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singlearticlepageparser.OggiScienzaBlogSingleArticlePageParser;

public class OggiScienzaBlogSingleTopicArticlesPageParser extends OggiScienzaBlogOutlinePageParser<OggiScienzaBlogSingleTopicArticlesPageParserResponse> {

	@Override
	public WebsiteParserResponse<OggiScienzaBlogSingleTopicArticlesPageParserResponse> getPostsFromResource(String resource, Date from, Date to) {
		/* lista dei link */
		List<AggregatorPost> responseContent = new ArrayList<AggregatorPost>();
		/* lista dgli aggregator post */
		List<String> linkContainer = new ArrayList<String>();
		try{
			/* connessione ad url della pagina */
			Document document = Jsoup.connect(resource).get();
			Element container = document.select("div.container.mh-mobile").first();
			if(null != container){
				Element mainContent = container.select("div.wrapper.clearfix > div.main").first();
				if(null != mainContent){
					Element contentLeftSection = mainContent.select("section.content.left").first();
					if(null != contentLeftSection){
						Elements contentLeftArticles = contentLeftSection.select("article");
						String postDateString = "";
						DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
						boolean articleLoopFlag = true;
						for(Element contentLeftArticle : contentLeftArticles){
							Element articleLink = contentLeftArticle.
									select("div.loop-wrap.loop-layout2 >"
												+ "div.clearfix >"
													+ "div.loop-content >"
														+ "header >"
															+ "h3.loop-title >"
																+ "a").first();
						if(null != articleLink){
							/* data del post - guardia della paginazione */
							postDateString = articleLink.attr("href").substring(33, 43);
							Date postDate = formatter.parse(postDateString);
							if(!(postDate.after(from) && (null == to || postDate.before(to)))){
								/* non devo continuare */
								articleLoopFlag = false;
								break;
							} else {
								if(!linkContainer.contains(articleLink.attr("href"))){
									/* link aggiunto alla soluzione */
									linkContainer.add(articleLink.attr("href"));
									OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
									for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(articleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
										if(!responseContent.contains(aggregatorPost)){
											/* aggiungo aggregator post alla soluzione */
											responseContent.add(aggregatorPost);
										}
									}
								}
							}
						}
					}
					/* CONSIDERING THE FOLLOWING ARTICLES PAGE */
					if(articleLoopFlag){ 
						/* CURRENT PAGE NUMBER ELEMENT */
						Element currentPageNumberElement = contentLeftSection.select("span.page-numbers.current").first();
						if(null != currentPageNumberElement){
							/* NEXT PAGE NUMBER ELEMENT */
							Element nextPageElement = currentPageNumberElement.nextElementSibling();
							int currentPageNumber = Integer.parseInt(contentLeftSection.select("span.page-numbers.current").first().text());
							System.out.println("Page Completed : " + currentPageNumber);
							if(null != nextPageElement){
								/* analizzo la pagina successiva */
								for(AggregatorPost aggregatorPost : this.getPostsFromResource(nextPageElement.attr("href"), from, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
									if(!responseContent.contains(aggregatorPost)){
										/* aggiungo aggregator post al risultato */
										responseContent.add(aggregatorPost);
									}
								}
							} else {
								/* ultima pagina */
								System.out.println(" - This is the last page available");
							}
						}
					}
				}
					/* right sidebar */
					Element contentRightSidebar = mainContent.select("aside.sidebar.sb-right").first();
					if(null != contentRightSidebar){
						for(String link : this.parserRightSidebar(contentRightSidebar.toString())){
							if(!linkContainer.contains(link)){
								linkContainer.add(link);
								OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
								for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(link, null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
									if(!responseContent.contains(aggregatorPost)){
										responseContent.add(aggregatorPost);
									}
								}
							}
						}
					}
			}
				/* footer */
				Element footer = container.select("footer").first();
				for(String link : this.parseFooter(footer.toString())){
					if(!linkContainer.contains(link)){
						linkContainer.add(link);
						OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
						for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(link, null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
							if(!responseContent.contains(aggregatorPost)){
								responseContent.add(aggregatorPost);
							}
						}
					}
				}
		}
			
			
		} catch (IOException | ParseException e) {
		}
		OggiScienzaBlogSingleTopicArticlesPageParserResponse oggiScienzaBlogSingleTopicArticlesPageParserResponse = new OggiScienzaBlogSingleTopicArticlesPageParserResponse(responseContent);
		WebsiteParserResponse<OggiScienzaBlogSingleTopicArticlesPageParserResponse> parserResponse = new WebsiteParserResponse<OggiScienzaBlogSingleTopicArticlesPageParserResponse>(oggiScienzaBlogSingleTopicArticlesPageParserResponse);
		return parserResponse;
	}

}
