import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, UsuarioResponsiveDTO } from '../../../../auth/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard-container">
      <header class="dashboard-header">
        <h1>Panel de Administración</h1>
        <div class="user-info">
          <span *ngIf="currentUser">
            Bienvenido, {{ currentUser.nombre }} {{ currentUser.apellido }}
            <small>({{ currentUser.rol.nombre }})</small>
          </span>
          <button (click)="logout()" class="logout-btn">Cerrar Sesión</button>
        </div>
      </header>
      
      <main class="dashboard-content">
        <div class="welcome-section">
          <h2>¡Bienvenido al Panel de Administración!</h2>
          <p>Desde aquí puedes gestionar todos los aspectos del sistema.</p>
        </div>

        <!-- Módulos de Administración -->
        <div class="admin-modules">
          <div class="module-card" (click)="navegarA('/admin/usuarios')">
            <div class="module-icon">👥</div>
            <div class="module-content">
              <h3>Gestión de Usuarios</h3>
              <p>Crear, editar y gestionar usuarios del sistema</p>
              <div class="module-actions">
                <span class="action-link">Ver usuarios →</span>
              </div>
            </div>
          </div>

  <div class="module-card" (click)="navegarA('/admin/activos')">
    <div class="module-icon">🖥️</div>
    <div class="module-content">
      <h3>Gestión de Activos</h3>
      <p>Crear, editar y gestionar activos del sistema</p>
      <div class="module-actions">
        <span class="action-link">Ver activos →</span>
      </div>
    </div>
  </div>

          <div class="module-card">
            <div class="module-icon">🛠️</div>
            <div class="module-content">
              <h3>Configuración del Sistema</h3>
              <p>Ajustes generales y configuraciones avanzadas</p>
              <div class="module-actions">
                <span class="action-link disabled">Próximamente →</span>
              </div>
            </div>
          </div>

          <div class="module-card">
            <div class="module-icon">📊</div>
            <div class="module-content">
              <h3>Reportes y Estadísticas</h3>
              <p>Visualiza métricas y genera reportes del sistema</p>
              <div class="module-actions">
                <span class="action-link disabled">Próximamente →</span>
              </div>
            </div>
          </div>

          <div class="module-card">
            <div class="module-icon">🔐</div>
            <div class="module-content">
              <h3>Seguridad y Roles</h3>
              <p>Gestiona permisos y roles de usuarios</p>
              <div class="module-actions">
                <span class="action-link disabled">Próximamente →</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Información del usuario actual -->
        <div *ngIf="currentUser" class="user-details-card">
          <h3>Información de tu cuenta</h3>
          <div class="user-info-grid">
            <div class="info-item">
              <strong>ID:</strong> 
              <span>{{ currentUser.id }}</span>
            </div>
            <div class="info-item">
              <strong>Nombre completo:</strong> 
              <span>{{ currentUser.nombre }} {{ currentUser.apellido }}</span>
            </div>
            <div class="info-item">
              <strong>Email:</strong> 
              <span>{{ currentUser.mail }}</span>
            </div>
            <div class="info-item">
              <strong>Rol:</strong> 
              <span class="rol-badge">{{ currentUser.rol.nombre }}</span>
            </div>
            <div class="info-item">
              <strong>Estado:</strong> 
              <span [class]="'estado-badge ' + (currentUser.estado ? 'activo' : 'inactivo')">
                {{ currentUser.estado ? 'Activo' : 'Inactivo' }}
              </span>
            </div>
            <div class="info-item">
              <strong>Fecha de creación:</strong> 
              <span>{{ currentUser.fechaCreacion | date:'dd/MM/yyyy' }}</span>
            </div>
          </div>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .dashboard-container {
      min-height: 100vh;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .dashboard-header {
      background: white;
      padding: 1.5rem 2rem;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .dashboard-header h1 {
      margin: 0;
      color: #2c3e50;
      font-size: 1.8rem;
      font-weight: 600;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .user-info span {
      color: #666;
      font-weight: 500;
    }

    .user-info small {
      color: #007bff;
      font-size: 0.85em;
      font-weight: 600;
    }

    .logout-btn {
      background: #dc3545;
      color: white;
      border: none;
      padding: 0.6rem 1.2rem;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-weight: 500;
    }

    .logout-btn:hover {
      background: #c82333;
      transform: translateY(-1px);
      box-shadow: 0 4px 8px rgba(220, 53, 69, 0.3);
    }

    .dashboard-content {
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;
    }

    .welcome-section {
      text-align: center;
      margin-bottom: 3rem;
    }

    .welcome-section h2 {
      color: #2c3e50;
      margin-bottom: 0.5rem;
      font-size: 2rem;
      font-weight: 300;
    }

    .welcome-section p {
      color: #666;
      font-size: 1.1rem;
    }

    .admin-modules {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 1.5rem;
      margin-bottom: 3rem;
    }

    .module-card {
      background: white;
      border-radius: 12px;
      padding: 2rem;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      transition: all 0.3s ease;
      cursor: pointer;
      border: 2px solid transparent;
    }

    .module-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
      border-color: #007bff;
    }

    .module-card:hover .module-icon {
      transform: scale(1.1);
    }

    .module-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
      display: block;
      transition: transform 0.3s ease;
    }

    .module-content h3 {
      color: #2c3e50;
      margin-bottom: 0.5rem;
      font-size: 1.3rem;
      font-weight: 600;
    }

    .module-content p {
      color: #666;
      margin-bottom: 1rem;
      line-height: 1.5;
    }

    .module-actions {
      display: flex;
      justify-content: flex-end;
    }

    .action-link {
      color: #007bff;
      font-weight: 600;
      font-size: 0.9rem;
      transition: color 0.3s ease;
    }

    .action-link.disabled {
      color: #adb5bd;
      cursor: not-allowed;
    }

    .module-card:hover .action-link:not(.disabled) {
      color: #0056b3;
    }

    .user-details-card {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }

    .user-details-card h3 {
      margin-top: 0;
      margin-bottom: 1.5rem;
      color: #2c3e50;
      font-size: 1.3rem;
      font-weight: 600;
    }

    .user-info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1rem;
    }

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0.75rem;
      background: #f8f9fa;
      border-radius: 6px;
      border-left: 4px solid #007bff;
    }

    .info-item strong {
      color: #495057;
      font-weight: 600;
    }

    .rol-badge {
      background: #e3f2fd;
      color: #1565c0;
      padding: 0.25rem 0.75rem;
      border-radius: 12px;
      font-size: 0.875rem;
      font-weight: 500;
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

    /* Responsive */
    @media (max-width: 768px) {
      .dashboard-header {
        flex-direction: column;
        gap: 1rem;
        text-align: center;
        padding: 1rem;
      }

      .dashboard-content {
        padding: 1rem;
      }

      .admin-modules {
        grid-template-columns: 1fr;
      }

      .user-info-grid {
        grid-template-columns: 1fr;
      }

      .info-item {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.25rem;
      }
    }
  `]
})
export class AdminDashboardComponent implements OnInit {
  currentUser: UsuarioResponsiveDTO | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  navegarA(ruta: string): void {
    this.router.navigate([ruta]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}