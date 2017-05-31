import { MenuItem } from '../shared/menu-item';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-ait-menu-item',
  templateUrl: 'menu-item.component.html',
  styleUrls: ['menu-item.component.css'],
  moduleId: module.id
})
export class MenuItemComponent implements OnInit {
  @Input()
  public item: MenuItem;

  @Input()
  public level = 0;

  constructor() { }

  ngOnInit() {
  }

  public goTo(item: MenuItem) {
    if (item.path) {
      console.log('going to: ', item.path);
    } else {
      item.expanded = !item.expanded;
    }
  }

  public hasChildren(item: MenuItem) {
    return item.children && item.children.length > 0;
  }

  public getStyle() {
  	const value=this.level * 10;
    return { 'padding-left': (value) + 'px;'/*, 'background-color': 'rgb('+(240-value)+','+(240-value)+','+(240-value)+');'*/};
  }

}
