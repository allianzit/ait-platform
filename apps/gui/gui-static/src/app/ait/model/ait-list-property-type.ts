import { AitListType } from './ait-list-type';

export class AitListPropertyType {
    constructor(
        public id: number,
        public enabled: boolean,
        public allowMultiple: boolean,
        public required: boolean,
        public code: string,
        public name: string,
        public icon: string,
        public type: any,
        public mask: string,
        public listType: AitListType,
        public validOptions: AitListType
    ) { }

}
