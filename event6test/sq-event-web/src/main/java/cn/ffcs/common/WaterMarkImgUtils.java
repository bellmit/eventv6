package cn.ffcs.common;

import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.springframework.ui.ModelMap;
import org.springframework.util.Base64Utils;

import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.uam.bo.UserInfo;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class WaterMarkImgUtils {
    //定义图片水印字体类型
    private static final String FONT_NAME = "微软雅黑";
    //定义图片水印字体加粗、变细、倾斜等样式
    private static final int FONT_STYLE = Font.BOLD;
    //设置字体大小
    private static final int FONT_SIZE = 20;
    //设置文字透明程度
    private static float ALPHA = 0.3F;
    //设置文字水印的水平及垂直间距
    private static final int MARGIN_X = 150,MARGIN_Y = 150;
    
    public static Integer IMG_WIDTH=0;
    
    public static Integer IMG_HEIGHT=0;
 
    /**
     * 给图片添加多个文字水印、可设置水印文字旋转角度
     * base64 
     * imgType 图片类型
     * color 水印文字的颜色
     * word 水印文字
     * degree 水印文字旋转角度，为null表示不旋转
     */
    public static byte[] markImageToTextBybase64(String base64,Color color, String word, Integer degree) {
    	 try {
             //Base64解码
             String[] arr= base64.split(",");//去除头部信息
             byte[] bytes = Base64Utils.decodeFromString(arr.length > 1 ? arr[1] : arr[0]);
             BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes,0,bytes.length)); 
             int width = image.getWidth(null);
             int height = image.getHeight(null);
             IMG_WIDTH = width;
             IMG_HEIGHT = height;
             BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
             Graphics2D graphics2D = bufferedImage.createGraphics();//创建绘图工具对象

             graphics2D.drawImage(image, 0, 0, width, height, null);//其中的0代表和原图位置一样
             graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));//设置水印文字（设置水印字体样式、粗细、大小）
             graphics2D.setColor(color);//设置水印颜色
             graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));//设置水印透明度
             //设置水印旋转(偷换概率其实旋转的图片)
             if (null != degree) {
                 graphics2D.rotate(Math.toRadians(degree),  bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
             }

             int wordWidth = FONT_SIZE * word.length(),wordHeight = FONT_SIZE;
             int x = -width/2,y = -height/2;
             //*1.5 为了水印能全面覆盖
             while (x < width * 1.5){
                 y = -height/2;
                 while (y < height * 1.5){
                     graphics2D.drawString(word,x,y);
                     y += wordHeight + MARGIN_Y;
                 }
                 x += wordWidth + MARGIN_X;
             }
             graphics2D.dispose();
             //输出图片
             ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ImageIO.write(bufferedImage, "jpg", byteOut);
             return byteOut.toByteArray();
         } catch (Exception e) {
             e.printStackTrace();
         }

         return null;
    }
    
    /**
     * 给图片添加多个文字水印、可设置水印文字旋转角度
     * source 需要添加水印的图片路径
     * outPut 添加水印后图片输出路径
     * imgName 图片名称
     * imgType 图片类型
     * color 水印文字的颜色
     * word 水印文字
     * degree 水印文字旋转角度，为null表示不旋转
     */
    public static String markImageByMoreText(String source,Color color, String word, Integer degree) {
        try {
            //读取原图片信息
            File file = new File(source);
            if (!file.isFile()) {
                return null;
            }
            //获取源图像的宽度、高度
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            String watermarkDirPath =// ConstantValue.TRANSCODE_ROOT_PATH +
            		"/mnt/watermark";
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();//创建绘图工具对象

            graphics2D.drawImage(image, 0, 0, width, height, null);//其中的0代表和原图位置一样
            graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));//设置水印文字（设置水印字体样式、粗细、大小）
            graphics2D.setColor(color);//设置水印颜色
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));//设置水印透明度
            //设置水印旋转(偷换概率其实旋转的图片)
            if (null != degree) {
                graphics2D.rotate(Math.toRadians(degree),  bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
            }

            int wordWidth = FONT_SIZE * word.length(),wordHeight = FONT_SIZE;
            int x = -width/2,y = -height/2;
            //*1.5 为了水印能全面覆盖
            while (x < width * 1.5){
                y = -height/2;
                while (y < height * 1.5){
                    graphics2D.drawString(word,x,y);
                    y += wordHeight + MARGIN_Y;
                }
                x += wordWidth + MARGIN_X;
            }
            graphics2D.dispose();
            //输出图片
            File sf = new File(watermarkDirPath, UUID.randomUUID().toString() + "." + "jpg");
            ImageIO.write(bufferedImage, "jpg", sf);

            return sf.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void waterMarkInfoToModel(HttpSession session, IFunConfigurationService funConfigurationService, ModelMap model){
        Object objUser = session.getAttribute(ConstantValue.USER_IN_SESSION);
        if (objUser == null || funConfigurationService == null || model == null){
            return;
        }
        UserInfo userInfo = (UserInfo) objUser;
        boolean needWaterMark = true;
        String currentValue = funConfigurationService.turnCodeToValue("WATER_MARK_FILTER", "JS_URL", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.CFG_ORG_TYPE_0);

        if(currentValue != null && currentValue.trim().length()>0){
            model.put("jsUrl",currentValue.replace("funConfigurationService.getAppDomain(\"$UI_DOMAIN\", null, null)",funConfigurationService.getAppDomain("$UI_DOMAIN", null, null)).trim());
        }else{
            needWaterMark = false;
        }

        model.put("watermarkContent",userInfo.getUserName());
        model.put("needWaterMark",needWaterMark);
    }

    public static String generateImage(String base64){
        if (StringUtils.isEmpty(base64)){
            return null;
        }
        String watermarkDirPath = //ConstantValue.TRANSCODE_ROOT_PATH +
        		"/mnt/watermark";//新生成的图片
        File watermarkDir = new File(watermarkDirPath);

        if(!watermarkDir.exists()){
            watermarkDir.mkdirs();
        }
        String imgPath = watermarkDirPath+"/"+ UUID.randomUUID().toString()+".jpg";
        try(OutputStream out = new FileOutputStream(imgPath);){
            //Base64解码
            String[] arr= base64.split(",");//去除头部信息
            byte[] b = Base64Utils.decodeFromString(arr.length > 1 ? arr[1] : arr[0]);
            //生成jpeg图片
            out.write(b);
            out.flush();

            return imgPath;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @description 生成水印图片
     * @param sourceImgPath    源图片路径
     * @param waterMarkContent 水印内容
     */
    public static BufferedImage addWatermark(BufferedImage sourceImgPath, String waterMarkContent) {
        // 水印字体，大小
        Font font = new Font("宋体", Font.BOLD, 22);
        // 水印颜色
        Color markContentColor = Color.blue;
        // 设置水印文字的旋转角度
        Integer degree = -45;
        // 设置水印透明度
        float alpha = 0.5f;
        try {
            // 文件转化为图片
            Image srcImg = sourceImgPath;
            // 获取图片的宽
            int srcImgWidth = srcImg.getWidth(null);
            // 获取图片的高
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            // 得到画笔
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // 设置水印颜色
            g.setColor(markContentColor);
            // 设置字体
            g.setFont(font);
            // 设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree));
            }
            // 画出水印,并设置水印位置
            g.drawString(waterMarkContent, -48, 110);
            // 释放资源
            g.dispose();
            // 直接输出文件用来测试结果
            //ImageIO.write(bufImg, "png", new File("C:/Users/cong/Desktop/test1.png"));
            return transferAlpha(bufImg);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        return null;
    }

    /**
     * @description 转化成透明背景的图片
     * @param bufImg    源图片
     */
    public static BufferedImage transferAlpha(BufferedImage bufImg) {

        try {
            Image image = bufImg;
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            // 直接输出文件用来测试结果
            //ImageIO.write(bufferedImage, "png", new File("C:/Users/cong/Desktop/test2.png"));
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
