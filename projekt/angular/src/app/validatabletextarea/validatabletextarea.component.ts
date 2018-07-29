import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from "@angular/core";

@Component({
  selector: 'app-validatabletextarea',
  templateUrl: './validatabletextarea.component.html',
  styleUrls: ['./validatabletextarea.component.css']
})
export class ValidatabletextareaComponent implements OnInit, AfterViewInit {

  @Input() placeholder:string;
  @Input() idContainer:string;
  @Input() rows:number;
  @Input() positionMessage:string;
  private textArea;
  private message:string;
  private value:string;
  @Output() changeEvent: EventEmitter<string> = new EventEmitter<string>();
  constructor() {
  }
  ngAfterViewInit(){
    let container=document.getElementById(this.idContainer);
    this.textArea=container.getElementsByTagName("textArea")[0];
    if(this.positionMessage==="above"){
      container.setAttribute("style","flex-direction: column-reverse");
    }else{
      container.setAttribute("style","flex-direction: column");
    }
    container.style.display='flex';
  }
  ngOnInit() {
  }

 setWrong(){
    this.textArea.classList.remove("highlight");
   //need to reload keyframes
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
