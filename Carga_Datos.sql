/*
Empresa        : EurekaBank
Script         : Carga de datos para Microservicios
Descripción    : Inserta los datos históricos divididos en las 3 bases de datos.
*/

SET NAMES utf8mb4;

-- ============================================================
-- 1. BASE DE DATOS: clientes_db
-- ============================================================
USE clientes_db;

DELETE FROM clients WHERE ID > 0;
DELETE FROM users WHERE USERNAME != 'MONSTER';
DELETE FROM parameters WHERE ID > 0;

ALTER TABLE clients AUTO_INCREMENT = 1;
ALTER TABLE parameters AUTO_INCREMENT = 1;

-- Los passwords han sido hasheados usando el nuevo PasswordUtil (salt:hash)
-- El password original para todos los usuarios era '123456'
INSERT INTO users (ID, PASSWORD, ROLE, STATUS, USERNAME) VALUES
(2,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00001'),
(3,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00003'),
(4,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00004'),
(5,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00005'),
(6,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00006'),
(7,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00007'),
(8,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00008'),
(9,  'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00009'),
(10, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00010'),
(11, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00011'),
(12, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00012'),
(13, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00013'),
(14, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00014'),
(15, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00015'),
(16, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00016'),
(17, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00017'),
(18, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00018'),
(19, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00019'),
(20, 'rs72j3vEFfdpiodh0n75Rw==:eN5OIpPwm43scDciwgRWeONNwiqWlkX+g74BIv6Ovdw=', 'USER', 'ACTIVE', 'cli00020');

INSERT INTO clients (ID, DNI, EMAIL, NAME, PHONE, STATUS, user_id) VALUES
(1,  '06914897', 'gcoronel@viabcp.com',         'CORONEL CASTILLO ERIC GUSTAVO',      '9666-4457',  'ACTIVE', 2),
(2,  '01576173', 'pvalencia@terra.com.pe',      'VALENCIA MORALES PEDRO HUGO',         '924-7834',   'ACTIVE', 3),
(3,  '06531983', 'c.romero@hotmail.com',        'ROMERO CASTILLO CARLOS ALBERTO',      '865-84762',  'ACTIVE', 4),
(4,  '10875611', 'a.aranda@hotmail.com',        'ARANDA LUNA ALAN ALBERTO',            '834-67125',  'ACTIVE', 5),
(5,  '10679245', 'j.ayala@yahoo.com',           'AYALA PAZ JORGE LUIS',                '963-34769',  'ACTIVE', 6),
(6,  '10145693', 'e.chavez@gmail.com',          'CHAVEZ CANALES EDGAR RAFAEL',         '999-96673',  'ACTIVE', 7),
(7,  '10773456', 'r.florez@hotmail.com',        'FLORES CHAFLOQUE ROSA LIZET',         '966-87567',  'ACTIVE', 8),
(8,  '10346723', 'c.flores@hotmail.com',        'FLORES CASTILLO CRISTIAN RAFAEL',     '978-43768',  'ACTIVE', 9),
(9,  '10192376', 'g.gonzales@yahoo.es',         'GONZALES GARCIA GABRIEL ALEJANDRO',   '945-56782',  'ACTIVE', 10),
(10, '10942287', 'j.lay@peru.com',              'LAY VALLEJOS JUAN CARLOS',            '956-12657',  'ACTIVE', 11),
(11, '10612376', 'd.montalvo@hotmail.com',      'MONTALVO SOTO DEYSI LIDIA',           '965-67235',  'ACTIVE', 12),
(12, '10761324', 'r.ricalde@gmail.com',         'RICALDE RAMIREZ ROSARIO ESMERALDA',   '991-23546',  'ACTIVE', 13),
(13, '10773345', 'e.rodriguez@gmail.com',       'RODRIGUEZ FLORES ENRIQUE MANUEL',     '976-82838',  'ACTIVE', 14),
(14, '10238943', 'f.rojas@yahoo.com',           'ROJAS OSCANOA FELIX NINO',            '962-32158',  'ACTIVE', 15),
(15, '10446791', 't.tejada@hotmail.com',        'TEJADA DEL AGUILA TANIA LORENA',      '966-23854',  'ACTIVE', 16),
(16, '10452682', 'r.valdivieso@terra.com.pe',   'VALDEVIESO LEYVA LIDIA ROXANA',       '956-78951',  'ACTIVE', 17),
(17, '10398247', 'j.valentin@terra.com.pe',     'VALENTIN COTRINA JUAN DIEGO',         '921-12456',  'ACTIVE', 18),
(18, '10934584', 'y.yauricasa@terra.com.pe',    'YAURICASA BAUTISTA YESABETH',         '977-75777',  'ACTIVE', 19),
(19, '10772365', 'f.zegarra@hotmail.com',       'ZEGARRA GARCIA FERNANDO MOISES',      '936-45876',  'ACTIVE', 20);

-- ============================================================
-- 2. BASE DE DATOS: cuentas_db
-- ============================================================
USE cuentas_db;

DELETE FROM accounts WHERE ID > 0;
ALTER TABLE accounts AUTO_INCREMENT = 1;

INSERT INTO accounts (ID, ACCOUNTNUMBER, BALANCE, STATUS, TYPE, client_id) VALUES
(1, '00200001', 7000.00, 'ACTIVE',    'SAVINGS', 1),
(2, '00200002', 6800.00, 'ACTIVE',    'SAVINGS', 2),
(3, '00200003', 6000.00, 'ACTIVE',    'SAVINGS', 7),
(4, '00100001', 6900.00, 'ACTIVE',    'SAVINGS', 5),
(5, '00100002', 4500.00, 'ACTIVE',    'SAVINGS', 5),
(6, '00300001',    0.00, 'ACTIVE', 'SAVINGS', 10);


-- ============================================================
-- 3. BASE DE DATOS: transacciones_db
-- ============================================================
USE transacciones_db;

DELETE FROM transactions WHERE ID > 0;
ALTER TABLE transactions AUTO_INCREMENT = 1;

INSERT INTO transactions (AMOUNT, DATE, DESCRIPTION, FEE, transfer_type, TYPE, source_account_id, target_account_id) VALUES
(1800.00, '2022-01-08 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 4, NULL),
(1000.00, '2022-01-25 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  4, NULL),
(2200.00, '2022-02-13 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 4, NULL),
(1500.00, '2022-03-08 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 4, NULL),

(2800.00, '2022-01-06 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 5, NULL),
(3200.00, '2022-01-15 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 5, NULL),
( 800.00, '2022-01-20 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  5, NULL),
(2000.00, '2022-02-14 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 5, NULL),
( 500.00, '2022-02-25 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  5, NULL),
( 800.00, '2022-03-03 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  5, NULL),
(1000.00, '2022-03-15 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 5, NULL),

(2500.00, '2022-01-11 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 3, NULL),
(1500.00, '2022-01-17 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 3, NULL),
( 500.00, '2022-01-20 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  3, NULL),
( 500.00, '2022-02-09 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  3, NULL),
(3500.00, '2022-02-25 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 3, NULL),
( 500.00, '2022-03-11 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  3, NULL),

(3800.00, '2022-01-09 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 2, NULL),
(4200.00, '2022-01-20 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 2, NULL),
(1200.00, '2022-03-06 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  2, NULL),

(5000.00, '2022-01-05 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 1, NULL),
(4000.00, '2022-01-07 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(2000.00, '2022-01-09 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(1000.00, '2022-01-11 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(2000.00, '2022-01-13 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(4000.00, '2022-01-15 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(2000.00, '2022-01-19 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(3000.00, '2022-01-21 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(7000.00, '2022-01-23 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(1000.00, '2022-01-27 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(3000.00, '2022-01-30 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(2000.00, '2022-02-04 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(4000.00, '2022-02-08 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),
(2000.00, '2022-02-13 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 1, NULL),
(1000.00, '2022-02-19 00:00:00.000000', 'Retiro',             NULL, NULL, 'WITHDRAW',  1, NULL),

(5600.00, '2022-01-07 00:00:00.000000', 'Apertura de Cuenta', NULL, NULL, 'DEPOSIT', 6, NULL),
(1400.00, '2022-01-18 00:00:00.000000', 'Deposito',           NULL, NULL, 'DEPOSIT', 6, NULL),
(7000.00, '2022-01-25 00:00:00.000000', 'Cancelar Cuenta',    NULL, NULL, 'WITHDRAW',  6, NULL);
