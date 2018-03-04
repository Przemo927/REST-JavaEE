import { User } from './user';
import { Discovery } from './discovery';

export class Comment {
id: number;
upVote: number;
downVote: number;
comment: string;
user: User;
discovery: Discovery;

constructor() {}
}