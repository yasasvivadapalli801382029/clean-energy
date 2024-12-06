import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserauthenticationService } from '../../services/auth/userauthentication.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthguardService } from 'src/services/authguard/authguard.service';
import * as CryptoJS from 'crypto-js';  // Import crypto-js for password hashing

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent {

  signinForm: FormGroup = new FormBuilder().group({
    userEmail: ['', [Validators.required, Validators.email]],
    userPassword: ['', Validators.required]
  });

  constructor(
    private userAuthenticationService: UserauthenticationService,
    private authguardService: AuthguardService,
    private router: Router,
    private _snackBar: MatSnackBar
  ) {
    this.checkIfUserIsLoggedIn();
  }

  checkIfUserIsLoggedIn() {
    if (this.authguardService.isTokenValid()) {
      console.log("User is logged in");
      this.navigateHome();
    } else {
      console.log("User not logged in");
    }
  }

  submitLogin() {
    const { userEmail, userPassword } = this.signinForm.value;
    
    // Hash the password using crypto-js (SHA-256) before sending it to the backend
    const hashedPassword = CryptoJS.SHA256(userPassword).toString(CryptoJS.enc.Base64);

    // Use the hashed password for login
    this.userAuthenticationService.login(userEmail, hashedPassword).then((response) => {
      this.openSnackBar("Login Successful", 5);
      this.navigateHome();
    }, (error) => {
      console.log(error.error.message);
      // console.log(error.body);
      this.openSnackBar("Login Failed: " + error.error.message, 5);
    });
  }

  navigateHome() {
    this.router.navigate(['/home']);
  }

  openSnackBar(message: string, time: number) {
    this._snackBar.open(message, 'Close', { duration: time * 1000 });
  }

}
