#Utilizamos multi-stage builds, donde el primer build se llama etapaUno
#En este primer build se genera el perfil de producción de la app de Vaadin
#Este perfil retorna un .jar con todo el FrontEnd y el BackEnd compilado
#Sirve como paso intermedio que descarga todas las dependencias necesarias
#De esta manera se deja el contenedor final lo más ligero posible
FROM eclipse-temurin:21-jdk AS etapaUno
WORKDIR /CebolliSportApp

#Se copia por completo la estructura de carpetas
COPY . .

#Se ejecuta el comando para generar el perfil de producción de una app Vaadin
RUN ./mvnw clean package -Pproduction


#-------------------------------//-----------------------------------


#Se genera el segundo build, del cual se trae solo lo necesario de etapaUno
#Esto se logra con la etiqueta --from=<nombreDelBuild> en el COPY
FROM eclipse-temurin:21-jre
WORKDIR /CebolliSportApp

#Se copia el .jar generado en el build anterior
COPY --from=etapaUno /CebolliSportApp/target/*.jar appFinal.jar

#Se traen los archivos planos de los que depende la estructura de la App
#Estos son su "Base de Datos" en archivos planos .txt
COPY --from=etapaUno /CebolliSportApp/Datos Datos/

#Las aplicaciones Vaadin corren en el puerto 8080, por lo tanto exponemos el 8080 para consistencia
EXPOSE 8080

#El entrypoint es como el CMD, esto simplemente ejecuta el comando en orden:
#java -jar appFinal.jar
#Eso para ejecutar el .jar generado desde el inicio.
ENTRYPOINT ["java", "-jar", "appFinal.jar"]
