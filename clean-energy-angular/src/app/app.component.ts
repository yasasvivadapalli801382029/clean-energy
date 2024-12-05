import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthguardService } from 'src/services/authguard/authguard.service';
import { LogoutdialogComponent } from './logoutdialog/logoutdialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'clean-energy-angular';
  isLoggedIn: boolean = false;

  constructor(private dialog: MatDialog,private authGuardService:AuthguardService,private router:Router) {
    
   }

   

  isLoggedin() {
    return this.authGuardService.isAuthenticated();
  }


  openLogoutConfirmationDialogbox() {

    const dialogRef = this.dialog.open(LogoutdialogComponent, {
      data: { action: 'Logout', content: 'Logout from this App?' }
    });
    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.authGuardService.setAuthenticatedFalse();
        this.router.navigate(['/signin']);
      }
      else
        dialogRef.close();

    });
  }
}
