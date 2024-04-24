import { User } from "./User";

export interface Video{
      uploadDate?: Date;
      videoId: number,
      fileName?: string,
      title: string,
      description: string,
      videoUrl?: string,
      thumbnailUrl?: string,
      tags?: string,
      videoStatus: string,
      viewCount?:number,
      user?:User;
}