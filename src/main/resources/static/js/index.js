let socketPort = "9291";
var deviceLineNum = 0,futureLineNum = 0,originalLineNum = 0;
var barCharts = echarts.init(document.getElementById('barChartsId'));
// 基于准备好的dom，初始化echarts实例
$(document).ready(function(){
    window.onresize = function () {
        barCharts.resize();
    }
    flushRunStatus("statusInterChangerId","NORMAL");
    flushStatusLine("lineInterChangerId","NORMAL");
    flushRunStatus("statusFilterId","NORMAL");
    flushStatusLine("lineFilterId","NORMAL");
    flushRunStatus("statusApi2","NORMAL");
    initOpenSocket();
    initData();
});

function initOpenSocket() {
    let socketPortTemp = $("input[name='socketPort']").val();
    if (socketPortTemp) socketPort = socketPortTemp;
    //判断当前浏览器是否支持WebSocket
    var websocket;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://127.0.0.1:" + socketPort + "/websocket");
    } else {
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        flushNetworkStatus("statusSocketId",'');
        websocket = new WebSocket("ws://127.0.0.1:" + socketPort + "/websocket");
    };

    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        //连接成功
        flushNetworkStatus("statusSocketId",'NORMAL');
    }


    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let data = JSON.parse(event.data);
        let dataType = data.dataType;
        let dataList = data.dataList;
        let status;
        switch (dataType) {
            case 'FEATURE_DATA':
                let futureHtml = [];
                if(dataList && dataList.length){
                    for (let i = 0; i < dataList.length; i++) {
                        let deviceCode = dataList[i]['deviceCode'];
                        let time = dataList[i]['time'];
                        let sourceTypeName = dataList[i]['sourceTypeName'];
                        let statusName = dataList[i]['statusName'];
                        let context = dataList[i]['context'];
                        futureHtml .push(`\
                <div id='futureLineNum${++futureLineNum}' class='log-row'> 
                    <div class='log-unit log-device'>${deviceCode}</div>
                    <div class='log-unit log-time'>${time}</div>
                    <div class='log-unit log-source-type'>${sourceTypeName}</div>
                    <div class='log-unit log-status'>${statusName}</div>
                    <div class='log-unit log-context'>${context}</div> 
                </div>`);
                    }
                }
                futureHtml.reverse();
                $("#infoBodyFuture").prepend(futureHtml.join(""));
                removeEles("futureLineNum",futureLineNum,50);
                $("#infoBodyFuture").scrollTop = 0;
                break;
            case 'ORIGINAL_DATA':
                let originalHtml = [];
                if(dataList && dataList.length){
                    for (let i = 0; i < dataList.length; i++) {
                        let deviceCode = dataList[i]['deviceCode'];
                        let time = dataList[i]['time'];
                        let sourceTypeName = dataList[i]['sourceTypeName'];
                        let sensorCode = dataList[i]['sensorCode'];
                        let dataType = dataList[i]['dataType'];
                        let statusName = dataList[i]['statusName'];
                        let contextLength = dataList[i]['contextLength'];
                        originalHtml .push(`
                <div id='originalLineNum${++originalLineNum}' class='log-row'> 
                    <div class='log-unit log-device'>${deviceCode}</div>
                    <div class='log-unit log-time'>${time}</div>
                    <div class='log-unit log-source-type'>${sourceTypeName}</div>
                    <div class='log-unit log-sensor'>${sensorCode}</div>
                    <div class='log-unit log-data-type'>${dataType}</div>
                    <div class='log-unit log-status'>${statusName}</div>
                    <div class='log-unit log-context'>length: ${contextLength}</div> 
                </div>`);
                    }
                }
                originalHtml.reverse();
                $("#infoBodyWave").prepend(originalHtml.join(""));
                removeEles("originalLineNum",originalLineNum,50);
                $("#infoBodyWave").scrollTop = 0;
                break;
            case 'DEVICE_DATA':
                let deviceHtml = [];
                if(dataList && dataList.length){
                    for (let i = 0; i < dataList.length; i++) {
                        let deviceCode = dataList[i]['deviceCode'];
                        let time = dataList[i]['time'];
                        let sourceTypeName = dataList[i]['sourceTypeName'];
                        let statusName = dataList[i]['statusName'];
                        let context = dataList[i]['context'];
                        deviceHtml.push(`
                        <div id='deviceLineNum${++deviceLineNum}' class='log-row'> 
                            <div class='log-unit log-device'>${deviceCode}</div>
                            <div class='log-unit log-time'>${time}</div>
                            <div class='log-unit log-source-type'>${sourceTypeName}</div>
                            <div class='log-unit log-status'>${statusName}</div>
                            <div class='log-unit log-context'>${context}</div> 
                        </div>`);
                    }
                }
                deviceHtml.reverse();
                $("#infoBodyDevice").prepend(deviceHtml.join(""));
                removeEles("deviceLineNum",deviceLineNum,100);
                $("#infoBodyDevice").scrollTop = 0;
                break;
            case 'RUN_STATUS_FILE_SEARCH':
                //运行状态-文件系统-特征文件
                status = dataList[0].status;
                flushRunStatus("statusApi1",status);
                break;
            case 'RUN_STATUS_TERMINAL_SERVER':
                //运行状态-TCP终端服务
                status = dataList[0].status;
                flushRunStatus("statusApi3",status);
                break;
            case 'RUN_STATUS_NETWORK_SWITCHES':
                //运行状态-上位机服务
                status = dataList[0].status;
                flushNetworkStatus("statusNetworkSwitchesId",status);
                flushStatusLine("lineNetworkSwitchesId",status);
                break;
            case 'DISK':
                //磁盘空间
                if(dataList && dataList.length) {
                    flushChart(dataList);
                }
                break;
            default:
        }
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        flushNetworkStatus("statusSocketId",'STOP');
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }

}

function initData(){
    $.get("/system/systemParams",function(res){
        if(res.status == 200){
            let data = res.data;
            let filtrateMaxRow = data["filtrateMaxRow"];
            let filtrateMinInterval = data["filtrateMinInterval"];
            let filtrateMaxDataLength = data["filtrateMaxDataLength"];
            let diskAllowSurplusSpace = data["diskAllowSurplusSpace"];
            let dataLogAllowSaveDays = data["dataLogAllowSaveDays"];

            let originalFiltrateMinInterval = data["originalFiltrateMinInterval"];
            let originalFiltrateMaxDataLength = data["originalFiltrateMaxDataLength"];

            $("#filtrateMaxRowId").text(filtrateMaxRow);
            $("#filtrateMinIntervalId").text(filtrateMinInterval);
            $("#filtrateMaxDataLengthId").text(filtrateMaxDataLength);

            $("#originalFiltrateMinIntervalId").text(originalFiltrateMinInterval);
            $("#originalFiltrateMaxDataLengthId").text(originalFiltrateMaxDataLength);

            $("#dataLogAllowSaveDaysId").text(dataLogAllowSaveDays);
        }
    });
    //获取系统磁盘状态
    $.get('/system/disk/info', function (res) {
        if (res.status == 200) {
            let list = res.data;
            flushChart(list);
        }
    });
}

function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');
}
function getShortLocalTime(nS) {
    let date = new Date(parseInt(nS))
    return date.getMonth()+'/'+date.getDay()+' '+date.getMonth();
}

function flushChart(list){
    /** 磁盘名称 diskName;
     磁盘总字节数 totalByte;
     磁盘已使用字节数 usedByte;
     已使用万分率 usedMillion;
     设置空闲万分率 setFreeMillion;*/
    let barXAxisData = [];
    let barSeriesUsedData = [];
    let barSeriesFreeData = [];
    let setFreeMillion = 5000;
    if(list){
        let length = list.length;
        for (let i = 0; i < length; i++) {
            let data = list[i];
            let diskName = data['diskName'];
            let usedMillion = data['usedMillion'];
            setFreeMillion = data['setFreeMillion'];
            barXAxisData.push(diskName);
            barSeriesUsedData.push(usedMillion);
            barSeriesFreeData.push(10000-usedMillion);
        }
    }

    let barOption = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter: function (params) {
                var used = params[0];
                return used.name + '<br/>' + used.seriesName + ' : ' + used.value+' ‱';
            }
        },
        grid:{
            x:0,
            y:0,
            x2:0,
            y2:30
        },
        xAxis: {
            axisLine:{show:false},
            type: 'category',
            data: barXAxisData,
            axisLabel:{
                interval: 1,
                hideOverlap: true,
            }
        },
        yAxis: {
            show:false,
        },
        label: {
            show: false,
        },
        series: [{
            name: '已使用',
            type: 'bar',
            barMaxWidth: '20',
            stack: 'disk',
            emphasis: {
                focus: 'series'
            },
            itemStyle:{color:'#D7D7D7'},
            data: barSeriesUsedData,
            markLine: {
                label:{
                    show: true,
                    position: 'middle',
                    color: 'orange',
                    formatter: (10000-setFreeMillion) +' ‱',
                },
                data: [{
                    yAxis: 10000-setFreeMillion
                }],
                lineStyle:{
                  color: 'orange',
                },
                silent: true
            }
        },
        {
            name: '剩余',
            type: 'bar',
            barMaxWidth: '20',
            stack: 'disk',
            emphasis: {
                focus: 'series'
            },
            itemStyle:{color:'#1A7DD7'},
            data: barSeriesFreeData
        }]
    };
    barCharts.setOption(barOption);
}

/**
 * 切换日志
 * @param type 1特征数据 2波形数据 3设备
 */
function choiceLogTab(type){
    $(".info-record .info-tabs>ul>li").attr("class","none");
    $(".info-body>div").attr("class","none");
    switch (type) {
        case 1:
            $("#infoTabFuture").attr("class","active");
            $("#infoBodyFuture").attr("class","active");
            break;
        case 2:
            $("#infoTabWave").attr("class","active");
            $("#infoBodyWave").attr("class","active");
            break;
        case 3:
            $("#infoTabDevice").attr("class","active");
            $("#infoBodyDevice").attr("class","active");
            break;
    }
}
/**
 * 刷新网络状态
 * @param id
 * @param status
 */
function flushNetworkStatus(id,status) {
    let _message;
    let _class;
    switch (status) {
        case 'NORMAL':
            _message = '网络正常';
            _class = 'normal';
            break;
        case 'STOP':
            _message = '网络断开';
            _class = 'stop';
            break;
        case 'ABNORMAL':
            _message = '网络异常';
            _class = 'abnormal';
            break;
        default:
            _message = '正在连接';
            _class = 'starting';
    }
    $("#"+id).attr('class', 'info-status-value');
    $("#"+id).addClass('status-'+_class);
    $("#"+id).text(_message);
}
/**
 * 刷新网络状态
 * @param id
 * @param status
 */
function flushStatusLine(id,status) {
    let _color;
    switch (status) {
        case 'NORMAL':
            _color = '#00A94F';
            break;
        case 'STOP':
            _color = 'grey';
            break;
        case 'ABNORMAL':
            _color = 'orange';
            break;
        default:
            _color = '#cec066';
    }
    $(`#${id}>span`).css('color',_color);
    $(`#${id} .direction-line`).css('backgroundColor',_color);
    if(_color == '#00A94F'||_color == '#cec066'){
        //正常或开启中，开启动画
        $(`#${id} .direction-line .sliding`).css('display','inline');
        $(`#${id} .direction-line .sliding`).css('animation-duration','3s');
    }else{
        $(`#${id} .direction-line .sliding`).css('display','none');
        $(`#${id} .direction-line .sliding`).css('animation-duration','0s');
    }
}

/**
 * 刷新运行状态
 * @param id
 * @param status
 */
function flushRunStatus(id,status) {
    let _message;
    let _class;
    switch (status) {
        case 'NORMAL':
            _message = '传输正常';
            _class = 'normal';
            break;
        case 'STOP':
            _message = '已停止';
            _class = 'stop';
            break;
        case 'ABNORMAL':
            _message = '传输异常';
            _class = 'abnormal';
            break;
        default:
            _message = '正在启动';
            _class = 'starting';
    }
    $("#"+id).attr('class', 'info-status-value');
    $("#"+id).addClass('status-'+_class);
    $("#"+id).text(_message);
}

function removeEles(id,currentNum,maxNum) {
    if ( currentNum > maxNum) {
        let i = (currentNum - maxNum - 10);
        if (i < 0) i = 0
        for (; i < (currentNum - maxNum); i++) {
            $(`#${id}${i}`).remove();
        }
    }
}