Una vez hayas hecho "login" como "admin", la aplicación debe permitir registrar nuevos usuarios. Este registro solo podrá hacerlo "admin" y todos los usuarios que se registren tendrán solo permisos de lectura.

Para registrar un nuevo usuario, debes pedir "login", contraseña y confirmación de contraseña (se recomienda utilizar el componente JPassword de Java Swing para ocultar los caracteres y comprobar que las contraseñas coincidan antes de continuar).

Posteriormente, el nuevo usuario se guardará en la tabla "users" con el valor "client" en el campo "type" (ten cuidado de utilizar código que sea seguro contra inyecciones de SQL) y se le otorgarán los permisos de acceso correspondientes para que pueda realizar la conexión:

`CREATE USER 'nombreUsuario' IDENTIFIED BY 'hashMD5delPassword';`
`GRANT SELECT on population.population TO 'nombreUsuario';`

Puedes ver que la contraseña debe guardarse en la base de datos como un hash MD5 y que cuando el nuevo usuario se conecte a la base de datos tendrá solo permisos de lectura (SELECT) de la tabla "population" (en la base de datos "population"). Así, no podría acceder, por ejemplo, a la tabla "users".