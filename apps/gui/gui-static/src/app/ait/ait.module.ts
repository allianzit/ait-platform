
import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule, JsonpModule } from '@angular/http';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive'; // this includes the core NgIdleModule but includes keepalive providers for easy wireup
import { MomentModule } from 'angular2-moment'; // optional, provides moment-style pipes for date formatting
import { NgxPopperModule } from 'ngx-popper';

import { AitMaterialModule } from './material/ait-material.module';

import { AitUppercaseDirective, AitCurrencyFormatterDirective } from './directives/index';
import { AitCurrencyPipe } from './pipes/ait-currency.pipe';
import { HomeComponent, MenuListComponent, MenuItemComponent, AitConfirmComponent, AitSnackbarComponent, AitPageNotFoundComponent, AitTipComponent, AitHoverWrapperComponent, AitLoaderComponent, AitUsersComponent, AitUsersListComponent, AitUserDetailComponent } from './component/index';
import { HomeService, CanActivateGuard } from './services/index';

export * from './directives/index';
export * from './component/index';
export * from './model/index';
export { AitCurrencyPipe } from './pipes/ait-currency.pipe';
export { HomeService, CanActivateGuard } from './services/index';



import localeEs from '@angular/common/locales/es';
registerLocaleData(localeEs);

@NgModule({
  //recursos internos
  imports: [
    CommonModule,
    AitMaterialModule,
    MomentModule,
    RouterModule,
    BrowserModule,
    HttpClientModule,
    HttpModule,
    JsonpModule,
    NgxPopperModule,

    NgIdleKeepaliveModule.forRoot()
  ],
  //recursos 
  declarations: [
    HomeComponent,
    MenuListComponent,
    MenuItemComponent,
    AitConfirmComponent,
    AitLoaderComponent,
    AitSnackbarComponent,
    AitPageNotFoundComponent,
    AitTipComponent,
    AitHoverWrapperComponent,
    AitCurrencyPipe,
    AitUppercaseDirective,
    AitCurrencyFormatterDirective,
    AitUsersComponent,
    AitUsersListComponent,
    AitUserDetailComponent
  ],
  entryComponents: [
    AitConfirmComponent,
    AitSnackbarComponent,
    AitLoaderComponent
  ],
  //servici exportados
  providers: [
    AitCurrencyPipe,
    HomeService,
    CanActivateGuard,
  ],
  //componentes exportados
  exports: [
    AitMaterialModule,
    MomentModule,
    RouterModule,
    BrowserModule,
    HttpClientModule,
    HttpModule,
    JsonpModule,
    NgxPopperModule,

    HomeComponent,
    MenuListComponent,
    MenuItemComponent,
    AitConfirmComponent,
    AitLoaderComponent,
    AitSnackbarComponent,
    AitPageNotFoundComponent,
    AitTipComponent,
    AitHoverWrapperComponent,
    AitCurrencyPipe,
    AitUppercaseDirective,
    AitCurrencyFormatterDirective,
    AitUsersComponent,
    AitUsersListComponent,
    AitUserDetailComponent
  ]
})
export class AitModule { }
