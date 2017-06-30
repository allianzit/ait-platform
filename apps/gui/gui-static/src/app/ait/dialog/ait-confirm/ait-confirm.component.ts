import { Component, OnInit, Inject } from '@angular/core';
import { MD_DIALOG_DATA } from '@angular/material';
import { AitDialog } from '../../model/ait-dialog';

@Component( {
    selector: 'app-ait-confirm',
    templateUrl: './ait-confirm.component.html',
    styleUrls: ['./ait-confirm.component.css']
} )
export class AitConfirmComponent implements OnInit {

    constructor( @Inject( MD_DIALOG_DATA ) public dialogConfig: AitDialog ) { }

    ngOnInit() {
    }

}



