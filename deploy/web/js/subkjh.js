// "use strict"


function DateUtil() {

}

/**
YYYYMMDDHHMISS to Milliseconds
 */
DateUtil.makeDate = function(hstime) {
	var s = hstime + "";
	var year = parseInt(s.substring(0, 4));
	var month = parseInt(s.substring(4, 6));
	var day = parseInt(s.substring(6, 8));
	var hour = parseInt(s.substring(8, 10));
	var minute = parseInt(s.substring(10, 12));
	var second = parseInt(s.substring(12, 14));

	const d = new Date(year, month - 1, day, hour, minute,
		second);

	return d;

}

DateUtil.convert = function(hstime) {
	
	let  s = hstime + "";
	if ( s.length >= 8 ) {
		let ret =  s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);

		if ( s.length == 14 ) {
			ret += " ";
			ret += s.substring(8, 10) + ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
		}		 
		return ret; 
	} else {
		return s;
	}
}


/**
mstime : 추가할 시간(milliseconds 단위)
 */
DateUtil.getDtm = function(mstime) {

	function addZero(i) {
		if (i < 10) { i = "0" + i }
		return i;
	}

	const d = new Date();

	if (mstime != undefined) {
		d.setTime(d.getTime() + mstime);
	}

	let year = d.getFullYear();
	let month = d.getMonth() + 1;
	let day = d.getDate();
	let hour = d.getHours();
	let minutes = d.getMinutes();
	let seconds = d.getSeconds();

	return year + addZero(month) + addZero(day) + addZero(hour) + addZero(minutes) + addZero(seconds);
}


DateUtil.getDdHh = function(mstime, delimiter = ' ') {

	function addZero(i) {
		if (i < 10) { i = "0" + i }
		return i;
	}

	const d = new Date();
	d.setTime(mstime);

	let day = d.getDate();
	let hour = d.getHours();

	return addZero(day) + delimiter + addZero(hour);
}



DateUtil.getYmd = function(mstime) {

	function addZero(i) {
		if (i < 10) { i = "0" + i }
		return i;
	}

	const d = new Date();

	if (mstime != undefined) {
		d.setTime(d.getTime() + mstime);
		console.log(d.getTime());
	}


	let year = d.getFullYear();
	let month = d.getMonth() + 1;
	let day = d.getDate();

	return year + addZero(month) + addZero(day);
}

export { DateUtil };
