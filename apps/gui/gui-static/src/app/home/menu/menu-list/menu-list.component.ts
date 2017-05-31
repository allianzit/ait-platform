import { MenuItem } from '../shared/menu-item';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-ait-menu-list',
  templateUrl: 'menu-list.component.html',
  styleUrls: ['menu-list.component.css'],
  moduleId: module.id
})
export class MenuListComponent {

  @Input()
  menu: MenuItem[];

}
