// src/app/features/activos/components/activos-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ActivosService } from '../../services/activos.service';
import { AuthService } from '../../../../auth/services/auth.service';
import { ActivoTecnologicoResponseDTO } from '../../models/activo.model';

@Component({
  standalone: true,
  selector: 'app-activos-list',
  imports: [
  CommonModule,
  RouterModule
]
,
  template: `
    <div class="activos-list-container">
      <div class="header">
        <div class="header-content">
          <h1>Gestión de Activos Tecnológicos</h1>
          <div class="header-actions">
            <button class="btn btn-primary" (click)="crearActivo()">
              <i class="icon">+</i>
              Nuevo Activo
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
        <p>Cargando activos...</p>
      </div>

      <!-- Tabla de activos -->
      <div *ngIf="!isLoading" class="table-container">
        <div class="table-wrapper">
          <table class="activos-table">
            <thead>
              <tr>
                <th>Código</th>
                <th>Nombre</th>
                <th>Marca/Modelo</th>
                <th>Empleado</th>
                <th>Área</th>
                <th>Estado</th>
                <th>Costo</th>
                <th>Fecha Adquisición</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let activo of activos" [class.inactive]="!activo.activo">
                <td>
                  <div class="codigo-info">
                    <strong>{{ activo.codigoInventario }}</strong>
                    <small *ngIf="activo.codigoTecnico">{{ activo.codigoTecnico }}</small>
                  </div>
                </td>
                <td>
                  <div class="activo-info">
                    <strong>{{ activo.nombre }}</strong>
                    <small *ngIf="activo.descripcion">{{ activo.descripcion | slice:0:50 }}{{ activo.descripcion && activo.descripcion.length > 50 ? '...' : '' }}</small>
                  </div>
                </td>
                <td>
                  <div class="modelo-info">
                    <strong>{{ activo.modeloArticuloMarca }}</strong>
                    <small>{{ activo.modeloArticuloModelo }}</small>
                  </div>
                </td>
                <td>{{ activo.empleadoNombreCompleto }}</td>
                <td>{{ activo.areaNombre }}</td>
                <td>
                  <span class="estado-badge" [class]="'estado-' + activo.estadoNombre.toLowerCase().replace(' ', '-')">
                    {{ activo.estadoNombre }}
                  </span>
                </td>
                <td class="costo">{{ activo.costo | currency:'$':'symbol':'1.2-2' }}</td>
                <td>{{ activo.fechaAdquisicion | date:'dd/MM/yyyy' }}</td>
                <td>
                  <div class="actions">
                    <button 
                      class="btn-action btn-view" 
                      (click)="verDetalleActivo(activo.id)"
                      title="Ver detalles">
                      <i class="icon">👁️</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-edit" 
                      (click)="editarActivo(activo.id)"
                      title="Editar activo">
                      <i class="icon">✏️</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-delete" 
                      (click)="confirmarEliminar(activo)"
                      title="Eliminar activo">
                      <i class="icon">🗑️</i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div *ngIf="activos.length === 0" class="no-data">
            <p>No hay activos registrados</p>
            <button class="btn btn-primary" (click)="crearActivo()">
              Crear primer activo
            </button>
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
            <p>¿Estás seguro de que deseas eliminar el activo <strong>{{ activoAEliminar?.nombre }}</strong>?</p>
            <p><strong>Código:</strong> {{ activoAEliminar?.codigoInventario }}</p>
            <p class="warning">Esta acción no se puede deshacer.</p>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" (click)="cancelarEliminar()">Cancelar</button>
            <button class="btn btn-danger" (click)="eliminarActivo()" [disabled]="isProcessing">
              {{ isProcessing ? 'Eliminando...' : 'Eliminar' }}
            </button>
          </div>
        </div>
      </div>
    </div>
    <!-- En activos-list.component.ts, cambia el botón a: -->
<button class="btn btn-primary" (click)="crearActivo()">
  <i class="icon">+</i>
  Nuevo Activo
</button>
  `,
  styles: [`
    .activos-list-container {
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
      max-width: 1400px;
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
      max-width: 1400px;
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
      max-width: 1400px;
      margin: 0 auto;
      padding: 2rem;
    }

    .table-wrapper {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      overflow-x: auto;
    }

    .activos-table {
      width: 100%;
      border-collapse: collapse;
      min-width: 1000px;
    }

    .activos-table th {
      background: #f8f9fa;
      padding: 1rem;
      text-align: left;
      font-weight: 600;
      color: #495057;
      border-bottom: 2px solid #dee2e6;
      white-space: nowrap;
    }

    .activos-table td {
      padding: 1rem;
      border-bottom: 1px solid #dee2e6;
      vertical-align: top;
    }

    .activos-table tr:hover {
      background: #f8f9fa;
    }

    .activos-table tr.inactive {
      opacity: 0.6;
      background: #f8f9fa;
    }

    .codigo-info strong {
      display: block;
      color: #007bff;
      font-weight: 600;
    }

    .codigo-info small {
      color: #666;
      font-size: 0.8em;
    }

    .activo-info strong {
      display: block;
      color: #333;
      margin-bottom: 0.25rem;
    }

    .activo-info small {
      color: #666;
      font-size: 0.85em;
      line-height: 1.2;
    }

    .modelo-info strong {
      display: block;
      color: #333;
    }

    .modelo-info small {
      color: #666;
      font-size: 0.9em;
    }

    .estado-badge {
      padding: 0.25rem 0.75rem;
      border-radius: 12px;
      font-size: 0.875rem;
      font-weight: 500;
      white-space: nowrap;
    }

    .estado-activo {
      background: #d4edda;
      color: #155724;
    }

    .estado-inactivo {
      background: #f8d7da;
      color: #721c24;
    }

    .estado-mantenimiento {
      background: #fff3cd;
      color: #856404;
    }

    .estado-reparacion {
      background: #f8d7da;
      color: #721c24;
    }

    .costo {
      text-align: right;
      font-weight: 600;
      color: #28a745;
    }

    .actions {
      display: flex;
      gap: 0.5rem;
      justify-content: center;
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

    .btn-view {
      background: #d1ecf1;
      color: #0c5460;
    }

    .btn-view:hover {
      background: #bee5eb;
      transform: scale(1.1);
    }

    .btn-edit {
      background: #fff3cd;
      color: #856404;
    }

    .btn-edit:hover {
      background: #ffeaa7;
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

    .no-data p {
      margin-bottom: 1rem;
      font-size: 1.1rem;
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

      .activos-table {
        font-size: 0.875rem;
      }

      .actions {
        flex-direction: column;
        gap: 0.25rem;
      }

      .btn-action {
        width: 28px;
        height: 28px;
      }
    }
  `]
})
export class ActivosListComponent implements OnInit {
  activos: ActivoTecnologicoResponseDTO[] = [];
  isLoading = false;
  isProcessing = false;
  successMessage = '';
  errorMessage = '';
  showDeleteModal = false;
  activoAEliminar: ActivoTecnologicoResponseDTO | null = null;

  constructor(
    private activosService: ActivosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarActivos();
  }

  cargarActivos(): void {
    this.isLoading = true;
    this.clearMessages();

this.activosService.listar().subscribe({
  next: (response: ActivoTecnologicoResponseDTO[]) => {
    this.isLoading = false;
    this.activos = response;
    console.log('✅ Activos cargados:', response.length);
  },
  error: (error: any) => {
    this.isLoading = false;
    this.errorMessage = error;
    console.error('❌ Error cargando activos:', error);
  }
});

  }

crearActivo(): void {
  console.log('🚀 Navegando a crear activo...');
  this.router.navigate(['/admin/activos/crear']);
}

  verDetalleActivo(id: number): void {
    this.router.navigate(['/activos/detalle', id]);
  }

editarActivo(id: number): void {
  console.log('✏️ Navegando a editar activo:', id);
  this.router.navigate(['/admin/activos', id, 'editar']);
}

  confirmarEliminar(activo: ActivoTecnologicoResponseDTO): void {
    this.activoAEliminar = activo;
    this.showDeleteModal = true;
  }

  cancelarEliminar(): void {
    this.activoAEliminar = null;
    this.showDeleteModal = false;
  }

  eliminarActivo(): void {
    if (!this.activoAEliminar) return;

    this.isProcessing = true;

this.activosService.eliminar(this.activoAEliminar!.id).subscribe({
  next: () => {
    this.isProcessing = false;
    this.activos = this.activos.filter(a => a.id !== this.activoAEliminar!.id);
    this.successMessage = 'Activo eliminado exitosamente';
    this.showDeleteModal = false;
    this.activoAEliminar = null;
    this.clearMessages(3000);
  },
  error: (error: any) => {
    this.isProcessing = false;
    this.errorMessage = error;
    this.clearMessages(5000);
  }
});

  }

  volverDashboard(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser?.rol.nombre.toUpperCase() === 'ADMINISTRADOR') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/dashboard']);
    }
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
  // Agrega este método temporal en activos-list.component.ts para debug
debugNavigation(): void {
  console.log('%c🐛 DEBUG COMPLETO - Estado actual:', 'color: purple; font-weight: bold; background: yellow; padding: 5px;');
  
  // Verificar almacenamiento local
  const token = localStorage.getItem('token');
  const userStr = localStorage.getItem('currentUser');
  
  console.log('💾 LocalStorage:');
  console.log('  Token:', token ? `PRESENTE (${token.length} chars)` : 'AUSENTE');
  console.log('  CurrentUser:', userStr ? 'PRESENTE' : 'AUSENTE');
  
  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      console.log('  Usuario parseado:', {
        id: user.id,
        nombre: user.nombre,
        apellido: user.apellido,
        rol: user.rol?.nombre
      });
    } catch (e) {
      console.error('  ❌ Error parseando usuario:', e);
    }
  }
  
  // Verificar servicio de auth
  console.log('🔐 AuthService:');
  console.log('  isAuthenticated():', this.authService.isAuthenticated());
  console.log('  getCurrentUser():', this.authService.getCurrentUser());
  console.log('  getToken():', this.authService.getToken() ? 'PRESENTE' : 'AUSENTE');
  
  // Verificar URL actual
  console.log('🌐 Router:');
  console.log('  URL actual:', this.router.url);
  
  // Intentar navegar con logging
  console.log('🚀 Intentando navegar a /admin/activos/crear');
  
  this.router.navigate(['/admin/activos/crear']).then(
    success => console.log('✅ Navegación exitosa:', success),
    error => console.error('❌ Error en navegación:', error)
  );
}
}