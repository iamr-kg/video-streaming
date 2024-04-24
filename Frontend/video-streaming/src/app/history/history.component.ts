import { Video } from '../models/Video';
import { UserService } from './../services/user.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  historyVideos!:Array<Video>
  constructor(private userService:UserService) { }

  ngOnInit(): void {
    this.userService.getHistory().subscribe((videos)=>{
      this.historyVideos = videos;
    })
  }

}
