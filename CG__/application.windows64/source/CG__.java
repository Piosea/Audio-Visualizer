import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import javax.swing.*; 
import java.awt.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CG__ extends PApplet {













Minim minim; 
AudioPlayer player;   
AudioMetaData meta;
FFT fft;

button loadbutton;
sphereart[] spheres;

float rotation=0;
int changered=0;
boolean changeredflag=true;
int changeblue=150;
boolean changeblueflag=true;

float position=0;

String getFile=null;
PFont font;


public void setup()
{
  

  // 加算合成モード

  minim=new Minim(this);

  font = createFont("Yu Gothic", 24, true); //フォントを指定
  textFont(font);

  loadbutton=new button("ファイルを選択", 100, height-50, 200, 50);
  spheres=new sphereart[16];
}

public void draw() {
  background(0);
  blendMode(ADD);

  if (getFile !=null) {
    fileLoader();
  }

  if (fft!=null) {
    pushMatrix();
    translate(width/2, height/2, 0);  //基準点を画面中央。z軸方向には-100
    perspective();
    rotation+=0.01f;
    camera(cos(rotation)*1300, -740, sin(rotation)*1300, 500, 0, 500, 0, 1, 0);
    // camera(500, -2478, 499, 500, 0, 500, 0, 1, 0);
    fft.forward(player.mix);//高速フーリエ変換の実行

    for (int i = 0; i < player.bufferSize()-1; i++)
    {

      float h = map(i, 0, fft.specSize(), 70, 255);
      pushMatrix();
      translate(i*3+1000, player.mix.get(i)*500+400, (int)random(-5000, 5000));
      stroke(170, h, 150);
      fill(170, h, 150);
      box(4);
      popMatrix();

      pushMatrix();
      translate(i*3-1500, player.mix.get(i)*500+400, (int)random(-5000, 5000));
      stroke(170, h, 151);
      fill(170, h, 150);
      box(4);
      popMatrix();

      stroke(changered, h, changeblue);
      for (int j=0; j<100; j++) {

        line( i*2, height, j*10, i*2, height - fft.getBand(i)*4-100, j*10);//スペクトルの表示
      }
    }
    for (int j=0; j<7; j++) {
      beginShape(QUADS);
      for (int i = 0; i < player.bufferSize()-1; i++)
      {
        float h = map(i, 0, fft.specSize(), 0, 180);
        fill(changered, h, changeblue);
        stroke(changered, h, changeblue);
        vertex(i*2, player.mix.get(i)*300+100, -3000+1000*j);
      }
      endShape();
    }
    for (int j=0; j<7; j++) {
      beginShape(QUADS);
      for (int i = 0; i < player.bufferSize()-1; i++)
      {
        float h = map(i, 0, fft.specSize(), 0, 180);
        fill(250, h, 150);
        stroke(changeblue, h, changered);
        vertex(i*2, player.mix.get(i)*300+100, -2500+1000*j);
      }
      endShape();
    }


    spheres[0]=new sphereart(-200, 0, -200, 26, 27, 28);
    spheres[1]=new sphereart(-500, 0, -500, 26, 27, 28);
    spheres[2]=new sphereart(-800, 0, -800, 26, 27, 28);
    spheres[3]=new sphereart(-1100, 0, -1100, 26, 27, 28);
    spheres[4]=new sphereart(-200, 0, 1200, 26, 27, 28);
    spheres[5]=new sphereart(-500, 0, 1500, 26, 27, 28);
    spheres[6]=new sphereart(-800, 0, 1800, 26, 27, 28);
    spheres[7]=new sphereart(-1100, 0, 2100, 26, 27, 28);
    spheres[8]=new sphereart(1200, 0, -200, 26, 27, 28);
    spheres[9]=new sphereart(1500, 0, -500, 26, 27, 28);
    spheres[10]=new sphereart(1800, 0, -800, 26, 27, 28);
    spheres[11]=new sphereart(2100, 0, -1100, 26, 27, 28);
    spheres[12]=new sphereart(1200, 0, 1200, 26, 27, 28);
    spheres[13]=new sphereart(1500, 0, 1500, 26, 27, 28);
    spheres[14]=new sphereart(1800, 0, 1800, 26, 27, 28);
    spheres[15]=new sphereart(2100, 0, 2100, 26, 27, 28);
    for (int i=0; i<16; i++) {
      spheres[i].display();
    }
    popMatrix();
  }
  menudisplay();

  if (changeredflag) {
    changered++;
  } else {
    changered--;
  }
  if (changered<=0||changered>=255) {
    changeredflag=!changeredflag;
  }
  if (changeblueflag) {
    changeblue++;
  } else {
    changeblue--;
  }
  if (changeblue<=0||changeblue>=255) {
    changeblueflag=!changeblueflag;
  }

}

public void mousePressed() {
  if (loadbutton.press()) {
    getFile =getFileName();
  }
    if(mouseX>=width/2+20&&mouseY>=height-50&&fft!=null){
    fill(255,0,0,50);
    ellipse(mouseX,height-30,30,30);
  }
}
public void mouseDragged(){
  if(mouseX>=width/2+20&&mouseY>=height-50&&fft!=null){
    fill(255,0,0,50);
    ellipse(mouseX,height-30,30,30);
  }
}
public void mouseReleased(){
  if(mouseX>=width/2+20&&mouseY>=height-50&&fft!=null){
    player.rewind( ) ;
  float cueposition=map(mouseX,width/2+20,width-20,0 ,player.length());
  player.cue((int)cueposition) ;
  
  }
}


public void menudisplay() {
  ortho();
  blendMode(BLEND);
  pushMatrix();

  fill(255, 150);
  stroke(0);
  rectMode(CENTER);
  rect(width/2, height-50, width, 100); 
  line(width/2, height-100, width/2, height);


  loadbutton.display();
  if (meta!=null) {
    textAlign( LEFT );
    text("Title: " + meta.title(), 230, height-60);
    text("Artist: " + meta.author(), 230, height-20);

    text(computeDuration(player.position())+"/"+computeDuration(player.length()), 607, height-60);
    line(width/2+20, height-30, width-20, height-30);
    fill(255, 0, 0);
    position=map(player.position(), 0, player.length(), width/2+20, width-20);
    ellipse(position, height-30, 30, 30);
  }


  popMatrix();
}

public String computeDuration(int ms) {
  int  in,h, m, s;
  in=ms/1000;
  h=in/3600;
  in %=3600;
  m=in/60;
  in%=60;
  s=in;
 if(s<10){
   return(m+":0"+s);
 }else{
  return(m+":"+s);
 }
}


public void fileLoader() { 
  String ext = getFile.substring(getFile.lastIndexOf('.') + 1); 
  ext.toLowerCase();
  //選択したファイルが「wav」フォーマットなら 
  if (ext.equals("wav")||ext.equals("mp3")) {

    if (player!=null) {
      player.pause();
    }
    player = minim.loadFile(getFile, 512);//音源ファイルのロード
    meta = player.getMetaData();
    fft = new FFT(player.bufferSize(), player.sampleRate()); 
    player.play();//音源ファイルの再生

  } 
  getFile = null;
}

//ファイル選択画面、選択ファイルパス取得の処理
public String getFileName() { 
  SwingUtilities.invokeLater(new Runnable() { 
    public void run() { 
      try { 
        JFileChooser fc = new JFileChooser(); 
        int returnVal = fc.showOpenDialog(null); 
        if (returnVal == JFileChooser.APPROVE_OPTION) { 
          File file = fc.getSelectedFile(); 
          getFile = file.getPath();
        }
      }
      catch (Exception e) { 
        e.printStackTrace();
      }
    }
  } 
  ); 
  return getFile;
}

//プログラム終了時に呼び出されます
public void stop()
{
  player.close();
  minim.stop();
  super.stop();
}
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
  public void display() {
    
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
  public boolean bounds(){
    return (mouseX >= x - w / 2 && mouseX < x + w / 2 && mouseY >= y - h / 2 && mouseY < y + h / 2);
  }
  
  public boolean press(){
    return(mousePressed && this.bounds());
  }
}
class sphereart{
int x, y, z, a, b, c;

 sphereart(int x, int y, int z, int a, int b, int c) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.a = a;
    this.b = b;
    this.c = c;
 }
  public void display() {

    pushMatrix();
    translate(x, y, z);
    int r=(int)random(175, 255);
    int g=(int)random(176, 255);
    int b=(int)random(176, 255);
    fill(r, g, b, 100);
    noStroke();
    sphere(fft.getBand(a)*10);
    sphere(fft.getBand(b)*10);
    sphere(fft.getBand(c)*10);
    popMatrix();
  }



}
  public void settings() {  size(1200, 800, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CG__" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
