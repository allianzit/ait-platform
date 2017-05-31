export class MenuItem {
  constructor(
    public id: number,
    public enabled: boolean = true,
    public title: string,
    public description: string,
    public order: number,
    public icon: string,
    public path: string,
    public children: MenuItem[],
    public expanded: boolean = false,
  ) {
    this.expanded = false;
  }

}
