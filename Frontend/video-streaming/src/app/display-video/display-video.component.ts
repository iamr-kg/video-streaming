
import { UserService } from './../services/user.service';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../services/video.service';
import { Video } from '../models/Video';

@Component({
  selector: 'app-display-video',
  templateUrl: './display-video.component.html',
  styleUrls: ['./display-video.component.css'],
})
export class DisplayVideoComponent implements OnInit {
  @Input()
  videoId!:number;
  videoDetails!:Video;
  videoUrl!:string;
  videoAvailable: boolean = false;
  tags!: string[];
  likeCount!: number;
  UserReaction: number = 0;
  disLikeCount!: number;
  showSubscribeButton: boolean = false;
  constructor(
    private activatedRoute: ActivatedRoute,
    private videoService: VideoService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.videoId = Number(this.activatedRoute.snapshot.paramMap.get('videoId'));
    this.videoService.getVideo(this.videoId).subscribe({
      next: (video) => {
        this.videoDetails = video;
        this.tags = (<string>video.tags)?.split(',');
        this.videoUrl = <string>video.videoUrl;
      },
      complete: () => {
        this.videoAvailable = true;
        this.isSubscribedToUser(<number>this.videoDetails.user?.id);
      },
    });

    this.videoService.getVideoReaction(this.videoId).subscribe({
      next: (reaction) => {
        this.likeCount = reaction.likeCount;
        this.disLikeCount = reaction.disLikeCount;
        this.UserReaction = reaction.reactionByUser;
      },
    });
  }
  isSubscribedToUser(userId: number) {
    this.userService.isSubscribedToUser(userId).subscribe((data) => {
      this.showSubscribeButton = !Boolean(data);
    });
  }
  disLikeVideo() {
    this.videoService.disLikeVideo(this.videoId).subscribe({
      next: (reaction) => {
        this.likeCount = reaction.likeCount;
        this.disLikeCount = reaction.disLikeCount;
        this.UserReaction = reaction.reactionByUser;
      },
    });
  }

  likeVideo() {
    this.videoService.likeVideo(this.videoId).subscribe({
      next: (reaction) => {
        this.likeCount = reaction.likeCount;
        this.disLikeCount = reaction.disLikeCount;
        this.UserReaction = reaction.reactionByUser;
      },
    });
  }

  unSubscribeToUser() {
    this.userService.unSubscribeToUser(<number>this.videoDetails.user?.id).subscribe({
      complete: () => (this.showSubscribeButton = true),
    });
  }
  subscribeToUser() {
    this.userService.subscribeToUser(<number>this.videoDetails.user?.id).subscribe({
      complete: () => (this.showSubscribeButton = false),
    });
  }
}
