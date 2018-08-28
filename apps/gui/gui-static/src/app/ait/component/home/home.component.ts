import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';

import { ObservableMedia } from '@angular/flex-layout';
import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { MatSnackBar, MatSnackBarRef } from '@angular/material';

import { Observable } from 'rxjs';




import { environment } from 'environments/environment';
import { AitSnackbarComponent } from '../snackbar/ait-snackbar.component';
import { AitPortalConfig, AitUser } from '../../model/index';
import { HomeService } from '../../services/index';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public mediaState = '';
  public environment = environment;
  public user: AitUser = new AitUser();
  public showHelp: boolean = false;

  timeoutMsg: MatSnackBarRef<AitSnackbarComponent>;

  constructor(private http: Http, private homeService: HomeService, public router: Router, public activatedRoute: ActivatedRoute, public media: ObservableMedia, private idle: Idle, private keepalive: Keepalive, private snackBar: MatSnackBar) {
    idle.setIdle(environment.idleSession);//tiempo maximo de inactividad
    idle.setTimeout(environment.timeoutSession);//tiempo durante el cual se muestra el mensaje
    keepalive.interval(15);

    // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
    idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    idle.onTimeout.subscribe(() => this.logout());
    idle.onTimeoutWarning.subscribe((countdown) => {
      if (!this.timeoutMsg) {
        //si no esta abierto el snackbar
        this.timeoutMsg = snackBar.openFromComponent(AitSnackbarComponent, {
          data: {
            message: `Tu sesión se cerrará automáticamente en ${countdown} segundos por inactividad!`,
            msgType: 'warning',
            html: true
          },
          duration: 150000,
          verticalPosition: 'top'
        });

        snackBar.open('Tu sesión se cerrará automáticamente en ' + countdown + ' segundos por inactividad!', null, {
          duration: 5000,
        });
      } else {
        this.timeoutMsg.instance.data.message = 'Tu sesión se cerrará automáticamente en ' + countdown + ' segundos por inactividad!';
      }
    });
    idle.onIdleEnd.subscribe(() => {
      localStorage.setItem('lastActiveTime', JSON.stringify(new Date().getTime()));
    });
  }

  ngOnInit(): void {
    var that = this;
    this.media.asObservable()
      .subscribe((change) => {
        this.mediaState = change ? `'${change.mqAlias}' = (${change.mediaQuery})` : '';
      });

    this.homeService.getUserInfo()
      .subscribe((userInfo) => {
        this.homeService.getPortalConfig()
          .subscribe((portalConfig) => {
            that.user = userInfo;
            that.idle.watch();
            localStorage.setItem('lastActiveTime', JSON.stringify(new Date().getTime()));
            if (that.hasRole('RTE_ADMIN') && this.router.url == '/') {
              that.router.navigate(['rte-dashboard']);
            }
          },
            error => console.log(error));
      },
        error => console.log(error));
  }

  getSidenavMode() {
    return this.media.isActive('gt-sm') ? 'side' : 'over';
  }

  logout() {
    this.homeService.appLogout()
      .subscribe((ok) => {
        if (ok) {
          this.user = new AitUser();
          localStorage.setItem('lastTime', JSON.stringify(new Date().getTime()));
          this.idle.stop();
          console.log(this.router.url);
          this.router.navigate(['']);
        }
      },
        error => console.log(error));
  }

  hasRole(role: string): boolean {
    return this.user.roles.includes(role);
  }

}
