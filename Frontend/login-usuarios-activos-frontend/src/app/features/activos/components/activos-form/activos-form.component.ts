import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ActivosService } from '../../services/activos.service';
import { ActivoTecnologicoRequestDTO } from '../../models/activo.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-activos-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './activos-form.component.html'
})

export class ActivosFormComponent implements OnInit {

  activoForm: FormGroup;
  isEditMode = false;
  id: number | null = null;

  // Listas desplegables
  modelos: any[] = [];
  proveedores: any[] = [];
  empleados: any[] = [];
  estados: any[] = [];

  constructor(
    private fb: FormBuilder,
    private activosService: ActivosService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.activoForm = this.fb.group({
      codigoInventario: ['', Validators.required],
      codigoTecnico: [''],
      nombre: ['', Validators.required],
      descripcion: [''],
      idModeloArticulo: [null, Validators.required],
      idProveedor: [null, Validators.required],
      costo: [0, [Validators.required, Validators.min(0)]],
      fechaAdquisicion: ['', Validators.required],
      garantiaMeses: [0, Validators.min(0)],
      fechaVencimientoGarantia: [''],
      idEstado: [null, Validators.required],
      idEmpleado: [null, Validators.required],
      observaciones: [''],
      usuarioCreacion: ['admin'], // Cambiar por usuario autenticado
      usuarioModificacion: [''],
      activo: [true]
    });
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.isEditMode = !!this.id;

    this.cargarDatosRelacionados();

    if (this.isEditMode) {
      this.activosService.obtenerPorId(this.id).subscribe(data => {
        this.activoForm.patchValue(data);
      });
    }
  }

  cargarDatosRelacionados(): void {
    // Idealmente deberían ser servicios separados (modeloService, proveedorService, etc.)
    this.activosService.getModelos().subscribe(data => this.modelos = data);
    this.activosService.getProveedores().subscribe(data => this.proveedores = data);
    this.activosService.getEmpleados().subscribe(data => this.empleados = data);
    this.activosService.getEstados().subscribe(data => this.estados = data);
  }

  guardar(): void {
    if (this.activoForm.invalid) {
      this.activoForm.markAllAsTouched();
      return;
    }

    const dto: ActivoTecnologicoRequestDTO = this.activoForm.value;

  if (this.isEditMode) {
    this.activosService.actualizar(this.id!, dto).subscribe(() => {
      this.router.navigate(['/admin/activos']);
    });
  } else {
    this.activosService.crear(dto).subscribe(() => {
      this.router.navigate(['/admin/activos']);
    });
    }

  }

cancelar(): void {
  this.router.navigate(['/admin/activos']);
}
}
