package com.example.nutrigrow.ui.screens.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrigrow.di.ViewModelFactory
import com.example.nutrigrow.models.BahanMakanan
import com.example.nutrigrow.ui.theme.PrimaryPink
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Utensils

/**
 * The "smart" or stateful composable for Food screen.
 */
@Composable
fun FoodRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModel: FoodViewModel = viewModel(factory = ViewModelFactory.getInstance(context))

    // Collect the UI state from the ViewModel's StateFlow
    val uiState by viewModel.uiState.collectAsState()

    // Pass the state down to the stateless screen and pass events up
    FoodScreen(
        modifier = modifier,
        uiState = uiState,
        onRefreshClicked = { viewModel.loadFoodData() }
    )
}

/**
 * The "dumb" or stateless composable for Food screen.
 */
@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    uiState: FoodUiState,
    onRefreshClicked: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Food Items",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryPink,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search food items...", color = Color.Gray) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPink,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = PrimaryPink
            )
        )
        
        // Handle the UI based on the uiState
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryPink)
                }
            }
            
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error: ${uiState.errorMessage}",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onRefreshClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            
            uiState.foodList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No food items available.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onRefreshClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
                        ) {
                            Text("Load Food Items")
                        }
                    }
                }
            }
            
            else -> {
                // Filter food items based on search text
                val filteredFoodList = if (searchText.isBlank()) {
                    uiState.foodList
                } else {
                    uiState.foodList.filter { 
                        it.nama.contains(searchText, ignoreCase = true) 
                    }
                }
                
                // Food Items List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredFoodList) { food ->
                        FoodCard(food = food)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: BahanMakanan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Utensils,
                    contentDescription = "Food placeholder",
                    tint = PrimaryPink,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Food Information
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = food.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = getHardcodedDescription(food.nama),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Nutritional info badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryPink.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Rich in nutrients",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryPink,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Function to provide hardcoded descriptions for food items
 */
fun getHardcodedDescription(foodName: String): String {
    return when (foodName.lowercase()) {
        "ginger", "jahe" -> "A warming spice with anti-inflammatory properties, great for digestion and immunity."
        "carrot", "wortel" -> "Rich in beta-carotene and vitamin A, excellent for eye health and immune system."
        "celery", "seledri" -> "Low-calorie vegetable packed with vitamins K and C, great for heart health."
        "onion", "bawang" -> "Contains antioxidants and compounds that may reduce inflammation and heart disease risk."
        "nutmeg", "pala" -> "Aromatic spice with antibacterial properties and potential digestive benefits."
        "spinach", "bayam" -> "Leafy green vegetable rich in iron, folate, and vitamins A, C, and K."
        "chicken", "ayam" -> "High-quality protein source with essential amino acids for muscle development."
        "rice", "beras" -> "Staple grain providing carbohydrates for energy and essential nutrients."
        "tomato", "tomat" -> "Rich in lycopene, vitamin C, and antioxidants that support heart health."
        "potato", "kentang" -> "Good source of potassium, vitamin C, and fiber for digestive health."
        "broccoli", "brokoli" -> "Cruciferous vegetable packed with vitamin C, fiber, and cancer-fighting compounds."
        "fish", "ikan" -> "Excellent source of omega-3 fatty acids and high-quality protein."
        "egg", "telur" -> "Complete protein source with all essential amino acids and choline for brain health."
        "milk", "susu" -> "Rich in calcium, protein, and vitamin D for strong bones and teeth."
        "banana", "pisang" -> "High in potassium, vitamin B6, and natural sugars for quick energy."
        else -> "Nutritious food item that provides essential vitamins, minerals, and energy for healthy growth."
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun FoodScreenPreview() {
    val sampleList = listOf(
        BahanMakanan(id = "1", nama = "Ginger", deskripsi = "Spice with anti-inflammatory properties."),
        BahanMakanan(id = "2", nama = "Carrot", deskripsi = "Rich in beta-carotene."),
        BahanMakanan(id = "3", nama = "Spinach", deskripsi = "Leafy green vegetable."),
        BahanMakanan(id = "4", nama = "Chicken", deskripsi = "High-quality protein source."),
        BahanMakanan(id = "5", nama = "Rice", deskripsi = "Staple grain for energy.")
    )
    FoodScreen(
        uiState = FoodUiState(foodList = sampleList),
        onRefreshClicked = {}
    )
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun FoodScreenLoadingPreview() {
    FoodScreen(
        uiState = FoodUiState(isLoading = true),
        onRefreshClicked = {}
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun FoodScreenErrorPreview() {
    FoodScreen(
        uiState = FoodUiState(errorMessage = "Failed to load food data. Please check your internet connection."),
        onRefreshClicked = {}
    )
}

