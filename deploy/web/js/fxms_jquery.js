class jQGrid {

	constructor(result, columns) {

		this.columns = [];
		this.datas = result;


		if (result == undefined || result.length <= 0) {
			return;
		}

		if (columns != undefined) {
			for (var i = 0; i < columns.length; i++) {
				this.addColumn(this.makeColumn(columns[i]));
			}
		} else {
			var keys = Object.keys(result[0]);
			for (var i = 0; i < keys.length; i++) {
				this.addColumn(this.makeColumn(keys[i]));
			}
		}

		console.log(this.columns, result);

	}

	/**
	* 0 : R|L|C
	* 1 : S,N,D,...
	* 2~4 : width
	 */
	makeColumn(str) {

		var THIS = this;
		var col = {};
		var ss = str.split(':');

		if (ss.length > 1) {


			let align = ss[0].substring(0, 1);
			let type = ss[0].substring(1, 2);
			let width = ss[0].substring(2, 5);

			console.log(ss, align, type, width);

			col.name = ss[1];
			col.label = ss.length > 1 ? ss[2] : ss[1];
			col.width = parseInt(width) * 10;
			col.align = align == 'R' ? 'right' : align == 'C' ? 'center' : 'left';
			col.template = type == 'N' ? 'number' : type == 'D' ? 'date' : 'string';

			if (type == 'D') {
				col.sorttype = 'date';
				col.formatter = function(s) {
					return THIS.formatDate(s);
				}
			}
			// col.template = 'number'; // booleanCheckbox

			return col;

		} else {

			return { name: str };

		}

	}

	formatDate(date) {

		var s = date.toString();

		if (s.length >= 8) {
			return s.substring(0, 4) + '-' + s.substring(4, 6) + '-' + s.substring(6, 8);
		}
		return s;
	}

	addColumn(col) {
		this.columns.push(col);
	}

	setDatas(datas) {
		this.datas = datas;
	}

	draw(tableId) {

		$("#" + tableId).html('');

		$("#" + tableId).jqGrid(
			{
				colModel: this.columns,
				data: this.datas,
				iconSet: "fontAwesome",
				idPrefix: "g1_",
				rownumbers: true,
				sortname: "invdate",
				sortorder: "desc",
				caption: "FxMS TEST"
			});

	}
}
