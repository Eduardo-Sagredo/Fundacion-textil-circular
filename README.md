# Fundacion-textil-circular

# Aplicación móvil Android diseñada para gestionar, evaluar y conectar a emprendedoras del rubro textil. El sistema permite el registro de perfiles, la evaluación de cuentas por parte de la administración y un canal de comunicación directo y estructurado entre las usuarias y el equipo de soporte técnico.

# Roles

* Administrador: Encargado de gestionar las solicitudes de nuevas cuentas (aprobar, rechazar, editar, eliminar) y brindar soporte técnico a través del sistema de mensajería.

* Emprendedora (Usuario): Usuaria del sistema con acceso a su perfil y la capacidad de abrir tickets de soporte para comunicarse con la administración.

# Dashboard administrador 

* Gestión de Cuentas: Paneles divididos para revisar solicitudes pendientes, cuentas activas y cuentas rechazadas.
* Acciones Administrativas: Herramientas integradas para editar perfiles, restablecer contraseñas y emitir motivos de rechazo.
* Bandeja de Entrada: Vista centralizada de todos los tickets y chats abiertos por las emprendedoras, ordenados por estado y actividad.

# Mensajería y Sistema de Tickets

* Tickets Únicos: Generación de chats con identificadores únicos basados en marcas de tiempo (timestamp) para segmentar las consultas de soporte.
* Restricción de Concurrencia: Bloqueo de creación de nuevos tickets si el usuario ya mantiene uno abierto y en curso.
* UI Reactiva: Burbujas de chat que adaptan su alineación y color dinámicamente según el rol de quien visualiza la pantalla.
* Respuestas Automáticas: Sistema de auto-respuesta genérica que se dispara de forma controlada únicamente en el primer mensaje recibido en un nuevo ticket.
* Cierre de Casos: Funcionalidad para que el administrador cierre el ticket, bloqueando nuevos mensajes y almacenando la conversación en "Mis conversaciones" como historial de solo lectura.

# Sincronización y Offline-First

* Almacenamiento Local: Guardado inmediato de mensajes y estados en caché local.
* Monitor de Red: Observación continua del estado de conexión a Internet.
* Estados de Mensaje: Indicadores visuales de "Pendiente" y "Enviado" en la interfaz gráfica.
* Sincronización Transparente: Sistema automatizado que detecta la reconexión a Internet y procesa el envío de los mensajes en cola utilizando bloqueos de concurrencia (Mutex) para evitar duplicados.

# Tecnologías Utilizadas

* Lenguaje: Kotlin
* Interfaz de Usuario: Jetpack Compose
* Base de Datos Local: Room
* Autenticación: Firebase Auth
* Programación Asíncrona: Coroutines, StateFlow

# Arquitectura

Arquitectura
Patrón de Interfaz: MVVM (Model-View-ViewModel)

Capa de Datos: Patrón Repository

* Reactividad: Flujos de datos unidireccionales desde la base de datos hasta la UI garantizando que la pantalla siempre refleje el estado real de la base de datos local.
* Capa de Datos: Patrón Repository
* Reactividad: Flujos de datos unidireccionales desde la base de datos hasta la UI garantizando que la pantalla siempre refleje el estado real de la base de datos local.

# Colaboradores

* Eduardo Sagredo
* Daniel Pinto
