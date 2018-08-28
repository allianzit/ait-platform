import { AitMenuItem } from '../../../model/index';
import { Component, Input } from '@angular/core';
import { MatSidenav } from '@angular/material';

@Component({
  selector: 'app-ait-menu-list',
  templateUrl: 'menu-list.component.html',
  styleUrls: ['menu-list.component.css'],
  moduleId: module.id
})
export class MenuListComponent {

  @Input()
  menu: AitMenuItem[];

  @Input()
  sideNav: MatSidenav;

}
