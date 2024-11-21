package com.example.farmermarket.presentation.screens.main_farmer

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.presentation.screens.auth_farmer.Field
import retrofit2.http.Url


@Composable
fun AddProductScreen(navController: NavController, viewModel: FarmerViewModel, appContext: Context){

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())){
        Column(modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                navController.popBackStack()
            }
        ) {
            Column (modifier = Modifier
                .size(40.dp)
                .background(Color(0x79000000)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        var productName by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Vegetables") }
        var description by remember { mutableStateOf("") }
        var unit by remember { mutableStateOf("Kg") }
        var price by remember { mutableStateOf("") }
        var totalReserve by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val selectedImages = remember { mutableStateListOf<Uri>() }
        val context = LocalContext.current

        var showError by remember { mutableStateOf(false) }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {

                println(selectedImages)
                selectedImages.add(it)

            }
        }


        Field(
            textName = "Product Name",
            hint = "Product name",
            focusRequester = remember { FocusRequester() },
            onValueChange = { productName = it },
            isError = showError && productName.isEmpty(),
            value = productName
        )

        Dropdown("Category", listOf("Vegetables", "Fruits", "Crops"), selectedCategory = category) { category = it }

        Field(
            textName = "Description",
            hint = "Description",
            focusRequester = remember { FocusRequester() },
            onValueChange = { description = it },
            isError = showError && description.isEmpty(),
            value = description
        )

        Dropdown("Unit", listOf("Kg", "Gr", "Pack"), selectedCategory = unit) { unit = it }

        Field(
            textName = "Price per Unit",
            hint = "15000",
            focusRequester = remember { FocusRequester() },
            onValueChange = { price = it },
            isError = showError && price.isEmpty(),
            value = price,
            isNumber = true
        )

        Field(
            textName = "Total Reserve",
            hint = "500",
            focusRequester = remember { FocusRequester() },
            onValueChange = { totalReserve = it },
            isError = showError && totalReserve.isEmpty(),
            value = totalReserve,
            isNumber = true
        )


        Text(text = "Photos", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp) )

        LazyRow(
            horizontalArrangement = Arrangement.Start,
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) {
            item{
                Button(onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .width(126.dp)
                        .height(87.dp),
                    colors =  ButtonDefaults.buttonColors(Color(0xFFEEEBEB)),
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color(0xFF828282))
                }
            }
            items(selectedImages) { uri ->
                Box( ){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uri)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .width(126.dp)
                            .height(87.dp)
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(3.dp)) ,
                        contentScale = ContentScale.Crop,
                    )
                    Icon(imageVector = Icons.Default.Close, contentDescription = "",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.align(Alignment.TopEnd).clickable {
                            selectedImages.remove(uri)
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF4CAD73),  // Green border
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent  // Transparent background
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                if (productName.isEmpty() || description.isEmpty() || price.isEmpty() || totalReserve.isEmpty() || selectedImages.isEmpty()) {
                    showError = true
                    Toast.makeText(context, "Please fill all fields and add at least one image", Toast.LENGTH_SHORT).show()

                } else {
                    // Reset error state
                    showError = false

                    // Create ProductDTO and send it
                    val productDto = ProductDTO(
                        category = category,
                        description = description,
                        name = productName,
                        price = price.toDoubleOrNull() ?: 0.0,
                        quantity = totalReserve,
                        unit = unit,
                        imageUrl = Url()
                    )

                    viewModel.createProduct(context, selectedImages, productDto)
                }
            }
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Add", color = Color(0xFF4CAD73), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }

    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,){
        if(viewModel.productAddState.value.isLoading){
            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

        }else if(viewModel.productAddState.value.error != ""){
            Toast.makeText(LocalContext.current, "Something went wrong! Try later", Toast.LENGTH_SHORT).show()
            viewModel.productAddState.value = viewModel.productAddState.value.copy(error = "")


        } else if(viewModel.productAddState.value.isSent == true){
            Toast.makeText(LocalContext.current, "Successfully added", Toast.LENGTH_SHORT).show()
            viewModel.productAddState.value = viewModel.productAddState.value.copy(isSent = null)
        }
    }
}