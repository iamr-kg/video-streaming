import { Component, OnInit } from '@angular/core';
import { Video } from '../models/Video';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-liked-videos',
  templateUrl: './liked-videos.component.html',
  styleUrls: ['./liked-videos.component.css']
})
export class LikedVideosComponent implements OnInit {
  likedVideos!:Array<Video>
  constructor(private userService:UserService) { }

  ngOnInit(): void {
    this.userService.getLikedVideos().subscribe((videos)=>{
      this.likedVideos = videos;
    })
  }

}
