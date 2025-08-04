import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivosListComponent } from './components/activos-list/activos-list.component';
import { ActivosFormComponent } from './components/activos-form/activos-form.component';
import { ActivosRoutingModule } from './activos-routing.module';

@NgModule({
  declarations: [
    ActivosListComponent,
    ActivosFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ActivosRoutingModule
  ]
})
export class ActivosModule { }
