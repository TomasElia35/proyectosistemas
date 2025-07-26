import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { InsumoService, InsumoRequestDTO } from '../../services/insumo.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-insumo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="insumo-form-container">
      <div class="header">
        <div class="header-content">
          <h1>{{ isEditMode ? 'Editar Insumo' : 'Crear Insumo' }}</h1>
          <button class="btn btn-secondary" (click)="volver()">
            <i class="icon">←</i>
            Volver
          </button>
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
        <p>{{ isEditMode ? 'Cargando datos del insumo...' : 'Cargando formulario...' }}</p>
      </div>

      <!-- Formulario -->
      <div *ngIf="!isLoading" class="form-container">
        <div class="form-wrapper">
          <form [formGroup]="insumoForm" (ngSubmit)="onSubmit()" class="insumo-form">
            
            <div class="form-row">
              <!-- Nombre -->
              <div class="form-group">
                <label for="nombre" class="form-label">Nombre *</label>
                <input
                  type="text"
                  id="nombre"
                  formControlName="nombre"
                  class="form-input"
                  [class.error]="nombre?.invalid && nombre?.touched"
                  placeholder="Ingrese el nombre del insumo"
                  (focus)="clearMessages()"
                >
                <div *ngIf="nombre?.invalid && nombre?.touched" class="error-message">
                  {{ getFieldErrorMessage('nombre') }}
                </div>
              </div>

              <!-- Código -->
              <div class="form-group">
                <label for="codigo" class="form-label">Código *</label>
                <input
                  type="text"
                  id="codigo"
                  formControlName="codigo"
                  class="form-input code-input"
                  [class.error]="codigo?.invalid && codigo?.touched"
                  placeholder="Ej: INS-2024-001"
                  (focus)="clearMessages()"
                  (blur)="onCodigoBlur()"
                >
                <div *ngIf="codigo?.invalid && codigo?.touched" class="error-message">
                  {{ getFieldErrorMessage('codigo') }}
                </div>
                <small class="help-text">Código único del insumo (se convertirá a mayúsculas)</small>
              </div>
            </div>

            <!-- Descripción -->
            <div class="form-group">
              <label for="descripcion" class="form-label">Descripción</label>
              <textarea
                id="descripcion"
                formControlName="descripcion"
                class="form-textarea"
                [class.error]="descripcion?.invalid && descripcion?.touched"
                placeholder="Descripción detallada del insumo..."
                rows="3"
                (focus)="clearMessages()"
              ></textarea>
              <div *ngIf="descripcion?.invalid && descripcion?.touched" class="error-message">
                {{ getFieldErrorMessage('descripcion') }}
              </div>
            </div>

            <div class="form-row">
              <!-- Categoría -->
              <div class="form-group">
                <label for="categoria" class="form-label">Categoría</label>
                <select
                  id="categoria"
                  formControlName="categoria"
                  class="form-select"
                  (focus)="clearMessages(); cargarCategorias()"
                >
                  <option value="">Seleccione una categoría</option>
                  <option *ngFor="let cat of categorias" [value]="cat">{{ cat }}</option>
                </select>
                <small class="help-text">O escriba una nueva categoría</small>
                <input
                  type="text"
                  placeholder="Nueva categoría..."
                  class="form-input form-input-small"
                  (keyup.enter)="agregarCategoria($event)"
                  #nuevaCategoria
                >
              </div>

              <!-- Proveedor -->
              <div class="form-group">
                <label for="proveedor" class="form-label">Proveedor</label>
                <select
                  id="proveedor"
                  formControlName="proveedor"
                  class="form-select"
                  (focus)="clearMessages(); cargarProveedores()"
                >
                  <option value="">Seleccione un proveedor</option>
                  <option *ngFor="let prov of proveedores" [value]="prov">{{ prov }}</option>
                </select>
                <small class="help-text">O escriba un nuevo proveedor</small>
                <input
                  type="text"
                  placeholder="Nuevo proveedor..."
                  class="form-input form-input-small"
                  (keyup.enter)="agregarProveedor($event)"
                  #nuevoProveedor
                >
              </div>
            </div>

            <div class="form-row">
              <!-- Stock inicial -->
              <div class="form-group">
                <label for="stock" class="form-label">Stock Inicial *</label>
                <input
                  type="number"
                  id="stock"
                  formControlName="stock"
                  class="form-input"
                  [class.error]="stock?.invalid && stock?.touched"
                  placeholder="0"
                  min="0"
                  (focus)="clearMessages()"
                >
                <div *ngIf="stock?.invalid && stock?.touched" class="error-message">
                  {{ getFieldErrorMessage('stock') }}
                </div>
              </div>

              <!-- Stock mínimo -->
              <div class="form-group">
                <label for="stockMinimo" class="form-label">Stock Mínimo</label>
                <input
                  type="number"
                  id="stockMinimo"
                  formControlName="stockMinimo"
                  class="form-input"
                  [class.error]="stockMinimo?.invalid && stockMinimo?.touched"
                  placeholder="0"
                  min="0"
                  (focus)="clearMessages()"
                >
                <div *ngIf="stockMinimo?.invalid && stockMinimo?.touched" class="error-message">
                  {{ getFieldErrorMessage('stockMinimo') }}
                </div>
                <small class="help-text">Alerta cuando el stock sea igual o menor a este valor</small>
              </div>

              <!-- Unidad de medida -->
              <div class="form-group">
                <label for="unidadMedida" class="form-label">Unidad de Medida</label>
                <select
                  id="unidadMedida"
                  formControlName="unidadMedida"
                  class="form-select"
                >
                  <option value="">Seleccione unidad</option>
                  <option value="UNIDAD">Unidad</option>
                  <option value="KG">Kilogramo</option>
                  <option value="LITROS">Litros</option>
                  <option value="METROS">Metros</option>
                  <option value="CAJAS">Cajas</option>
                  <option value="PAQUETES">Paquetes</option>
                </select>
              </div>
            </div>

            <!-- Precio -->
            <div class="form-row">
              <div class="form-group">
                <label for="precio" class="form-label">Precio Unitario</label>
                <div class="input-group">
                  <span class="input-prefix">$</span>
                  <input
                    type="number"
                    id="precio"
                    formControlName="precio"
                    class="form-input"
                    [class.error]="precio?.invalid && precio?.touched"
                    placeholder="0.00"
                    min="0"
                    step="0.01"
                    (focus)="clearMessages()"
                  >
                </div>
                <div *ngIf="precio?.invalid && precio?.touched" class="error-message">
                  {{ getFieldErrorMessage('precio') }}
                </div>
              </div>

              <!-- Estado -->
              <div class="form-group">
                <div class="checkbox-wrapper">
                  <label class="checkbox-label">
                    <input
                      type="checkbox"
                      formControlName="estado"
                      class="checkbox-input"
                    >
                    <span class="checkbox-custom"></span>
                    Insumo activo
                  </label>
                  <small class="help-text">Los insumos inactivos no aparecen en búsquedas</small>
                </div>
              </div>
            </div>

            <!-- Vista previa de datos -->
            <div class="preview-section" *ngIf="insumoForm.valid">
              <h3>Vista previa</h3>
              <div class="preview-card">
                <div class="preview-header">
                  <strong>{{ insumoForm.get('nombre')?.value }}</strong>
                  <span class="preview-code">{{ insumoForm.get('codigo')?.value?.toUpperCase() }}</span>
                </div>
                <div class="preview-body">
                  <p *ngIf="insumoForm.get('descripcion')?.value">{{ insumoForm.get('descripcion')?.value }}</p>
                  <div class="preview-details">
                    <span *ngIf="insumoForm.get('categoria')?.value" class="detail-tag">{{ insumoForm.get('categoria')?.value }}</span>
                    <span *ngIf="insumoForm.get('proveedor')?.value" class="detail-tag">{{ insumoForm.get('proveedor')?.value }}</span>
                  </div>
                  <div class="preview-stock">
                    <span>Stock: {{ insumoForm.get('stock')?.value || 0 }}</span>
                    <span *ngIf="insumoForm.get('stockMinimo')?.value">Mín: {{ insumoForm.get('stockMinimo')?.value }}</span>
                    <span *ngIf="insumoForm.get('unidadMedida')?.value">{{ insumoForm.get('unidadMedida')?.value }}</span>
                  </div>
                  <div class="preview-price" *ngIf="insumoForm.get('precio')?.value">
                    <strong>\${{ insumoForm.get('precio')?.value | number:'1.2-2' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <!-- Botones -->
            <div class="form-actions">
              <button
                type="button"
                class="btn btn-secondary"
                (click)="volver()"
                [disabled]="isSubmitting"
              >
                Cancelar
              </button>
              
              <button
                type="submit"
                class="btn btn-primary"
                [disabled]="isSubmitting || insumoForm.invalid"
                [class.loading]="isSubmitting"
              >
                <span *ngIf="!isSubmitting">
                  {{ isEditMode ? 'Actualizar Insumo' : 'Crear Insumo' }}
                </span>
                <span *ngIf="isSubmitting" class="loading-content">
                  <div class="spinner"></div>
                  {{ isEditMode ? 'Actualizando...' : 'Creando...' }}
                </span>
              </button>
            </div>

          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .insumo-form-container {
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
      align-items: center;
      max-width: 1000px;
      margin: 0 auto;
    }

    .header h1 {
      margin: 0;
      color: #333;
      font-size: 1.8rem;
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
      min-height: 48px;
      justify-content: center;
    }

    .btn-primary:hover:not(:disabled) {
      background: #0056b3;
      transform: translateY(-1px);
    }

    .btn-primary:disabled {
      background: #6c757d;
      cursor: not-allowed;
      transform: none;
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover:not(:disabled) {
      background: #545b62;
    }

    .messages {
      max-width: 1000px;
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
      width: 20px;
      height: 20px;
      border: 2px solid #f3f3f3;
      border-top: 2px solid #007bff;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .form-container {
      max-width: 1000px;
      margin: 0 auto;
      padding: 2rem;
    }

    .form-wrapper {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      padding: 2rem;
    }

    .insumo-form {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }

    .form-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1.5rem;
    }

    .form-group {
      display: flex;
      flex-direction: column;
    }

    .form-label {
      color: #333;
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 8px;
    }

    .form-input,
    .form-select,
    .form-textarea {
      padding: 12px 16px;
      border: 2px solid #e1e5e9;
      border-radius: 8px;
      font-size: 16px;
      transition: all 0.3s ease;
      background-color: #fff;
    }

    .form-input:focus,
    .form-select:focus,
    .form-textarea:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    .form-input.error,
    .form-select.error,
    .form-textarea.error {
      border-color: #dc3545;
      box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
    }

    .form-input::placeholder,
    .form-textarea::placeholder {
      color: #adb5bd;
    }

    .form-textarea {
      resize: vertical;
      min-height: 80px;
    }

    .form-input-small {
      margin-top: 0.5rem;
      font-size: 14px;
      padding: 8px 12px;
    }

    .code-input {
      font-family: 'Courier New', monospace;
      text-transform: uppercase;
    }

    .input-group {
      position: relative;
      display: flex;
      align-items: center;
    }

    .input-prefix {
      position: absolute;
      left: 12px;
      color: #6c757d;
      font-weight: 500;
      z-index: 1;
    }

    .input-group .form-input {
      padding-left: 2rem;
    }

    .error-message {
      color: #dc3545;
      font-size: 12px;
      margin-top: 6px;
      font-weight: 500;
    }

    .help-text {
      color: #6c757d;
      font-size: 12px;
      margin-top: 4px;
    }

    .checkbox-wrapper {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
      margin-top: 1.5rem;
    }

    .checkbox-label {
      display: flex;
      align-items: center;
      cursor: pointer;
      user-select: none;
      gap: 0.75rem;
      font-weight: 500;
      color: #333;
    }

    .checkbox-input {
      position: absolute;
      opacity: 0;
      cursor: pointer;
    }

    .checkbox-custom {
      width: 20px;
      height: 20px;
      border: 2px solid #e1e5e9;
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      background: white;
    }

    .checkbox-input:checked + .checkbox-custom {
      background: #007bff;
      border-color: #007bff;
    }

    .checkbox-input:checked + .checkbox-custom::after {
      content: '✓';
      color: white;
      font-size: 12px;
      font-weight: bold;
    }

    .checkbox-input:focus + .checkbox-custom {
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    .preview-section {
      margin-top: 2rem;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
      border-left: 4px solid #007bff;
    }

    .preview-section h3 {
      margin: 0 0 1rem 0;
      color: #495057;
      font-size: 1.1rem;
    }

    .preview-card {
      background: white;
      border-radius: 6px;
      padding: 1rem;
      border: 1px solid #dee2e6;
    }

    .preview-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.5rem;
      padding-bottom: 0.5rem;
      border-bottom: 1px solid #dee2e6;
    }

    .preview-header strong {
      color: #333;
      font-size: 1.1rem;
    }

    .preview-code {
      font-family: 'Courier New', monospace;
      background: #e9ecef;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      font-size: 0.8rem;
    }

    .preview-body p {
      color: #6c757d;
      margin: 0.5rem 0;
      font-size: 0.9rem;
    }

    .preview-details {
      display: flex;
      gap: 0.5rem;
      margin: 0.5rem 0;
      flex-wrap: wrap;
    }

    .detail-tag {
      background: #e3f2fd;
      color: #1565c0;
      padding: 0.25rem 0.5rem;
      border-radius: 12px;
      font-size: 0.75rem;
      font-weight: 500;
    }

    .preview-stock {
      display: flex;
      gap: 1rem;
      margin: 0.5rem 0;
      color: #495057;
      font-size: 0.9rem;
    }

    .preview-price {
      text-align: right;
      margin-top: 0.5rem;
      padding-top: 0.5rem;
      border-top: 1px solid #dee2e6;
    }

    .preview-price strong {
      color: #28a745;
      font-size: 1.1rem;
    }

    .form-actions {
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
      margin-top: 2rem;
      padding-top: 1.5rem;
      border-top: 1px solid #dee2e6;
    }

    .loading-content {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .header-content {
        flex-direction: column;
        gap: 1rem;
        align-items: stretch;
      }

      .form-container {
        padding: 1rem;
      }

      .form-wrapper {
        padding: 1.5rem;
      }

      .form-row {
        grid-template-columns: 1fr;
      }

      .form-actions {
        flex-direction: column-reverse;
      }

      .btn {
        width: 100%;
        justify-content: center;
      }

      .preview-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.5rem;
      }
    }
  `]
})
export class InsumoFormComponent implements OnInit {
  insumoForm: FormGroup;
  categorias: string[] = [];
  proveedores: string[] = [];
  isEditMode = false;
  insumoId: number | null = null;
  isLoading = false;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';
  isAdmin = false;

  constructor(
    private fb: FormBuilder,
    private insumoService: InsumoService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.insumoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      descripcion: ['', [Validators.maxLength(500)]],
      codigo: ['', [Validators.required, Validators.maxLength(50)]],
      precio: ['', [Validators.min(0)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      stockMinimo: ['', [Validators.min(0)]],
      unidadMedida: [''],
      categoria: [''],
      proveedor: [''],
      estado: [true]
    });
  }

  ngOnInit(): void {
    this.checkAdminRole();
    this.checkEditMode();
    this.cargarCategorias();
    this.cargarProveedores();
  }

  checkAdminRole(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isAdmin = user?.rol?.nombre === 'ADMINISTRADOR';
    });
  }

  checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.insumoId = parseInt(id, 10);
      this.cargarDatosInsumo();
    }
  }

  cargarDatosInsumo(): void {
    if (!this.insumoId) return;

    this.isLoading = true;

    this.insumoService.obtenerInsumoPorId(this.insumoId).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.exito) {
          const insumo = response.datos;
          this.insumoForm.patchValue({
            nombre: insumo.nombre,
            descripcion: insumo.descripcion,
            codigo: insumo.codigo,
            precio: insumo.precio,
            stock: insumo.stock,
            stockMinimo: insumo.stockMinimo,
            unidadMedida: insumo.unidadMedida,
            categoria: insumo.categoria,
            proveedor: insumo.proveedor,
            estado: insumo.estado
          });
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

  cargarCategorias(): void {
    this.insumoService.obtenerCategorias().subscribe({
      next: (response) => {
        if (response.exito) {
          this.categorias = response.datos || [];
        }
      },
      error: (error) => {
        console.error('Error cargando categorías:', error);
      }
    });
  }

  cargarProveedores(): void {
    this.insumoService.obtenerProveedores().subscribe({
      next: (response) => {
        if (response.exito) {
          this.proveedores = response.datos || [];
        }
      },
      error: (error) => {
        console.error('Error cargando proveedores:', error);
      }
    });
  }

  onCodigoBlur(): void {
    const codigoControl = this.insumoForm.get('codigo');
    if (codigoControl?.value) {
      codigoControl.setValue(codigoControl.value.toUpperCase());
    }
  }

  agregarCategoria(event: Event): void {
    const input = event.target as HTMLInputElement;
    const nuevaCategoria = input.value.trim();
    
    if (nuevaCategoria && !this.categorias.includes(nuevaCategoria)) {
      this.categorias.push(nuevaCategoria);
      this.insumoForm.get('categoria')?.setValue(nuevaCategoria);
      input.value = '';
    }
  }

  agregarProveedor(event: Event): void {
    const input = event.target as HTMLInputElement;
    const nuevoProveedor = input.value.trim();
    
    if (nuevoProveedor && !this.proveedores.includes(nuevoProveedor)) {
      this.proveedores.push(nuevoProveedor);
      this.insumoForm.get('proveedor')?.setValue(nuevoProveedor);
      input.value = '';
    }
  }

  onSubmit(): void {
    if (this.insumoForm.valid) {
      this.isSubmitting = true;
      this.clearMessages();

      const formData = this.insumoForm.value;
      
      // Preparar datos para enviar
      const insumoData: InsumoRequestDTO = {
        nombre: formData.nombre.trim(),
        descripcion: formData.descripcion?.trim() || undefined,
        codigo: formData.codigo.trim().toUpperCase(),
        precio: formData.precio || undefined,
        stock: formData.stock,
        stockMinimo: formData.stockMinimo || undefined,
        unidadMedida: formData.unidadMedida || undefined,
        categoria: formData.categoria || undefined,
        proveedor: formData.proveedor || undefined,
        estado: formData.estado
      };

      const operation = this.isEditMode
        ? this.insumoService.actualizarInsumo(this.insumoId!, insumoData)
        : this.insumoService.crearInsumo(insumoData);

      operation.subscribe({
        next: (response) => {
          this.isSubmitting = false;
          if (response.exito) {
            this.successMessage = response.mensaje;
            // Redirigir después de 2 segundos
            setTimeout(() => {
              this.volver();
            }, 2000);
          } else {
            this.errorMessage = response.mensaje;
          }
        },
        error: (error) => {
          this.isSubmitting = false;
          this.errorMessage = error;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  markFormGroupTouched(): void {
    Object.keys(this.insumoForm.controls).forEach(key => {
      const control = this.insumoForm.get(key);
      control?.markAsTouched();
    });
  }

  volver(): void {
    if (this.isAdmin) {
      this.router.navigate(['/admin/insumos']);
    } else {
      this.router.navigate(['/insumos']);
    }
  }

  clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }

  // Getters para acceso fácil a los controles
  get nombre() { return this.insumoForm.get('nombre'); }
  get descripcion() { return this.insumoForm.get('descripcion'); }
  get codigo() { return this.insumoForm.get('codigo'); }
  get precio() { return this.insumoForm.get('precio'); }
  get stock() { return this.insumoForm.get('stock'); }
  get stockMinimo() { return this.insumoForm.get('stockMinimo'); }
  get unidadMedida() { return this.insumoForm.get('unidadMedida'); }
  get categoria() { return this.insumoForm.get('categoria'); }
  get proveedor() { return this.insumoForm.get('proveedor'); }
  get estado() { return this.insumoForm.get('estado'); }

  getFieldErrorMessage(fieldName: string): string {
    const field = this.insumoForm.get(fieldName);
    
    if (field?.hasError('required')) {
      const fieldNames: { [key: string]: string } = {
        nombre: 'El nombre',
        codigo: 'El código',
        stock: 'El stock'
      };
      return `${fieldNames[fieldName]} es obligatorio`;
    }
    
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength']?.requiredLength;
      return `Debe tener al menos ${minLength} caracteres`;
    }
    
    if (field?.hasError('maxlength')) {
      const maxLength = field.errors?.['maxlength']?.requiredLength;
      return `No puede exceder ${maxLength} caracteres`;
    }
    
    if (field?.hasError('min')) {
      return 'El valor no puede ser negativo';
    }
    
    return '';
  }
}