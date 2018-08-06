class button {
  int x;
  int y;
  int w;
  int h;
  String str;
  button( String _str,int _x, int _y, int _w, int _h) {
    x=_x;
    y=_y;
    w=_w;
    h=_h;
    str=_str;
  }
  void display() {
    
    fill(255);
    if(bounds()){
      stroke(255,255,0);
      fill(20,20);
    }else{
      stroke(0);
    }
    rectMode(CENTER);
    rect(x, y, w, h);
    textAlign( CENTER );
    textSize(h/2);
    fill(0);
    text(str, x, y+10);
  }
  boolean bounds(){
    return (mouseX >= x - w / 2 && mouseX < x + w / 2 && mouseY >= y - h / 2 && mouseY < y + h / 2);
  }
  
  boolean press(){
    return(mousePressed && this.bounds());
  }
}