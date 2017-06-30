import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModule, MdInputModule, MdDialogModule, MdSnackBarModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule( {
    imports: [BrowserAnimationsModule, MaterialModule, MdInputModule, FormsModule, ReactiveFormsModule, MdDialogModule, MdSnackBarModule],
    exports: [BrowserAnimationsModule, MaterialModule, MdInputModule, FormsModule, ReactiveFormsModule, MdDialogModule, MdSnackBarModule],
} )
export class AitMaterialModule { }
