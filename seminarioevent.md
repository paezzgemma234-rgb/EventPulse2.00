# Seminario de Desarrollo Android: EventPulse (Festivales de Arte Digital)

Este documento registra la bitácora de desarrollo, prompts utilizados, iteraciones del sistema, resultados obtenidos y correcciones manuales para el proyecto **EventPulse**.

---

## 1. Prompts Utilizados & Secuencia de Trabajo

### Prompt Inicial
> *"Actuá como un desarrollador Android senior especializado en Kotlin, Jetpack Compose. Tu objetivo es desarrollar EventPulse: Festivales de Arte Digital, una aplicación Android funcional, visualmente impactante, modular, escalable y adaptable a diferentes dispositivos..."*

### Prompt de Elección de Flujo
> *"Crear un plan de implementación primero"* -> Generación de `implementation_plan.artifact.md` y `task.artifact.md`.

### Prompt de Personalización Estética
> *"que sean estos colores: MidnightVoid (#080B18), DeepSpace (#12182A), NeonMagenta (#FF2E93), ElectricViolet (#8B5CF6), CyberCyan (#22D3EE), DigitalOrange (#FF8A3D), AcidLime (#C6F64A), SoftWhite (#F5F5FA), MutedSilver (#A7B0C5)."*

### Prompt de Arquitectura & Funcionalidades Extendidas
> *"Aplicá una arquitectura clara, separando la interfaz de usuario, la lógica de negocio y el acceso a datos. Organizá el código en componentes reutilizables... Incorporá ViewModel, Coroutines y StateFlow. Implementá Navigation Compose para navegar entre las pantallas. Defini el modelo de datos para festivales, fechas, categorías, artistas y favoritos..."*

### Prompt de Interacciones, Adaptabilidad & Documentación
> *"Implementá las acciones necesarias para que la aplicación no sea solamente visual: abrir detalle, filtrar por fecha/categoría, favoritos, consultar programación, navegar entre pantallas y mensajes vacíos. Adaptá la distribución a teléfonos y tablets. Documentá en seminarioevent.md..."*

---

## 2. Iteraciones y Decisiones de Arquitectura

1. **Separación Estricta de Capas**:
   - **`data`**: `EventRepository` e `EventRepositoryImpl` brindan acceso asíncrono asumiendo operaciones reactivas en segundo plano mediante `Flow`.
   - **`model`**: Modelos inmutables en Kotlin (`DigitalEvent`, `Category`, `Artist`, `StageSchedule`, `DigitalTicket`).
   - **`ui`**: Paginación, filtros y estado centralizado con `MainViewModel` exponiendo `StateFlow`.
   - **`navigation`**: Definición de rutas tipo-seguras con **Jetpack Navigation Compose** (`NavHost`, `composable`, `navArgument`).

2. **Adaptabilidad a Dispositivos (Phones & Tablets / Foldables)**:
   - Uso de `BoxWithConstraints` en las pantallas principales (`HomeScreen`, `ScheduleScreen`, `SavedTicketsScreen`).
   - Cuando el ancho de pantalla es `>= 600.dp` (Tablets/Modo horizontal), la distribución cambia automáticamente de `LazyColumn` de una columna a una grilla adaptativa de dos columnas (`LazyVerticalGrid(columns = GridCells.Fixed(2))`).

3. **Interacciones Avanzadas del Usuario**:
   - **Búsqueda reactiva**: Filtrado instantáneo por título, subtítulo o artista mediante `StateFlow` y `combine`.
   - **Filtros dobles**: Categorías temáticas (*Instalaciones Inmersivas, Música Electrónica, Arte Generativo, VR/AR, Performances AV*) y fechas (*En Vivo, Octubre, Noviembre, Diciembre*).
   - **Favoritos**: Toggle inmediato con actualización en la pestaña dedicada *Mi EventPulse*.
   - **Entradas Digitales**: Modal interactivo con renderizado de código QR animado en canvas neón.
   - **Estados Vacíos**: Pantalla de `EmptyStateCard` cuando los filtros no arrojan resultados o no hay favoritos/entradas guardadas.

---

## 3. Correcciones Manuales & Resoluciones Técnicas

- **Integración de Gradle Catalog (`libs.versions.toml`)**:
  - Se añadieron explícitamente las librerías `androidx.navigation:navigation-compose` y `androidx.lifecycle:lifecycle-viewmodel-compose`.
  - Se sincronizó el proyecto con `gradle_sync` para garantizar el indexado IDE de símbolos de navegación Compose.

- **Importes y Sintaxis Compose**:
  - Corrección de declaraciones de alias en `app/build.gradle.kts` (`libs.androidx.navigation.compose` y `libs.androidx.lifecycle.viewmodel.compose`).
  - Limpieza de directivas de importación innecesarias en los componentes UI.

---

## 4. Registro de Commits Recomendados para Git

```bash
# Commit 1: Sistema de diseño y tema cyberpunk
git commit -m "style: agregar tema Cyberpunk con paleta de colores neón y modificadores custom"

# Commit 2: Capa de datos y modelos
git commit -m "feat: agregar modelos de dominio (DigitalEvent, Category, Artist) y repositorio mock"

# Commit 3: ViewModel y reactividad
git commit -m "feat: implementar MainViewModel con StateFlow y filtrado reactivo de eventos"

# Commit 4: Navegación Jetpack Compose
git commit -m "feat: integrar Jetpack Navigation Compose y rutas de la aplicación"

# Commit 5: Pantallas e interfaz adaptativa
git commit -m "ui: implementar HomeScreen, EventDetailScreen, ScheduleScreen y SavedTicketsScreen con soporte tablet"

# Commit 6: Documentación
git commit -m "docs: agregar bitácora de desarrollo y decisiones técnicas en seminarioevent.md"
```
