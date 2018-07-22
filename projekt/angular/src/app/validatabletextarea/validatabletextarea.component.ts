import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-validatabletextarea',
  templateUrl: './validatabletextarea.component.html',
  styleUrls: ['./validatabletextarea.component.css']
})
export class ValidatabletextareaComponent implements OnInit {

  @Input() placeholder:string;
  @Input() rows:number;
  @Input() positionMessage:string;
  private textArea;
  private message:string;
  private value:string;
  @Output() changeEvent: EventEmitter<string> = new EventEmitter<string>();
  constructor() {
  }

  ngOnInit() {
    this.textArea=document.getElementById("textArea");
    if(this.positionMessage==="above"){
      document.getElementById("container").style.flexDirection='column-reverse';
    }
  }

 setWrong(){
    this.textArea.classList.remove("highlight");
    void this.textArea.offsetWidth;
    this.textArea.classList.add("highlight");
 }
 setValidationMessage(message:string){
   this.message=message;
 }
 emit(){
   this.changeEvent.emit(this.value);
 }

}
