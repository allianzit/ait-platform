export class AitMenuItem {
  constructor(
    public id: number,
    public enabled: boolean = true,
    public title: string,
    public description: string,
    public order: number,
    public icon: string,
    public path: string,
    public children: AitMenuItem[],
    public expanded: boolean = false,
  ) {
    this.expanded = false;
  }

}
