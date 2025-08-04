import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActivosListComponent } from './components/activos-list/activos-list.component';
import { ActivosFormComponent } from '../activos/components/activos-form/activos-form.component';

const routes: Routes = [
  { path: '', component: ActivosListComponent },
  { path: 'crear', component: ActivosFormComponent },
  { path: ':id/editar', component: ActivosFormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ActivosRoutingModule {}
