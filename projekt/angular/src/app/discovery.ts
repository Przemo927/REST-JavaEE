import { User } from './user';

export class Discovery {
  id: number;
  name: string;
  url: string;
  description: string;
  timestamp: Date;
  user: User;
}