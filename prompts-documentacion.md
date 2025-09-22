# Prompts de Documentacion

## Prompt 1: Pedi que me de una estructura para los comentarios

### Prompt Utilizado:

me podrias dar una la estructura para utilizar en los comentarios de los endpoints

### Respuesta Recibida:

¡Perfecto! 😃
La estructura que podrias usar para documentar cada endpoint es bastante clara y repetible. 
Te paso el formato base que podés usar siempre que agregues un nuevo endpoint:

📌 Estructura de comentarios para un endpoint
```bash
// Descripción general del endpoint
// Método: <VERBO_HTTP> <RUTA_COMPLETA>
// Qué retorna (y en qué casos)
// Posibles códigos de respuesta (si es relevante)
```
Resumen de la estructura ✍️

1. Primera línea → Qué hace el endpoint (acción principal).
2. Segunda línea → Método HTTP y la ruta.
3. Tercera línea → Qué retorna.
4. Opcional → Códigos de error o condiciones especiales (404, 400, etc.).