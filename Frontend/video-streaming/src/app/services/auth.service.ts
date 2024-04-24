import { Observable } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Injectable, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private oidcService:OidcSecurityService,private userService:UserService,private router:Router) { }

  ngOnInit(): void {
  }
  isAuthenticated():Observable<boolean>{
    return this.oidcService.isAuthenticated();
  }
  logout() {
    this.oidcService
      .logoffAndRevokeTokens()
      .subscribe((result) => console.log(result));
  }
login() {
  this.oidcService.authorize();
  }
}
