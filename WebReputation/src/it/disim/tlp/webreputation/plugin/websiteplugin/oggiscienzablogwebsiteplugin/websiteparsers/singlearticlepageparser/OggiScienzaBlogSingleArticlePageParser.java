package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singlearticlepageparser;

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

public class OggiScienzaBlogSingleArticlePageParser extends OggiScienzaBlogOutlinePageParser<OggiScienzaBlogSingleArticlePageParserResponse> {

	@Override
	public WebsiteParserResponse<OggiScienzaBlogSingleArticlePageParserResponse> getPostsFromResource(String resource, Date from, Date to) {
		
		/* lista degli aggregator post */
		List<AggregatorPost> responseContent = new ArrayList<AggregatorPost>();
		if(null != resource){
			/* lista dei link */
			List<String> linkContainer = new ArrayList<String>();
			/* formato data (anno/mese/giorno) */
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				/* connessione ad url della pagina */
				Document document = Jsoup.connect(resource).get();
				Element singleArticlePageMainContainer = document.select("div.wrapper.clearfix > div.main").first();
				if(null != singleArticlePageMainContainer){
					Element articleContent = singleArticlePageMainContainer.select("div.content.left").first();
					if(null != articleContent){
						Element articleTitle = null;
						Element articleCommentsNumber = null;
						Element articleBody = articleContent.select("article").first();
						if(null != articleBody){
							articleTitle = articleBody.select("header.post-header > h1.entry-title").first();
							Element articleAuthor = articleBody.select("header.post-header > p.meta.post-meta > span.vcard.author > span.fn > a").first();
							articleCommentsNumber = articleBody.select("p.meta.post-meta").first(); // COMMENTS NUMBER 
							Elements articleText = articleBody.select("div.entry.clearfix p");
							List<String> articleTextParagraphs = new ArrayList<String>();
							for(Element articleTextParagraph : articleText){
								articleTextParagraphs.add(articleTextParagraph.text());
							}
							articleTextParagraphs.remove(0);
							if(articleTextParagraphs.size() > 1){
								articleTextParagraphs.remove(articleTextParagraphs.size() - 1);
							}
							String postText = "";
							for(String paragraph : articleTextParagraphs){
								postText += " " + paragraph;
							}
							/* SINGLE POST AGGREGATOR POST */
							AggregatorPost post = new AggregatorPost();
							if(null != articleTitle){
								post.setTitle(articleTitle.text() + " - Post");
							} else {
								post.setTitle("Unknown");
							}
							post.setText(postText);
							if(null != articleAuthor){
								post.setAuthor(articleAuthor.text());
							} else {
								post.setAuthor("Unknown");
							}
							post.setDate(formatter.parse(resource.substring(33, 43)));
							if(null != articleCommentsNumber){
								post.setVisibility(Integer.parseInt(articleCommentsNumber.text().substring(articleCommentsNumber.text().indexOf("// ") + 3, articleCommentsNumber.text().toLowerCase().indexOf("comment")).replaceAll(" ", "")));
							} else {
								post.setVisibility(0);
							}
							post.setSource("OggiScienza");
							post.setLink(resource);
							responseContent.add(post);
						}
						Elements articleComments = articleContent.select("ol.commentlist > li.comment");
						for(Element articleComment : articleComments){
							Element commentInfo = articleComment.select("div > div.vcard.meta").first();
							Element commentText = articleComment.select("div > div.comment-text > p").first();
							String[] info = commentInfo.text().split("// ");
							/* SINGLE COMMENT - AGGREGATOR POST */
							AggregatorPost comment = new AggregatorPost();
							comment.setTitle(articleTitle.text() + " - Comment");
							if(null != commentText){
								comment.setText(commentText.text());
							} else {
								comment.setText("Unknown");
							}
							if(null != commentInfo){
								comment.setAuthor(info[0]);
								comment.setDate(formatter.parse(this.convertDateString(info[1].toLowerCase())));
							} 
							if(null != articleCommentsNumber){
								comment.setVisibility(Integer.parseInt(articleCommentsNumber.text().substring(articleCommentsNumber.text().indexOf("// ") + 3, articleCommentsNumber.text().toLowerCase().indexOf("comment")).replaceAll(" ", "")));
							} else {
								comment.setVisibility(0);
							}
							comment.setSource("OggiScienza");
							comment.setLink(resource);
							responseContent.add(comment);
						}
					}
					/* PARSING RIGHT SIDEBAR */
					Element singleArticlePageRightSidebar = singleArticlePageMainContainer.select("aside.sidebar.sb-right").first();
					if(null != from && null != singleArticlePageRightSidebar){
						for(String link : this.parserRightSidebar(singleArticlePageRightSidebar.toString())){
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
				/* PARSING FOOTER */
				Element singleArticlePageFooter = document.select("footer").first();
				if(null != from && null != singleArticlePageFooter){
					for(String link : this.parseFooter(singleArticlePageFooter.toString())){
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
		}
		OggiScienzaBlogSingleArticlePageParserResponse oggiScienzaBlogSingleArticlePageParserResponse = new OggiScienzaBlogSingleArticlePageParserResponse(responseContent);
		WebsiteParserResponse<OggiScienzaBlogSingleArticlePageParserResponse> parserResponse = new WebsiteParserResponse<OggiScienzaBlogSingleArticlePageParserResponse>(oggiScienzaBlogSingleArticlePageParserResponse);
		return parserResponse;
	}
	
	private String convertDateString(String dateString){
		String[] datePieces = dateString.substring(0, dateString.indexOf("alle")).replaceAll(" ", "/").split("/");
		String dateYear = datePieces[2];
		String dateMonth = 
				(datePieces[1].equals("gennaio"))? "01" : 
					(datePieces[1].equals("febbraio"))? "02" : 
						(datePieces[1].equals("marzo"))? "03" :
							(datePieces[1].equals("aprile"))? "04" :
								(datePieces[1].equals("maggio"))? "05" :
									(datePieces[1].equals("giugno"))? "06" :
										(datePieces[1].equals("luglio"))? "07" :
											(datePieces[1].equals("agosto"))? "08" :
												(datePieces[1].equals("settembre"))? "09" :
													(datePieces[1].equals("ottobre"))? "10" :
														(datePieces[1].equals("novembre"))? "11" :
															(datePieces[1].equals("dicembre"))? "12" : "";
		String dateDay = datePieces[0];
		return dateYear + "/" + dateMonth + "/" + dateDay;
	}

}
