# 🗂️ Kanban Task Manager - Microservicios con Spring Boot

Este proyecto es un sistema de gestión de tareas estilo Kanban, diseñado con una arquitectura basada en microservicios. Cada servicio tiene una responsabilidad clara y se comunica con los demás mediante APIs RESTful. Los usuarios pueden gestionar **tableros (boards)** y **tareas (tasks)** dentro de cada tablero.

---

## 🧱 Arquitectura de Microservicios

- **🛠 config-server**: Centraliza la configuración de todos los servicios.
- **🔍 eureka**: Service discovery para registrar y ubicar servicios.
- **🌐 gateway**: Enrutador que actúa como punto de entrada único.
- **📋 board**: Gestión de tableros.
- **✅ task**: Gestión de tareas asociadas a tableros..

---

## ⚙️ Tecnologías Utilizadas

- Java 21
- Spring Boot
- Spring Cloud (Eureka, Config, Gateway)
- REST API (JSON)
- MySQL
- JPA/Hibernate
- Maven
- IntelliJ IDEA

---

## 🚀 Clonación y Ejecución del Proyecto

### 1. Clona el repositorio

```bash
git clone https://github.com/tu-usuario/kanban-task-manager.git
cd kanban-task-manager
```

### 2. Inicia la base de datos
```bash
    podman-compose --env-file .env up
```
Automáticamente se crean las bases de datos necesarias. 

### 3. Configura las variables de entorno
Configura estas variables en IntelliJ IDEA 
(Run > Edit Configurations > Environment Variables) o en tu sistema operativo.

#### cofig-server
```
CONFIG_SERVER_PORT=8888
```

#### eureka
```
EUREKA_PORT=8761
EUREKA_HOST=localhost
```

#### gateway
```
GATEWAY_SERVICE_PORT=8080
```

#### board
```
BOARD_SERVICE_PORT=8090
BOARD_DB_URI=localhost:3306/kanban_board_db
BOARD_DB_USERNAME=root
BOARD_DB_PASSWORD=tu_contraseña
BOARD_SERVICE_URL=http://localhost:8080/boards
```
#### task
```
TASK_SERVICE_PORT=9090
TASK_DB_URI=localhost:3306/kanban_task_db
TASK_DB_USERNAME=root
TASK_DB_PASSWORD=tu_contraseña
TASK_SERVICE_URL=http://localhost:8080/tasks
```


### 4. Ejecuta los servicios
Debes iniciar los servicios en un orden específico para que el **Service Discovery** y el **Config Server** funcionen correctamente.

1. **Inicia Config Server**  

2. **Inicia Eureka Server**  

3. **Inicia API Gateway**  

4. **Inicia Board Service**  

5. **Inicia Task Service**  


