import {Component, OnInit} from "@angular/core";
import {ContentPdfService} from "../service/contentpdf.service";
import {DraggableService} from "../service/draggable.service";

@Component({
  selector: 'app-pdfmodule',
  templateUrl: './pdfmodule.component.html',
  styleUrls: ['./pdfmodule.component.css']
})
export class PdfmoduleComponent implements OnInit {

  private contentDiv;
  private draggaleElement;
  private para:any;

  constructor(private contentService:ContentPdfService) { }

  ngOnInit() {
    this.contentDiv=document.getElementById("pdfModule");
    //clear content of div
    this.contentDiv.innerHTML="";
    this.loadDataToDisplay();
    this.serveDraggableElement();
    this.serveSplitElementFromParent();
    this.para = document.createElement("p");
    this.para.innerText='Przemek';
    this.para.style.position='absolute';
    this.para.style.display='none';
    this.contentDiv.appendChild(this.para);
  }

  private loadDataToDisplay(){
    if(this.contentService.currentData!==undefined){
      this.contentService.currentData.subscribe(content=>{
        content=PdfmoduleComponent.deleteHrefs(content);
        this.contentDiv.innerHTML=content;
      });
    }
  }

  private static deleteHrefs(contentPage):string{
    let hrefRegexp=new RegExp('href=".*?"');
    let match;
    while(match = hrefRegexp.exec(contentPage)){
      contentPage=contentPage.replace(match[0],'');
    }
    return contentPage;
  }

  private serveDraggableElement(){
    this.contentDiv.addEventListener('mousedown', (e)=> {
      this.startDraggale(e);
    });
    this.contentDiv.addEventListener('mouseup', ()=>{
      this.suspendDraggable();
    });

    this.contentDiv.addEventListener('mousemove', (e)=>{
      if(e.ctrlKey){
        e.target.style.border='1px solid black';
      }
    });
    this.contentDiv.addEventListener('mouseout', (e)=>{
      e.target.style.border='';
    });

  }

  private serveSplitElementFromParent(){
    this.contentDiv.addEventListener('contextmenu', (e)=>{
      if(e.ctrlKey){
        this.splitElementFromParentElement(e);
      }
    });
    this.contentDiv.addEventListener('mousemove', (e)=>{
      if(e.shiftKey){
        e.target.style.border='orange 5px solid';
        this.para.style.top=e.clientY;
        this.para.style.left=e.clientX;
        this.para.style.pointerEvents='none';
        this.para.style.display='';
      }
    });
  }
  private startDraggale(e:MouseEvent){
    if(e.ctrlKey){
      this.draggaleElement = DraggableService.prepareDraggableElement(this.contentDiv, e.target);
      this.draggaleElement.setInitialPosition(e.clientX,e.clientY);
    }
  }
  private suspendDraggable(){
    if(this.draggaleElement !== undefined){
      this.draggaleElement.suspendDraggable();
    }
  }
  private splitElementFromParentElement(e:MouseEvent){
    let mainDiv = document.getElementById("main");
    let target = <HTMLElement>e.target;
    if(target.parentElement.id!=="main"){
      let position=target.getBoundingClientRect();
      target.style.position='absolute';
      target.style.top=position.top.toString();
      target.style.left=position.left.toString();
      target.style.right=position.right.toString();
      mainDiv.appendChild(target);
    }
  }

}

