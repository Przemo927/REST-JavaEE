import {Injectable} from "@angular/core";
import {DraggableElement} from "./draggableelement";

@Injectable()
export class DraggableService {

  constructor() {
  }
  public static prepareDraggableElement(container:any,element:any):DraggableElement{
    let draggableElement=new DraggableElement();
    draggableElement.prepareDraggableElement(container,element);
    return draggableElement;
  }

}
