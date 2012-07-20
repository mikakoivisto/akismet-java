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

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mika Koivisto
 */
public class AkismetIntegrationTest {

	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();

		InputStream is = this.getClass().getResourceAsStream("/akismet-test.properties");

		props.load(is);

		_apiKey = props.getProperty("api-key");

		_api = new Akismet(_apiKey, "http://www.test.com");

		_comment = new AkismetComment();

		_comment.setAuthor("Test");
		_comment.setAuthorEmail("test@test.com");
		_comment.setAuthorUrl("http://www.test.com");
		_comment.setPermalink("http://test.com/blog/post=1");
		_comment.setReferrer("http://www.test.com/blog");
		_comment.setType("comment");
		_comment.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1");
		_comment.setUserIp("127.0.0.1");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidApiKey() {
		Akismet api = new Akismet(_apiKey, "http://test.com");

		try {
			if (!api.verifyKey()) {
				fail("Invalid api key");
			}
		}
		catch (AkismetException ae) {
			fail("Api call failed with exception");
		}
	}

	@Test
	public void testInValidApiKey() {
		Akismet api = new Akismet("invalidkey", "http://some.blog.com");

		try {
			if (api.verifyKey()) {
				fail("Valid api key");
			}
		}
		catch (AkismetException ae) {
			fail("Api call failed with exception");
		}
	}

	@Test
	public void testCommentCheckSpam() {
		_comment.setAuthor("viagra-test-123");
		_comment.setContent("viagra-test-123");

		try {
			if (!_api.checkComment(_comment)) {
				fail("Failed to identify as spam");
			}
		}
		catch (AkismetException ae) {
			fail("Api call failed with exception");
		}
	}

	@Test
	public void testSubmitHam() {
		_comment.setAuthor("Innocent");
		_comment.setAuthorEmail("innocent@gmail.com");
		_comment.setContent("This is really innocent message.");
		_comment.setUserIp("192.168.1.1");

		try {
			_api.submitHam(_comment);
	
			if (_api.checkComment(_comment)) {
				fail("Failed to identify as ham");
			}
		}
		catch (AkismetException ae) {
			fail("Api call failed with exception");
		}
	}

	@Test
	public void testSubmitSpam() {
		_comment.setAuthor("I am spammy author");
		_comment.setContent("This is really really really spammy message. Buy viagra now. <a href=\"http://www.google.com\">Google</a>");
		_comment.setUserIp("127.0.0.1");

		try {
			_api.submitSpam(_comment);
	
			if (!_api.checkComment(_comment)) {
				fail("Failed to identify as spam");
			}
		}
		catch (AkismetException ae) {
			fail("Api call failed with exception");
		}
	}

	private Akismet _api;
	private String _apiKey;
	private AkismetComment _comment;
}
