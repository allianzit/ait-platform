import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { ObservableMedia } from '@angular/flex-layout';
import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { MdSnackBar, MdSnackBarRef, SimpleSnackBar } from '@angular/material';

import 'rxjs/operator/map';
import 'rxjs/operator/filter';
import 'rxjs/operator/catch';

import { environment } from '../../environments/environment';
import { HomeService } from './shared/home.service';
import { PortalConfig } from './shared/portal-config';
import { AitUser } from '../ait/model/ait-user';

@Component( {
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
} )
export class HomeComponent implements OnInit {

    public mediaState = '';
    public environment = environment;
    public user: AitUser = new AitUser();

    timeoutMsg: MdSnackBarRef<SimpleSnackBar>;

    constructor( private http: Http, private homeService: HomeService, public media: ObservableMedia, private idle: Idle, private keepalive: Keepalive, private snackBar: MdSnackBar ) {
        idle.setIdle( environment.idleSession );//tiempo maximo de inactividad
        idle.setTimeout( environment.timeoutSession );//tiempo durante el cual se muestra el mensaje
        keepalive.interval( 15 );

        // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
        idle.setInterrupts( DEFAULT_INTERRUPTSOURCES );

        idle.onTimeout.subscribe(() => this.logout() );
        idle.onTimeoutWarning.subscribe(( countdown ) => {
            if ( !this.timeoutMsg ) {
                this.timeoutMsg = snackBar.open( 'Tu sesión se cerrará automáticamente en ' + countdown + ' segundos por inactividad!', null, {
                    duration: 5000,
                } );
            } else {
                this.timeoutMsg.instance.message = 'Tu sesión se cerrará automáticamente en ' + countdown + ' segundos por inactividad!';
            }
        } );
    }

    ngOnInit(): void {
        this.media.asObservable()
            .subscribe(( change ) => {
                this.mediaState = change ? `'${change.mqAlias}' = (${change.mediaQuery})` : '';
            } );

        this.homeService.getUserInfo()
            .subscribe(( userInfo ) => {
                this.user = userInfo;
                this.idle.watch();
            },
            error => console.log( error ) );
    }

    getSidenavMode() {
        return this.media.isActive( 'gt-sm' ) ? 'side' : 'over';
    }

    logout() {
        this.homeService.appLogout()
            .subscribe(( ok ) => {
                if ( ok ) {
                    this.user = new AitUser();
                    this.idle.stop();
                }
            },
            error => console.log( error ) );
    }

    toggleDarkTheme() {
        this.homeService.isDarkTheme = !this.homeService.isDarkTheme;
    }

    isDarkTheme() {
        return this.homeService.isDarkTheme;
    }

}
