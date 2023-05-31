package test.dbo;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.12.20 15:38
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_ZA_DAILY_WEATHER", comment = "관측소기준일간날씨테이블")
@FxIndex(name = "FX_ZA_DAILY_WEATHER__PK", type = INDEX_TYPE.PK, columns = {"STNID", "TM"})
public class FX_ZA_DAILY_WEATHER implements Serializable {

public FX_ZA_DAILY_WEATHER() {
 }

@FxColumn(name = "STNID", size = 3, comment = "지점 번호")
private String stnid;


@FxColumn(name = "STNNM", size = 6, nullable = true, comment = "지점명")
private String stnnm;


@FxColumn(name = "TM", size = 10, comment = "시간")
private String tm;


@FxColumn(name = "AVGTA", size = 9, nullable = true, comment = "평균 기온")
private Float avgta;


@FxColumn(name = "MINTA", size = 9, nullable = true, comment = "최저 기온")
private Float minta;


@FxColumn(name = "MINTAHRMT", size = 9, nullable = true, comment = "최저 기온 시각")
private Float mintahrmt;


@FxColumn(name = "MAXTA", size = 9, nullable = true, comment = "최고 기온")
private Float maxta;


@FxColumn(name = "MAXTAHRMT", size = 9, nullable = true, comment = "최고 기온 시각")
private Float maxtahrmt;


@FxColumn(name = "SUMRNDUR", size = 9, nullable = true, comment = "강수 계속시간")
private Float sumrndur;


@FxColumn(name = "MI10MAXRN", size = 9, nullable = true, comment = "10분 최다강수량")
private Float mi10maxrn;


@FxColumn(name = "MI10MAXRNHRMT", size = 9, nullable = true, comment = "10분 최다강수량 시각")
private Float mi10maxrnhrmt;


@FxColumn(name = "HR1MAXRN", size = 9, nullable = true, comment = "1시간 최다강수량")
private Float hr1maxrn;


@FxColumn(name = "HR1MAXRNHRMT", size = 9, nullable = true, comment = "1시간 최다 강수량 시각")
private Float hr1maxrnhrmt;


@FxColumn(name = "SUMRN", size = 9, nullable = true, comment = "일강수량")
private Float sumrn;


@FxColumn(name = "MAXINSWS", size = 9, nullable = true, comment = "최대 순간풍속")
private Float maxinsws;


@FxColumn(name = "MAXINSWSWD", size = 9, nullable = true, comment = "최대 순간 풍속 풍향")
private Float maxinswswd;


@FxColumn(name = "MAXINSWSHRMT", size = 9, nullable = true, comment = "최대 순간풍속 시각")
private Float maxinswshrmt;


@FxColumn(name = "MAXWS", size = 9, nullable = true, comment = "최대 풍속")
private Float maxws;


@FxColumn(name = "MAXWSWD", size = 9, nullable = true, comment = "최대 풍속 풍향")
private Float maxwswd;


@FxColumn(name = "MAXWSHRMT", size = 9, nullable = true, comment = "최대 풍속 시각")
private Float maxwshrmt;


@FxColumn(name = "AVGWS", size = 9, nullable = true, comment = "평균 풍속")
private Float avgws;


@FxColumn(name = "HR24SUMRWS", size = 9, nullable = true, comment = "풍정합")
private Float hr24sumrws;


@FxColumn(name = "MAXWD", size = 9, nullable = true, comment = "최다 풍향")
private Float maxwd;


@FxColumn(name = "AVGTD", size = 9, nullable = true, comment = "평균 이슬점온도")
private Float avgtd;


@FxColumn(name = "MINRHM", size = 9, nullable = true, comment = "최소 상대습도")
private Float minrhm;


@FxColumn(name = "MINRHMHRMT", size = 9, nullable = true, comment = "평균 상대습도 시각")
private Float minrhmhrmt;


@FxColumn(name = "AVGRHM", size = 9, nullable = true, comment = "평균 상대습도")
private Float avgrhm;


@FxColumn(name = "AVGPV", size = 9, nullable = true, comment = "평균 증기압")
private Float avgpv;


@FxColumn(name = "AVGPA", size = 9, nullable = true, comment = "평균 현지기압")
private Float avgpa;


@FxColumn(name = "MAXPS", size = 9, nullable = true, comment = "최고 해면 기압")
private Float maxps;


@FxColumn(name = "MAXPSHRMT", size = 9, nullable = true, comment = "최고 해면기압 시각")
private Float maxpshrmt;


@FxColumn(name = "MINPS", size = 9, nullable = true, comment = "최저 해면기압")
private Float minps;


@FxColumn(name = "MINPSHRMT", size = 9, nullable = true, comment = "최저 해면기압 시각")
private Float minpshrmt;


@FxColumn(name = "AVGPS", size = 9, nullable = true, comment = "평균 해면기압")
private Float avgps;


@FxColumn(name = "SSDUR", size = 9, nullable = true, comment = "가조시간")
private Float ssdur;


@FxColumn(name = "SUMSSHR", size = 9, nullable = true, comment = "합계 일조 시간")
private Float sumsshr;


@FxColumn(name = "HR1MAXICSRHRMT", size = 9, nullable = true, comment = "1시간 최다 일사 시각")
private Float hr1maxicsrhrmt;


@FxColumn(name = "HR1MAXICSR", size = 9, nullable = true, comment = "1시간 최다 일사량")
private Float hr1maxicsr;


@FxColumn(name = "SUMGSR", size = 9, nullable = true, comment = "합계 일사량")
private Float sumgsr;


@FxColumn(name = "DDMEFS", size = 9, nullable = true, comment = "일 최심신적설")
private Float ddmefs;


@FxColumn(name = "DDMEFSHRMT", size = 9, nullable = true, comment = "일 최심신적설 시각")
private Float ddmefshrmt;


@FxColumn(name = "DDMES", size = 9, nullable = true, comment = "일 최심적설")
private Float ddmes;


@FxColumn(name = "DDMESHRMT", size = 9, nullable = true, comment = "일 최심적설 시각")
private Float ddmeshrmt;


@FxColumn(name = "SUMDPTHFHSC", size = 9, nullable = true, comment = "합계 3시간 신적설")
private Float sumdpthfhsc;


@FxColumn(name = "AVGTCA", size = 9, nullable = true, comment = "평균 전운량")
private Float avgtca;


@FxColumn(name = "AVGLMAC", size = 9, nullable = true, comment = "평균 중하층운량")
private Float avglmac;


@FxColumn(name = "AVGTS", size = 9, nullable = true, comment = "평균 지면온도")
private Float avgts;


@FxColumn(name = "MINTG", size = 9, nullable = true, comment = "최저 초상온도")
private Float mintg;


@FxColumn(name = "AVGCM5TE", size = 9, nullable = true, comment = "평균 5cm 지중온도")
private Float avgcm5te;


@FxColumn(name = "AVGCM10TE", size = 9, nullable = true, comment = "평균10cm 지중온도")
private Float avgcm10te;


@FxColumn(name = "AVGCM20TE", size = 9, nullable = true, comment = "평균 20cm 지중온도")
private Float avgcm20te;


@FxColumn(name = "AVGCM30TE", size = 9, nullable = true, comment = "평균 30cm 지중온도")
private Float avgcm30te;


@FxColumn(name = "AVGM05TE", size = 9, nullable = true, comment = "0.5m 지중온도")
private Float avgm05te;


@FxColumn(name = "AVGM10TE", size = 9, nullable = true, comment = "1.0m 지중온도")
private Float avgm10te;


@FxColumn(name = "AVGM15TE", size = 9, nullable = true, comment = "1.5m 지중온도")
private Float avgm15te;


@FxColumn(name = "AVGM30TE", size = 9, nullable = true, comment = "3.0m 지중온도")
private Float avgm30te;


@FxColumn(name = "AVGM50TE", size = 9, nullable = true, comment = "5.0m 지중온도")
private Float avgm50te;


@FxColumn(name = "SUMLRGEV", size = 9, nullable = true, comment = "합계 대형증발량")
private Float sumlrgev;


@FxColumn(name = "SUMSMLEV", size = 9, nullable = true, comment = "합계 소형증발량")
private Float sumsmlev;


@FxColumn(name = "N99RN", size = 9, nullable = true, comment = "9-9강수")
private Float n99rn;


@FxColumn(name = "ISCS", size = 1000, nullable = true, comment = "일기현상")
private String iscs;


@FxColumn(name = "SUMFOGDUR", size = 6, nullable = true, comment = "안개 계속 시간")
private Float sumfogdur;


/**
* 지점 번호
* @return 지점 번호
*/
public String getStnid() {
return stnid;
}
/**
* 지점 번호
*@param stnid 지점 번호
*/
public void setStnid(String stnid) {
	this.stnid = stnid;
}
/**
* 지점명
* @return 지점명
*/
public String getStnnm() {
return stnnm;
}
/**
* 지점명
*@param stnnm 지점명
*/
public void setStnnm(String stnnm) {
	this.stnnm = stnnm;
}
/**
* 시간
* @return 시간
*/
public String getTm() {
return tm;
}
/**
* 시간
*@param tm 시간
*/
public void setTm(String tm) {
	this.tm = tm;
}
/**
* 평균 기온
* @return 평균 기온
*/
public Float getAvgta() {
return avgta;
}
/**
* 평균 기온
*@param avgta 평균 기온
*/
public void setAvgta(Float avgta) {
	this.avgta = avgta;
}
/**
* 최저 기온
* @return 최저 기온
*/
public Float getMinta() {
return minta;
}
/**
* 최저 기온
*@param minta 최저 기온
*/
public void setMinta(Float minta) {
	this.minta = minta;
}
/**
* 최저 기온 시각
* @return 최저 기온 시각
*/
public Float getMintahrmt() {
return mintahrmt;
}
/**
* 최저 기온 시각
*@param mintahrmt 최저 기온 시각
*/
public void setMintahrmt(Float mintahrmt) {
	this.mintahrmt = mintahrmt;
}
/**
* 최고 기온
* @return 최고 기온
*/
public Float getMaxta() {
return maxta;
}
/**
* 최고 기온
*@param maxta 최고 기온
*/
public void setMaxta(Float maxta) {
	this.maxta = maxta;
}
/**
* 최고 기온 시각
* @return 최고 기온 시각
*/
public Float getMaxtahrmt() {
return maxtahrmt;
}
/**
* 최고 기온 시각
*@param maxtahrmt 최고 기온 시각
*/
public void setMaxtahrmt(Float maxtahrmt) {
	this.maxtahrmt = maxtahrmt;
}
/**
* 강수 계속시간
* @return 강수 계속시간
*/
public Float getSumrndur() {
return sumrndur;
}
/**
* 강수 계속시간
*@param sumrndur 강수 계속시간
*/
public void setSumrndur(Float sumrndur) {
	this.sumrndur = sumrndur;
}
/**
* 10분 최다강수량
* @return 10분 최다강수량
*/
public Float getMi10maxrn() {
return mi10maxrn;
}
/**
* 10분 최다강수량
*@param mi10maxrn 10분 최다강수량
*/
public void setMi10maxrn(Float mi10maxrn) {
	this.mi10maxrn = mi10maxrn;
}
/**
* 10분 최다강수량 시각
* @return 10분 최다강수량 시각
*/
public Float getMi10maxrnhrmt() {
return mi10maxrnhrmt;
}
/**
* 10분 최다강수량 시각
*@param mi10maxrnhrmt 10분 최다강수량 시각
*/
public void setMi10maxrnhrmt(Float mi10maxrnhrmt) {
	this.mi10maxrnhrmt = mi10maxrnhrmt;
}
/**
* 1시간 최다강수량
* @return 1시간 최다강수량
*/
public Float getHr1maxrn() {
return hr1maxrn;
}
/**
* 1시간 최다강수량
*@param hr1maxrn 1시간 최다강수량
*/
public void setHr1maxrn(Float hr1maxrn) {
	this.hr1maxrn = hr1maxrn;
}
/**
* 1시간 최다 강수량 시각
* @return 1시간 최다 강수량 시각
*/
public Float getHr1maxrnhrmt() {
return hr1maxrnhrmt;
}
/**
* 1시간 최다 강수량 시각
*@param hr1maxrnhrmt 1시간 최다 강수량 시각
*/
public void setHr1maxrnhrmt(Float hr1maxrnhrmt) {
	this.hr1maxrnhrmt = hr1maxrnhrmt;
}
/**
* 일강수량
* @return 일강수량
*/
public Float getSumrn() {
return sumrn;
}
/**
* 일강수량
*@param sumrn 일강수량
*/
public void setSumrn(Float sumrn) {
	this.sumrn = sumrn;
}
/**
* 최대 순간풍속
* @return 최대 순간풍속
*/
public Float getMaxinsws() {
return maxinsws;
}
/**
* 최대 순간풍속
*@param maxinsws 최대 순간풍속
*/
public void setMaxinsws(Float maxinsws) {
	this.maxinsws = maxinsws;
}
/**
* 최대 순간 풍속 풍향
* @return 최대 순간 풍속 풍향
*/
public Float getMaxinswswd() {
return maxinswswd;
}
/**
* 최대 순간 풍속 풍향
*@param maxinswswd 최대 순간 풍속 풍향
*/
public void setMaxinswswd(Float maxinswswd) {
	this.maxinswswd = maxinswswd;
}
/**
* 최대 순간풍속 시각
* @return 최대 순간풍속 시각
*/
public Float getMaxinswshrmt() {
return maxinswshrmt;
}
/**
* 최대 순간풍속 시각
*@param maxinswshrmt 최대 순간풍속 시각
*/
public void setMaxinswshrmt(Float maxinswshrmt) {
	this.maxinswshrmt = maxinswshrmt;
}
/**
* 최대 풍속
* @return 최대 풍속
*/
public Float getMaxws() {
return maxws;
}
/**
* 최대 풍속
*@param maxws 최대 풍속
*/
public void setMaxws(Float maxws) {
	this.maxws = maxws;
}
/**
* 최대 풍속 풍향
* @return 최대 풍속 풍향
*/
public Float getMaxwswd() {
return maxwswd;
}
/**
* 최대 풍속 풍향
*@param maxwswd 최대 풍속 풍향
*/
public void setMaxwswd(Float maxwswd) {
	this.maxwswd = maxwswd;
}
/**
* 최대 풍속 시각
* @return 최대 풍속 시각
*/
public Float getMaxwshrmt() {
return maxwshrmt;
}
/**
* 최대 풍속 시각
*@param maxwshrmt 최대 풍속 시각
*/
public void setMaxwshrmt(Float maxwshrmt) {
	this.maxwshrmt = maxwshrmt;
}
/**
* 평균 풍속
* @return 평균 풍속
*/
public Float getAvgws() {
return avgws;
}
/**
* 평균 풍속
*@param avgws 평균 풍속
*/
public void setAvgws(Float avgws) {
	this.avgws = avgws;
}
/**
* 풍정합
* @return 풍정합
*/
public Float getHr24sumrws() {
return hr24sumrws;
}
/**
* 풍정합
*@param hr24sumrws 풍정합
*/
public void setHr24sumrws(Float hr24sumrws) {
	this.hr24sumrws = hr24sumrws;
}
/**
* 최다 풍향
* @return 최다 풍향
*/
public Float getMaxwd() {
return maxwd;
}
/**
* 최다 풍향
*@param maxwd 최다 풍향
*/
public void setMaxwd(Float maxwd) {
	this.maxwd = maxwd;
}
/**
* 평균 이슬점온도
* @return 평균 이슬점온도
*/
public Float getAvgtd() {
return avgtd;
}
/**
* 평균 이슬점온도
*@param avgtd 평균 이슬점온도
*/
public void setAvgtd(Float avgtd) {
	this.avgtd = avgtd;
}
/**
* 최소 상대습도
* @return 최소 상대습도
*/
public Float getMinrhm() {
return minrhm;
}
/**
* 최소 상대습도
*@param minrhm 최소 상대습도
*/
public void setMinrhm(Float minrhm) {
	this.minrhm = minrhm;
}
/**
* 평균 상대습도 시각
* @return 평균 상대습도 시각
*/
public Float getMinrhmhrmt() {
return minrhmhrmt;
}
/**
* 평균 상대습도 시각
*@param minrhmhrmt 평균 상대습도 시각
*/
public void setMinrhmhrmt(Float minrhmhrmt) {
	this.minrhmhrmt = minrhmhrmt;
}
/**
* 평균 상대습도
* @return 평균 상대습도
*/
public Float getAvgrhm() {
return avgrhm;
}
/**
* 평균 상대습도
*@param avgrhm 평균 상대습도
*/
public void setAvgrhm(Float avgrhm) {
	this.avgrhm = avgrhm;
}
/**
* 평균 증기압
* @return 평균 증기압
*/
public Float getAvgpv() {
return avgpv;
}
/**
* 평균 증기압
*@param avgpv 평균 증기압
*/
public void setAvgpv(Float avgpv) {
	this.avgpv = avgpv;
}
/**
* 평균 현지기압
* @return 평균 현지기압
*/
public Float getAvgpa() {
return avgpa;
}
/**
* 평균 현지기압
*@param avgpa 평균 현지기압
*/
public void setAvgpa(Float avgpa) {
	this.avgpa = avgpa;
}
/**
* 최고 해면 기압
* @return 최고 해면 기압
*/
public Float getMaxps() {
return maxps;
}
/**
* 최고 해면 기압
*@param maxps 최고 해면 기압
*/
public void setMaxps(Float maxps) {
	this.maxps = maxps;
}
/**
* 최고 해면기압 시각
* @return 최고 해면기압 시각
*/
public Float getMaxpshrmt() {
return maxpshrmt;
}
/**
* 최고 해면기압 시각
*@param maxpshrmt 최고 해면기압 시각
*/
public void setMaxpshrmt(Float maxpshrmt) {
	this.maxpshrmt = maxpshrmt;
}
/**
* 최저 해면기압
* @return 최저 해면기압
*/
public Float getMinps() {
return minps;
}
/**
* 최저 해면기압
*@param minps 최저 해면기압
*/
public void setMinps(Float minps) {
	this.minps = minps;
}
/**
* 최저 해면기압 시각
* @return 최저 해면기압 시각
*/
public Float getMinpshrmt() {
return minpshrmt;
}
/**
* 최저 해면기압 시각
*@param minpshrmt 최저 해면기압 시각
*/
public void setMinpshrmt(Float minpshrmt) {
	this.minpshrmt = minpshrmt;
}
/**
* 평균 해면기압
* @return 평균 해면기압
*/
public Float getAvgps() {
return avgps;
}
/**
* 평균 해면기압
*@param avgps 평균 해면기압
*/
public void setAvgps(Float avgps) {
	this.avgps = avgps;
}
/**
* 가조시간
* @return 가조시간
*/
public Float getSsdur() {
return ssdur;
}
/**
* 가조시간
*@param ssdur 가조시간
*/
public void setSsdur(Float ssdur) {
	this.ssdur = ssdur;
}
/**
* 합계 일조 시간
* @return 합계 일조 시간
*/
public Float getSumsshr() {
return sumsshr;
}
/**
* 합계 일조 시간
*@param sumsshr 합계 일조 시간
*/
public void setSumsshr(Float sumsshr) {
	this.sumsshr = sumsshr;
}
/**
* 1시간 최다 일사 시각
* @return 1시간 최다 일사 시각
*/
public Float getHr1maxicsrhrmt() {
return hr1maxicsrhrmt;
}
/**
* 1시간 최다 일사 시각
*@param hr1maxicsrhrmt 1시간 최다 일사 시각
*/
public void setHr1maxicsrhrmt(Float hr1maxicsrhrmt) {
	this.hr1maxicsrhrmt = hr1maxicsrhrmt;
}
/**
* 1시간 최다 일사량
* @return 1시간 최다 일사량
*/
public Float getHr1maxicsr() {
return hr1maxicsr;
}
/**
* 1시간 최다 일사량
*@param hr1maxicsr 1시간 최다 일사량
*/
public void setHr1maxicsr(Float hr1maxicsr) {
	this.hr1maxicsr = hr1maxicsr;
}
/**
* 합계 일사량
* @return 합계 일사량
*/
public Float getSumgsr() {
return sumgsr;
}
/**
* 합계 일사량
*@param sumgsr 합계 일사량
*/
public void setSumgsr(Float sumgsr) {
	this.sumgsr = sumgsr;
}
/**
* 일 최심신적설
* @return 일 최심신적설
*/
public Float getDdmefs() {
return ddmefs;
}
/**
* 일 최심신적설
*@param ddmefs 일 최심신적설
*/
public void setDdmefs(Float ddmefs) {
	this.ddmefs = ddmefs;
}
/**
* 일 최심신적설 시각
* @return 일 최심신적설 시각
*/
public Float getDdmefshrmt() {
return ddmefshrmt;
}
/**
* 일 최심신적설 시각
*@param ddmefshrmt 일 최심신적설 시각
*/
public void setDdmefshrmt(Float ddmefshrmt) {
	this.ddmefshrmt = ddmefshrmt;
}
/**
* 일 최심적설
* @return 일 최심적설
*/
public Float getDdmes() {
return ddmes;
}
/**
* 일 최심적설
*@param ddmes 일 최심적설
*/
public void setDdmes(Float ddmes) {
	this.ddmes = ddmes;
}
/**
* 일 최심적설 시각
* @return 일 최심적설 시각
*/
public Float getDdmeshrmt() {
return ddmeshrmt;
}
/**
* 일 최심적설 시각
*@param ddmeshrmt 일 최심적설 시각
*/
public void setDdmeshrmt(Float ddmeshrmt) {
	this.ddmeshrmt = ddmeshrmt;
}
/**
* 합계 3시간 신적설
* @return 합계 3시간 신적설
*/
public Float getSumdpthfhsc() {
return sumdpthfhsc;
}
/**
* 합계 3시간 신적설
*@param sumdpthfhsc 합계 3시간 신적설
*/
public void setSumdpthfhsc(Float sumdpthfhsc) {
	this.sumdpthfhsc = sumdpthfhsc;
}
/**
* 평균 전운량
* @return 평균 전운량
*/
public Float getAvgtca() {
return avgtca;
}
/**
* 평균 전운량
*@param avgtca 평균 전운량
*/
public void setAvgtca(Float avgtca) {
	this.avgtca = avgtca;
}
/**
* 평균 중하층운량
* @return 평균 중하층운량
*/
public Float getAvglmac() {
return avglmac;
}
/**
* 평균 중하층운량
*@param avglmac 평균 중하층운량
*/
public void setAvglmac(Float avglmac) {
	this.avglmac = avglmac;
}
/**
* 평균 지면온도
* @return 평균 지면온도
*/
public Float getAvgts() {
return avgts;
}
/**
* 평균 지면온도
*@param avgts 평균 지면온도
*/
public void setAvgts(Float avgts) {
	this.avgts = avgts;
}
/**
* 최저 초상온도
* @return 최저 초상온도
*/
public Float getMintg() {
return mintg;
}
/**
* 최저 초상온도
*@param mintg 최저 초상온도
*/
public void setMintg(Float mintg) {
	this.mintg = mintg;
}
/**
* 평균 5cm 지중온도
* @return 평균 5cm 지중온도
*/
public Float getAvgcm5te() {
return avgcm5te;
}
/**
* 평균 5cm 지중온도
*@param avgcm5te 평균 5cm 지중온도
*/
public void setAvgcm5te(Float avgcm5te) {
	this.avgcm5te = avgcm5te;
}
/**
* 평균10cm 지중온도
* @return 평균10cm 지중온도
*/
public Float getAvgcm10te() {
return avgcm10te;
}
/**
* 평균10cm 지중온도
*@param avgcm10te 평균10cm 지중온도
*/
public void setAvgcm10te(Float avgcm10te) {
	this.avgcm10te = avgcm10te;
}
/**
* 평균 20cm 지중온도
* @return 평균 20cm 지중온도
*/
public Float getAvgcm20te() {
return avgcm20te;
}
/**
* 평균 20cm 지중온도
*@param avgcm20te 평균 20cm 지중온도
*/
public void setAvgcm20te(Float avgcm20te) {
	this.avgcm20te = avgcm20te;
}
/**
* 평균 30cm 지중온도
* @return 평균 30cm 지중온도
*/
public Float getAvgcm30te() {
return avgcm30te;
}
/**
* 평균 30cm 지중온도
*@param avgcm30te 평균 30cm 지중온도
*/
public void setAvgcm30te(Float avgcm30te) {
	this.avgcm30te = avgcm30te;
}
/**
* 0.5m 지중온도
* @return 0.5m 지중온도
*/
public Float getAvgm05te() {
return avgm05te;
}
/**
* 0.5m 지중온도
*@param avgm05te 0.5m 지중온도
*/
public void setAvgm05te(Float avgm05te) {
	this.avgm05te = avgm05te;
}
/**
* 1.0m 지중온도
* @return 1.0m 지중온도
*/
public Float getAvgm10te() {
return avgm10te;
}
/**
* 1.0m 지중온도
*@param avgm10te 1.0m 지중온도
*/
public void setAvgm10te(Float avgm10te) {
	this.avgm10te = avgm10te;
}
/**
* 1.5m 지중온도
* @return 1.5m 지중온도
*/
public Float getAvgm15te() {
return avgm15te;
}
/**
* 1.5m 지중온도
*@param avgm15te 1.5m 지중온도
*/
public void setAvgm15te(Float avgm15te) {
	this.avgm15te = avgm15te;
}
/**
* 3.0m 지중온도
* @return 3.0m 지중온도
*/
public Float getAvgm30te() {
return avgm30te;
}
/**
* 3.0m 지중온도
*@param avgm30te 3.0m 지중온도
*/
public void setAvgm30te(Float avgm30te) {
	this.avgm30te = avgm30te;
}
/**
* 5.0m 지중온도
* @return 5.0m 지중온도
*/
public Float getAvgm50te() {
return avgm50te;
}
/**
* 5.0m 지중온도
*@param avgm50te 5.0m 지중온도
*/
public void setAvgm50te(Float avgm50te) {
	this.avgm50te = avgm50te;
}
/**
* 합계 대형증발량
* @return 합계 대형증발량
*/
public Float getSumlrgev() {
return sumlrgev;
}
/**
* 합계 대형증발량
*@param sumlrgev 합계 대형증발량
*/
public void setSumlrgev(Float sumlrgev) {
	this.sumlrgev = sumlrgev;
}
/**
* 합계 소형증발량
* @return 합계 소형증발량
*/
public Float getSumsmlev() {
return sumsmlev;
}
/**
* 합계 소형증발량
*@param sumsmlev 합계 소형증발량
*/
public void setSumsmlev(Float sumsmlev) {
	this.sumsmlev = sumsmlev;
}
/**
* 9-9강수
* @return 9-9강수
*/
public Float getN99rn() {
return n99rn;
}
/**
* 9-9강수
*@param n99rn 9-9강수
*/
public void setN99rn(Float n99rn) {
	this.n99rn = n99rn;
}
/**
* 일기현상
* @return 일기현상
*/
public String getIscs() {
return iscs;
}
/**
* 일기현상
*@param iscs 일기현상
*/
public void setIscs(String iscs) {
	this.iscs = iscs;
}
/**
* 안개 계속 시간
* @return 안개 계속 시간
*/
public Float getSumfogdur() {
return sumfogdur;
}
/**
* 안개 계속 시간
*@param sumfogdur 안개 계속 시간
*/
public void setSumfogdur(Float sumfogdur) {
	this.sumfogdur = sumfogdur;
}
}
