# Taller de verificación de conocimientos técnicos 4 - Parcial
Este trabajo se trata de construir una *Calculadora Web para estimar la media y la desviación estándar de un conjunto de números*. La solución consta de un servidor backend que responde a solicitudes HTTP GET de la Facade, un servidor Facade que responde a solicitudes HTTP GET del cliente , y un cliente Html+JS que envía los comandos y muestra las respuestas.

# Funcionamiento

El trabajo tiene una fachada la cual funciona como un intermediario entre el cliente y el servidor, la fachada 
corre por el puerto 35000. Se encarga de traer el HTML y de gestionar las solicitudes GET, cuando recibe una solicitud se comunica con el servidor backend el cual
corre por el puerto 36000. Este servidor se encarga de gestionar la calculadora en memoria y de devolver la respuesta apropiada.

Así mismo, se modela la solicitud y respuesta mediante las clases HttpRequest y HttpResponse.

* HttpRequest: Maneja la solicitud entrante, guardando los parámetros y la URI.
* HttpResponse: Modela la respuesta HTTP, contiene el status, el status message (OK, No Content, etc) y
el cuerpo.

Así se tiene un manejo más organizado de las solicitudes y se pueden gestionar facilmente.

# Instalación

Compilar el proyecto

```
mvn clean package
```

Ejecutar el archivo principal (Facade)

```
java -cp target/classes edu.eci.arep.Facade
```

o directamente dando clic en Run

# Pruebas

El video se encuentra en la carpeta `video.zip`
* http://localhost:35000/add

<img src=images/img.png />
<img src=images/img_1.png />
<img src=images/img_2.png />

* http://localhost:35000/list

<img src=images/img_3.png />

* http://localhost:35000/stats

<img src=images/img_4.png />

* http://localhost:35000/clear

<img src=images/img_5.png />


## Probar Errores Http

* Metodo que no existe 

<img src=images/img_6.png />

* Servidor Backend no está corriendo

<img src=images/img_7.png />

* Parámetro inválido

<img src=images/img_8.png />

* Lista vacia al calcular stadisticas

<img src=images/img_9.png />

# Autor

Andrea Camila Torres González