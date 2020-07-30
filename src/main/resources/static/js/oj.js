function verifyemail(str) {
    var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
    if (reg.test(str)) {
        return true;
    } else {
        return false;
    }
}