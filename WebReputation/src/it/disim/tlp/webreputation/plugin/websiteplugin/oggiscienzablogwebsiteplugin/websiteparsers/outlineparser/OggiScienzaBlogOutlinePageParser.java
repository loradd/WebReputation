package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.outlineparser;

/** @author Lorenzo Addazi */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser.WebsiteParser;
import it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser.WebsiteParserResponse;

public abstract class OggiScienzaBlogOutlinePageParser<RESPONSE_OBJECT_TYPE> implements WebsiteParser<RESPONSE_OBJECT_TYPE> {
	
	/* resource : stringa html */
	public List<String> parseFooter(String resource){
		/* lista dei link nel footer */
		List<String> linkContainer = new ArrayList<String>();
		if(!resource.isEmpty()){
			/* recupero la stringa sorgente */
			Document footer = Jsoup.parse(resource);
			if(null != footer){
				/* colonna articoli da foto */
				Element fromPhotosFooterColumn = footer.select("div.col-1-4.mq-footer > div.footer-widget.footer-1").first();
				if(null != fromPhotosFooterColumn){
					Elements fromPhotosArticlesLinks = fromPhotosFooterColumn.
						select("ul.nip-widget.clearfix >"
							+ "li.nip-thumb >"
								+ "a");
					for(Element fromPhotosArticleLink : fromPhotosArticlesLinks){
						if(!linkContainer.contains(fromPhotosArticleLink.attr("href"))){
							/* link aggiunto nella lista risultato */
							linkContainer.add(fromPhotosArticleLink.attr("href"));
						}
					}
				}
				/* colonna articoli recenti */
				Element mostRecentFooterColumn = footer.select("div.col-1-4.mq-footer > div.footer-widget.footer-2").first();
				if(null != mostRecentFooterColumn){
					Elements mostRecentArticlesLinks = mostRecentFooterColumn.
						select("ul.cp-widget.clearfix >"
							+ "li.cp-wrap.cp-small.cp-no-image.clearfix >"
								+ "div.cp-data >"
									+ "p.cp-widget-title >"
										+ "a");
					for(Element mostRecentArticleLink : mostRecentArticlesLinks){
						if(!linkContainer.contains(mostRecentArticleLink.attr("href"))){
							/* link aggiunto nella lista risultato */
							linkContainer.add(mostRecentArticleLink.attr("href"));
						}
					}
				}
				/* colonna articoli popolari */
				Element mostPopularFooterColumn = footer.select("div.col-1-4.mq-footer > div.footer-widget.footer-3").first();
				if(null != mostPopularFooterColumn){
					Elements mostPopularArticlesLinks = mostPopularFooterColumn.
						select("ul.cp-widget.clearfix >"
							+ "li.cp-wrap.cp-small.cp-no-image.clearfix >"
								+ "div.cp-data >"
									+ "p.cp-widget-title >"
										+ "a");
					for(Element mostPopularArticleLink : mostPopularArticlesLinks){
						if(!linkContainer.contains(mostPopularArticleLink.attr("href"))){
							/* link aggiunto nella lista risultato */
							linkContainer.add(mostPopularArticleLink.attr("href"));
						}
					}
				}
			}
		}
		return linkContainer;
	}
	
	/* resource : stringa html */
	public List<String> parserRightSidebar(String resource){
		/* lista dei link nella sidebar destra */
		List<String> result = new ArrayList<String>();
		if(!resource.isEmpty()){
			Document rightSidebar = Jsoup.parse(resource);
			if(null != rightSidebar){
				Elements sidebarWidgets = rightSidebar.select("div.sb-widget");
				if(!sidebarWidgets.isEmpty()){
					/* articoli popolari */
					Element mostPopularWidget = sidebarWidgets.get(2);
					if(null != mostPopularWidget){
						Elements mostPopularLinks = mostPopularWidget.select("p.cp-widget-title > a");
						for(Element mostPopularLink : mostPopularLinks){
							if(!result.contains(mostPopularLink.attr("href"))){
								/* link aggiunto al risultato */
								result.add(mostPopularLink.attr("href"));
							}
						}
					}
					/* articoli recenti */
					Element mostRecentWidget = sidebarWidgets.get(3);
					if(null != mostRecentWidget){
						Elements mostRecentLinks = mostRecentWidget.select("p.cp-widget-title > a");
						for(Element mostRecentLink : mostRecentLinks){
							if(!result.contains(mostRecentLink.attr("href"))){
								/* link aggiunto al risultato */
								result.add(mostRecentLink.attr("href"));
							}
						}
					}
					/* articoli da foto */
					Element fromPhotosWidget = sidebarWidgets.get(4);
					if(null != fromPhotosWidget){
						Elements fromPhotoLinks = fromPhotosWidget.select("li.nip-thumb > a");
						for(Element fromPhotoLink : fromPhotoLinks){
							if(!result.contains(fromPhotoLink.attr("href"))){
								/* link aggiunto alla soluzione */
								result.add(fromPhotoLink.attr("href"));
							}
						}
					}
				}
			}
		}
		return result;
	}
	public abstract WebsiteParserResponse<RESPONSE_OBJECT_TYPE> getPostsFromResource(String resource, Date from, Date to);
	
}
