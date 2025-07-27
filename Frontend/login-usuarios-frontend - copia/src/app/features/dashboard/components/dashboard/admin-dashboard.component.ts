import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, UsuarioResponsiveDTO } from '../../../../auth/services/auth.service';
import { InsumoService } from '../../../../auth/services/insumo.service';

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

        <!-- Estadísticas rápidas -->
        <div class="stats-section" *ngIf="estadisticas">
          <div class="stat-card">
            <div class="stat-icon">📦</div>
            <div class="stat-content">
              <h4>Total Insumos</h4>
              <span class="stat-number">{{ estadisticas.totalInsumos }}</span>
            </div>
          </div>
          <div class="stat-card" [class.alert]="estadisticas.stockBajo > 0">
            <div class="stat-icon">⚠️</div>
            <div class="stat-content">
              <h4>Stock Bajo</h4>
              <span class="stat-number" [class.danger]="estadisticas.stockBajo > 0">
                {{ estadisticas.stockBajo }}
              </span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-content">
              <h4>Total Usuarios</h4>
              <span class="stat-number">{{ estadisticas.totalUsuarios || 'N/A' }}</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">✅</div>
            <div class="stat-content">
              <h4>Insumos Activos</h4>
              <span class="stat-number">{{ estadisticas.insumosActivos }}</span>
            </div>
          </div>
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

          <div class="module-card" (click)="navegarA('/admin/insumos')">
            <div class="module-icon">📦</div>
            <div class="module-content">
              <h3>Gestión de Insumos</h3>
              <p>Administrar inventario, stock y control de insumos</p>
              <div class="module-actions">
                <span class="action-link">Ver insumos →</span>
              </div>
            </div>
          </div>

          <div class="module-card" 
               (click)="navegarAStockBajo()" 
               [class.highlight]="hayInsumosStockBajo"
               [class.pulse]="hayInsumosStockBajo">
            <div class="module-icon">⚠️</div>
            <div class="module-content">
              <h3>Alertas de Stock</h3>
              <p>Monitorear insumos con stock bajo o crítico</p>
              <div class="module-actions">
                <span class="action-link" [class.urgent]="hayInsumosStockBajo">
                  {{ hayInsumosStockBajo ? 'Revisar urgente →' : 'Sin alertas →' }}
                </span>
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
        </div>

        <!-- Alertas de stock bajo (si existen) -->
        <div *ngIf="insumosStockBajo.length > 0" class="alertas-section">
          <h3>⚠️ Alertas de Stock Bajo</h3>
          <div class="alertas-grid">
            <div *ngFor="let insumo of insumosStockBajo.slice(0, 6)" class="alerta-card">
              <div class="alerta-header">
                <strong>{{ insumo.nombre }}</strong>
                <span class="codigo">{{ insumo.codigo }}</span>
              </div>
              <div class="alerta-stock">
                <span class="stock-actual danger">{{ insumo.stock }}</span>
                <span class="stock-minimo">Mín: {{ insumo.stockMinimo }}</span>
              </div>
              <div class="alerta-actions">
                <button class="btn-sm btn-warning" (click)="navegarAInsumo(insumo.id)">
                  Actualizar Stock
                </button>
              </div>
            </div>
          </div>
          <div *ngIf="insumosStockBajo.length > 6" class="ver-todas">
            <button class="btn btn-warning" (click)="navegarAStockBajo()">
              Ver todas las alertas ({{ insumosStockBajo.length }})
            </button>
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

        <!-- Loading -->
        <div *ngIf="isLoadingStats" class="loading-stats">
          <div class="spinner"></div>
          <p>Cargando estadísticas...</p>
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
      margin-bottom: 2rem;
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

    /* Estadísticas */
    .stats-section {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
      margin-bottom: 2rem;
    }

    .stat-card {
      background: white;
      padding: 1.5rem;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      display: flex;
      align-items: center;
      gap: 1rem;
      transition: all 0.3s ease;
    }

    .stat-card.alert {
      border-left: 4px solid #ffc107;
      background: linear-gradient(135deg, #fff3cd 0%, #ffffff 100%);
    }

    .stat-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0,0,0,0.15);
    }

    .stat-icon {
      font-size: 2rem;
      opacity: 0.8;
    }

    .stat-content h4 {
      margin: 0 0 0.25rem 0;
      color: #666;
      font-size: 0.9rem;
      font-weight: 500;
    }

    .stat-number {
      font-size: 1.8rem;
      font-weight: 700;
      color: #2c3e50;
    }

    .stat-number.danger {
      color: #dc3545;
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

    .module-card.highlight {
      border-color: #ffc107;
      background: linear-gradient(135deg, #fff3cd 0%, #ffffff 100%);
    }

    .module-card.pulse {
      animation: pulse 2s infinite;
    }

    @keyframes pulse {
      0% { transform: scale(1); }
      50% { transform: scale(1.02); }
      100% { transform: scale(1); }
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

    .action-link.urgent {
      color: #dc3545;
      animation: blink 1.5s infinite;
    }

    @keyframes blink {
      0%, 50% { opacity: 1; }
      51%, 100% { opacity: 0.7; }
    }

    .action-link.disabled {
      color: #adb5bd;
      cursor: not-allowed;
    }

    .module-card:hover .action-link:not(.disabled) {
      color: #0056b3;
    }

    /* Alertas de stock */
    .alertas-section {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      margin-bottom: 3rem;
      border-left: 4px solid #ffc107;
    }

    .alertas-section h3 {
      margin-top: 0;
      margin-bottom: 1.5rem;
      color: #856404;
      font-size: 1.3rem;
    }

    .alertas-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }

    .alerta-card {
      background: #fff3cd;
      border: 1px solid #ffeaa7;
      border-radius: 8px;
      padding: 1rem;
      transition: all 0.3s ease;
    }

    .alerta-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
    }

    .alerta-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.5rem;
    }

    .alerta-header strong {
      color: #856404;
    }

    .codigo {
      font-family: 'Courier New', monospace;
      font-size: 0.8rem;
      background: rgba(0,0,0,0.1);
      padding: 0.2rem 0.4rem;
      border-radius: 4px;
    }

    .alerta-stock {
      margin-bottom: 1rem;
      display: flex;
      gap: 1rem;
      align-items: center;
    }

    .stock-actual {
      font-size: 1.2rem;
      font-weight: 700;
    }

    .stock-actual.danger {
      color: #dc3545;
    }

    .stock-minimo {
      font-size: 0.9rem;
      color: #666;
    }

    .alerta-actions {
      display: flex;
      justify-content: flex-end;
    }

    .btn-sm {
      padding: 0.4rem 0.8rem;
      font-size: 0.8rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-sm.btn-warning {
      background: #ffc107;
      color: #212529;
    }

    .btn-sm.btn-warning:hover {
      background: #e0a800;
      transform: translateY(-1px);
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-warning {
      background: #ffc107;
      color: #212529;
    }

    .btn-warning:hover {
      background: #e0a800;
      transform: translateY(-1px);
    }

    .ver-todas {
      text-align: center;
      padding-top: 1rem;
      border-top: 1px solid #ffeaa7;
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

    .loading-stats {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 2rem;
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

      .stats-section {
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      }

      .admin-modules {
        grid-template-columns: 1fr;
      }

      .alertas-grid {
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

      .stat-card {
        flex-direction: column;
        text-align: center;
        gap: 0.5rem;
      }

      .alerta-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.25rem;
      }
    }
  `]
})
export class AdminDashboardComponent implements OnInit, OnDestroy {
  currentUser: UsuarioResponsiveDTO | null = null;
  isLoadingStats = false;
  
  // Estadísticas
  estadisticas = {
    totalInsumos: 0,
    stockBajo: 0,
    totalUsuarios: 0,
    insumosActivos: 0
  };

  // Alertas
  insumosStockBajo: any[] = [];
  hayInsumosStockBajo = false;

  private subscriptions = new Subscription();

  constructor(
    private authService: AuthService,
    private insumoService: InsumoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.cargarEstadisticas();
    this.cargarAlertasStockBajo();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private loadUserData(): void {
    const userSub = this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
    this.subscriptions.add(userSub);
  }

  private cargarEstadisticas(): void {
    this.isLoadingStats = true;
    
    // Cargar estadísticas de insumos
    const insumosSub = this.insumoService.obtenerTodosLosInsumos().subscribe({
      next: (response) => {
        if (response.exito && response.datos) {
          const insumos = response.datos;
          this.estadisticas.totalInsumos = insumos.length;
          this.estadisticas.insumosActivos = insumos.filter((insumo: any) => insumo.estado).length;
          this.estadisticas.stockBajo = insumos.filter((insumo: any) => insumo.stockBajo).length;
          this.hayInsumosStockBajo = this.estadisticas.stockBajo > 0;
        }
        this.isLoadingStats = false;
      },
      error: (error) => {
        console.error('Error cargando estadísticas de insumos:', error);
        this.isLoadingStats = false;
      }
    });
    this.subscriptions.add(insumosSub);

    // TODO: Implementar estadísticas de usuarios cuando esté disponible el endpoint
    // Por ahora dejamos el valor por defecto
    this.estadisticas.totalUsuarios = 0;
  }

  private cargarAlertasStockBajo(): void {
    const alertasSub = this.insumoService.obtenerInsumosConStockBajo().subscribe({
      next: (response) => {
        if (response.exito && response.datos) {
          this.insumosStockBajo = response.datos;
          this.hayInsumosStockBajo = this.insumosStockBajo.length > 0;
        }
      },
      error: (error) => {
        console.error('Error cargando alertas de stock bajo:', error);
      }
    });
    this.subscriptions.add(alertasSub);
  }

  navegarA(ruta: string): void {
    this.router.navigate([ruta]);
  }

  navegarAStockBajo(): void {
    // Navegar a insumos con filtro de stock bajo aplicado
    this.router.navigate(['/admin/insumos'], { 
      queryParams: { stockBajo: 'true' } 
    });
  }

  navegarAInsumo(id: number): void {
    this.router.navigate(['/admin/insumos/editar', id]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}