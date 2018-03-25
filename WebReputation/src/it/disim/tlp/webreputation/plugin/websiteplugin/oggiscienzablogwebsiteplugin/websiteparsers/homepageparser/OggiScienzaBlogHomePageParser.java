package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.homepageparser;

/** @author Lorenzo Addazi */

import java.io.IOException;
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

public class OggiScienzaBlogHomePageParser extends OggiScienzaBlogOutlinePageParser<OggiScienzaBlogHomePageParserResponse> {
	
	@Override
	public WebsiteParserResponse<OggiScienzaBlogHomePageParserResponse> getPostsFromResource(String resource, Date from, Date to) {
		/* lista dei link */
		List<String> linkContainer = new ArrayList<String>();
		/* lista degli aggregator post */
		List<AggregatorPost> responseContent = new ArrayList<AggregatorPost>();
		try {
			/* connessione ad url della pagina */
			Document document = Jsoup.connect(resource).get(); 
			Element container = document.select("div.container.mh-mobile").first(); // Normal Retrieval Flow : Single Element
			if(null != container){
				Elements mainContentClearfixList = container.select("div.wrapper.hp.clearfix > div.main > div.clearfix"); // Normal Retrieval Flow : Single Element
				Element upperClearfix = mainContentClearfixList.first();
				if(null != upperClearfix){
					Element mainArticleLink = upperClearfix.
							select("div.content.left > "
									+ "div.sb-widget.home-2.home-wide > "
										+ "article.spotlight > "
											+ "a").first();
					if(null != mainArticleLink && !linkContainer.contains(mainArticleLink.attr("href"))){
						linkContainer.add(mainArticleLink.attr("href"));
						OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
						for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(mainArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
							if(!responseContent.contains(aggregatorPost)){
								/* aggiungo aggregatorPost al risultato */
								responseContent.add(aggregatorPost);
							}
						}
					}
					Elements mostRecentSidebarLinks = upperClearfix.
							select("div.hp-sidebar.sb-right.hp-home-6 >"
									+ "div.sb-widget.home-6.home-wide-2 >"
										+ "ul.cp-widget.clearfix >"
											+ "li.cp-wrap.cp-small.clearfix >"
												+ "div.cp-data >"
													+ "p.cp-widget-title >"
														+ "a");
					for(Element mostRecentSidebarLink : mostRecentSidebarLinks){
						if(!linkContainer.contains(mostRecentSidebarLink.attr("href"))){
							linkContainer.add(mostRecentSidebarLink.attr("href"));
							OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
							for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(mostRecentSidebarLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
								if(!responseContent.contains(aggregatorPost)){
									/* aggiungo aggregatorPost al risultato */
									responseContent.add(aggregatorPost);
								}
							}
						}
					}
				}
				Element bottomClearfix = mainContentClearfixList.last();
				if(null != bottomClearfix){
					Elements leftBottomColumn = bottomClearfix.
							select("div.hp-sidebar.hp-sidebar-left >"
									+ "div.sb-widget.home-8 >"
										+ "ul.cp-widget.clearfix");
					for(Element leftBottomColumnRow : leftBottomColumn){
						Element mainRowArticleLink = leftBottomColumnRow.
								select("li.cp-wrap.clearfix >"
										+ "div.cp-data >"
											+ "h3.cp-xl-title >"
												+ "a").first();
						if(null != mainRowArticleLink && !linkContainer.contains(mainRowArticleLink.attr("href"))){
							linkContainer.add(mainRowArticleLink.attr("href"));
							OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
							for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(mainRowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
								if(!responseContent.contains(aggregatorPost)){
									/* aggiungo aggregatorPost al risultato */
									responseContent.add(aggregatorPost);
								}
							}
						}
						Elements rowArticleLinkList = leftBottomColumnRow.
								select("li.cp-wrap.cp-small.clearfix >"
										+ "div.cp-data >"
											+ "p.cp-widget-title >"
												+ "a");
						for(Element rowArticleLink : rowArticleLinkList){
							if(!linkContainer.contains(rowArticleLink.attr("href"))){
								linkContainer.add(rowArticleLink.attr("href"));
								OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
								for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(rowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
									if(!responseContent.contains(aggregatorPost)){
										/* aggiungo aggregatorPost al risultato */
										responseContent.add(aggregatorPost);
									}
								}
							}
						}
					}
					Elements centerBottomColumn = bottomClearfix.
							select("div.hp-sidebar.sb-right.hp-sidebar-right >"
									+ "div.sb-widget.home-9 >"
										+ "ul.cp-widget.clearfix");
					for(Element centerBottomColumnRow : centerBottomColumn){
						Element mainRowArticleLink = centerBottomColumnRow.
								select("li.cp-wrap.clearfix >"
										+ "div.cp-data >"
											+ "h3.cp-xl-title >"
												+ "a").first();
						if(null != mainRowArticleLink && !linkContainer.contains(mainRowArticleLink.attr("href"))){
							linkContainer.add(mainRowArticleLink.attr("href"));
							OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
							for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(mainRowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
								if(!responseContent.contains(aggregatorPost)){
									/* AGGIUNGO AGGREGATOR POST */
									responseContent.add(aggregatorPost);
								}
							}
						}
						Elements rowArticleLinkList = centerBottomColumnRow.
								select("li.cp-wrap.cp-small.clearfix >"
										+ "div.cp-data >"
											+ "p.cp-widget-title >"
												+ "a");
						for(Element rowArticleLink : rowArticleLinkList){
							if(!linkContainer.contains(rowArticleLink.attr("href"))){
								linkContainer.add(rowArticleLink.attr("href"));
								OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
								for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(rowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
									if(!responseContent.contains(aggregatorPost)){
										/* aggiungo aggregatorPost al risultato */
										responseContent.add(aggregatorPost);
									}
								}
							}
						}
					}
					Elements rightBottomColumn = bottomClearfix.
							select("div.hp-sidebar.sb-right >"
									+ "div.sb-widget.home-10.home-wide-2 >"
										+ "ul.cp-widget.clearfix");
					for(Element rightBottomColumnRow : rightBottomColumn){
						Element mainRowArticleLink = rightBottomColumnRow.
								select("li.cp-wrap.clearfix >"
										+ "div.cp-data >"
											+ "h3.cp-xl-title >"
												+ "a").first();
						if(null != mainRowArticleLink && !linkContainer.contains(mainRowArticleLink.attr("href"))){
							linkContainer.add(mainRowArticleLink.attr("href"));
							OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
							for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(mainRowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
								if(!responseContent.contains(aggregatorPost)){
									/* aggiungo aggregatorPost al risultato */
									responseContent.add(aggregatorPost);
								}
							}
						}
						Elements rowArticleLinkList = rightBottomColumnRow.
								select("li.cp-wrap.cp-small.clearfix >"
										+ "div.cp-data >"
											+ "p.cp-widget-title >"
												+ "a");
						for(Element rowArticleLink : rowArticleLinkList){
							if(!linkContainer.contains(rowArticleLink.attr("href"))){
								linkContainer.add(rowArticleLink.attr("href"));
								OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
								for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(rowArticleLink.attr("href"), null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
									if(!responseContent.contains(aggregatorPost)){
										/* aggiungo aggregatorPost al risultato */
										responseContent.add(aggregatorPost);
									}
								}
							}
						}
					}
				}
				/* footer */
				Element footer = container.select("footer").first();
				if(null != footer){
					for(String footerArticleLink : this.parseFooter(footer.toString())){
						if(!linkContainer.contains(footerArticleLink)){
							linkContainer.add(footerArticleLink);
							OggiScienzaBlogSingleArticlePageParser singleArticleParser = new OggiScienzaBlogSingleArticlePageParser();
							for(AggregatorPost aggregatorPost : singleArticleParser.getPostsFromResource(footerArticleLink, null, to).getWebsiteParserResponseObject().getWebsiteParserResponseObject()){
								if(!responseContent.contains(aggregatorPost)){
									/* aggiungo aggregatorPost al risultato */
									responseContent.add(aggregatorPost);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {}
		OggiScienzaBlogHomePageParserResponse oggiScienzaBlogHomePageParserResponse = new OggiScienzaBlogHomePageParserResponse(responseContent);
		WebsiteParserResponse<OggiScienzaBlogHomePageParserResponse> parserResponse = new WebsiteParserResponse<OggiScienzaBlogHomePageParserResponse>(oggiScienzaBlogHomePageParserResponse);
		return parserResponse;
	}

}
