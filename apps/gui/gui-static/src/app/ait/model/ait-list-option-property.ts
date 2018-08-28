import { AitListOption } from './ait-list-option';
import { AitListPropertyType } from './ait-list-property-type';

export class AitListOptionProperty {
  constructor(
    public id: number,
    public enabled: boolean,
    public textValue: string,
    public numberValue: number,
    public dateValue: any,
    public option: AitListOption,
    public propertyType: AitListPropertyType
  ) { }

}
