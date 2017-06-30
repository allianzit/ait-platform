import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { HomeService } from './home.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CanActivateGuard implements CanActivate {

    constructor( private router: Router, private homeService: HomeService ) { }

    canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
        return this.homeService.getUserInfo().map(
            ( userInfo ) => {
                for ( let role of route.data.roles ) {
                    if ( userInfo.roles.includes( role ) ) {
                        return true;
                    }
                }
                this.router.navigate( ["/404"] );
                return false;
            }
        ).catch(( error ) => {
            this.router.navigate( ["/404"] );
            return Observable.throw( error );
        } );
    }
}