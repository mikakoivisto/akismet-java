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

/**
 * @author Mika Koivisto
 */
public class AkismetComment {

	public static final String TYPE_COMMENT = "comment";
	public static final String TYPE_TRACKBACK = "trackback";
	public static final String TYPE_PINGBACK = "pingback";

	public String getAuthor() {
		return _author;
	}

	public String getAuthorEmail() {
		return _authorEmail;
	}

	public String getAuthorUrl() {
		return _authorUrl;
	}

	public String getContent() {
		return _content;
	}

	public String getPermalink() {
		return _permalink;
	}

	public String getReferrer() {
		return _referrer;
	}

	public String getType() {
		return _type;
	}

	public String getUserAgent() {
		return _userAgent;
	}

	public String getUserIp() {
		return _userIp;
	}

	public void setAuthor(String author) {
		_author = author;
	}

	public void setAuthorEmail(String authorEmail) {
		_authorEmail = authorEmail;
	}

	public void setAuthorUrl(String authorUrl) {
		_authorUrl = authorUrl;
	}

	public void setContent(String content) {
		_content = content;
	}

	public void setPermalink(String permalink) {
		_permalink = permalink;
	}

	public void setReferrer(String referrer) {
		_referrer = referrer;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setUserAgent(String userAgent) {
		_userAgent = userAgent;
	}

	public void setUserIp(String userIp) {
		_userIp = userIp;
	}

	private String _author;
	private String _authorEmail;
	private String _authorUrl;
	private String _content;
	private String _permalink;
	private String _referrer;
	private String _type;
	private String _userAgent;
	private String _userIp;

}
