package com.example.farmermarket.presentation.screens.auth_farmer

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.R
import com.example.farmermarket.Role
import com.example.farmermarket.Screens
import com.example.farmermarket.common.Constants
import com.example.farmermarket.data.remote.dto.SignupDto
import com.example.farmermarket.presentation.screens.auth_buyer.AuthViewModel

@Composable
fun FarmerRegistrationScreen (navController: NavController, authViewModel: AuthViewModel ) {

    Constants.role = Role.FARMER.name
    var nameValue by remember { mutableStateOf("") }
    var surnameValue by remember { mutableStateOf("") }
    var middlnameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var phoneNumberValue by remember { mutableStateOf("+7") }

    var passwordValue by remember { mutableStateOf("") }
    var passwordValue2 by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val signUpState = authViewModel.signUpState

    if (signUpState.value.incorrectPasswordLength == true){
        authViewModel.signUpState.value.incorrectPasswordLength = false
        Toast.makeText(context, "Incorrect password length", Toast.LENGTH_SHORT).show()
    }else if(signUpState.value.userAlreadyExists == true){
        authViewModel.signUpState.value.userAlreadyExists = false
        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
    }else if(signUpState.value.internalServerError == true){
        authViewModel.signUpState.value.internalServerError = false
        Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {

        Image(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth().height((LocalConfiguration.current.screenHeightDp.dp )/ 2),
            painter = painterResource(id = R.drawable.group_799),
            contentDescription = null, // or provide a meaningful description
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter

        )

        Field(textName = "Name", hint = "Your name", focusRequester = focusRequester,  onValueChange = {nameValue = it} , false, nameValue)
        Field(textName = "Surname", hint = "Surname", focusRequester = focusRequester,  onValueChange = {surnameValue = it} , false, surnameValue)
        Field(textName = "Middlename", hint = "Middle name", focusRequester = focusRequester,  onValueChange = {middlnameValue = it} , false, middlnameValue)
        Field(textName = "Email", hint = "Your email", focusRequester = focusRequester, onValueChange ={emailValue=it} , value = emailValue)
        Field(textName = "Phone Number", hint = "87777777777", focusRequester = focusRequester, onValueChange = {phoneNumberValue = it},isNumber = true, value = phoneNumberValue )



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
            onClick = {

                if (emailValue.isEmpty() || nameValue.isEmpty() || surnameValue.toString().isEmpty() || middlnameValue.toString().isEmpty() || passwordValue.isEmpty()) {

                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()

                }else if(passwordValue != passwordValue2) {

                    Toast.makeText(context, "Password values don't match", Toast.LENGTH_SHORT).show()
                }else{

                    val signupDto = SignupDto(
                        password = passwordValue,
                        phone = phoneNumberValue,
                        email = emailValue,
                        first_name = nameValue,
                        last_name = surnameValue,
                        middle_name = middlnameValue  // Or you can leave this out entirely if it's not required
                    )
                    authViewModel.signUp(signupDto, onSuccess = {
                        navController.navigate(Screens.SUCCESS_SCREEN.name)
                    })
                }

                 }) {
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
fun Field(textName: String, hint: String,focusRequester:  FocusRequester, onValueChange: (String)->Unit, isNumber:Boolean = false,value: String, isError : Boolean = false){

    Text(text = textName, fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(start = 24.dp, bottom = 16.dp) )

    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isNumber){KeyboardType.Number}else{KeyboardType.Text}
        ),
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text(text = hint, color = Color(0xFFBDBDBD)) },
        maxLines = 1,
        isError = isError,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = Color.DarkGray,
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
            disabledContainerColor = Color(0xFFF2F2F2),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color(0xFFF2F2F2),
            unfocusedIndicatorColor = Color(0xFFF2F2F2),
            errorIndicatorColor = Color.Transparent,

        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            .focusRequester(focusRequester)
        ,
        value = value,
        onValueChange = { input ->
//            if (!isNumber || input.all { it.isDigit() }) {
                onValueChange(input)
//            }
        }
    )
}