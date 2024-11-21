Siguiendo en el rol de administrador, la aplicación debe permitir importar el archivo CSV que se adjunta en la actividad para guardar los datos en la base de datos.

No puedes hacerlo directamente, sino que primero se debe extraer la información del encabezado del archivo para crear la tabla "population". Para evitar duplicidades, antes de crear la tabla debes borrar versiones anteriores (si existieran). 

Para simplificar, puedes asumir que todos los campos de la tabla son del tipo VARCHAR(30).