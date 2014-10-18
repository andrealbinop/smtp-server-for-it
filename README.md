# smtp-server-for-it

A simple SMTP server for your integration testing needs. Based on [Spring Boot](http://projects.spring.io/spring-boot/) and [SubEtha SMT](https://code.google.com/p/subethasmtp/), it's perfect to use with browser automation tools such as [SeleniumIDE](http://docs.seleniumhq.org/), since received email messages can be instantly viewed with a HTTP request. This can be useful to test user registration flows that require activation through clicking links on email messages sent by the server.
 
**Download latest version

## Build and run

The build process requires [Gradle](http://www.gradle.org/).

1. Build:
    '$ gradle build'
2. Run:
    '$ java -jar build/libs/smtp-server-for-it.jar'

## Frequently asked questions

### 1. Can I use a different SMTP port? Is 25 the default port?

Yes and yes, to choose a different port run with the following args:
    '$ java -jar build/libs/smtp-server-for-it.jar --smtp.port=(other port)' 
    
### 2. How can I access the messages sent to the server?

Through HTTP, there are a few access options (considering that the SMTP started on localhost and listens HTTP connections on 8080 port):
- http://localhost:8080/email/last: Returns the content of the last sent mail to the SMTP server. 
- http://localhost:8080/email/\[0...n\]: Returns the content of the nth email sent to the server.