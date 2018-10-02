
export class DraggableElement{

  private active = false;
  private draggable = false;
  private currentX;
  private currentY;
  private initialX;
  private initialY;
  private xOffset = 0;
  private yOffset = 0;
  private draggableElement;
  private container;
  private emptyString = "";

  public prepareDraggableElement(container:any,element:any){
    element.addEventListener("mouseup", this.dragEnd, false);
    element.addEventListener("mousemove", this.drag, false);

    element.addEventListener("touchend", this.dragEnd, false);
    element.addEventListener("touchmove", this.drag, false);

    this.draggableElement=element;
    this.prepareContainerToDraggable(container);
    this.draggable=true;
    this.addBorderToElement();
  }

  public suspendDraggable(){
    this.draggableElement.removeEventListener("mouseup", this.dragEnd, false);
    this.draggableElement.removeEventListener("mousemove", this.drag, false);

    this.draggableElement.removeEventListener("touchend", this.dragEnd, false);
    this.draggableElement.removeEventListener("touchmove", this.drag, false);

    this.container.removeEventListener("touchmove", this.drag, false);
    this.container.removeEventListener("mousemove", this.drag, false);

    this.draggable=false;
    this.removeBorderFromElement();
  }

  private addBorderToElement(){
    this.draggableElement.style.border="1px black solid";
  }

  private removeBorderFromElement(){
    this.draggableElement.style.border="";
  }

  private prepareContainerToDraggable(container:any){
    container.addEventListener("touchmove", this.drag, false);
    container.addEventListener("mousemove", this.drag, false);
    this.container=container;
  }

  private dragEnd=(e)=> {
    this.initialX = this.currentX;
    this.initialY = this.currentY;
    this.active = false;
  };

  private drag=(e)=> {
    if (this.active) {
      e.preventDefault();

      if (e.type === "touchmove") {
        this.currentX = e.touches[0].clientX - this.initialX;
        this.currentY = e.touches[0].clientY - this.initialY;
      } else {
        this.currentX = e.clientX - this.initialX;
        this.currentY = e.clientY - this.initialY;
      }

      this.xOffset = this.currentX;
      this.yOffset = this.currentY;

      DraggableElement.setTranslate(this.currentX, this.currentY, this.draggableElement);
    }
  };

  static setTranslate(xPos, yPos, el) {
    el.style.transform = "translate3d(" + xPos + "px, " + yPos + "px, 0)";
  }

  setInitialPosition(clientX,clientY){
    let transform=this.draggableElement.style.transform;
    if(transform!==this.emptyString){
      let translateValues=this.getTranslateValues(transform);
      this.initialX = clientX - Number(translateValues[0]) - this.xOffset;
      this.initialY = clientY - Number(translateValues[1]) - this.yOffset;
    }else {
      this.initialX = clientX - this.xOffset;
      this.initialY = clientY - this.yOffset;
    }
    this.active=true;
  }

  private getTranslateValues(transform:string):string[]{
    let regex=new RegExp("-*[0-9]+px, -*[0-9]+px");
    let translateformValues=transform.match(regex)[0].split(",");
    for(let i=0;i<translateformValues.length;i++) {
      translateformValues[i] = this.deleteWhiteSpaces(translateformValues[i]);
      translateformValues[i] = this.deletePx(translateformValues[i]);
    }

    return translateformValues;
  }

  private deleteWhiteSpaces(value:string):string{
    let whiteSpaceRegex=new RegExp("\\s+");
    while(value.match(whiteSpaceRegex)){
      value = value.replace(whiteSpaceRegex,this.emptyString);
    }
    return value;
  }

  private deletePx(value:string):string {
    while(value.includes("px")){
      value = value.replace("px",this.emptyString);
    }
    return value;
  }

}
