Akismet Java API
======================

This is a java implementation of Akismet comment spam checking api. Signup for your api key at http://akismet.com.

## Requirements

* SLF4J binding

## Install

1. First clone this repository to a directory of you choice

	$ git clone git://github.com/mikakoivisto/akismet-java.git

2. Install to your local maven repository

	$ mvn clean install

3. Verify that your api key works

	$ mvn test -Dakismet.api.key=YOUR_KEY

4. Define as dependency in your application

<dependency>
	<groupId>fi.javaguru.akismet</groupId>
	<artifactId>akismet-java</artifactId>
</dependency>

Remember to add your preferred SLF4J biding for logging. 

## Usage

The api implements all Akismet api methods: verify-key, comment-check, submit-spam and submit-ham. The api parameters are encapsulated in AkismetComment object.
See api usage examples from the AkismetIntegrationTest class.

