import { AbstractControl, ValidatorFn, FormControl } from '@angular/forms';

export function AitRequiredIdValidator( control: FormControl ) {
    return control.value && control.value.id > 0 ? null : { requiredId: 'Campo requerido!' };
}