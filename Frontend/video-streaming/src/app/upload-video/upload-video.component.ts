import { VideoService } from './../services/video.service';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-upload-video',
  templateUrl: './upload-video.component.html',
  styleUrls: ['./upload-video.component.css']
})
export class UploadVideoComponent implements OnInit {
  file!: File;
  allowedFileType : String[] = ["video/mp4"];
  isValidFile:boolean = false;
  constructor(private _snackBar: MatSnackBar,private videoService:VideoService, private router:Router) { }  
  ngOnInit(): void {

  }
onFileChange($event:any) {
  this.file  = $event instanceof Event? ($event.target as HTMLInputElement)?.files?.item(0) as File:$event[0];
  this.isValidFile = this.allowedFileType.includes(this.file?.type as String);
  if(!this.isValidFile || (this.file?.size as number)<=0){
    this._snackBar.open("please upload a valid video",'Close',{duration:2000});
    this.isValidFile = false;
    return;
  }
  this.isValidFile = true; 
}
  uploadVideo(file: File) {
    this.videoService.uploadVideo(file).subscribe({
       next:(data)=>{
        this.router.navigate(['/video-details',data.videoId]);
      },
      error:(err:Error)=>{
        console.log(err.message);
      },
      complete:()=>{
        this._snackBar.open("video uploaded successfully",'close',{duration:2000});
        this.isValidFile = false;
      }
    }
    )
  }
  delteFile() {
    this.isValidFile = false;
  }


}
