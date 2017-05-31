// http://swimlane.github.io/ngx-datatable/
import { Component, OnInit, ViewChild } from '@angular/core';
import { DatatableComponent } from '@swimlane/ngx-datatable';

@Component( {
    selector: 'app-demo',
    templateUrl: './demo.component.html',
    styleUrls: ['./demo.component.css']
} )
export class DemoComponent implements OnInit {

    @ViewChild( DatatableComponent ) table: DatatableComponent;

    public loadingIndicator = false;
    rows = [
        { name: 'Austin', gender: 'Male', company: 'Swimlane' },
        { name: 'Dany', gender: 'Male', company: 'KFC' },
        { name: 'Molly', gender: 'Female', company: 'Burger King' },
        { name: 'Molly2', gender: 'Female', company: 'Burger King2' },
        { name: 'Molly3', gender: 'Female', company: 'Burger King3' },
        { name: 'Molly4', gender: 'Female', company: 'Burger King4' },
        { name: 'Molly5', gender: 'Female', company: 'Burger King5' },
    ];
    columns = [
        { prop: 'name', value: 'Nombre' },
        { prop: 'gender', value: 'Genero' },
        { prop: 'company', value: 'Pais' }
    ];

    temp = [...this.rows];



    constructor() { }

    ngOnInit() {
    }

    updateFilter( event ) {
        this.loadingIndicator = true;

        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.temp.filter( function( d ) {
            let values = Object.values( d );
            var ok = false;
            for ( let value of values ) {
                ok = String( value ).toLowerCase().indexOf( val ) !== -1 || !val;
                if ( ok ) {
                    break;
                }
            }
            return ok;
        } );

        // update the rows
        this.rows = temp;
        // Whenever the filter changes, always go back to the first page
        this.table.offset = 0;
        setTimeout(() => this.loadingIndicator = false, 150 );
    }

}
