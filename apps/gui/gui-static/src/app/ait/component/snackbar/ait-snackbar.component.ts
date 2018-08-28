import { Component, OnInit, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material';

@Component({
  selector: 'ait-snackbar',
  templateUrl: './ait-snackbar.component.html',
  styleUrls: ['./ait-snackbar.component.css']
})
export class AitSnackbarComponent implements OnInit {

  constructor(private snackBar: MatSnackBar, @Inject(MAT_SNACK_BAR_DATA) public data: any) { }

  ngOnInit() {
  }

  close() {
    this.snackBar.dismiss();
  }
}



