import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CanActivateGuard, AitPageNotFoundComponent} from 'app/ait/ait.module';

const routes: Routes = [
  { path: '', redirectTo: '/', pathMatch: 'full' },
  { path: '404', component: AitPageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
