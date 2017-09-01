package com.example.dsm2016.smartcartver4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.io.Serializable;


/**
 * Created by Skj on 2017-08-26.
 */

public class PositionManager implements SensorEventListener, Serializable {

    Context context;
    //double[] gravityData = new double[2];    //x축, y축 중력 가속도 데이터

    //가속도 센서를 통한 거리 측정 관련 데이터
    double[] acceleration = {0, 0};    //x축 이전 및 현재 가속도 데이터
    double[] velocity = {0, 0};    //x축 이전 및 현재 속도 데이터
    double[] distance = {0, 0};    //x축 이동 거리
    long accelCurrentTime = 0;    //현재 데이터를 얻어 온 시간
    int countMoveEnded = 0;    //움직임이 없는 경우를 카운트하는 변수
    boolean accelChanged = false;    //가속도 센서 값을 읽어 계산이 실행되었는지 판단하는 변수
    double deltaDistance = 0.0;    //순간 거리 변화량(cm)
    //double[] accelerationY = new double[2];    //y축 이전 및 현재 가속도 데이터
    //double[] velocityY = new double[2];    //y축 이전 및 현재 속도 데이터
    //double[] positionY = new double[2];    //y축 이동 거리

    //가속도와 자기계 센서를 통한 방위 측정 관련 데이터
    float[] accelForMagnet = new float[3];    //x, y, z축 가속도 데이터
    float[] magnetism = new float[3];    //자력 데이터
    float[] R = new float[9];    //x, y, z축에 대한 모든 회전각
    float[] orientation = new float[3];    //방위값
    double orientationValue = 0;    //방위값 저장 변수
    int direction;    //동서남북 저장 변수 1 : north, 2 : east, 3 : south, 4 : west
    int countGotAverage = 0;    //평균 계산 횟수 카운팅 변수
    double averageOrientation = 0.0;    //평균 값
    boolean gyroChanged = false;    //자이로 센서 값을 읽어 계산이 실행되었는지 판단하는 변수

    //자이로스코프 센서를 통한 회전 각도 측정 관련 데이터
    long gyroCurrentTIme = 0;    //현재 데이터를 얻어온 시간
    double[] gyroZ = {0.0, 0.0};    //z축 회전 각속도
    double yaw = 0.0;    //z축 회전 각도(라디안)
    double radianToDegree = 180.0 / Math.PI;    //라디안을 각도로 변환시키는 상수
    double turnedDegree = 0.0;    //회전한 각도(도)
    boolean degreeChanged = false;    //휴대폰이 회전했는지 체킹하는 변수
    boolean gyroCalibrated = false;    //앱 실행 시 초반 튀는 자이로 값 보정을 위한 변수
    double theta = 180.0;    //turnedDegree 저장 변수, 완전한 일반각을 구하기 위해 180도를 저장하고 시작

    //카트의 위치 정보 관련 데이터
    double coordX = 1200.0, coordY = 150.0;
    double deltaX = 0.0, deltaY = 0.0;

    public PositionManager(Context context) {
        this.context = context;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            //가속도 센서의 데이터 변화에 따른 처리
            case Sensor.TYPE_ACCELEROMETER:
                //중력 가속도 데이터를 구하기 위해 사용되는 low-pass filter 적용 시 사용되는 비율 데이터
                //t : 센서가 가속도의 63%를 인지하기 위해 걸리는 시간
                //dt : 이벤트 전송율 혹 이벤트 전송속도
                //alpha = t / (t + dt)
                //final float alpha = (float) 0.8;

                //후에 자기계 센서를 위해 가속도 값 얻어오기
                System.arraycopy(event.values, 0, accelForMagnet, 0, event.values.length);

                if (accelCurrentTime == 0)
                    accelCurrentTime = System.currentTimeMillis();
                else {
                    long accelLastTime = accelCurrentTime;
                    accelCurrentTime = System.currentTimeMillis();

                    //이전 가속도와 현재 가속도 측정 시간 gap 측정
                    //msec를 sec로 변환
                    double deltaT = ((double) (accelCurrentTime - accelLastTime) / 1000);
                    double accelerationWindow = 0.3;    //가속도 센서값 노이즈 판단 기준점
                    //Log.d("deltaT", Double.toString(deltaT));

                    //low-pass filter
                    //gravityData[0] = alpha * gravityData[0] + (1 - alpha) * event.values[0];    //x축의 중력 가속도

                    //측정된 데이터에서 가속도만을 알아내기 위해 중력 가속도 데이터 제거
                    //acceleration[1] = event.values[0] - gravityData[0];

                    acceleration[1] = (double) (event.values[0]);    //현재 가속도 값 얻어오기
                    //acceleration[1] = Double.parseDouble(String.format("%.10f", acceleration[1]));    //소수 열째자리까지로 표현
                    acceleration[1] = FilteringWindow(acceleration[1], accelerationWindow);    //노이즈 값 무시

                    //가속도 적분 -> 속도
                    velocity[1] = CalcIntegral(velocity[0], acceleration[0], acceleration[1], deltaT);
                    //velocity[1] = Double.parseDouble(String.format("%.10f", velocity[1]));    //소수 열째자리까지 표현

                    //속도 적분 -> 거리
                    distance[1] = CalcIntegral(distance[0], velocity[0], velocity[1], deltaT);
                    distance[1] = Double.parseDouble(String.format("%.3f", distance[1]));    //소수 셋째자리까지 표현
                    deltaDistance = (distance[1] - distance[0]) * 100.0;   //이동 거리 순간 변화량 측정, m -> cm 변환

                    //((MainActivity) context).tvAcceleration.setText(Double.toString(acceleration[1]));
                    //((MainActivity) context).tvVelocity.setText(Double.toString(velocity[1]));
                    //((MainActivity) context).tvDistance.setText(Double.toString(distance[1]));

                    //정지한 상태인지 체크
                    MovementEndCheck("accelerometer");

                    //현재 데이터를 이전 데이터로 저장
                    acceleration[0] = acceleration[1];
                    velocity[0] = velocity[1];
                    distance[0] = distance[1];

                    accelChanged = true;
                }
                break;

            //방향 센서 데이터 변화에 따른 처리
//            case Sensor.TYPE_MAGNETIC_FIELD:
//                System.arraycopy(event.values, 0, magnetism, 0, event.values.length);    //현재 자기계 센서값 읽어오기
//                SensorManager.getRotationMatrix(R, null, accelForMagnet, magnetism);    //회전 행렬 값 얻어오기
//                SensorManager.getOrientation(R, orientation);    //방위값 저장
//
////                if(orientation[0] > 0.3)
////                    direction = 2;
////                else if(orientation[0] >= -0.3 && orientation[0] <= 0.3)
////                    direction = 1;
////                else if(orientation[0] < -0.3)
////                    direction = 3;
//                //방위 부호가 마이너스이면 플러스로 변환
//                if(Math.toDegrees((double)orientation[0]) < 0.0)
//                    orientationValue = Math.toDegrees((double)orientation[0]) + 360.0;
//                else
//                    orientationValue = Math.toDegrees((double)orientation[0]);
//
//                //평균값 계산이 완료되면 다음 동작 수행
//                if(CountCalcAverage(orientationValue) == 10) {
//                    ((MainActivity)context).tvOrientation.setText(Double.toString(orientationValue));
//                }
//
//
//                break;

            //자이로 센서 데이터 변화에 따른 처리
            case Sensor.TYPE_GYROSCOPE:
                if (gyroCurrentTIme == 0)
                    gyroCurrentTIme = System.currentTimeMillis();
                else {
                    long gyroLastTime = gyroCurrentTIme;
                    gyroCurrentTIme = System.currentTimeMillis();

                    //시간 변화량 측정
                    double deltaT = ((double) (gyroCurrentTIme - gyroLastTime) / 1000);    //msec -> sec
                    double gyroWindow = 0.02;

                    //z축 회전 각속도 측정 및 노이즈 무시
                    gyroZ[1] = event.values[2];
                    gyroZ[1] = FilteringWindow(gyroZ[1], gyroWindow);

                    //z축 회전 각속도 적분 -> 회전한 각도
                    yaw = CalcIntegral(yaw, gyroZ[0], gyroZ[1], deltaT);
                    turnedDegree = yaw * radianToDegree;    //radian -> turnedDegree
                    //현재 각속도를 이전 각속도로 저장
                    gyroZ[0] = gyroZ[1];

                    //회전이 끝났는지 체크
                    MovementEndCheck("gyroscope");
                    //((MainActivity)context).tvTurnedDegree.setText(Double.toString(theta));
                    //Log.i("gyro", Double.toString(gyroZ[1]));
                    //Log.i("theta", Double.toString(theta));
                    //Log.i("turnedDegree", Double.toString(turnedDegree));
                    gyroChanged = true;
                }
                break;
        }
        //현재 카트 좌표 계산
        CalcCoord();
    }

    //움직임이 없을 떄 센서 값 노이즈 발생 시 제거하는 메소드
    double FilteringWindow(double value, double window) {
        //value가 노이즈 판단 기준점인 window보다 낮으면
        if (Math.abs(value) <= window) {
            value = 0;
        }
        return value;
    }

    //움직임이 없을 때 각 센서 데이터들을 강제로 0으로 세팅하는 메소드
    //가속도 센서의 경우 움직임이 없으면 FilteringWindow() 메소드로 가속도가 0으로 다운되니 속도도 0으로 다운시킴
    //자이로 센서의 경우 회전이 없으면 회전한 각도인 yaw를 0으로 다운시켜야 함
    void MovementEndCheck(String sensorType) {
        //움직임이 없는 가속도 센서에 대한 처리
        if (sensorType.equals("accelerometer")) {
            if (acceleration[1] == 0)
                countMoveEnded++;
            else
                countMoveEnded = 0;
            if (countMoveEnded >= 5) {
                velocity[1] = 0;
                velocity[0] = 0;
            }
        }

        //회전이 없는 자이로스코프 센서에 대한 처리
        else if (sensorType.equals("gyroscope")) {
            //처음 정지했을 때와 회전을 다하고 난 뒤 정지했을 때
            if ((gyroZ[1] == 0) /*|| (degreeChanged == true)*/) {
                //yaw = 0;
                //if(degreeChanged == false)
                //   degreeChanged = true;
                //회전을 다하고 정지했을 때
                //자이로 센서값이 0이면 초반 튀는 값이 보정됨
                gyroCalibrated = true;
                //회전이 이뤄졌었다면
                if (degreeChanged == true) {
                    degreeChanged = false;
                    //이전까지의 회전 각도를 더해줌
                    theta = theta + (-turnedDegree);
                }
                yaw = 0;
                //degreeChanged = false;
            }

            //정지하기 전까지의 회전각 저장
            else {
                //초반 튀는 값이 보정되여야만 실행
                if (gyroCalibrated == true)
                    //회전각이 변하면 정지할 것으로 앎
                    if (degreeChanged == false)
                        degreeChanged = true;
                //if(degreeChanged == false)
                //    return;
                // else {
                //    theta = theta + (-turnedDegree);    //삼각함수를 위해선 실제 각도 부호와 반대로 해야 함
                //Log.i("theta", Double.toString(turnedDegree));
                //degreeChanged = true;
                //}
            }
        }

    }

    //적분 수행 메소드
    //baseData : 이전까지의 속도/거리, prevData : previous 가속도/속도, curData : current 가속도/속도, time : 시간 변화량
    double CalcIntegral(double baseData, double prevData, double curData, double time) {
        double integratedData = 0;

        //이전 데이터와 현재 데이터의 평균을 높이로 하여 그래프 상 직사각형의 넓이를 구함
        integratedData = baseData + (prevData + ((curData - prevData) / 2)) * time;
        return integratedData;
    }

    //위치 좌표 변화를 측정하여 현재 카트 좌표를 실시간으로 계산하는 메소드
    void CalcCoord() {
        if ((accelChanged == true) && (gyroChanged == true)) {
            deltaX = deltaDistance * Math.cos(Math.toRadians(theta));    //x좌표 변화량 : 이동 거리 * cos 세타
            deltaY = deltaDistance * Math.sin(Math.toRadians(theta));    //y좌표 변화량 : 이동 거리 * sin 세타
            //현재 카트 x, y좌표 변경
            coordX = coordX + deltaX;
            coordY = coordY + deltaY;

            //좌표값 저장
//            SharedPreferences coordData = context.getSharedPreferences("coordData", 0);
//            SharedPreferences.Editor editor = coordData.edit();
//            editor.putString("coordX", Double.toString(coordX));
//            editor.putString("coordY", Double.toString(coordY));
//            editor.commit();
            //Log.i("coordX", Double.toString(coordX));
            //Log.i("coordY", Double.toString(coordY));
        }
    }

//    //카트와 각 물품들까지의 거리 측정
//    ListItem CalcDistance(ListItem[] items, double[] itemDistance) {
//        double[] distance = new double[8];
//        for (int i = 0; i < items.length; i++) {
//            //itemDistance[i] = Math.sqrt(Math.pow(coordX - items[i].coordX, 2) + Math.pow(coordY - items[i].coordY, 2));
//            //Log.i("distance", Double.toString(itemDistance[0]));
//            //SystemClock.sleep(500);
//            if (itemDistance[i] < 50.0)
//                return items[i];
//                //itemsArrayList.add(items[i]);
//            else
//                return null;
//            //itemsArrayList.remove(items[i]);
//        }
//        return null;
//    }
//}

    //10번 방위 값을 읽어와 방위값의 평균을 구하는 메소드
//    int CountCalcAverage(double value) {
//        if(countGotAverage < 10) {
//            averageOrientation += value;
//            countGotAverage++;
//            averageOrientation = averageOrientation / (double) countGotAverage;
//            return countGotAverage;
//        }
//        else
//            return 0;
//    }

}
