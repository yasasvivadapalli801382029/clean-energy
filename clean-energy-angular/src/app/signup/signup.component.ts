import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserauthenticationService } from 'src/services/auth/userauthentication.service';
import { AuthguardService } from 'src/services/authguard/authguard.service';
import * as CryptoJS from 'crypto-js';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  signupForm: FormGroup = new FormBuilder().group({
    userName: ['', Validators.required],
    userEmail: ['', [Validators.required, Validators.email]],  // Added email validation
    userPassword: ['', [Validators.required, Validators.minLength(6)]]  // Added password length validation
  });

  constructor(
    private userauthenticationService: UserauthenticationService,
    private authguardService: AuthguardService,
    private router: Router,
    private _snackBar: MatSnackBar
  ) {}

  checkIfUserIsLoggedIn() {
    if (this.authguardService.isTokenValid()) {
      console.log("User is logged in");
      this.navigateHome();
    } else {
      console.log("User not logged in");
    }
  }
  
  submitSignup() {
    const userSignupRequest: any = this.signupForm.value;
    
    // Hash the password using crypto-js (SHA-256 example)
    const hashedPassword = CryptoJS.SHA256(userSignupRequest.userPassword).toString(CryptoJS.enc.Base64);
    
    // Update the request object to include the hashed password
    userSignupRequest.userPassword = hashedPassword;
  
    this.userauthenticationService.signup(userSignupRequest).subscribe((response) => {
      this.openSnackBar("Signup Successful", 5);
      this.navigateToSignin();
    }, (error) => {
      this.openSnackBar("Signup Failed: " + error.error.message, 5);
    });
  }
  

  navigateHome() {
    this.router.navigate(['/home']);
  }

  navigateToSignin() {
    this.router.navigate(['/signin']);
  }

  openSnackBar(message: string, time: number) {
    this._snackBar.open(message, 'Close', { duration: time * 1000 });
  }
}
