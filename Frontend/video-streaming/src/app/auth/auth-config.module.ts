import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';
@NgModule({
    imports: [
        
        AuthModule.forRoot({
          config: {
            authority: "https://dev-kg08ljqumrfxqu3c.us.auth0.com",
            redirectUrl: "https://127.0.0.1:4200/callback",
            postLogoutRedirectUri: "https://127.0.0.1:4200/",
            clientId: 'laLGnXT3joVWvQgGq8cMUp9cwoD5eHUc',
            scope: 'openid profile email offline_access',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            secureRoutes: ['http://localhost:8080/'],
            //triggerAuthorizationResultEvent:true,
            customParamsAuthRequest:{
              audience:'http://localhost:8080',
            }
          },
        }),
      ],
    exports: [AuthModule],
})


export class AuthConfigModule{

}