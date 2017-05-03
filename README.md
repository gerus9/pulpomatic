# Pulpomatic

Prueba técnica (Android)

## Capas
 
 | Packages | Descripción |
 | ------ | ------ |
 | base | Contiene todos la base que se utilizo para implementar el modelo MVP 
 | dialogs | Contiene los dialogs que se muestran en la aplicación
 | models | Son los modelos que representan la información 
 | navigator | Contiene la información de como se va a navegar entre la aplicación
 | notifications | Contiene el objeto que manipula las notificaciones mostradas por la aplicación 
 | services | Los servicios que se ejecutan en background en la aplicación
 | sharedPreferences | Contiene el diccionario que se guardan en los shared preferences
 | utils | Contiene archivos que pueden ser utiles en cualquier clase
 | view | Contiene todas las vistas dividas en paquetes por cada capa del modelo MVP
 
 
 ## Implementación
 
 * Vectores 
 * BuildTypes
 * Variants Outputs
 * ButterKnife
 * Gson
 * Animations
 
 ## Arquitectura
 
 La aplicación tiene un modelo MVP acoplado a la ciclo de vida del activity, por lo que en la carpeta de view se puede ver 
 como se implementa MVP en la actividad principal MapsActivity, los demas paquetes representa la esctructura modularizada, que 
 permite la reemplazar el módulo completo sin afectar directamente a otras capas de la aplicación.
 
 
 Desarrollador por:
  -  Gerus - 
  
  
