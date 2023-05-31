// "use strict"

/**
 * 
 */

class AlarmColor {
	static critical = '#ff6666';
	static major = '#FFB266';
	static minor = '#FFE5CC';
	static warning = '#66B2FF';
	static normal = '#000000';

	static getColor(level) {
		if (AlarmLevel.CRITICAL == level) {
			return AlarmColor.critical;
		} else if (AlarmLevel.MAJOR == level) {
			return AlarmColor.major;
		} else if (AlarmLevel.MINOR == level) {
			return AlarmColor.minor;
		} else if (AlarmLevel.WARNING == level) {
			return AlarmColor.warning;
		} else {
			return AlarmColor.normal;
		}
	}
}

class AlarmLevel {

	static CRITICAL = 1;
	static MAJOR = 2;
	static MINOR = 3;
	static WARNING = 4;
	static CLEAR = 9;

	static getName(level) {

		if (level == AlarmLevel.CRITICAL) {
			return "critical";
		} else if (level == AlarmLevel.MAJOR) {
			return "major";
		} else if (level == AlarmLevel.MINOR) {
			return "minor";
		} else if (level == AlarmLevel.WARNING) {
			return "warning";
		} else {
			return "normal";
		}
	}
}

class FxMS {

	constructor(userId, userPwd, callbackOnLogin) {
		this.speed = 0;
		this.jwt = null;
		this.userId = userId;
		this.userPwd = userPwd;
		this.host = "10.0.1.11";
		this.port = 10005;

		// FXMS-SESSIONID=FXMS1340828340010

		console.log(document.cookie);

		var THIS = this;

		if (callbackOnLogin) {
			this.login(function(isOk, result) {
				if (isOk && result.Authorization ) {
					THIS.jwt = result.Authorization;
					callbackOnLogin(result);
				}
			});
		}

	}

	/**
	로그인 여부 확인
	 */
	isLogined() {
		return this.jwt != null;
	}

	/**
	URL 호출
	 */
	call(path, para, callback) {

		const xhttp = new XMLHttpRequest();
		xhttp.onload = function() {
			if (callback != null) {

				if (xhttp.status == 200) {

					var resObj = JSON.parse(this.responseText);

					console.log(xhttp, resObj);

					// {"is-ok":"Y","result":[{"hh":"00"},{"hh":"01"}, .. 결과가 목록으로 제공됨],"session-id":"FXMS1340921950007"}
					// {"is-ok":"N","errmsg":"Session Timeout : FXMS1340921950009","session-id":"FXMS1340921950009"}

					if (resObj['is-ok'] == 'Y') {
						callback(true, resObj.result);
					} else {
						callback(false, resObj.errmsg);
					}
				} else {
					callback(false, null);
				}
			}
		}

		let url = this.makeUrl(path, {});
		var sendStr = JSON.stringify(para);
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Authorization", this.jwt);
		xhttp.send(sendStr);

		console.log(url, para, sendStr);

		//let url = this.makeUrl(path, para);
		//xhttp.open("GET", url, true);
		//xhttp.send();
	}

	login(callback) {
		this.call("login", { 'user-id': this.userId, 'password': this.userPwd }, callback);
	}

	logout(callback) {
		this.call("user/logout", {}, callback);
	}


	makeUrl(path, para) {

		var url = "http://" + this.host + ':' + this.port + '/' + path;

		var keys = Object.keys(para);
		for (var i = 0; i < keys.length; i++) {
			if (i == 0) {
				url += '?';
			} else {
				url += '&';
			}
			url += keys[i] + '=' + para[keys[i]];
		}

		return url;
	}

	getPsItem(psId, callback) {

		if (this.psItemMap) {

			if (callback) {
				callback(this.psItemMap.get(psId));
			} else {
				return this.psItemMap.get(psId);
			}

		} else {
			var psItemMap = new Map();
			this.call('ps/select-ps-item-list', {}, function(isOk, result) {
				if (isOk) {
					for (var i = 0; i < result.length; i++) {
						psItemMap.set(result[i].psId, result[i]);
					}
					callback(psItemMap.get(psId));
				}
			});
			this.psItemMap = psItemMap;
		}

		return null;
	}

}

/**
실시간으로 알람의 발생, 해제를 받는 클래스
 */
class AlarmReceiver {

	/**
	 */
	constructor(fxms, userId, userPwd) {
		this.fxms = fxms;
		this.userId = userId;
		this.userPwd = userPwd;
		this.ws = null;
		this.callbackList = [];
		this.alarmHstList = [];

		// 알람번호 기준으로 알람 정보 보유함.
		this.alarmMap = new Map();
	}

	addCallback(callback) {
		this.callbackList.push(callback);
	}

	/**
	현재 발생된 알람을 가져온다.
	 */
	loadAlarm() {

		var THIS = this;

		this.fxms.call('alarm/select-alarm-cur-grid-list', {

		}, function(isOk, result) {
			if (isOk) {
				THIS.setAlarmList(result);
			} else {
				alert(result);
			}
		});
	}

	/**
	알람 목록 설정
	 */
	setAlarmList(alarmList) {
		if (alarmList) {
			for (var i = 0; i < alarmList.length; i++) {
				this.addAlarm(alarmList[i]);
			}
		}
	}

	/**
	알람을 맵에 넣거나 맵에서 제거한다.
	 */
	addAlarm(alarm) {

		console.log((alarm.rlseDtm == 0 ? '발생' : '해제') + ' ' + alarm.alarmNo + ' ' + alarm.alarmKey, alarm);

		// 발생
		if (alarm.rlseDtm == 0) {

			// 알람 목록
			this.alarmMap.set(alarm.alarmNo, alarm);


			this.alarmHstList.push(alarm);
			if (this.alarmHstList.length > 100) {
				this.alarmHstList.splice(0, 1);
			}
		}

		// 해제
		else {

			var old = this.alarmMap.get(alarm.alarmNo);
			if (old != null) {
				old.rlseDtm = alarm.rlseDtm;
			}
			this.alarmMap.delete(alarm.alarmNo);

		}
	}

	/**
	입력된 관리대상의 최고 알람 등급을 조회한다.
	 */
	getAlarmLevel(moNo) {

		var level = AlarmLevel.CLEAR;
		var alarmList = this.getAlarmList(moNo);
		var alarm;
		for (var i = 0; i < alarmList.length; i++) {
			alarm = alarmList[i];
			if (alarm.alarmLevel < level) {
				level = alarm.alarmLevel;
			}
		}

		// console.log("alarm-size=" + alarmList.length + ", alarm-level=" + level);

		return level;
	}

	/**
	입력된 관리대상의 알람을 조회한다.
	 */
	getAlarmList(moNo) {

		var list = [];
		var alarmList = this.getAlarmAllList();
		var alarm;
		for (var i = 0; i < alarmList.length; i++) {
			alarm = alarmList[i];
			if (alarm.moNo == moNo) {
				list.push(alarm);
			}
		}

		return list;
	}

	open(callback) {

		var THIS = this;

		this.fxms.call('system/select-var-list', { varGrpName: 'alarm' }, function(isOk, result) {
			if (isOk) {
				var host = null;
				var port = null;
				for (var i = 0; i < result.length; i++) {
					if (result[i].varName == 'fxms-alarm-broadcaster') {
						host = result[i].varVal;
					} else if (result[i].varName == 'fxms-alarm-broadcaster-port') {
						port = result[i].varVal;
					}
				}
				THIS.connect(host, port, callback);
			}
		});

	}

	connect(host, port, callback) {

		this.host = host;
		this.port = port;

		console.log('AlarmReceiver ' + this.host + ':' + this.port);
		if (this.host == null || this.port == null) {
			alert('연결 정보가 없습니다.');
			callback('error', this.host + ':' + this.port);
			return;
		}

		var THIS = this;
		var ws = new WebSocket("ws://" + this.host + ':' + this.port);
		this.ws = ws;

		// websocket 서버에 연결되면 연결 메시지를 화면에 출력한다.
		ws.onopen = function(e) {

			for (let callback of THIS.callbackList) {
				callback('opened', e);
			}

			// 연결 후 로그인 정보를 보낸다.
			var map = { userId: THIS.userId, userPwd: THIS.userPwd };
			var sendStr = JSON.stringify(map);
			THIS.ws.send(sendStr);

			// 현재 발생된 알람을 가져온다.
			THIS.loadAlarm();

		};

		// websocket 에서 수신한 메시지를 화면에 출력한다.
		ws.onmessage = function(e) {

			var recvObj = JSON.parse(e.data);

			console.log(recvObj);

			if (recvObj.eventType == 'Alarm') {
				THIS.addAlarm(recvObj);
			}

			for (let callback of THIS.callbackList) {
				callback(recvObj, e);
			}

		};

		// websocket 세션이 종료되면 화면에 출력한다.
		ws.onclose = function(e) {
			for (let callback of THIS.callbackList) {
				callback('closed', e);
			}
		}
	}


	/**
	 * 현재 알람 목록을 조회한다.
	 */
	getAlarmAllList() {
		return Array.from(this.alarmMap.values());
	}

	getAlarmHstList() {
		return this.alarmHstList;
	}


}

/**
 수집한 값을 실시간으로 엿보는 클래스
  */
class ValuePeeker {

	constructor(fxms, userId, userPwd) {
		this.fxms = fxms;
		this.userId = userId;
		this.userPwd = userPwd;
		this.ws = null;
		this.reqMap = new Map();
	}

	getValueList(moNo, psId) {
		let item = this.getItem(moNo, psId)
		if (item == null) {
			return null;
		}
		return item.values;
	}


	getItem(moNo, psId) {
		let key = this.getKey(moNo, psId);
		return this.reqMap.get(key);
	}

	makeNewItem(moNo, psId) {

		let key = this.getKey(moNo, psId);
		let item = {};

		item.key = key;
		item.values = [];
		item.callbacks = [];
		this.reqMap.set(key, item);
		return item;
	}

	getKey(moNo, psId) {
		let key = moNo + ':' + psId;
		return key;
	}

	addPeek(moNo, psId, callback) {

		let item = this.getItem(moNo, psId)
		if (item == null) {
			item = this.makeNewItem(moNo, psId);
			let sendStr = JSON.stringify({ action: 'add', moNo: moNo, psId: psId });
			this.ws.send(sendStr);
		}

		item.callbacks.push(callback);
	}

	removePeek(moNo, psId, callback) {

		let item = this.getItem(moNo, psId)
		if (item == null) {
			return;
		}

		var sendStr = JSON.stringify({ action: 'remove', moNo: moNo, psId: psId });
		this.ws.send(sendStr);
	}


	open(callback) {

		let THIS = this;

		this.fxms.call('system/select-var-list', { varGrpName: 'value' }, function(isOk, result) {
			if (isOk) {
				var host = null;
				var port = null;
				for (var i = 0; i < result.length; i++) {
					if (result[i].varName == 'fxms-value-peeker') {
						host = result[i].varVal;
					} else if (result[i].varName == 'fxms-value-peeker-port') {
						port = result[i].varVal;
					}
				}
				THIS.connect(host, port, callback);
			}
		});
	}

	connect(host, port, callback) {

		this.host = host;
		this.port = port;

		console.log('ValuePeeker ' + host + ':' + port);

		if (host == null || port == null) {
			alert('연결 정보가 없습니다.');
			this.callback('error', host + ':' + port);
			return;
		}

		var THIS = this;
		var ws = new WebSocket("ws://" + host + ':' + port);
		this.ws = ws;

		// websocket 서버에 연결되면 연결 메시지를 화면에 출력한다.
		ws.onopen = function(e) {
			callback('opened', e);

			var map = { userId: THIS.userId, userPwd: THIS.userPwd };
			var sendStr = JSON.stringify(map);

			THIS.ws.send(sendStr);
		};

		// websocket 에서 수신한 메시지를 화면에 출력한다.
		ws.onmessage = function(e) {
			var recvObj = JSON.parse(e.data);

			if (recvObj.eventType == 'SessionVo') {
				callback(recvObj, e);
				return;
			}

			if (recvObj.moNo != undefined) {

				let item = THIS.getItem(recvObj.moNo, recvObj.psId);

				if (item != null) {
					let data = { date: recvObj.date, value: recvObj.value };
					item.values.push(data);
					for (let callback of item.callbacks) {
						callback(recvObj);
					}

					// 보관 데이터가 많으면 삭제한다.
					if (item.values.length > 1000) {
						item.values.splice(0, 1);
					}
				}

			}

		};

		// websocket 세션이 종료되면 화면에 출력한다.
		ws.onclose = function(e) {
			callback('closed', e);
		}
	}

	close() {
		if (this.ws != null) {
			this.ws.close(0);
		}
	}
}

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


class FxmsGojs {

	/**
	div에 대응하는 하나의 FxmsGojs가 필요하다.
	 */
	constructor(div) {
		this.goDiagram = null;
		this.div = div;
	}

	getNodeList() {
		return this.goDiagram.model.nodeDataArray;
	}

	getLinkList() {
		return this.goDiagram.model.linkDataArray;
	}

	makeProcessFlow(nodeDataArray, linkDataArray) {

		if (this.goDiagram == null) {
			this.goDiagram = this.init(this.div);
		}

		var model = new go.GraphLinksModel({
			nodeDataArray: nodeDataArray,
			linkDataArray: linkDataArray
		});

		this.goDiagram.model = model;


		// 모델이 추가된 후에 아래 내용이 진행되어야 한다.

		// Animate the flow in the pipes
		var animation = new go.Animation();
		animation.easing = go.Animation.EaseLinear;
		this.goDiagram.links.each(link => animation.add(link.findObject("PIPE"), "strokeDashOffset", 20, 0));

		// Run indefinitely
		animation.runCount = Infinity;
		animation.start();

	}

	destory() {
		if (this.goDiagram != null) {
			this.goDiagram.div = null;
			this.goDiagram = null;
		}
	}

	init(div) {


		// Since 2.2 you can also author concise templates with method chaining instead of GraphObject.make
		// For details, see https://gojs.net/latest/intro/buildingObjects.html
		const $ = go.GraphObject.make;  // for more concise visual tree definitions

		const myDiagram = $(go.Diagram, div,
			{
				"grid.visible": false,
				"grid.gridCellSize": new go.Size(30, 20),
				"draggingTool.isGridSnapEnabled": true,
				"resizingTool.isGridSnapEnabled": true,
				"rotatingTool.snapAngleMultiple": 90,
				"rotatingTool.snapAngleEpsilon": 45,
				"undoManager.isEnabled": true
			});


		var process = $(go.Node, "Auto",
			{
				locationSpot: new go.Spot(0.5, 0.5), locationObjectName: "SHAPE",
				resizable: true, resizeObjectName: "SHAPE"
			},
			new go.Binding("location", "pos", go.Point.parse).makeTwoWay(go.Point.stringify),
			$(go.Shape, "Cylinder1",
				{
					name: "SHAPE",
					strokeWidth: 2,
					fill: $(go.Brush, "Linear",
						{
							start: go.Spot.Left, end: go.Spot.Right,
							0: "gray", 0.5: "white", 1: "gray"
						}),
					minSize: new go.Size(50, 50),
					portId: "", fromSpot: go.Spot.AllSides, toSpot: go.Spot.AllSides
				},
				new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
			$(go.TextBlock,
				{
					alignment: go.Spot.Center, textAlign: "center", margin: 5,
					editable: true
				},
				new go.Binding("text").makeTwoWay())
		);

		var valve = $(go.Node, "Vertical",
			{
				locationSpot: new go.Spot(0.5, 1, 0, -21), locationObjectName: "SHAPE",
				selectionObjectName: "SHAPE", rotatable: true
			},
			new go.Binding("angle").makeTwoWay(),
			new go.Binding("location", "pos", go.Point.parse).makeTwoWay(go.Point.stringify),
			$(go.TextBlock,
				{ alignment: go.Spot.Center, textAlign: "center", margin: 5, editable: true },
				new go.Binding("text").makeTwoWay(),
				// keep the text upright, even when the whole node has been rotated upside down
				new go.Binding("angle", "angle", a => a === 180 ? 180 : 0).ofObject()),
			$(go.Shape,
				{
					name: "SHAPE",
					geometryString: "F1 M0 0 L40 20 40 0 0 20z M20 10 L20 30 M12 30 L28 30",
					strokeWidth: 2,
					fill: $(go.Brush, "Linear", { 0: "gray", 0.35: "white", 0.7: "gray" }),
					portId: "", fromSpot: new go.Spot(1, 0.35), toSpot: new go.Spot(0, 0.35)
				})
		);

		var shape = $(go.Node, "Auto",
			{
				locationSpot: new go.Spot(0.5, 0.5), locationObjectName: "SHAPE",
				resizable: true, resizeObjectName: "SHAPE"
			},
			new go.Binding("location", "pos", go.Point.parse).makeTwoWay(go.Point.stringify),
			$(go.Shape, "Customer/Supplier",
				{
					name: "SHAPE",
					strokeWidth: 2,
					fill: $(go.Brush, "Linear",
						{
							start: go.Spot.Left, end: go.Spot.Right,
							0: "gray", 0.5: "white", 1: "gray"
						}),
					minSize: new go.Size(50, 50),
					portId: "", fromSpot: go.Spot.AllSides, toSpot: go.Spot.AllSides
				},
				new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
			$(go.TextBlock,
				{
					alignment: go.Spot.Center, textAlign: "center", margin: 5,
					editable: true
				},
				new go.Binding("text").makeTwoWay())
		);

		// A minimal Diagram
		myDiagram.nodeTemplate =
			$(go.Node, "Auto",
				$(go.Shape, "RoundedRectangle",
					new go.Binding("fill", "color")),
				$(go.TextBlock,
					{ margin: 3, font: '28px sans-serif' },  // some room around the text
					new go.Binding("text", "key"))
			);

		myDiagram.nodeTemplateMap.add("Process", process);
		myDiagram.nodeTemplateMap.add("Valve", valve);
		myDiagram.nodeTemplateMap.add("Shape", shape);

		myDiagram.linkTemplate =
			$(go.Link,
				{ routing: go.Link.AvoidsNodes, curve: go.Link.JumpGap, corner: 10, reshapable: true, toShortLength: 7 },
				new go.Binding("points").makeTwoWay(),
				// mark each Shape to get the link geometry with isPanelMain: true
				$(go.Shape, { isPanelMain: true, stroke: "black", strokeWidth: 7 }),
				$(go.Shape, { isPanelMain: true, stroke: "gray", strokeWidth: 5 }),
				$(go.Shape, { isPanelMain: true, stroke: "white", strokeWidth: 3, name: "PIPE", strokeDashArray: [10, 10] }),
				$(go.Shape, { toArrow: "Triangle", scale: 1.3, fill: "gray", stroke: null })
			);


		return myDiagram;
	}

}

class FxmsDiagram {

	constructor(fxms) {
		this.fxms = fxms;
	}


	addNew(diagTitle, callback) {
		var bas = {
			diagTitle: diagTitle,
			opId: 'ui-input-diagram'
		};

		this.fxms.call('diagram/insert-diagram', bas, function(isOk, result) {
			let retObj = result;
			if (isOk) {
				retObj = result.diagNo;
			}
			callback(isOk, retObj);
		});

	}

	save(diagNo, nodeDataArray, linkDataArray, callback) {

		var nodeList = [];
		var linkList = [];
		var obj;
		var nodeNo = 1;
		var pos;
		var width, height;

		var nodeMap = new Map();

		console.log(diagNo, nodeDataArray, linkDataArray);

		for (var i = 0; i < nodeDataArray.length; i++) {
			obj = nodeDataArray[i];


			try {
				pos = obj.size.split(' ');
				width = parseInt(pos[0]);
				height = parseInt(pos[1]);
			} catch (e) {
				width = 0;
				height = 0;
			}

			pos = obj.pos.split(' ');

			var node = {
				diagNo: diagNo,
				diagNodeNo: nodeNo,
				diagNodeKey: obj.key,
				diagNodeTypeCd: obj.category,
				diagNodeText: obj.text,
				diagNodeX: parseInt(pos[0]),
				diagNodeY: parseInt(pos[1]),
				diagNodeAngle: (obj.angle == undefined ? 0 : obj.angle),
				diagNodeWidth: width,
				diagNodeHeight: height,
				diagNodeJson : {}
			}
			nodeList.push(node);

			// 노드에 대한 관리번호 보관
			nodeMap.set(obj.key, nodeNo);

			if (obj.fxAttrName) {
				node.diagNodeJson[obj.fxAttrName] = obj.fxAttrValue;
			}

			nodeNo++;
		}

		for (var i = 0; i < linkDataArray.length; i++) {
			obj = linkDataArray[i];
			var link = {
				diagNo: diagNo,
				diagNodeNo: nodeNo,
				linkDiagNodeNo1: nodeMap.get(obj.from), // 노드에 대한 관리번호 지정
				linkDiagNodeNo2: nodeMap.get(obj.to), // 노드에 대한 관리번호 지정
				linkDiagNodeJson: { points : obj.points }
			}

			// 링크 양쪽에 노드가 존재하면 추가한다.
			if (link.linkDiagNodeNo1 && link.linkDiagNodeNo2) {
				linkList.push(link);
				nodeNo++;
			}
		}

		console.log(nodeList, linkList);
		var bas = {
			diagNo: diagNo,
			nodeList: nodeList,
			lineList: linkList
		};

		this.fxms.call('diagram/set-diagram-config', bas, function(isOk, result) {
			callback(isOk, result);
		});


		//DIAG_NO
		//DIAG_NODE_NO
		//DIAG_NODE_TYPE_CD
		//DIAG_NODE_TEXT
		//DIAG_NODE_X
		//DIAG_NODE_Y
		//DIAG_NODE_WDTH
		//DIAG_NODE_HGHT
		//DIAG_NODE_ANGLE

		//DIAG_NO
		//DIAG_NODE_NO
		//ATTR_NAME
		//ATTR_VAL

		//DIAG_NO
		//DIAG_NODE_NO
		//LINK_DIAG_NODE_NO1
		//LINK_DIAG_NODE_NO2
		//LINK_DIAG_POINTS

		//category: "Process"
		//key: "P1"
		//pos: "60 40"
		//text: "Process"



		//{"from":"P1","to":"V1","points":[-122.008544921875,-40,-112.008544921875,-40,63.4957275390625,-40,63.4957275390625,120.2,239,120.2,249,120.2]},


	}

	selectDiagramConf(diagNo, fxCallback) {

		var diagramInfo = {};

		this.fxms.call('diagram/get-diagram-config', {
			diagNo: diagNo
		}, function(isOk, result) {
			if (isOk == false) {
				fxCallback(false, result);
				return;
			}


			diagramInfo.nodeList = result.nodeList;
			diagramInfo.lineList = result.lineList;

			var obj;
			var node;
			var nodeMap = new Map();

			// 노드 맵에 보관
			for (var i = 0; i < diagramInfo.nodeList.length; i++) {
				node = diagramInfo.nodeList[i];
				nodeMap.set(node.diagNodeNo, node);
			}


			diagramInfo.nodeDataArray = [];
			for (var i = 0; i < diagramInfo.nodeList.length; i++) {
				node = diagramInfo.nodeList[i];
				obj = {
					key: node.diagNodeKey,
					category: node.diagNodeTypeCd,
					pos: node.diagNodeX + ' ' + node.diagNodeY,
					text: node.diagNodeText,
					angle: node.diagNodeAngle
				}
				diagramInfo.nodeDataArray.push(obj);

			}

			diagramInfo.linkDataArray = [];
			for (var i = 0; i < diagramInfo.lineList.length; i++) {
				node = diagramInfo.lineList[i];
				var from = nodeMap.get(node.linkDiagNodeNo1);
				var to = nodeMap.get(node.linkDiagNodeNo2);
				if (from && to) {
					obj = {
						from: from.diagNodeKey,
						to: to.diagNodeKey
					}
					diagramInfo.linkDataArray.push(obj);
				}
			}

			//diagNo: 1
			//diagNodeAngle: 0
			//diagNodeHght: 0
			//diagNodeNo: 0
			//diagNodeKey : 
			//diagNodeText: "Process"
			//diagNodeTypeCd: "Process"
			//diagNodeX: 150
			//diagNodeY: 120

			//chgDtm: 20220620161738
			//chgUserNo: 106
			//diagNo: 1
			//diagNodeNo: 12
			//linkDiagNodeNo1: 0
			//linkDiagNodeNo2: 0
			//linkDiagPoints: "{__gohashid=1204.0, Ka=null, w=true, Mg=null, m=[{x=424.0, y=420.0, w=true}, {x=414.0, y=420.0, w=true}, {x=357.5, y=420.0, w=true}, {x=357.5, y=419.8, w=true}, {x=301.0, y=419.8, w=true}, {x=291.0, y=419.8, w=true}], Da=8.0}"
			//regDtm: 20220620161738
			//regUserNo: 106


			// { "key": "V2", "category": "Valve", "pos": "150 280", "text": "VM", "angle": 270 },
			// { "from": "P1", "to": "V1" },


			fxCallback(true, diagramInfo);
		});
	}

	/**
	 * 입력된 div에 diagNo에 해당되는 다이아그램을 그리고 결과를 callback에 통보한다.
	 */
	showDiagram(div, diagNo, callback) {

		console.log('showDiagram', div, diagNo);

		this.selectDiagramConf(diagNo, function(isOk, diagramObj) {

			let retObj = diagramObj;

			if (isOk) {
				let gojs = new FxmsGojs(div);
				gojs.makeProcessFlow(diagramObj.nodeDataArray, diagramObj.linkDataArray);
				retObj = gojs;
			}

			if (callback) {
				callback(isOk, retObj);
			}

		});

	}
}


class FxmsPipe {

	constructor(fxms) {
		this.fxms = fxms;
		this.gojs = null;
	}


	selectPipePath(pipeId, callback) {

		var diagramInfo = {};

		this.fxms.call('vup/get-pipe-path', {
			pipeId: pipeId
		}, function(isOk, result) {

			if (isOk == false) {
				callback(false, result);
				return;
			}

			diagramInfo.pipeInfo = result.pipeInfo;
			diagramInfo.itemList = result.itemList;
			diagramInfo.linkList = result.linkList;

			var obj;
			var node;

			function getKey(node) {
				return node.facName.length == 0 ? node.moName : node.facName;
			}
			function getNodeClass(node) {
				if (node.facNo > 0) {
					return 'FACILITY';
				} else if (node.moNo > 0) {
					return 'SENSOR';
				} else {
					return 'TEXT';
				}
			}
			function getNodeAttrName(node) {
				if (node.facNo > 0) {
					return 'FAC_NO';
				} else if (node.moNo > 0) {
					return 'MO_NO';
				} else {
					return null;
				}
			}
			function getNodeAttrValue(node) {
				if (node.facNo > 0) {
					return node.facNo;
				} else if (node.moNo > 0) {
					return node.moNo;
				} else {
					return -1;
				}
			}

			function getCategory(node) {
				var nodeClass = getNodeClass(node);
				if (nodeClass == 'FACILITY') return 'Process';
				else if (nodeClass == 'SENSOR') return 'Valve';
				return 'Shape';
			}
			function getAngle(node) {
				getNodeClass(node) == 'MO' ? 180 : 0;
			}

			diagramInfo.nodeDataArray = [];
			for (var i = 0; i < diagramInfo.itemList.length; i++) {
				node = diagramInfo.itemList[i];

				obj = {
					key: getKey(node),
					category: getCategory(node),
					// pos: node.diagNodeX + ' ' + node.diagNodeY,
					text: node.facName.length == 0 ? node.moName : node.facName,
					angle: getAngle(node),
					fxAttrName: getNodeAttrName(node),
					fxAttrValue: getNodeAttrValue(node)
				}
				diagramInfo.nodeDataArray.push(obj);

			}

			diagramInfo.linkDataArray = [];
			for (var i = 0; i < diagramInfo.linkList.length; i++) {
				node = diagramInfo.linkList[i];
				obj = {
					from: getKey(node.startItem),
					to: getKey(node.endItem)
				}
				diagramInfo.linkDataArray.push(obj);
			}

			//diagNo: 1
			//diagNodeAngle: 0
			//diagNodeHght: 0
			//diagNodeNo: 0
			//diagNodeKey : 
			//diagNodeText: "Process"
			//diagNodeTypeCd: "Process"
			//diagNodeX: 150
			//diagNodeY: 120

			//chgDtm: 20220620161738
			//chgUserNo: 106
			//diagNo: 1
			//diagNodeNo: 12
			//linkDiagNodeNo1: 0
			//linkDiagNodeNo2: 0
			//linkDiagPoints: "{__gohashid=1204.0, Ka=null, w=true, Mg=null, m=[{x=424.0, y=420.0, w=true}, {x=414.0, y=420.0, w=true}, {x=357.5, y=420.0, w=true}, {x=357.5, y=419.8, w=true}, {x=301.0, y=419.8, w=true}, {x=291.0, y=419.8, w=true}], Da=8.0}"
			//regDtm: 20220620161738
			//regUserNo: 106


			// { "key": "V2", "category": "Valve", "pos": "150 280", "text": "VM", "angle": 270 },
			// { "from": "P1", "to": "V1" },

			console.log("selectPipePath", diagramInfo);

			callback(true, diagramInfo);
		});
	}

	setPipeDiagNo(pipeId, diagNo, callback) {
		var bas = {
			pipeId: pipeId,
			diagNo: diagNo
		};

		this.fxms.call('vup/update-pipe-diag-no', bas, function(isOk, result) {
			if (isOk) {
				callback(true);
			} else {
				callback(false, result);
			}
		});
	}

	/**
	 * 배관 내역을 그린다.
	 * pipeId : 그릴 배관ID
	 * div : div element
	 * callback : 처리 결과 통보
	 * pipeObj : 그릴 배관 정보
	 */
	drawPipe(div, pipeId, pipeObj, callback) {

		if (this.gojs != null) {
			this.gojs.destory();
			this.gojs = null;
		}

		var THIS = this;
		var diagram = new FxmsDiagram(THIS.fxms);


		// 다이아그램과 연결된 정보가 있으면 해당 내용을 그리고 종료한다.
		if (pipeObj && pipeObj.diagNo > 0) {
			diagram.showDiagram(div, pipeObj.diagNo, function(isOk, retObj) {
				if (isOk) {
					THIS.gojs = retObj;
					callback(true, pipeObj);
				} else {
					callback(false, retObj);
				}
			});
			return;
		}

		// 배관 연결 정보를 가져온다.
		this.selectPipePath(pipeId, function(isOk, pipe) {

			if (isOk) {

				var pipeObj = pipe.pipeInfo;

				// 다이아그램이 설정되어 있으면 다이아그램을 보인다.
				if (pipeObj.diagNo > 0) {

					// 설정된 다이아그램을 조회한다.
					diagram.showDiagram(div, pipeObj.diagNo, function(isOk, retObj) {
						if (isOk) {
							THIS.gojs = retObj;
						}
					});

				}
				// 다이아그램이 설정되어 있지 않으면 노드와 링크가 편집되지 않은 상태로 보인다.
				else {
					THIS.gojs = new FxmsGojs(div);
					THIS.gojs.makeProcessFlow(pipe.nodeDataArray, pipe.linkDataArray);
					callback(true, pipeObj);
				}

			} else {
				callback(false, pipe);
			}

		});
	}

	/**
	 * 배관 다이아그램을 기록한다.
	 */
	saveDiagram(pipeInfo, callback) {

		var THIS = this;
		var diag = new FxmsDiagram(this.fxms);

		if (pipeInfo.diagNo <= 0) {

			// 배관과 연결된 다이아그램이 없으면 등록한다.
			diag.addNew(pipeInfo.pipeName, function(isOk, diagNo) {

				if (isOk) {
					// 배관에 다이아그램 ID 설정
					THIS.setPipeDiagNo(pipeInfo.pipeId, diagNo,
						function(isOk, msg) {
							if (isOk) {
								// 다이이그램 내용 기록
								diag.save(diagNo, THIS.gojs.getNodeList(), THIS.gojs.getLinkList(), callback);
							} else {
								alert(msg);
							}
						});
				}
			});

		} else {
			diag.save(pipeInfo.diagNo, THIS.gojs.getNodeList(), THIS.gojs.getLinkList(), callback);
		}
	}

}

class FxmsHighChart {

	constructor(fxms, callback) {

		this.fxms = fxms;
		this.psItemMap = null;

		// moNo+psId = recvCall
		this.peekMap = new Map();

		// fxms 생성하고 성능 목록이 조회되면 set 함수 호출
		var THIS = this;
		this.fxms.call('ps/select-ps-item-list', {}, function(isOk, result) {
			if (isOk == false) {
				return;
			}

			var psItemMap = new Map();
			for (var i = 0; i < result.length; i++) {
				psItemMap.set(result[i].psId, result[i]);
			}
			THIS.psItemMap = psItemMap;

			callback();
		});

	}

	getPsItem(psId) {

		if (this.psItemMap) {
			return this.psItemMap.get(psId);
		}

		return null;
	}

	/**
	성능항목을 가져온다.
	 */
	setRtInfo(moNo, psId, psItemCallback, valueCallback) {

		// 성능항목 가져오기
		let psItem = this.psItemMap.get(psId);
		if (psItem == null) {
			alert(psId + ' not found');
			return;
		}

		// 챠트를 그릴수 있도록 callback 호출
		psItemCallback(psItem);

		// 이미 동일한 내용으로 요청이 되어 있으면 callback 함수만 등록하고 리턴한다.		
		if (list.length > 1) {
			return;
		}

		// 엿보기 성능 추가하기
		fxValuePeeker.addPeek(moNo, psId, valueCallback);

	}

	drawRtLine(valuePeeker, div, moNo, psId) {

		let psItem = this.psItemMap.get(psId);
		if (psItem == null) {
			// TODO
			return;
		}

		// 차트를 그린다.

		let chart = FxmsHighChart.drawLineChart(div, psItem);

		valuePeeker.addPeek(moNo, psId, function(recvObj) {

			console.log('drawRtLine', recvObj);

			// 수집값을 보여준다.
			if (chart) {
				try {
					var date = DateUtil.makeDate(recvObj.date);
					var series = chart.series[0];
					series.addPoint(
						[date.getTime(), recvObj.value],
						true, true);


				} catch (e) {
					console.log(e);
				}
			}
		});
	}

	drawRtGuage(valuePeeker, div, moNo, psId) {

		let psItem = this.psItemMap.get(psId);
		if (psItem == null) {
			// TODO
			return;
		}

		// 차트를 그린다.
		let chart = FxmsHighChart.drawGuage(div, psItem);

		valuePeeker.addPeek(moNo, psId, function(recvObj) {

			// 수집값을 보여준다.
			if (chart) {
				try {
					var point = chart.series[0].points[0]
					point.update(recvObj.value);
				} catch {
				}
			}
		});

	}

	static drawGuage(div, psItem) {

		console.log(psItem);

		let plotBands = [];
		let num = psItem.maxVal - psItem.minVal;
		let war = parseInt(num * .5 + psItem.minVal);
		let min = parseInt(num * .6 + psItem.minVal);
		let maj = parseInt(num * .7 + psItem.minVal);
		let cri = parseInt(num * .8 + psItem.minVal);

		function add(from, to, color) {
			return {
				from: from,
				to: to,
				color: color
			};
		}

		plotBands.push(add(war, min, AlarmColor.warning));
		plotBands.push(add(min, maj, AlarmColor.minor));
		plotBands.push(add(maj, cri, AlarmColor.major));
		plotBands.push(add(cri, psItem.maxVal, AlarmColor.critical));

		var chart = Highcharts.chart(div, {

			chart: {
				type: 'gauge',
				plotBackgroundColor: null,
				plotBackgroundImage: null,
				plotBorderWidth: 0,
				plotShadow: false
			},

			title: {
				//					text : 'Speedometer'
				text: psItem.psName
			},

			pane: {
				startAngle: -150,
				endAngle: 150,
				background: [{
					backgroundColor: {
						linearGradient: {
							x1: 0,
							y1: 0,
							x2: 0,
							y2: 1
						},
						stops: [[0, '#FFF'], [1, '#333']]
					},
					borderWidth: 0,
					outerRadius: '109%'
				}, {
					backgroundColor: {
						linearGradient: {
							x1: 0,
							y1: 0,
							x2: 0,
							y2: 1
						},
						stops: [[0, '#333'], [1, '#FFF']]
					},
					borderWidth: 1,
					outerRadius: '107%'
				}, {
					// default background
				}, {
					backgroundColor: '#DDD',
					borderWidth: 0,
					outerRadius: '105%',
					innerRadius: '103%'
				}]
			},

			// the value axis
			yAxis: {
				min: psItem.minVal,
				max: psItem.maxVal,

				minorTickInterval: 'auto',
				minorTickWidth: 1,
				minorTickLength: 10,
				minorTickPosition: 'inside',
				minorTickColor: '#666',

				tickPixelInterval: 30,
				tickWidth: 2,
				tickPosition: 'inside',
				tickLength: 10,
				tickColor: '#666',
				labels: {
					step: 2,
					rotation: 'auto'
				},
				title: {
					text: psItem.psUnit
				},
				plotBands: plotBands
			},

			series: [{
				name: psItem.psName,
				data: [0],
				tooltip: {
					valueSuffix: ' ' + psItem.psUnit
				}
			}]

		});

		return chart;
	}

	/**
	 * HighChart를 이용하여 라인차트를 생성한다.
	 */
	static drawLineChart(div, psItem) {

		// datas는 [ [ 일자, 값], [일자, 값] ... ] 형식이다.

		let chart = Highcharts.chart(
			div,
			{
				chart: {
					zoomType: 'x'
				},
				title: {
					text: psItem.psName
				},
				subtitle: {
					text: document.ontouchstart === undefined ? 'Click and drag in the plot area to zoom in'
						: 'Pinch the chart to zoom in'
				},
				xAxis: {
					type: 'datetime'
				},
				yAxis: {
					title: {
						text: psItem.psUnit
					}
				},
				legend: {
					enabled: false
				},
				plotOptions: {
					area: {
						fillColor: {
							linearGradient: {
								x1: 0,
								y1: 0,
								x2: 0,
								y2: 1
							},
							stops: [
								[
									0,
									Highcharts
										.getOptions().colors[0]],
								[
									1,
									Highcharts
										.color(
											Highcharts
												.getOptions().colors[0])
										.setOpacity(
											0)
										.get(
											'rgba')]]
						},
						marker: {
							radius: 2
						},
						lineWidth: 1,
						states: {
							hover: {
								lineWidth: 1
							}
						},
						threshold: null
					}
				},

				series: [{
					type: 'area',
					name: psItem.psName,
					data: (function() {
						var datas = [];
						var time = new Date().getTime();
						for (var i = -999; i <= 0; i++) {
							datas.push([time + i * 1000, 0]);
						}
						return datas;
					}())
				}]
			});

		return chart;

	}

	drawChartSpline(div, title, subtitle, psId, psDataCd, statFunc, startDtm, endDtm) {

		this.fxms.call('ps/select-ps-value-list', {
			moNo: 0,
			psId: psId,
			psDataCd: psDataCd,
			psStatFuncList: statFunc,
			startPsDate: startDtm,
			endPsDate: endDtm
		},
			function(isOk, result) {

				console.log(result);

				if (isOk) {

					var data;
					var series = [];
					var valueList;
					var psItem;
					var minDtm = 99999999000000;
					var pointInterval = 300000;



					for (var n = 0; n < result.length; n++) {

						valueList = result[n].valueList;
						psItem = result[n].psItem;
						data = [];

						// 관리대상별 값 변환
						for (var i = 0; i < valueList.length; i++) {

							valueList[i][0] = DateUtil.makeDate(
								valueList[i][0]).getTime();

							if (valueList[i][0] < minDtm) {
								minDtm = valueList[i][0];
							}

							data.push(parseFloat(valueList[i][1]
								.toFixed(3)));

						}

						// 관리대상의 값 추가
						series.push({
							name: result[n].mo.moName,
							data: data
						});

					}

					// 값의 시간 간격을 구한다.
					if (result.length >= 1 && result[0].valueList.length >= 2) {
						valueList = result[0].valueList;
						pointInterval = valueList[1][0] - valueList[0][0];
					}

					console.log(pointInterval);

					FxmsHighChart.drawSpline(div, title, subtitle, series, psItem, minDtm, pointInterval);

				} else {
					alert(result);
				}

			});

	}

	static drawSpline(div, title, subtitle, series, psItem, minDate, pointInterval) {

		// 로컬 시간 사용
		Highcharts.setOptions({
			global: {
				useUTC: false
			}
		});

		Highcharts
			.chart(
				div,
				{
					chart: {
						type: 'spline',
						scrollablePlotArea: {
							minWidth: 600,
							scrollPositionX: 1
						}
					},
					title: {
						text: title,
						align: 'left'
					},
					subtitle: {
						text: subtitle,
						align: 'left'
					},
					xAxis: {
						type: 'datetime',
						labels: {
							overflow: 'justify'
						}
					},
					yAxis: {
						title: {
							text: psItem.psUnit.dispText
						},
						minorGridLineWidth: 0,
						gridLineWidth: 0,
						alternateGridColor: null,
						plotBands: [
							{ // Light air
								from: 0.3,
								to: 1.5,
								color: 'rgba(68, 170, 213, 0.1)',
								label: {
									text: 'Light air',
									style: {
										color: '#606060'
									}
								}
							},
							{ // Light breeze
								from: 1.5,
								to: 3.3,
								color: 'rgba(0, 0, 0, 0)',
								label: {
									text: 'Light breeze',
									style: {
										color: '#606060'
									}
								}
							},
							{ // Gentle breeze
								from: 3.3,
								to: 5.5,
								color: 'rgba(68, 170, 213, 0.1)',
								label: {
									text: 'Gentle breeze',
									style: {
										color: '#606060'
									}
								}
							},
							{ // Moderate breeze
								from: 150,
								to: 200,
								color: 'rgba(0, 0, 0, 0)',
								label: {
									text: 'Moderate breeze',
									style: {
										color: '#606060'
									}
								}
							},
							{ // Fresh breeze
								from: 200,
								to: 300,
								color: 'rgba(68, 170, 213, 0.1)',
								label: {
									text: 'Fresh breeze',
									style: {
										color: '#606060'
									}
								}
							},
							{ // Strong breeze
								from: 300,
								to: 400,
								color: 'rgba(0, 0, 0, 0)',
								label: {
									text: 'Strong breeze',
									style: {
										color: '#606060'
									}
								}
							},
							{ // High wind
								from: 400,
								to: 450,
								color: 'rgba(68, 170, 213, 0.1)',
								label: {
									text: 'High wind',
									style: {
										color: '#606060'
									}
								}
							}]
					},
					tooltip: {
						valueSuffix: ' '
							+ psItem.psUnit.dispText
					},
					plotOptions: {
						spline: {
							lineWidth: 4,
							states: {
								hover: {
									lineWidth: 5
								}
							},
							marker: {
								enabled: false
							},
							pointInterval: pointInterval,
							pointStart: minDate
						}
					},

					// 데이터
					series: series,

					navigation: {
						menuItemStyle: {
							fontSize: '10px'
						}
					}
				});

	}

	drawChartAreaRange(div, title, moNo, psId) {

		let psItem = this.getPsItem(psId);

		this.fxms.call('ps/select-ps-value-min-max-avg-list', {
			moNo: moNo,
			psId: psId
		}, function(isOk, result) {

			if (isOk) {

				var averages = [];
				var ranges = [];
				var obj;

				for (var i = 0; i < result.length; i++) {
					obj = result[i];

					obj.psDate = DateUtil.makeDate(obj.psDate).getTime();

					ranges.push([obj.psDate, obj.minValue, obj.maxValue]);

					averages.push([obj.psDate, obj.avgValue]);
				}

				FxmsHighChart.darAreaRange(div, title, psItem, ranges, averages);

			} else {
				alert(result);
			}

		});
	}

	static darAreaRange(div, title, psItem, ranges, averages) {

		Highcharts.setOptions({
			global: {
				useUTC: false
			}
		});

		Highcharts
			.chart(
				div,
				{

					title: {
						text: title
					},

					xAxis: {
						type: 'datetime',
						accessibility: {
							rangeDescription: 'Range: Jul 1st 2009 to Jul 31st 2009.'
						}
					},

					yAxis: {
						title: {
							text: null
						}
					},

					tooltip: {
						crosshairs: true,
						shared: true,
						valueSuffix: ' ' + psItem.psUnit
					},

					series: [
						{
							name: psItem.psName,
							data: averages,
							zIndex: 1,
							marker: {
								fillColor: 'white',
								lineWidth: 2,
								lineColor: Highcharts
									.getOptions().colors[0]
							}
						},
						{
							name: 'Range',
							data: ranges,
							type: 'arearange',
							lineWidth: 0,
							linkedTo: ':previous',
							color: Highcharts
								.getOptions().colors[0],
							fillOpacity: 0.3,
							zIndex: 0,
							marker: {
								enabled: false
							}
						}]
				});

	}


	drawChartSankey(div, title, psId, psDataCd, statFunc, startDtm, endDtm) {

		this.fxms.call('ps/select-ps-value-list', {
			moNo: 0,
			psId: psId,
			psDataCd: psDataCd,
			psStatFuncList: statFunc,
			startPsDate: startDtm,
			endPsDate: endDtm
		},
			function(isOk, result) {

				console.log(result);

				if (isOk) {

					var valueList;
					var datas = [];
					var data;

					for (var n = 0; n < result.length; n++) {

						valueList = result[n].valueList;
						data = [];
						data.push('전력');
						data.push(result[n].mo.moName);
						var sumVal = 0;

						for (var i = 0; i < valueList.length; i++) {
							sumVal += parseFloat(valueList[i][1].toFixed(1));
						}
						data.push(sumVal);
						datas.push(data);


						for (var i = 0; i < valueList.length; i++) {
							data = [];
							data.push(result[n].mo.moName);
							data.push(DateUtil.getDdHh(DateUtil.makeDate(valueList[i][0]).getTime()));
							data.push(parseFloat(valueList[i][1].toFixed(1)));
							datas.push(data);
						}
					}

					FxmsHighChart.drawSankey(div, title, datas);

				} else {
					alert(result);
				}

			});

	}

	drawChartStream(div, title, subtitle, psId, psDataCd, statFunc, startDtm, endDtm) {

		this.fxms.call('ps/select-ps-value-list', {
			moNo: 0,
			psId: psId,
			psDataCd: psDataCd,
			psStatFuncList: statFunc,
			startPsDate: startDtm,
			endPsDate: endDtm
		},
			function(isOk, result) {

				console.log(result);

				if (isOk) {

					if (isOk) {

						var data;
						var series = [];
						var categories = [];
						var valueList;
						var minDtm = 99999999000000;

						for (var n = 0; n < result.length; n++) {
							valueList = result[n].valueList;

							data = [];
							for (var i = 0; i < valueList.length; i++) {

								valueList[i][0] = DateUtil.makeDate(
									valueList[i][0]).getTime();

								if (valueList[i][0] < minDtm) {
									minDtm = valueList[i][0];
								}

								if (n == 0) {
									categories.push(DateUtil.getDdHh(
										valueList[i][0], '일 '));
								}

								data.push(parseFloat(valueList[i][1]
									.toFixed(3)));

							}

							series.push({
								name: result[n].mo.moName,
								data: data
							});

						}

						FxmsHighChart.drawStream(div, title, subtitle, series, categories);

					} else {
						alert(result);
					}

				} else {
					alert(result);
				}

			});

	}

	static drawStream(div, title, subtitle, series, categories) {

		// 로컬 시간 사용
		Highcharts.setOptions({
			global: {
				useUTC: false
			}
		});

		var colors = Highcharts.getOptions().colors;
		Highcharts
			.chart(
				div,
				{

					chart: {
						type: 'streamgraph',
						marginBottom: 30,
						zoomType: 'x'
					},

					// Make sure connected countries have similar colors
					colors: [
						colors[0],
						colors[1],
						colors[2],
						colors[3],
						colors[4],
						// East Germany, West Germany and Germany
						Highcharts.color(
							colors[5])
							.brighten(0.2)
							.get(),
						Highcharts.color(
							colors[5])
							.brighten(0.1)
							.get(),

						colors[5],
						colors[6],
						colors[7],
						colors[8],
						colors[9],
						colors[0],
						colors[1],
						colors[3],
						// Soviet Union, Russia
						Highcharts.color(
							colors[2])
							.brighten(-0.1)
							.get(),
						Highcharts.color(
							colors[2])
							.brighten(-0.2)
							.get(),
						Highcharts.color(
							colors[2])
							.brighten(-0.3)
							.get()],

					title: {
						floating: true,
						align: 'left',
						text: title
					},
					subtitle: {
						floating: true,
						align: 'left',
						y: 30,
						text: subtitle
					},

					xAxis: {
						maxPadding: 0,
						type: 'category',
						crosshair: true,
						categories: categories, // x축 라벨
						labels: {
							align: 'left',
							reserveSpace: false,
							rotation: 270
						},
						lineWidth: 0,
						margin: 20,
						tickWidth: 0
					},

					yAxis: {
						visible: false,
						startOnTick: false,
						endOnTick: false
					},

					legend: {
						enabled: false
					},

					plotOptions: {
						series: {
							label: {
								minFontSize: 5,
								maxFontSize: 15,
								style: {
									color: 'rgba(255,255,255,0.75)'
								}
							},
							accessibility: {
								exposeAsGroupOnly: true
							}
						}
					},

					// Data parsed with olympic-medals.node.js
					series: series,

					exporting: {
						sourceWidth: 800,
						sourceHeight: 600
					}

				});
	}

	static drawSankey(div, title, datas) {

		// 로컬 시간 사용
		Highcharts.setOptions({
			global: {
				useUTC: false
			}
		});

		Highcharts
			.chart(
				div,
				{

					title: {
						text: title
					},
					// colors : [ 'red', 'blue', 'green'],
					accessibility: {
						point: {
							valueDescriptionFormat: '{index}. {point.from} to {point.to}, {point.weight}.'
						}
					},
					series: [{
						keys: ['from', 'to', 'weight'],
						data: datas,
						type: 'sankey',
						name: title
					}]

				});
	}
}

class FxmsComponent {

	/**
	Component 처리가 완료되면 callback을 호출한다.
	 */
	constructor(fxms, callback) {

		this.fxms = fxms;

		this.receiver = new AlarmReceiver(fxms, 'SOIL', '1111')
		this.receiver.open(fxms);

		callback();

	}

	drawAlarmList(div) {

		var THIS = this;

		function showAlarm() {

			let txtRecv = document.getElementById(div);
			txtRecv.innerHTML = '';


			let alarmList = THIS.receiver.getAlarmAllList();
			var a;
			var p = '';
			var cls;
			for (var i = 0; i < alarmList.length; i++) {

				a = alarmList[i];
				cls = AlarmColor.getColor(a.alarmLevel);


				p += "<p style='background-color:" + cls + "'>"
					+ '  *** '
					+ a.moNo + ',' + a.moName + ' ::: ' + a.alarmNo
					+ ', ' + a.alarmLevel + ', ' + a.alarmMsg
					+ ', ' + a.occurDtm + ', ' + a.rlseDtm + "</p>"

			}

			txtRecv.innerHTML = p;
		}

		showAlarm();

		this.receiver.addCallback(function(recvObj, e) {
			if (recvObj != undefined && recvObj.eventType == 'Alarm') {
				showAlarm();
			}
		});
	}

	/**
	관리대상 알람 상태를 보여준다.
	 */
	drawMoAlarmList(div, moClass, inloNo) {

		function getMoId(moNo) {
			return div + '_mo_' + moNo;
		}

		function addMo(mo) {

			var style = 'mo-state-button';
			style += ' alarm-level-' + AlarmLevel.getName(mo.MO_ALARM_ST_VAL)

			var moInfo = '<button id="' + getMoId(mo.MO_NO) + '" class="' + style
				+ '" onclick="myFunction(\'' + mo.MO_NAME + '\')">'
				+ mo.MO_NAME + '</button>';

			let pList = document.getElementById(div);
			pList.innerHTML += moInfo;

		}

		function applyAlarmLevel(moNo, alarmLevel) {
			let moDiv = document.getElementById(getMoId(moNo));

			if (moDiv == null) {
				return;
			}

			for (let i = 0; i < moDiv.classList.length; i++) {
				if (moDiv.classList[i].startsWith('alarm-level')) {
					moDiv.classList.remove(moDiv.classList[i]);
				}
			}

			moDiv.classList.add('alarm-level-' + AlarmLevel.getName(alarmLevel));
		}

		this.fxms.call('mo/select-mo-alarm-state-list', {
			moClass: moClass,
			inloNo: inloNo
		}, function(isOk, result) {
			if (isOk) {
				for (var i = 0; i < result.length; i++) {
					addMo(result[i]);
				}
			}
		});

		var THIS = this;
		this.receiver.addCallback(function(recvObj, e) {
			if (recvObj != undefined && recvObj.eventType == 'Alarm') {
				// 알람의 상태를 반영한다.
				applyAlarmLevel(recvObj.moNo, THIS.receiver.getAlarmLevel(recvObj.moNo));
			}
		});

	}

	/**
	관리대상 알람 상태를 보여준다.
	 */
	drawMoOnlineList(div, moClass, inloNo) {

		function getMoId(moNo) {
			return div + '_mo_' + moNo;
		}


		function addMo(mo) {

			var style = 'mo-state-button';

			if (mo.MO_ONLINE_ST_VAL == 1) {
				style += ' mo-state-online';
			} else {
				style += ' mo-state-offline';
			}

			var moInfo = '<button id="' + getMoId(mo.MO_NO) + '" class="' + style + '" onclick="myFunction(\''
				+ mo.MO_NAME + '\')">' + mo.MO_NAME + '</button>';

			let pList = document.getElementById(div);
			pList.innerHTML += moInfo;
		}

		this.fxms.call('mo/select-mo-online-state-list', {
			moClass: moClass,
			inloNo: inloNo
		}, function(isOk, result) {
			if (isOk) {
				for (var i = 0; i < result.length; i++) {
					addMo(result[i]);
				}
			}
		});

		this.receiver.addCallback(function(recvObj, e) {
			if (recvObj != undefined && recvObj.eventType == 'MoOnlineState') {
				let moDiv = document.getElementById(getMoId(recvObj.mo.moNo));

				moDiv.classList.remove("mo-state-offline");
				moDiv.classList.remove("mo-state-online");
				// moDiv.classList.add('mo-state-button');

				if (recvObj.moStatus == 'Offline')
					moDiv.classList.add('mo-state-offline');
				else
					moDiv.classList.add('mo-state-online');
				// console.log(recvObj, moDiv);
			}
		});

	}

	/**
	 * 다아이그램 그리기
	 */
	drawDiagram(div, diagNo) {
		new FxmsDiagram(this.fxms).showDiagram(div, diagNo);
	}

}


class FxmsSelectBoxComponent {

	/**
	Component 처리가 완료되면 callback을 호출한다.
	 */
	constructor(fxms) {

		this.fxms = fxms;

	}

	makeCode(id, searchPara, callback, prompt) {
		this.makeBox(id, 'code/select-code-list', searchPara, {
			value: 'cdCode',
			text: 'cdName'
		}, callback, prompt);
	}

	makePipe(id, searchPara, callback, prompt) {
		this.makeBox(id, 'vup/select-pipe-list', searchPara, {
			value: 'pipeId',
			text: 'pipeName'
		}, callback, prompt);
	}


	makeBox(id, url, searchPara, boxPara, callback, prompt) {

		// id에 해당되는 url 가져오기

		this.fxms.call(url, searchPara, function(isOk, result) {
			if (isOk) {
				let data;
				let html = '';

				if (prompt != undefined || prompt != null) {
					html = '<option value="" >' + prompt + '</option>';
				}

				let map = new Map();
				for (var i = 0; i < result.length; i++) {
					data = result[i];
					html += '<option value="' + data[boxPara.value] + '">' + data[boxPara.text] + '</option>';
					//<option value="#cd5c5c" selected>Indian-Red</option>
					//<option value="#ff0000" disabled>Red</option>
					//<option value="#8b0000">Dark-Red</option>
					map.set(data[boxPara.value], data);
				}

				let element = document.getElementById(id);
				element.innerHTML = html;

				// change event를 받을 callback이 정의되어 있으면 이벤트를 등록한다.
				if (callback) {

					console.log(element);

					// element.removeEventListener("change");

					element.addEventListener("change", function(e) {
						let value = e.target.value;
						if (value.length != 0) {
							// 선택된 ID와 그 객체를 보낸다.
							callback(value, map.get(value));
						}
					});

				}
			}
		});

	}
}
