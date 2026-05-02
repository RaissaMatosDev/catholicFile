import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class Dashboard implements OnInit {
  nomeUsuario = '';
  isAdmin = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

ngOnInit(): void {
  const payload = this.authService.getPayload();
  this.nomeUsuario = payload?.sub ?? 'Usuário';
  this.isAdmin = payload?.roles?.includes('ROLE_ADMINISTRADOR') ?? false;
}
  logout(): void {
    this.authService.logout();
  }
}
