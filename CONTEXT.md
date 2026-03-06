# CONTEXT.md — adempiere-base

---

## Identidad

- **Nombre:** adempiere-base
- **Propósito:** Librería core del ERP ADempiere (fork SolopERP). Contiene los modelos de dominio, procesos de negocio, motor de costos, motor contable y utilidades de infraestructura que consumen todos los servicios del ecosistema Solop.
- **Tipo:** Librería

---

## Stack

- **Lenguaje/Runtime:** Java 11 (source) / Java 17 (bytecode target) + Scala 3.0.1 (módulo `service`)
- **Framework principal:** ADempiere Framework (ORM propio basado en `PO`, sin Spring ni Jakarta EE)
- **Base de datos:** PostgreSQL (principal), Oracle (soporte legacy via ojdbc6), MySQL (limitado)
- **Cola/Mensajería:** Apache Kafka 2.8.2, Apache ActiveMQ 5.7.0
- **Testing:** No se detectó framework de testing configurado en `build.gradle`. Existe `org/spin/queue/test/DefaultTest.java` de forma aislada. NO DETERMINADO — requiere input del equipo.

---

## Requisitos técnicos

- **Runtime:** Java 17 (requerido para el bytecode target; source compila en 11)
- **Gestor de paquetes:** Gradle (wrapper incluido: `gradlew`). JVM heap configurado en 8 GB (`-Xmx8192M` en `gradle.properties`).
- **Herramientas de build:** Gradle con plugins `java-library`, `maven-publish`, `signing`, `scala`, `com.google.protobuf:0.9.4`
- **SDK / Librerías con restricción de versión:**
  - `io.grpc:grpc-netty-shaded@1.65.1` — versión alineada con el protobuf plugin; cambiarla rompe la generación de stubs
  - `com.google.protobuf:protobuf-java@3.25.4` — versión fija por compatibilidad con `protoc` usado en CI
  - `org.apache.kafka:kafka_2.13@2.8.2` — requiere Scala 2.13 en classpath; actualizar requiere verificar compatibilidad binaria
  - `net.sf.jasperreports:jasperreports@6.21.4` y `@6.17.0` — ambas versiones declaradas como `api`; posible conflicto de classpath
  - `log4j:log4j@1.2.17` — versión legacy con CVEs conocidos; no actualizar sin evaluar impacto en todo el framework
  - `org.apache.poi:poi@3.17` — versión antigua; incompatible con poi 4.x+
  - `org.scala-lang:scala3-library_3@3.0.1` — versión inicial de Scala 3; actualizar puede requerir recompilación del módulo `service`
- **Servicios externos requeridos:**
  - `PostgreSQL → 9.6+ → base de datos operacional (recomendado 13+)`
  - `Apache Kafka → 2.8.x → exportación de diccionario y cola de documentos (módulo kafka/ECA56)`
  - `AWS S3 → API v1 (sdk 1.12.x) → almacenamiento de archivos adjuntos (módulo s3)`
  - `Firebase Admin → 9.3.0 → notificaciones push`
  - `GitHub Packages Maven → → repositorio de publicación del artefacto`
- **Compatibilidad con ecosistema SolopERP:**
  - `adempiere-gRPC-Server → requiere este JAR como dependencia core → si la versión no coincide, los stubs gRPC quedan desincronizados`
  - `adempiere-vue / adempiere-ui → consume la API REST/gRPC expuesta → cambios en proto rompen contratos`
  - NO DETERMINADO — versiones exactas de compatibilidad requieren input del equipo.
- **Entorno de desarrollo:**
  - Requiere instancia PostgreSQL accesible con el esquema ADempiere inicializado
  - Las credenciales de publicación se inyectan vía propiedades Gradle: `deployUsername` / `deployPassword`
  - El build genera código protobuf en `build/generated/source/protobuf/main/java/` antes de compilar; correr `gradle generateProto` si el IDE no lo hace automáticamente

---

## Modelo de dominio

- `MBPartner` — Tercero de negocio (cliente, proveedor, empleado, banco)
  - relación → `MOrder` (1:N, como cliente o proveedor)
  - relación → `MInvoice` (1:N)
  - relación → `MBPartnerLocation` (1:N, direcciones)
- `MOrder` — Orden de compra o venta
  - relación → `MOrderLine` (1:N, líneas de orden)
  - relación → `MInvoice` (1:N, facturas generadas)
  - relación → `MInOut` (1:N, recepciones/despachos)
- `MInvoice` — Factura (AP/AR)
  - relación → `MInvoiceLine` (1:N)
  - relación → `MPayment` (N:M, via allocations)
  - relación → `MInvoiceTax` (1:N, impuestos)
- `MProduct` — Producto o servicio
  - relación → `MProductPrice` (1:N, listas de precio)
  - relación → `MStorage` (1:N, stock por almacén/lote/ubicación)
- `MWarehouse` — Almacén
  - relación → `MLocator` (1:N, ubicaciones físicas)
  - relación → `MStorage` (1:N)
- `MInventory` — Inventario físico
  - relación → `MInventoryLine` (1:N)
- `MMovement` — Movimiento de mercancía entre almacenes
  - relación → `MMovementLine` (1:N)
- `MAcctSchema` — Esquema contable (plan de cuentas base)
  - relación → `MAccount` (1:N, combinaciones contables)
- `MPPOrder` — Orden de manufactura
  - relación → `MPPOrderBOM` (1:N, lista de materiales)
  - relación → `MPPCostCollector` (1:N, colectores de costo)
- `MHREmployee` — Empleado
  - relación → `MHRProcess` (N:M, procesos de nómina)
  - relación → `MHRMovement` (1:N, movimientos de nómina)
- `MProject` — Proyecto
  - relación → `MProjectLine` (1:N)
  - relación → `MOrder` (1:N, órdenes generadas)
- `MADQueue` — Cola interna del sistema para procesamiento asíncrono
  - relación → `MADQueueType` (N:1)

---

## Dependencias internas

- Este repositorio **es** la base; no consume otros repos de la organización Solop.
- Es consumido por: `adempiere-gRPC-Server`, servicios gRPC individuales del ecosistema Solop.

---

## Interfaz pública

Este repo es una **librería**. Exporta:

**Clases/paquetes principales:**

- `org.compiere.model.*` — Modelos de negocio (MOrder, MInvoice, MBPartner, MProduct, etc.) y framework ORM (PO, Query, ModelValidator)
- `org.compiere.process.SvrProcess` — Clase base para todos los procesos de servidor
- `org.compiere.process.DocAction` / `DocumentEngine` — Interfaz y motor de ciclo de vida documental (Draft → InProgress → Completed → Reversed)
- `org.compiere.util.DB` — Capa de acceso a base de datos (queries, transacciones)
- `org.compiere.util.Trx` — Gestión de transacciones JDBC
- `org.compiere.util.Env` — Contexto global de sesión (propiedades de entorno, org, moneda)
- `org.adempiere.core.domains.models.*` — Clases generadas `I_*` (interfaces/constantes de columnas) y `X_*` (accesores generados por tabla)
- `org.adempiere.engine.*` — Motor de costeo (Standard, FIFO/LIFO, Average PO/Invoice, Last)
- `org.spin.service.grpc.*` — Utilidades gRPC: interceptores de autenticación, `ValueManager`, `FilterManager`, `SessionManager`
- `org.spin.authentication.services.*` — Proveedores OAuth/OIDC (Google, GitHub, Microsoft)
- `org.spin.model.MWHWithholding` — Motor de retenciones fiscales
- `org.spin.queue.*` — Sistema de colas interno con notificadores (Email, Telegram, Discord)
- `org.solop.grpc.updates` (proto) — Mensaje protobuf `Update`/`Step`/`StepValue` para distribución de cambios de diccionario

---

## Flujos principales

**Ciclo de vida de un documento (ej. Orden de Venta)**
1. Se instancia `MOrder` y se setean campos base (BPartner, DocType, fecha)
2. Se agregan `MOrderLine` con producto, cantidad y precio
3. Se llama `MOrder.processIt(DocAction.ACTION_Complete)` → `DocumentEngine` despacha al método `completeIt()`
4. El engine ejecuta validaciones, calcula impuestos, reserva stock y escribe asientos contables en cola
5. Los `ModelValidator` registrados reciben el evento `TIMING_AFTER_COMPLETE` para lógica de extensión

**Proceso de costeo**
1. Un movimiento de inventario (`MInOut`, `MMovement`, `MPPCostCollector`) genera líneas de costo
2. `CostEngineFactory` selecciona el `ICostingMethod` según el esquema contable del producto
3. El método de costeo calcula el costo unitario y actualiza `M_CostDetail` y `M_Cost`
4. `StorageEngine` actualiza cantidades disponibles en `M_Storage`

**Exportación de diccionario vía Kafka (ECA56)**
1. Un cambio en el diccionario ADempiere (tabla, columna, proceso, etc.) dispara el `ModelValidator` `EngineAsQueue`
2. `KafkaLoader` serializa el cambio como mensaje Kafka usando `MapSerializer`
3. Consumidores externos reciben el evento para sincronizar su configuración local

**Publicación del artefacto**
1. Se crea un release en GitHub con tag de versión
2. CI (`publish.yml`) detecta el evento `release.published`
3. Gradle ejecuta `gradle publish` con `deployVersion=${{ github.event.release.tag_name }}`
4. El JAR se publica en `https://maven.pkg.github.com/solop-develop/adempiere-base` como `org.solop.adempiere:adempiere-core`

---

## Estructura relevante

```
adempiere-base/
├── base/src/main/java/          # Core del framework: modelos, procesos, utilidades, contabilidad
│   ├── org/compiere/model/      # ~500 clases M* (lógica de negocio) + ModelValidator interface
│   ├── org/compiere/process/    # SvrProcess, DocAction, DocumentEngine, procesos base
│   ├── org/compiere/util/       # DB, Trx, Env, Ini, CLogger
│   ├── org/compiere/db/         # Drivers: PostgreSQL, Oracle + HikariCP
│   └── org/adempiere/engine/    # Motor de costeo (5 métodos)
├── base/src/main/java/org/adempiere/core/domains/models/
│   │                            # ~1942 clases generadas: I_* (interfaces) y X_* (accesores ORM)
├── src/main/proto/updates.proto # Definición protobuf para distribución de cambios
├── grpc_utils/src/main/java/    # Interceptores gRPC, SessionManager, ValueManager, QueryBuilders
├── authentication/src/main/java/# OAuth/OIDC providers
├── kafka/src/main/java/         # Integración Kafka (ECA56): exportación de diccionario
├── processors/src/main/java/    # Procesadores batch: accounting, scheduler, alert, workflow
├── manufacturing/               # BOM, órdenes de manufactura, forecast engine
├── hr_and_payroll/              # Empleados, nómina, contratos
├── withholding_engine/          # Motor de retenciones fiscales
├── pos/                         # Validadores y procesos de Punto de Venta
├── build.gradle                 # Configuración única de build (monorepo sin sub-proyectos Gradle)
└── .github/workflows/           # CI: build.yml (push/PR), publish.yml (release)
```

---

## Patrones y convenciones

- **Patrón arquitectónico:** Capas ADempiere clásicas:
  1. `X_TableName` (generado, nunca editar manualmente) → accesores ORM básicos
  2. `M_TableName` (lógica de negocio, extiende `X_` o `PO`) → reglas, cálculos, relaciones
  3. `SvrProcess` (procesos de servidor) → operaciones batch invocadas desde el menú o scheduler
  4. `ModelValidator` (hooks) → lógica transversal sin modificar el modelo core
- **Manejo de errores:** Excepciones custom en `org.adempiere.exceptions.*` (ej. `AdempiereException`, `FillMandatoryException`, `PeriodClosedException`). Los procesos retornan `String` con el mensaje de resultado; los errores se lanzan como `AdempiereException` y son capturados por el framework.
- **Naming de clases:**
  - `M` prefix → modelo de negocio (`MOrder`, `MInvoice`)
  - `X_` prefix → generado por el diccionario (no editar)
  - `I_` prefix → interface con constantes de columna
  - `Doc_` prefix → documento contable (ej. `Doc_Invoice`)
  - `Abstract` suffix → clase generada con binding de parámetros de proceso (no editar)
  - `Callout` prefix → manejador de eventos de campo UI
- **Convención de paquetes por módulo:** `org.{compiere|adempiere|eevolution|solop|spin}.{módulo}.*`
- **Transacciones:** Siempre usar `Trx.get(name, true)` y pasar el `trxName` a los métodos de modelo. Los modelos tienen `get_TrxName()`. Nunca autocommit en lógica de negocio.
- **Procesos con parámetros:** Crear clase que extienda la clase `Abstract` generada (que extiende `SvrProcess`) y sobrescribir `doIt()`.

---

## Configuración y despliegue

- **Cómo se despliega:** Publicado como JAR en GitHub Packages (Maven). No es un servicio desplegable por sí mismo.
- **Build local:** `./gradlew build` — requiere credenciales de Maven para resolver dependencias de GitHub Packages si hubiera alguna.
- **Variables de entorno críticas (solo nombres):**
  - `ORG_GRADLE_PROJECT_deployUsername` / `deployUsername` — usuario para publicar en GitHub Packages
  - `ORG_GRADLE_PROJECT_deployPassword` / `deployPassword` — token PAT con permisos `write:packages`
  - `ORG_GRADLE_PROJECT_deployVersion` / `deployVersion` — versión del artefacto (en CI se toma del tag de release; local usa `local-1.0.0`)
- **Particularidades del build:**
  - El plugin protobuf genera código en `build/generated/source/protobuf/main/java/` que se incluye como `srcDirs` en el sourceSet. Si el IDE no lo reconoce, ejecutar `gradle generateProto` primero.
  - La tarea `jar` inyecta `EntityType=D` en el manifiesto.
  - Hay dos versiones de jasperreports declaradas (`6.21.4` y `6.17.0`); Gradle resolverá la más reciente pero ambas están en el POM.
- **CI:**
  - `build.yml` → ejecuta `gradle build` en push/PR a `develop`, `main`, `master`, `bugfix/**`, `feature/**`, `hotfix/**`, `test/**`
  - `publish.yml` → ejecuta `gradle publish` solo en evento `release.published` con el tag como versión
- **Ambientes:** No aplica (es librería). El entorno de ejecución lo define el servicio consumidor.

---

## Decisiones importantes

- **Decisión:** Todo el código de todos los módulos funcionales (25+) compila en un único JAR (`adempiere-core`).
  **Razón:** El framework ADempiere original es monolítico; separarlo requeriría refactorizar dependencias circulares entre módulos.
  **Consecuencia:** Cualquier cambio en cualquier módulo requiere recompilar y republicar el artefacto completo. Los consumidores no pueden tomar solo un subconjunto de funcionalidad.

- **Decisión:** Las clases `X_*` e `I_*` en `base/.../models/` (~1942 archivos) son **generadas** por el diccionario ADempiere y nunca deben editarse manualmente.
  **Razón:** Se regeneran automáticamente al ejecutar el generador de código del diccionario.
  **Consecuencia:** Los cambios en esas clases se pierden en la próxima regeneración. Toda lógica de negocio va en las clases `M*` o en `ModelValidator`.

- **Decisión:** `sourceCompatibility = 11` con `targetCompatibility = 17`.
  **Razón:** Permite compilar código fuente Java 11 y generar bytecode optimizado para JVM 17 sin refactorizar la sintaxis legacy.
  **Consecuencia:** No se pueden usar features de Java 12-17 en el código fuente (records, sealed classes, pattern matching, etc.) sin cambiar `sourceCompatibility`.

- **Decisión:** `log4j 1.2.17` se mantiene como dependencia `api`.
  **Razón:** El framework ADempiere core usa `CLogger` que envuelve `java.util.logging`, pero dependencias transitivas legacy requieren log4j 1.x.
  **Consecuencia:** Existe riesgo de seguridad conocido (aunque log4j 1.x no tiene la vulnerabilidad Log4Shell de 2.x). No "arreglar" actualizando a log4j 2.x sin analizar el impacto completo.

- **Decisión:** La autenticación gRPC se maneja vía interceptor (`AuthorizationServerInterceptor`) con JWT, no con frameworks externos.
  **Razón:** Integración directa con el sistema de sesiones de ADempiere (`SessionManager`, `TokenManager`).
  **Consecuencia:** Los servicios gRPC que usen este repo deben registrar el interceptor explícitamente. No hay autoconfiguración.

---

## Zonas frágiles y deuda técnica

- **Motor de costeo (`org.adempiere.engine.*`):** Lógica compleja con múltiples métodos de costeo interdependientes (FIFO/LIFO, promedio, estándar). Cambios aquí pueden afectar la valoración de inventario retroactivamente. NO tocar sin casos de prueba contables verificados.

- **Generación protobuf en build:** Los stubs gRPC se generan en `build/` y se incluyen como `srcDirs`. Si se limpia el directorio (`gradle clean`) el IDE pierde las referencias hasta la próxima compilación. No es un bug, es parte del flujo de build.

- **Dos versiones de JasperReports en el mismo POM:** `6.21.4` y `6.17.0` declaradas como `api`. Gradle resuelve `6.21.4` pero ambas aparecen en el POM publicado, lo que puede causar conflictos en consumidores que excluyan dependencias manualmente.

- **`log4j:log4j:1.2.17` como dependencia `api`:** Se propaga a todos los consumidores. CVE conocido en esta versión (aunque diferente a Log4Shell). No actualizar sin análisis de impacto completo en el classpath.

- **Módulo `service` en Scala 3.0.1:** Versión inicial de Scala 3 con API inestable. El código es mínimo (2 clases encontradas) pero actualizar Scala puede ser disruptivo. NO DETERMINADO si está en uso activo — requiere input del equipo.

- **`StorageUpdate` en `org.solop.queue.storage`:** Importado directamente en `MOrder` para notificar actualizaciones de almacenamiento vía cola. Este acoplamiento entre el modelo core y el subsistema de colas es frágil; cambios en la interfaz de la cola impactan directamente el ciclo de vida de la orden.

- **Deuda de naming de paquetes:** Coexisten cuatro namespaces raíz (`org.compiere`, `org.adempiere`, `org.eevolution`, `org.solop`, `org.spin`) heredados de diferentes eras del proyecto. No hay una convención unificada — la ubicación del código no siempre indica su funcionalidad.
