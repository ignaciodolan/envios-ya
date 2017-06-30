# EnviosYa

### Instalación
 **Repositorio**
- cd [carpeta destino]
- git clone git@github.com:ArqSoftCourse/m7a-obli-CarusoDolanGuichon.git

**Proyecto**

1) 
Se deben crear 5 bases de datos: clients, cadets, login, reviews, shipments

2)
crear pools:
//Client pool
create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property portNumber=3306:password=root:user=root:serverName=localhost:databaseName=clients:ServerName=localhost:port=3306 ClientPool

//Shipment pool
create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property portNumber=3306:password=root:user=root:serverName=localhost:databaseName=shipments:ServerName=localhost:port=3306 ShipmentPool

//Cadet pool
create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property portNumber=3306:password=root:user=root:serverName=localhost:databaseName=cadets:ServerName=localhost:port=3306 Cadet

//Review pool
create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property portNumber=3306:password=root:user=root:serverName=localhost:databaseName=cadets:ServerName=localhost:port=3306 Cadet

3) crea resource

create-jdbc-resource --connectionpoolid ClientPool jdbc/clients
create-jdbc-resource --connectionpoolid ShipmentPool jdbc/shipments
create-jdbc-resource --connectionpoolid Cadet jdbc/cadets
create-jdbc-resource --connectionpoolid Login jdbc/login
create-jdbc-resource --connectionpoolid Reviews jdbc/reviews

4) Crear Queues
create-jms-resource --restype javax.jms.Queue --property Name=Queue jms/Queue

jms/QueueCheckClient
jms/QueueCheckDuplicates
jms/QueueAddReview
jms/QueueCheckComment
jms/QueueCheckWords
jms/QueueCheckSemantic


### Desarrollo
**GIT**
Por consola:
```sh
cd [carpeta destino]
git checkout -b "[nombre rama nueva]"
```
*Se trabaja en el codigo*
```sh
git add 'archivo cambiado' o git add . (para agregar todo)
git commit -m "Mensaje con descripcion de cambios realizados"
git push -u origin [nombre rama nueva]
```
Luego para crear Pull Requests se va al proyecto en github y se crea desde ahi.
