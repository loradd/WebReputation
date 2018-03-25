package it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.exceptions.pluginexceptions.ResourceURLFormatException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.SocialNetworkPlugin;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.authentication.AuthRequest;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.authentication.AuthResponse;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin.authentication.LinkedInAuthRequest;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin.authentication.LinkedInAuthResponse;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LinkedInPlugin implements
		SocialNetworkPlugin<LinkedInAuthRequest, LinkedInAuthResponse> {

	/* LinkedIn REST API Fixed Prefix */
	private final static String linkedInApiPrefix = "http://api.linkedin.com/v1/";
	
	/* Company Updates Fields */
	private static String postTitle = ""; // same for comments
	private static String postText = "";
	private static String postAuthor = "";
	private static Date postDate = null;
	private static int postVisibility = 0;
	private final static String postSource = "LinkedIn"; // same for comments
	private static String postLink = ""; // same for comments
	/* Company Update Comments Fields */
	private static String commentText = "";
	private static String commentAuthor = "";
	private static Date commentDate = null;
	private static int commentVisibility = 0;
	
	private static final DateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	static {
		dateformat.setTimeZone(TimeZone.getTimeZone("CET"));
	} 
	
	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from)
			throws QuotaExceededException {

		/* LINKEDIN AUTHENTICATION */
		LinkedInAuthRequest linkedInAuthRequest = new LinkedInAuthRequest();
		AuthRequest<LinkedInAuthRequest> authRequest = new AuthRequest<LinkedInAuthRequest>(
				linkedInAuthRequest);
		AuthResponse<LinkedInAuthResponse> authResponse = this
				.authentication(authRequest);

		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		try {
			if (type.toLowerCase().startsWith("group_posts")) {

				result.addAll(this.fetchGroupPostsFromLinkedIn(resource, from,
						null, authResponse));

			} else if (type.toLowerCase().startsWith("user_groups_posts")) {

				result.addAll(this.fetchAllUserGroupsPostsFromLinkedIn(
						resource, from, null, authResponse));

			} else if (type.toLowerCase().startsWith("user_companies_updates")) {

				result.addAll(this.fetchAllUserCompaniesUpdatesFromLinkedIn(
						resource, from, null, authResponse));

			} else if (type.toLowerCase().startsWith("company_updates")) {

				result.addAll(this.fetchAllCompanyUpdatesFromLinkedIn(resource,
						from, null, authResponse));

			}
		} catch (ParserConfigurationException | SAXException | IOException
				| ResourceURLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<AggregatorPost> getPosts(String resource, String type,
			Date from, Date to) throws QuotaExceededException {
		/* LINKEDIN AUTHENTICATION */
		LinkedInAuthRequest linkedInAuthRequest = new LinkedInAuthRequest();
		AuthRequest<LinkedInAuthRequest> authRequest = new AuthRequest<LinkedInAuthRequest>(
				linkedInAuthRequest);
		AuthResponse<LinkedInAuthResponse> authResponse = this
				.authentication(authRequest);

		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		try {
			if (type.toLowerCase().startsWith("group_posts")) {

				result.addAll(this.fetchGroupPostsFromLinkedIn(resource, from,
						to, authResponse));

			} else if (type.toLowerCase().startsWith("user_groups_posts")) {

				result.addAll(this.fetchAllUserGroupsPostsFromLinkedIn(
						resource, from, to, authResponse));

			} else if (type.toLowerCase().startsWith("user_companies_updates")) {

				result.addAll(this.fetchAllUserCompaniesUpdatesFromLinkedIn(
						resource, from, to, authResponse));

			} else if (type.toLowerCase().startsWith("company_updates")) {

				result.addAll(this.fetchAllCompanyUpdatesFromLinkedIn(resource,
						from, to, authResponse));

			}
		} catch (ParserConfigurationException | SAXException | IOException
				| ResourceURLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private List<AggregatorPost> fetchAllCompanyUpdatesFromLinkedIn(
			String resource, Date from, Date to,
			AuthResponse<LinkedInAuthResponse> authResponse) {

		/* Result AggregatorPost List */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		/* Company ID or Universal Name */
		String company = resource.substring(resource.lastIndexOf('/') + 1);
		try {
			if (company.length() > 0) { // non-empty company url field

				DocumentBuilder documentBuilder;
				documentBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				InputSource inputSource = new InputSource();
				Document document;

				/* Company by Universal Name --> Get ID */
				if (Pattern.matches("[a-zA-Z]+", company)) {

					String companyIdFromUniversalNameRequest = linkedInApiPrefix
							+ "companies/universal-name=" + company + ":(id)";
					OAuthRequest getCompanyIdFromUniversalName = new OAuthRequest(
							Verb.GET, companyIdFromUniversalNameRequest);
					authResponse
							.getAuthResponse()
							.getLinkedInService()
							.signRequest(
									authResponse.getAuthResponse()
											.getLinkedInOAuthToken(),
									getCompanyIdFromUniversalName);
					Response getPostsFromGroupResponse = getCompanyIdFromUniversalName
							.send();
					inputSource.setCharacterStream(new StringReader(
							getPostsFromGroupResponse.getBody()));
					document = documentBuilder.parse(inputSource);

					/* Company Id */
					company = document.getElementsByTagName("id").item(0)
							.getTextContent();

				}
				/*
				 * https://api.linkedin.com/v1/companies/{id}/ updates:(
				 * update-content:( company:(name), company-status-update:(
				 * share:( timestamp, content:( submitted-url, title,
				 * description)))), update-comments:( comment, person:(
				 * first-name, last-name), timestamp), likes)
				 * ?event-type=status-update
				 */
				String companyUpdatesFromLinkedInRequest = linkedInApiPrefix
						+ "companies/"
						+ company
						+ "/updates:(update-content:(company:(name),company-status-update:(share:(timestamp,content:(submitted-url,title,description)))),update-comments:(comment,person:(first-name,last-name),timestamp),likes)?event-type=status-update";
				OAuthRequest getCompanyUpdatesFromLinkedIn = new OAuthRequest(
						Verb.GET, companyUpdatesFromLinkedInRequest);
				authResponse
						.getAuthResponse()
						.getLinkedInService()
						.signRequest(
								authResponse.getAuthResponse()
										.getLinkedInOAuthToken(),
								getCompanyUpdatesFromLinkedIn);
				Response getCompanyUpdatesFromLinkedInResponse = getCompanyUpdatesFromLinkedIn
						.send();
				inputSource.setCharacterStream(new StringReader(
						getCompanyUpdatesFromLinkedInResponse.getBody()));
				document = documentBuilder.parse(inputSource);

				/* Company Updates List */
				NodeList companyUpdates = document
						.getElementsByTagName("update");
				/* Iterate through all Company Updates */
				for (int i = 0; i < companyUpdates.getLength(); i++) {

					AggregatorPost companyUpdate = new AggregatorPost();

					/* Update */
					Element update = (Element) companyUpdates.item(i);
					/* Update:(Update-Content)) */
					Element updateContent = (Element) update
							.getElementsByTagName("update-content").item(0);
					/* Update:(Update-Content:(Company-Status-Update)) */
					Element companyStatusUpdate = (Element) updateContent
							.getElementsByTagName("company-status-update")
							.item(0);
					/* Update:(Update-Content:(Company-Status-Update:(Share))) */
					Element companyStatusUpdateShare = (Element) companyStatusUpdate
							.getElementsByTagName("share").item(0);
					/*
					 * Update:(Update-Content:(Company-Status-Update:(Share:(
					 * Timestamp))))
					 */
					if (null != companyStatusUpdateShare.getElementsByTagName(
							"timestamp").item(0)) {
						postDate = dateformat
								.parse(dateformat.format(new Date(
										(new Timestamp(
												Long.parseLong(companyStatusUpdateShare
														.getElementsByTagName(
																"timestamp")
														.item(0)
														.getTextContent())))
												.getTime())));
					}
					companyUpdate.setDate(postDate);

					if ((postDate.after(from) || postDate.equals(from))
							&& (null == to || postDate.before(to) || postDate
									.equals(to))) {
						/*
						 * Update:(Update-Content:(Company-Status-Update:(Share:(
						 * Content))))
						 */
						Element companyStatusUpdateShareContent = (Element) companyStatusUpdateShare
								.getElementsByTagName("content").item(0);
						/*
						 * Update:(Update-Content:(Company-Status-Update:(Share:(
						 * Content:(Submitted-Url)))))
						 */
						/*if (null != companyStatusUpdateShareContent
								.getElementsByTagName("submitted-url").item(0)) {
							postLink = companyStatusUpdateShareContent
									.getElementsByTagName("submitted-url")
									.item(0).getTextContent();
						}*/
						companyUpdate.setLink(resource);//postLink);
						/*
						 * Update:(Update-Content:(Company-Status-Update:(Share:(
						 * Content:(Title)))))
						 */
						if (null != companyStatusUpdateShareContent
								.getElementsByTagName("title").item(0)) {
							postTitle = StringUtils.replaceChars(
									companyStatusUpdateShareContent
											.getElementsByTagName("title")
											.item(0).getTextContent(),
									"àèéìòù", "aeeiou");
						}
						companyUpdate.setTitle(postTitle);
						/*
						 * Update:(Update-Content:(Company-Status-Update:(Share:(
						 * Content:(Description)))))
						 */
						if (null != companyStatusUpdateShareContent
								.getElementsByTagName("description").item(0)) {
							postText = StringUtils
									.replaceChars(
											companyStatusUpdateShareContent
													.getElementsByTagName(
															"description")
													.item(0).getTextContent(),
											"àèéìòù", "aeeiou");
						}
						companyUpdate.setText(postText);
						/* Update:(Update-Content:(Company)) */
						Element updateContentCompany = (Element) updateContent
								.getElementsByTagName("company").item(0);
						/* Update:(Update-Content:(Company:(Name))) */
						if (null != updateContentCompany.getElementsByTagName(
								"name").item(0)) {
							postAuthor = StringUtils.replaceChars(
									updateContentCompany
											.getElementsByTagName("name")
											.item(0).getTextContent(),
									"àèéìòù", "aeeiou ");
						}
						companyUpdate.setAuthor(postAuthor);
						/* Update:(Update-Comments) */
						Element updateComments = (Element) update
								.getElementsByTagName("update-comments")
								.item(0);
						/* Update:(Update-Comments) Count */
						if (null != updateComments.getAttributes().item(0)) {
							postVisibility = Integer.parseInt(updateComments
									.getAttributes().item(0).getNodeValue());
						}
						/* Update:(Update-Comments:(Update-Comment)) List */
						NodeList updateCommentsList = updateComments
								.getElementsByTagName("update-comment");
						for (int j = 0; j < updateCommentsList.getLength(); j++) {
							AggregatorPost companyUpdateComment = new AggregatorPost();
							/* Update:(Update-Comments:(Update-Comment)) */
							Element updateComment = (Element) updateCommentsList
									.item(j);
							/*
							 * Update:(Update-Comments:(Update-Comment:(Comment))
							 * )
							 */
							if (null != updateComment.getElementsByTagName(
									"comment").item(0)) {
								commentText = StringUtils
										.replaceChars(
												updateComment
														.getElementsByTagName(
																"comment")
														.item(0)
														.getTextContent(),
												"àèéìòù", "aeeiou");
							} else {
								commentText = "";
							}
							companyUpdateComment.setText(commentText);
							/*
							 * Update:(Update-Comments:(Update-Comment:(Person)))
							 */
							Element updateCommentCreatorInfo = (Element) updateComment
									.getElementsByTagName("person").item(0);
							/*
							 * Update:(Update-Comments:(Update-Comment:(Person:(
							 * first-name))))
							 */
							if (null != updateCommentCreatorInfo
									.getElementsByTagName("first-name").item(0)) {
								commentAuthor = StringUtils.replaceChars(
										updateCommentCreatorInfo
												.getElementsByTagName(
														"first-name").item(0)
												.getTextContent(), "àèéìòù",
										"aeeiou");
							} else {
								commentAuthor = "";
							}
							/*
							 * Update:(Update-Comments:(Update-Comment:(Person:(last
							 * -name))))
							 */
							if (null != updateCommentCreatorInfo
									.getElementsByTagName("last-name").item(0)) {
								commentAuthor.concat(" "
										+ StringUtils.replaceChars(
												updateCommentCreatorInfo
														.getElementsByTagName(
																"last-name")
														.item(0)
														.getTextContent(),
												"àèéìòù", "aeeiou"));
							}
							companyUpdateComment.setAuthor(commentAuthor);
							/*
							 * Update:(Update-Comments:(Update-Comment:(Timestamp
							 * )))
							 */
							if (null != updateComment.getElementsByTagName(
									"timestamp").item(0)) {
								commentDate = dateformat.parse(dateformat
										.format(new Date((new Timestamp(Long
												.parseLong(updateComment
														.getElementsByTagName(
																"timestamp")
														.item(0)
														.getTextContent())))
												.getTime())));
							} else {
								commentDate = null;
							}
							companyUpdateComment.setDate(commentDate);
							companyUpdateComment
									.setVisibility(commentVisibility);
							companyUpdateComment.setSource(postSource);
							companyUpdateComment.setTitle(postTitle);
							companyUpdateComment.setLink(postLink);
							result.add(companyUpdateComment);
						}

						/* Update:(Likes) */
						Element updateLikes = (Element) update
								.getElementsByTagName("likes").item(0);
						/* Update:(Likes) Count */
						if (null != updateLikes
								&& null != updateLikes.getAttributes().item(0)) {
							postVisibility += Integer.parseInt(updateLikes
									.getAttributes().item(0).getNodeValue());
						}
						companyUpdate.setVisibility(postVisibility);
						companyUpdate.setSource(postSource);
						result.add(companyUpdate);
					} else {
						postDate = null;
						continue;
					}
				}

			}
		} catch (SAXException | IOException | ParserConfigurationException
				| ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	private List<AggregatorPost> fetchAllUserCompaniesUpdatesFromLinkedIn(
			String resource, Date from, Date to,
			AuthResponse<LinkedInAuthResponse> authResponse) {

		/* Result Aggregator Post List */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		/* LinkedIn API REST Request String */
		String userFollowedCompaniesUpdatesRequest = linkedInApiPrefix
				+ "people/~/following/companies:(id)";
		try {
			/* Build LinkedIn REST API Request */
			OAuthRequest getUserFollowedCompaniesUpdatesRequest = new OAuthRequest(
					Verb.GET, userFollowedCompaniesUpdatesRequest);
			/* Authorize LinkedIn REST API Request */
			authResponse
					.getAuthResponse()
					.getLinkedInService()
					.signRequest(
							authResponse.getAuthResponse()
									.getLinkedInOAuthToken(),
							getUserFollowedCompaniesUpdatesRequest);
			Response getUserFollowedCompaniesUpdatesResponse = getUserFollowedCompaniesUpdatesRequest
					.send();

			DocumentBuilder documentBuilder;

			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(
					getUserFollowedCompaniesUpdatesResponse.getBody()));
			Document document = documentBuilder.parse(inputSource);

			NodeList followedCompanies = document.getElementsByTagName("id");
			for (int i = 0; i < followedCompanies.getLength(); i++) {
				result.addAll(this.fetchAllCompanyUpdatesFromLinkedIn(
						"https://www.linkedin.com/company/"
								+ followedCompanies.item(i).getTextContent(),
						from, to, authResponse));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<AggregatorPost> fetchAllUserGroupsPostsFromLinkedIn(
			String resource, Date from, Date to,
			AuthResponse<LinkedInAuthResponse> authResponse)
			throws ParserConfigurationException, SAXException, IOException,
			ResourceURLFormatException {

		/* RESULT AGGREGATOR POST LIST */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();

		/* LINKEDIN REST API REQUEST STRING */
		String groupsFromUserRequest = linkedInApiPrefix
				+ "people/~/group-memberships:(group:(name,site-group-url))";

		/* BUILD LINKEDIN REST API REQUEST */
		OAuthRequest getGroupsFromUser = new OAuthRequest(Verb.GET,
				groupsFromUserRequest);

		/* AUTHORIZE REQUEST */
		authResponse
				.getAuthResponse()
				.getLinkedInService()
				.signRequest(
						authResponse.getAuthResponse().getLinkedInOAuthToken(),
						getGroupsFromUser);

		/* SEND REQUEST */
		Response getPostsFromGroupResponse = getGroupsFromUser.send();

		/* RESPONSE DOM */
		DocumentBuilder documentBuilder;
		documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(new StringReader(
				getPostsFromGroupResponse.getBody()));
		Document document = documentBuilder.parse(inputSource);

		/* GET ALL GROUPS */
		NodeList groupNodes = document.getElementsByTagName("group");

		/* ITERATE THROUGH GROUPS */
		for (int i = 0; i < groupNodes.getLength(); i++) {

			/* GET SINGLE GROUP */
			Element element = (Element) groupNodes.item(i);

			String groupURL = element.getElementsByTagName("site-group-url")
					.item(0).getTextContent();
			// System.out.println(groupURL);

			result.addAll(fetchGroupPostsFromLinkedIn(groupURL, from, to,
					authResponse));
		}

		return result;
	}

	/* Check if retrieve post data should be encapsulated in a method */

	private List<AggregatorPost> fetchGroupPostsFromLinkedIn(String resource,
			Date from, Date to, AuthResponse<LinkedInAuthResponse> authResponse)
			throws ParserConfigurationException, SAXException, IOException,
			ResourceURLFormatException {

		/* RESULT AGGREGATOR POST LIST */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();

		/* CHECK URL RESOURCE FORMAT */
		Pattern groupIdPattern = Pattern.compile("gid=([^&]+)");
		Matcher groupIdMatcher = groupIdPattern.matcher(resource);

		/* CORRECT URL */
		if (groupIdMatcher.find()) {

			/* LINKEDIN REST API REQUEST STRING */
			String postsFromGroupRequest = linkedInApiPrefix
					+ "groups/"
					+ groupIdMatcher.group().substring(4)
					+ ":(posts:(title,creator,summary,creation-timestamp,likes,comments,site-group-post-url))";

			/* BUILD LINKEDIN REST API REQUEST */
			OAuthRequest getPostsFromGroup = new OAuthRequest(Verb.GET,
					postsFromGroupRequest);

			/* AUTHORIZE REQUEST */
			authResponse
					.getAuthResponse()
					.getLinkedInService()
					.signRequest(
							authResponse.getAuthResponse()
									.getLinkedInOAuthToken(), getPostsFromGroup);

			/* SEND REQUEST */
			Response getPostsFromGroupResponse = getPostsFromGroup.send();

			/* RESPONSE DOM */
			DocumentBuilder documentBuilder;
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(
					getPostsFromGroupResponse.getBody()));
			Document document = documentBuilder.parse(inputSource);

			/* GET ALL POSTS */
			NodeList groupNodes = document.getElementsByTagName("post");

			/* ITERATE THROUGH POSTS */
			for (int i = 0; i < groupNodes.getLength(); i++) {

				/* GET SINGLE POST */
				Element element = (Element) groupNodes.item(i);
				
				/* POST DATE AFTER FROM PARAMETER DATE */
				if (null != element
						.getElementsByTagName("creation-timestamp").item(0)) {
					try {
						postDate = dateformat
								.parse(dateformat.format(new Date(
										(new Timestamp(
												Long.parseLong(element
														.getElementsByTagName("creation-timestamp").item(0)
														.getTextContent())))
												.getTime())));
					} catch (NumberFormatException | DOMException
							| ParseException e) {
							e.printStackTrace();
					}
				}
				if ((postDate.after(from) || postDate.equals(from))
						&& (null == to || postDate.before(to) || postDate
								.equals(to))) {

					/* NEW POST */
					AggregatorPost post = new AggregatorPost();

					/* POST TITLE */
					postTitle = StringUtils.replaceChars(element
							.getElementsByTagName("title").item(0)
							.getTextContent(), "àèéìòù", "aeeiou");
					post.setTitle(postTitle);

					/* POST AUTHOR */
					Element postCreatorInfo = (Element) element
							.getElementsByTagName("creator").item(0); // Ignore
																		// Multiple
																		// Authors
					postAuthor = postCreatorInfo
							.getElementsByTagName("first-name").item(0)
							.getTextContent()
							+ " "
							+ postCreatorInfo.getElementsByTagName("last-name")
									.item(0).getTextContent();
					post.setAuthor(postAuthor);

					/* POST DATE */
					try {
						postDate = dateformat
								.parse(dateformat.format(new Date(
										(new Timestamp(
												Long.parseLong(element
														.getElementsByTagName("creation-timestamp")
														.item(0).getTextContent()))).getTime())));
					} catch (NumberFormatException | DOMException
							| ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					post.setDate(postDate);

					/* POST TEXT */
					postText = StringUtils.replaceChars(element
							.getElementsByTagName("summary").item(0)
							.getTextContent(), "àèéìòù", "aeeiou");
					post.setText(postText);

					/* POST SOURCE */
					post.setSource(postSource);

					/* POST VISIBILITY */
					if( null != element
							.getElementsByTagName("comments").item(0) && null != element
									.getElementsByTagName("comments").item(0)
									.getAttributes().item(0) && null != element
									.getElementsByTagName("likes").item(0) && null != element
									.getElementsByTagName("likes").item(0)
									.getAttributes().item(0)){
						postVisibility = Integer.parseInt(element
								.getElementsByTagName("comments").item(0)
								.getAttributes().item(0).getNodeValue())
								+ Integer.parseInt(element
										.getElementsByTagName("likes").item(0)
										.getAttributes().item(0).getNodeValue());
					}
					
					post.setVisibility(postVisibility);

					/* POST LINK */
					postLink = element
							.getElementsByTagName("site-group-post-url")
							.item(0).getTextContent();
					post.setLink(postLink);

					/* ADD POST */
					result.add(post);
					NodeList postComments = element
							.getElementsByTagName("comment");
					for (int j = 0; j < postComments.getLength(); j++) {
						Element postComment = (Element) postComments.item(j);
						AggregatorPost comment = new AggregatorPost();
						/* Comment Title - same as related Post */
						comment.setTitle(postTitle);
						/* Comment Text */
						commentText = postComment
								.getElementsByTagName("text").item(0)
								.getTextContent();
						comment.setText(commentText);
						/* Comment Author */
						Element commentAuthorInfo = (Element) postComment
								.getElementsByTagName("creator").item(0);
						commentAuthor = commentAuthorInfo
								.getElementsByTagName("first-name").item(0)
								.getTextContent()
								+ " "
								+ commentAuthorInfo
										.getElementsByTagName("last-name")
										.item(0).getTextContent();
						comment.setAuthor(commentAuthor);
						/* Comment Creation Date */
						try {
							commentDate = dateformat
									.parse(dateformat.format(new Date(
											(new Timestamp(
													Long.parseLong(postComment
															.getElementsByTagName(
																	"creation-timestamp").item(0)
															.getTextContent()))).getTime())));
						} catch (NumberFormatException | DOMException
								| ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						comment.setDate(commentDate);
						/* Comment Visibility */
						comment.setVisibility(commentVisibility);
						/* Comment Source */
						comment.setSource(postSource);
						/* Comment link */
						comment.setLink(postLink);
						result.add(comment);
					}

				} else {
					postDate = null;
					continue;
				}
			}
		} else {
			/* Wrong URL format */
			throw new ResourceURLFormatException(
					"Wrong Resource URL Format ( Missing Group id )");
		}
		return result;

	}

	@Override
	public AuthResponse<LinkedInAuthResponse> authentication(
			AuthRequest<LinkedInAuthRequest> request) {
		/* Creates New LinkedIn Service Stub */
		OAuthService linkedInService = new ServiceBuilder()
				.provider(LinkedInApi.class)
				.apiKey(request.getAuthRequest().getApiKey())
				.apiSecret(request.getAuthRequest().getSecretKey()).build();
		/* Obtains New LinkedIn Authorization Token */
		Token linkedInOAuthToken = new Token(request.getAuthRequest()
				.getoAuthUserToken(), request.getAuthRequest()
				.getoAuthUserSecret());
		/* Returns New LinkedInAuthResponse */
		return new AuthResponse<LinkedInAuthResponse>(new LinkedInAuthResponse(
				linkedInService, linkedInOAuthToken));
	}
}
