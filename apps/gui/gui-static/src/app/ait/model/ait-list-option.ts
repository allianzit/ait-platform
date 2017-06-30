import { AitListType } from './ait-list-type';
import { AitListOptionProperty } from './ait-list-option-property';

export class AitListOption {
    constructor(
        public id: number,
        public enabled: boolean,
        public name: string,
        public internalCode: string,
        public externalCode: string,
        public listType: AitListType,
        public parent: AitListOption,
        public properties: AitListOptionProperty[],
    ) { }

}
