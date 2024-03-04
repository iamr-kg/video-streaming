import { Observable } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private oidcService:OidcSecurityService,private http:HttpClient,private userService:UserService) { }

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
    this.userService.registerUserInDb();
  }
}
