import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Video } from '../models/Video';

@Injectable({
  providedIn: 'root'
})

export class VideoService {
  saveDetails(metaData:Video):Observable<Video> {
    return this.http.put<Video>("http://localhost:8080/vsa/video/videoDetails",metaData);
  }
  uploadThumbnail(file :File,videoId:number):Observable<any> {
    const formData = new FormData();
    formData.append("file",file);
    formData.append("videoId",videoId.toString());
    return this.http.post("http://localhost:8080/vsa/video/uploadThumbnail",formData,{responseType:'text'});
  }

  constructor(private http:HttpClient) { }

  uploadVideo(file:File):Observable<Video>{
    const formData = new FormData();
    formData.append("file",file)
    return this.http.post<Video>("http://localhost:8080/vsa/video/uploadVideo",formData);
  }

  getVideo(videoId:number):Observable<Video>{
    return this.http.get<Video>("http://localhost:8080/vsa/video/getVideo/"+videoId);
  }

  getAllVideos():Observable<Array<Video>>{
    return this.http.get<Array<Video>>("http://localhost:8080/vsa/video/getAllVideos");
  }
}
