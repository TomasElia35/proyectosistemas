// src/main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app/app.routes';
import { authInterceptor } from './app/auth/interceptors/auth.interceptor';
//        ^^^^^^^^^^^^^^^ - NOMBRE CORRECTO

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptor]) // ✅ AQUÍ VA
    )
  ]
}).catch(err => console.error(err));