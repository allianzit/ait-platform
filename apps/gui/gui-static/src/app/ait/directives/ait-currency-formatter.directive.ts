import { Directive, HostListener, ElementRef, OnInit } from "@angular/core";
import { AitCurrencyPipe } from "../pipes/ait-currency.pipe";

@Directive({ selector: "[aitCurrencyFormatter]" })
export class AitCurrencyFormatterDirective implements OnInit {

  private el: any;

  constructor(private elementRef: ElementRef, private currencyPipe: AitCurrencyPipe) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit() {
    this.el.value = this.currencyPipe.transform(this.el.value);
  }

  @HostListener("focus", ["$event.target.value"])
  onFocus(value) {
    this.el.value = this.currencyPipe.parse(value); // opossite of transform
  }

  @HostListener("blur", ["$event.target.value"])
  onBlur(value) {
    this.el.value = this.currencyPipe.transform(value);
  }

  @HostListener('input', ['$event'])
  onInput(event: Event) {
    this.elementRef.nativeElement.value = (<HTMLInputElement>event.currentTarget).value.replace(/[^0-9.]/g, '');
  }
}
