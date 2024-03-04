import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isAutenticated$: any;

  constructor(public authService:AuthService) { }

  ngOnInit(): void {
    this.isAutenticated$ = this.authService.isAuthenticated();
  }

  logout() {
    this.authService
      .logout();
  }
login() {
    this.authService.login();
  }
}
