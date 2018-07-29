import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-validatableinput',
  templateUrl: './validatableinput.component.html',
  styleUrls: ['./validatableinput.component.css']
})
export class ValidatableinputComponent implements OnInit, AfterViewInit {

  private input:any;
  @Input() placeholder:string;
  @Input() positionMessage:string;
  @Input() type:string;
  @Input() idContainer:string;
  private value:string;
  private message:string;
  @Output() changeEvent: EventEmitter<string> = new EventEmitter<string>();
  constructor() { }

  ngAfterViewInit(){
    let container=document.getElementById(this.idContainer);
    this.input=container.getElementsByTagName("input")[0];
    if(this.positionMessage==="above"){
      container.style.flexDirection='column-reverse';
    }else {
      container.style.flexDirection='column';
    }
    container.style.display='flex';
  }
  ngOnInit() {

  }
  emit(){
    this.changeEvent.emit(this.value);
  }
  setWrong(){
    this.input.classList.remove("highlight");
    void this.input.offsetWidth;
    this.input.classList.add("highlight");
  }
  setValidationMessage(message:string){
    this.message=message;
  }
  hideError(){
    this.input.style.animation='empty';
    this.message=undefined;
  }

}
