/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package fi.javaguru.akismet;

/**
 * @author Mika Koivisto
 */
public class AkismetException extends Exception {

	public AkismetException(Throwable throwable) {
		super(throwable);
	}

	public AkismetException(String message) {
		super(message);
	}

	public AkismetException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AkismetException(String message, String server, String helpMessage) {
		super(message);

		_server = server;
		_helpMessage = helpMessage;
	}

	public String getHelpMessage() {
		return _helpMessage;
	}

	public String getServer() {
		return _server;
	}

	private String _helpMessage;
	private String _server;
}
