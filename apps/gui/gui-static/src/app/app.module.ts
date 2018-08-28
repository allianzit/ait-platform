import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import { AitModule, HomeComponent} from './ait/ait.module';

import { AppRoutingModule } from './app-routing.module';

@NgModule({
  declarations: [
],
  imports: [
    RouterModule,
    AitModule,
    AppRoutingModule
  ],
  entryComponents: [],
  providers: [],
  bootstrap: [HomeComponent]
})
export class AppModule { }
