juegoDeLaVida
=============

Aplicación en java sobre el Juego de la vida de John Horton Conway

Juego es un ejemplo de automatas celular de ceros jugadores en donde solo tienes un estado inicial y apartir de ese estado se van dando generaciones 
de acuerdo a dos condiciones:

  -Una célula muerta con exactamente 3 células vecinas vivas "nace" (al turno siguiente estará viva).
  -Una célula viva con 2 ó 3 células vecinas vivas sigue viva, en otro caso muere o permanece muerta (por "soledad" o "superpoblación").

La celula posee ocho celulas vecina que son la que estan alrededor de ella

0 0 0 
0 1 0
0 0 0

Ellas no mas tienen dos estados 'encendido' o 'viva' y 'apagado' o 'muerta' y el estado de todas evolucionan de acuerdo
a un tiempo en concreto.
