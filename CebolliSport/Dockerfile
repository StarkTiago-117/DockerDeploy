FROM eclipse-temurin:21-jdk AS etapaUno
WORKDIR /CebolliSportApp
COPY . .
RUN ./mvnw clean package -Pproduction


FROM eclipse-temurin:21-jre
WORKDIR /CebolliSportApp
COPY --from=etapaUno /CebolliSportApp/target/*.jar appFinal.jar
COPY --from=etapaUno /CebolliSportApp/Datos Datos/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "appFinal.jar"]
