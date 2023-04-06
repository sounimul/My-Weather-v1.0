package weather.weatherspring.service;

import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Coordinate;


@Transactional
public class LocationService {

    public Coordinate getXY(Coordinate coordinate){
        /* 단기예보 지도 정보 */
//        double RE = 6371.00877; // 지도반경
        double GRID = 5.0; // 격자간격 (km)
//        double SLAT1 = 30.0; // 표준위도 1
//        double SLAT2 = 60.0; // 표준위도 2
//        double OLON = 126.0; // 기준점 경도
//        double OLAT = 38.0; // 기준점 위도
        double XO = 210/GRID; // 기준점 X좌표
        double YO = 675/GRID; // 기준점 Y좌표

        double DEGRAD = Math.PI / 180.0;

        double re = 6371.00877 / GRID;
        double slat1 = 30.0 * DEGRAD;
        double slat2 = 60.0 * DEGRAD;
        double olon = 126.0 * DEGRAD;
        double olat = 38.0 * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf,sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro,sn);

        /* 위경도 -> (X,Y) */
        double ra = Math.tan(Math.PI * 0.25 + coordinate.getLatitude() * DEGRAD *0.5);
        ra = re * sf / Math.pow(ra,sn);
        double theta = coordinate.getLongitude() * DEGRAD - olon;
        if(theta > Math.PI) theta -= 2.0 * Math.PI;
        if(theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        double x = (int)(ra * Math.sin(theta) + XO)+1.5;
        double y = (int)(ro - ra*Math.cos(theta) + YO)+1.5;
        coordinate.setXcoor((long)x);
        coordinate.setYcoor((long)y);

        return coordinate;
    }

}
