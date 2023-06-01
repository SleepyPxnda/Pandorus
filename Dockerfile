#
# Build stage
#
FROM maven:3.9.2-eclipse-temurin-20-alpine AS build
COPY core /home/app/core
COPY database /home/app/database
COPY serverinfo /home/app/serverinfo
COPY tempchannel /home/app/tempchannel
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install

#
# Package stage
#
FROM openjdk:20
COPY --from=build /home/app/core/target/core-0.0.1-SNAPSHOT-exec.jar /usr/local/lib/core.jar
COPY --from=build /home/app/database/target/database-0.0.1-SNAPSHOT-exec.jar /usr/local/lib/database.jar
COPY --from=build /home/app/serverinfo/target/serverinfo-0.0.1-SNAPSHOT-exec.jar /usr/local/lib/serverinfo.jar
COPY --from=build /home/app/tempchannel/target/tempchannel-0.0.1-SNAPSHOT-exec.jar /usr/local/lib/tempchannel.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/core.jar"]