import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import { AitDialog } from '../../model/index';

@Component({
  selector: 'app-ait-confirm',
  templateUrl: './ait-confirm.component.html',
  styleUrls: ['./ait-confirm.component.css']
})
export class AitConfirmComponent implements OnInit {

  constructor( @Inject(MAT_DIALOG_DATA) public dialogConfig: AitDialog) { }

  ngOnInit() {
  }

  getColor() {
    switch (this.dialogConfig.dialogType) {
      case 'error':
        return 'red';
      case 'warning':
        return 'darkorange';
      case 'info':
        return 'dodgerblue';
      case 'confirm':
        return 'green';
    }
  }

}



