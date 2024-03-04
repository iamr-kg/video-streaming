import { Video } from '../models/Video';
import { VideoService } from './../services/video.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit {
  featuredeVideos:Array<Video> = [];
  constructor(private videoService:VideoService) { }

  ngOnInit(): void {
    this.videoService.getAllVideos().subscribe({next:(values)=> {
      this.featuredeVideos = values;
    },
    })
  }

}
