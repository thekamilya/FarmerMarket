package com.example.farmermarket.presentation.screens.auth_farmer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.R
import com.example.farmermarket.Screens

@Composable
fun FarmerRegistrationScreen (navController: NavController) {

    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var phoneNumberValue by remember { mutableStateOf("") }
    var governmentIdValue by remember { mutableStateOf("") }
    var addressValue by remember { mutableStateOf("") }
    var pharmSizeValue by remember { mutableStateOf("") }
    var additionalInfoValue by remember { mutableStateOf("") }

    var passwordValue by remember { mutableStateOf("") }
    var passwordValue2 by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {

        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth().height((LocalConfiguration.current.screenHeightDp.dp )/ 2),
            painter = painterResource(id = R.drawable.farmer_login_graphic),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter

        )

        Field(textName = "Name", hint = "yourname", focusRequester = focusRequester,  onValueChange = {nameValue = it} , false, nameValue)
        Field(textName = "Email", hint = "youremail", focusRequester = focusRequester, onValueChange ={emailValue=it} , value = emailValue)
        Field(textName = "Phone Number", hint = "87777777777", focusRequester = focusRequester, onValueChange = {phoneNumberValue = it},isNumber = true, value = phoneNumberValue )
        Field(textName = "Government issued ID", hint = "0000000000", focusRequester =focusRequester , onValueChange = {governmentIdValue = it},isNumber = true, value = governmentIdValue)
        Field(textName = "Farm address" , hint ="Astana, Kabanbay Batyr" , focusRequester = focusRequester , onValueChange = {addressValue = it}, value = addressValue )
        Field(textName = "Farm size", hint = "2.3", focusRequester = focusRequester, onValueChange = {pharmSizeValue = it} ,isNumber = true, value =  pharmSizeValue )
        Field(textName =  "Any further information about your farm", hint = "My farm is", focusRequester = focusRequester , onValueChange = {additionalInfoValue = it}, value = additionalInfoValue)


        Text(text = "Password", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp) )


        OutlinedTextField(

            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = "yourpassword", color = Color(0xFFBDBDBD)) },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.DarkGray,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color(0xFFF2F2F2),
                unfocusedIndicatorColor = Color(0xFFF2F2F2),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
                .focusRequester(focusRequester)
            ,
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None // Show password
            } else {
                PasswordVisualTransformation() // Hide password
            },
            trailingIcon = {
                val image = if (passwordVisibility) {
                    Icons.Default.Visibility // Icon to indicate password is visible
                } else {
                    Icons.Default.VisibilityOff // Icon to indicate password is hidden
                }

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            value = passwordValue,
            onValueChange = { passwordValue = it }
        )

        Text(text = "Re-enter password", fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 16.dp) )


        OutlinedTextField(

            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = "yourpassword", color = Color(0xFFBDBDBD)) },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.DarkGray,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color(0xFFF2F2F2),
                unfocusedIndicatorColor = Color(0xFFF2F2F2),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
                .focusRequester(focusRequester)
            ,
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None // Show password
            } else {
                PasswordVisualTransformation() // Hide password
            },
            trailingIcon = {
                val image = if (passwordVisibility) {
                    Icons.Default.Visibility // Icon to indicate password is visible
                } else {
                    Icons.Default.VisibilityOff // Icon to indicate password is hidden
                }

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            value = passwordValue2,
            onValueChange = { passwordValue2 = it }
        )

        Spacer(modifier = Modifier.height(20.dp))


        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            colors = ButtonDefaults.buttonColors( Color(0xFF4CAD73)),
            shape = RoundedCornerShape(10.dp),
            onClick = { navController.navigate(Screens.VERIFICATION.name) }) {
            Spacer(modifier = Modifier.height(35.dp))
            Text(text = "Register", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(35.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
            Text(text = "Already have an account? ", color = Color(0xFF0EB177))
            Text(text = "Log in ",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                color = Color(0xFF0EB177),
                fontWeight = FontWeight.Bold)

        }
        Spacer(modifier = Modifier.height(35.dp))



    }
}



@Composable
fun Field(textName: String, hint: String,focusRequester:  FocusRequester, onValueChange: (String)->Unit, isNumber:Boolean = false,value: String){

    Text(text = textName, fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(start = 24.dp, bottom = 16.dp) )

    TextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isNumber){KeyboardType.Number}else{KeyboardType.Text}
        ),
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text(text = hint, color = Color(0xFFBDBDBD)) },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = Color.DarkGray,
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
            disabledContainerColor = Color(0xFFF2F2F2),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color(0xFFF2F2F2),
            unfocusedIndicatorColor = Color(0xFFF2F2F2),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            .focusRequester(focusRequester)
        ,
        value = value,
        onValueChange = {  onValueChange(it) }
    )
}