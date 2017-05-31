import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { ObservableMedia } from '@angular/flex-layout';

import 'rxjs/operator/map';
import 'rxjs/operator/filter';
import 'rxjs/operator/catch';

import { environment } from '../../environments/environment';
import { HomeService } from './shared/home.service';
import { PortalConfig } from './shared/portal-config';
import { UserInfo } from './shared/user-info';

@Component( {
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
} )
export class HomeComponent implements OnInit {

    public environment = environment;
    public mediaState = '';
    private portalConfig: PortalConfig;

    public user: UserInfo = new UserInfo( 0, null, null, null, null, [] );

    constructor( private http: Http, private homeService: HomeService, public media: ObservableMedia ) { }

    ngOnInit(): void {
        this.media.asObservable()
            .subscribe(( change ) => {
                this.mediaState = change ? `'${change.mqAlias}' = (${change.mediaQuery})` : '';
            } );

        this.homeService.getUserInfo()
            .subscribe(( userInfo ) => {
                this.user = userInfo;
                this.homeService.getPortalConfig()
                    .subscribe(( config ) => {
                        this.portalConfig = config;
                    },
                    error => console.log( error ) );
            },
            error => console.log( error ) );

    }

    getSidenavMode() {
        return this.media.isActive( 'gt-sm' ) ? 'side' : 'over';
    }

    logout() {
        this.homeService.appLogout( this.portalConfig )
            .subscribe(( ok ) => {
                if ( ok ) {
                    this.user = new UserInfo( 0, null, null, null, null, [] );
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
