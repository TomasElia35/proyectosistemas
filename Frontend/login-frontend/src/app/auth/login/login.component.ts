import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth.service';
import { LoginRequest } from '../../shared/models/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  returnUrl!: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    // Redirigir si ya está logueado
    if (this.authService.currentUserValue) {
      this.redirectToAppropriateRole();
    }
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      mail: ['', [Validators.required, Validators.email]],
      contrasena: ['', Validators.required]
    });

    // Obtener URL de retorno de los parámetros de consulta o usar por defecto
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const loginData: LoginRequest = {
      mail: this.f['mail'].value,
      contrasena: this.f['contrasena'].value
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        this.loading = false;
        this.redirectToAppropriateRole();
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.mensaje || 'Error en el login. Verifique sus credenciales.';
      }
    });
  }

  private redirectToAppropriateRole(): void {
    const userRole = this.authService.getUserRole();
    
    if (userRole === 'ADMINISTRADOR') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/user/dashboard']);
    }
  }
}