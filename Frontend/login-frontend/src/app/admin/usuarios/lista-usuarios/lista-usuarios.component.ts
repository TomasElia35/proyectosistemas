// src/app/admin/usuarios/lista-usuarios/lista-usuarios.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../usuario.service';
import { AuthService } from '../../../auth/auth.service';
import { Usuario } from '../../../shared/models/usuario.model';

@Component({
  selector: 'app-lista-usuarios',
  templateUrl: './lista-usuarios.component.html',
  styleUrls: ['./lista-usuarios.component.css']
})
export class ListaUsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  loading = false;
  error = '';
  currentUser: Usuario | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.loading = true;
    this.error = '';

    this.usuarioService.obtenerUsuarios().subscribe({
      next: (response) => {
        this.loading = false;
        if (response.exito) {
          this.usuarios = response.datos || [];
        } else {
          this.error = response.mensaje;
        }
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.mensaje || 'Error al cargar usuarios';
      }
    });
  }

  crearUsuario(): void {
    this.router.navigate(['/admin/usuarios/crear']);
  }

  editarUsuario(id: number): void {
    this.router.navigate(['/admin/usuarios/editar', id]);
  }

  cambiarEstado(usuario: Usuario): void {
    if (usuario.id === this.currentUser?.id) {
      alert('No puedes desactivar tu propia cuenta');
      return;
    }

    const nuevoEstado = !usuario.estado;
    const mensaje = nuevoEstado ? 'activar' : 'desactivar';
    
    if (confirm(`¿Está seguro que desea ${mensaje} al usuario ${usuario.nombre} ${usuario.apellido}?`)) {
      this.usuarioService.cambiarEstadoUsuario(usuario.id!, nuevoEstado).subscribe({
        next: (response) => {
          if (response.exito) {
            usuario.estado = nuevoEstado;
          } else {
            alert(response.mensaje);
          }
        },
        error: (error) => {
          alert(error.error?.mensaje || 'Error al cambiar estado del usuario');
        }
      });
    }
  }

  eliminarUsuario(usuario: Usuario): void {
    if (usuario.id === this.currentUser?.id) {
      alert('No puedes eliminar tu propia cuenta');
      return;
    }

    if (confirm(`¿Está seguro que desea eliminar al usuario ${usuario.nombre} ${usuario.apellido}?\nEsta acción no se puede deshacer.`)) {
      this.usuarioService.eliminarUsuario(usuario.id!).subscribe({
        next: (response) => {
          if (response.exito) {
            this.cargarUsuarios(); // Recargar la lista
          } else {
            alert(response.mensaje);
          }
        },
        error: (error) => {
          alert(error.error?.mensaje || 'Error al eliminar usuario');
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
  }

  volverDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }
}