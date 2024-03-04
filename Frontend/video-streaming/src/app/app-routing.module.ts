import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UploadVideoComponent } from './upload-video/upload-video.component';
import { VideoDetailsComponent } from './video-details/video-details.component';
import { DisplayVideoComponent } from './display-video/display-video.component';
import { HomeComponent } from './home/home.component';
import { FeaturedComponent } from './featured/featured.component';
import { HistoryComponent } from './history/history.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { LikedVideosComponent } from './liked-videos/liked-videos.component';

const routes: Routes = [{path:"upload",component:UploadVideoComponent},
{path:"video-details/:videoId",component:VideoDetailsComponent},
{path:"",component:HomeComponent,children:[{
  path:"featured",component:FeaturedComponent
},{
  path:"history",component:HistoryComponent
},{
  path:"subscription",component:SubscriptionComponent
},{
  path:"liked",component:LikedVideosComponent,
}]},
{path:"display-video/:videoId",component:DisplayVideoComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
