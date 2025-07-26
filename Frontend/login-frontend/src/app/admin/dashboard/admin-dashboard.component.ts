// src/app/admin/dashboard/admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Usuario } from '../../shared/models/usuario.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: Usuario | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
  }

  logout(): void {
    this.authService.logout();
  }
}
