import { VideoReaction } from './../models/videoreaction';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Video } from '../models/Video';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class VideoService {
  disLikeVideo(videoId:number): Observable<VideoReaction> {
    return this.http.post<VideoReaction>(environment.baseUrl + "/video/disLikeVideo/"+videoId,{});
  }
  likeVideo(videoId:number):Observable<VideoReaction> {
    return this.http.post<VideoReaction>(environment.baseUrl + "/video/likeVideo/"+videoId,{});
  }
  getVideoReaction(videoId:number): Observable<VideoReaction>{
  return this.http.get<VideoReaction>(environment.baseUrl + "/video/getVideoReactionCount/"+videoId);
  }
  saveDetails(metaData:Video):Observable<Video> {
    return this.http.put<Video>(environment.baseUrl + "/video/videoDetails",metaData);
  }
  uploadThumbnail(file :File,videoId:number):Observable<any> {
    const formData = new FormData();
    formData.append("file",file);
    formData.append("videoId",videoId.toString());
    return this.http.post(environment.baseUrl + "/video/uploadThumbnail",formData,{responseType:'text'});
  }

  constructor(private http:HttpClient) { }

  uploadVideo(file:File):Observable<Video>{
    const formData = new FormData();
    formData.append("file",file)
    return this.http.post<Video>(environment.baseUrl + "/video/uploadVideo",formData);
  }

  getVideo(videoId:number):Observable<Video>{
    return this.http.get<Video>(environment.baseUrl + "/video/getVideo/"+videoId);
  }

  getAllVideos():Observable<Array<Video>>{
    return this.http.get<Array<Video>>(environment.baseUrl + "/video/getAllVideos");
  }
}
