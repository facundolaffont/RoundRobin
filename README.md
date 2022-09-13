# Descripción
Pequeño ensayo del funcionamiento de un planificador de corto plazo de tipo RoundRobin.

# Características
* Uso de reloj automático, con periodo configurable entre 100 ms y 5000 ms.
* Uso de reloj manual, con periodo configurable por comando.
* Ingreso de procesos con nombre y tiempo de ejecución.
* Enlistado automático de procesos activos.
* Cálculo automático del tiempo de retorno promedio y tiempo de espera promedio.

# Uso
Luego de compilar, se ejecuta el main desde la consola, de cualquiera de las siguientes dos formas:

```sh
java Main <quantum> <periodo del reloj>
```

```sh
java Main <quantum>
```

Donde `<quantum>` debe ser reemplazado por un entero mayor a cero, y `<periodo del reloj>` debe ser reemplazado por un entero entre 100 y 5000.

Si se omite el `<periodo del reloj>`, se está ejecutando el simulador con reloj manual, y si se determina un periodo, se está ejecutando con reloj automático.

# Vista de la aplicación
<p align="center">
  <img src="https://github.com/facundolaffont/SimuladorDeRoundRobin/blob/main/img/Reloj%20autom%C3%A1tico.jpg?raw=true"/>
</p>

# Pendiente
+ Agregar ícono.
+ Obtener una mejor captura de pantalla.
