import { PortalConfig } from './portal-config';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { AitUser } from '../../ait/model/ait-user';
import { environment } from '../../../environments/environment';

@Injectable()
export class HomeService {
    public isDarkTheme = false;

    constructor( private http: Http ) { }

    public getStoredItem( itemName ): any {
        let item = localStorage.getItem( itemName );
        if ( item ) {
            try {
                return JSON.parse( item );
            } catch ( e ) {
                localStorage.removeItem( itemName );
            }
        }
        return null;
    }

    private getItem( itemName ): Observable<any> {
        let item = localStorage.getItem( itemName );
        if ( item ) {
            try {
                return Observable.of( JSON.parse( item ) );
            } catch ( e ) {
                localStorage.removeItem( itemName );
            }
        }
        return null;
    }
    public getUserInfo(): Observable<AitUser> {
        let userInfo = this.getItem( 'userInfo' );
        if ( userInfo ) {
            return userInfo;
        }
        return this.http.get( `${environment.apiPath}common/secure/me` )
            .map(( res: Response ) => {
                const userInfo = this.extractData( res );
                localStorage.setItem( 'userInfo', JSON.stringify( userInfo ) );
                this.getPortalConfig();
                return userInfo;
            } )
            .catch( this.handleError );
    }

    public getPortalConfig() {
        this.http.get( `${environment.apiPath}common/secure/portalConfig` )
            .map(( res: Response ) => {
                const portalConfig = this.extractData( res );
                localStorage.setItem( 'portalConfig', JSON.stringify( portalConfig ) );
            } ).catch( this.handleError );
    }

    public appLogout(): Observable<boolean> {
        return this.http.post( `${environment.apiPath}logout`, {} )
            .map(( resp ) => {
                if ( resp.status === 200 ) {
                    const portalConfig = this.getStoredItem( 'portalConfig' );
                    const url: string = `${portalConfig.keyCloakUrl}logout?redirect_uri=${portalConfig.redirectUri}`;
                    window.location.href = url;
                    localStorage.removeItem( 'userInfo' );
                    return true;
                }
                return false;
            } )
            .catch( this.handleError );
    }

    private extractData( res: Response ) {
        const body = res.json();
        return body || {};
    }

    private handleError( error: Response | any ) {
        // In a real world app, you might use a remote logging infrastructure
        let errMsg: string;
        if ( error instanceof Response ) {
            const body = error.json() || '';
            const err = body.error || JSON.stringify( body );
            errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
        } else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error( errMsg );
        return Observable.throw( errMsg );
    }
}
