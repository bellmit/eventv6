package cn.ffcs.zhsq.util;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import  cn.ffcs.shequ.web.spring.SpringContextUtil;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * describe : 多边形比较工具（点与多边形关系的判断）
 *              如果点在多边形内，则返回true；如果点在多边形外则返回false
 * @author :   sulch
 * @date :   2019/2/26 17:16
 **/
public class PolygonUtils {
    private List<Point> polygon = new ArrayList<Point>();
//    private IMixedGridInfoService mixedGridInfoService;
//    private IArcgisInfoService arcgisInfoService;

    public PolygonUtils(){

    }
    /**
     * 通过网格id获取网格多边形的点集
     * @param gridId
     */
    public PolygonUtils(Long gridId, IArcgisInfoService arcgisInfoService){
        initService();
        if(gridId != null){//获取网格轮廓
            String polygonStr = "";
            List<ArcgisInfoOfGrid> list = arcgisInfoService.getArcgisDataOfGrid(gridId ,5);
            if(list != null && list.size() >0){
                polygonStr = list.get(0).getHs();
            }
            initPolygon(polygonStr);
        }
    }

    /**
     * 通过网格信息域编码获取网格多边形的点集
     * @param infoOrgCode
     */
    public PolygonUtils(String infoOrgCode, IMixedGridInfoService mixedGridInfoService, IArcgisInfoService arcgisInfoService){
        initService();
        if(StringUtils.isNotBlank(infoOrgCode)){//获取网格轮廓
            List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
            String polygonStr = "";
            if(gridInfos != null && gridInfos.size() > 0){
                List<ArcgisInfoOfGrid> list = arcgisInfoService.getArcgisDataOfGrid(gridInfos.get(0).getGridId() ,5);
                if(list != null && list.size() >0){
                    polygonStr = list.get(0).getHs();
                }
                initPolygon(polygonStr);
            }

        }
    }

    /**
     * 初始化服务
     */
    private void initService(){
//        mixedGridInfoService = SpringContextUtil.getApplicationContext().getBean(IMixedGridInfoService.class);
//        arcgisInfoService = SpringContextUtil.getApplicationContext().getBean(IArcgisInfoService.class);
    }

    /**
     * 初始化多边形对象
     * @param hs
     */
    private void initPolygon(String hs){
        if(StringUtils.isNotBlank(hs)){
            String[] xyArr = hs.split(",");
            if(xyArr != null && xyArr.length>0){
                for(int i=0;i<xyArr.length && xyArr[i] != null && xyArr[i+1] != null;i=i+2){
                    Point point = new Point(Double.parseDouble(xyArr[i]), Double.parseDouble(xyArr[i+1]));
                    polygon.add(point);
                }
            }
        }
    }

    //方法返回值即为两个数中最大值
    public double Max(double Num1,double Num2){
        //判断Num1是否大于Num2,是则返回Num1
        if(Num1>Num2){
            return Num1;
        } else {
            //否则Num1<=Num2，返回Num2
            return Num2;
        }
    }

    //方法返回值即为两个数中最大值
    public double Min(double Num1,double Num2){
        //判断Num1是否小于Num2,是则返回Num1
        if(Num1<Num2){
            return Num1;
        } else {
            //否则Num1>=Num2，返回Num2
            return Num2;
        }
    }

    public boolean onsegment(Point pi,Point pj,Point Q) {
        if ((Q.getX() - pi.getX()) * (pj.getY() - pi.getY()) == (pj.getX() - pi.getX()) * (Q.getY() - pi.getY()) && Min(pi.getX(), pj.getX()) <= Q.getX() && Q.getX() <= Max(pi.getX(), pj.getX()) && Min(pi.getY(), pj.getY()) <= Q.getY() && Q.getY() <= Max(pi.getY(), pj.getY())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否在多边形内
     * @param p 目标点
     * @param polygon 多边形点集
     * @return
     */
    public boolean insidePolygon(double x, double y, List<Point> polygon) {
        if(x > 0 && y > 0 && polygon != null && polygon.size() > 0){
            Point p = new Point(x, y);
            int counter=0;
            double xinters;
            Point p1,p2;
            p1= polygon.get(0);
            for(int i=1;i<=polygon.size();i++){
                p2=polygon.get(i%polygon.size());
                if(onsegment(p1,p2,p)){
                    return true;
                }
                if(p.getY()>Min(p1.getY(),p2.getY())){
                    if(p.getY()<=Max(p1.getY(),p2.getY())){
                        if(p.getX()<=Max(p1.getX(),p2.getX())){
                            if(p1.getY()!=p2.getY()){
                                xinters=(p.getY()-p1.getY())*(p2.getX()-p1.getX())/(p2.getY()-p1.getY())+p1.getX();
                                if(p1.getX()==p2.getX()||p.getX()<=xinters){
                                    counter++;
                                }
                            }
                        }
                    }
                }
                p1=p2;
            }
            if(counter%2==0){
                return false;
            }
            return true;

        }else{
            return false;
        }

    }

    /**
     * 判断是否在多边形内
     * @param x  目标点的x(经度)
     * @param y  目标点的y（纬度）
     * @param polygonStr 多边形点集字符串（用“,”分隔，如：118.74023612663987,26.5000803046875,119.17968925163987,26.57423801953125）
     * @return
     */
    public boolean insidePolygon(double x, double y, String polygonStr) {
        if(x > 0 && y > 0 && StringUtils.isNotBlank(polygonStr)){
            Point p = new Point(x, y);
            initPolygon(polygonStr);

            int counter=0;
            double xinters;
            Point p1,p2;
            p1= polygon.get(0);
            for(int i=1;i<=polygon.size();i++){
                p2=polygon.get(i%polygon.size());
                if(onsegment(p1,p2,p)){
                    return true;
                }
                if(p.getY()>Min(p1.getY(),p2.getY())){
                    if(p.getY()<=Max(p1.getY(),p2.getY())){
                        if(p.getX()<=Max(p1.getX(),p2.getX())){
                            if(p1.getY()!=p2.getY()){
                                xinters=(p.getY()-p1.getY())*(p2.getX()-p1.getX())/(p2.getY()-p1.getY())+p1.getX();
                                if(p1.getX()==p2.getX()||p.getX()<=xinters){
                                    counter++;
                                }
                            }
                        }
                    }
                }
                p1=p2;
            }
            if(counter%2==0){
                return false;
            }
            return true;

        }else{
            return false;
        }

    }

    /**
     * 判断是否在多边形内
     * @param x  目标点的x(经度)
     * @param y  目标点的y（纬度）
     * @param polygonStr 多边形点集字符串（用“,”分隔，如：118.74023612663987,26.5000803046875,119.17968925163987,26.57423801953125）
     * @return
     */
    public boolean insidePolygon(double x, double y) {
        if(x > 0 && y > 0 && polygon != null && polygon.size() > 0){
            Point p = new Point(x, y);
            String polygonStr = "";

            int counter=0;
            double xinters;
            Point p1,p2;
            p1= polygon.get(0);
            for(int i=1;i<=polygon.size();i++){
                p2=polygon.get(i%polygon.size());
                if(onsegment(p1,p2,p)){
                    return true;
                }
                if(p.getY()>Min(p1.getY(),p2.getY())){
                    if(p.getY()<=Max(p1.getY(),p2.getY())){
                        if(p.getX()<=Max(p1.getX(),p2.getX())){
                            if(p1.getY()!=p2.getY()){
                                xinters=(p.getY()-p1.getY())*(p2.getX()-p1.getX())/(p2.getY()-p1.getY())+p1.getX();
                                if(p1.getX()==p2.getX()||p.getX()<=xinters){
                                    counter++;
                                }
                            }
                        }
                    }
                }
                p1=p2;
            }
            if(counter%2==0){
                return false;
            }
            return true;

        }else{
            return false;
        }

    }

    private final double EARTH_RADIUS = 6371393; // 平均半径,单位：m

    /**
     * 通过AB点经纬度获取距离
     * @param pointA A点(经，纬)
     * @param pointB B点(经，纬)
     * @return 距离(单位：米)
     */
    public double getDistance(double pointAX, double pointAY, double pointBX, double pointBY) {
        Point pointA = new Point(pointAX, pointAY);
        Point pointB = new Point(pointBX, pointBY);
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(pointA.getX()); // A经弧度
        double radiansAY = Math.toRadians(pointA.getY()); // A纬弧度
        double radiansBX = Math.toRadians(pointB.getX()); // B经弧度
        double radiansBY = Math.toRadians(pointB.getY()); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }

    public static void main(String[] args) {
        //within
        //Point p = new Point(119.013177715995, 26.3490182929688);
        //double x = 119.013177715995;
        //double y = 26.3490182929688;
        //outside
//        Point p = new Point(118.80443748162, 26.5975839667969);
//        double x = 118.80443748162;
//        double y = 26.5975839667969;
//        String hs = "118.78933128044847,26.45476170117188,118.82366355583909,26.511066632812504,118.92631705925706,26.522052960937508,119.00081809685472,26.53990574414063,119.06673606560472,26.54539890820313,119.12029441521409,26.511066632812504,119.18758567497972,26.502826886718754,119.24526389763597,26.486347394531254,119.26998313591722,26.44926853710938,119.28508933708909,26.40257664257813,119.30019553826097,26.346271710937504,119.31392844841722,26.316059308593754,119.30431541130784,26.274860578125004,119.28371604607347,26.25151463085938,119.25350364372972,26.241901593750004,119.21230491326097,26.24052830273438,119.18346580193284,26.22679539257813,119.14226707146409,26.222675519531254,119.10930808708909,26.217182355468754,119.06536277458909,26.21031590039063,119.02004417107347,26.211689191406254,118.97884544060472,26.21306248242188,118.94588645622972,26.21580906445313,118.92116721794847,26.219928937500004,118.89232810662034,26.222675519531254,118.87584861443284,26.230915265625004,118.85387595818284,26.244648175781254,118.83464988396409,26.283100324218754,118.80718406365159,26.302326398437504,118.78383811638597,26.318805890625004,118.74504264519456,26.336658673828133,118.73027976677659,26.351764875000004,118.72066672966722,26.36961765820313,118.70968040154222,26.395710187500004,118.70693381951097,26.41630955273438,118.70418723747972,26.43278904492188,118.70006736443284,26.45476170117188,118.79207786247972,26.45476170117188,118.76873191521409,26.48497410351563,118.82778342888597,26.456134992187504,118.79997428581956,26.391590314453133,118.78933128044847,26.45476170117188";
//        Date startDate = new Date();
//        if(PolygonUtils.insidePolygon(x, y, hs)){
//            System.out.println("判定结果：within");
//        }else{
//            System.out.println("判定结果：outside");
//        }
//        Date endDate = new Date();
//        long useTime = endDate.getTime() - startDate.getTime();
//        System.out.println("开始时间："+ startDate.getTime());
//        System.out.println("结束时间："+ endDate.getTime());
//        System.out.println("用时："+ useTime + "毫秒");
        // 北京 东单地铁站
//        Point pointDD = new Point(116.425249, 39.914504);
//        // 北京 西单地铁站
//        Point pointXD = new Point(116.382001, 39.913329);
//        System.out.println("东单地铁站--西单地铁站，距离："+getDistance(pointDD, pointXD));
//        System.out.println();

    }
}

class Point{
    private double x,y;
    public Point(){

    }
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
