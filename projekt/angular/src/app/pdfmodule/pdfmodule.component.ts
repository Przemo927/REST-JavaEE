import {Component, OnDestroy, OnInit} from "@angular/core";
import {ContentPdfService} from "../contentpdf.service";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-pdfmodule',
  templateUrl: './pdfmodule.component.html',
  styleUrls: ['./pdfmodule.component.css']
})
export class PdfmoduleComponent implements OnInit, OnDestroy {

  constructor(private contentService:ContentPdfService) { }

  ngOnInit() {
    let contentDiv=document.getElementById("pdfModule");
    //clear content of div
    contentDiv.innerHTML="";

    if(this.contentService.currentData!==undefined){
      this.contentService.currentData.subscribe(content=>{
        content=this.deleteHrefs(content);
        contentDiv.innerHTML=content;
      });
    }

    contentDiv.addEventListener('click', function(e) {
      if(e.ctrlKey){
        e.target;
      }
    });
  }
  ngOnDestroy(){

  }

  private deleteHrefs(contentPage):string{
    let hrefRegexp=new RegExp('href=".*?"');
    let match;
    while(match = hrefRegexp.exec(contentPage)){
      contentPage=contentPage.replace(match[0],'');
    }
    return contentPage;
  }
}
