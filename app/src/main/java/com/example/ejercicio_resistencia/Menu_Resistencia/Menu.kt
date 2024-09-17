import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun menu() {
    val context = LocalContext.current

    // Variables para las bandas y tolerancia seleccionadas
    var banda1 by remember { mutableStateOf<String?>(null) }
    var banda2 by remember { mutableStateOf<String?>(null) }
    var banda3 by remember { mutableStateOf<String?>(null) }
    var tolerancia by remember { mutableStateOf<String?>(null) }
    var resistencia by remember { mutableStateOf<String?>(null) }

    // Listas de opciones de colores y tolerancias
    val coloresBandas = listOf(
        "Negro - 0" to Color.Black,
        "Marrón - 1" to Color(139, 69, 19),
        "Rojo - 2" to Color.Red,
        "Naranja - 3" to Color(255, 165, 0),
        "Amarillo - 4" to Color.Yellow,
        "Verde - 5" to Color.Green,
        "Azul - 6" to Color.Blue,
        "Violeta - 7" to Color(148, 0, 211),
        "Gris - 8" to Color.Gray,
        "Blanco - 9" to Color.White
    )

    val tolerancias = listOf(
        "Oro - ±5%" to Color(255, 215, 0),
        "Plata - ±10%" to Color(192, 192, 192)
    )

    // Función para calcular la resistencia
    fun calcularResistencia(b1: Int, b2: Int, b3: Int): Double {
        return (b1 * 10 + b2) * 10.0.pow(b3)
    }

    // Función para calcular la tolerancia
    fun calcularTolerancia(bandaTol: String): String {
        return when (bandaTol) {
            "Oro - ±5%" -> "±5%"
            "Plata - ±10%" -> "±10%"
            else -> "±20%"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Título de la aplicación
            Text(
                text = "Calculadora de Resistencia de 3 Bandas",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                color = Color(0xFF4CAF50),
                modifier = Modifier
                    .background(Color(0xFFE8F5E9)) // Fondo verde claro
                    .border(2.dp, Color(0xFF4CAF50)) // Borde verde
                    .padding(16.dp) // Espaciado interno
                    .padding(bottom = 24.dp)
            )

            // Menú desplegable para Banda 1
            desplegableColor("Seleccione Banda 1", coloresBandas, banda1) { banda1 = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Menú desplegable para Banda 2
            desplegableColor("Seleccione Banda 2", coloresBandas, banda2) { banda2 = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Menú desplegable para Banda 3
            desplegableColor("Seleccione Banda 3", coloresBandas, banda3) { banda3 = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Menú desplegable para Tolerancia
            desplegableColor("Seleccione Tolerancia", tolerancias, tolerancia) { tolerancia = it }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para calcular la resistencia con color modificado
            Button(
                onClick = {
                    if (banda1 != null && banda2 != null && banda3 != null) {
                        val res = calcularResistencia(
                            banda1!![banda1!!.length - 1].digitToInt(),
                            banda2!![banda2!!.length - 1].digitToInt(),
                            banda3!![banda3!!.length - 1].digitToInt()
                        )
                        val tol = calcularTolerancia(tolerancia ?: "Oro - ±5%")
                        resistencia = "$res Ω $tol"
                        Toast.makeText(context, "Resistencia: $res Ω $tol", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Color verde del botón
                    contentColor = Color.White // Color del texto del botón
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Calcular Resistencia")
            }

            resistencia?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// Composable para los menús desplegables de selección de colores y tolerancias
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun desplegableColor(label: String, opciones: List<Pair<String, Color>>, valorSeleccionado: String?, onSeleccionar: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            value = valorSeleccionado ?: label,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(opcion.second)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opcion.first, style = TextStyle(fontSize = 16.sp))
                        }
                    },
                    onClick = {
                        onSeleccionar(opcion.first)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
