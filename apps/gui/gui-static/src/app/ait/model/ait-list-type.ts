
export class AitListType {
  constructor(
    public id: number,
    public enabled: boolean,
    public editable: boolean,
    public allowMultiple: boolean,
    public code: string,
    public name: string,
    public description: string,
    public icon: string,
    public valuesByReference: boolean,
    public parent: AitListType
  ) { }

}
