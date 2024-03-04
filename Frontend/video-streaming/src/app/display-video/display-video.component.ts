import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../services/video.service';

@Component({
  selector: 'app-display-video',
  templateUrl: './display-video.component.html',
  styleUrls: ['./display-video.component.css']
})
export class DisplayVideoComponent implements OnInit {
  videoId!:number;
  videoUrl!:string;
  videoTitle!:String;
  thumbnailUrl!:String;
  videoAvailable:boolean = false;
  description!: String;
  tags!: string[];

  constructor(private activatedRoute: ActivatedRoute,private videoService:VideoService) { }

  ngOnInit(): void {
    this.videoId = Number(this.activatedRoute.snapshot.paramMap.get('videoId'));
    this.videoService.getVideo(this.videoId).subscribe({
      next:(video)=>{
        this.videoUrl = <string>video.videoUrl;
        this.videoTitle = <String>video.title;
        this.thumbnailUrl = <String>video.thumbnailUrl;
        this.description = <string>video.description;
        this.tags = (<string>video.tags)?.split(",");
      },
    complete:()=> this.videoAvailable = true});
    
  }

}
