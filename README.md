# DockerDeploy

Para desplegar el contenedor con la App Vaadin primero debe clonar el repositorio:

git clone https://github.com/StarkTiago-117/DockerDeploy

Debe dirigirse a la carpeta DockerDeploy/CebolliSport
Una vez allí solo debe construir el Docker con:
sudo docker build -t cebollisport:latest .

Se esperan aproximadamente: 1min 46sg si se despliega en la nube.
Se esperan aproximadamente: +7min 46sg si se despliega en local.

Una vez exitosa la construcción del contenedor, se ejecuta en el puerto 8080
(Si se monta en la nube, requiere abrir el puerto 8080)

sudo docker -d -p 8080:8080 cebollisport:latest

A partir de este momento se puede interactuar con la App Vaadin desde el localhost:8080
en el navegador de su preferencia.

Se puede verificar con:
sudo docker ps -a

