# 🔥 Guía Completa: Subir App a Firebase App Distribution

## ✅ Configuración completada en el proyecto:
- ✓ Plugins de Firebase agregados
- ✓ Dependencias de Firebase instaladas
- ✓ Build.gradle configurado

---

## 📋 PASOS PARA SUBIR TU APP A FIREBASE

### **Paso 1: Crear proyecto en Firebase Console**

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en **"Agregar proyecto"** (o usa uno existente)
3. Nombre del proyecto: **"Restock Mobile"** (o el que prefieras)
4. Acepta los términos de Firebase
5. (Opcional) Desactiva Google Analytics si no lo necesitas
6. Haz clic en **"Crear proyecto"**

---

### **Paso 2: Registrar tu app Android en Firebase**

1. En la consola de Firebase, haz clic en el ícono de **Android** (ícono de robot verde)
2. Completa los datos:
   - **Nombre del paquete Android**: `com.uitopic.restockmobile`
   - **Alias de la app**: `Restock Mobile` (opcional)
   - **Certificado SHA-1**: Déjalo vacío por ahora (opcional)
3. Haz clic en **"Registrar app"**

---

### **Paso 3: Descargar google-services.json**

1. Firebase te mostrará un archivo `google-services.json` para descargar
2. **IMPORTANTE**: Descarga este archivo
3. Colócalo en la carpeta: `app/google-services.json`
   ```
   restock-mobile-android-develop/
   └── app/
       └── google-services.json  ← Aquí
   ```
4. Haz clic en **"Siguiente"** y luego **"Continuar a la consola"**

---

### **Paso 4: Sincronizar el proyecto**

1. En Android Studio, haz clic en **"Sync Now"** (aparecerá una notificación)
2. O ve a: `File > Sync Project with Gradle Files`
3. Espera a que termine la sincronización

---

### **Paso 5: Generar el APK**

Tienes dos opciones:

#### **Opción A: APK de Debug (Rápido, para testing)**
1. En Android Studio: `Build > Build Bundle(s) / APK(s) > Build APK(s)`
2. Espera a que termine la compilación
3. Cuando aparezca "APK(s) generated successfully", haz clic en **"locate"**
4. El APK estará en: `app/build/outputs/apk/debug/app-debug.apk`

#### **Opción B: APK de Release (Firmado, para producción)**
1. En Android Studio: `Build > Generate Signed Bundle / APK`
2. Selecciona **APK** y haz clic en **Next**
3. Crea un keystore (si no tienes uno):
   - Haz clic en **"Create new..."**
   - Completa los datos y guarda el archivo .jks
   - **¡IMPORTANTE!** Guarda bien la contraseña, la necesitarás
4. Haz clic en **Next** y luego **Finish**
5. El APK estará en: `app/build/outputs/apk/release/app-release.apk`

---

### **Paso 6: Subir APK a Firebase App Distribution**

#### **Método 1: Desde la Consola de Firebase (Más fácil)**

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Selecciona tu proyecto
3. En el menú lateral, ve a **"Release & Monitor" > "App Distribution"**
4. Haz clic en **"Get started"** (si es la primera vez)
5. Haz clic en **"Distribute app"** o **"New release"**
6. Arrastra y suelta tu APK (o haz clic para seleccionarlo)
7. Agrega notas de la versión (por ejemplo: "Primera versión beta - Sección de Sales implementada")
8. Haz clic en **"Next"**

---

### **Paso 7: Agregar testers (usuarios que probarán la app)**

1. En la sección **"Testers"**, haz clic en **"Add testers"**
2. Puedes agregar testers de dos formas:

   **Opción A: Por email individual**
   - Escribe el email del tester y presiona Enter
   - Repite para cada tester

   **Opción B: Crear grupos**
   - Haz clic en **"Create new group"**
   - Dale un nombre (ej: "Equipo QA", "Desarrolladores")
   - Agrega los emails de los miembros del grupo

3. Haz clic en **"Next"**

---

### **Paso 8: Distribuir la app**

1. Revisa toda la información
2. Haz clic en **"Distribute"**
3. Firebase procesará el APK y enviará invitaciones

---

### **Paso 9: Los testers reciben la app**

Los testers recibirán un **email de Firebase App Distribution** con:
- Un enlace para descargar la app
- Instrucciones de instalación

**Los testers deben:**
1. Abrir el email en su dispositivo Android
2. Hacer clic en el enlace
3. Si es la primera vez, instalar **"Firebase App Tester"** desde Play Store
4. Permitir instalación de fuentes desconocidas (si el sistema lo pide)
5. Descargar e instalar la app

---

## 🚀 Distribuir nuevas versiones

Para distribuir actualizaciones:

1. **Incrementa el versionCode** en `app/build.gradle.kts`:
   ```kotlin
   versionCode = 2  // Era 1, ahora 2
   versionName = "1.1"  // Era "1.0", ahora "1.1"
   ```

2. **Genera un nuevo APK** (repite Paso 5)

3. **Sube el nuevo APK a Firebase** (repite Paso 6-8)

4. Los testers recibirán una notificación de actualización automáticamente

---

## 📱 Método Alternativo: Firebase CLI (Avanzado)

También puedes distribuir desde la terminal usando Firebase CLI:

```bash
# 1. Instalar Firebase CLI
npm install -g firebase-tools

# 2. Login en Firebase
firebase login

# 3. Distribuir el APK
firebase appdistribution:distribute app/build/outputs/apk/debug/app-debug.apk \
  --app YOUR_FIREBASE_APP_ID \
  --groups "testers" \
  --release-notes "Nueva versión con mejoras"
```

---

## ⚠️ Solución de Problemas

### Error: "google-services.json not found"
- Asegúrate de que el archivo esté en `app/google-services.json`
- Sincroniza el proyecto nuevamente

### Error al sincronizar Gradle
- Ve a: `File > Invalidate Caches / Restart`
- Limpia el proyecto: `Build > Clean Project`
- Reconstruye: `Build > Rebuild Project`

### Los testers no pueden instalar
- Deben habilitar "Fuentes desconocidas" en Configuración > Seguridad
- Asegúrate de que tengan Android 5.0 (API 21) o superior

### APK muy grande
- Considera usar AAB (Android App Bundle) en lugar de APK
- Habilita ProGuard para reducir el tamaño en el build de release

---

## 📊 Monitoreo de distribución

En Firebase Console > App Distribution puedes ver:
- ✓ Número de invitaciones enviadas
- ✓ Cuántos testers aceptaron
- ✓ Cuántos descargaron la app
- ✓ Feedback de los testers

---

## 🎯 Resumen Rápido

1. ✅ Crear proyecto en Firebase Console
2. ✅ Registrar app Android con package: `com.uitopic.restockmobile`
3. ✅ Descargar `google-services.json` y colocarlo en `app/`
4. ✅ Sincronizar proyecto en Android Studio
5. ✅ Generar APK: `Build > Build APK(s)`
6. ✅ Ir a Firebase > App Distribution
7. ✅ Subir APK y agregar testers
8. ✅ Distribuir
9. ✅ Los testers reciben email e instalan

---

## 📞 Links útiles

- [Firebase Console](https://console.firebase.google.com/)
- [Documentación oficial](https://firebase.google.com/docs/app-distribution)
- [Firebase App Tester (Play Store)](https://play.google.com/store/apps/details?id=com.google.firebase.appdistribution)

---

**¡Listo!** Tu app estará disponible para que otros la prueben en minutos. 🎉

