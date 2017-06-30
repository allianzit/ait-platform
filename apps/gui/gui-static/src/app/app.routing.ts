import { RouterModule, Routes } from '@angular/router';
import { AitPageNotFoundComponent } from './ait/page-not-found/ait-page-not-found.component';

import { DemoComponent } from './demo/demo.component';
import { CanActivateGuard } from './home/shared/can-activate-guard';

const appRoutes: Routes = [
    { path: 'demo', component: DemoComponent },
    { path: '', redirectTo: '/', pathMatch: 'full' },
    { path: '404', component: AitPageNotFoundComponent },
];

export const routing = RouterModule.forRoot( appRoutes );
