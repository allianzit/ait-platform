import { PortalConfig } from './portal-config';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { UserInfo } from './user-info';
import { environment } from '../../../environments/environment';

@Injectable()
export class HomeService {
    public isDarkTheme = false;
    
    constructor( private http: Http ) { }

    public getUserInfo(): Observable<UserInfo> {
        return this.http.get( environment.apiPath + 'common/secure/me' )
            .map( this.extractData )
            .catch( this.handleError );
    }

    public getPortalConfig(): Observable<PortalConfig> {
        return this.http.get( environment.apiPath + 'common/secure/portalConfig' )
            .map( this.extractData ).catch( this.handleError );
    }

    public appLogout( portalConfig: PortalConfig ): Observable<boolean> {
        return this.http.post( environment.apiPath + 'logout', {} )
            .map(( resp ) => {
                if ( resp.status === 200 ) {
                    console.log( portalConfig );
                    const url: string = portalConfig.keyCloakUrl + 'logout' + '?redirect_uri=' + portalConfig.redirectUri;
                    window.location.href = url;
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
