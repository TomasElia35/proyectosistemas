import { Routes } from '@angular/router';
import { ActivosListComponent } from './components/activos-list/activos-list.component';
import { ActivosFormComponent } from './components/activos-form/activos-form.component';

export const ROUTES: Routes = [
  { path: '', component: ActivosListComponent },
  { path: 'nuevo', component: ActivosFormComponent },
  { path: ':id/editar', component: ActivosFormComponent }
];
