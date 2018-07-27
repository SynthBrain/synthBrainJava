package core.Base;

import com.badlogic.gdx.graphics.Color;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;



public class VisionCortex {

    protected int width;    //стартовые размеры массива
    protected int height;

    public float[][] layer_0;
    public int[][] argb;
    public Color[][] layer_RGB;
    private BufferedImage img;
    private java.awt.Color pixelColor;

    private Webcam webcam;
    private Dimension size;

    public VisionCortex(){
        webcam = Webcam.getDefault();
        Dimension size = new Dimension();
        size.height = 144; //120
        size.width = 176; //160
        //webcam.setViewSize(size);

        webCam();
        inputImage("InputVision.jpg");
        width = img.getWidth();
        height = img.getHeight();
        argb = new int[width][height];
        layer_0 = new float[width][height];
        layer_RGB = new Color[width][height];

        for (int i = 0; i < layer_RGB.length; i++){
            for (int j = 0; j < layer_RGB[i].length; j++){
                layer_RGB[i][j] = new Color();
            }
        }
        image_in_charge();
    }


    public void webCam(){
        //Webcam webcam = Webcam.getDefault();

        webcam.open();
        try {
            ImageIO.write(webcam.getImage(), "JPG", new File("InputVision.jpg"));
            //webcam.close();
            //System.out.println("File create");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRROR");
        }

        //**********************************************************
        /*
        Dimension size = new Dimension();
		size.height = 480;
		size.width = 640;

		// get default webcam and open it
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(size);


		webcam.open();
		// get image
		BufferedImage image = webcam.getImage();

		// save image to PNG file
		ImageIO.write(image, "PNG", new File("test.png"));
		webcam.close();


        Webcam webcam = Webcam.getDefault();
        webcam.open();

        // get image
        BufferedImage image = webcam.getImage();

        // save image to PNG file
        try {
            ImageIO.write(image, "PNG", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public void inputImage(String path){
        try {
            //загружаем кадр полученный с сенсоров
            img = ImageIO.read(new File(path));
            //размер массива в байтах
            //byte[] imageBytes = ((DataBufferByte) img.getData().getDataBuffer()).getData();
            //System.out.println("data length " + imageBytes.length);

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
            e.printStackTrace();
        }
    }

    public void image_in_charge() {
        for (int i = 0; i < argb.length; i++) {
            for (int j = 0; j < argb[i].length; j++) {
                argb[i][j] = img.getRGB(i, j);

                pixelColor = new java.awt.Color(img.getRGB(i,j));

                float red = (pixelColor.getRed() / 255f);
                float green = (pixelColor.getGreen() / 255f);
                float blue = (pixelColor.getBlue() / 255f);
                float alpha = (pixelColor.getAlpha() / 255f);
                //System.out.println(red+" "+green+" "+blue+" "+alpha);

                layer_RGB[i][j].set(red,green,blue,alpha);

                layer_0[i][j] = (float) Math.round(((red + green + blue) / 3) * 100);// / 2.55);
                //layer_0[i][j] = ((red + green + blue) / 3) * 100;// / 2.55);

                // Абсолютный чёрный исключаем и заменяем более адекватным уровнем чёрного

                /*
                if (layer_0[i][j] < 0.0010) {
                    layer_0[i][j] = (float) 0.0010;
                }*/
                //System.out.print(" . " + layer_0[i][j]);
            }
            //System.out.println();
        }
        //System.out.println("Изображение усвоено");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

 /*
                int alpha = (argb[i][j] >> 24) & 0xff;
                int red = (argb[i][j] >> 16) & 0xff;
                int green = (argb[i][j] >> 8) & 0xff;
                int blue = (argb[i][j]) & 0xff;

                int red1 = (argb[i][j] >> 24) & 0xff;
                int green1 = (argb[i][j] >> 16) & 0xff;
                int blue1 = (argb[i][j] >> 8) & 0xff;
                int alpha1 =(argb[i][j]) & 0xff;

                float r = (((argb[i][j] & 0xff000000) >>> 24) / 255f);
                float g = (((argb[i][j] & 0x00ff0000) >>> 16) / 255f);
                float b = ((argb[i][j] & 0x0000ff00) >>> 8) / 255f;
                float a = ((argb[i][j] & 0x000000ff)) / 255f;

                int a1 = (argb[i][j] & 0xff000000)>>24;
                int r1 = (argb[i][j] & 0xff0000)>>16;
                int g1 = (argb[i][j] & 0xff00)>>8;
                int b1 = (argb[i][j] & 0xff);*/