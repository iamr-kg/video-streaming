import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'video-streaming';

  constructor(public oidcSecurityService:OidcSecurityService){}
  ngOnInit(): void {
   this.oidcSecurityService.checkAuth().subscribe({
    next:(value)=>{
      console.log(`The auth server response:`,value);
    }
   });
  } 
}


