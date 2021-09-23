Pequeño ensayo en código del funcionamiento de un RoundRobin.

# Características
Soporta las siguientes características:
* Uso de reloj automático, con periodo configurable entre 100 ms y 5000 ms.
* Uso de reloj manual, con periodo configurable por comando.
* Ingreso de procesos con nombre y tiempo de ejecución.
* Enlistado automático de procesos activos.
* Cálculo automático del tiempo de retorno promedio y tiempo de espera promedio.

# Uso
Luego de compilar, se ejecuta el main desde la consola, de cualquiera de las siguientes dos formas:

`java Main <quantum> <periodo del reloj>`

`java Main <quantum>`

Donde `<quantum>` debe ser reemplazado por un entero mayor a cero, y `<periodo del reloj>` debe ser reemplazado por un entero entre 100 y 5000.

De forma tal que si se omite el `<periodo del reloj>`, se está ejecutando el simulador con reloj manual, y si se determina un periodo, se está ejecutando con reloj automático.
