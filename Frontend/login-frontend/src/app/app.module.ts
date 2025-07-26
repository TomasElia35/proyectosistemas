import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS, provideHttpClient, withInterceptors } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Auth Components
import { LoginComponent } from './auth/login/login.component';

// Admin Components
import { AdminDashboardComponent } from './admin/dashboard/admin-dashboard.component';
import { ListaUsuariosComponent } from './admin/usuarios/lista-usuarios/lista-usuarios.component';
import { CrearUsuarioComponent } from './admin/usuarios/crear-usuario/crear-usuario.component';

// User Components
import { UserDashboardComponent } from './user/dashboard/user-dashboard.component';

// Interceptors
import { authInterceptor } from './shared/interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminDashboardComponent,
    ListaUsuariosComponent,
    CrearUsuarioComponent,
    UserDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor])  // ✅ registrar interceptor como función
    )
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }