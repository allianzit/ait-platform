import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';
import { AitMenuItem } from '../../../model/index';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-ait-menu-item',
  templateUrl: 'menu-item.component.html',
  styleUrls: ['menu-item.component.css'],
  moduleId: module.id
})
export class MenuItemComponent implements OnInit {
  @Input()
  public item: AitMenuItem;

  @Input()
  sideNav: MatSidenav;

  @Input()
  public level = 0;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  public goTo(item: AitMenuItem) {
    if (item.path) {
      this.goToPath(item.path);
    } else {
      item.expanded = !item.expanded;
    }
  }

  public goToPath(path: string) {
    this.sideNav.toggle();
    setTimeout(() => this.router.navigate([path]), 50);
  }

  public hasChildren(item: AitMenuItem) {
    return item.children && item.children.length > 0;
  }

  public getStyle() {
    const value = this.level * 10;
    return { 'padding-left': (value) + 'px;'/*, 'background-color': 'rgb('+(240-value)+','+(240-value)+','+(240-value)+');'*/ };
  }

}
