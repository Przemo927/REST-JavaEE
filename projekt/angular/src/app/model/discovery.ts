import { User } from './user';
import {Comment} from "./comment";

export class Discovery {
  id: number;
  name: string;
  url: string;
  description: string;
  timestamp: Date;
  user: User;
  comments:Comment[];
}
