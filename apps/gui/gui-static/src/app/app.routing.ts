import { RouterModule, Routes } from '@angular/router';

import { DemoComponent } from './demo/demo.component';

const appRoutes: Routes = [
  //  { path: '', component: AppComponent },
  { path: 'demo', component: DemoComponent },
  {
    path: '',
    redirectTo: '/',
    pathMatch: 'full'
  },
  //  { path: '**', component: PageNotFoundComponent }
];

export const routing = RouterModule.forRoot(appRoutes);
