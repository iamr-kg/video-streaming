import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UploadVideoComponent } from './upload-video/upload-video.component';
import { FileDragNDropDirective } from './directives/file-drag-n-drop.directive';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { HeaderComponent } from './header/header.component';
import { VideoDetailsComponent } from './video-details/video-details.component';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { VideoPlayerComponent } from './video-player/video-player.component';
import { AuthConfigModule } from './auth/auth-config.module';
import { AuthInterceptor, AutoLoginPartialRoutesGuard } from 'angular-auth-oidc-client';
import { DisplayVideoComponent } from './display-video/display-video.component';
import { SideNavComponent } from './side-nav/side-nav.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { FeaturedComponent } from './featured/featured.component';
import { HistoryComponent } from './history/history.component';
import { LikedVideosComponent } from './liked-videos/liked-videos.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { HomeComponent } from './home/home.component';
import { MatListModule } from '@angular/material/list';
import { VideoCardComponent } from './video-card/video-card.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  schemas: [],
  declarations: [
    AppComponent,
    UploadVideoComponent,
    FileDragNDropDirective,
    HeaderComponent,
    VideoDetailsComponent,
    VideoPlayerComponent,
    DisplayVideoComponent,
    SideNavComponent,
    HomeComponent,
    FeaturedComponent,
    HistoryComponent,
    LikedVideosComponent,
    SubscriptionComponent,
    VideoCardComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    HttpClientModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatSelectModule,
    MatFormFieldModule,
    MatChipsModule,
    ReactiveFormsModule,
    MatInputModule,
    VgCoreModule,
    VgControlsModule,
    VgOverlayPlayModule,
    VgBufferingModule,
    AuthConfigModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
  ],

  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },],
  bootstrap: [AppComponent],
})
export class AppModule {}
