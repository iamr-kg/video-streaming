import { User } from './../models/User';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Video } from '../models/Video';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  user!:User;
  
  public getUser() : User {
    return this.user;
  }
  
  userSubscriptions!:User[]
  constructor(private http:HttpClient) { }

  registerUserInDb(){
    environment.baseUrl
    this.http.get<User>(environment.baseUrl + "/users/register").subscribe(
      (data)=> {
        this.user = data;
        sessionStorage.setItem("userId",data.id.toString()) ;
        sessionStorage.setItem("userPicture",data.picture) ;
        sessionStorage.setItem("userName",data.fullName)
      });
    }

  subscribeToUser(userId:number):Observable<any>{
    return this.http.post(environment.baseUrl + "/users/subscribeTo/"+userId,{});
  }

  unSubscribeToUser(userId:number):Observable<any>{
    return this.http.post(environment.baseUrl + "/users/unSubscribeTo/"+userId,{});
  }

  isSubscribedToUser(userId:number):Observable<any>{
    return this.http.get(environment.baseUrl + "/users/isSubscribedToUser/"+userId,{},
   )
  }

  getHistory():Observable<Array<Video>>{
    return this.http.get<Array<Video>>(environment.baseUrl + "/users/userHistory");
  }
  
  getLikedVideos():Observable<Array<Video>>{
    return this.http.get<Array<Video>>(environment.baseUrl + "/users/likedVideos");
  }
}

