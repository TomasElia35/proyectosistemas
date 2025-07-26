// src/app/admin/usuarios/crear-usuario/crear-usuario.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../usuario.service';
import { AuthService } from '../../../auth/auth.service';
import { UsuarioRequest } from '../../../shared/models/usuario.model';
import { Rol } from '../../../shared/models/rol.model';

@Component({
  selector: 'app-crear-usuario',
  templateUrl: './crear-usuario.component.html',
  styleUrls: ['./crear-usuario.component.css']
})
export class CrearUsuarioComponent implements OnInit {
  usuarioForm!: FormGroup;
  loading = false;
  error = '';
  roles: Rol[] = [];
  currentUser = this.authService.currentUserValue;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.cargarRoles();
  }

  initForm(): void {
    this.usuarioForm = this.formBuilder.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      mail: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      rol: ['', Validators.required],
      estado: [true]
    });
  }

  cargarRoles(): void {
    this.usuarioService.obtenerRoles().subscribe({
      next: (response) => {
        if (response.exito) {
          this.roles = response.datos || [];
        } else {
          this.error = 'Error al cargar roles';
        }
      },
      error: () => {
        this.error = 'Error al cargar roles';
      }
    });
  }

  onSubmit(): void {
    if (this.usuarioForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const usuarioData: UsuarioRequest = {
      nombre: this.usuarioForm.value.nombre,
      apellido: this.usuarioForm.value.apellido,
      mail: this.usuarioForm.value.mail,
      contrasena: this.usuarioForm.value.contrasena,
      rol: parseInt(this.usuarioForm.value.rol),
      estado: this.usuarioForm.value.estado
    };

    this.usuarioService.crearUsuario(usuarioData).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.exito) {
          alert('Usuario creado exitosamente');
          this.router.navigate(['/admin/usuarios']);
        } else {
          this.error = response.mensaje;
        }
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.mensaje || 'Error al crear usuario';
      }
    });
  }

  volver(): void {
    this.router.navigate(['/admin/usuarios']);
  }

  logout(): void {
    this.authService.logout();
  }
}