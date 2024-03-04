import { Component, Input, OnInit } from '@angular/core';
import { Video } from '../models/Video';

@Component({
  selector: 'app-video-card',
  templateUrl: './video-card.component.html',
  styleUrls: ['./video-card.component.css']
})
export class VideoCardComponent implements OnInit {
  @Input() video!:Video;

  constructor() { }

  ngOnInit(): void {
  }

}
