FROM eclipse-temurin:latest

RUN mkdir /opt/app
RUN mkdir /opt/app/res
RUN mkdir /opt/app/wrk
COPY ./out/artifacts/act_calc_fnc_jar/ /opt/app
WORKDIR /opt/app
CMD java -jar act-calc-fnc.jar --dsf 2002-01 --dst 2021-09 --drf 2021-09 --fos stock.csv --too hgb
