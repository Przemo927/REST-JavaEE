import {EventPosition} from './eventposition';
import { User } from './user';

export class Event {
id: number;
nameOfCity: string;
timestamp: Date;
user: User;
eventPosition: Array<EventPosition>
}