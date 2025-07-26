import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InsumoService, InsumoResponseDTO, InsumoFiltroDTO, InsumoUpdateStockDTO } from '../../services/insumo.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-insumo-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="insumo-list-container">
      <div class="header">
        <div class="header-content">
          <div class="header-left">
            <h1>Gestión de Insumos</h1>
            <div class="stats" *ngIf="insumos.length > 0">
              <span class="stat">Total: {{ insumos.length }}</span>
              <span class="stat" *ngIf="insumosConStockBajo > 0" class="stat-warning">
                ⚠️ Stock bajo: {{ insumosConStockBajo }}
              </span>
            </div>
          </div>
          <div class="header-actions">
            <button class="btn btn-primary" (click)="crearInsumo()">
              <i class="icon">+</i>
              Nuevo Insumo
            </button>
            <button class="btn btn-warning" (click)="verStockBajo()" [disabled]="insumosConStockBajo === 0">
              <i class="icon">⚠️</i>
              Stock Bajo ({{ insumosConStockBajo }})
            </button>
            <button class="btn btn-secondary" (click)="volverDashboard()">
              <i class="icon">←</i>
              Volver
            </button>
          </div>
        </div>
      </div>

      <!-- Filtros -->
      <div class="filters-section">
        <div class="filters-wrapper">
          <h3>Filtros de búsqueda</h3>
          <div class="filters-grid">
            <div class="filter-group">
              <label>Nombre:</label>
              <input 
                type="text" 
                [(ngModel)]="filtros.nombre" 
                placeholder="Buscar por nombre..."
                (input)="aplicarFiltros()"
                class="filter-input">
            </div>
            
            <div class="filter-group">
              <label>Categoría:</label>
              <select [(ngModel)]="filtros.categoria" (change)="aplicarFiltros()" class="filter-select">
                <option value="">Todas las categorías</option>
                <option *ngFor="let categoria of categorias" [value]="categoria">{{ categoria }}</option>
              </select>
            </div>
            
            <div class="filter-group">
              <label>Proveedor:</label>
              <select [(ngModel)]="filtros.proveedor" (change)="aplicarFiltros()" class="filter-select">
                <option value="">Todos los proveedores</option>
                <option *ngFor="let proveedor of proveedores" [value]="proveedor">{{ proveedor }}</option>
              </select>
            </div>
            
            <div class="filter-group">
              <label>Estado:</label>
              <select [(ngModel)]="filtros.estado" (change)="aplicarFiltros()" class="filter-select">
                <option [ngValue]="undefined">Todos</option>
                <option [ngValue]="true">Activos</option>
                <option [ngValue]="false">Inactivos</option>
              </select>
            </div>
            
            <div class="filter-group">
              <label class="checkbox-label">
                <input 
                  type="checkbox" 
                  [(ngModel)]="filtros.soloStockBajo"
                  (change)="aplicarFiltros()">
                Solo stock bajo
              </label>
            </div>
            
            <div class="filter-actions">
              <button class="btn btn-sm btn-secondary" (click)="limpiarFiltros()">
                Limpiar Filtros
              </button>
              <button class="btn btn-sm btn-primary" (click)="aplicarFiltros()">
                Aplicar
              </button>
            </div>
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
        <p>Cargando insumos...</p>
      </div>

      <!-- Tabla de insumos -->
      <div *ngIf="!isLoading" class="table-container">
        <div class="table-wrapper">
          <table class="insumos-table">
            <thead>
              <tr>
                <th>Código</th>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Stock</th>
                <th>Stock Mín.</th>
                <th>Precio</th>
                <th>Proveedor</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let insumo of insumosFiltered" 
                  [class.inactive]="!insumo.estado" 
                  [class.stock-bajo]="insumo.stockBajo">
                <td>
                  <span class="codigo">{{ insumo.codigo }}</span>
                </td>
                <td>
                  <div class="insumo-info">
                    <strong>{{ insumo.nombre }}</strong>
                    <small *ngIf="insumo.descripcion">{{ insumo.descripcion }}</small>
                  </div>
                </td>
                <td>
                  <span class="categoria-badge" *ngIf="insumo.categoria">{{ insumo.categoria }}</span>
                  <span *ngIf="!insumo.categoria" class="text-muted">Sin categoría</span>
                </td>
                <td>
                  <div class="stock-info">
                    <span class="stock-actual" [class.stock-bajo]="insumo.stockBajo">
                      {{ insumo.stock }}
                    </span>
                    <small *ngIf="insumo.unidadMedida">{{ insumo.unidadMedida }}</small>
                    <span *ngIf="insumo.stockBajo" class="stock-warning">⚠️</span>
                  </div>
                </td>
                <td>{{ insumo.stockMinimo || 'N/A' }}</td>
                <td>
                  <span *ngIf="insumo.precio" class="precio">\${{ insumo.precio | number:'1.2-2' }}</span>
                  <span *ngIf="!insumo.precio" class="text-muted">N/A</span>
                </td>
                <td>
                  <span *ngIf="insumo.proveedor">{{ insumo.proveedor }}</span>
                  <span *ngIf="!insumo.proveedor" class="text-muted">Sin proveedor</span>
                </td>
                <td>
                  <span class="estado-badge" [class.activo]="insumo.estado" [class.inactivo]="!insumo.estado">
                    {{ insumo.estado ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td>
                  <div class="actions">
                    <button 
                      class="btn-action btn-edit" 
                      (click)="editarInsumo(insumo.id)"
                      title="Editar insumo">
                      <i class="icon">✏️</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-stock" 
                      (click)="abrirModalStock(insumo)"
                      [disabled]="!insumo.estado"
                      title="Actualizar stock">
                      <i class="icon">📊</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-toggle" 
                      (click)="toggleEstadoInsumo(insumo)"
                      [title]="insumo.estado ? 'Desactivar insumo' : 'Activar insumo'"
                      *ngIf="isAdmin">
                      <i class="icon">{{ insumo.estado ? '🔒' : '🔓' }}</i>
                    </button>
                    
                    <button 
                      class="btn-action btn-delete" 
                      (click)="confirmarEliminar(insumo)"
                      title="Eliminar insumo"
                      *ngIf="isAdmin">
                      <i class="icon">🗑️</i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div *ngIf="insumosFiltered.length === 0 && !isLoading" class="no-data">
            <p *ngIf="insumos.length === 0">No hay insumos registrados</p>
            <p *ngIf="insumos.length > 0">No se encontraron insumos con los filtros aplicados</p>
            <button *ngIf="insumos.length === 0" class="btn btn-primary" (click)="crearInsumo()">
              Crear primer insumo
            </button>
          </div>
        </div>
      </div>

      <!-- Modal de actualización de stock -->
      <div *ngIf="showStockModal" class="modal-overlay" (click)="cerrarModalStock()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Actualizar Stock</h3>
            <div class="insumo-modal-info">
              <strong>{{ insumoSeleccionado?.nombre }}</strong>
              <span>Stock actual: {{ insumoSeleccionado?.stock }}</span>
            </div>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>Tipo de operación:</label>
              <select [(ngModel)]="stockUpdate.tipoOperacion" class="form-select">
                <option value="ENTRADA">Entrada (sumar stock)</option>
                <option value="SALIDA">Salida (restar stock)</option>
              </select>
            </div>
            
            <div class="form-group">
              <label>Cantidad:</label>
              <input 
                type="number" 
                [(ngModel)]="stockUpdate.cantidad" 
                min="1" 
                class="form-input"
                placeholder="Ingresa la cantidad">
            </div>
            
            <div class="form-group">
              <label>Observación (opcional):</label>
              <textarea 
                [(ngModel)]="stockUpdate.observacion" 
                class="form-textarea"
                placeholder="Motivo del movimiento..."
                rows="3"></textarea>
            </div>
            
            <div class="stock-preview" *ngIf="stockUpdate.cantidad && stockUpdate.tipoOperacion">
              <p>
                <strong>Stock resultante:</strong> 
                {{ calcularNuevoStock() }}
                <span *ngIf="calcularNuevoStock() < 0" class="error-text">(Stock insuficiente)</span>
              </p>
            </div>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" (click)="cerrarModalStock()">Cancelar</button>
            <button 
              class="btn btn-primary" 
              (click)="actualizarStock()" 
              [disabled]="isProcessing || !stockUpdate.cantidad || calcularNuevoStock() < 0">
              {{ isProcessing ? 'Actualizando...' : 'Actualizar Stock' }}
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
            <p>¿Estás seguro de que deseas eliminar el insumo <strong>{{ insumoAEliminar?.nombre }}</strong>?</p>
            <div class="insumo-details">
              <p><strong>Código:</strong> {{ insumoAEliminar?.codigo }}</p>
              <p><strong>Stock actual:</strong> {{ insumoAEliminar?.stock }}</p>
            </div>
            <p class="warning">Esta acción no se puede deshacer.</p>
          </div>
          <div class="modal-actions">
            <button class="btn btn-secondary" (click)="cancelarEliminar()">Cancelar</button>
            <button class="btn btn-danger" (click)="eliminarInsumo()" [disabled]="isProcessing">
              {{ isProcessing ? 'Eliminando...' : 'Eliminar' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .insumo-list-container {
      min-height: 100vh;
      background-color: #f8f9fa;
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
      align-items: flex-start;
      max-width: 1400px;
      margin: 0 auto;
    }

    .header-left h1 {
      margin: 0 0 0.5rem 0;
      color: #333;
      font-size: 1.8rem;
    }

    .stats {
      display: flex;
      gap: 1rem;
    }

    .stat {
      padding: 0.25rem 0.75rem;
      background: #e9ecef;
      border-radius: 12px;
      font-size: 0.875rem;
      font-weight: 500;
    }

    .stat-warning {
      background: #fff3cd !important;
      color: #856404 !important;
    }

    .header-actions {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
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
      white-space: nowrap;
    }

    .btn-sm {
      padding: 0.5rem 1rem;
      font-size: 0.875rem;
    }

    .btn-primary {
      background: #007bff;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #0056b3;
      transform: translateY(-1px);
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover:not(:disabled) {
      background: #545b62;
    }

    .btn-warning {
      background: #ffc107;
      color: #212529;
    }

    .btn-warning:hover:not(:disabled) {
      background: #e0a800;
    }

    .btn-danger {
      background: #dc3545;
      color: white;
    }

    .btn-danger:hover:not(:disabled) {
      background: #c82333;
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      transform: none;
    }

    /* Filtros */
    .filters-section {
      max-width: 1400px;
      margin: 1rem auto;
      padding: 0 2rem;
    }

    .filters-wrapper {
      background: white;
      padding: 1.5rem;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .filters-wrapper h3 {
      margin: 0 0 1rem 0;
      color: #495057;
      font-size: 1.1rem;
    }

    .filters-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
      align-items: end;
    }

    .filter-group {
      display: flex;
      flex-direction: column;
    }

    .filter-group label {
      font-weight: 500;
      margin-bottom: 0.25rem;
      color: #495057;
      font-size: 0.875rem;
    }

    .filter-input,
    .filter-select {
      padding: 0.5rem;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 0.875rem;
    }

    .filter-input:focus,
    .filter-select:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
    }

    .checkbox-label {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      cursor: pointer;
      margin-top: 1.5rem;
    }

    .filter-actions {
      display: flex;
      gap: 0.5rem;
    }

    /* Mensajes y Loading */
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

    /* Tabla */
    .table-container {
      max-width: 1400px;
      margin: 0 auto;
      padding: 2rem;
    }

    .table-wrapper {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      overflow: hidden;
    }

    .insumos-table {
      width: 100%;
      border-collapse: collapse;
      font-size: 0.875rem;
    }

    .insumos-table th {
      background: #f8f9fa;
      padding: 1rem 0.75rem;
      text-align: left;
      font-weight: 600;
      color: #495057;
      border-bottom: 2px solid #dee2e6;
      white-space: nowrap;
    }

    .insumos-table td {
      padding: 1rem 0.75rem;
      border-bottom: 1px solid #dee2e6;
      vertical-align: top;
    }

    .insumos-table tr:hover {
      background: #f8f9fa;
    }

    .insumos-table tr.inactive {
      opacity: 0.6;
      background: #f8f9fa;
    }

    .insumos-table tr.stock-bajo {
      background: #fff3cd;
    }

    .codigo {
      font-family: 'Courier New', monospace;
      font-weight: 500;
      background: #e9ecef;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      font-size: 0.8rem;
    }

    .insumo-info strong {
      display: block;
      margin-bottom: 0.25rem;
    }

    .insumo-info small {
      color: #6c757d;
      font-size: 0.8rem;
    }

    .categoria-badge {
      background: #e3f2fd;
      color: #1565c0;
      padding: 0.25rem 0.5rem;
      border-radius: 12px;
      font-size: 0.75rem;
      font-weight: 500;
    }

    .stock-info {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
    }

    .stock-actual {
      font-weight: 600;
      font-size: 1rem;
    }

    .stock-actual.stock-bajo {
      color: #dc3545;
    }

    .stock-warning {
      margin-left: 0.5rem;
    }

    .precio {
      font-weight: 500;
      color: #28a745;
    }

    .estado-badge {
      padding: 0.25rem 0.75rem;
      border-radius: 12px;
      font-size: 0.75rem;
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

    .text-muted {
      color: #6c757d;
      font-style: italic;
    }

    .actions {
      display: flex;
      gap: 0.25rem;
      flex-wrap: wrap;
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
      font-size: 0.8rem;
    }

    .btn-edit {
      background: #fff3cd;
      color: #856404;
    }

    .btn-edit:hover:not(:disabled) {
      background: #ffeaa7;
      transform: scale(1.1);
    }

    .btn-stock {
      background: #d1ecf1;
      color: #0c5460;
    }

    .btn-stock:hover:not(:disabled) {
      background: #bee5eb;
      transform: scale(1.1);
    }

    .btn-toggle {
      background: #e2e3e5;
      color: #383d41;
    }

    .btn-toggle:hover:not(:disabled) {
      background: #d6d8db;
      transform: scale(1.1);
    }

    .btn-delete {
      background: #f8d7da;
      color: #721c24;
    }

    .btn-delete:hover:not(:disabled) {
      background: #f5c6cb;
      transform: scale(1.1);
    }

    .btn-action:disabled {
      opacity: 0.5;
      cursor: not-allowed;
      transform: none;
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
      margin: 0 0 0.5rem 0;
      color: #333;
    }

    .insumo-modal-info {
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
    }

    .insumo-modal-info strong {
      color: #495057;
    }

    .insumo-modal-info span {
      color: #6c757d;
      font-size: 0.875rem;
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

    .form-group {
      margin-bottom: 1rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 500;
      color: #495057;
    }

    .form-input,
    .form-select,
    .form-textarea {
      width: 100%;
      padding: 0.75rem;
      border: 1px solid #ced4da;
      border-radius: 4px;
      font-size: 0.875rem;
    }

    .form-input:focus,
    .form-select:focus,
    .form-textarea:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
    }

    .form-textarea {
      resize: vertical;
      min-height: 80px;
    }

    .stock-preview {
      margin-top: 1rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 4px;
      border-left: 4px solid #007bff;
    }

    .stock-preview p {
      margin: 0;
    }

    .error-text {
      color: #dc3545;
      font-weight: 500;
    }

    .insumo-details {
      background: #f8f9fa;
      padding: 1rem;
      border-radius: 4px;
      margin: 1rem 0;
    }

    .insumo-details p {
      margin: 0.25rem 0;
    }

    /* Responsive */
    @media (max-width: 1200px) {
      .header-content {
        flex-direction: column;
        gap: 1rem;
      }

      .header-actions {
        justify-content: center;
      }

      .filters-grid {
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      }
    }

    @media (max-width: 768px) {
      .header-content,
      .table-container,
      .filters-section,
      .messages {
        padding-left: 1rem;
        padding-right: 1rem;
      }

      .insumos-table {
        font-size: 0.75rem;
      }

      .insumos-table th,
      .insumos-table td {
        padding: 0.5rem 0.25rem;
      }

      .actions {
        flex-direction: column;
      }

      .btn-action {
        width: 28px;
        height: 28px;
      }

      .filters-grid {
        grid-template-columns: 1fr;
      }

      .filter-actions {
        grid-column: 1;
        justify-content: stretch;
      }

      .filter-actions .btn {
        flex: 1;
      }
  }]