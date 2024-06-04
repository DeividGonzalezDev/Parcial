# Parcial Esp. Backend II

## Roles

Los roles usados son compuestos; es decir, roles de reino que estan compuestos por su respectivo rol cliente

Existen 3 roles
- ADMIN: Accesso a todos lo endpoints
  - localhost:9090/elaparato/*
  - localhost:9090/admin/elaparato/*
- VENDEDOR: Acceso a los endpoints de venta
  - localhost:9090/elaparato/ventas/*
- REPOSITOR: Acceso a los endpoints de productos
  - localhost:9090/elaparato/productos/*

## Arquitectura de la Aplicación
La aplicación está compuesta por varios componentes que interactúan entre sí para gestionar la autenticación, autorización y acceso a los recursos. A continuación se describe la arquitectura en detalle:

### Gateway (Puerto 9090)
El Gateway actúa como un punto de entrada para la aplicación y es un cliente de Keycloak, usando el flujo normal de Authorization Code. Este flujo se utiliza para autenticar a los usuarios finales.

#### Flujo de Authorization Code
- Usuario solicita acceso: El usuario accede a una URL protegida en el Gateway.
- Redirección a Keycloak: El Gateway redirige al usuario a Keycloak para autenticarse.
- Autenticación en Keycloak: El usuario ingresa sus credenciales en la página de inicio de sesión de Keycloak.
- Redirección de vuelta al Gateway: Keycloak redirige de vuelta al Gateway con un código de autorización.
- Intercambio del código por un token: El Gateway intercambia el código de autorización por un token de acceso y un token de refresco.
- Acceso a recursos: El Gateway utiliza el token de acceso para autenticar y autorizar las solicitudes del usuario a los servicios backend.

### Servicio 'el-aparato' (Puerto 8086)
Este servicio maneja la lógica de ventas y productos y funciona como un Resource Server. Cuenta con un conversor de JWT que valida y procesa el token JWT recibido del Gateway.

#### Funciones principales:
- Ventas: Gestiona las operaciones relacionadas con las ventas.
- Productos: Gestiona las operaciones relacionadas con los productos.

### Servicio 'keyloack-admin' (Puerto 8081)
El servicio 'keyloack-admin' permite la gestión de usuarios y está protegido por el flujo de Client Credentials. Es un cliente de Keycloak y solo puede ser accedido por administradores.

#### Flujo de Client Credentials
- Solicitud de token: El servicio 'keyloack-admin' solicita un token de acceso a Keycloak utilizando sus credenciales de cliente.
- Recepción de token: Keycloak devuelve un token de acceso al servicio 'keyloack-admin'.
- Acceso a recursos administrativos: El servicio utiliza este token para realizar operaciones administrativas como ver, guardar y eliminar usuarios.

### Detalle del Flujo y Funcionamiento

- Autenticación del Usuario Final: Cuando un usuario intenta acceder a la aplicación, es redirigido a Keycloak para autenticarse. Una vez autenticado, el Gateway recibe un token de acceso.
- Autorización y Redirección: El Gateway verifica el token y redirige la solicitud al servicio correspondiente ('el-aparato' o 'keyloack-admin') según el rol del usuario.
- Gestión de Recursos:
  - Los usuarios con rol VENDEDOR pueden acceder a los endpoints de ventas.
  - Los usuarios con rol REPOSITOR pueden acceder a los endpoints de productos.
  - Los usuarios con rol ADMIN tienen acceso completo a todos los recursos y al servicio administrativo.
- Gestión Administrativa: El servicio 'keyloack-admin' permite a los administradores gestionar usuarios, utilizando el flujo de Client Credentials para obtener un token de acceso directamente desde Keycloak.
