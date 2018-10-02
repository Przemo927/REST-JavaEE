import {Component, OnInit} from "@angular/core";
import {ContentPdfService} from "../contentpdf.service";
import {DraggableService} from "../draggable.service";

@Component({
  selector: 'app-pdfmodule',
  templateUrl: './pdfmodule.component.html',
  styleUrls: ['./pdfmodule.component.css']
})
export class PdfmoduleComponent implements OnInit {

  private contentDiv;
  private draggaleElement;

  constructor(private contentService:ContentPdfService) { }

  ngOnInit() {
    this.contentDiv=document.getElementById("pdfModule");
    //clear content of div
    this.contentDiv.innerHTML="";
    this.loadDataToDisplay();
    this.serveDraggableElement();
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
      if(e.ctrlKey){
        this.draggaleElement = DraggableService.prepareDraggableElement(this.contentDiv, e.target);
        this.draggaleElement.setInitialPosition(e.clientX,e.clientY);
      }
    });
    this.contentDiv.addEventListener('mouseup', ()=>{
      if(this.draggaleElement!==undefined){
        this.draggaleElement.suspendDraggable();
      }
    });
  }
}
