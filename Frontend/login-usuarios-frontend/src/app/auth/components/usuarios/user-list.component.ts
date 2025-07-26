import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService, UsuarioResponsiveDTO } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="user-list-container">
      <div class="header">
        <div class="header-content">
          <h1>Gestión de Usuarios</h1>
          <div class="header-actions">
            <button class="btn btn-primary" (click)="crearUsuario()">
              <i class="icon">+</i>
              Nuevo Usuario
            </button>
            <button class="btn btn-secondary" (click)="volverDashboard()">
              <i class="icon">←</i>
              Volver al Dashboard
            </button>
          </div>
        </div>
      </div>

      <!-- Mensajes -->
      <div class="messages" *ngIf="successMessage || errorMessage">
        <div *ngIf="successMessage" class="alert alert-success">
          <i class="icon-success">✓</i>
          {{ successMessage }}
        </div>
        <div *ngIf="errorMessage" class="alert alert-error">
          <i class="icon-error">⚠</i>
          {{ errorMessage }}
        </div>
      </div>

      <!-- Loading -->
      <div *ngIf="isLoading" class="loading-container">
        <div class="spinner"></div>
        <p>Cargando usuarios...</p>
      </div>

      <!-- Tabla de usuarios -->
      <div *ngIf="!isLoading" class="table-container">
        <div class="table-wrapper">
          <table class="users-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Estado</th>
                <th>Fecha Creación</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let usuario of usuarios" [class.inactive]="!usuario.estado">
                <td>{{ usuario.id }}</td>
                <td>{{ usuario.nombre }} {{ usuario.apellido }}</td>
                <td>{{ usuario.mail }}</td>
                <td>
                  <span class="rol-badge" [class]="'rol-' + usuario.rol.nombre.toLowerCase()">
                    {{ usuario.rol.nombre }}
                  </span>
                </td>
                <td>
                  <span class="estado-badge" [class.activo]="usuario.estado" [class.inactivo]="!usuario.estado">
                    {{ usuario.estado ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td>{{ usuario.fechaCreacion | date:'dd/MM/yyyy' }}</td>
                <td>
                  <div class="actions">
                    <button 
                      class="btn-action btn-edit" 
                      (click)="editarUsuario(usuario.id)"
                      title="Editar usuario">
                      <i class="icon">✏️</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-toggle" 
                      (click)="toggleEstadoUsuario(usuario)"
                      [title]="usuario.estado ? 'Desactivar usuario' : 'Activar usuario'">
                      <i class="icon">{{ usuario.estado ? '🔒' : '🔓' }}</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-delete" 
                      (click)="confirmarEliminar(usuario)"
                      title="Eliminar usuario">
                      <i class="icon">🗑️</i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div *ngIf="usuarios.length === 0" class="no-data">
            <p>No hay usuarios registrados</p>
          </div>
        </div>
      </div>

      <!-- Modal de confirmación de eliminación -->
      <div *ngIf="showDeleteModal" class="modal-overlay" (click)="cancelarEliminar()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Confirmar Eliminación</h3>
          </div>
          <div class="modal-body">
            <p>¿Estás seguro de que deseas eliminar al usuario <strong>{{ usuarioAEliminar?.nombre }} {{ usuarioAEliminar?.apellido }}</strong>?</p>
            <p class="warning">Esta acción no se puede deshacer.</p>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" (click)="cancelarEliminar()">Cancelar</button>
            <button class="btn btn-danger" (click)="eliminarUsuario()" [disabled]="isProcessing">
              {{ isProcessing ? 'Eliminando...' : 'Eliminar' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .user-list-container {
      min-height: 100vh;
      background-color: #f8f9fa;
      padding: 0;
    }

    .header {
      background: white;
      border-bottom: 1px solid #dee2e6;
      padding: 1.5rem 2rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      max-width: 1200px;
      margin: 0 auto;
    }

    .header h1 {
      margin: 0;
      color: #333;
      font-size: 1.8rem;
    }

    .header-actions {
      display: flex;
      gap: 1rem;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 500;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      transition: all 0.3s ease;
      text-decoration: none;
    }

    .btn-primary {
      background: #007bff;
      color: white;
    }

    .btn-primary:hover {
      background: #0056b3;
      transform: translateY(-1px);
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background: #545b62;
    }

    .btn-danger {
      background: #dc3545;
      color: white;
    }

    .btn-danger:hover {
      background: #c82333;
    }

    .messages {
      max-width: 1200px;
      margin: 1rem auto;
      padding: 0 2rem;
    }

    .alert {
      padding: 1rem;
      border-radius: 6px;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      margin-bottom: 1rem;
    }

    .alert-success {
      background: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }

    .alert-error {
      background: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 4rem;
      gap: 1rem;
    }

    .spinner {
      width: 40px;
      height: 40px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #007bff;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .table-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
    }

    .table-wrapper {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      overflow: hidden;
    }

    .users-table {
      width: 100%;
      border-collapse: collapse;
    }

    .users-table th {
      background: #f8f9fa;
      padding: 1rem;
      text-align: left;
      font-weight: 600;
      color: #495057;
      border-bottom: 2px solid #dee2e6;
    }

    .users-table td {
      padding: 1rem;
      border-bottom: 1px solid #dee2e6;
    }

    .users-table tr:hover {
      background: #f8f9fa;
    }

    .users-table tr.inactive {
      opacity: 0.6;
      background: #f8f9fa;
    }

    .rol-badge {
      padding: 0.25rem 0.75rem;
      border-radius: 12px;
      font-size: 0.875rem;
      font-weight: 500;
    }

    .rol-administrador {
      background: #e3f2fd;
      color: #1565c0;
    }

    .rol-tecnico {
      background: #f3e5f5;
      color: #7b1fa2;
    }

    .rol-cliente {
      background: #e8f5e8;
      color: #2e7d32;
    }

    .estado-badge {
      padding: 0.25rem 0.75rem;
      border-radius: 12px;
      font-size: 0.875rem;
      font-weight: 500;
    }

    .estado-badge.activo {
      background: #d4edda;
      color: #155724;
    }

    .estado-badge.inactivo {
      background: #f8d7da;
      color: #721c24;
    }

    .actions {
      display: flex;
      gap: 0.5rem;
    }

    .btn-action {
      width: 32px;
      height: 32px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
    }

    .btn-edit {
      background: #fff3cd;
      color: #856404;
    }

    .btn-edit:hover {
      background: #ffeaa7;
      transform: scale(1.1);
    }

    .btn-toggle {
      background: #d1ecf1;
      color: #0c5460;
    }

    .btn-toggle:hover {
      background: #bee5eb;
      transform: scale(1.1);
    }

    .btn-delete {
      background: #f8d7da;
      color: #721c24;
    }

    .btn-delete:hover {
      background: #f5c6cb;
      transform: scale(1.1);
    }

    .no-data {
      padding: 3rem;
      text-align: center;
      color: #6c757d;
    }

    /* Modal */
    .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 1050;
    }

    .modal {
      background: white;
      border-radius: 8px;
      max-width: 500px;
      width: 90%;
      max-height: 90%;
      overflow: auto;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
    }

    .modal-header {
      padding: 1.5rem;
      border-bottom: 1px solid #dee2e6;
    }

    .modal-header h3 {
      margin: 0;
      color: #333;
    }

    .modal-body {
      padding: 1.5rem;
    }

    .modal-body .warning {
      color: #dc3545;
      font-weight: 500;
      margin-top: 1rem;
    }

    .modal-actions {
      padding: 1rem 1.5rem;
      border-top: 1px solid #dee2e6;
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .header-content {
        flex-direction: column;
        gap: 1rem;
        align-items: stretch;
      }

      .header-actions {
        justify-content: center;
      }

      .table-container {
        padding: 1rem;
      }

      .users-table {
        font-size: 0.875rem;
      }

      .users-table th,
      .users-table td {
        padding: 0.5rem;
      }

      .actions {
        flex-direction: column;
      }
    }
  `]
})
export class UserListComponent implements OnInit {
  usuarios: UsuarioResponsiveDTO[] = [];
  isLoading = false;
  isProcessing = false;
  successMessage = '';
  errorMessage = '';
  showDeleteModal = false;
  usuarioAEliminar: UsuarioResponsiveDTO | null = null;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.isLoading = true;
    this.clearMessages();

    this.userService.obtenerTodosLosUsuarios().subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.exito) {
          this.usuarios = response.datos;
        } else {
          this.errorMessage = response.mensaje;
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error;
      }
    });
  }

  crearUsuario(): void {
    this.router.navigate(['/admin/usuarios/crear']);
  }

  editarUsuario(id: number): void {
    this.router.navigate(['/admin/usuarios/editar', id]);
  }

  toggleEstadoUsuario(usuario: UsuarioResponsiveDTO): void {
    const nuevoEstado = !usuario.estado;
    const accion = nuevoEstado ? 'activar' : 'desactivar';

    this.userService.cambiarEstadoUsuario(usuario.id, nuevoEstado).subscribe({
      next: (response) => {
        if (response.exito) {
          usuario.estado = nuevoEstado;
          this.successMessage = `Usuario ${accion}do exitosamente`;
          this.clearMessages(3000);
        } else {
          this.errorMessage = response.mensaje;
          this.clearMessages(5000);
        }
      },
      error: (error) => {
        this.errorMessage = error;
        this.clearMessages(5000);
      }
    });
  }

  confirmarEliminar(usuario: UsuarioResponsiveDTO): void {
    this.usuarioAEliminar = usuario;
    this.showDeleteModal = true;
  }

  cancelarEliminar(): void {
    this.usuarioAEliminar = null;
    this.showDeleteModal = false;
  }

  eliminarUsuario(): void {
    if (!this.usuarioAEliminar) return;

    this.isProcessing = true;

    this.userService.eliminarUsuario(this.usuarioAEliminar.id).subscribe({
      next: (response) => {
        this.isProcessing = false;
        if (response.exito) {
          this.usuarios = this.usuarios.filter(u => u.id !== this.usuarioAEliminar!.id);
          this.successMessage = 'Usuario eliminado exitosamente';
          this.showDeleteModal = false;
          this.usuarioAEliminar = null;
          this.clearMessages(3000);
        } else {
          this.errorMessage = response.mensaje;
          this.clearMessages(5000);
        }
      },
      error: (error) => {
        this.isProcessing = false;
        this.errorMessage = error;
        this.clearMessages(5000);
      }
    });
  }

  volverDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  private clearMessages(delay = 0): void {
    if (delay > 0) {
      setTimeout(() => {
        this.successMessage = '';
        this.errorMessage = '';
      }, delay);
    } else {
      this.successMessage = '';
      this.errorMessage = '';
    }
  }
}