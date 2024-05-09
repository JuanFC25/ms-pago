FROM eclipse-temurin:17
LABEL maintainer="mdomingu@mail.com"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} dan-pago.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/dan-pago.jar"]