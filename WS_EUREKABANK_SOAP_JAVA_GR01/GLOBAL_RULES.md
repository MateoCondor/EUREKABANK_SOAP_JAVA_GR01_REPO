Estoy construyendo un sistema bancario con Spring Boot + JPA + Hibernate + Lombok.

Reglas obligatorias:
- Arquitectura en capas: controller, service, repository, dto, model, mapper
- Usar DTOs para entrada y salida (NO exponer entidades directamente)
- Usar MapStruct para mapear entidades <-> DTOs
- Manejo de errores con excepciones personalizadas
- Usar @Transactional en lógica crítica
- Relaciones bien definidas con JPA (ManyToOne, OneToMany)
- Usar enums para estados y tipos
- Código limpio, nombres claros en inglés
- Seguir principios SOLID
- Validaciones con Jakarta Validation (@NotNull, @Size, etc.)
- Código limpio y consistente en inglés

Estructura de modulos:
 ├── controller
 ├── service
 ├── repository
 ├── model
 ├── dto
 └── mapper

IMPORTANTE:
- Todos los módulos deben integrarse correctamente
- No duplicar lógica
- Reutilizar entidades relacionadas (Cliente en Cuenta, Cuenta en Movimiento)
