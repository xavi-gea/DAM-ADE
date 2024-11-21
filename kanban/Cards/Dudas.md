Pero claro, como usuario client no puede acceder a "users" para saber su type y enviarle a un panel u otro.

¿Hay que determinar el rol de otra manera?

¿Hay que crear primero el usuario a nivel "global" con el CREATE y luego añadirlo también a la tabla users con el INSERT?

Está claro (más o menos).

Se intentará obtener el type del usuario. Si no se puede porque falla el select del type, se asumirá que es de type client.