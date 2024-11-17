La aplicación Java debe permitir realizar el "login" de usuarios. Si no se hace "login", no se podrá utilizar ninguna funcionalidad de la aplicación.

Como inicialmente solo hay un usuario disponible (admin), el primer "login" lo tendrás que hacer con este usuario. Para hacer el "login", recogerás el nombre de usuario (admin) y la contraseña (admin) a través de la interfaz gráfica y los utilizarás para hacer la conexión a la base de datos.

Antes de intentar hacer login, asegurarse de que se ha hecho grant al admin:

`GRANT ALL PRIVILEGES ON *.* TO 'admin' IDENTIFIED BY '21232f297a57a5a743894a0e4a801fc3' WITH GRANT OPTION;` 

Debes tener en cuenta que la cadena que recojas para la contraseña no puede ser el hash directamente (ningún usuario podría recordar su hash), sino que debes recoger la cadena en texto plano ("admin" en este caso) y generar el hash MD5 a través de la aplicación Java. Una vez tengas el hash, ya puedes hacer la conexión.