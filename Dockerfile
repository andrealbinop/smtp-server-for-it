FROM andreptb/oracle-java:8

ENV JAVA_OPTS -Djava.security.egd=file:/dev/./urandom

RUN wget -q https://github.com/andreptb/smtp-server-for-it/releases/download/0.1.0/smtp-server-for-it.jar -O smtp-server-for-it.jar

CMD ["java", "-jar", "smtp-server-for-it.jar"]
