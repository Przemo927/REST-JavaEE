
export class DraggableElement{

  private active = false;
  private currentX;
  private currentY;
  private initialX;
  private initialY;
  private xOffset = 0;
  private yOffset = 0;
  private draggableElement;
  private container;

  public prepareDraggableElement(container:any,element:any){
    element.addEventListener("mousedown", this.dragStart, false);
    element.addEventListener("mouseup", this.dragEnd, false);
    element.addEventListener("mousemove", this.drag, false);

    element.addEventListener("touchstart", this.dragStart, false);
    element.addEventListener("touchend", this.dragEnd, false);
    element.addEventListener("touchmove", this.drag, false);
    this.draggableElement=element;
    this.prepareContainerToDraggable(container);
  }

  private prepareContainerToDraggable(container:any){
    container.addEventListener("touchmove", this.drag, false);
    container.addEventListener("mousemove", this.drag, false);
    this.container=container;
  }

  private dragStart=(e)=> {
    if (e.type === "touchstart") {
      this.initialX = e.touches[0].clientX - this.xOffset;
      this.initialY = e.touches[0].clientY - this.yOffset;
    } else {
      this.initialX = e.clientX - this.xOffset;
      this.initialY = e.clientY - this.yOffset;
    }

    if (e.target.id === this.draggableElement.id) {
      this.active = true;
    }
  };

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

      this.setTranslate(this.currentX, this.currentY, this.draggableElement);
    }
  };

  setTranslate(xPos, yPos, el) {
    el.style.transform = "translate3d(" + xPos + "px, " + yPos + "px, 0)";
  }

}
