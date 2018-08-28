import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'ait-hover-wrapper',
  templateUrl: './ait-hover-wrapper.component.html',
  styleUrls: ['./ait-hover-wrapper.component.css']
})
export class AitHoverWrapperComponent implements OnInit {

  @Input()
  show: boolean = false;
  @Input()
  tittle: string;

  @Input()
  bodyContent: string;

  showHelp: boolean = false;
  border: string = 'none';

  constructor() { }

  ngOnInit() {
  }

  changeStyle($event) {
    this.border = this.show && $event.type == 'mouseenter' ? 'dotted' : 'none';
  }
}



