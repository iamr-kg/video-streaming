
import { Component, Input, OnInit } from '@angular/core';
import { Video } from '../models/Video';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatSnackBar } from '@angular/material/snack-bar';
import { VideoService } from '../services/video.service';
@Component({
  selector: 'app-video-details',
  templateUrl: './video-details.component.html',
  styleUrls: ['./video-details.component.css'],
 
})
export class VideoDetailsComponent implements OnInit {
  fileInfo: String = '';
  file!: File;
  videoId!: number;
  saveVideoDetailsFrom!: FormGroup;
  title: FormControl = new FormControl();
  description: FormControl = new FormControl();
  videoStatus: FormControl = new FormControl();
  videoUrl!:string;
  addOnBlur = true;
  readonly separatorKeysCodes = [13, 188] as const;
  tags: String[] = [];
  constructor(private activatedRoute: ActivatedRoute,private videoService:VideoService,private _snackBar: MatSnackBar) {
    this.saveVideoDetailsFrom = new FormGroup({
      title: this.title,
      description: this.description,
      videoStatus: this.videoStatus,
    });
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      if (params.has('videoId')) {
        this.videoId = Number(params.get('videoId'));
      }
    });

    this.videoService.getVideo(this.videoId).subscribe({
      next:(video)=>{
        this.videoUrl =  <string>video.videoUrl;
      }
    })
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(value: String): void {
    const index = this.tags.indexOf(value);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }

  onThumbnailFileSelect($event: EventTarget | null) {
    function formatBytes(bytes: number): String {
      const UNITS = ['Bytes', 'kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
      const factor = 1024;
      let index = 0;

      while (bytes >= factor) {
        bytes /= factor;
        index++;
      }

      return `${parseFloat(bytes.toFixed(2))} ${UNITS[index]}`;
    }
    this.file = ($event as HTMLInputElement).files?.item(0) as File;

    this.fileInfo = `${this.file.name} (${formatBytes(this.file.size)})`;
  }

  onThumbnailUpload() {
    this.videoService.uploadThumbnail(this.file,this.videoId).subscribe({
      next:(data)=>{
        console.log(data);
        this._snackBar.open(`Thumbnail uploaded sucessfully`,'close',{duration:2000});
      },
      error:(err)=>{
        this._snackBar.open(`Uploading Error`,'close',{duration:2000});
      }
    })
    }

  saveVideoDetails(){
    let videoMetaData:Video = {
      videoId : this.videoId,
      title: this.saveVideoDetailsFrom.get('title')?.value,
      description: this.saveVideoDetailsFrom.get('description')?.value,
      videoStatus: this.saveVideoDetailsFrom.get('videoStatus')?.value,
      tags:this.tags.toString()
    }
    this.videoService.saveDetails(videoMetaData).subscribe({
      next:()=>{
        this._snackBar.open(`Details saved sucessfully`,'close',{duration:2000});
      }
    })

  }
}
