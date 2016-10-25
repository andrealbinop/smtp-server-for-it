# smtp-server-for-it [![Build Status](https://travis-ci.org/andreptb/smtp-server-for-it.svg?branch=master)](https://travis-ci.org/andreptb/smtp-server-for-it)

A simple SMTP server for your integration testing needs. Based on [Spring Boot](http://projects.spring.io/spring-boot/) and [SubEtha SMT](https://code.google.com/p/subethasmtp/), it's perfect to use with browser automation tools such as [SeleniumIDE](http://docs.seleniumhq.org/), since received email messages can be instantly viewed with a HTTP request. This can be useful to test user registration flows that require activation through clicking links on email messages sent by the server. The project is released under [GNU 3](LICENSE).

## Docker Image

Supported tags and respective `Dockerfile` links:
  -	[`0.2.0`, `latest` (*andreptb/smtp-server-for-it*)](https://github.com/andreptb/smtp-server-for-it/blob/master/Dockerfile)  

## Running:
```
docker run -d -p 25:25 -p 8080:8080 andreptb/smtp-server-for-it
```

## Building and testing

The build process requires [Maven](http://maven.apache.org/).

1. Building:
    '$ mvn package'
2. Running:
    '$ java -jar target/smtp-server-for-it.jar'

## Frequently asked questions

### 1. How can I access the messages sent to the server?

Through HTTP, there are a few access options (considering that the SMTP started on localhost and listens HTTP connections on 8080 port):
- To access a given message metadata (id, subject, date, content type, from, to, cc, bcc, etc):
    - http://localhost:8080/email/\[-n...0...n|\]: Returns the metadata of the nth email message sent to the server, supporting negative offset.
- To access the message body:
    - http://localhost:8080/email/body/\[-n...0...n\]: Returns the body of the nth email message sent to the server, supporting negative offset.

### 2. Can I use a different SMTP or HTTP port?

* Running standalone jar:
```
java -jar build/libs/smtp-server-for-it.jar --smtp.port=(other smtp port) --server.port=(other http port)
```
* With docker the command would be:
```
docker run -d -p (other smtp port):25 -p (other http port):8080 andreptb/smtp-server-for-it
```
