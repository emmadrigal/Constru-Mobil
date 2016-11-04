/*
Lista de Cambios:
Se eliminaron las tablas de cliente, proveedor y empleado y ser reemplazó por una tabla usuario
Se creo una tabla de Rol
Se creo una tabla de relación entre Usuario y Rol
Cambio de tipos de los teléfonos de varchar a int
*/

CREATE DATABASE EPATEC
GO
CREATE LOGIN emmanuel   
    WITH PASSWORD = 'password';  
GO  

ALTER DATABASE EPATEC SET  READ_WRITE 
GO

USE EPATEC
GO
 
-- Creates a database user for the login created above.  
CREATE USER emmanuel FOR LOGIN emmanuel;  
GO  

EXEC sp_addrolemember N'db_datareader', N'emmanuel'
EXEC sp_addrolemember N'db_datawriter', N'emmanuel'
GO

CREATE TABLE CATEGORIA(
	Nombre varchar (50) NOT NULL PRIMARY KEY ,
	Descripción varchar(max)
)
GO

CREATE TABLE USUARIO(
	Cedula bigint NOT NULL PRIMARY KEY,
	Nombre varchar(max) NOT NULL,
	Apellidos varchar(max) NOT NULL,
	Grado_de_Penalizacion tinyint NULL,
	Lugar_de_Residencia varchar(max),
	Fecha_de_Nacimiento date,
	Telefono int
)
GO

CREATE TABLE ROL(
	Id_rol bigint PRIMARY KEY IDENTITY(1,1),
	nombre varchar(max)
)
GO

CREATE TABLE ROL_USUARIO(
	id_Rol_Usuario bigint PRIMARY KEY IDENTITY(1,1),
	usuario bigint FOREIGN KEY REFERENCES USUARIO(Cedula),
	rol bigint FOREIGN KEY REFERENCES ROL(Id_rol)
)
GO

CREATE TABLE SUCURSAL(
	Id_Sucursal bigint NOT NULL PRIMARY KEY 
)
GO

CREATE TABLE EmpleadoSucursal(
	Id_EmpleadoSucursal bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	id_emplado bigint FOREIGN KEY REFERENCES USUARIO(Cedula),
	id_sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal)
)
GO

CREATE TABLE PRODUCTO(
	Nombre_Producto varchar(30) NOT NULL PRIMARY KEY ,
	Id_Sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal),
	Cedula_Provedor bigint FOREIGN KEY REFERENCES USUARIO(Cedula),
	Nombre_Categoría varchar(50) FOREIGN KEY REFERENCES CATEGORIA(Nombre),
	Descripción varchar(max),
	Exento bit,
	Cantidad_Disponible int NOT NULL,
	Precio int
)
GO

CREATE TABLE PEDIDO(
	Id_Pedido bigint NOT NULL IDENTITY(1,1) PRIMARY KEY,
	Cedula_Cliente bigint FOREIGN KEY REFERENCES USUARIO(Cedula),
	Id_Sucursal bigint FOREIGN KEY REFERENCES SUCURSAL(Id_Sucursal),
	Telefono_Preferido int NOT NULL,
	Hora_de_Creación datetime NOT NULL
)
GO

CREATE TABLE CONTIENE (
	id_Contiene bigint PRIMARY KEY IDENTITY(1,1),
	Nombre_Producto varchar(30) FOREIGN KEY REFERENCES PRODUCTO(Nombre_Producto),
	Id_Pedido bigint FOREIGN KEY REFERENCES PEDIDO(Id_Pedido),
	Cantidad int
)
GO

