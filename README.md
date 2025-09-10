# Taller de verificación de conocimientos técnicos 4
Este trabajo se trata de construir una *Calculadora Web para estimar la media y la desviación estándar de un conjunto de números*. La solución consta de un servidor backend que responde a solicitudes HTTP GET de la Facade, un servidor Facade que responde a solicitudes HTTP GET del cliente , y un cliente Html+JS que envía los comandos y muestra las respuestas.

# Instalación

Compilar el proyecto

```
mvn clean package
```
Ejecutar el archivo principal (Fachada)
```
mvn exec:java
```

# Pruebas

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

Comprobar que se limpio la lista

<img src=images/img_6.png />

## Probar Errores Http

* Metodo que no existe 

<img src=images/img_7.png />
