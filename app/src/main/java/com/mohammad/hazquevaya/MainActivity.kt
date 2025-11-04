package com.mohammad.hazquevaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohammad.hazquevaya.ui.theme.HazquevayaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HazquevayaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   /* Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    Main(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HazquevayaTheme {
        Greeting("Android")
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    // Datos de entrada
    val comunidades = listOf(
        mapOf("nombre" to "Paiporta", "poblacion" to 5000, "pozos" to 8, "acueducto" to true),
        mapOf("nombre" to "Picanya", "poblacion" to 3000, "pozos" to 3, "acueducto" to false),
        mapOf("nombre" to "Sedavi", "poblacion" to 7000, "pozos" to 12, "acueducto" to true)
    )

    val ratios = comunidades.map {
        (it["pozos"] as Int).toDouble() / (it["poblacion"] as Int).toDouble()
    }

    val maxRatio = ratios.maxOrNull() ?: 0.0

    val calcularAcceso: (Map<String, Any>) -> Double = { comunidad ->
        val accesoBase = if (comunidad["acueducto"] as Boolean) 0.9 else 0.4
        val ratio = (comunidad["pozos"] as Int).toDouble() / (comunidad["poblacion"] as Int).toDouble()

        val bonoPozos = if (maxRatio > 0) (ratio / maxRatio) * 0.3 else 0.0

        (accesoBase + bonoPozos).coerceIn(0.0, 1.0)
    }

    // 4️⃣ Calcular población total y con acceso
    var poblacionTotal = 0.0
    var poblacionConAcceso = 0.0

    for (comunidad in comunidades) {
        val poblacion = comunidad["poblacion"] as Int
        val acceso = calcularAcceso(comunidad)
        poblacionTotal += poblacion
        poblacionConAcceso += poblacion * acceso
    }

    val porcentajeConAcceso = poblacionConAcceso * 100 / poblacionTotal

    // 5️⃣ Mostrar resultados en consola
    println("=== Cálculo de Acceso al Agua Potable ===")
    println()
    for (comunidad in comunidades) {
        val acceso = calcularAcceso(comunidad)
        println("${comunidad["nombre"]}: ${"%.1f".format(acceso * 100)}% de acceso al agua")
    }
    println()
    println("Población total: ${poblacionTotal.toInt()}")
    println("Población con acceso: ${poblacionConAcceso.toInt()}")
    println("Porcentaje total con acceso: ${porcentajeConAcceso}%")
    println("=========================================")
}
