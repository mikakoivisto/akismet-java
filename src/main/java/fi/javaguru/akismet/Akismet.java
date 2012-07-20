/**
 * Copyright (c) 2012 Mika Koivisto <mika@javaguru.fi>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package fi.javaguru.akismet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mika Koivisto
 */
public class Akismet {

	public Akismet(String apiKey, String blog) {
		if (apiKey == null) {
			throw new IllegalArgumentException("Api key can't be null");
		}

		_apiKey = apiKey;
		_blog = blog;
		_httpClient = new DefaultHttpClient();

		_commentCheckURL = "http://".concat(_apiKey).concat(".rest.akismet.com/1.1/comment-check");
		_submitSpamURL = "http://".concat(apiKey).concat(".rest.akismet.com/1.1/submit-spam");
		_submitHamURL = "http://".concat(apiKey).concat(".rest.akismet.com/1.1/submit-ham");
	}

	public boolean verifyKey() throws AkismetException {
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("key", _apiKey));
		nvps.add(new BasicNameValuePair("blog", _blog));

		String result = callApi(_verifyKeyURL, nvps);

		if ((result != null) && result.equals("valid")) {
			return true;
		}

		return false;
	}

	public boolean checkComment(AkismetComment comment) throws AkismetException {
		String result = callApi(_commentCheckURL, toNvp(comment));

		if (result.equals("true")) {
			return true;
		}

		return false;
	}

	public void submitHam(AkismetComment comment) throws AkismetException {
		String result = callApi(_submitHamURL, toNvp(comment));
	}

	public void submitSpam(AkismetComment comment) throws AkismetException {
		String result = callApi(_submitSpamURL, toNvp(comment));
	}

	protected String callApi(
			String url, List<NameValuePair> nvps)
		throws AkismetException {

		HttpPost httpPost = new HttpPost(url);

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = _httpClient.execute(httpPost);

			String server = null;
			String helpMessage = null;

			Header headerServer = response.getLastHeader("X-akismet-server");
			if (headerServer != null) {
				server = headerServer.getValue();
			}

			Header headerDebug = response.getLastHeader("X-akismet-debug-help");
			if (headerDebug != null) {
				helpMessage = headerDebug.getValue();
			}

			HttpEntity entity = response.getEntity();

			String body = EntityUtils.toString(entity);

			if (_log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				if (server != null) {
					sb.append("X-akismet-server: ");
					sb.append(server);
					sb.append(" ");
				}
				if (helpMessage != null) {
					sb.append("X-akismet-debug-help: ");
					sb.append(helpMessage);
					sb.append(" ");
				}
				if (body != null) {
					sb.append("response: " + body);
				}

				_log.debug(sb.toString());
			}

			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() != 200) {
				throw new AkismetException(
					"Request failed with status " + statusLine.toString());
			}

			if ((body != null) && !url.equals(_verifyKeyURL)) {
				if (body.equals(_INVALID_RESPONSE)) {
					throw new AkismetException(
						"Invalid response", server, helpMessage);
				}
			}

			return body;
		}
		catch (Exception e) {
			throw new AkismetException(e);
		}
		finally {
			httpPost.releaseConnection();
		}
	}

	protected List<NameValuePair> toNvp(AkismetComment comment) {
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();

		nvps.add(new BasicNameValuePair("blog", _blog));
		nvps.add(new BasicNameValuePair("user_ip", comment.getUserIp()));
		nvps.add(new BasicNameValuePair("user_agent", comment.getUserAgent()));
		nvps.add(new BasicNameValuePair("referrer", comment.getReferrer()));
		nvps.add(new BasicNameValuePair("permalink", comment.getPermalink()));
		nvps.add(new BasicNameValuePair("comment_type", comment.getType()));
		nvps.add(new BasicNameValuePair("comment_author", comment.getAuthor()));
		nvps.add(new BasicNameValuePair("comment_author_email", comment.getAuthorEmail()));
		nvps.add(new BasicNameValuePair("comment_author_url", comment.getAuthorUrl()));
		nvps.add(new BasicNameValuePair("comment_content", comment.getContent()));

		return nvps;
	}

	private static final Logger _log = LoggerFactory.getLogger(Akismet.class);
	private static final String _INVALID_RESPONSE = "invalid";
	private static final String _verifyKeyURL = "http://rest.akismet.com/1.1/verify-key";

	private String _apiKey;
	private String _blog;
	private HttpClient _httpClient;
	private String _commentCheckURL;
	private String _submitSpamURL;
	private String _submitHamURL;

}
