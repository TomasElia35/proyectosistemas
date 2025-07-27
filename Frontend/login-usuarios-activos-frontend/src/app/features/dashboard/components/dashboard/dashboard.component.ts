import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, UsuarioResponsiveDTO } from '../../../../auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard-container">
      <header class="dashboard-header">
        <h1>Dashboard</h1>
        <div class="user-info">
          <span *ngIf="currentUser">
            Bienvenido, {{ currentUser.nombre }} {{ currentUser.apellido }}
            <small>({{ currentUser.rol.nombre }})</small>
          </span>
          <button (click)="logout()" class="logout-btn">Cerrar Sesión</button>
        </div>
      </header>
      
      <main class="dashboard-content">
        <div class="welcome-card">
          <h2>¡Bienvenido al sistema!</h2>
          <p>Has iniciado sesión correctamente.</p>
          
          <div *ngIf="currentUser" class="user-details">
            <h3>Información del usuario:</h3>
            <ul>
              <li><strong>ID:</strong> {{ currentUser.id }}</li>
              <li><strong>Nombre:</strong> {{ currentUser.nombre }} {{ currentUser.apellido }}</li>
              <li><strong>Email:</strong> {{ currentUser.mail }}</li>
              <li><strong>Rol:</strong> {{ currentUser.rol.nombre }}</li>
              <li><strong>Estado:</strong> {{ currentUser.estado ? 'Activo' : 'Inactivo' }}</li>
              <li><strong>Fecha de creación:</strong> {{ currentUser.fechaCreacion | date:'dd/MM/yyyy' }}</li>
            </ul>
          </div>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .dashboard-container {
      min-height: 100vh;
      background-color: #f5f5f5;
    }

    .dashboard-header {
      background: white;
      padding: 1rem 2rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .dashboard-header h1 {
      margin: 0;
      color: #333;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .user-info span {
      color: #666;
    }

    .user-info small {
      color: #888;
      font-size: 0.8em;
    }

    .logout-btn {
      background: #dc3545;
      color: white;
      border: none;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .logout-btn:hover {
      background: #c82333;
    }

    .dashboard-content {
      padding: 2rem;
    }

    .welcome-card {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      max-width: 800px;
      margin: 0 auto;
    }

    .welcome-card h2 {
      color: #333;
      margin-top: 0;
    }

    .user-details {
      margin-top: 2rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 4px;
    }

    .user-details h3 {
      margin-top: 0;
      color: #495057;
    }

    .user-details ul {
      list-style: none;
      padding: 0;
    }

    .user-details ul li {
      padding: 0.25rem 0;
      border-bottom: 1px solid #dee2e6;
    }

    .user-details ul li:last-child {
      border-bottom: none;
    }
  `]
})
export class DashboardComponent implements OnInit {
  currentUser: UsuarioResponsiveDTO | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}