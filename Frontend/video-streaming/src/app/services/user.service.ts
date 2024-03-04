import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userId!:number;
  constructor(private http:HttpClient) { }

  registerUserInDb(){
    this.http.get("http://localhost:8080/vsa/users/register",{responseType:"text"}).subscribe((data)=> this.userId = Number(data));
  }
}

