import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-logoutdialog',
  templateUrl: './logoutdialog.component.html',
  styleUrls: ['./logoutdialog.component.css']
})
export class LogoutdialogComponent {

  constructor(public dialogRef: MatDialogRef<LogoutdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }
}
