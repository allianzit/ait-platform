import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DataSource } from '@angular/cdk/collections';
import { Observable } from 'rxjs';

import { animate, state, style, transition, trigger } from '@angular/animations';


import { MatDialog, MatDialogRef, MatSnackBar, MatPaginator, MatSort, MatTableDataSource, MatTable } from '@angular/material';
import { AitListOption, AitEnum, AitUser } from '../../../model/index';
import { HomeService } from '../../../services/index';
import { AitSnackbarComponent } from '../../snackbar/ait-snackbar.component';

@Component({
  selector: 'ait-users-list',
  templateUrl: './ait-users-list.component.html',
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
      state('expanded', style({ height: '*', visibility: 'visible' })),
      transition('expanded <=> collapsed', animate('1225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
      transition('collapsed => expanded', animate('1225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AitUsersListComponent implements OnInit {

  displayedColumns = ['username', 'firstName', 'lastName', 'email'];
  dataSource: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild('usersTable') usersTable: MatTable<any>;
  @ViewChild(MatSort) sort: MatSort;

  filter = '';
  newUser: AitUser;
  expandedElement: any;
  expandedElementId: number;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private homeSrv: HomeService, private snackBar: MatSnackBar, private dialog: MatDialog) { }

  ngOnInit() {
    setTimeout(() => this.loadData(), 0);
  }

  private loadData(applyFilter: boolean = false) {
    this.expandedElement = null;
    this.expandedElementId = null;
    this.newUser = null;

    var dialog = this.homeSrv.showLoader();
    this.homeSrv.getAllUsers()
      .subscribe((data) => {
        this.dataSource = new MatTableDataSource();
        data.forEach(item => {
          var row = {
            data: item,
            enabled: item.enabled,
            username: item.username,
            firstName: item.firstName,
            lastName: item.lastName,
            email: item.email,
            roles: item.roles.join()
          };
          this.dataSource.data.push(row);
        });
        this.dataSource.paginator = this.paginator;
        this.dataSource.paginator._intl.itemsPerPageLabel = 'Registros por página';
        this.dataSource.paginator._intl.nextPageLabel = 'Siguiente';
        this.dataSource.paginator._intl.previousPageLabel = 'Anterior';
        this.dataSource.sort = this.sort;
        this.sort.direction = 'asc';
        this.sort.sort({ id: 'username', start: 'asc', disableClear: true });
        if (applyFilter) {
          this.applyFilter();
        }
        dialog.close();
      }, error => dialog.close());
  }

  isExpansionDetailRow = (i: number, row: Object) => row.hasOwnProperty('detailRow');

  getExpandedStatus(row) {
    if (!this.expandedElement) {
      return 'collapsed';
    }
    return row.data.id == this.expandedElement.data.id ? 'expanded' : 'collapsed';
  }

  clearFilter() {
    this.filter = '';
    this.applyFilter();
  }

  applyFilter() {
    var filterValue = this.filter.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  toNewUser() {
    this.expandedElement = null;
    this.expandedElementId = null;
    this.newUser = new AitUser();
  }

  editUser(entity: any = null) {
    if (!this.expandedElementId || this.expandedElementId != entity.data.id) {
      if (this.expandedElementId) {
        this.expandedElement = null;
        this.expandedElementId = null;
      }
      setTimeout(() => {
        this.expandedElement = entity;
        this.expandedElementId = entity.data.id;
      }, 0);
    } else {
      this.expandedElement = null;
      this.expandedElementId = null;
    }
  }

  openSnackBar(message: string, msgType = 'info', html = false) {
    this.snackBar.openFromComponent(AitSnackbarComponent, {
      data: { message: message, msgType: msgType, html: html },
      duration: 150000,
      verticalPosition: 'top'
    });
  }

  save(entity: AitUser) {
    this.openSnackBar('Información del usuario guardada correctamente');
    this.loadData(true);
  }

  cancel(user: AitUser) {
    if (this.expandedElementId) {
      this.dataSource.data.splice(this.expandedElementId, 1);
      this.expandedElement = null;
      this.expandedElementId = null;
    } else {
      this.newUser = null;
    }
  }
}

