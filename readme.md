# ⏰ Reloj Digital Personalizable

Aplicación de escritorio desarrollada en **Java (Swing)** que permite visualizar un reloj digital altamente configurable con personalización en tiempo real.

---

## 🚀 Características principales

- 🖼️ Fondo personalizable con imágenes
- 🎨 Cambio dinámico de color del reloj
- 🔤 Selección de fuente (tipografía, tamaño y estilo)
- 📍 Posicionamiento del reloj en pantalla
- 💾 Persistencia de configuración (se guarda automáticamente)
- ⚡ Actualización en tiempo real (sin reiniciar la app)
- 🎯 Interfaz moderna con efectos visuales (UX mejorada)

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una estructura modular basada en separación de responsabilidades:

```
Reloj_Clock
│
├── app
│   ├── Main.java
│   ├── ConfigManager.java
│   └── ConfigChangeListener.java
│
├── Controller
│   ├── UIController.java
│   └── MainEventHandler.java
│
├── ui
│   ├── ClockPanel.java
│   ├── TimeOverlayPanel.java
│   ├── ConfImagePanel.java
│   ├── ConfColorPanel.java
│   └── ConfFontPanel.java
│
├── logic
│   ├── ClockService.java
│   └── FileHandler.java
│
├── animation
│   └── AnimationManager.java
│
├── utils
│   └── ResourceLoader.java
```

---

## 🧠 Conceptos clave implementados

- **MVC adaptado (Controlador centralizado)**
- **Separación UI / lógica / persistencia**
- **Programación orientada a objetos (POO)**
- **Gestión de eventos desacoplada**
- **Persistencia con archivos de configuración**
- **Renderizado personalizado con `paintComponent()`**

---

## 🛠️ Tecnologías utilizadas

- Java 17+
- Swing (GUI)
- AWT (renderizado gráfico)
- NetBeans (IDE original)
- Git & GitHub

---

## 📸 Demo (GIF)


```
/assets/demo.gif
```

---

## ⚙️ Instalación y ejecución

### 1. Clonar repositorio
```bash
git clone https://github.com/TU-USUARIO/Reloj_Clock.git
```

### 2. Abrir en IDE
- NetBeans (recomendado)
- IntelliJ IDEA
- Visual Studio Code

### 3. Ejecutar
Ejecutar la clase:
```
main.java.app.Main
```

---

## 📦 Estructura de configuración

El sistema guarda automáticamente:

- Color del reloj
- Fuente
- Posición
- Imagen de fondo

Ubicación: gestionado por `FileHandler`

---

## 🧪 Buenas prácticas implementadas

- ✔ Uso de **Javadoc completo**
- ✔ Validaciones para evitar errores (`null`, formatos)
- ✔ Separación clara de responsabilidades
- ✔ Métodos reutilizables
- ✔ Manejo de excepciones controlado
- ✔ Código limpio y documentado

---

## 🔮 Próximas mejoras

- 🌙 Modo oscuro / claro automático
- 🎞️ Animaciones de transición (fade, zoom)
- 🧩 Soporte para plugins o widgets
- 🌐 Versión web (JavaFX o React)
- ☁️ Sincronización en la nube
- 🕒 Formatos de hora avanzados (12h / 24h dinámico)

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas:

1. Fork del proyecto
2. Crear rama (`feature/nueva-funcionalidad`)
3. Commit de cambios
4. Pull Request

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

---

## 👨‍💻 Autor

**Diego Jiménez**  
Ingeniería de Sistemas  
Colombia 🇨🇴

---

## ⭐ Si te gusta el proyecto

Dale una estrella ⭐ en GitHub y compártelo con otros desarrolladores.

---

## 🧩 Valor del proyecto

Este proyecto no solo es una aplicación visual, sino un ejemplo de:

- Arquitectura bien organizada
- Buenas prácticas de ingeniería de software
- Código mantenible y escalable

Ideal para portafolio profesional 🚀

