
import {throwError as observableThrowError,  Observable } from 'rxjs';

import {catchError, map} from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { HomeService } from '../services/home.service';

@Injectable()
export class CanActivateGuard implements CanActivate {

  constructor(private router: Router, private homeService: HomeService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.homeService.getUserInfo().pipe(map(
      (userInfo) => {
        for (let role of route.data.roles) {
          if (userInfo.roles.includes(role)) {
            return true;
          }
        }
        this.router.navigate(["/404"]);
        return false;
      }
    ),catchError((error) => {
      this.router.navigate(["/404"]);
      return observableThrowError(error);
    }),);
  }
}
