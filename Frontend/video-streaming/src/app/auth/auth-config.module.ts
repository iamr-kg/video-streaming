import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';
@NgModule({
    imports: [
        // ...
        AuthModule.forRoot({
          config: {
            authority: "https://dev-kg08ljqumrfxqu3c.us.auth0.com",
            redirectUrl: window.location.origin,
            postLogoutRedirectUri: window.location.origin,
            clientId: 'laLGnXT3joVWvQgGq8cMUp9cwoD5eHUc',
            scope: 'openid profile email offline_access',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            secureRoutes: ['http://localhost:8080/'],
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