# Contexto del Proyecto - ShoppingListApp

Este documento proporciona información esencial sobre la estructura, arquitectura y convenciones del proyecto ShoppingListApp para facilitar el trabajo con IA y desarrolladores.

## 📁 Estructura del Proyecto

El proyecto sigue una arquitectura modular basada en Clean Architecture con 4 módulos principales:

### Módulos del Proyecto

```
ShoppingListApp/
├── app/                    # Capa de Presentación
│   ├── src/main/java/.../
│   │   ├── di/            # Módulos de inyección de dependencias (Hilt)
│   │   ├── navigation/    # Configuración de navegación con tipos seguros
│   │   ├── screens/       # Pantallas y ViewModels
│   │   │   ├── shoppingListsHomeScreen/
│   │   │   └── shoppingListItemsScreen/
│   │   ├── ui/            # Componentes UI reutilizables
│   │   │   ├── common/    # Componentes comunes (Dialogs, BottomSheets, etc.)
│   │   │   └── theme/     # Tema de Material Design 3
│   │   └── MainActivity.kt
│   └── build.gradle.kts
│
├── domain/                 # Capa de Dominio (Lógica de Negocio)
│   ├── src/main/java/.../
│   │   ├── model/         # Modelos de dominio
│   │   ├── repository/    # Interfaces de repositorios
│   │   └── usecase/       # Casos de uso (lógica de negocio)
│   └── build.gradle.kts
│
├── data/                  # Capa de Datos
│   ├── src/main/java/.../
│   │   ├── datasource/    # Fuentes de datos (interfaces)
│   │   ├── repository/    # Implementaciones de repositorios
│   │   └── di/            # Módulos de DI para la capa de datos
│   └── build.gradle.kts
│
├── framework/             # Capa de Infraestructura
│   ├── src/main/java/.../
│   │   ├── database/      # Room Database, Entities, DAOs
│   │   ├── datasource/    # Implementaciones concretas de DataSources
│   │   ├── mapper/        # Mappers entre Entities y Models
│   │   └── di/            # Módulos de DI para framework
│   └── build.gradle.kts
│
├── gradle/
│   └── libs.versions.toml # Gestión centralizada de dependencias
│
└── build.gradle.kts       # Configuración raíz del proyecto
```

### Jerarquía de Dependencias

```
app → domain, data, framework
data → domain
framework → data, domain
domain → (sin dependencias externas, solo Kotlin stdlib)
```

## 🎨 Estilo y Convenciones de Código

### Lenguaje y Framework

- **Lenguaje**: Kotlin exclusivamente (Java solo si es estrictamente necesario para interoperabilidad)
- **UI**: Jetpack Compose únicamente (no crear layouts XML ni usar Views de Android)
- **Versión mínima de Android**: API 24 (Android 7.0)
- **Target SDK**: 36
- **Compile SDK**: 36

### Convenciones de Nomenclatura

- **Clases, Interfaces, Objetos y Composables**: `PascalCase`
  - Ejemplo: `ShoppingListViewModel`, `CustomAlertDialog`
  
- **Funciones y Variables**: `camelCase`
  - Ejemplo: `getShoppingLists()`, `shoppingListId`
  
- **Constantes**: `SCREAMING_SNAKE_CASE`
  - Ejemplo: `MAX_ITEMS_PER_LIST`

### Estilo de Código

- **Imports**: Siempre al inicio del archivo
- **Comentarios**: 
  - Escribir el comentario en una línea separada antes del código
  - NO escribir comentarios inline junto al código
  - Ejemplo correcto:
    ```kotlin
    // Cargar las listas de compras
    loadShoppingLists()
    ```
  - Ejemplo incorrecto:
    ```kotlin
    loadShoppingLists() // Cargar las listas
    ```

- **Idioma del código**: Variables, comentarios y clases en inglés
- **Idioma de documentación**: Español (para respuestas y documentación)

### Estructura de Archivos

- Cada pantalla tiene su propia carpeta con:
  - `[ScreenName]Screen.kt` - Composable de la UI
  - `[ScreenName]ViewModel.kt` - ViewModel con lógica de presentación
  - `[ScreenName]UiState.kt` - Data class para el estado (si es necesario)

## 🏗️ Arquitectura y Patrones de Diseño

### Arquitectura: Clean Architecture + MVVM

El proyecto implementa Clean Architecture dividida en 4 capas:

#### 1. Capa de Presentación (`app/`)
- **Responsabilidad**: UI, navegación, gestión de estado de UI
- **Componentes**:
  - **Composables**: Pantallas y componentes UI reutilizables
  - **ViewModels**: Gestionan el estado de la UI y coordinan con casos de uso
  - **Navigation**: Navegación con tipos seguros usando Compose Navigation
- **Patrón**: MVVM (Model-View-ViewModel)
- **Estado**: `StateFlow` para estado reactivo

#### 2. Capa de Dominio (`domain/`)
- **Responsabilidad**: Lógica de negocio pura, independiente de frameworks
- **Componentes**:
  - **Use Cases**: Encapsulan operaciones de negocio específicas
  - **Models**: Modelos de dominio (entidades de negocio)
  - **Repository Interfaces**: Contratos para acceso a datos
- **Características**:
  - No depende de otras capas
  - Solo usa Kotlin stdlib y coroutines
  - Testeable de forma aislada

#### 3. Capa de Datos (`data/`)
- **Responsabilidad**: Implementación de repositorios y orquestación de fuentes de datos
- **Componentes**:
  - **Repository Implementations**: Implementan interfaces del dominio
  - **Data Sources**: Interfaces para fuentes de datos (local, remoto, etc.)
- **Características**:
  - Depende solo de `domain`
  - Coordina múltiples fuentes de datos si es necesario

#### 4. Capa de Framework (`framework/`)
- **Responsabilidad**: Implementaciones concretas de infraestructura
- **Componentes**:
  - **Room Database**: Base de datos local
  - **Entities**: Entidades de Room
  - **DAOs**: Data Access Objects para Room
  - **Mappers**: Conversión entre Entities y Models de dominio
  - **Data Source Implementations**: Implementaciones concretas
- **Características**:
  - Implementa detalles técnicos específicos de Android
  - Aislado del resto de la aplicación

### Flujo de Datos

```
UI (Composable) 
  ↓ observa
ViewModel 
  ↓ llama
Use Case 
  ↓ usa
Repository (interface)
  ↓ implementado por
Repository Implementation (data)
  ↓ usa
DataSource (interface)
  ↓ implementado por
RoomDataSource (framework)
  ↓ accede a
Room Database
```

### Patrones de Diseño Implementados

1. **Repository Pattern**: Abstracción del acceso a datos
2. **Use Case Pattern**: Encapsulación de lógica de negocio
3. **Dependency Injection**: Hilt para gestión de dependencias
4. **Observer Pattern**: StateFlow para estado reactivo
5. **Mapper Pattern**: Conversión entre capas (Entity ↔ Model)

### Inyección de Dependencias

- **Framework**: Hilt (Dagger)
- **Módulos**:
  - `AppModule` (app): Configuración general de la app
  - `DataModule` (data): Proporciona implementaciones de repositorios
  - `FrameworkModule` (framework): Proporciona Room Database y DAOs
- **Anotaciones**:
  - `@HiltAndroidApp` en Application
  - `@HiltViewModel` en ViewModels
  - `@Inject constructor()` en clases inyectables
  - `@Module` y `@Provides` en módulos

### Navegación

- **Framework**: Navigation Compose con tipos seguros
- **Destinos**: Definidos como objetos/data classes con `@Serializable`
- **Uso**:
  ```kotlin
  composable<ShoppingListHome> { ... }
  navController.navigate(ShoppingListItems(shoppingListId = "123"))
  ```

## 📦 Dependencias Críticas

### Gestión de Dependencias

Todas las dependencias se declaran en `gradle/libs.versions.toml` y se referencian mediante alias en los `build.gradle.kts`:

```kotlin
implementation(libs.androidx.core.ktx)
```

### Dependencias Principales

#### UI y Compose
- **Jetpack Compose BOM**: `2025.06.01`
- **Material 3**: Para componentes de UI
- **Navigation Compose**: `2.9.0` - Navegación con tipos seguros
- **Activity Compose**: `1.10.1`

#### Arquitectura y Estado
- **Lifecycle Runtime KTX**: `2.9.1`
- **Coroutines**: `1.10.2` (core y android)
- **StateFlow**: Para estado reactivo en ViewModels

#### Persistencia
- **Room**: `2.7.2`
  - `room-runtime`
  - `room-ktx`
  - `room-compiler` (KSP)

#### Inyección de Dependencias
- **Hilt**: `2.56.2`
  - `hilt-android`
  - `hilt-compiler` (KSP)
  - `hilt-navigation-compose`: `1.0.0`

#### Serialización
- **Kotlinx Serialization**: `1.9.0` - Para navegación con tipos seguros

#### Testing
- **JUnit**: `4.13.2`
- **MockK**: `1.13.8` - Mocking para tests
- **Turbine**: `1.0.0` - Testing de Flows
- **Coroutines Test**: Para testing de código asíncrono

### Plugins de Gradle

- **Android Gradle Plugin**: `8.10.0`
- **Kotlin**: `2.2.0`
- **KSP**: `2.2.0-2.0.2` - Para procesamiento de anotaciones
- **Hilt Plugin**: `2.56.2`
- **Compose Compiler**: Integrado en Kotlin plugin

## 🛠️ Comandos y Scripts Principales

### Compilación y Build

```bash
# Compilar el proyecto completo
./gradlew build

# Compilar solo la app
./gradlew :app:build

# Compilar un módulo específico
./gradlew :domain:build
./gradlew :data:build
./gradlew :framework:build

# Limpiar build
./gradlew clean

# Limpiar y compilar
./gradlew clean build
```

### Testing

```bash
# Ejecutar todos los tests unitarios
./gradlew test

# Ejecutar tests de un módulo específico
./gradlew :domain:test

# Ejecutar tests instrumentados (Android)
./gradlew connectedAndroidTest

# Ver reportes de tests
# Los reportes se generan en: build/reports/tests/
```

### Instalación y Ejecución

```bash
# Instalar APK en dispositivo/emulador
./gradlew installDebug

# Ejecutar app en dispositivo conectado
./gradlew installDebug && adb shell am start -n com.fcojaviergarciarodriguez.shoppinglistapp/.MainActivity
```

### Análisis y Linting

```bash
# Ejecutar lint
./gradlew lint

# Ver reportes de lint
# Los reportes se generan en: app/build/reports/lint/
```

### Gestión de Dependencias

```bash
# Actualizar dependencias (si hay nuevas versiones)
./gradlew --refresh-dependencies build

# Ver dependencias del proyecto
./gradlew :app:dependencies

# Ver árbol de dependencias de un módulo
./gradlew :domain:dependencies
```

### Comandos Útiles

```bash
# Sincronizar proyecto Gradle
./gradlew --refresh-dependencies

# Ver tareas disponibles
./gradlew tasks

# Ver tareas de un módulo específico
./gradlew :app:tasks
```

## 📝 Notas Importantes

### Antes de Finalizar Tareas

- **Siempre ejecutar**: `./gradlew build` antes de considerar cualquier tarea finalizada
- Esto asegura que el proyecto compila correctamente

### Confirmación de Cambios

- Solicitar confirmación previa antes de realizar tareas no solicitadas explícitamente
- No hacer refactorizaciones o arreglos adicionales sin aprobación

### Estructura de Tests

- Los tests unitarios están en `src/test/`
- Los tests instrumentados están en `src/androidTest/`
- El módulo `domain` tiene tests unitarios completos usando MockK y Turbine

### Internacionalización

- Strings en `res/values/strings.xml` (inglés)
- Strings en `res/values-es/strings.xml` (español)
- La app soporta múltiples idiomas

---

**Última actualización**: Generado automáticamente basado en la estructura actual del proyecto.

