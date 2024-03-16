package Calculations;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SatelliteCalculations {
    public SatelliteCalculations() throws IOException {
    }
    static final double u = 3.986005e14; //m^3/s^2
    static final double WE = 7.2921151467e-5; //rad/s
    static final double a =6378137.0;
    static final double e2 = 0.00669438002290;
    int phi=52;
    int lam=21;
    int height=100;
    List<List<Double>> nav = AlmanacModule.readAlmanac("src/main/resources/Almanac2024053.alm");
    public static double[][] Rneu(double phi, double lam) {
        double[][] R = new double[3][3];
        R[0][0] = -Math.sin(phi) * Math.cos(lam);
        R[0][1] = -Math.sin(lam);
        R[0][2] = Math.cos(phi) * Math.cos(lam);

        R[1][0] = -Math.sin(phi) * Math.sin(lam);
        R[1][1] = Math.cos(lam);
        R[1][2] = Math.cos(phi) * Math.sin(lam);

        R[2][0] = Math.cos(phi);
        R[2][1] = 0;
        R[2][2] = Math.sin(phi);

        return R;
    }
    public static double julday(int year, int month, int day, int hour) {
        if (month <= 2) {
            year = year - 1;
            month = month + 12;
        }
        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + (double) hour /24 - 1537.5;
    }
    public static double[] getGPSTime(int year, int month, int day, int hour, int minute, int second){
        double days = julday(year,month,day,0)- julday(1980,1,6,0);
        double week = days/7;
        double CalculatedDay = days%7;
        double secondOfWeek = CalculatedDay*86400+hour*3600+minute*60+second;
        return new double[]{week,secondOfWeek};
    }
    public double Np(int phi, double a, double e2){
        return a/(Math.pow((1-e2*Math.sin(phi)*Math.sin(phi)),0.5));
    }
    static int hour=12;
    static int minute=0;
    static int second=0;
    static int year=2024;
    static int month=2;
    static int day=29;
    public static double[] weekSecond = getGPSTime(year,month,day,hour,minute,second);

    public static double[] getSatPos(double t, double week, List<Double> rowNav){
        double u = 3.986005e14;
        double WE = 7.2921151467e-5;
        double e = rowNav.get(2);
        double a = Math.pow(rowNav.get(3),2);
        System.out.println("a: " + a);
        double omega = Math.toRadians(rowNav.get(4));
        double w = Math.toRadians(rowNav.get(5));
        double M = Math.toRadians(rowNav.get(6));
        System.out.println("M: " + M);
        double toa = rowNav.get(7);
        System.out.println("toa: " + toa);
        double i = Math.toRadians(rowNav.get(8)+54);
        double omegaTime = Math.toRadians(rowNav.get(9)/1000);
        double GPSWeek = rowNav.get(12);
        double time = week * 7 * 86400 + weekSecond[1];
        System.out.println("time: " + time);
        double toaWeek = GPSWeek *7*86400+toa;
        System.out.println("toaWeek: " + toaWeek);
        double tk = time - toaWeek;
        System.out.println("tk: " + tk);
        double n = Math.sqrt(u/Math.pow(a, 3));
        double Mk = M+n*tk;
        System.out.println("Mk: " + Mk);
        List<Double> E = new ArrayList<>();
        int j =0;
        E.add(Mk);
        while(true) {
            E.add(Mk+e*Math.sin(E.get(j)));
            j++;
            if(Math.abs(E.get(j)-E.get(j-1))< Math.pow(10,-12)){
                break;
            }
        }
        for (double Es : E){
            System.out.println("E: " + Es);
        }
        double vk = Math.atan2(Math.sqrt(1 - Math.pow(e, 2)) * Math.sin(E.get(j)), Math.cos(E.get(j)) - e);
        System.out.println("vk: " + vk);
        double phik = vk+w;
        System.out.println("phik: " + phik);
        double rk = a*(1-e*Math.cos(E.get(j)));
        System.out.println("rk: " + rk);
        double xk = rk*Math.cos(phik);
        System.out.println("xk: " + xk);
        double yk = rk*Math.sin(phik);
        System.out.println("yk: " + yk);
        double omegaK = omega+(omegaTime-WE)*tk-(WE-toa);
        System.out.println("omegaK: " + omegaK);
        double X = xk * Math.cos(omegaK) - yk * Math.cos(i) * Math.sin(omegaK);
        double Y = xk * Math.sin(omegaK) + yk * Math.cos(i) * Math.cos(omegaK);
        double Z = yk * Math.sin(i);
        return new double[]{X,Y,Z};
    }

//    public static void calculateSatelliteData(int start, int stop, double[][] nav, double[] XYZr, double maska){

    //}




}