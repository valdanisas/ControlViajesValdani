# ⛏️ Control de Material — App Nativa Android

## 🎯 ¿Cómo obtener el APK? (Sin necesitar Android Studio)

### OPCIÓN A — GitHub Actions (Recomendada ✅ · Gratuita · Sin instalar nada)

**Paso 1: Crear cuenta en GitHub**
1. Vaya a **https://github.com** → "Sign up" → cuenta gratuita

**Paso 2: Crear repositorio y subir archivos**
1. En GitHub: "New repository" → nombre: `ctrl-material` → "Create repository"
2. Haga clic en "uploading an existing file"
3. **Suba TODOS los archivos y carpetas** de este ZIP manteniendo la estructura de carpetas
4. Haga clic en "Commit changes"

**Paso 3: Ver el build automático**
1. Vaya a la pestaña **Actions** en su repositorio
2. Verá el workflow "🔨 Build APK" ejecutándose (tarda ~5 minutos)
3. Cuando termine (✅ verde), haga clic en el workflow
4. En la sección **Artifacts** al fondo, descargue **ControlMaterial-debug-apk**

**Paso 4: Instalar en Android**
1. Pase el APK al teléfono (WhatsApp, email, cable USB, Google Drive)
2. En el teléfono: **Ajustes → Seguridad → Instalar apps desconocidas → Chrome/Explorador → Activar**
3. Abra el APK desde la carpeta de Descargas
4. Toque **Instalar**
5. ¡Listo! La app aparece con ícono propio en su pantalla

---

### OPCIÓN B — Android Studio (Si tiene el programa instalado)

1. Abra Android Studio → "Open an existing project"
2. Seleccione la carpeta de este proyecto
3. Espere que sincronice (primera vez descarga dependencias ~5 min)
4. Menú: **Build → Build Bundle(s)/APK(s) → Build APK(s)**
5. El APK queda en: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🍎 ¿Y en iPhone (iOS)?

Para iOS **sí o sí se necesita una Mac con Xcode** para compilar. Las opciones son:

### Opción 1: PWA en Safari (más fácil, sin Mac)
El archivo `ControlMaterial_PWA.zip` (del paquete anterior) funciona en iPhone.
- Súbalo a Netlify → ábralo en Safari → "Añadir a pantalla de inicio"
- La experiencia es igual a la app nativa, sin costo adicional

### Opción 2: App nativa iOS (requiere Mac + Xcode)
1. Instale Node.js y Capacitor: `npm install -g @capacitor/cli`
2. En la carpeta del proyecto: `npx cap add ios`
3. Abra en Xcode: `npx cap open ios`
4. En Xcode → firme con su Apple ID → Build & Run al iPhone conectado
- Requiere Apple Developer Program ($99/año) para distribuir a otros

---

## 📱 Características de la app nativa vs PWA

| Característica             | App Nativa (APK) | PWA Safari/Chrome |
|---------------------------|-----------------|-------------------|
| Sin barra del navegador   | ✅               | ✅                 |
| Ícono en pantalla          | ✅               | ✅                 |
| Compartir en WhatsApp     | ✅ Nativo        | ✅ Share Sheet     |
| Funciona offline          | ✅               | ✅                 |
| Sin App Store             | ✅ (Android)     | ✅                 |
| Instalación directa       | ✅ APK           | Safari + "Añadir" |

---

## 📁 Estructura de archivos del proyecto

```
ControlMaterial_Android/
├── .github/
│   └── workflows/
│       └── build-apk.yml          ← Construcción automática del APK
├── app/
│   ├── build.gradle               ← Configuración de la app
│   └── src/main/
│       ├── AndroidManifest.xml    ← Permisos y configuración Android
│       ├── kotlin/.../
│       │   └── MainActivity.kt    ← Puente JS ↔ Android nativo
│       └── assets/www/
│           └── index.html         ← La aplicación completa
├── build.gradle                   ← Configuración raíz
├── settings.gradle
└── gradle/wrapper/
    └── gradle-wrapper.properties
```

## 🔄 Actualizar la app
Cada vez que suba cambios a GitHub (al branch `main`), el APK se reconstruye automáticamente en ~5 minutos.
