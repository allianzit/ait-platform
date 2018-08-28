
import { throwError as observableThrowError, of as observableOf, Observable } from 'rxjs';

import { catchError, map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { HttpErrorResponse } from '@angular/common/http';



import { AitUser, AitPortalConfig, AitRole } from '../model/index';
import { AitLoaderComponent } from '../component/loader/ait-loader.component';
import { environment } from 'environments/environment';
import { MatDialog, MatDialogRef } from '@angular/material';
import { AitConfirmComponent } from 'app/ait/component/confirm/ait-confirm.component';

@Injectable()
export class HomeService {
  public isDarkTheme = false;

  constructor(private http: Http, private dialog: MatDialog) { }

  public getStoredItem(itemName): any {
    let item = localStorage.getItem(itemName);
    if (item) {
      try {
        return JSON.parse(item);
      } catch (e) {
        localStorage.removeItem(itemName);
      }
    }
    return null;
  }

  private getItem(itemName): Observable<any> {
    let item = localStorage.getItem(itemName);
    if (item) {
      try {
        return observableOf(JSON.parse(item));
      } catch (e) {
        localStorage.removeItem(itemName);
      }
    }
    return null;
  }
  public getUserInfo(): Observable<AitUser> {
    let that = this;
    let lastActiveTime = this.getStoredItem('lastActiveTime');
    if (lastActiveTime) {
      if ((new Date().getTime() - lastActiveTime) < environment.timeoutSession * 1000) {
        let userInfo = this.getItem('userInfo');
        if (userInfo) {
          return userInfo;
        }
      }
    }
    return this.http.get(`${environment.apiPath}common/secure/me`).pipe(
      map((res: Response) => {
        const userInfo = this.extractData(res);
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        return userInfo;
      }),
      catchError((error: HttpErrorResponse) => {
        return null;// this.handleError(error, this.dialog);
      }), );
  }

  public getUsersByRole(...roles: string[]): Observable<AitUser[]> {
    return this.http.get(`${environment.apiPath}common/secure/user/byRole/${roles.join()}`).pipe(
      map(this.extractData),
      catchError((error: HttpErrorResponse) => {
        return this.handleError(error, this.dialog);
      }), );
  }

  public getAllUsers(): Observable<AitUser[]> {
    return this.http.get(`${environment.apiPath}common/secure/user/all`).pipe(
      map(this.extractData),
      catchError((error: HttpErrorResponse) => {
        return this.handleError(error, this.dialog);
      }), );
  }

  public getRoles(): Observable<AitRole[]> {
    //return this.http.get(`${environment.apiPath}common/secure/role/all`).pipe(
    //  map(this.extractData),
    //  catchError((error: HttpErrorResponse) => {
    //    return this.handleError(error, this.dialog);
    //  }), );
    var list = new Array<AitRole>();
    list.push({ id: 'AIT_ADMIN', description: 'Super Administrador' });
    list.push({ id: 'RTE_ADMIN', description: 'Administrador RTE' });
    list.push({ id: 'RTE_EVALUATOR', description: 'Evaluador' });
    list.push({ id: 'RTE_ASSEMBLY', description: 'Ensambladora' });
    list.push({ id: 'RTE_REQUEST', description: 'Solicitante' });
    return observableOf(list);
  }

  public saveUser(user: AitUser, updateKeycloakIfExist: boolean): Observable<AitUser> {
    return this.http.post(`${environment.apiPath}common/secure/user?updateKeycloakIfExist=${updateKeycloakIfExist}`, user).pipe(
      map(this.extractData),
      catchError((error: HttpErrorResponse) => {
        return this.handleError(error, this.dialog);
      }), );
  }


  public getPortalConfig(): Observable<AitPortalConfig> {
    return this.http.get(`${environment.apiPath}common/secure/portalConfig`).pipe(
      map((res: Response) => {
        const portalConfig = this.extractData(res);
        localStorage.setItem('portalConfig', JSON.stringify(portalConfig));
        return portalConfig;
      }),
      catchError((error: HttpErrorResponse) => {
        return this.handleError(error, this.dialog);
      }), );
  }

  public appLogout(): Observable<boolean> {
    localStorage.removeItem('userInfo');
    return this.http.post(`${environment.apiPath}logout`, {}).pipe(
      map((resp) => {
        if (resp.status === 200) {
          const portalConfig = this.getStoredItem('portalConfig');
          const url: string = `${portalConfig.keyCloakUrl}logout?redirect_uri=${portalConfig.redirectUri}`;
          window.location.href = url;
          return true;
        }
        return false;
      }),
      catchError((error: HttpErrorResponse) => {
        return this.handleError(error, this.dialog);
      }), );
  }

  private extractData(res: Response) {
    const body = res.json();
    return body || {};
  }

  private handleError(error: HttpErrorResponse, dialog: MatDialog) {
    let errMsg: string;
    let title = 'Error procesando la petición';
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errMsg = error.error.message;
    } else if (error instanceof Response) {
      try {
        const body = error.json() || '';
        errMsg = body.message || JSON.stringify(body);
        if (body.isTrusted) {
          errMsg = "Por favor inicie sesión";
        }
        if (body.errors) {
          title = errMsg;
          errMsg = '';
          for (var i = 0; i < body.errors.length; i++) {
            errMsg += `<li>${body.errors[i]}</li>`;
          }

        }
      } catch (e) {
        errMsg = `Error desconocido al procesar la petición: Status: ${error.status}`;
      }
      //            errMsg = `${error.status} - ${error.statusText || ''} ${errMsg}`;
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
      errMsg = `Error desconocido: ${error.status}, Cuerpo: ${error.error}`;
    }
    console.error('Un error ha ocurrido:', errMsg);
    this.dialog.open(AitConfirmComponent, {
      data: {
        title: title,
        message: errMsg,
        dialogType: 'error',
        html: true
      },
    });
    return observableThrowError(errMsg);
  };

  public showLoader(): MatDialogRef<AitLoaderComponent> {
    return this.dialog.open(AitLoaderComponent, { disableClose: true, panelClass: 'ait-loader', backdropClass: 'ait-backdrop' });
  }
}
