## **BPM EUREKA BANK CON SOAP EN JAVA** 

Una institución bancaria requiere **automatizar y estandarizar sus procesos operativos** relacionados con las transacciones financieras básicas de sus clientes. Actualmente, los sistemas que realizan **consultas de movimientos, depósitos, retiros y transferencias** se encuentran disponibles como **servicios web SOAP** , pero **no existe un proceso de negocio orquestado** que: 

- Controle el flujo de cada operación 

- Valide reglas de negocio 

- Gestione errores 

- Registre auditoría 

- Coordine la interacción entre el cliente y los servicios bancarios 

## **PROBLEMA A RESOLVER** 

Diseñar e implementar un Proceso de Negocio (BPM) que interactúe con un servicio web SOAP bancario, permitiendo gestionar de forma segura y controlada las operaciones de: 

- Consulta de movimientos 

- Depósitos 

- Retiros 

- Transferencias 

Utilizando  notación  BPMN  2.0,  asegurando  validaciones,  control  transaccional  y  manejo  de excepciones. 

## **ACTORES Y COMPONENTES DEL SISTEMA** 

Los actores BPMN (Pools y Lanes) se detallan a continuación 

## **POOL PRINCIPAL:** 

Proceso BPM – Gestión de Operaciones Bancarias 

## **LANES INTERNAS:** 

- Cliente 

- Motor BPM 

- Servicio Web SOAP Bancario 

- Sistema de Auditoría 

## **SERVICIO WEB SOAP (CONTEXTO)** 

El servicio SOAP expone las siguientes operaciones: 

- consultarMovimientos(cuenta) 

- realizarDeposito(cuenta, monto) 

- realizarRetiro(cuenta, monto) 

- realizarTransferencia(cuentaOrigen, cuentaDestino, monto) 

El BPM no ejecuta la lógica bancaria, sino que: 

- Orquesta 

- Valida 

- Controla el flujo 

- Consume el servicio SOAP 

## **SOLUCIÓN PROPUESTA USANDO BPMN DESCRIPCIÓN GENERAL DEL PROCESO** 

1. El cliente solicita una operación bancaria 

2. El BPM valida datos y reglas de negocio 

3. El BPM invoca el servicio SOAP correspondiente 

4. Se analiza la respuesta 

5. Se registra la transacción 

6. Se notifica el resultado al cliente 

## **MODELO BPMN – FLUJO PRINCIPAL** 

## **EVENTO DE INICIO** 

## **Evento de Inicio** 

- Tipo: Message Start Event 

- Mensaje: SolicitudOperacionBancaria 

## **ACTIVIDADES INICIALES** 

## **Tarea: Capturar solicitud** 

- Tipo: User Task 

- Datos: tipo de operación, cuentas, monto 

## **Tarea: Validar datos de entrada** 

- Tipo: Service Task 

- Validaciones: 

   - Cuenta válida 

   - Monto > 0 

   - Cuentas distintas (transferencia) 

## **GATEWAY EXCLUSIVO (XOR)** 

**Gateway: Tipo de operación** Decide el flujo según la operación solicitada: 

- Consulta de movimientos 

- Depósito 

- Retiro 

- Transferencia 

## **SUBPROCESOS BPMN POR OPERACIÓN** 

## **CONSULTA DE MOVIMIENTOS** 

## **Service Task: Invocar SOAP – consultarMovimientos** 

- Operación SOAP 

- Entrada: número de cuenta 

## **Task: Procesar respuesta** 

- Formatear datos 

- Preparar resultado 

## **DEPÓSITO** 

**Service Task: Invocar SOAP – realizarDeposito**  Validar monto máximo permitido 

**Task: Confirmar depósito** 

## **RETIRO** 

**Service Task: Verificar saldo** 

- Regla de negocio 

**Gateway XOR: ¿Saldo suficiente?** 

- NO → Error 

- SÍ → continuar 

**Service Task: Invocar SOAP – realizarRetiro** 

## **TRANSFERENCIA** 

**Service Task: Verificar saldo cuenta origen Gateway XOR: ¿Saldo suficiente? Service Task: Invocar SOAP – realizarTransferencia** 

## **MANEJO DE ERRORES (BPMN AVANZADO)** 

## **EVENTOS INTERMEDIOS DE ERROR** 

**Error Boundary Event** adjunto a cada Service Task SOAP: 

- Servicio no disponible 

- Error de negocio 

- Timeout 

**Task: Registrar error Task: Notificar fallo al cliente** 

## **AUDITORÍA Y TRAZABILIDAD** 

## **SERVICE TASK: REGISTRAR TRANSACCIÓN** 

- Base de datos 

- Logs 

- Cumplimiento normativo 

Se ejecuta **en paralelo** usando: **Gateway paralelo (AND)** 

## **EVENTO DE FIN** 

## **EVENTO DE FIN** 

- Tipo: Message End Event 

- Mensaje: `ResultadoOperacion` 

## **REPRESENTACIÓN TEXTUAL BPMN SIMPLIFICADA** 

```
(Start)
   ↓
[Capturar Solicitud]
   ↓
[Validar Datos]
   ↓
<XOR Tipo Operación>
   ├── Consulta → [SOAP consultarMovimientos]
   ├── Depósito → [SOAP realizarDeposito]
   ├── Retiro → [Validar Saldo] → [SOAP realizarRetiro]
   └── Transferencia → [Validar Saldo] → [SOAP realizarTransferencia]
   ↓
[Registrar Auditoría]
   ↓
[Notificar Cliente]
   ↓
(End)
```

## **BENEFICIOS DE LA SOLUCIÓN BPM + SOAP** 

- ✔ Separación entre lógica de negocio y servicios 

- ✔ Orquestación centralizada 

- ✔ Reutilización de servicios SOAP 

- ✔ Control transaccional 

- ✔ Trazabilidad completa 

- ✔ Escalabilidad y cumplimiento normativo 

## **TECNOLOGÍAS TÍPICAS DE IMPLEMENTACIÓN** 

- **Motor BPM:** Camunda, Bonita, jBPM 

- **Servicios SOAP:** JAX-WS, WCF 

- **Lenguaje:** Java / .NET 

- **Formato mensajes:** XML 

- **Seguridad:** WS-Security 

## Diagrama BPMN de operaciones báncarias 

Imagen. Operacicones Bancarias Eureka Bank 

## **INTEGRACIÓN BPMN + BONITA BPM + JAVA + SOAP** 

A continuación, tienes la integración completa de un proceso BPMN con Bonita BPM, Java y servicios web SOAP, explicada paso a paso, con nivel académico–profesional, muy adecuada para Ingeniería en Tecnologías de la Información. 

## **ARQUITECTURA GENERAL DE LA SOLUCIÓN** 

## **Vista lógica** 

```
Cliente / Portal Bonita
        ↓
Proceso BPMN (Bonita Engine)
        ↓
Conectores Bonita (Java / SOAP)
        ↓
Servicios Web SOAP Bancarios
        ↓
Core Bancario
```

## **COMPONENTES DE LA SOLUCIÓN** 

|Componente|Tecnología|
|---|---|
|**Motor BPM**|Bonita BPM|
|**Modelado**|BPMN 2.0|
|**Lenguaje**|Java|
|**Integración**|Conectores SOAP|
|**Servicios**|SOAP(WSDL)|
|**UI**|Bonita Portal|
|**Seguridad**|WS-Security (opcional)|



## **MODELO BPMN EN BONITA** 

## **Pools y Lanes** 

**Pool:** Gestión de Operaciones Bancarias **Lanes:** 

- Cliente 

- Proceso BPM 

- Servicio Web SOAP 

- Auditoría 

## **VARIABLES DEL PROCESO EN BONITA** 

## **Variables de negocio (Business Data Model)** 

```
String tipoOperacion
String cuentaOrigen
String cuentaDestino
Double monto
String resultado
```

## **DIAGRAMA BPMN (RESUMEN FUNCIONAL)** 

(Start) ↓ [User Task: Solicitar Operación] ↓ [Service Task: Validar Datos] ↓ <XOR: Tipo Operación> 

├─ Consulta → SOAP consultarMovimientos 

├─ Depósito → SOAP realizarDeposito ├─ Retiro → Validar Saldo → SOAP realizarRetiro 

└─ Transferencia → Validar Saldo → SOAP realizarTransferencia ↓ [Service Task: Registrar Auditoría] ↓ [End] 

## **INTEGRACIÓN SOAP EN BONITA (CONECTORES)** 

Bonita utiliza **Connectors** para invocar servicios externos. 

## **CONSUMO DE SERVICIO SOAP DESDE BONITA** 

## **IMPORTAR EL WSDL** 

1. Abrir **Bonita Studio** 

2. Resources → Extensions → Import 

3. Importar archivo .wsdl 

4. Bonita genera el conector SOAP 

## **CONFIGURACIÓN DEL CONECTOR SOAP** 

En el **Service Task** : 

- Connector type: SOAP 

- Operation: realizarDeposito 

- Input: 

   - cuentaOrigen 

   - monto 

- Output: 

   - resultado 

## **ALTERNATIVA AVANZADA: CONECTOR JAVA PERSONALIZADO** 

## **CREAR UN CONECTOR JAVA** 

public class DepositoSOAPConnector { 

public String ejecutarDeposito(String cuenta, Double monto) throws Exception { 

BankService service = new BankService(); BankPortType port = service.getBankPort(); return port.realizarDeposito(cuenta, monto); } } 

## **USO DEL CONECTOR EN BONITA** 

- Tipo: **Service Task** 

- Implementation: Java 

- Method: `ejecutarDeposito` 

## **GATEWAY BPMN EN BONITA** 

## **Condiciones XOR** 

```
tipoOperacion == "CONSULTA"
tipoOperacion == "DEPOSITO"
tipoOperacion == "RETIRO"
tipoOperacion == "TRANSFERENCIA"
```

## **MANEJO DE ERRORES EN BONITA** 

## **BOUNDARY ERROR EVENT** 

- Asociado a Service Task SOAP 

- Error code: `SOAP_ERROR` 

## **LANZAR ERROR DESDE JAVA** 

```
throw new BonitaException("SOAP_ERROR");
```

## **REGISTRO DE AUDITORÍA** 

## **SERVICE TASK: REGISTRAR AUDITORÍA** 

- Tipo: Java 

- Guarda: 

   - Usuario 

   - Fecha  Tipo operación  Resultado 

Ejemplo: 

```
System.out.println("Auditoría: " + tipoOperacion + " - " + resultado);
```

## **FORMULARIOS BONITA (UI)** 

## **FORMULARIO DE SOLICITUD** 

Campos: 

- Tipo de operación (ComboBox) 

- Cuenta origen 

- Cuenta destino 

- Monto 

Bonita genera automáticamente la UI. 

## **13. DESPLIEGUE DEL PROCESO** 

1. Validar el proceso en Bonita Studio 

2. Publicar en Bonita Engine 

3. Ejecutar desde Bonita Portal 

## **FLUJO COMPLETO INTEGRADO** 

```
Usuario
 ↓
Bonita Portal
 ↓
Proceso BPMN
 ↓
Conector SOAP
 ↓
Sistema Bancario
 ↑
Respuesta SOAP
 ↑
Proceso BPM
 ↑
Resultado al usuario
```

## **VENTAJAS DE USAR BONITA + SOAP** 

- ✔ Modelado visual sencillo 

- ✔ Integración SOAP nativa 

- ✔ Bajo acoplamiento 

- ✔ Formularios automáticos 

- ✔ Escalabilidad empresarial 

- ✔ Ideal para procesos bancarios 

## **ENFOQUE ACADÉMICO (ESPE – INGENIERÍA TI)** 

Esta integración demuestra: 

- Uso correcto de BPMN 2.0 

- Orquestación SOA 

- Consumo de servicios SOAP 

- Aplicación de reglas de negocio 

- Gestión de errores 

- Auditoría 

## **INTEGRACIÓN BPMN + CAMUNDA + JAVA + SOAP** 

A continuación, tiene **la integración completa y coherente de BPMN + Camunda + Java + SOAP** , explicada **a nivel arquitectónico, conceptual y técnico** , como se espera en **Ingeniería en Tecnologías de la Información** y en escenarios **empresariales reales** . 

## **ARQUITECTURA GENERAL DE LA SOLUCIÓN** 

## **ARQUITECTURA EN CAPAS** 

```
Cliente / Frontend
        ↓
Proceso BPMN (Camunda Engine)
        ↓
Delegates Java (Service Tasks)
        ↓
Cliente SOAP (JAX-WS / Apache CXF)
        ↓
Sistema Bancario Core
```

## **COMPONENTES PRINCIPALES** 

|Componente|Tecnología|
|---|---|
|**Motor BPM**|Camunda BPM|
|**Lenguaje**|Java|
|**Servicios Bancarios**|SOAP(WSDL)|
|**Cliente SOAP**|JAX-WS/Apache CXF|
|**Orquestación**|BPMN 2.0|
|**Persistencia**|Base de datos Camunda|
|**Seguridad**|WS-Security (opcional)|



## **MODELO BPMN EN CAMUNDA** 

## **Elementos clave del BPMN** 

- **Service Tasks** → Implementados con Java Delegates 

- **Exclusive Gateway (XOR)** → Selección de operación 

- **Boundary Error Event** → Manejo de errores SOAP 

- **Variables de proceso** → Datos bancarios 

## **Ejemplo de configuración de Service Task** 

## En el modelador Camunda: 

```
Type: Service Task
Implementation: Java Class
Class: com.banco.bpm.soap.DepositoDelegate
```

## **VARIABLES DEL PROCESO BPM** 

```
cuentaOrigen : String
cuentaDestino : String
monto : Double
tipoOperacion : String
resultado : String
```

Estas variables se usan **entre tareas BPMN y Java** . 

## **CLIENTE SOAP EN JAVA (JAX-WS)** 

## **WSDL (ejemplo conceptual)** 

```
BankService.wsdl
```

- `consultarMovimientos` 

- `realizarDeposito` 

- `realizarRetiro` 

- `realizarTransferencia` 

## **Generación de stubs SOAP** 

`wsimport -keep -p com.banco.soap.client BankService.wsdl` Esto genera: 

- `BankService` 

- `BankPortType` 

- DTOs XML 

## **IMPLEMENTACIÓN JAVA DELEGATE (CAMUNDA)** 

## **EJEMPLO: DEPÓSITO BANCARIO** 

```
package com.banco.bpm.soap;
```

```
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import com.banco.soap.client.*;
```

```
public class DepositoDelegate implements JavaDelegate {
```

```
    @Override
```

```
    public void execute(DelegateExecution execution) throws Exception {
```

```
        String cuenta = (String) execution.getVariable("cuentaOrigen");
        Double monto = (Double) execution.getVariable("monto");
```

```
        BankService service = new BankService();
        BankPortType port = service.getBankPort();
```

```
        String respuesta = port.realizarDeposito(cuenta, monto);
```

```
        execution.setVariable("resultado", respuesta);
```

```
    }
```

```
}
```

## **GATEWAY BPMN CONTROLADO POR VARIABLES GATEWAY XOR: TIPO DE OPERACIÓN** 

Condiciones en Camunda: 

```
${tipoOperacion == "CONSULTA"}
${tipoOperacion == "DEPOSITO"}
${tipoOperacion == "RETIRO"}
${tipoOperacion == "TRANSFERENCIA"}
```

## **MANEJO DE ERRORES SOAP EN BPMN** 

## **BOUNDARY ERROR EVENT** 

En el Service Task: 

- Adjuntar **Boundary Error Event** 

- Error Code: `SOAP_ERROR` 

## **LANZAR ERROR DESDE JAVA** 

```
throw new BpmnError("SOAP_ERROR", "Servicio no disponible");
```

## **FLUJO ALTERNO DE ERROR** 

**Service Task:** Registrar error **User Task:** Notificar al cliente **End Event:** Error controlado 

## **REGISTRO DE AUDITORÍA (PARALELO)** 

## **GATEWAY PARALELO (AND)** 

- Rama 1: Continuar proceso 

- Rama 2: Registrar auditoría `execution.getProcessEngineServices()` 

```
         .getRuntimeService()
         .createMessageCorrelation("AUDIT")
         .setVariable("tipoOperacion", tipoOperacion)
         .correlate();
```

## **DESPLIEGUE EN CAMUNDA** 

## **ESTRUCTURA DEL PROYECTO (MAVEN)** 

```
bpm-banco/
 ├── src/main/java
 │    └── com/banco/bpm/soap
 ├── src/main/resources
 │    ├── diagram.bpmn
 │    └── application.properties
 └── pom.xml
```

## **DEPENDENCIAS MAVEN** 

```
<dependency>
  <groupId>org.camunda.bpm</groupId>
  <artifactId>camunda-engine</artifactId>
</dependency>
<dependency>
  <groupId>org.apache.cxf</groupId>
  <artifactId>cxf-rt-frontend-jaxws</artifactId>
</dependency>
```

## **SEGURIDAD SOAP (WS-SECURITY)** 

```
Map<String, Object> ctx = ((BindingProvider) port).getRequestContext();
ctx.put(BindingProvider.USERNAME_PROPERTY, "usuario");
ctx.put(BindingProvider.PASSWORD_PROPERTY, "password");
```

## **FLUJO COMPLETO INTEGRADO** 

```
Cliente
  ↓
Proceso BPMN (Camunda)
  ↓
Java Delegate
  ↓
Cliente SOAP
  ↓
Sistema Bancario
  ↑
Respuesta SOAP
  ↑
Camunda actualiza variables
  ↑
Fin del proceso
```

## **BENEFICIOS DE ESTA INTEGRACIÓN** 

- Orquestación clara y mantenible 

- Servicios SOAP reutilizables 

- Separación BPM / lógica técnica 

- Manejo formal de errores 

- Auditoría y trazabilidad 

- Preparado para microservicios 

## **NIVEL ACADÉMICO** 

Esta solución integra correctamente: 

- **BPM** 

- **SOA (SOAP)** 

- **Java Empresarial** 

- **Arquitectura en capas** 

- **Gobernanza de procesos** 

