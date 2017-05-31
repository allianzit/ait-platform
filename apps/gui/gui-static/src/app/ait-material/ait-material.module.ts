import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule, MdInputModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

@NgModule({
  imports: [BrowserAnimationsModule, MaterialModule, MdInputModule],
  exports: [BrowserAnimationsModule, MaterialModule, MdInputModule],
})
export class AitMaterialModule { }
