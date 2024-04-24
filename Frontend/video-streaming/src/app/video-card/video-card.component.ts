import { Component, Input, OnInit } from '@angular/core';
import { Video } from '../models/Video';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-video-card',
  templateUrl: './video-card.component.html',
  styleUrls: ['./video-card.component.css']
})
export class VideoCardComponent implements OnInit {
  @Input() video!:Video;
  userPicture!:string;

  constructor(private userService:UserService) { }

  ngOnInit(): void {
    this.userPicture = <string>sessionStorage.getItem('userPicture');
  }

}
