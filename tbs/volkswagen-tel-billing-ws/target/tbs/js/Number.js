/*
 * To fix the number toFixed function issue
 */
Number.prototype.toFixed = function (d) {
    var s = this + "";
    if (!d) d = 0;
    if (s.indexOf(".") == -1) s += ".";
    s += new Array(d + 1).join("0");
    if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0," + (d + 1) + "})?)\\d*$").test(s)) {
        var s = "0" + RegExp.$2, pm = RegExp.$1, a = RegExp.$3.length, b = true;
        if (a == d + 2) {
            a = s.match(/\d/g);
            if (parseInt(a[a.length - 1]) > 4) {
                for (var i = a.length - 2; i >= 0; i--) {
                    a[i] = parseInt(a[i]) + 1;
                    if (a[i] == 10) {
                        a[i] = 0;
                        b = i != 1;
                    } else break;
                }
            }
            s = a.join("").replace(new RegExp("(\\d+)(\\d{" + d + "})\\d$"), "$1.$2");

        } if (b) s = s.substr(1);
        return (pm + s).replace(/\.$/, "");
    } return this + "";

};

Number.prototype.format = function (fmt) {

    var s = this.toFixed(2).toString();

    var decimals = "";
    if (s.indexOf(".") > 0) {
        var arr = s.split('.');
        s = arr[0]; decimals = arr[1];
    }

    if (this > 0 && this < 1000) {
        if (decimals != "" && parseInt(decimals) != 0) {
            return s + "." + decimals;
        }
        return s;
    }

    while (s.length % 3 != 0) {
        s = '0' + s;
    }

    var str = [], _s = s.split('');
    k = 0;
    for (var i = 0; i < _s.length; i++) {
        var a = _s[i];
        k++;
        str.push(a);
        if (k % 3 == 0) {
            str.push(",");
            k = 0;
        }
    }

    str.length = str.length - 1;

    var _formatted = str.join('').replace(/^0+/, '');
    if (decimals.length > 0
    			&& parseInt(decimals) != 0) {
        _formatted += '.' + decimals;
    }

    return _formatted.length == 0 ? '0' : _formatted;

};