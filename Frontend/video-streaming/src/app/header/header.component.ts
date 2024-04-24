import { OidcSecurityService } from 'angular-auth-oidc-client';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isAutenticated: boolean = false;

  constructor(public authService:AuthService,private cd:ChangeDetectorRef) { }

  ngOnInit(): void {
     this.authService.isAuthenticated().subscribe((value)=> {this.isAutenticated = value
     console.log(this.isAutenticated)});
  }

  logout() {
    this.authService
      .logout();
  }
login() {
    this.authService.login();
  }
}
