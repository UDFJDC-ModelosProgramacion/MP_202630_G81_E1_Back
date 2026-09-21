# Reglas de negocio — Refugio, Mascota, Veterinario, Seguimiento

> Responsable de las 4 entidades: \*\*David Santiago Cruz Aroca\*\*
> Estas reglas están implementadas y validadas en `RefugioService`, `MascotaService`, `VeterinarioService` y `SeguimientoService` (paquete `services`), lanzando `IllegalOperationException` o `EntityNotFoundException` según el caso.

| Entidad/asociación | Método | Regla | Responsable |
|---|---|---|---|
| RefugioEntity | Create | El nombre del refugio no puede ser nulo ni vacío | Cruz |
| RefugioEntity | Create | No pueden existir dos refugios con el mismo nombre | Cruz |
| RefugioEntity | Update | El refugio a actualizar debe existir | Cruz |
| RefugioEntity | Update | El nombre no puede quedar vacío ni duplicar el de otro refugio | Cruz |
| RefugioEntity | Delete | El refugio a eliminar debe existir | Cruz |
| RefugioEntity | Delete | No se puede eliminar un refugio que tenga mascotas registradas | Cruz |
| MascotaEntity | Create | El nombre y la especie no pueden ser nulos ni vacíos | Cruz |
| MascotaEntity | Create | La edad no puede ser negativa | Cruz |
| MascotaEntity | Create | El estado, si se especifica, debe ser uno de: Disponible, En proceso de adopcion, Adoptado | Cruz |
| MascotaEntity | Create | Si se asocia un refugio, este debe existir | Cruz |
| MascotaEntity | Update | La mascota a actualizar debe existir | Cruz |
| MascotaEntity | Update | Se validan las mismas reglas que en la creación (nombre, especie, edad, estado, refugio) | Cruz |
| MascotaEntity | Delete | La mascota a eliminar debe existir | Cruz |
| MascotaEntity | Delete | No se puede eliminar una mascota que tenga seguimientos veterinarios registrados (se conserva su historial médico) | Cruz |
| VeterinarioEntity | Create | El nombre y la especialidad no pueden ser nulos ni vacíos | Cruz |
| VeterinarioEntity | Create | Si se asocia un refugio, este debe existir | Cruz |
| VeterinarioEntity | Update | El veterinario a actualizar debe existir | Cruz |
| VeterinarioEntity | Update | Se validan las mismas reglas que en la creación (nombre, especialidad, refugio) | Cruz |
| VeterinarioEntity | Delete | El veterinario a eliminar debe existir | Cruz |
| VeterinarioEntity | Delete | No se puede eliminar un veterinario que tenga seguimientos asignados (se conserva el historial de atenciones) | Cruz |
| SeguimientoEntity | Create | La fecha de asignación no puede ser nula | Cruz |
| SeguimientoEntity | Create | Si hay próxima cita, no puede ser anterior a la fecha de asignación | Cruz |
| SeguimientoEntity | Create | La mascota asociada debe existir | Cruz |
| SeguimientoEntity | Create | El veterinario asociado debe existir | Cruz |
| SeguimientoEntity | Create | El seguimiento debe estar asociado a una mascota (no puede ser nulo) | Cruz |
| SeguimientoEntity | Create | El seguimiento debe estar asociado a un veterinario (no puede ser nulo) | Cruz |
| SeguimientoEntity | Update | El seguimiento a actualizar debe existir | Cruz |
| SeguimientoEntity | Update | No se puede modificar un seguimiento que ya está en estado "Completado" | Cruz |
| SeguimientoEntity | Update | Se validan las mismas reglas de fechas, de asociación y de existencia de mascota/veterinario que en la creación | Cruz |
| SeguimientoEntity | Delete | El seguimiento a eliminar debe existir | Cruz |
| SeguimientoEntity | Delete | No se puede eliminar un seguimiento que ya esté en estado "Completado" (se conserva el historial médico) | Cruz |

## Nota sobre asociaciones muchos a muchos

Las 4 entidades asignadas (Refugio, Mascota, Veterinario, Seguimiento) solo tienen relaciones \*\*uno a muchos / muchos a uno\*\* entre sí (Refugio–Mascota, Refugio–Veterinario, Mascota–Seguimiento, Veterinario–Seguimiento). No hay ninguna asociación muchos a muchos entre estas 4 entidades, por lo que no aplica una fila adicional de "asociación" en la tabla para ellas.

## Limitación conocida

La eliminación de un refugio solo valida que no tenga mascotas; no valida veterinarios asociados.
