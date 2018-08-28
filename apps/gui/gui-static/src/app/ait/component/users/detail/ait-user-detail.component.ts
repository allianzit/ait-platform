import { ViewChild, Component, Input, Output, OnInit, EventEmitter, HostListener } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DataSource } from '@angular/cdk/collections';
import { Observable } from 'rxjs';

import { MatDialog, MatDialogRef, MatSnackBar, MatPaginator, MatSort, MatTableDataSource, MatTable } from '@angular/material';
import { HomeService } from '../../../services/index';
import { AitUser, AitRole } from '../../../model/index';
import { AitSnackbarComponent } from '../../snackbar/ait-snackbar.component';
import { AitConfirmComponent } from '../../confirm/ait-confirm.component';


@Component({
  selector: 'ait-user-detail',
  templateUrl: './ait-user-detail.component.html'
})
export class AitUserDetailComponent implements OnInit {


  @Input()
  user: AitUser;

  @Output()
  onSave = new EventEmitter();

  @Output()
  onCancel = new EventEmitter();

  filter: string = '';
  mobile: boolean = false;
  updateKeycloakIfExist: boolean = true;
  roles: AitRole[];
  newRoles: AitRole[] = [];

  displayedColumns = ['username', 'firstName', 'lastName', 'email', 'opc'];
  dataSource: MatTableDataSource<any>;
  @ViewChild('usersTable') entityTable: MatTable<any>;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private homeSrv: HomeService, private snackBar: MatSnackBar, private dialog: MatDialog) { }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.mobile = event.target.innerWidth <= 960;
    this.updateKeycloakIfExist = true;
  }

  ngOnInit() {
    setTimeout(() => this.loadData(), 0);
  }

  private loadData() {
    var dialog = this.homeSrv.showLoader();
    this.homeSrv.getRoles()
      .subscribe((data) => {
        this.roles = data;
        this.newRoles = [];
        if (this.user.roles) {
          data.forEach(r => {
            this.user.roles.forEach(role => {
              if (role == r.id) {
                this.newRoles.push(r);
              }
            });
          });
        }
        dialog.close();
      }, error => dialog.close());
  }

  openSnackBar(message: string, msgType = 'info', html = false) {
    this.snackBar.openFromComponent(AitSnackbarComponent, {
      data: { message: message, msgType: msgType, html: html },
      duration: 150000,
      verticalPosition: 'top'
    });
  }

  save(form: any) {
    var dialog = this.homeSrv.showLoader();

    var newUser: AitUser = JSON.parse(JSON.stringify(this.user));
    newUser.roles = [];
    this.newRoles.forEach(r => {
      newUser.roles.push(r.id);
    });
    newUser.username = newUser.username.toLowerCase();
    newUser.firstName = newUser.firstName.toUpperCase();
    newUser.lastName = newUser.lastName ? newUser.lastName.toUpperCase() : '';
    newUser.email = newUser.email.toLowerCase();

    this.homeSrv.saveUser(newUser, this.updateKeycloakIfExist)
      .subscribe((data) => {
        this.onSave.emit(newUser);
        dialog.close();
      }, error => dialog.close());
  }

  cancel(form: any) {
    if (!form.pristine) {
      this.dialog.open(AitConfirmComponent, {
        disableClose: true,
        data: {
          title: 'Descartar cambios',
          message: 'Confirma que desea descartar los cambios realizados al usuario?',
          dialogType: 'confirm'
        },
      }).afterClosed().subscribe(result => {
        if (result == true) {
          this.onCancel.emit(this.user);
        }
      });
    } else {
      this.onCancel.emit(this.user);
    }
  }

  compareRoles(o1: any, o2: any): boolean {
    return o1 && o2 && o1.id === o2.id;
  }

}

