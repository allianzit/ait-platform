import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { AitMaterialModule } from './ait-material/ait-material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { routing } from './app.routing';

import { HomeComponent } from './home/home.component';
import { DemoComponent } from './demo/demo.component';
import { HomeService } from './home/shared/home.service';
import { MenuListComponent } from './home/menu/menu-list/menu-list.component';
import { MenuItemComponent } from './home/menu/menu-item/menu-item.component';
import { Ng2OrderModule } from 'ng2-order-pipe';

@NgModule({
  declarations: [
    HomeComponent,
    DemoComponent,
    MenuListComponent,
    MenuItemComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    AitMaterialModule,
    FlexLayoutModule,
    Ng2OrderModule,
    NgxDatatableModule,
    routing
  ],
  providers: [HomeService],
  bootstrap: [HomeComponent]
})
export class AppModule { }
