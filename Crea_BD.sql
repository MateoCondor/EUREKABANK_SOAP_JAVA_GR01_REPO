-- ============================================================
-- Script de inicialización de bases de datos para EUREKABANK
-- Arquitectura: Shared MySQL, Separated Databases (una por microservicio)
--
-- Ejecutar una sola vez antes de desplegar los microservicios.
-- El usuario MySQL debe tener permisos CREATE DATABASE.
-- ============================================================

-- 1. MS_CLIENTES: users, clients, parameters
CREATE DATABASE IF NOT EXISTS clientes_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 2. MS_CUENTAS: accounts
CREATE DATABASE IF NOT EXISTS cuentas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 3. MS_TRANSACCIONES: transactions
CREATE DATABASE IF NOT EXISTS transacciones_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- ============================================================
-- Permisos (ajusta el usuario 'root'@'localhost' según tu config)
-- ============================================================
-- GRANT ALL PRIVILEGES ON clientes_db.*      TO 'root'@'localhost';
-- GRANT ALL PRIVILEGES ON cuentas_db.*       TO 'root'@'localhost';
-- GRANT ALL PRIVILEGES ON transacciones_db.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================================
-- Verificación
-- ============================================================
SHOW DATABASES LIKE '%_db';
