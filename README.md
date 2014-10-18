# smtp-server-for-it

A simple SMTP server for your integration testing needs. Based on [Spring Boot](http://projects.spring.io/spring-boot/) and [SubEtha SMT](https://code.google.com/p/subethasmtp/), it's perfect to use with browser automation tools such as [SeleniumIDE](http://docs.seleniumhq.org/), since received email messages can be instantly viewed with a HTTP request. This can be useful to test user registration flows that require activation through clicking links on email messages sent by the server. The project is released under [GNU 3](LICENSE).
 
## Build and run

The build process requires [Gradle](http://www.gradle.org/).

1. Build:
    '$ gradle build'
2. Run:
    '$ java -jar build/libs/smtp-server-for-it.jar'

## Frequently asked questions

### 1. How can I access the messages sent to the server?

Through HTTP, there are a few access options (considering that the SMTP started on localhost and listens HTTP connections on 8080 port):
- To access a given message metadata (id, subject, date, content type, from, to, cc, bcc, etc):
    - http://localhost:8080/email/\[0...n|\]: Returns the metadata of the nth email message sent to the server.
- To access the message body:
    - http://localhost:8080/email/body/\[0...n\]: Returns the body of the nth email message sent to the server.
    
**Important**: For both aforementioned URIs, the path param **last** can be used instead of the email index, returning the last email sent to the server.
   
### 2. Can I use a different SMTP port? Is 25 the default port?

Yes and yes, to choose a different port run with the following args:
    '$ java -jar build/libs/smtp-server-for-it.jar --smtp.port=(other port)'
     
### 3. Is it possible to change the HTTP port of the web server?

Yes, since the project is based on [Spring Boot](http://projects.spring.io/spring-boot/) and [SubEtha SMT](https://code.google.com/p/subethasmtp/), any web parameters can be changed through command line, as explained [here](docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-command-line-args). There are a lot of other parameters that can be configured from the command line, also explained [here](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).


 
 

