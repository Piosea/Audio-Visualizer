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
  void display() {

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