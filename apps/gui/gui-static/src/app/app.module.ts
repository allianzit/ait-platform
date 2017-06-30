import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { AitMaterialModule } from './ait/ait-material/ait-material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { Ng2OrderModule } from 'ng2-order-pipe';
import { CurrencyMaskModule } from "ng2-currency-mask";
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive'; // this includes the core NgIdleModule but includes keepalive providers for easy wireup
import { MomentModule } from 'angular2-moment'; // optional, provides moment-style pipes for date formatting

import { routing } from './app.routing';
import { HomeComponent } from './home/home.component';
import { DemoComponent } from './demo/demo.component';
import { HomeService } from './home/shared/home.service';
import { CanActivateGuard } from './home/shared/can-activate-guard';

import { MenuListComponent } from './home/menu/menu-list/menu-list.component';
import { MenuItemComponent } from './home/menu/menu-item/menu-item.component';

import { AitConfirmComponent } from './ait/dialog/ait-confirm/ait-confirm.component';
import { AitPageNotFoundComponent } from './ait/page-not-found/ait-page-not-found.component';

@NgModule( {
    declarations: [
        HomeComponent,
        DemoComponent,
        MenuListComponent,
        MenuItemComponent,
        AitConfirmComponent,
        AitPageNotFoundComponent,
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
        routing,
        CurrencyMaskModule,
        MomentModule,
        NgIdleKeepaliveModule.forRoot()
    ],
    entryComponents: [
        AitConfirmComponent
    ],
    providers: [HomeService, CanActivateGuard],
    bootstrap: [HomeComponent]
} )
export class AppModule { }
