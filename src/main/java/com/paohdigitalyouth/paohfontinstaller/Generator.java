package com.paohdigitalyouth.paohfontinstaller;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;

import com.snatik.storage.Storage;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by HtetzNaing on 1/27/2018.
 */

public class Generator {
    Context context;
    String font,title,dev,screen,version,osversion;
    String workPath;
    String fontPath;
    Storage storage;
    String fontName = "DroidSansChinese.ttf";
    String fontPic = "pic_font_default.jpg";
    String cover = "cover.jpg";
    String pic1 = "pic_font_default.jpg";
    String pic2 = "preview_fonts_0.jpg";
    String pic3 = "preview_unlock_0.jpg";
    String dec = "description.xml";

    public Generator(Context context, String font, String title, String dev, String screen, String version, String osversion) {
        this.context = context;
        this.font=font;
        this.title = title;
        this.dev = dev;
        this.screen = screen;
        this.version = version;
        this.osversion = osversion;

        workPath = Environment.getExternalStorageDirectory()+"/Android/data/"+context.getPackageName()+"/build/";
        fontPath = Environment.getExternalStorageDirectory()+"/HWThemes/";
        storage = new Storage(context);
        createDir();
        assets2SD(context,"huawei/icon.png",workPath,"htetz.zip");
        unZipWithPass(workPath+"htetz.zip","<@FuckYou4/>",workPath);
        storage.deleteFile(workPath+"htetz.zip");
    }

    public String build(){
        String b = "";
        try {
            if (new File(font).exists()==true){
                storage.deleteFile(workPath+"fonts/"+fontName);
                storage.copy(font,workPath+"fonts/"+fontName);

                String lol = storage.readTextFile(workPath+dec);
                lol = lol.replace("Title",title);
                lol = lol.replace("KhunHtetzNaing",dev);
                lol = lol.replace("zScreen",screen);
                lol = lol.replace("zVersion",version);
                lol = lol.replace("zOSVersion",osversion);
                storage.deleteFile(workPath+dec);
                storage.createFile(workPath+dec,lol);

                Converter converter = new Converter();
                String text = title+"\nby Huawei Font Generator\n\nABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n1234567890!@#$%^&*()";
                Typeface typeface = Typeface.createFromFile(workPath+"fonts/"+fontName);
                Bitmap bitmap = converter.textAsBitmap(text,30,0, Color.BLACK, typeface);
                storage.deleteFile(workPath+"fonts/"+fontPic);
                converter.saveImage(bitmap,workPath+"fonts/"+fontPic);
                storage.deleteFile(workPath+"preview/"+cover);
                converter.saveImage(bitmap,workPath+"preview/"+cover);

                storage.deleteFile(workPath+"preview/"+pic1);
                converter.saveImage(bitmap,workPath+"preview/"+pic1);

                storage.deleteFile(workPath+"preview/"+pic2);
                converter.saveImage(bitmap,workPath+"preview/"+pic2);

                storage.deleteFile(workPath+"preview/"+pic3);
                converter.saveImage(bitmap,workPath+"preview/"+pic3);

                File file = new File(workPath);
                File[] file1  = file.listFiles();

                ArrayList<String> lll = new ArrayList<>();
                for (int i = 0; i<file1.length; i++){
                    lll.add(file1[i].toString());
                }
                storage.deleteFile(fontPath+title+".hwt");
                zipper(lll,fontPath+title+".hwt");
                storage.deleteDirectory(workPath);
                b = "Completed!\nNow you can change font ;)";
            }else{
                b = "Font file not found";
            }
        }catch (Exception e){
            b = "Error";
        }
        return b;
    }

    public boolean createDir() {
        boolean b = false;
        storage.createDirectory(workPath);
        storage.createDirectory(fontPath, false);
        if (storage.isDirectoryExists(fontPath) == true && storage.isDirectoryExists(workPath) == true) {
            b = true;
        }
        return b;
    }

    public boolean assets2SD(Context context, String inputFileName, String OutputDir, String OutputFileName) {
        boolean lol = false;
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            try {
                in = assetManager.open(inputFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = new FileOutputStream(OutputDir + OutputFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(OutputDir+OutputFileName);
        if (file.exists()!=false){
            lol=true;
        }else{
            lol=false;
        }
        return lol;
    }

    public boolean unZipWithPass(String zip, String pass, String output){
        File n = new File(output);
        if (!n.exists()){
            n.mkdir();
        }

        boolean lol = false;

        net.lingala.zip4j.core.ZipFile zipFile = null;
        try {
            zipFile = new net.lingala.zip4j.core.ZipFile(zip);
            if(zipFile.isEncrypted()){
                zipFile.setPassword(pass);
            }
            zipFile.extractAll(output);
            lol = true;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return lol;
    }

    public String zipper(ArrayList<String> allFiles, String zipFileName) throws IOException
    {
        String zippath = zipFileName;
        try
        {
            if (new File(zippath).exists())
            {
                new File(zippath).delete();
            }
            ZipFile zipFile = new ZipFile(zippath);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            if (allFiles.size() > 0){
                for (String fileName : allFiles){
                    File file = new File(fileName);
                    if (file.isDirectory()){
                        zipFile.addFolder(file,zipParameters);
                    }else{
                        zipFile.addFile(file, zipParameters);
                    }
                }
            }
        }
        catch (ZipException e){
            e.printStackTrace();
        }
        return zippath;
    }
}
