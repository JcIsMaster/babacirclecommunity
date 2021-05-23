package com.example.babacirclecommunity.common.utils;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author MQ
 * @date 2021/1/20 14:28
 */
@Component
public class Upload {

    public static int getRandomInt(int Min , int Max){
        Random rand = new Random();
        return rand.nextInt(Max - Min + 1) + Min;
    }

    Path path=null;
    public synchronized List<String> upload(MultipartFile files) throws Exception {

        //得到存入数据库的token
        String token = ConstantUtil.getToken();
        String checkImg = ConstantUtil.checkImg(files, token);
        if(checkImg.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"文件违规!");
        }

        String modeFiles=null;
            String modeFile=null;
            String times="";
        List<String> psList=new ArrayList<>();
            try {
                String fileName = files.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String time=System.currentTimeMillis()/1000+""+getRandomInt(10000, 99999);
                times=time+"."+suffixName;
                byte[] bytes=files.getBytes();
                String visbit="img";
                if(suffixName.equals("AVI")||suffixName.equals("mov")||suffixName.equals("rmvb")||suffixName.equals("FLV")||suffixName.equals("mp4")||suffixName.equals("3GP")){
                    visbit="video";
                }

                path = Paths.get("e:/file/"+visbit+"/"+ times);
                Files.write(path,bytes);

                 modeFiles=path.toString();
                if (visbit.equals("img")) {
                    //在拿到压缩后的图片  处理后 返回给小程序
                    String mode = modeFiles.replace("file\\", "");
                    String  modes = mode.replace("\\", "/");
                    //FfmpegUtil.getCompressImg(modeFile, modes);
                    modeFiles = modes.replace("e:", "https://www.gofatoo.com");
                    modeFiles=modeFiles.replace("file/", "");
                    psList.add(modeFiles);
                }else{
                    //得到视屏封面
                    String videoCover = FfmpegUtil.getVideoCover(path.toString());
                    String mode = modeFiles.replace("file\\", "");
                    String modes = mode.replace("\\", "/");
                    modeFile = modes.replace("e:", "https://www.gofatoo.com");
                    psList.add(modeFile);
                    psList.add(videoCover);
                }

            }

            catch(IOException e){

            }
        return psList;
    }

    public synchronized List<String> uploads(MultipartFile[]  files) throws Exception {

            List<String> psList=new ArrayList<>();

            String modeFile=null;

            String times="";
           for (int i = 0; i < files.length; i++) {
               if (Objects.isNull(files[i]) || files[i].isEmpty()) {
                   throw new ApplicationException(CodeType.SERVICE_ERROR, "文件为空，请重新上传");
               }
               try {
                   String fileName = files[i].getOriginalFilename();
                   String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
                   String time = System.currentTimeMillis() / 1000 + "" + getRandomInt(10000, 99999);
                   times = time + "." + suffixName;
                   byte[] bytes = files[i].getBytes();
                   String visbit = "img";
                   if (suffixName.equals("AVI") || suffixName.equals("mov") || suffixName.equals("rmvb") || suffixName.equals("FLV") || suffixName.equals("mp4") || suffixName.equals("3GP")) {
                       visbit = "video";
                   }

                   path = Paths.get("e:/file/" + visbit + "/" + times);
                   Files.write(path, bytes);

                   String modeFiles = path.toString();

                   if (visbit.equals("img")) {
                       String mode = modeFiles.replace("file\\", "");
                       String modes = mode.replace("\\", "/");
                       //FfmpegUtil.getCompressImg(modeFile, modes);
                       modeFiles = modes.replace("e:", "https://www.gofatoo.com");
                       modeFile = modeFiles.replace("file/", "");
                       psList.add(modeFile);
                   } else {
                       //得到视屏封面
                       String videoCover = FfmpegUtil.getVideoCover(path.toString());
                       String mode = modeFiles.replace("file\\", "");
                       String modes = mode.replace("\\", "/");
                       modeFile = modes.replace("e:", "https://www.gofatoo.com");
                       psList.add(modeFile);
                       psList.add(videoCover);
                   }
               } catch (IOException e) {
                   throw new ApplicationException(CodeType.SERVICE_ERROR);
               }
           }
        return psList;



    }
}
